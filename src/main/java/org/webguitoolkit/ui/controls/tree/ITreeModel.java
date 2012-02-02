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

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * Model for the Tree class. It just provides some meta info in this model,
 * the real data is the provided by the recursive ITreeNode-Objects.
 * see getRootElement().
 * </pre>
 * 
 * @author Arno
 */
public interface ITreeModel extends Serializable {
	/**
	 * Getting the root node of the actually data-nodes. However in some tree visualization you may not need a single root. Then
	 * set the isRootVisible to false and provide a dummy which just returns the 'real' root elements.
	 * 
	 * @return ITreeNode or null if tree not visible
	 */
	public ITreeNode getRootElement();

	/**
	 * isRootVisible - return here false to degrade the root node als dummy place holder for the real root nodes ( which can be
	 * multiple).
	 */
	public boolean isRootVisible();

	/**
	 * method to get all children of a node, usefull for lazy loading
	 * 
	 * @param treeNode
	 * @return
	 */
	public List getChildren(ITreeNode treeNode);

	/**
	 * method to check if a node has children
	 * 
	 * @param treeNode
	 * @return
	 */
	public boolean hasChildren(ITreeNode treeNode);

	/**
	 * loadLazy - return true if nodes are loaded lazy, this can have impact on searching the tree
	 * 
	 * @return true if lazy loading is enabled
	 */
	public boolean isLoadLazy();

	/**
	 * sets if branches of the tree should be loaded on demand or if the whole tree is loaded on initialisation
	 */
	public void setLoadLazy(boolean loadLazy);

	/**
	 * clear isLoaded for all loaded nodes, needed for lazy loading
	 */
	public void clearLoaded();

	/**
	 * clear isLoaded for children, needed for lazy loading
	 */
	public void clearLoaded(ITreeNode parent);

	/**
	 * returns true if there are checkboxes in the tree
	 */
	public boolean hasCheckBoxes();

	/**
	 * @return all loaded tree nodes
	 */
	public List getAllLoaded();

	/**
	 * returns the treeNode by id
	 * 
	 * @param id
	 * @return
	 */
	public ITreeNode getTreeNode(String id);
}
