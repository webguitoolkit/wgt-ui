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

import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.controls.AbstractLayout;
import org.webguitoolkit.ui.html.HTMLTag;
import org.webguitoolkit.ui.html.XHTMLTagFactory;

/**
 * <h1>Base class for all table based layouts (GridLayout, BorderLayout, SequentialTableLayout)</h1>
 * 
 * Handles the styling of rows
 * 
 * @author Martin
 */
public abstract class AbstractTableBasedLayout extends AbstractLayout {

	private String rowStyle = "";
	private String rowCssClass = "";
	private String tableStyle = "";
	private String tableCssClass = "";
	private int tableCellspacing = -1;
	private int tableCellpadding = -1;

	/**
	 * Sets the style of the rows like "height: 100px;"
	 * 
	 * @param newStyle the style for rows
	 */
	public void setRowStyle(String newStyle) {
		rowStyle = newStyle;
	}

	/**
	 * adds a css style class to the rows style class
	 * 
	 * @param addClass the style class to add
	 */
	public void addRowCssClass(String addClass) {
		rowCssClass += (" " + addClass);
	}

	/**
	 * Sets the style of the table like "width: 100%;"
	 * 
	 * @param newStyle the style for table
	 */
	public void setTableStyle(String newStyle) {
		tableStyle = newStyle;
	}

	/**
	 * adds a css style class to the tables style class
	 * 
	 * @param addClass the style class to add
	 */
	public void addTableCssClass(String addClass) {
		tableCssClass += (" " + addClass);
	}

	public void setTableCssClass(String tableCssClass) {
		this.tableCssClass = tableCssClass;
	}

	public void setTableCellspacing(int tableCellspacing) {
		this.tableCellspacing = tableCellspacing;
	}

	public void setTableCellpadding(int tableCellpadding) {
		this.tableCellpadding = tableCellpadding;
	}

	/**
	 * creates a row with style informations
	 * 
	 * @param table the table the row belongs to
	 * @param baseCssClass row base style
	 * @return empty row
	 */
	protected TR createRow(Table table, String baseCssClass) {
		String cssClass = rowCssClass;
		if (baseCssClass != null)
			cssClass = baseCssClass + " " + rowCssClass;

		TR row = new TR();
		row.setStyle(rowStyle);
		row.setClass(cssClass);
		table.addElement(row);

		return row;
	}

	/**
	 * creates a row with style informations
	 * 
	 * @param table the table the row belongs to
	 * @param baseCssClass row base style
	 * @return empty row
	 */
	protected HTMLTag createRow(HTMLTag table, String baseCssClass) {
		String cssClass = rowCssClass;
		if (baseCssClass != null)
			cssClass = baseCssClass + " " + rowCssClass;
		
		HTMLTag row = XHTMLTagFactory.getInstance().newTr(table, null);
		row.setStyle(rowStyle);
		row.setCssClass(cssClass);
		
		return row;
	}

	/**
	 * creates a table with style informations
	 * 
	 * @param baseCssClass the tables base style
	 * @return empty table
	 */
	protected Table createTable(String baseCssClass) {
		String cssClass = tableCssClass;
		if (baseCssClass != null)
			cssClass = baseCssClass + " " + tableCssClass;

		Table table = new Table();
		table.setStyle(tableStyle);
		table.setClass(cssClass);
		if (tableCellspacing >= 0)
			table.setCellSpacing(tableCellspacing);
		if (tableCellpadding >= 0)
			table.setCellPadding(tableCellpadding);

		return table;
	}
}
