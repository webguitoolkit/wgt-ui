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

import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * <h1>The LayoutPosition for SequentialLayouts</h1> 
 *
 */
public interface ITableBasedLayoutData extends ILayoutData{
	/**
	 * @param name the name of the TDs attribute
	 * @param value the attribute value
	 * @return the LayoutPosition
	 */
	ITableBasedLayoutData addCellAttribute( String name, String value );
	/**
	 * @param colspan the number of columns to span over
	 * @return the LayoutPosition
	 */
	ITableBasedLayoutData setCellColSpan( int colspan );
	/**
	 * @param rowspan the number of rows to span over
	 * @return the LayoutPosition
	 */
	ITableBasedLayoutData setCellRowSpan( int rowspan );
	/**
	 * @param style the cell style
	 * @return the LayoutPosition
	 */
	ITableBasedLayoutData setCellStyle( Style style);
	/**
	 * @param style the cell style as string
	 * @return the LayoutPosition
	 */
	ITableBasedLayoutData setCellStyle( String style);
	/**
	 * @param theClass the cells css class
	 * @return the LayoutPosition
	 */
	ITableBasedLayoutData setCellCssClass( String theClass );
}
