/*
Copyright 2008 Endress+Hauser Infoserve GmbH&Co KG
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
implied. See the License for the specific language governing permissions
and limitations under the License.
*/
package org.webguitoolkit.ui.controls.tree;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenu;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.http.resourceprocessor.JSResourceProcessor;

/**
 * <pre>
 * displaying a tree This controls assumes, that you have includeed the tree
 * js-libraries in your html.
 * 
 * + Root
 *   + Child1
 *   | + Child11
 *   | + Child12
 *   | + Child13
 *   + Child2
 *     + Child21
 * 
 * </pre>
 * 
 * @author martin hermann
 * 
 */
public class Tree extends BaseControl implements ITree {

	// definition of the tree
	public static final String TYPE_TREE_VALUE = "jtrv";
	public static final String TYPE_TREE_INIT = "jtin";
	public static final String TYPE_TREE = "jtre";
	public static final String TYPE_TREE_LOAD_LAZY = "jtll";
	public static final String TYPE_TREE_NODE_UPDATE = "jtnu";
	public static final String TYPE_TREE_RELOAD_CHILDREN = "jtrc";
	public static final String TYPE_TREE_SELECTION = "jtse";
	public static final String TYPE_TREE_SILENT_SELECTION = "jtss";
	public static final String TYPE_TREE_NODE_EXPAND = "jtne";
	public static final String TYPE_TREE_NODE_SILENT_EXPAND = "jtnse";
	public static final String TYPE_TREE_COLLAPSE = "jtnc";

	public static final String EVENT_LOAD_LAZY = "node_load_lazy";
	public static final String EVENT_NODE_ONCLOSE = "node_onclose";

	private boolean isInit;
	private String treeWidth;
	private String treeHeight;
	private ITreeModel model;
	private Hashtable<String, IContextMenu> contextMenus;
	private boolean fireOnCheckEvent;
	private int clickFolderMode = 3;

	/**
	 * constructs a new tree
	 */
	public Tree() {
		super();
	}

	/**
	 * constructs a new tree with the given id
	 * 
	 * @param id the id of the tree
	 */
	public Tree(String id) {
		super(id);
	}

	/**
	 * the HTML generation of a tree consists only of the surrounding div-tag and the empty tree definition. The content of the
	 * tree will be loaded through the InitEvent.
	 * 
	 * @param out the writer for HTML generation
	 */
	@Override
	protected void endHTML(PrintWriter out) {
		out.println("<div ");
		stdParameter(out);
		out.println(">");
		out.println("</div>");
	}

	/**
	 * initialize method called by the framework
	 */
	@Override
	protected void init() {

		isInit = false;

		// add context menu to tree for reuse. 
		// If many nodes have the same context menu it is only rendered once
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			BaseControl child = (BaseControl)it.next();
			if (child instanceof IContextMenu) {
				contextMenus.put(child.getId(), (IContextMenu)child);
			}
		}

