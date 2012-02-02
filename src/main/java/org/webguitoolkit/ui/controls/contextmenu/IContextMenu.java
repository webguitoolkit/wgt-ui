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
 * <h1>Interface for a context menu</h1>
 * <p>
 * A ContextMenu is a menu that is displayed on right click on a GUI element. The ContextMenu can be bound to any control.
 * The ContextMenu has to have at least one ContextMenuItem.
 * </p>
 * <pre>
 * Some Control--ContextMenu-------
 *             | ContextMenuItem1 |
 *             |------------------|
 *             | ContextMenuItem2 |
 *             --------------------
 * </pre>
 * <b>Creation of a context menu:</b><br>
 * <pre>
 * 	//the control that owns the context menu
 * 	ILabel control = factory.createLabel(layout, "Some Control");
 * 	// creation of the context menu
 * 	IContextMenu cMenu = factory.createContextMenu( control );
 * 	// creation of a context menu item
 * 	factory.createContextMenuItem(cMenu, "Item 1", new ContextListener());
 * </pre>
 * <p>
 * A ContextMenu can be created once and used on several other controls, this is used 
 * implicitly with the tree and the table, but can also be done for every other control by call the <code>bindTo()</code> function.
 * </p>
 * <br>
 * CSS classes : contextMenu
 * 
 * @author Peter
 * @author Martin
 * 
 * @see IContextMenuItem
 */
public interface IContextMenu extends IBaseControl {

	/**
	 * Initialize with no binding.
	 */
	void initNoBind();

	/**
	 * can be used to bind a ContextMenu to any control or HTML element with a id.
	 * 
	 * @param cssId
	 *            the id of the DOM element witch is usually the same as the components id.
	 */
	void bindTo(String cssId);

	/**
	 * can be used to bind a ContextMenu to a tables row.
	 * 
	 * @param cssId
	 *            the id of the tables row.
	 */
	void bindToTable(String cssId);

	/**
	 * binds a context menu to a tree node, this function is used by the tree.
	 * 
	 * @param cssId the CSS ID
	 */

	void bindToTreeNode(String cssId);

	/**
	 * Add an item to the ContexMenu
	 * 
	 * @param cmi
	 *            the item
	 */
	void addContextMenuItem(IContextMenuItem cmi);


	/**
	 * disables a ContextMenu for a given Object
	 * @param cssId id of the bound element
	 */
	void unbind( String cssId );

}
