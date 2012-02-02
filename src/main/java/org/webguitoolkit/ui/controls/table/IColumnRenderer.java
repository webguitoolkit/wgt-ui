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

import java.io.Serializable;

import org.webguitoolkit.ui.ajax.IContext;

/**
 * <pre>
 * A ColumnRenderer defines how the cells in a column are rendered.
 * It generates the HTML code and is responsible of loading data into
 * the cells and clear empty cells.
 * </pre>
 */
public interface IColumnRenderer extends Serializable {

	/**
	 * the returned string is HTML to be placed inside the table cell.
	 * 
	 * @param col the current column
	 * @param cellId id of the cell
	 * @param idxRow row number
	 * @param idxCol column number
	 * @return the HTML code
	 */
	String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol);

	/**
	 * loads the data to the front end via context. Called by the table when scrolling or filling a the table with values from the
	 * model.
	 * 
	 * @param cellId id of the cell
	 * @param data the data object
	 * @param col the current column
	 * @param ctx the context
	 * @param idxRow row number
	 * @param idxCol column number
	 */
	void load(String cellId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol);

	/**
	 * empties the cell. Called by the table when there is no data for a cell to clean old values
	 * 
	 * @param cellId id of the cell
	 * @param col the current column
	 * @param ctx the context
	 * @param idxRow row number
	 * @param idxCol column number
	 */
	void clear(String cellId, ITableColumn col, IContext ctx, int idxRow, int idxCol);

	/**
	 * @param data the data object
	 * @param col the current column
	 * @return a string representation of the content for filtering. no html!
	 */
	String renderAsPlainText(Object data, ITableColumn col);
}
