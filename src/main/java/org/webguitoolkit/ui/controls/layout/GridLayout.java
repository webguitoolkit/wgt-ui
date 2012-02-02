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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.html.HTMLTag;
import org.webguitoolkit.ui.html.XHTMLTagFactory;


/**
 * <h1>The GridLayout displays the controls on a x/y defined position.</h1>
 * <pre>
 * -------------------------
 * | 0,0 | 1,0 | 2,0 | 3,0 |
 * -------------------------
 * | 0,1 | 1,1 | 2,1 | 3,1 |
 * -------------------------
 * | 0,2 | 1,2 | 2,2 | 3,2 |
 * -------------------------
 * </pre>
 * 
 * <b>Creation of a GridLayout</b>
 * <pre>
 * 	Canvas canvas = factroy.createCanvas( parent );
 * 	// set the layout manager
 * 	canvas.setLayoutManager( new GridLayout() );
 * 	
 * 	// create controls
 * 	IText text = factory.createText(viewConnector, "test1");
 * 	// set the position in the layout
 * 	text.setLayoutPosition( GridLayout.getLayoutPosition(1, 0) );
 * 	text.setValue("Pos 1 0");
 * 	
 * 	ILabel label = factory.createLabel(viewConnector, "Pos 0 0", text );
 * 	// set the position in the layout
 * 	label.setLayoutPosition( GridLayout.getLayoutPosition(0, 0));
 * 
 *  text = factory.createText(viewConnector, "text2");
 * 	// set the position in the layout
 * 	text.setLayoutPosition( GridLayout.getLayoutPosition(3, 1));
 * 	text.setValue("Pos 3 1");
 * 
 * 	label = factory.createLabel(viewConnector, "Pos 2 1", text );
 * 	// set the position in the layout
 * 	label.setLayoutPosition( GridLayout.getLayoutPosition(2, 1));
 * </pre>
 * 
 * @author Martin
 *
 */
public class GridLayout extends AbstractTableBasedLayout implements ILayout {
	
	/**
	 * Base style for the HTML Table
	 */
	public static final String CSS_CLASS_TABLE = "wgtGridLayoutTable";
	/**
	 * Base style for the HTML TR
	 */
	public static final String CSS_CLASS_ROW = "wgtGridLayoutRow";

	/**
	 * Creates a LayoutPosition object for the GridLayout
	 * @param col the x position starting with 0
	 * @param row the y position starting with 0
	 * @return the LayoutPosition object
	 */
	public static ITableBasedLayoutData getLayoutData( int col, int row ){
		return new GridLayoutData( col, row );
	}
	
	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayout#draw(java.util.List, java.io.PrintWriter )
	 * @param controls list of the controls to draw
	 * @param out the writer
	 */
	public void draw(List<BaseControl> controls, PrintWriter out ) {

		// sort controls to the order they are placed
		List sorted = new ArrayList( controls );
		Collections.sort( sorted, new GridLayoutComparator() );
		
		// temp variables
		int currentY = -1;
		int currentX = -1;

		// create the ECS table
//		Table table = createTable( CSS_CLASS_TABLE );
		HTMLTag table = XHTMLTagFactory.getInstance().newTable( null, null);
		
		HTMLTag currentRow = XHTMLTagFactory.getInstance().newTr(null, null);
		HTMLTag currentCell = XHTMLTagFactory.getInstance().newTd(null, null);
		
		// iterate over the sorted controls and draw them
		for (Iterator iter = sorted.iterator(); iter.hasNext();) {
			BaseControl control = (BaseControl) iter.next();
			ILayoutData pos = control.getLayoutData();
			if ( pos instanceof GridLayoutData ){
				GridLayoutData gridData = (GridLayoutData) pos;
				int y = gridData.getY();
				int x = gridData.getX();
				if( y != currentY ){// if the row has changed add the amount of rows that are missing
					for( int i = 0; i < y - currentY; i ++ ){
						currentRow = createRow( table, CSS_CLASS_ROW );
						currentX = -1;
					}
				}
				if( x > currentX+1 ){// add empty cells
					for( int i = 0; i < x - currentX -1; i ++ ){
						currentCell = XHTMLTagFactory.getInstance().newTd(currentRow, null);
					}
				}
				if( x!=currentX || y != currentY ){// create the cell
					currentCell = gridData.getTheHTMLCell();
				}
				currentY = y;
				currentX = x;
				
				currentCell.setParent(currentRow);
			}
			else { // place all other in a the last row
				if( currentY > 0 ){
					currentY = -1;
					currentX = 0;
					currentRow = XHTMLTagFactory.getInstance().newTr(table,null);
				}
				currentCell = XHTMLTagFactory.getInstance().newTd(currentCell,null);
			}
			drawCell(currentCell, control );
		}
		table.output( out );
	}
	/**
	 * Comperator for sorting LayoutPositions
	 * @author Martin
	 */
	private class GridLayoutComparator implements Comparator{

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 * @param control1 first control
		 * @param control2 second control
		 * @return positive values if the first is higher, negative if the second, 0 if the same
		 */
		public int compare(Object control1, Object control2) {
			ILayoutElement l1 = (ILayoutElement) control1;
			ILayoutElement l2 = (ILayoutElement) control2;
			GridLayoutData gl1;
			GridLayoutData gl2;
			if( l1.getLayoutData() instanceof GridLayoutData )
				gl1 = (GridLayoutData) l1.getLayoutData();
			else
				return 1;

			if( l2.getLayoutData() instanceof GridLayoutData )
				gl2 = (GridLayoutData) l2.getLayoutData();
			else
				return -1;

			if( gl1.getY() == gl2.getY() )
				return new Integer(gl1.getX()).compareTo( new Integer(gl2.getX()));
			else
				return new Integer(gl1.getY()).compareTo( new Integer(gl2.getY()));
		}
		
	}
}

/**
 * The position object for the GridLayout
 * @author Martin
 */
class GridLayoutData extends AbstractTableBasedLayoutData implements ITableBasedLayoutData{
	private final int x;
	private final int y;

	/**
	 * Constructor
	 * @param x x position -
	 * @param y y position |
	 */
	public GridLayoutData( int x, int y ){
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y position
	 */
	public int getY() {
		return y;
	}
}
