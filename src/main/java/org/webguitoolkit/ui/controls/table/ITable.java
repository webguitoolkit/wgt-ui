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
package org.webguitoolkit.ui.controls.table;

import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.util.export.TableExportOptions;

/**
 * <h1>Interface for the standard table.</h1>
 * <p>
 * The table is a control to display data tabular.<br>
 * The control comprises the possibility to filter and sort the data (configurable per column), export data into excel and pdf and
 * customize the columns displayed on the front end.
 * </p>
 * <p>
 * The table uses a table model to organize its content data. The DefaultTableModel contains all the features that a normal data
 * table needs.
 * </p>
 * <h2>Event handling</h2> The click on a row can trigger a event, handled by a ITableListener <h2>Creation of a table</h2>
 * 
 * <pre>
 * ITable table = factory.createTable(layout, &quot;Table Sample&quot;, 5);
 * table.setListener(new TableListener());
 * // create a column for the table
 * factory.createTableColumn(table, &quot;Name&quot;, &quot;name&quot;, true);
 * factory.createTableColumn(table, &quot;Description&quot;, &quot;description&quot;, true);
 * // create data
 * List&lt;IDataBag&gt; data = new ArrayList&lt;IDataBag&gt;();
 * for (int i = 0; i &lt; 10; i++) {
 * 	IDataBag bag = new DataBag(null);
 * 	bag.addProperty(&quot;name&quot;, &quot;name &quot; + i);
 * 	bag.addProperty(&quot;description&quot;, &quot;description &quot; + i);
 * 	data.add(bag);
 * }
 * table.getDefaultModel().setTableData(data);
 * // send data to the front end
 * table.reload();
 * </pre>
 * 
 * <h2>Export table data</h2> this generates to buttons in the table header to export the table data
 * 
 * <pre>
 * TableExportOptions export = new TableExportOptions();
 * export.setExportAsExcel(true);
 * export.setExcelButtonLabelText(&quot;Excel Export&quot;);
 * export.setExcelSheetName(&quot;ExportShhet&quot;);
 * export.setExportAsPDF(true);
 * export.setPdfButtonLabelText(&quot;PDF Export&quot;);
 * table.setExportOptions(export);
 * </pre>
 * <p>
 * <b>CSS class</b> : rowCssClass, rowCssClassAlternate, wgtButton2, wgtFilterTable, wgtFilterText, wgtInlineTableFilter,
 * wgtInputCheckbox, wgtInputText, wgtMessageButton, wgtPointerCursor, wgtScrollIcon, wgtSortIcon, wgtTableColumnHeaderCell,
 * wgtTableColumnHeaderCellSorted, wgtTableEditCell, wgtTableEditDiv, wgtTableEditHeaderCell, wgtTableEditRowsRow, wgtTableFilter,
 * wgtTableFilterCell, wgtTableFooter, wgtTableFooterInputText, wgtTableFooterText, wgtTableHeader, wgtTableHeaderButtons,
 * wgtTableHeaderLeftofButtons, wgtTDNormal, wgtTRAlternate, wgtTRNormal
 * </p>
 * 
 * @author Peter
 * 
 * @see org.webguitoolkit.ui.controls.table.ITableModel
 * @see org.webguitoolkit.ui.controls.table.ITableListener
 * 
 */
public interface ITable extends IBaseControl {
	/**
	 * constant for display mode: scroll able table
	 */
	String DISPLAY_MODE_SCROLLBAR = "SCROLLBAR";

	/**
	 * constant for display mode: table with scroll buttons
	 */
	String DISPLAY_MODE_SCROLL_BUTTONS = "SCROLL_BUTTONS";

	/**
	 * constant for display mode: table without scrolling
	 */
	String DISPLAY_MODE_NO_SCROLL = "NO_SCROLL";

	/**
	 * sets the columns for the table.
	 * 
	 * @param columns list of ITableColumn
	 */
	void setColumns(List<ITableColumn> columns);

	/**
	 * sets the drop down filters on top of the table
	 * 
	 * @param filters list if ITableFilters
	 */
	void setFilters(List<ITableFilter> filters);

	/**
	 * sort the data by column i prefer using sort(String) if the user can change order of columns
	 * 
	 * @param i column to sort (first column = 0)
	 */
	void sort(int i);

	/**
	 * sort the data by column i with specified direction
	 */
	void sort(int i, boolean ascending);

	/**
	 * sort the data by column property
	 * 
	 * @param propertyOfColumn name of the column property
	 */
	void sort(String propertyOfColumn);

