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

import org.webguitoolkit.ui.controls.IActionControl;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.validation.IValidator;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * <p>
 * A <code>FormControl</code> is a control for user input. It is similar to input tags in HTML. To provide a
 * <code>FormControl</code> with data and save the data to a business object a <code>Compound</code> can be used.
 * </p>
 * 
 * @author Peter
 * @author Martin
 * 
 * @see org.webguitoolkit.ui.controls.form.ICompound
 */
public interface IFormControl extends IActionControl, ICompoundLifecycleElement {

	/**
	 * <p>
	 * Sets the value of the FormControl. By default the value attribute of the HTML representation will be set.
	 * </p>
	 * 
	 * @param value the value as text
	 */
	void setValue(String value);

	/**
	 * <p>
	 * Set the converter for displaying the value. e.g. DateConverter
	 * </p>
	 * 
	 * @param conv the converter, if none is specified the value is a simple text
	 */
	void setConverter(IConverter conv);

	/**
	 * <p>
	 * Set the validator for displaying the value. e.g. MandatoryValdator
	 * </p>
	 * 
	 * @param validator the validator to add
	 */
	void addValidator(IValidator validator);

	/**
	 * Clears Validators of the FormControl
	 * 
	 */
	void clearValidators();

	/**
	 * Sets the focus to this FormControl.
	 */
	void focus();

	/**
	 * For fields that can be set on creation and can't be edited later (like key attributes) you have to set the isFinal flag.
	 * 
	 * @param isFinal if the value can be changed after creation
	 */
	void setFinal(boolean isFinal);

	/**
	 * If a FormControl and a label belonging together this can be set here. This will be used later on for error messages and so
	 * on.
	 * 
	 * @param describingLabel the Label
	 */
	void setDescribingLabel(ILabel describingLabel);

	/**
	 * Set the tabindex of a html element. The developer has to make sure that he does not use the same tabindex more than once on
	 * the page Changing the tabindex after rendering the html element has no effect until the element is redrawn
	 * 
	 * @param tabindex the tabindex of the element
	 */
	void setTabindex(int tabindex);

	/**
	 * returns the tabindex value of a html element.
	 * 
	 * @return
	 */
	public int getTabindex();

	/**
	 * Set the tabindex of a html element using the tabindex of another FormControl Element adding 1. The developer has to make
	 * sure that he does not use the same tabindex more than once on the page Changing the tabindex after rendering the html
	 * element has no effect until the element is redrawn
	 * 
	 * @param predecessor
	 */
	public void setTabindexPredecessor(IFormControl predecessor);

	/**
	 * get the current value for this form element as String
	 * 
	 * @return the value as string
	 */
	String getValue();

	/**
	 * returns the converted value if a converter is set otherwise the string value is returned
	 * 
	 * @return the value after processing the converter
	 * @throws ConversionException if there is a error at converting
	 */
	Object getConvertedValue() throws ConversionException;

	/**
	 * change the display mode to readonly, edit or new
	 * 
	 * @param mode constants can be found in compound
	 */
	void changeMode(int mode);

	/**
	 * calls the validators of the form control
	 * 
	 * @throws ValidationException if one validator fails
	 */
	void validate() throws ValidationException;

	/**
	 * @param property set the property name of the object in the business object
	 */
	void setProperty(String property);

	/**
	 * get the property name of the object in the business object
	 */
	String getProperty();

	/**
	 * @return the label that describes the FormControl
	 */
	ILabel getDescribingLabel();

	/**
	 * sets the value of the FormControl default it is set to the value attribute of the HTML representation
	 */
	void setObjectValue(Object dataObject);

}
