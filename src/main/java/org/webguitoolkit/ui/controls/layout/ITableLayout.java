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

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.ILabel;

/** * <h1>Interface for a TableLayout</h1>
 * <p>
 * The TableLayout is the "traditional" way of arranging the controls on a container.<br>
 * Additional Layout types are BorderLayout, GridLayout, SequentialTableLayoutand  SequentialDivLayout.
 * </p>
 * Each component added to the TableLayout creates a new cell in the layout row. 
 * It is possible to add more than one component to a TableLayout cell or to create a newRow. 
 * <p/>
 * <b>Creation of a Text field</b><br>
 * <pre>
 *		ITableLayout layout = factory.createTableLayout(viewConnector);
 *		//add labels to first TableLayout row
 *		factory.createLabel(layout, "first row, first cell" );
 *		factory.createLabel(layout, "first row, second cell" );
 *		//creates a new TalbeLayout row
 *		layout.newRow();
 *		//add labels to second TableLayout row
 *		factory.createLabel(layout, "second row, first cell" );
 *		factory.createLabel(layout, "second row, second cell" );
 *		//creates a new TalbeLayout row
 *		layout.newRow();
 *		//add labels to third TableLayout row
 *		factory.createLabel(layout, "third row, first cell" );
 *		ILabel l = new Label("third row, first cell");
 *		//appends a component to the current cell
 *		layout.appendToCell(l);
 *		factory.createLabel(layout, "third row, second cell" );
 * </pre>
 * <p/>
 * <b>Event handling</b><br>
 * The TableLayout can't trigger events, therefore no listener can be added.<br>
 * <p/>
 * <b>CSS classes:</b> none
 * 
 * @author Peter
 * @author Lars
 * 
 */
public interface ITableLayout extends IBaseControl, IComposite {

	/**
	 * adds a Cell to the table. You need to construct the TD-element yourself, then it is added with the correct
	 * methods, so internal references for row and cells don't get confused. Don't add cells manually.
	 * 
	 * @param cell cell to be created
	 * @return the created cell
	 */
	 TD addCell(TD cell);

	/**
	 * just add a new line into the grid layout, all following components will be added to a new TR element. currentRow()
	 * changes.
	 * 
	 * @return the new row
	 */
	 ITableLayout newRow();
	
	/**
	 * @return enable chain style programming
	 * @deprecated use newRow()
	 */
	 ITableLayout newLine();

	/**
	 * was : public TableLayout addWithRef(FormControl control, Label label);
	 * 
	 * @param control the control
	 * @param label referenced label
	 * @return enable chain style programming
	 */
	 ITableLayout addWithRef(IFormControl control, ILabel label);

	/**
	 * was : public TableLayout appendToCurrentCell(BaseControl comp);
	 * 
	 * @param control the appendix
	 * @return the cell itself to enable chain style programming
	 */
	 ITableLayout appendToCell(IBaseControl control);

	/**
	 * 
	 * @param layoutMode true = ?
	 */
	 void setLayoutMode(boolean layoutMode);

	/**
	 * 
	 * @return the ne cell
	 */
	 TD addEmptyCell();

	/**
	 * @return the current cell in process
	 */
	 TD getCurrentCell();
	 
	 
		/**
		 * @return the current row in process
		 */
		 TR getCurrentRow();
	
	/**
	 * 
	 * @param comp the appendix
	 * @return the layout itself to enable chain style programming
	 * @deprecated use appendToCell instead
	 */
	 TableLayout appendToCurrentCell(IBaseControl comp);
	
	/**
	 * @return base ESC table
	 */
	 Table getEcsTable();

}