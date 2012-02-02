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

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.controls.BaseControl;


/**
 * <h1>The BorderLayout displays the controls on a point of the compass defined position.</h1>
 * <pre>
 * ------------------------
 * |        north         |
 * ------------------------
 * | west | center | east |
 * ------------------------
 * |        south         |
 * ------------------------
 * </pre>
 * 
 * <b>Creation of a BorderLayout</b>
 * <pre>
 * 	Canvas canvas = factroy.createCanvas( parent );
 * 	// set the layout manager
 * 	canvas.setLayoutManager( new BorderLayout() );
 * 	
 * 	// create controls
 * 	IText text = factory.createText(viewConnector, "footer");
 * 	// set the position in the layout
 * 	text.setLayoutData( BorderLayout.south() );
 * 	text.setValue("Footer (South)");
 * 	
 * 	ILabel label = factory.createLabel(viewConnector, "Header (North)", text );
 * 	// set the position in the layout
 * 	label.setLayoutData( BorderLayout.north() );
 * 	label.getStyle().add("font-size", "20px");
 * 
 * 	text = factory.createText(viewConnector, "content");
 * 	// set the position in the layout
 * 	text.setLayoutData( BorderLayout.center() );
 * 	text.setValue("Content (Center)");
 * 
 * 	label = factory.createLabel(viewConnector, "Info (East)", text );
 * 	// set the position in the layout
 * 	label.setLayoutData( BorderLayout.east() );
 * 
 * 	label = factory.createLabel(viewConnector, "Menu (West)", text );
 * 	// set the position in the layout
 * 	label.setLayoutData( BorderLayout.west() );
 * </pre>
 * 
 * @author Martin
 *
 */
public class BorderLayout extends AbstractTableBasedLayout implements ILayout {
	
	private static final int COLS = 3;
	