	/**
	 * this changes the selection A value of -1 deselects the highlight in the table.
	 * 
	 * @param newSelection in number of application rows
	 * @param callListener if the TableListener has to be called
	 */
	public void selectionChange(int newSelection, boolean callListener);

	/**
	 * @deprecated use selectionChange(int newSelection, boolean callListener )
	 * 
	 *             this changes the selection. A value of -1 deselects the highlight in the table.
	 * 
	 * @param newSelection number of the row to select
	 */
	void selectionChange(int newSelection);

	/**
	 * sets the number of data rows to display in the table
	 * 
	 * @param rows number of rows
	 */
	void setRows(int rows);

	/**
	 * sets the table model
	 * 
	 * @param model to set
	 */
	void setModel(ITableModel model);

	/**
	 * title of the table if null no title will be displayed
	 * 
	 * @param title string
	 */
	void setTitle(String title);

	/**
	 * title of the table using TextService the key should be available in language properties
	 * 
	 * @param titleKey the resource key
	 */
	void setTitleKey(String titleKey);

	/**
	 * this saves the information entered in the Table to the DataBag of the underlying DefaultTableMode. If you are not using the
	 * DefaultTableModel with DataBag, call internalUnload to transport the data into your model.<br>
	 * <b>The saveXXX methods can be only used if you are using the DefaultTableModel and DataBags.</b><br>
	 * <p>
	 * Keep in mind that rows may get transported into the DataBag if the table is being scrolled.<br>
	 * Don't forget to ask the surrounding compound for any errors.
	 * </p>
	 */
	void save2Bag();

	/**
	 * this transports the data from the DataBag into the delegates. It is actually an operation on the DefaultTableModel.<br>
	 * <b>The saveXXX methods can be only used if you are using the DefaultTableModel and DataBags.</b><br>
	 * 
	 * It just runs through the list of rows and calls everywhere bag.save2Delegate().
	 */
	void save2Delegate();

	/**
	 * this calls save2Bag and save2delegate to transport the edited table right into the DefaultModel.<br>
	 * <b>The saveXXX methods can be only used if you are using the DefaultTableModel and DataBags.</b>
	 */
	void save();

	/**
	 * sets the css class for a table row
	 * 
	 * @param rowCssClass name of the css class (default: rowCssClass)
	 */
	void setRowCssClass(String rowCssClass);

	/**
	 * sets the css class for a table row
	 * 
	 * @param rowCssClassAlternate name of the css class (default: rowCssClassAlternate)
	 */
	void setRowCssClassAlternate(String rowCssClassAlternate);

	/**
	 * sets if a event should be triggered when users clicks on table row
	 * 
	 * @param fireRowSelectEvent false if no event should be triggered (default: true)
	 */
	void setFireRowSelectEvent(boolean fireRowSelectEvent);

	/**
	 * sets the listener for the table
	 * 
	 * @param listener to handle events
	 */
	void setListener(ITableListener listener);

	/**
	 * adds a column to the current list
	 * 
	 * @param column to add
	 */
	void addColumn(ITableColumn column);

	/**
	 * adds a filter to the current list
	 * 
	 * @param filter to add
	 */
	void addFilter(ITableFilter filter);

	/**
	 * adds a column with a checkbox to the table (including select all checkbox)
	 * 
	 * @param checkboxProperty name of the property in the DataBag
	 * @param titelKey text key for column title
	 * @return
	 */
	ITableColumn addCheckboxColumn(String checkboxProperty, String titelKey);

	/**
	 * adds a column with a checkbox to the table (including select all checkbox)
	 * 
	 * @param checkboxProperty name of the property in the DataBag
	 * @param titelKey text key for column title
	 * @param chRenderer false if no checkbox column renderer should be used
	 * @return
	 * 
	 *         why use checkbox column without checkboxcolumnrenderer??? -- because you want to attach your own renderer.
	 * 
	 */
	ITableColumn addCheckboxColumn(String checkboxProperty, String titelKey, boolean chRenderer);

	/**
	 * configures if display of columns and rows can be edited by user
	 * 
	 * @param editable true if user can edit config
	 */
	void setEditable(boolean editable);

	/**
	 * name of the property displayed while dragging row
	 * 
	 * @param displayProperty name
	 */
	void setDraggable(String displayProperty);

	/**
	 * enables the table to be a drop-zone
	 */
	void setDroppable();

