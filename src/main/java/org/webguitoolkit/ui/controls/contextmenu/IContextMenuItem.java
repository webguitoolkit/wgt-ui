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

import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * <h1>Interface for a ContextMenuItem</h1>
 * <p>
 * The ContextMenuItem is one of the entries in a context menu. It can be displayed as text with icon, 
 * the icon is optional. When clicking on a ContextMenuItem a event is triggered, this event can be processed
 * by a ContextMenuListener. 
 * </p>
 * 
 * <b>Creation of a context menu:</b><br>
 * <pre>
 * 	//the control that owns the context menu
 * 	ILabel control = factory.createLabel(layout, "Some Control");
 * 	// creation of the context menu
 * 	IContextMenu cMenu = factory.createContextMenu( control );
 * 	// creation of a context menu items
 * 	factory.createContextMenuItem(cMenu, "Edit", new EditListener());
 * 	IContextMenuItem deleteItem = factory.createContextMenuItem(cMenu, "Delete", new DeleteListener());
 * 	// set a image that is displayed in front of the text
 * 	deleteItem.setImageSrc("./images/delete.gif");
 * </pre>
 * 
 * <b>The action listener:</b><br>
 * <pre>
 *class EditListener implements IContextMenuListener {
 *
 *	// implement this function when context menu is used on any control except tree and table
 *	public void onAction(ClientEvent event, IBaseControl control) {
 *		getPage().sendInfo("You called " + ((ContextMenuItem)event.getSource()).getLabel()
 * 		                    + " for label " + ((ILabel)control).getLabel() );
 *	}
 *	// implement this method when context menu is used on a table
 *	public void onAction(ClientEvent event, ITable table, int row) {
 *	}
 *	// implement this method when context menu is used on tree nodes
 *	public void onAction(ClientEvent event, ITree tree, String nodeId) {
 *	}
 *}
 * </pre>
 * @author Peter
 * @author Martin
 * 
 * @see IContextMenuItem
 * @see IContextMenuListener
 */
public interface IContextMenuItem extends IBaseControl {
	/**
	 * sets the image for the context menu item that is displayed in front of the label
	 * @param imgSrc the image URL
	 */
	void setImgSrc(String imgSrc);

	/**
	 * sets the label text
	 * @param label plain text
	 */
	void setLabel(String label);

	/**
	 * sets the label as key of the resource bundle
	 * @param labelKey key of the label
	 */
	void setLabelKey(String labelKey);

	/**
	 * sets the listener that processes the click events.
	 * @param listener the listener
	 */
	void setListener(IContextMenuListener listener);
	
	/**
	 * sets the menuItem disabled or enabled
	 * @param true if disabled, false if enabled
	 */
	void setDisabled(boolean disabled);

}
