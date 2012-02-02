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

import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * Interface for the Tree control.
 *
 * @author Peter
 *
 */
public interface ITree extends IBaseControl {

	// static tree events
	public static final String EVENT_NODE_ONCLICK = "node_onclick";
	public static final String EVENT_NODE_ONOPEN = "node_onopen";
	public static final String EVENT_NODE_ONCHECK = "node_checked";
	public static final String EVENT_DROPPED = "node_dropped";
	public static final String EVENT_DRAGGED = "node_dragged";

	public static final int CLICK_FOLDER_MODE_ACTIVATE= 1;
	public static final int CLICK_FOLDER_MODE_EXPAND= 2;
	public static final int CLICK_FOLDER_MODE_ACTIVATE_AND_EXPAND= 3; //default


	/**
	 * set the given node as the selected one in the tree
	 */
	void selectNode(ITreeNode node);

	/**
	 * expand the given node in the tree
	 */
	void expandNode(ITreeNode node);

	/**
	 * expand the given node in the tree and listener is called by the tree if flag is set
	 */
	void expandNode(ITreeNode node, boolean callListener);

	/**
	 * @return the id of the selected node in this tree or null if non is selected the id corresponds to the
	 * ITreeNode.getId()
	 */
	String getSelectedNodeId();


	/**
	 * @return the id of the checked nodes in this tree or empty array if non is checked. The id corresponds to the
	 * ITreeNode.getId()
	 */
	String[] getCheckedNodeIds();

	/**
	 * @return the underlying model
	 */
	ITreeModel getModel();

	/**
	 *
	 * @param model
	 */
	void setModel(ITreeModel model);

	/**
	 * reload the child elements of a node ( on delete or insert)
	 */
	void reloadChildren(ITreeNode parent);

	/**
	 * if the nodes label has changed
	 */
	void updateNodeLabel(ITreeNode node);

	/**
	 * saves check-box settings to the model
	 */
	void saveToModel();

	/**
	 *
	 * @param fireOnCheckEvent
	 */
	void setFireOnCheckEvent(boolean fireOnCheckEvent);


	/**
	 *	Set click mode for tree, use constants of ITree.
	 *	May not be supported by all implementations
	 *
	 * @param clickFolderMode
	 */
	void setClickFolderMode(int clickFolderMode);

	/**
	 * update the TreeNode (caption, icon, children)
	 * @param node
	 */
	void updateItem(ITreeNode node);

	/**
	 * send model TreeNodes to the browser
	 */
	void load();

	/**
	 * send TreeNode to the browser
	 * @param gtnNode
	 */
	void load(ITreeNode gtnNode);

	/**
	 * collapse the tree
	 */
	void collapse();

}