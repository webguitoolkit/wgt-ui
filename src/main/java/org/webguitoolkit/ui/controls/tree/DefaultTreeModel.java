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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.webguitoolkit.ui.util.comparator.NodeComparator;


/**
 * <pre>
 * Default implementation of a TreeModel.
 * </pre>
 */
public class DefaultTreeModel extends AbstractTreeModel {

	public DefaultTreeModel() {
		super( false, false, false, false );
	}
	public DefaultTreeModel( boolean rootVisible, boolean loadLazy, boolean hasCheckboxes, boolean hasDragDrop ) {
		super( rootVisible, loadLazy, hasCheckboxes, hasDragDrop );
	}
	

	public void setRootElement(ITreeNode rootElement) {
		this.rootElement = rootElement;
	}

	public void setRootVisible(boolean rootVisible) {
		this.rootVisible = rootVisible;
	}
	
	/**
	 * a map with all nodes, key is the node id, which may be generated.
	 * @return
	 */
	public Map getNodeList() {
		return nodeList;
	}

	public void setNodeList(Map nodeList) {
		this.nodeList = nodeList;
	}
		
	public DefaultTreeNode searchTreeNode(Object dataObject) {
		return searchTreeNode( (DefaultTreeNode)rootElement, dataObject);
	}
	private DefaultTreeNode searchTreeNode( DefaultTreeNode node, Object dataObject) {
		if( node.getDataObject() == dataObject )
			return node;
		if( node.hasChildren() ){
			for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
				DefaultTreeNode found = searchTreeNode( (DefaultTreeNode) it.next(), dataObject );
				if( found!=null) return found;
			}
		}
		return null;
	}

	
	/**
	 * sorts the tree. At each hierarchie level it sorts the entries alphabetical by the displayname.
	 */
	public void sort() {
		sortSubTree(getRootElement());
	}
	
	
	public void sortSubTree(ITreeNode node) {
		final List children = ((DefaultTreeNode)node).getChildren();
		if (children==null) return;
		Collections.sort(children, new NodeComparator());
		for (Iterator it = children.iterator(); it.hasNext();) {
			DefaultTreeNode subnode = (DefaultTreeNode) it.next();
			sortSubTree(subnode);
		}
		
	}

	public List getChildNodes( ITreeNode treeNode ) {
		return ((DefaultTreeNode)treeNode).getChildren();
	}

	public boolean hasChildren( ITreeNode treeNode ) {
		return ((DefaultTreeNode)treeNode).getChildren()==null || !((DefaultTreeNode)treeNode).getChildren().isEmpty();
	}

	public void setHasCheckBoxes( boolean hasCheckboxes ) {
		this.hasCheckboxes = hasCheckboxes;
	}


	public void setHasDragDrop( boolean hasDragDrop ) {
		this.hasDragDrop = hasDragDrop;
	}
	public String getContextMenuIdForNode(ITreeNode node) {
		return ((DefaultTreeNode)node).getContextMenuId();
	}
}
