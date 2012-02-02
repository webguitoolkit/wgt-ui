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

import java.util.List;

/**
 * <h1>Interface for PopupSelectable Controls</h1>
 * <p>
 * Check {@link IPopupSelect} and {@link PopupSelectAction} for details
 * </p>
 * 
 * @author Ben
 * 
 */
public interface IPopupSelectable {
	/**
	 * <p>
	 * Set an optional window title for the pop up. This title should force the users action. i.e.
	 * </p>
	 * 
	 * <pre>
	 * select.setWindowTitle(&quot;Please select a customer&quot;);
	 * </pre>
	 * 
	 * @param windowTitle
	 *            the string for the pop up title
	 */
	public void setWindowTitle(String windowTitle);

	/**
	 * <p>
	 * Set the properties of the options to be shown in the pop up
	 * </p>
	 * 
	 * <pre>
	 * select.setColumns(new String[] { &quot;customerName&quot;, &quot;cutomerNumber&quot; });
	 * </pre>
	 * 
	 * @param columns
	 *            string array of properties for the pop up
	 */
	public void setColumns(String[] columns);

	/**
	 * <p>
	 * Set the property Titles of the columns to be shown in the pop up table. Use the same order of columns as specified in setColumns(String[]
	 * columns)
	 * </p>
	 * 
	 * <pre>
	 * select.setColumnTitles(new String[] { &quot;Customer Name&quot;, &quot;Customer Number&quot; });
	 * </pre>
	 * 
	 * @param columnTitles
	 *            string array of names for the pop up, same order as setColumns(...)
	 */
	public void setColumnTitles(String[] columnTitles);

	/**
	 * <p>
	 * Set the list of available options in the pop up. Leave this list empty, if you implement a IPopupSearch
	 * </p>
	 * 
	 * @param availableObjects
	 *            list of object to select from
	 */
	public void setAvailableObjects(List availableObjects);

	/**
	 * <p>
	 * Decide whether the user should be able to select multiple entries from the options or not. this changes the size of the control.
	 * </p>
	 * 
	 * @param isMultiSelect
	 *            true for multiple selection else false
	 */
	public void setIsMultiselect(boolean isMultiSelect);

	/**
	 * <p>
	 * Set this true to enable the search. This requires an implementation of IPopupSerach to be registered with setPopupSearch(IPopupSearch search)
	 * </p>
	 * 
	 * @param showSearch
	 *            true to enable the PopupSearch
	 */
	public void setShowSearch(boolean showSearch);

	/**
	 * <p>
	 * Set a IPopupSearch optionally. This can reduce the amount of objects by loading only the required ones. Set setIsMultiselect() to true to
	 * enable the search. Create a IPupupSearch by extending the AbcstractPopupSearch
	 * </p>
	 * 
	 * @param search
	 *            implementation of IPopupSearch
	 * @see AbstractPopupSearch, IPopupSearch
	 */
	public void setPopupSearch(IPopupSearch search);

	/**
	 * The {@link PopupModel} holds additional settings for customizing the IPopupSelect.
	 * 
	 * @return the {@link PopupModel}
	 */
	public PopupModel getPopupModel();

	/**
	 * <p>
	 * Set this to true to view an optional reset button in the IPopupSearch.
	 * </p>
	 * 
	 * @param resetSearch
	 *            true to enable reset button in IPopupSearch
	 */
	public void setResetSearch(boolean resetSearch);

	/**
	 * <p>
	 * Set this to true to view a message when no results where found in the IPopupSearch.
	 * </p>
	 * 
	 * @param showNoResultsFound
	 *            true to show a message in IPopupSearch
	 */
	public void setShowNoResultsFoundMessage(boolean showNoResultsFound);

}