		Page page = getPage();
		page.addControllerJS(JSResourceProcessor.TREE_JS);
	}

	/**
	 * loads the tree from the model and sends the whole new tree to the client. (can't send parts of the tree)
	 */
	public void load() {

		StringBuffer buff = new StringBuffer();

		ITreeNode root = getModel().getRootElement();
		if (root == null)
			return;
		getModel().clearLoaded();
		//
		// buff.append(" children: ");
		if (getModel().isRootVisible()) {
			List nodes = new ArrayList();
			nodes.add(root);
			serializeXML(buff, nodes);
		}
		else {
			serializeXML(buff, getModel().getChildren(root));
		}
		// buff.append( "});" );

		if (!isInit) {
			String treeParams = "{ width: '" + treeWidth + "', height: '" + treeHeight + "', imagePath: './images/wgt/tree/', checkboxes: "
					+ getModel().hasCheckBoxes() + ", checkEvent: " + getFireOnCheckEvent() + ", clickFolderMode: " + clickFolderMode
					+ " }";
			getContext().add(getId(), treeParams, TYPE_TREE_INIT, IContext.STATUS_NOT_EDITABLE);
			isInit = true;
		}
		//
		getContext().add(getId(), buff.toString(), TYPE_TREE, IContext.STATUS_NOT_EDITABLE);
		getContext().add(id4Value(), "", TYPE_TREE_VALUE, IContext.STATUS_EDITABLE);
		getContext().add(id4selected(), "", TYPE_TREE_SELECTION, IContext.STATUS_EDITABLE);
		getContext().moveDown(getId() + ".drag");
		getContext().moveDown(getId() + ".drop");
		for (Object contextMenuId : getContextMenus().keySet())
			getContext().moveDown((String)contextMenuId);
	}

	/**
	 * serialize this tree into treeDefinition needed by the tree recursively
	 * 
	 * @param treeDefBuffer the StringBuffer to write to
	 * @param node the node that has to be serialized
	 */
	public void serializeXML(StringBuffer treeDefBuffer, ITreeNode node) {
		serializeXML(treeDefBuffer, node, null);
	}

	/**
	 * 
	 * @param treeDefBuffer the StringBuffer to write to
	 * @param node the node that has to be serialized
	 * @param childrenToLoad children that has to be loaded
	 */
	public void serializeXML(StringBuffer treeDefBuffer, ITreeNode node, List childrenToLoad) {

		String nodeString = "{";
		// what is the id of this...
		String id = node.getId();
		if (id == null) {
			Logger.getLogger(this.getClass()).error("treeNode with no id found!");
		}

		String contextMenuId = node.getContextMenuId();
		if (contextMenuId != null)
			((ContextMenu)getContextMenus().get(contextMenuId)).bindToJQTreeNode(id);

		// add the caption ------------------
		nodeString += " title: \"" + StringEscapeUtils.escapeJavaScript(node.getCaption()) + "\",";
		// add the id -----
		nodeString += " key: \"" + id + "\",";

		if (node.isDraggable()) {
			getPage().addControllerJS(JSResourceProcessor.DRAG_DROP_JS);
			nodeString += " draggable: { ";
			nodeString += "  helper: function(ev, ui) { ";
			nodeString += "      var helperObj = jQuery('#dragObject').clone().attr( 'id', 'dragObject_clone' ).show();";
			nodeString += " 		return helperObj;";
			nodeString += "  },";
			nodeString += " 	cursor: 'move',";
			nodeString += " 	cursorAt: {top:5, left:5},";
			nodeString += " 	appendTo: 'body',";
			nodeString += " 	zIndex: 20,";
			nodeString += " 	start: function(ev, ui) {";
			nodeString += " 		eventParam( '" + getId() + "', new Array( '" + id + "' ) );";
			nodeString += "     	fireWGTEvent( '" + getId() + "', 'node_dragged' );";
			nodeString += "  }";
			nodeString += " },";
		}
		if (node.isDroppable()) {
			getPage().addControllerJS(JSResourceProcessor.DRAG_DROP_JS);
			nodeString += " droppable: {";
			//nodeString += "	accept: 'a',";
			nodeString += "	hoverClass: 'wgtDroppableHover',";
			nodeString += "	drop: function(ev, ui) {";
			nodeString += "		eventParam( '" + getId() + "', new Array( '" + id + "', ui.draggable.parent().get(0).id ) );";
			nodeString += "		fireWGTEvent( '" + getId() + "', 'node_dropped' );";
			nodeString += "	}";
			nodeString += " },";
		}

		// // style of the tree node text
		// if( StringUtils.isNotEmpty( node.getStyle() ) ){
		// treeDefBuffer.append(" style=\"" + StringEscapeUtils.escapeXml(node.getStyle()) + "\"");
		// }

		// format ICONS
		if (node.getIconSrc() != null) {
			String[] images = node.getIconSrc();
			if (images.length < 1) {
				throw new WGTException("getIconSrc must return String[3]");
			}
			nodeString += " icon: \"" + images[0] + "\",";
		}

		if (StringUtils.isNotEmpty(node.getTooltip())) {
			nodeString += " tooltip: \"" + node.getTooltip() + "\",";
		}

		// check to change the lastslected
		if (node.isSelected())
			nodeString += " activate: true,";

		if (model.hasCheckBoxes()) {
			if (node.hasCheckBox()) {
				if (node.isChecked()){
					nodeString += " select: true,";

					// add node id to context
					String checkedNodes = getContext().getValue(id4Value());
					if( checkedNodes == null )
						checkedNodes = "";
					if( checkedNodes.equals("") )
						checkedNodes += node.getId();
					else
						checkedNodes += ","+node.getId();

					// set new value to context
					getContext().add(id4Value(), checkedNodes, TYPE_TREE_VALUE, IContext.STATUS_EDITABLE);
				}
			}
			else
				nodeString += " hideCheckbox: true,";
		}

		// node serialize the children recusively
		if (getModel().hasChildren(node)) {
			treeDefBuffer.append(nodeString);
			treeDefBuffer.append(" isFolder: true,");
			// format EXPANDED
			if (node.isExpanded()) {
				treeDefBuffer.append(" expand: true, children: ");
				serializeXML(treeDefBuffer, getModel().getChildren(node), childrenToLoad);
			}
			else if (getModel().isLoadLazy() && !containsNode(childrenToLoad, node)) {
				treeDefBuffer.append(" isLazy: true");
			}
			else {
				treeDefBuffer.append(" children: ");
				serializeXML(treeDefBuffer, getModel().getChildren(node), childrenToLoad);
			}
		}
		else {
			// remove trailing ','
			treeDefBuffer.append(nodeString.substring(0, nodeString.length() - 1));
		}
		treeDefBuffer.append(" } "); // close the definition

		node.setLoaded(true);
	}

	/**
	 * @param childrenToLoad children that have to be loaded
	 * @param node the current node to check
	 * @return true if the children contains the current node
	 */
	private boolean containsNode(List childrenToLoad, ITreeNode node) {
		if (childrenToLoad == null)
			return false;
		for (Iterator iter = childrenToLoad.iterator(); iter.hasNext();) {
			ITreeNode child = (ITreeNode)iter.next();
			if (child == node || (child.getId() != null && child.getId().equals(node.getId())))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param treeDefBuffer the StringBuffer to write to
	 * @param nodes the nodes that has to be serialized
	 */
	public void serializeXML(StringBuffer treeDefBuffer, List nodes) {
		serializeXML(treeDefBuffer, nodes, null);
	}

	/**
	 * 
	 * @param treeDefBuffer the StringBuffer to write to
	 * @param nodes the nodes that has to be serialized
	 * @param childrenToLoad children that has to be loaded
	 */
	public void serializeXML(StringBuffer treeDefBuffer, List nodes, List childrenToLoad) {
		if (nodes == null)
			return;
		treeDefBuffer.append(" [ ");
		for (Iterator it = nodes.iterator(); it.hasNext();) {
			ITreeNode baby = (ITreeNode)it.next();
			serializeXML(treeDefBuffer, baby, childrenToLoad);
			if (it.hasNext())
				treeDefBuffer.append(",");
		}
		treeDefBuffer.append(" ] ");
	}

	/**
	 * set the given node as the selected one in the tree
	 * 
	 * @param node that has to be selected
	 */
	public void selectNode(ITreeNode node) {
		selectNode(node, false);
	}

	/**
	 * set the given node as the selected one in the tree and calls the listener if the flag is set
	 * 
	 * @param node that has to be selected
	 * @param callListener if the listener for select has to be called
	 */
	public void selectNode(ITreeNode node, boolean callListener) {
		if (getModel().isLoadLazy() && !node.isLoaded()) {
			loadAllParents(node);
		}
		
		// tse - id:treeName, value:nodeId to be selected
		getContext().add(id4selected(), node.getId(), TYPE_TREE_SILENT_SELECTION, IContext.STATUS_EDITABLE);

		if (callListener) {
			getContext().add(id4eventNode(), node.getId(), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
			ITreeListener listener = node.getListener();
			if (listener == null) {
				listener = getDefaultListener();
			}
			listener.onTreeNodeClicked(this, node.getId());
		}
	}

	/**
	 * method to load node and all parents, used for lazy loading tree
	 * 
	 * @param node the node to be loaded
	 */
	private void loadAllParents(ITreeNode node) {
		List nodesToLoad = new ArrayList();
		nodesToLoad.add(node);
		ITreeNode parent = node.getParent();
		if (parent == null) // root
			return;

		while (!parent.isLoaded() && parent.getParent() != null) {
			nodesToLoad.add(parent);
			parent = parent.getParent();
		}

		StringBuffer treeNodeBuffer = new StringBuffer();
		serializeXML(treeNodeBuffer, getModel().getChildren(parent), nodesToLoad);
		getContext().add(parent.getId(), treeNodeBuffer.toString(), TYPE_TREE_LOAD_LAZY, IContext.STATUS_IDEM);
		getContext().moveDown(getId() + ".drag");
		getContext().moveDown(getId() + ".drop");

		for (Iterator iter = contextMenus.keySet().iterator(); iter.hasNext();)
			getContext().moveDown((String)iter.next());

	}

	/**
	 * expand the given node in the tree
	 * 
	 * @param node the node to be expanded
	 */
	public void expandNode(ITreeNode node) {
		if (node == null)
			return;
		if (getModel().isLoadLazy() && !node.isLoaded()) {
			loadAllParents(node);
		}
		// tne - id:treeName, value:nodeId to be expanded
		getContext().add(id4expand(), node.getId(), TYPE_TREE_NODE_EXPAND, IContext.STATUS_COMMAND);
	}

	/**
	 * expand the given node in the tree
	 * 
	 * @param node the node to be expanded
	 */
	public void expandNode(ITreeNode node, boolean callListener ) {
		if (node == null)
			return;
		if (getModel().isLoadLazy() && !node.isLoaded()) {
			loadAllParents(node);
		}
		// tne - id:treeName, value:nodeId to be expanded
		if( callListener )
			getContext().add(id4expand(), node.getId(), TYPE_TREE_NODE_EXPAND, IContext.STATUS_COMMAND);
		else
			getContext().add(id4expand(), node.getId(), TYPE_TREE_NODE_SILENT_EXPAND, IContext.STATUS_COMMAND);
	}

	/**
	 * return the id of the selected node in this tree or null if non is selected the id corresponds to the ITreeNode.getId()
	 * 
	 * @return the current selected nodeId
	 */
	public String getSelectedNodeId() {
		return getContext().getValue(id4selected());
	}

	/**
	 * return the id of the checked nodes
	 * 
	 * @return the current checked nodeIds
	 */
	public String[] getCheckedNodeIds() {
		String checked = getContext().getValue(id4Value());
		if (checked != null && !"".equals(checked))
			return checked.split(",");
		return new String[] {};
	}

	/**
	 * @return the id in the context used to carry the value of the selected node
	 */
	protected String id4selected() {
		return getId() + ".selId";
	}

	/**
	 * @return the id in the context used to carry the value of the selected node
	 */
	protected String id4expand() {
		return getId() + ".expand";
	}

	/**
	 * @return the id in the context used execute events on the tree
	 */
	protected String id4eventNode() {
		return getId() + ".evn";
	}

	/**
	 * get the model in a lazy mode, create from class modelClass or use DefaultTreeModel
	 * 
	 * @return the tree model used to build the nodes of the tree
	 */
	public ITreeModel getModel() {
		if (model == null) {
			// by default use push mode
			model = new DefaultTreeModel();
			if (model instanceof AbstractTreeModel)
				((AbstractTreeModel)model).setTreeId(getId());
		}
		return model;
	}

	/**
	 * @param model the tree model used to build the nodes of the tree
	 */
	public void setModel(ITreeModel model) {
		this.model = model;
		if (model instanceof AbstractTreeModel)
			((AbstractTreeModel)model).setTreeId(getId());
	}

	/**
	 * dispatch events, as of now we only have node events
	 * 
	 * @param event the event that has to be handled
	 */
	@Override
	public void dispatch(ClientEvent event) {
		// super.dispatch(event);

		String nodeId = event.getParameter(0);
		ITreeNode treeNode = getModel().getTreeNode(nodeId);
		if (treeNode != null) {

			getContext().add(id4eventNode(), nodeId, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);

			ITreeListener listener = treeNode.getListener();
			if (listener == null) {
				listener = getDefaultListener();
			}
			if (EVENT_NODE_ONCLICK.equals(event.getType())) {
				getContext().add(id4selected(), nodeId, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
				listener.onTreeNodeClicked(this, nodeId);
			}
			else if (EVENT_NODE_ONCHECK.equals(event.getType())) {
				listener.onTreeNodeChecked(this, nodeId);
			}
			else if (EVENT_NODE_ONOPEN.equals(event.getType())) {
				listener.onTreeNodeOpen(this, nodeId);
			}
			else if (EVENT_NODE_ONCLOSE.equals(event.getType())) {
				listener.onTreeNodeClose(this, nodeId);
			}
			else if (EVENT_DROPPED.equals(event.getType())) {
				String droppedId = event.getParameter(1);
				listener.onTreeNodeDropped(this, nodeId, droppedId);
			}
			else if (EVENT_DRAGGED.equals(event.getType())) {
				listener.onTreeNodeDragged(this, nodeId);
			}
			else if (EVENT_LOAD_LAZY.equals(event.getType())) {
				ITreeModel model = getModel();
				List children = model.getChildren(model.getTreeNode(nodeId));

				StringBuffer buff = new StringBuffer();

				serializeXML(buff, children);
				getContext().add(nodeId, buff.toString(), TYPE_TREE_LOAD_LAZY, IContext.STATUS_NOT_EDITABLE);
				getContext().moveDown(getId() + ".drag");
				getContext().moveDown(getId() + ".drop");
				for (Iterator iter = getContextMenus().keySet().iterator(); iter.hasNext();)
					getContext().moveDown((String)iter.next());
				
				listener.onTreeNodeOpen(this, nodeId);
			}
		}
	}

	/**
	 * 
	 * @return the default tree listener used when no TreeListener is set
	 */
	private ITreeListener getDefaultListener() {

		ITreeListener listener = new AbstractTreeListener() {
			@Override
			public void onTreeNodeClicked(ITree tree, String nodeId) {
				getContext().add(id4selected(), nodeId, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
			}
		};
		return listener;
	}

	/**
	 * @return the id of the node from the last nodeevent. NOTE: This can be different from the getSelectedId()!!!
	 */
	public String getEventNodeId() {
		return getContext().getValue(id4eventNode());
	}

	/**
	 * convenience method is you are using the tree in the recommended way: - use DefaultTreeModel - use DefaultTreeNode with
	 * associated object (setDataObject)
	 * 
	 * @param treeNodeId the id of the TreeNode
	 * @return the dataObject from the TreeNode with given id.
	 */
	public Object dataObject(String treeNodeId) {
		if (getModel().getTreeNode(treeNodeId) instanceof DefaultTreeNode)
			return ((DefaultTreeNode)getModel().getTreeNode(treeNodeId)).getDataObject();
		else if (getModel().getTreeNode(treeNodeId) instanceof GenericTreeNode)
			return ((GenericTreeNode)getModel().getTreeNode(treeNodeId)).getDataObject();
		else
			throw new WGTException("Tree node has no getDataObjectMethod");
	}

	/**
	 * reload the child elements of a node ( on delete or insert)
	 * 
	 * @param parent the node where the children have to be reloaded
	 */
	public void reloadChildren(ITreeNode parent) {
		ITreeModel model = getModel();
		List children = model.getChildren(parent);

		StringBuffer buff = new StringBuffer();

		serializeXML(buff, children);
		getContext().add(parent.getId(), buff.toString(), TYPE_TREE_RELOAD_CHILDREN, IContext.STATUS_NOT_EDITABLE);
		for (Iterator iter = getContextMenus().keySet().iterator(); iter.hasNext();)
			getContext().moveDown((String)iter.next());
//		MH: 13.09.11 call clean cleanLoaded on children
//		getModel().clearLoaded(parent);
		for ( ITreeNode node : (List<ITreeNode>)children)
			getModel().clearLoaded(node);

		getContext().moveDown(getId() + ".drag");
		getContext().moveDown(getId() + ".drop");
		for (Iterator iter = getContextMenus().keySet().iterator(); iter.hasNext();)
			getContext().moveDown((String)iter.next());
	}

	/**
	 * if the nodes label has changed
	 * 
	 * @param node the node to be updated
	 */
	public void updateNodeLabel(ITreeNode node) {
		getContext().add(node.getId(), "[{ title:\"" + node.getCaption() + "\" }]", TYPE_TREE_NODE_UPDATE, IContext.STATUS_NOT_EDITABLE);
	}

	/**
	 * saves CheckBox settings to the model
	 */
	public void saveToModel() {
		String value = getContext().getValue(id4Value());
		List checked = Arrays.asList(value.split(","));
		List allLoaded = getModel().getAllLoaded();

		for (Iterator iter = allLoaded.iterator(); iter.hasNext();) {
			ITreeNode treeNode = (ITreeNode)iter.next();
			treeNode.setChecked(checked.contains(treeNode.getId()));
		}
	}

	/**
	 * @return the id in the context used to carry the value (checked nodes)
	 */
	private String id4Value() {
		return getId() + ".value";
	}

	/**
	 * @param width the width of the tree area
	 */
	public void setTreeWidth(String width) {
		treeWidth = width;
	}

	/**
	 * @param height the height of the tree area
	 */
	public void setTreeHeight(String height) {
		treeHeight = height;
	}

	/**
	 * @return true if onCheck event is enabled
	 */
	public boolean getFireOnCheckEvent() {
		return fireOnCheckEvent;
	}

	/**
	 * @param fireOnCheckEvent if a event should be fired on checking the tree nodes CheckBox
	 */
	public void setFireOnCheckEvent(boolean fireOnCheckEvent) {
		this.fireOnCheckEvent = fireOnCheckEvent;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.tree.ITree#setClickFolderMode(int)
	 */
	public void setClickFolderMode(int clickFolderMode) {
		if (clickFolderMode == ITree.CLICK_FOLDER_MODE_ACTIVATE || clickFolderMode == ITree.CLICK_FOLDER_MODE_ACTIVATE_AND_EXPAND
				|| clickFolderMode == ITree.CLICK_FOLDER_MODE_EXPAND)
			this.clickFolderMode = clickFolderMode;
		else
			throw new WGTException("Unvalid value for clickMode: " + clickFolderMode);
	}

	/**
	 * @param nodeId the nodeId of the node to be bound as droppable
	 */
	protected void initDroppable(String nodeId) {
		String js = "initTreeDroppable('" + getId() + "','" + nodeId + "')";
		getContext().sendJavaScript(getId() + ".drop", js);
	}

	/**
	 * @param nodeId the nodeId of the node to be bound as draggable
	 */
	protected void initDraggable(String nodeId) {
		String js = "initTreeDraggable('" + getId() + "','" + nodeId + "')";
		getContext().sendJavaScript(getId() + ".drag", js);
	}

	/**
	 * @param node the node to be updated
	 */
	public void updateItem(ITreeNode node) {
		getContext().add(node.getId(),
				"[{ title:\"" + node.getCaption() + "\", icon:\"" + node.getIconSrc()[0] + "\",  select:" + node.isChecked() + " }]",
				TYPE_TREE_NODE_UPDATE, IContext.STATUS_NOT_EDITABLE);
	}

	/**
	 * 
	 * @return a HashTable of context menus
	 */
	public Hashtable getContextMenus() {
		contextMenus = new Hashtable<String, IContextMenu>();
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			BaseControl child = (BaseControl)it.next();
			if (child instanceof ContextMenu) {
				contextMenus.put(((IContextMenu)child).getId(), (IContextMenu)child);
			}
		}
		return contextMenus;
	}

	/**
	 * 
	 * @param contextMenu the contextMenu to be added
	 */
	public void addContextMenu(ContextMenu contextMenu) {
		super.addContextMenu(contextMenu);
		if (contextMenus == null)
			contextMenus = new Hashtable();
		contextMenus.put(contextMenu.getId(), contextMenu);
	}

	/**
	 * loads the tree from the model and sends the whole new tree to the client. (can't send parts of the tree)
	 * 
	 * @param node the node to load to the front end
	 */
	public void load(ITreeNode node) {
		if (getModel().isLoadLazy() && !node.isLoaded()) {
			loadAllParents(node);
		}
	}

	/**
	 * collapse the tree
	 */
	public void collapse() {
		getContext().add(getId() + "." + TYPE_TREE_COLLAPSE, "", TYPE_TREE_COLLAPSE, IContext.STATUS_COMMAND);
	}
}