	/**
	 * Base style for the HTML Table
	 */
	public static final String CSS_CLASS_TABLE = "wgtBorderLayoutTable";
	/**
	 * Base style for the HTML TR of the north row
	 */
	public static final String CSS_CLASS_NORTH_ROW = "wgtBorderLayoutNorthRow";
	/**
	 * Base style for the HTML TR of the center row
	 */
	public static final String CSS_CLASS_CENTER_ROW = "wgtBorderLayoutCenterRow";
	/**
	 * Base style for the HTML TR of the south row
	 */
	public static final String CSS_CLASS_SOUTH_ROW = "wgtBorderLayoutSouthRow";
	/**
	 * Base style for the HTML TD of the east row
	 */
	public static final String CSS_CLASS_EAST_CELL = "wgtBorderLayoutEastCell";
	/**
	 * Base style for the HTML TD of the center row
	 */
	public static final String CSS_CLASS_CENTER_CELL = "wgtBorderLayoutCenterCell";
	/**
	 * Base style for the HTML TD of the west row
	 */
	public static final String CSS_CLASS_WEST_CELL = "wgtBorderLayoutWestCell";

	
	public static final int NORTH = 0;
	public static final int WEST = 1;
	public static final int CENTER = 2;
	public static final int EAST = 3;
	public static final int SOUTH = 4;
	/**
	 * Creates a LayoutPosition object for the north cell of the BorderLayout
	 * <p>
	 * <b>Only the first data of the direction is evaluated for the cell style</b>
	 * </p>
	 * @return the LayoutPosition object
	 */
	public static ITableBasedLayoutData north(){
		return new BorderLayoutData(NORTH);
	}
	/**
	 * Creates a LayoutPosition object for the west cell of the BorderLayout
	 * <p>
	 * <b>Only the first data of the direction is evaluated for the cell style</b>
	 * </p>
	 * @return the LayoutPosition object
	 */
	public static ITableBasedLayoutData west(){
		return new BorderLayoutData(WEST);
	}
	/**
	 * Creates a LayoutPosition object for the center cell of the BorderLayout
	 * <p>
	 * <b>Only the first data of the direction is evaluated for the cell style</b>
	 * </p>
	 * @return the LayoutPosition object
	 */
	public static ITableBasedLayoutData center(){
		return new BorderLayoutData(CENTER);
	}
	/**
	 * Creates a LayoutPosition object for the east cell of the BorderLayout
	 * <p>
	 * <b>Only the first data of the direction is evaluated for the cell style</b>
	 * </p>
	 * @return the LayoutPosition object
	 */
	public static ITableBasedLayoutData east(){
		return new BorderLayoutData(EAST);
	}
	/**
	 * Creates a LayoutPosition object for the south cell of the BorderLayout
	 * <p>
	 * <b>Only the first data of the direction is evaluated for the cell style</b>
	 * </p>
	 * @return the LayoutPosition object
	 */
	public static ITableBasedLayoutData south(){
		return new BorderLayoutData(SOUTH);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayout#draw(java.util.List, java.io.PrintWriter )
	 * @param controls list of the controls to draw
	 * @param out the writer
	 */
	public void draw(List<BaseControl> controls, PrintWriter out ) {
		
		List<ILayoutElement> north = new ArrayList<ILayoutElement>();
		List<ILayoutElement> south = new ArrayList<ILayoutElement>();
		List<ILayoutElement> west = new ArrayList<ILayoutElement>();
		List<ILayoutElement> east = new ArrayList<ILayoutElement>();
		List<ILayoutElement> center = new ArrayList<ILayoutElement>();
		
		// HTML Table cells
		TD southTD = null;
		TD northTD = null;
		TD eastTD = null;
		TD westTD = null;
		TD centerTD = null;
		
		for (Iterator iter = controls.iterator(); iter.hasNext();) {
			ILayoutElement control = (ILayoutElement) iter.next();
			ILayoutData layoutData = control.getLayoutData();
			if( layoutData instanceof BorderLayoutData ){
				BorderLayoutData bld = (BorderLayoutData) layoutData;
				if( bld.getPosition() == SOUTH ){
					// get cell definition from first element
					if( southTD==null )
						southTD = bld.getTheCell();
					south.add(control);
				}
				else if( bld.getPosition() == NORTH ){
					// get cell definition from first element
					if( northTD==null )
						northTD = bld.getTheCell();
					north.add(control);
				}
				else if( bld.getPosition() == WEST ){
					// get cell definition from first element
					if( westTD==null ){
						westTD = bld.getTheCell();
						if( westTD.getAttribute("class") != null)
							westTD.setClass( CSS_CLASS_WEST_CELL+" "+westTD.getAttribute("class") );
						else
							westTD.setClass( CSS_CLASS_WEST_CELL );
					}
					west.add(control);
				}
				else if( bld.getPosition() == EAST ){
					// get cell definition from first element
					if( eastTD==null ){
						eastTD = bld.getTheCell();
						if( eastTD.getAttribute("class") != null)
							eastTD.setClass( CSS_CLASS_EAST_CELL+" "+eastTD.getAttribute("class") );
						else
							eastTD.setClass( CSS_CLASS_EAST_CELL );
					}
					east.add(control);
				}
				else if( bld.getPosition() == CENTER ){
					// get cell definition from first element
					if( centerTD==null ){
						centerTD = bld.getTheCell();
						if( centerTD.getAttribute("class") != null)
							centerTD.setClass( CSS_CLASS_CENTER_CELL+" "+centerTD.getAttribute("class") );
						else
							centerTD.setClass( CSS_CLASS_CENTER_CELL );
					}
					center.add(control);
				}
			}
			else{
				// center is default
				center.add(control);
			}
		}
		
		Table table = createTable(CSS_CLASS_TABLE);
		
		// draw north
		if( northTD != null ){
			TR northRow = createRow(table, CSS_CLASS_NORTH_ROW );
			northTD.setColSpan( COLS );
			northRow.addElement( northTD );
			
			drawCell(northTD, north); 
		}
		
		// draw center row
		TR centerRow = createRow(table, CSS_CLASS_CENTER_ROW );
		
		// draw west
		if( westTD == null )
			westTD = new TD();
		centerRow.addElement( westTD );
		drawCell(westTD, west); 
			
		// draw center
		if( centerTD == null )
			centerTD = new TD();
		centerRow.addElement( centerTD );
		drawCell(centerTD, center); 
		
		// draw east
		if( eastTD == null )
			eastTD = new TD();
		centerRow.addElement( eastTD );
		drawCell(eastTD, east); 

		// draw south
		if( southTD != null ){
			TR southRow = createRow(table, CSS_CLASS_SOUTH_ROW );
			southTD.setColSpan( COLS );
			southRow.addElement( southTD );
			
			drawCell(southTD, south); 
		}		
		table.output( out );
	}
}
/**
 * The position object for the BorderLayout
 * @author Martin
 */
class BorderLayoutData extends AbstractTableBasedLayoutData implements ITableBasedLayoutData{
	private final int position;
	
	/**
	 * Constructor
	 * @param position position in the layout (north, south, ...)
	 */
	public BorderLayoutData( int position ){
		super();
		this.position = position;
	}
	
	/**
	 * @return the position in the layout
	 */
	public int getPosition() {
		return position;
	}
}
