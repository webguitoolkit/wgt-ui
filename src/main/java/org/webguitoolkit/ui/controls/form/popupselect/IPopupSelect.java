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

import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.form.IFormControl;

/**
 * <h1>Interface for PopupSelect</h1>
 * <p>
 * A PopupSelect is a control to provide complex search/select capabilities to the user in a ICompound.
 * It is an alternative for the ISelect. It can be configured as single select or multiple select.
 * Advantages to ISelect: Select from a large option list, use filters and searches to reduce the options.
 * Implement a IPopupSearch to minimize the load of options in the list.
 * </p>
 * <b>Creation of a PopupSelect</b><br>
 * <pre>
 * // the label is displayed in the style wgtLabelFor
 * ILabel cusomerLabel = factory.createLabel(layout, "resource.bundle.key.customerLabel");
 * // PopupSelect for selection of a single business object. Field "customer" when used in a compound.
 * // Options from a loaded option list (customers).
 * // Display property specifies, which property of the selected customer is show in the front end.
 * // In the columns parameter you can specify what columns are show in the pop up. Use columTitles
 * // to set the column headers.
 * IPopupSelect customerSelect = getFactory().createPopupSelect(layout, "customer", "Name", false, new String[] { "Name", "Address" },
 *				new String[] { "Customer Name", "Customer Address" }, cusomerLabel);
 * //load customers as options
 * customerSelect.setAvailableObjects(new ArrayList(someCostomers));
 * </pre>
 * <br>
 * <b>Event handling</b><br>
 * IPopupSelect fields can trigger events, this events can be handled by action listeners.<br>
 * <pre>
 * //the action listener code
 * class MyActionListener implements IActionListener{
 *   public void onAction(ClientEvent e){
 *     e.getSource().getPage().sendInfo("Option selected! ");
 *   }
 * }
 *
 * //code where the text field is created
 * //default event when selecting an option
 * customerSelect.setActionListener( new MyActionListener() );
 * </pre>
 * <br>
 * CSS classes : wgtInputTextarea, wgtReadonly, wgtInputTextWith2Button, wgtPointerCursor
 * @author Peter, Ben
 *
 */
public interface IPopupSelect extends IFormControl, IPopupSelectable {
	/**
	 * <p>
	 * Set the property representing the selected business object in the compound
	 * </p>
	 * <pre>
	 * 	select.setDisplayProperty("customerName");
	 * </pre>
	 * @param displayProperty property name
	 */
	void setDisplayProperty(String displayProperty);

	/**
	 * <p>
	 * Set a selected values by the program logic.
	 * The given objects must match the objects in the list of available objects (options).
	 * <br />
	 * Use this method for not single selections
	 * </p>
	 * @param selectedObjects objects for selected option
	 */
	void setSelectedObjects(List selectedObjects);


	/**
	* <p>define a listener for table configuration changes (optionl)</p>
	*	
	 * @param liz
	 */
	void setEditTableConfigListener(IServerEventListener liz);

	/**
	 * <p>
	 * Set a selected value by the program logic.
	 * The given object must match the objects in the list of available objects (options).
	 * <br />
	 * Use this method for not multiple selections
	 * </p>
	 *
	 * @param selectedObject object for selected option
	 */
	void setSelectedObject(Object selectedObject);

	/**
	 * <p>
	 * Get selected value(s)
	 * </p>
	 * @return selected objects
	 */
	public List getSelectedObjects();

	/**
	 * <p>
	 * Get a selected value.
	 * <br />
	 * Use this method for not multiple selections
	 * </p>
	 * @return selected object
	 */
	Object getSelectedObject();


}