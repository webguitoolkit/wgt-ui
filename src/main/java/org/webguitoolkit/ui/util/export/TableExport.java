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
package org.webguitoolkit.ui.util.export;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.renderer.CheckboxColumnRenderer;

/**
 * @author i01002487
 * 
 */
abstract public class TableExport implements Serializable, ITableExport {

	protected String exportType;

	public TableExport(String exportType) {
		this.exportType = exportType;
	}

	protected List<ITableColumn> getTableColumns(boolean onlyDisplayed, Table table) {
		List<ITableColumn> sortedListOfColumns = new LinkedList<ITableColumn>();

		for (ITableColumn col : table.getColumns()) {
			if (showColumn(onlyDisplayed, col)) {
				sortedListOfColumns.add(col);
			}
		}

		return sortedListOfColumns;
	}

	/**
	 * Don't show a column if it is
	 * <ul>
	 * <li>not set to 'exportable'</li>
	 * <li>a checkbox column</li>
	 * <li>not displayed but the onlyDisplayed-flag is true</li>
	 * </ul>
	 * 
	 * @param column
	 * @return
	 */
	private boolean showColumn(boolean onlyDisplayed, ITableColumn column) {
		// don't show a checkbox column
		boolean showColumn = true;

		if (!column.isExporatble()) {
			showColumn = false;
		}
		else if (column.getRenderer() instanceof CheckboxColumnRenderer) {
			showColumn = false;
		}
		else if (onlyDisplayed && !column.getIsDisplayed()) {
			showColumn = false;
		}

		return showColumn;
	}

	public boolean canHandle(String format) {
		return exportType.equals(format);
	}

	abstract public void writeTo(Table table, HttpServletResponse response);

	abstract public void writeTo(Table table, OutputStream out);

}
