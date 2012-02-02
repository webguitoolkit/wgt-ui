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
package org.webguitoolkit.ui.controls;

import java.io.Serializable;

import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.layout.ILayoutElement;
import org.webguitoolkit.ui.controls.util.Tooltip;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * <b>Base interface for all Controls in WGT.</b><br>
 * 
 * Methods for the visibility, stylesheet and the tooltips.
 * 
 * @author Peter
 * @author Martin
 * 
 */
public interface IBaseControl extends ILayoutElement, Serializable {

	/**
	 * gets the root element of the component tree, it is always the page element.<br>
	 * 
	 * @return the page (root element of the component tree)
	 */
	Page getPage();

	/**
	 * Control visibility<br>
	 * 
	 * @param vis true if the control should be displayed
	 */
	void setVisible(boolean vis);

	/**
	 * Check visibility<br>
	 * 
	 * @return true if the control is visible
	 */
	boolean isVisible();

	/**
	 * change visibility (if hidden to shown or the other way)<br>
	 */
	void toggleVisible();

	/**
	 * @return space separated list of classes
	 */
	String getCssClass();

	/**
	 * @param cssClass the CSS class to set defined in the style sheet file
	 * @deprecated use addCssClass
	 */
	void setCssClass(String cssClass);

	/**
	 * Adds a CSS classes to the default class.<br>
	 * 
	 * @param cssClass the CSS class to add.
	 */
	void addCssClass(String cssClass);

	/**
	 * Remove CSS classes form the list of classes for the control<br>
	 * 
	 * @param cssClass the CSS class to remove.
	 */
	void removeCssClass(String cssClass);

	/**
	 * @return the tool tip string
	 */
	String getTooltip();

	/**
	 * Sets the tool tip of the control using the standard HTML title tag<br>
	 * 
	 * @param tooltip the tool tip string
	 */
	void setTooltip(String tooltip);

	/**
	 * Localized method using the TextService for receiving the actual tool tip string.<br>
	 * 
	 * @param tooltipKey the resource key of the tooltip
	 */
	void setTooltipKey(String tooltipKey);

	/**
	 * Adds a context menu to the control. The context menu can be created once and added to different controls.<br>
	 * 
	 * @param contextMenu the context menu to add to the control
	 */

	void addContextMenu(IContextMenu contextMenu);

	/**
	 * Alternative tooltip option.<br>
	 * <p>
	 * Expects a Tooltip object for the jquery tooltip. Uses the Tooltip Text attribute to set the title tag. This is necessary
	 * because most of the jquery tooltip options will use the HTML title tag as source of the tool tip text<br>
	 * </p>
	 * 
	 * @param tooltip the tool tip object to be set
	 */
	void setTooltip(Tooltip tooltip);

	/**
	 * @return the ToolTip object if using the advanced jQuery tool tip
	 */
	Tooltip getTooltipAdvanced();

	/**
	 * Alternative tooltip option.<br>
	 * <p>
	 * Expects a Tooltip object for the jquery tooltip. Uses the Tooltip Text attribute to set the title tag. This is necessary
	 * because most of the jquery tooltip options will use the HTML title tag as source of the tool tip text<br>
	 * </p>
	 * 
	 * @param tooltipAdvanced the tool tip object to be set
	 */
	void setTooltipAdvanced(Tooltip tooltipAdvanced);

	/**
	 * User CSS classes if possible!
	 * 
	 * @return the current style object, representing the inline style.
	 */
	Style getStyle();

	/**
	 * sets the inline style of an element
	 * 
	 * User CSS classes if possible!
	 * 
	 * @param style the style object
	 */
	void setStyle(Style style);

	/**
	 * The unique created automatically if not set in the constructor.
	 * 
	 * @return unique HTML id
	 */
	String getId();
	
	/**
	 * Sets the name attribute for writing automated frontend tests.
	 * 
	 * @param name attribute for automated tests
	 */
	void setName( String name );
	String getName();
}
