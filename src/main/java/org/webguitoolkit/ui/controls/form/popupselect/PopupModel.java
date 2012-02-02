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
package org.webguitoolkit.ui.controls.form.popupselect;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.table.ITable;

/**
 * <h1>Model for {@link IPopupSelect}</h1>
 * <p>
 * The PopupMode holds additional settings for customizing the IPopupSelect. an instance of this class is created with every
 * instance of a {@link IPopupSelect}. Obtain the current model instance from the method getPopupModel() There is no need to
 * change the model values, the default values are working as a default implementation.
 * <p>
 * <b>Obtain the model instance</b><br>
 * <p>
 * 
 * <pre>
 * yourPopupSelect.getPopupModel();
 * </pre>
 * 
 * </P>
 * 
 * @author Ben
 * 
 */
public class PopupModel implements Serializable {

	public static final int SELECTION_MODE_SINGLE = 0;
	public static final int SELECTION_MODE_MULTIPLE = 1;

	private String noSelectionErrorKey, tableWidth;
	private int selectionMode = 0; // default
	private String[] columns;
	private HashMap resourceKeys;
	private HashMap selectValues;
	private List data;
	private int rows = 10;
	private int sortByColumn = -1;
	private String windowTitle;
	private String tableConfig = null;
	private boolean isFilterEnabled = false;
	private Popup popup = null;
	private String tableDisplayMode;
	private boolean tableEditable = false;
	private boolean showApplyAndContinue = false;
	private int popupWidth = -1, popupHeight = -1;

	/**
	 * @return the number of rows for the pop up
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows set the number of rows in the option table
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * <p>
	 * Add the property and the Title of the column. This will be shown in the pop up table. This should be done initial while
	 * creating the {@link IPopupSelect} through the WebGuiFactory. Use this method to do this later at runtime
	 * </p>
	 * 
	 * <pre>
	 * yourPopupSelect.getPopupModel.addResourceKey(&quot;yourProperty&quot;, &quot;resource.key.in.bundle@Your Property&quot;);
	 * </pre>
	 * 
	 * @param locator the property in the option object
	 * @param resourceKey key from the resource bundle
	 */
	public void addResourceKey(String locator, String resourceKey) {
		if (resourceKeys == null)
			resourceKeys = new HashMap();

		resourceKeys.put(locator, resourceKey);
	}

	/**
	 * <p>
	 * If you specified a search field as select, please use this method to load the values to the select. Must be a collection of
	 * String[]{id,value}
	 * </p>
	 * 
	 * @param locator
	 * @param newSelectValues
	 */
	public void addSelectValues(String locator, Collection newSelectValues) {
		if (selectValues == null)
			selectValues = new HashMap();

		selectValues.put(locator, newSelectValues);
	}

	/**
	 * @param locator
	 * @return
	 */
	protected Collection getSelectValues(String locator) {
		Collection result = null;
		if (selectValues != null && selectValues.get(locator) != null) {
			result = (Collection)selectValues.get(locator);
		}
		return result;
	}

	/**
	 * <p>
	 * Obtain the resource key for a column by the property
	 * </p>
	 * 
	 * @param locator
	 * @return
	 */
	protected String getReourceKey(String locator) {
		String result = null;
		if (resourceKeys != null && resourceKeys.get(locator) != null) {
			result = (String)resourceKeys.get(locator);
		}
		return result;
	}

	/**
	 * <p>
	 * check if a property was already added
	 * </p>
	 * 
	 * @param locator/property
	 * @return true for available
	 */
	public boolean isInColumns(String locator) {
		if (columns == null || locator == null) {
			return false;
		}
		else {
			for (int i = 0; i < columns.length; i++) {
				if (columns[i] != null && columns[i].equals(locator))
					return true;
			}
			return false;
		}
	}

	/**
	 * @return the noSelectionErrorKey
	 */
	public String getNoSelectionErrorKey() {

		if (noSelectionErrorKey != null) {
			if (noSelectionErrorKey.indexOf("@") == -1)
				this.noSelectionErrorKey += "@Please make a selection";
		}
		else {
			this.noSelectionErrorKey = "Please make a selection";
		}
		return noSelectionErrorKey;

	}

	/**
	 * <p>
	 * set message key to show error message (by default an English message is shown)
	 * </p>
	 * 
	 * @param noSelectionErrorKey the noSelectionErrorKey to set
	 */
	public void setNoSelectionErrorKey(String noSelectionErrorKey) {
		this.noSelectionErrorKey = noSelectionErrorKey;
	}

