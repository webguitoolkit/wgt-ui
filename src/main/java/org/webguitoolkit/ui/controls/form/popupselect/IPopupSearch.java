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

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.layout.ITableLayout;

/**
 * <h1>Interface for a PopupSearch</h1>
 * <p>
 * An implementation of this interface is required to enable the search for a {@link IPopupSelect}. Extend the {@link AbstractPopupSearch} for
 * convenience methods.
 * </p>
 * <p>
 * A good practice is, to define the search inputs global in your Pop up search class and use the methods from this interface to implement the
 * behavior.
 * </p>
 * 
 * <pre>
 * class ChangeCountryListener implements IActionListener {
 * 	//create a IText to ask the user for input as a global variable
 * 	private IText searchNumber;
 * 
 * 	//implement your generateControls() method
 * 	public void generateControls(ITableLayout layout, WebGuiFactory factory) {
 * 		searchNumber = createTextField(layout, factory, &quot;customer.number@No.&quot;, &quot;customerNumber&quot;);
 * 	}
 * 
 * 	// implement the search logic
 * 	public List performSearch() {
 * 		//get the value
 * 		String number = searchNumber.getValue();
 * 
 * 		//query the objects using the number and your persistence layer
 * 		List result = Query.getCustomersByNumber(number);
 * 		//return the results to show them
 * 		return result;
 * 	}
 * 
 * 	//optionally implement reset
 * 	public void resetSearch() {
 * 		//reset the input values
 * 		searchNumber.setValue(&quot;&quot;);
 * 	}
 * }
 * </pre>
 * 
 * @author Ben
 * @see IPopupSelect
 * @see AbstractPopupSearch
 */
public interface IPopupSearch {

	/**
	 * <p>
	 * Generate the search form in this method by using the convenience methods from the {@link AbstractPopupSearch}.
	 * </p>
	 * 
	 * @param layout
	 *            TableLayout for the search form
	 * @param factory
	 *            WebGuiFactory to create controls
	 */
	void generateControls(ITableLayout layout, WebGuiFactory factory);

	/**
	 * implement the search logic. Query the results from the persistence layer as required.
	 * 
	 * @return list of objects to be used as option in the {@link IPopupSelect}
	 */
	List performSearch();

	/**
	 * implement, if you enabled showReset() in the {@link IPopupSelect} 
	 */
	void resetSearch();
	
	/**
	 * @return corresponding PopupModel
	 */
	PopupModel getPopupModel();
	

	
	/**
	 * set corresponding PopupModel
	 * 
	 * @param popupModel
	 */
	void setPopupModel(PopupModel popupModel);
}
