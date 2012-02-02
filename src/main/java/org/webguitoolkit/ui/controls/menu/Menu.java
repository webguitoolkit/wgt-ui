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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.IMG;
import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * The Menu is an element in the MenuBar and is displayed horizontally.
 * It can contain a set of MenuItems.
 * 
 * The Menu can be displayed as label or with a image.
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
public class Menu extends ActionControl implements IMenu {

	public static String DOT_LABEL = ".label";

	private String label;
	private String iconSrc = null;
	private String dropdownIconSrc = null;
	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
    public Menu() {
		super();
		setDefaultCssClass("main");
	}
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
     * @param id unique HTML id
     */
	public Menu(String id) {
		super(id);
		setDefaultCssClass("main");
	}
	
	protected void startHTML(PrintWriter out) {
		out.print("<li id=\"" + getId() + "\"");
		// eat the setAttribute .class
		String classAt = getContext().processValue(getId() + ".class");
		if (StringUtils.isEmpty(classAt)) {
			out.print(JSUtil.at("class", getCssClass(), ""));
		}
		else {
			out.print(JSUtil.atNotEmpty("class", classAt));
		}
		out.print(">\n");
		
        // if source is specified render the link as image
        if( StringUtils.isNotEmpty(getIconSrc()) ) {
        	IMG img = new IMG();
        	img.setSrc( getIconSrc() );
        	img.setBorder( 0 );
        	img.setClass( "wgtMenuImg" );
        	//link.addElement( img );
        	img.output(out);
        } 
		out.println( getLabel() );    	
		out.println("<ul>" );    	
	}
	protected void endHTML( PrintWriter out ){
		out.println("</ul>" );  
        // if source is specified render the link as image
        if( StringUtils.isNotEmpty(getDropdownIconSrc()) ) {
        	IMG img = new IMG();
        	img.setSrc( getDropdownIconSrc() );
        	img.setBorder( 0 );
        	img.setClass( "wgtMenuDropdownImg" );
        	//link.addElement( img );
        	img.output(out);
        } 		
		out.println("</li>");
    }

	protected void init() {
	}
	
	public void addMenu( IMenu menu ){
		add( menu );
	}
	public void addMenuItem( IMenuItem menuItem ){
		add( menuItem );
	}
	public void addMenuItemSeparator( IMenuItemSeparator sep ){
		add( sep );
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setLabelKey(String labelKey) {
		this.label = TextService.getString( labelKey );
	}
	public String getIconSrc() {
		return iconSrc;
	}
	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}
	
	public String getDropdownIconSrc() {
		return dropdownIconSrc;
	}
	public void setDropdownIconSrc(String newDopdownIconSrc) {
		this.dropdownIconSrc = newDopdownIconSrc;
	}
	
	/**
	 * Use this method to realise highlighting of the menu. Call it in the
	 * onAction-method of a dependent menu-item. 
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		// if the menu should be set active we have to deactivate all other menus
		if (active) {
			List<BaseControl> sameLevelElements = this.getParent().getChildren();
			for (BaseControl baseControl : sameLevelElements) {
				if (baseControl instanceof Menu && !this.equals(baseControl)) {
					((Menu) baseControl).removeCssClass("menu_active");
				}
			}
		}
		addCssClass("menu_active");
	}
	public boolean isActive() {
		return cssClassList.contains("menu_active");
	}
}
