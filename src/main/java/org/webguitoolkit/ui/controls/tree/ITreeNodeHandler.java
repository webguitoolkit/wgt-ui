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

import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;

/**
 * <pre>
 * Defines how a TreeNode is displayed, the ContextMenu, if there are CheckBoxes, the Listener for the Node,
 * the ChildSelector, if the Node is draggable or droppable.
 * </pre>
 */
public interface ITreeNodeHandler extends Serializable {
	/**
	 * 
	 * @return the property to control the caption (?)
	 */
	String getDisplayProperty();

	/**
	 * 
	 * @return the property to control the tool tip
	 */
	String getTooltipProperty();

	/**
	 * 
	 * @return the path to the icon
	 */
	String[] getIconSrc();

	/**
	 * 
	 * @return the menu
	 */
	IContextMenu getContextMenu();

	/**
	 * 
	 * @return true if the node has a check box
	 */
	boolean isHasCheckboxes();

	/**
	 * 
	 * @return the attribute controlling the check box
	 */
	String getCheckboxParameter();

	/**
	 * 
	 * @return the current listener
	 */
	ITreeListener getListener();

	/**
	 * 
	 * @return the array of child selectors (names)
	 */
	String[] getChildSelectors();

	/**
	 * 
	 * @return true if dragable
	 */
	boolean isDraggable();

	/**
	 * 
	 * @return true if droppable
	 */
	boolean isDroppable();

	/**
	 * @return the CSS style string
	 */
	String getStyle();

	/**
	 * Indicate if a object can be handled by this Handler.
	 * 
	 * @param o an instance of the object
	 * @return true if it fits
	 */
	boolean canHandle(Object o);
}
