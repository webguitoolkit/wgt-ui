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

import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * <pre>
 * The MenuBar is used for navigation in an application. It is displayed like top menus
 * in application that fold down (File -> Open).
 * 
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
 */
public class MenuBar extends ActionControl implements IMenuBar {

	private int diplayMode = 0;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public MenuBar() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public MenuBar(String id) {
		super(id);
	}

	protected void startHTML(PrintWriter out) {
		if (this.diplayMode == DISPLAY_MODE_DROPDOWN_BUTTON) {
			out.println("<div " + JSUtil.atId(getId()) + " " + JSUtil.at("style", "display: none;", "") + " class=\"wgtDropdownBtn\"><ul "
					+ JSUtil.atId(getId() + "_mb") + " >");
		}
		else {
			out.println("<div " + JSUtil.atId(getId()) + " " + JSUtil.at("style", "display: none;", "") + " ><ul "
					+ JSUtil.atId(getId() + "_mb") + " >");
		}
	}

	protected void endHTML(PrintWriter out) {
		out.println("</ul></div>");
		initJQuery();
	}

	protected void init() {
		getPage().addWgtJS("menu.js");
	}

	public void addMenu(IMenu menu) {
		add(menu);
	}

	public void addMenuItem(IMenuItem menuItem) {
		add(menuItem);
	}

	protected void initJQuery() {
		String jsString = "jQuery('#" + getId() + "_mb').clickMenu({" + "arrowSrc: './images/wgt/pathmarker.gif'," + "onClick: function(){"
				+ "var a = jQuery(this).find('>a'); " + "if( a.length ){" + "fireWGTEvent( jQuery(a).attr('id'), '"
				+ ClientEvent.TYPE_ACTION + "' );" + "jQuery('#" + getId() + "_mb').trigger('closemenu');}" + "return false;" + "}});";
		getContext().sendJavaScript(getId() + "_js", jsString);
		getContext().setVisible(getId(), true);
	}

	public void scheduleRedraw() {
		super.redraw();
		// initJQuery();
	}

	/**
	 * @return the diplayMode
	 */
	public int getDiplayMode() {
		return diplayMode;
	}

	/**
	 * Use constants to set required display mode. No action required for default behaviour.
	 * 
	 * @param diplayMode the diplayMode to set
	 */
	public void setDiplayMode(int diplayMode) {
		this.diplayMode = diplayMode;
	}

}