	/**
	 * @return the columns properties as String array
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * <p>
	 * Specify the properties of data elements to show in the table
	 * </p>
	 * 
	 * @param columns the columns to set
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	/**
	 * @return the option/available objects
	 */
	public List getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List data) {
		this.data = data;
	}

	/**
	 * @return true if some columns are available, else false
	 */
	public boolean isValid() {
		if (columns != null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @return the tableWidth
	 */
	public String getTableWidth() {
		if (tableWidth == null) {
			return "100%"; // default
		}

		return tableWidth;
	}

	/**
	 * <p>
	 * use css style i.e 120px
	 * </p>
	 * 
	 * @param tableWidth the tableWidth to set
	 */
	public void setTableWidth(String tableWidth) {
		this.tableWidth = tableWidth;
	}

	/**
	 * Possible values are PopupModel.SELECTION_MODE_SINGLE or PopupModel.SELECTION_MODE_MULTIPLE
	 * 
	 * @return the selectionMode
	 */
	public int getSelectionMode() {
		return selectionMode;
	}

	/**
	 * <p>
	 * Possible values are PopupModel.SELECTION_MODE_SINGLE or PopupModel.SELECTION_MODE_MULTIPLE
	 * </p>
	 * 
	 * @param selectionMode the selectionMode to set
	 */
	public void setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
	}

	/**
	 * @return the sortByColumn
	 */
	public int getSortByColumn() {
		return sortByColumn;
	}

	/**
	 * @param sortByColumn the sortByColumn to set
	 */
	public void setSortByColumn(int sortByColumn) {
		this.sortByColumn = sortByColumn;
	}

	/**
	 * <p>
	 * Set the titles of the column. These will be shown in the pop up table. The titles have to be in the same order as columns
	 * </p>
	 * 
	 * @param columnTitles
	 */
	public void setResourceKeys(String[] columnTitles) {
		for (int i = 0; i < columnTitles.length; i++) {
			if (columns != null && StringUtils.isNotEmpty(columns[i]))
				addResourceKey(columns[i], columnTitles[i]);
		}
	}

	/**
	 * @return the given pop up window title
	 */
	public String getWindowTitle() {
		return windowTitle;
	}

	/**
	 * <p>
	 * use this method to resize the window
	 * </p>
	 * 
	 * @param windowTitle
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	/**
	 * @param isFilterEnabled set this value true to enable filtering in pop up
	 */
	public void setFilterEnabled(boolean isFilterEnabled) {
		this.isFilterEnabled = isFilterEnabled;
	}

	/**
	 * @return indicator for enabled filtering in options table
	 */
	public boolean isFilterEnabled() {
		return isFilterEnabled;
	}

	/**
	 * <p>
	 * Set the initial table configuration. See {@link ITable}.loadTableConfig(String) for details
	 * </p>
	 * 
	 * @see ITable
	 * @param tableConfig
	 */
	public void setTableConfig(String tableConfig) {
		this.tableConfig = tableConfig;
	}

	/**
	 * @return the initial column configuration
	 */
	public String getTableConfig() {
		return tableConfig;
	}

	public void setPopup(Popup popup) {
		this.popup = popup;
	}

	public Popup getPopup() {
		return popup;
	}

	/**
	 * @return
	 */
	public String getTableDisplayMode() {
		if (StringUtils.isEmpty(tableDisplayMode)) {
			this.tableDisplayMode = ITable.DISPLAY_MODE_SCROLLBAR;
		}
		return tableDisplayMode;
	}

	/**
	 * @return
	 */
	public boolean isTableEditable() {
		return tableEditable;
	}

	/**
	 * @param tableDisplayMode the tableDisplayMode to set
	 */
	public void setTableDisplayMode(String tableDisplayMode) {
		this.tableDisplayMode = tableDisplayMode;
	}

	/**
	 * @param tableEditable the tableEditable to set
	 */
	public void setTableEditable(boolean tableEditable) {
		this.tableEditable = tableEditable;
	}

	/**
	 * @return width of popup, -1 uses default width
	 */
	public int getPopupWidth() {
		return popupWidth;
	}

	/**
	 * the width of the popup can be adjusted by this method
	 * 
	 * @param popupWidth
	 */
	public void setPopupWidth(int popupWidth) {
		this.popupWidth = popupWidth;
	}

	/**
	 * @return height of popup, -1 uses default height
	 */
	public int getPopupHeight() {
		return popupHeight;
	}

	/**
	 * the height of the Popup can be adjusted by this method
	 * 
	 * @param popupHeight
	 */
	public void setPopupHeight(int popupHeight) {
		this.popupHeight = popupHeight;
	}

	public boolean isShowApplyAndContinue() {
		return showApplyAndContinue;
	}

	public void setShowApplyAndContinue(boolean showApplyAndContinue) {
		this.showApplyAndContinue = showApplyAndContinue;
	}

}
