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

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.LI;
import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * The MenuItem is an element in the Menu, the MenuItems are displayed vertically.
 * It can contain a set of sub MenuItems.
 * 
 * The MenuItem can be displayed as label or with a image. An ActionListener can 
 * be implemented to do the work if the item is pressed.
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

public class MenuItem extends ActionControl implements IMenuItem {

	private String label;
	private String iconSrc = null;
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public MenuItem(){
		super();
		setCssClass( "wgtMenuItem" );
	}
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public MenuItem(String id) {
		super(id);
		setCssClass( "wgtMenuItem" );
	}
	
	protected void endHTML( PrintWriter out ){
		LI li = new LI();
		if(hasStyle()){
			//support custom styles
			li.setStyle(getStyleAsString());
		}
		
		A link = new A();
		link.setHref("#");
		if( getParent() instanceof MenuBar ){
			link.setOnClick( JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION, JSUtil.ACTION_ON_CLICK )+"return false;" );
		}
		stdParameter(link );
		li.addElement( link );

        // if source is specified render the link as image
        if( StringUtils.isNotEmpty(getIconSrc()) ) {
        	IMG img = new IMG();
        	img.setSrc( getIconSrc() );
        	img.setBorder( 0 );
        	img.setClass( "wgtMenuImg" );
        	link.addElement( img );
        	li.setClass("wgtMenuItemImgBG");
        } 
        if( StringUtils.isNotEmpty( getLabel() ) ){
        	link.addElement( getLabel() );
        }
		li.output( out );
	}
	protected void init() {
	}
	public String getIconSrc() {
		return iconSrc;
	}
	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
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

}
