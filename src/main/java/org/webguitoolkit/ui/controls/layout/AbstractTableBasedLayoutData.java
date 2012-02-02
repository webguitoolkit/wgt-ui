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
package org.webguitoolkit.ui.controls.layout;

import java.util.Enumeration;

import org.apache.ecs.html.TD;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.html.HTMLTag;
import org.webguitoolkit.ui.html.HTMLUtil;
import org.webguitoolkit.ui.html.XHTMLTagFactory;

/**
 * Base class for LayoutData objects used in Table Layouts
 * 
 * @author Martin
 */
public class AbstractTableBasedLayoutData implements ILayoutData, ITableBasedLayoutData{

	private final TD theCell;
	
	/**
	 * Constructor
	 */
	public AbstractTableBasedLayoutData(){
		this.theCell = new TD();
	}	

	/**
	 * @return the cell
	 */
	protected TD getTheCell(){
		return (TD) theCell.clone();
	}
	
	/**
	 * @return the cell
	 */
	protected HTMLTag getTheHTMLCell(){
		HTMLTag cell = XHTMLTagFactory.getInstance().newTd(null, null);
		Enumeration<String> attributes = theCell.attributes();
		while( attributes.hasMoreElements() ){
			String attrib = attributes.nextElement();
			cell.addAttribute(HTMLUtil.getHTMLAttribute(attrib), theCell.getAttribute(attrib));
		}
		return cell;
	}
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ITableBasedLayoutData#addCellAttribute(java.lang.String, java.lang.String)
	 * @param name the name of the TDs attribute
	 * @param value the attribute value
	 * @return the LayoutPosition
	 */
	public ITableBasedLayoutData addCellAttribute( String name, String value ){
		theCell.addAttribute(name, value);
		return this;
	}
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ITableBasedLayoutData#setCellColSpan(int)
	 * @param colspan the number of columns to span over
	 * @return the LayoutPosition
	 */
	public ITableBasedLayoutData setCellColSpan( int colspan ){
		theCell.setColSpan( colspan );
		return this;
	}
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ITableBasedLayoutData#setCellRowSpan(int)
	 * @param rowspan the number of rows to span over
	 * @return the LayoutPosition
	 */
	public ITableBasedLayoutData setCellRowSpan( int rowspan ){
		theCell.setRowSpan( rowspan );
		return this;
	}
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ITableBasedLayoutData#setCellStyle(org.webguitoolkit.ui.controls.util.style.Style)
	 * @param style the cell style
	 * @return the LayoutPosition
	 */
	public ITableBasedLayoutData setCellStyle( Style style){
		theCell.setStyle( style.getOutput() );
		return this;
	}
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ITableBasedLayoutData#setCellStyle(java.lang.String)
	 * @param style the cell style as string
	 * @return the LayoutPosition
	 */
	public ITableBasedLayoutData setCellStyle( String style){
		theCell.setStyle( style );
		return this;
	}
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ITableBasedLayoutData#setCellCssClass(java.lang.String)
	 * @param theClass the cells css class
	 * @return the LayoutPosition
	 */
	public ITableBasedLayoutData setCellCssClass( String theClass ){
		theCell.setClass( theClass );
		return this;
	}
}
