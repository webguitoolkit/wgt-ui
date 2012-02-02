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

/**
 * <pre>
 * dataStructure for the TreeNode, The TreeModel with reference this ITreeNode, 
 * which is the recursive structure of the representing tree-data.
 * 
 * Here is only the data modeled. for all event see the (action)listeners.
 * </pre>
 * 
 * @author arno
 */
public interface ITreeNode extends Serializable {

	/**
	 * The name of the node. The caption is the text printed right to the icon of the node.
	 * 
	 * @return the caption string
	 */
	String getCaption();

	/**
	 * Id to identify this node, will be used in the listeners to identify which nodes has been causing the event. the returned
	 * string must comply to the css rules for css-ids. must be unique within the recursive structure.
	 * 
	 * @return the id
	 */
	String getId();

	/**
	 * is this node being displayed as expanded initially (initially means at the time the html for this tree is being rendered).
	 * 
	 * @return true if the node has been expanded
	 */
	boolean isExpanded();

	/**
	 * There can be only one node selected at the most. If none of the node return true, no node is selected. If one or more nodes
	 * return true, the last node which returned true will be selected in the tree. This only applies to the initial drawing (html
	 * output) of the tree. It has no effect on subsequent AXAJ calls and tree changes.
	 * 
	 * @return true if the node is selected
	 */
	boolean isSelected();

	/**
	 * path to the icons for this node, the string array must have length 3: 0 - closed, (for folders) 1 - opened (for folders) 2
	 * - document (for leafs)
	 * 
	 * @return null if you want to use the standard icons.
	 */
	String[] getIconSrc();

	/**
	 * @return the listener for the specified tree node
	 */
	ITreeListener getListener();

	/**
	 * @return true if the node is already loaded to the clients
	 */
	boolean isLoaded();

	/**
	 * @param isLoaded true if the node is already loaded to the clients
	 */
	void setLoaded(boolean isLoaded);

	/**
	 * @return true this node has a check box (additionally the hasCheckBoxes of the model must return true )
	 */
	boolean hasCheckBox();

	/**
	 * @return true if the node has a check box
	 */
	boolean isChecked();

	/**
	 * sets the checked flag of the tree node
	 * 
	 * @param checked true to check the node
	 */
	void setChecked(boolean checked);

	/**
	 * @return the ID of the menu
	 */
	String getContextMenuId();

	/**
	 * @return true if there can something dropped on the tree node
	 */
	boolean isDraggable();

	/**
	 * @return true if the tree node can be dragged
	 */
	boolean isDroppable();

	/**
	 * @return the current parent initialized by getChildren() of the underlying model.
	 */
	ITreeNode getParent();

	/**
	 * @return a tool tip text
	 */
	String getTooltip();

	/**
	 * @return the style of the nodes text
	 */
	String getStyle();

}
