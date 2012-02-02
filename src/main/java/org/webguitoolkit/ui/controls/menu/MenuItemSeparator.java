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

import java.io.PrintWriter;

import org.apache.ecs.html.LI;
import org.webguitoolkit.ui.controls.BaseControl;

/**
 * <pre>
 * The MenuItemSeperator is used to group MenuItems, it renders a little 
 * space between to MenuItems.
 * 
 * -----MenuBar----------------------------------------------
 * |  Menu       |  Menu        |  Menu       |
 * ----------------------------------------------------------
 *               | MenuItem     |               
 *                --------------
 *               | MenuItem     |               
 *                --------------
 *              -MenuItemSeperator-
 *                --------------
 *               | MenuItem     |               
 *                --------------
 * </pre>
 */
public class MenuItemSeparator extends BaseControl implements IMenuItemSeparator{

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public MenuItemSeparator(){
		super();
	}
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public MenuItemSeparator(String id) {
		super(id);
	}
	protected void endHTML( PrintWriter out ){
		LI li = new LI();
		li.setClass( "wgtMenuSeperator" );
		li.output( out );
	}
	protected void init() {
	}
}
