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
package org.webguitoolkit.ui.controls.form;

import java.util.Collection;

/**
 * <h1>The Interface of the Multi-select.</h1>
 * <p>The Multi-select is intended the present a select-box with the option to select
 * multiple entries. A IPopupSelect using multiple selection can be an alternative.
 * </p>
 * <b>Creation of a Multiselect</b><br>
 * <pre>
 *  IMultiselect multi = factory.createMultiselect(layout, "multi", 5);
 *  multi.setMaxSelectedOptions(Multiselect.MAX_SELECTED_OPTIONS_UNLIMITED);
 *  multi.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
 *  List<String[]> sel = new ArrayList<String[]>();
 *  sel.add(new String[]{"uk","UK"});
 *  sel.add(new String[]{"de","Germany"});
 *  sel.add(new String[]{"us","USA"});
 *  sel.add(new String[]{"cl","Chile"});
 *  multi.loadList(sel);	
 * </pre>
 * <br>
 * CSS classes : wgtMultiselectContainer_dis, wgtMultiselectContainer, wgtMultiselect_entries, wgtMultiselect_entry_dis,
 * wgtMultiselect_entry
 * 
 * @author Peter, Ben
 * 
 */
public interface IMultiselect extends IFormControl {

	int MAX_SELECTED_OPTIONS_UNLIMITED = -1;
	int MAX_SELECTED_OPTIONS_SINGLE = 1;

	/**
	 * <p>
	 * Set the underlying model.
	 * </p>
	 * @param model the model
	 */
	void setModel(ISelectModel model);

	/**
	 * <p>
	 * return the mode of this select list. Usually this is an DefaultSelectMode or AssociationSelectModel. There are
	 * various methods which actually create the model for you. There is little use to call this method from an
	 * application.
	 * </p>
	 * @return model.
	 */
	ISelectModel getModel();

	/**
	 * @return the state
	 */
	boolean isReadOnly();

	/**
	 * <p>
	 * Use this parameter to set the maximum number of selection. Default behaviour is MultiSelect with no limitation
	 * (MAX_SELECTED_OPTIONS_UNLIMITED).
	 * </p>
	 * @param count
	 *            number of selection possible
	 */

	void setMaxSelectedOptions(int count);

	/**
	 * @param sel
	 *            the collection to be used in the select.
	 */
	void loadList(Collection sel);

}