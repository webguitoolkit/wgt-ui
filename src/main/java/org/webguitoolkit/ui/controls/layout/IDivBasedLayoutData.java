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
public interface IDivBasedLayoutData extends ILayoutData{
	String RIGHT = "right";
	String LEFT = "left";
	/**
	 * @param name the name of the TDs attribute
	 * @param value the attribute value
	 * @return the LayoutData
	 */
	IDivBasedLayoutData addCellAttribute( String name, String value );
	/**
	 * @param direction the floating of the element that follows
	 * @return the LayoutData
	 */
	IDivBasedLayoutData setCellFloat( String direction );
	/**
	 * @param direction the floating that should be cleared
	 * @return the LayoutData
	 */
	IDivBasedLayoutData setCellClear( String direction );
	/**
	 * @param style the cell style
	 * @return the LayoutData
	 */
	IDivBasedLayoutData setCellStyle( Style style);
	/**
	 * @param style the cell style as string
	 * @return the LayoutData
	 */
	IDivBasedLayoutData setCellStyle( String style);
	/**
	 * @param theClass the cells css class
	 * @return the LayoutData
	 */
	IDivBasedLayoutData setCellCssClass( String theClass );
}
