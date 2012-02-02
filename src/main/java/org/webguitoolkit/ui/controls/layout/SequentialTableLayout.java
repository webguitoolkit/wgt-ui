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
import java.util.Iterator;
import java.util.List;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.controls.BaseControl;

/**
 * <h1>SequentialTableLayout</h1>
 * <p>
 * This layout adds the controls sequential to a HTML table.
 * </p>
 * 
 * <pre>
 * ---------------------------------------------------
 * | add   | add   | add               | add (isLast) |
 * ---------------------------------------------------
 * | add   | add   | add append        | add (isLast) |
 * ---------------------------------------------------
 * | empty | empty | add emptyBefore 2 |
 * -------------------------------------
 * </pre>
 * <p>
 * It replaces the TableLayout.
 * <p>
 * 
 * @author Martin
 * 
 */
public class SequentialTableLayout extends AbstractTableBasedLayout implements ILayout {

	/**
	 * Base style for the HTML Table
	 */
	public static final String CSS_CLASS_TABLE = "wgtSequentialTableLayoutTable";
	/**
	 * Base style for the HTML TR
	 */
	public static final String CSS_CLASS_ROW = "wgtSequentialTableLayoutRow";

	/**
	 * LayoutData to append a control to the last cell
	 */
	public static final ILayoutData APPEND = new ILayoutData() {};

	/**
	 * Creates a LayoutPosition object for SequentialLayout
	 * 
	 * @param emptyCellsBefore empty cells that have to be added before the control
	 * @param emptyCellsAfter empty cells that have to be added after the control
	 * @param isLast if it is the last control in the row
	 * @return the layout position object
	 */
	public static ITableBasedLayoutData getLayoutData(int emptyCellsBefore, int emptyCellsAfter, boolean isLast) {
		return new SequentialLayoutData(emptyCellsBefore, emptyCellsAfter, isLast);
	}

	/**
	 * Creates a LayoutPosition object for SequentialLayout
	 * 
	 * @return the layout position object
	 */
	public static ITableBasedLayoutData getLayoutData() {
		return new SequentialLayoutData(0, 0, false);
	}

	/**
	 * Creates a LayoutPosition object for SequentialLayout
	 * 
	 * @return the layout position object
	 */
	public static ITableBasedLayoutData getLastInRow() {
		return new SequentialLayoutData(0, 0, true);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayout#draw(java.util.List, java.io.PrintWriter)
	 * @param controls list of the controls to draw
	 * @param out the writer
	 */
	public void draw(List<BaseControl> controls, PrintWriter out) {

		// use table as HTML representation
		Table table = createTable(CSS_CLASS_TABLE);
		TR currentRow = createRow(table, CSS_CLASS_ROW);
		TD currentCell = null;

		// iterate over controls
		for (Iterator iter = controls.iterator(); iter.hasNext();) {
			BaseControl control = (BaseControl)iter.next();
			ILayoutData pos = control.getLayoutData();

			// if append add to current cell
			if (pos == APPEND) {
				if( currentCell == null )
					currentCell = new TD();
				drawCell(currentCell, control);
			}
			// if SequentialLayoutPosition, do what is defined in the object
			else if (pos instanceof SequentialLayoutData) {

				if (((SequentialLayoutData)pos).getEmptyCellsBefore() > 0) {
					for (int i = 0; i < ((SequentialLayoutData)pos).getEmptyCellsBefore(); i++) {
						TD empty = new TD();
						currentRow.addElement(empty);
					}
				}

				currentCell = ((SequentialLayoutData)pos).getTheCell();
				currentRow.addElement(currentCell);
				drawCell(currentCell, control);

				if (((SequentialLayoutData)pos).getEmptyCellsAfter() > 0) {
					for (int i = 0; i < ((SequentialLayoutData)pos).getEmptyCellsBefore(); i++) {
						TD empty = new TD();
						currentRow.addElement(empty);
					}
				}

				// if last add a new row (TR)
				if (((SequentialLayoutData)pos).isLast()) {
					currentRow = createRow(table, CSS_CLASS_ROW);
				}
			}
			// simple sequence for all others
			else {
				currentCell = new TD();
				currentRow.addElement(currentCell);
				drawCell(currentCell, control);
			}
		}
		table.output(out);
	}
}

/**
 * layout position object used in the sequential layout
 * 
 * @author Martin
 */
class SequentialLayoutData extends AbstractTableBasedLayoutData implements ILayoutData, ITableBasedLayoutData {

	private final int emptyCellsBefore;
	private final int emptyCellsAfter;
	private final boolean isLast;

	/**
	 * Constructor
	 * 
	 * @param emptyCellsBefore empty cells that have to be added before the control
	 * @param emptyCellsAfter empty cells that have to be added after the control
	 * @param isLast if it is the last element in the row
	 */
	public SequentialLayoutData(int emptyCellsBefore, int emptyCellsAfter, boolean isLast) {
		super();
		this.emptyCellsBefore = emptyCellsBefore;
		this.emptyCellsAfter = emptyCellsAfter;
		this.isLast = isLast;
	}

	/**
	 * @return the empty cells that have to be added before the control
	 */
	protected int getEmptyCellsBefore() {
		return emptyCellsBefore;
	}

	/**
	 * @return the empty cells that have to be added after the control
	 */
	protected int getEmptyCellsAfter() {
		return emptyCellsAfter;
	}

	/**
	 * @return true if it is the last element in a row
	 */
	public boolean isLast() {
		return isLast;
	}
}
