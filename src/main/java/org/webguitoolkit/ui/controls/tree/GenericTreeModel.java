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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;

/**
 * <pre>
 * The generic tree model is used to show object trees.
 *
 * The displayed field and the child node selector of a TreeNode is provided by the TreeNodeHandler.
 * </pre>
 */
public class GenericTreeModel extends AbstractTreeModel {

	protected Set treeNodeHandler = new HashSet();
	protected ITreeNodeHandler defaultTreeNodeHandler = null;
	protected Object root;

	public GenericTreeModel(boolean rootVisible, boolean loadLazy,
			boolean hasCheckboxes, boolean hasDragDrop) {
		super(rootVisible, loadLazy, hasCheckboxes, hasDragDrop);
	}

	public void setRoot( Object o ){
		root = o;
	}

	@Override
	public ITreeNode getRootElement() {
		ITreeNodeHandler tnd = getTreeNodeHandler(root);
		GenericTreeNode tn = null;
		if( root instanceof IDataBag )
			tn = new GenericTreeNode( tnd, (IDataBag)root );
		else
			tn = new GenericTreeNode( tnd, WebGuiFactory.getInstance().createDataBag(root) );
		tn.setId(  generateNodeId() );
		nodeList.put( tn.getId(), tn );
		rootElement = tn;
		return super.getRootElement();
	}

	protected ITreeNodeHandler getTreeNodeHandler( Object o ){
		if( o instanceof IDataBag )
			o = ((IDataBag)o).getDelegate();

		for( Iterator iter =treeNodeHandler.iterator(); iter.hasNext(); ){
			ITreeNodeHandler definition = (ITreeNodeHandler) iter.next();
			if( definition.canHandle( o ) ){
				return definition;
			}
		}
		return defaultTreeNodeHandler;
	}

	public void addTreeNodeHandler( ITreeNodeHandler handler ){
		treeNodeHandler.add( handler );
	}

	@Override
	public List getChildNodes(ITreeNode treeNode) {
		List childObjects =  ((GenericTreeNode)treeNode).getChildObjects();
		List returnList = new ArrayList();
		int id = 0;
		for( Iterator iter = childObjects.iterator(); iter.hasNext(); ){
			Object o = iter.next();
			GenericTreeNode genericTreeNode;
			if( o instanceof IDataBag )
				genericTreeNode = new GenericTreeNode( getTreeNodeHandler(o), (IDataBag)o, treeNode );
			else
				genericTreeNode = new GenericTreeNode( getTreeNodeHandler(o), WebGuiFactory.getInstance().createDataBag(o), treeNode );
			genericTreeNode.setId( generateNodeId(treeNode, id++));
			returnList.add(genericTreeNode);
		}
		return returnList;
	}

	public boolean hasChildren(ITreeNode treeNode) {
		return ((GenericTreeNode)treeNode).hasChildren();
	}

	public ITreeNodeHandler getDefaultTreeNodeHandler() {
		return defaultTreeNodeHandler;
	}

	public void setDefaultTreeNodeHandler(
			ITreeNodeHandler defaultTreeNodeHandler) {
		this.defaultTreeNodeHandler = defaultTreeNodeHandler;
	}


}
