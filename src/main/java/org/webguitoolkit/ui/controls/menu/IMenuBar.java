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
package org.webguitoolkit.ui.controls.menu;

import org.webguitoolkit.ui.controls.IActionControl;

/**
 * Interface of the MenuBar. The MenuBar is used for navigation in an application. It is displayed like top menus in
 * application that fold down (File -> Open).
 * 
 * <pre>
 * -----MenuBar----------------------------------------------
 * |  Menu       |  Menu        |  Menu       |
 * ----------------------------------------------------------
 *               | MenuItem     |               
 *                --------------
 *               | MenuItem     |               
 *                --------------
 *               | MenuItem     |               
 *                --------------
 * </pre>
 * 
 * CSS classes : wgtDropdownBtn
 * 
 * @author Peter
 * 
 */
public interface IMenuBar extends IActionControl {

	int DISPLAY_MODE_MENU = 0;// default
	int DISPLAY_MODE_DROPDOWN_BUTTON = 1;// default

	/**
	 * Add a menu to the bar.
	 * 
	 * @param menu
	 */
	void addMenu(IMenu menu);

	/**
	 * Add an Item directly to the bar
	 * 
	 * @param menuItem
	 */
	void addMenuItem(IMenuItem menuItem);

	/**
	 * Control how the menu is displayed.
	 * 
	 * @param diplayMode
	 *            the diplayMode to use.Use constants to set required display mode. No action required for default
	 *            behavior.
	 */
	void setDiplayMode(int diplayMode);

}