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
package org.webguitoolkit.ui.controls.contextmenu;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ecs.html.Div;
import org.apache.ecs.html.UL;
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.tree.ITree;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * container class for ContextMenuItems, a ContextMenu instance can be added to more then one control.
 */
public class ContextMenu extends BaseControl implements IContextMenu {

	protected List contextMenuItem = new ArrayList();
	protected boolean isDrawn = false;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public ContextMenu() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public ContextMenu(String id) {
		super(id);
		setCssClass("contextMenu");
	}

	public void removeInternal() {
		// MH fixed memory leaks! 19.11.2008
		getContext().removeNode(getId());
		super.removeInternal();
	}

	protected void draw(PrintWriter out) {
		if (!isDrawn) {
			Div div = new Div();
			div.setClass("contextMenu");
			div.setStyle("display: none;");
			div.setID(getId());

			UL ul = new UL();
			div.addElement(ul);
			for (Iterator iter = contextMenuItem.iterator(); iter.hasNext();) {
				ContextMenuItem cmi = (ContextMenuItem)iter.next();
				ul.addElement(new EcsAdapter(cmi));
			}

			getContext().add(page.getId() + ".append" + (getId().replace('.', '_')), div.toString(), IContext.TYPE_ADD_2_NODE_LAST,
					IContext.STATUS_IDEM);
			getContext().moveDown(getId());
		}
		isDrawn = true;
		if (!(parent instanceof ITree) && !(parent instanceof ITable))
			bindTo(parent.getId());
	}

	protected void endHTML(PrintWriter out) {
	}

	protected void init() {
		initNoBind();
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.contextmenu.IContextMenu#initNoBind()
	 */
	public void initNoBind() {
		getPage().addHeaderJS("./" + ResourceServlet.SERVLET_URL_PATTERN + "/contextmenu.js");
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.contextmenu.IContextMenu#bindTo(java.lang.String)
	 */
	public void bindTo(String cssId) {
		bindTo(cssId, IContextMenuListener.EVENT_TYPE_ACTION);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.contextmenu.IContextMenu#bindToTable(java.lang.String)
	 */
	public void bindToTable(String cssId) {
		bindTo(cssId, IContextMenuListener.EVENT_TYPE_TABLE_ACTION);
	}

	/**
	 * @param treeNodeId id of the tree node to be bound to
	 */
	public void bindToJQTreeNode(String treeNodeId) {
		bindTo(treeNodeId, IContextMenuListener.EVENT_TYPE_TREE_ENTRY_ACTION);
	}

	private void bindTo(String cssId, int eventType) {
		if (!isDrawn)
			drawInternal(null);
		String bindings = "";
		boolean first = true;
		for (Iterator iter = contextMenuItem.iterator(); iter.hasNext();) {
			ContextMenuItem cmi = (ContextMenuItem)iter.next();
			if (cmi.hasListener()) {
				if (!first)
					bindings += ",";
				first = false;
				bindings += "'" + cmi.getId() + "': function(t){" + JSUtil.jsEventParam(cmi.getId(), new String[] { cssId })
						+ JSUtil.jsFireEvent(cmi.getId(), eventType) + "}";
			}
		}

		String properties = "{ menuId: '" + getId() + "', targetId: '" + cssId + "', bind : true, bindings:{ " + bindings + " } }";
		getContext().add(getId(), properties, Context.TYPE_CONTEXT_MENU_BIND, Context.STATUS_COMMAND);
	}

	/**
	 * disables a ContextMenu for a given Object
	 */
	public void unbind(String cssId) {
		String properties = "{ targetId: '" + cssId + "', bind : false }";
		getContext().add(getId(), properties, Context.TYPE_CONTEXT_MENU_BIND, Context.STATUS_COMMAND);
	}

	/**
	 * disables a ContextMenu for a given Object
	 */
	public static void unbind(String cssId, IContext context) {
		if (context != null) {
			String properties = "{ targetId: '" + cssId + "', bind : false }";
			context.add(cssId + ".contextmenu", properties, Context.TYPE_CONTEXT_MENU_BIND, Context.STATUS_COMMAND);
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.contextmenu.IContextMenu#bindToTreeNode(java.lang.String)
	 */
	public void bindToTreeNode(String treeNodeId) {
		String bindings = "";
		boolean first = true;
		for (Iterator iter = contextMenuItem.iterator(); iter.hasNext();) {
			ContextMenuItem cmi = (ContextMenuItem)iter.next();
			if (cmi.hasListener()) {
				if (!first)
					bindings += ",";
				first = false;
				bindings += "'" + cmi.getId() + "': function(t){" + JSUtil.jsEventParam(cmi.getId(), new String[] { treeNodeId })
						+ JSUtil.jsFireEvent(cmi.getId(), IContextMenuListener.EVENT_TYPE_TREE_ENTRY_ACTION) + "}";
			}
		}

		// select objects with the tree extension objBelong,
		// find current node
		// add context menu to first tr
		String js = "var objBel = jQuery( 'table[objBelong]' );" + "for(var i = 0;i<objBel.length;i++){ "
				+ "if( objBel.get(i).objBelong.id=='" + treeNodeId + "' ){ "
				+ "jQuery( objBel.get(i).childNodes[0].childNodes[0] ).find( 'td:gt(1) :first-child' ).contextMenu('" + getId() + "',{ "
				+ "bindings:{ " + bindings + " }" + "}); break;} }";

		getContext().sendJavaScript(getId(), js);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.contextmenu.IContextMenu#addContextMenuItem(org.webguitoolkit.ui.controls.contextmenu.ContextMenuItem)
	 */
	public void addContextMenuItem(IContextMenuItem cmi) {
		contextMenuItem.add(cmi);
		add(cmi);
	}
}
