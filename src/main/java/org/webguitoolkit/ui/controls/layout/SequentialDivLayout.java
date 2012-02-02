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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ecs.html.Div;
import org.webguitoolkit.ui.controls.AbstractLayout;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * This layout adds the controls sequential to the container.
 * 
 * @author Martin
 */
public class SequentialDivLayout extends AbstractLayout implements ILayout {

	/**
	 * Creates a LayoutPosition object for SequentialLayout
	 * @return the layout position object
	 */
	public static IDivBasedLayoutData getLayoutData(){
		return new SequentialDivPosition();
	}

	
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayout#draw(java.util.List, java.io.PrintWriter)
	 * @param controls list of the controls to draw
	 * @param out the writer
	 */
	public void draw(List<BaseControl> controls, PrintWriter out) {
		
		List divs = new ArrayList();
		
		// iterate over controls
		for (Iterator iter = controls.iterator(); iter.hasNext();) {
			BaseControl control = (BaseControl) iter.next();
			ILayoutData pos = control.getLayoutData();
			
			// if SequentialDivPosition, do what is defined in the object
			if ( pos instanceof SequentialDivPosition ){
				Div div = ((SequentialDivPosition)pos).getCell();
				drawCell(div, control );
				div.output( out );
			}
			//simple sequence for all others
			else { 
				Div div = new Div();
				drawCell(div, control );
				div.output( out );
			}
		}
	}
}

/**
 * layout position object used in the sequential div layout
 * 
 * @author Martin
 */
class SequentialDivPosition implements IDivBasedLayoutData{

	private final Div theCell;
	private String floating = null;
	private String clearing = null;
	private String style = null;
	
	/**
	 * Constructor
	 */
	public SequentialDivPosition(){
		theCell = new Div();
	}
	/**
	 * @param name the name of the TDs attribute
	 * @param value the attribute value
	 * @return the LayoutData
	 * 
	 * @see org.webguitoolkit.ui.controls.layout.IDivBasedLayoutData#addCellAttribute(java.lang.String, java.lang.String)
	 */
	public IDivBasedLayoutData addCellAttribute(String name, String value) {
		theCell.addAttribute(name, value);
		return this;
	}

	/**
	 * @param direction the floating that should be cleared
	 * @return the LayoutData
	 * 
 	 * @see org.webguitoolkit.ui.controls.layout.IDivBasedLayoutData#setCellClear(java.lang.String)
	 */
	public IDivBasedLayoutData setCellClear(String direction) {
		if( direction!=null )
			clearing = "clear: " + direction + ";";
		else
			clearing = null;
		setCellStyle();
		return this;
	}

	/**
	 * @param theClass the cells css class
	 * @return the LayoutData
	 * 
	 * @see org.webguitoolkit.ui.controls.layout.IDivBasedLayoutData#setCellCssClass(java.lang.String)
	 */
	public IDivBasedLayoutData setCellCssClass(String theClass) {
		theCell.setClass( theClass );
		return this;
	}

	/**
	 * @param direction the floating of the element that follows
	 * @return the LayoutData
	 * 
	 * @see org.webguitoolkit.ui.controls.layout.IDivBasedLayoutData#setCellFloat(java.lang.String)
	 */
	public IDivBasedLayoutData setCellFloat(String direction) {
		if( direction!=null )
			floating = "float: " + direction + ";";
		else
			floating = null;
		setCellStyle();
		return this;
	}

	/**
	 * @param style the cell style
	 * @return the LayoutData
	 * 
	 * @see org.webguitoolkit.ui.controls.layout.IDivBasedLayoutData#setCellStyle(org.webguitoolkit.ui.controls.util.style.Style)
	 */
	public IDivBasedLayoutData setCellStyle(Style style) {
		return setCellStyle( style.getOutput() );
	}

	/**
	 * @param style the cell style as string
	 * @return the LayoutData
	 * 
	 * @see org.webguitoolkit.ui.controls.layout.IDivBasedLayoutData#setCellStyle(java.lang.String)
	 */
	public IDivBasedLayoutData setCellStyle(String style) {
		this.style = style;
		if( !style.trim().endsWith(";"))
			this.style += ";";
		setCellStyle();
		return this;
	}

	/**
	 * internal setting the style
	 */
	private void setCellStyle() {
		String theStyle = "";
		if( style != null )
			theStyle += style;
		if( floating != null )
			theStyle += floating;
		if( clearing != null )
			theStyle += clearing;
		
		theCell.setStyle( theStyle );
	}

	/**
	 * @return the HTML div element
	 */
	protected Div getCell(){
		return theCell;
	}

}