	/**
	 * sets the display mode of the table<br>
	 * This method can only be called in the creation phase.<br>
	 * 
	 * @param displayMode - use constants defined in ITable
	 */
	void setDisplayMode(String displayMode);

	/**
	 * adds a row handler to the table
	 * 
	 * @param rowHandler to add
	 */
	void addRowHandler(IRowHandler rowHandler);

	/**
	 * adds a button to the table header
	 * 
	 * @param button to add
	 */
	void addButton(IButton button);

	/**
	 * adds a FormControl
	 * 
	 * @param formControl to add
	 */
	void addCellControl(IFormControl formControl);

	/**
	 * 
	 * @param pdf true = enable pdf
	 * @param excel true = enable excel
	 * 
	 * @deprecated use setExportOptions
	 */
	void addExportButton(boolean pdf, boolean excel);

	/**
	 * Returns the TableExportOptions of the table or null. With the TableExportOptions you can enable the table to over buttons
	 * for exporting table data as PDF and Excel
	 * 
	 * @return exportOptions for table
	 */
	TableExportOptions getExportOptions();

	/**
	 * With the TableExportOptions you can enable the table to over buttons for exporting table data as PDF and Excel
	 * 
	 * @param exportOptions for table
	 */
	void setExportOptions(TableExportOptions exportOptions);

	/**
	 * get the table model should only be called if your table uses the DefaultTable model!
	 * 
	 * @return the table model casted to DefaultTableModel
	 */
	DefaultTableModel getDefaultModel();

	/**
	 * reload informs the table-component, that the model has canged, and it should not assume that the filers stayed the same.
	 * the data will be send to front end
	 */
	void reload();

	void reload(int firstRow);

	/**
	 * gets the table model, if no model set a DefaultTableModel will be returned
	 * 
	 * @return the tables model
	 */
	ITableModel getModel();

	/**
	 * gets the data object of the selected row
	 * 
	 * @return IDataBag of selected row
	 */
	IDataBag getSelectedRow();

	/**
	 * convenience to get the information which is the lead selection of the table. This is the index in the model's list
	 * (getFilteredList, if you are using the defaultTableModel).
	 * 
	 * @return -1 if no selection
	 */
	int getSelectedRowIndex();

	/**
	 * gets the data object
	 * 
	 * @param row the row to get data bag from
	 * @return IDataBag of the specified row
	 */
	IDataBag getRow(int row);

	/**
	 * get the default ColumnRenderer of the table
	 * 
	 * @return the ColumnRenderer
	 */
	IColumnRenderer getDefaultRenderer();

	/**
	 * asks the model to return the data for the view and packs the data into the context.
	 */
	void load();

	/**
	 * asks the model to return the data for the view and packs the data into the context.
	 * 
	 * @param offset the offset of the tables content
	 */
	void load(int offset);

	/**
	 * Load a column configuration to the table. A configuration contains a list of the visible columns (property) separated by
	 * ';'. Optionally the initial width can be specified (separated by ','): Example: "name,50px;forename;address,200px;"
	 * 
	 * @param config
	 */
	void loadColumnConfig(String config);

	/**
	 * convenience to get the information which is the first row shown
	 * 
	 * @return the index of the first row
	 */
	int getFirstRow();

	/**
	 * returns the offset of the tables scope
	 * 
	 * @return the offset
	 */
	int getOffset();

	/**
	 * Returns the current column configuration as string so that it can be loaded again if the user visits this page the next
	 * time.
	 * 
	 * @return the current configuration of the table as string
	 * @see ITable#loadColumnConfig(String)
	 */
	String getColumnConfig();

	/**
	 * convenience method. It is assumed, that you are using DefaultTableModel. This removes a row and reloads the table
	 * (including reordering and sorting).
	 * 
	 * @param removeObject the object to be removed.
	 * @return true if the entry was found and removed from the list.
	 */
	boolean removeAndReload(Object removeObject);

	/**
	 * convenience method. It is assumed, that you are using DefaultTableModel. This Method adds a new entry after sorting and
	 * applying filters to the table model so it is displayed at first position.
	 * 
	 * @param newObject the object to be added to the table.
	 */
	void addAndReload(Object newObject);

	/**
	 * number of rows actually on the screen may be less than the visible size since the model may not have enough data.
	 * 
	 * @return number of active rows on screen
	 */
	int getRowsLoaded();

	Compound getRowCompound();

	TableColumn getCurrentColumn();

	/**
	 * change the readOnly mode of the entire table, resulting in changing the readOnly mode of all table rows with form elements
	 */
	void changeMode(int mode);
}
