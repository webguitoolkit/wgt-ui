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

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.form.ICheckBox;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.ISelect;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.layout.ITableLayout;

/**
 * <p>
 * This is the base for custom implementations of IPopupSearch. It helps 
 * building a search form for the IPopupSelect. 
 * @see IPopupSearch
 * </p>
 * @author Ben
 *
 */
public abstract class AbstractPopupSearch implements IPopupSearch {
	
	private PopupModel popupModel=null;

	/**
	 * <p>
	 * Create a simple IText as input for the search form
	 * </p>
	 * 
	 * @param layout the TableLayout for your search form
	 * @param factory the WebGuiFactory
	 * @param labelKey the key of the label in applications resource bundle
	 * @param property a unique name for the property in this search form
	 * @return the created IText. It is already added to the layout.
	 */
	protected IText createTextField(ITableLayout layout, WebGuiFactory factory, String labelKey, String property) {
		return createTextField(layout, factory, labelKey, property, true);
	}

	/**
	 * <p>
	 * Create a ICheckbox as input for the search form
	 * </p>
	 * 
	 * @param layout the TableLayout for your search form
	 * @param factory the WebGuiFactory
	 * @param labelKey the key of the label in applications resource bundle
	 * @param property a unique name for the property in this search form
	 * @return the created ICheckbox. It is already added to the layout.
	 */
	protected ICheckBox createCheckboxField(ITableLayout layout, WebGuiFactory factory, String labelKey, String property) {
		return createCheckboxField(layout, factory, labelKey, property, true);
	}

	/**
	 * <p>
	 * Create a ISelect as input for the search form
	 * </p>
	 * 
	 * @param layout the TableLayout for your search form
	 * @param factory the WebGuiFactory
	 * @param labelKey the key of the label in applications resource bundle
	 * @param property a unique name for the property in this search form
	 * @return the created ISelect. It is already added to the layout.
	 */
	protected ISelect createSelect(ITableLayout layout, WebGuiFactory factory, String labelKey, String property) {
		return createSelect(layout, factory, labelKey, property, true);
	}

	/**
	 * <p>
	 * Create a simple IText as input for the search form
	 * </p>
	 * 
	 * @param layout the TableLayout for your search form
	 * @param factory the WebGuiFactory
	 * @param labelKey the key of the label in applications resource bundle
	 * @param property a unique name for the property in this search form
	 * @param addNewLine true for a new line after this input
	 * @return the created IText. It is already added to the layout.
	 * 
	 */
	protected IText createTextField(ITableLayout layout, WebGuiFactory factory, String labelKey, String property, boolean addNewLine) {
		ILabel l = factory.createLabel(layout, labelKey);
		IText input = factory.createText(layout, property, l);
		if (addNewLine)
			layout.newRow();
		return input;
	}

	/**
	 * <p>
	 * Create a ICheckbox as input for the search form
	 * </p>
	 * 
	 * @param layout the TableLayout for your search form
	 * @param factory the WebGuiFactory
	 * @param labelKey the key of the label in applications resource bundle
	 * @param property a unique name for the property in this search form
	 * @param addNewLine true for a new line after this input
	 * @return the created ICheckbox. It is already added to the layout.
	 */
	protected ICheckBox createCheckboxField(ITableLayout layout, WebGuiFactory factory, String labelKey, String property, boolean addNewLine) {
		ILabel l = factory.createLabel(layout, labelKey);
		ICheckBox input = factory.createCheckBox(layout, property);
		input.setDescribingLabel(l);
		if (addNewLine)
			layout.newRow();
		return input;
	}

	/**
	 * <p>
	 * Create a ISelect as input for the search form
	 * </p>
	 * 
	 * @param layout the TableLayout for your search form
	 * @param factory the WebGuiFactory
	 * @param labelKey the key of the label in applications resource bundle
	 * @param property a unique name for the property in this search form
	 * @param addNewLine true for a new line after this input
	 * @return the created ISelect. It is already added to the layout.
	 */
	protected ISelect createSelect(ITableLayout layout, WebGuiFactory factory, String labelKey, String property, boolean addNewLine) {
		ILabel l = factory.createLabel(layout, labelKey);
		ISelect input = factory.createSelect(layout, property);
		input.setDescribingLabel(l);
		if (addNewLine)
			layout.newRow();
		return input;
	}

	public PopupModel getPopupModel() {
		return popupModel;
	}

	public void setPopupModel(PopupModel popupModel) {
		this.popupModel = popupModel;
	}
}
