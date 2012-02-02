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
 * 
 * The Menu is an element in the MenuBar and is displayed horizontally. It can contain a set of MenuItems.
 * 
 * The Menu can be displayed as label or with a image.
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
 * CSS classes : wgtMenuImg, wgtMenuDropdownImg
 * 
 * @author Peter
 * 
 */
public interface IMenu extends IActionControl {
	/**
	 * 
	 * @param menu the additional Menu
	 */
	 void addMenu(IMenu menu);

	/**
	 * 
	 * @param menuItem the additional Item
	 */
	 void addMenuItem(IMenuItem menuItem);

	/**
	 * 
	 * @param separator 
	 */
	 void addMenuItemSeparator(IMenuItemSeparator separator);

	/**
	 * 
	 * @param label
	 */
	 void setLabel(String label);

	/**
	 * 
	 * @param labelKey
	 */
	 void setLabelKey(String labelKey);

	/**
	 * 
	 * @param iconSrc
	 */
	 void setIconSrc(String iconSrc);

	/**
	 * 
	 * @param newDopdownIconSrc
	 */
	 void setDropdownIconSrc(String newDopdownIconSrc);

}