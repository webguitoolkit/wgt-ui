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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.validation.IValidator;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * <p>
 * Base Class for all form fields like Radio, Select, Text, ... .
 * </p>
 * <p>
 * A compound can contain FormControls like a HTML form can contain input elements.
 * </p>
 * 
 * <p>
 * The property of a FormControl is used to map property of an object (usually stored at the compound) to the value of the
 * FormControl.
 * </p>
 * 
 * <p>
 * For fields that can be set on creation and can't be edited later (like key attributes) you have to set the isFinal flag.
 * </p>
 */

public abstract class FormControl extends ActionControl implements ICompoundLifecycleElement, IFormControl {
	public static final String DOT_RO = ".ro";
	public static final String TABINDEX = "tabindex";
	public static final String ERROR_STYLE_CLASS = "wgtInputError";

	// property under which the content is safed in the model.
	protected String property;
	// Convert the property data type into string to be displayed
	protected IConverter conv;
	// validator for the input fields
	protected List validators;
	// is this element writable in edit mode?
	protected boolean isFinal;
	/**
	 * the surrounding compound
	 */
	protected ICompound compound;

	protected ILabel describingLabel;
	// sets the tabindex of html form elements buttons and anker
	protected int tabindex;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public FormControl() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public FormControl(String id) {
		super(id);
	}

	/**
	 * the property of a bean that contains the data of that FormControl, there has to be a getter and setter in the bean or a
	 * parameter in the databag
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * the property of a bean that contains the data of that FormControl, there has to be a getter and setter in the bean or a
	 * parameter in the databag
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * compound is giving the instruction to load from this dataobject. This will set the context will the property from this
	 * dataobject you may pass null to set all elements null.
	 */
	public void loadFrom(Object data) {
		// get the property fro the data objecrt
		String dataForText = (data == null) ? "" : PropertyAccessor.retrieveString(data, getProperty(), getConverter());
		// move to contxt
		if (dataForText == null)
			dataForText = "";
		setValue(dataForText);
	}

	/**
	 * sets the value of the FormControl default it is set to the value attribute of the HTML representation
	 */
	public void setValue(String dataForText) {
		getContext().add(getId(), dataForText, IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
	}

	/**
	 * sets the value of the FormControl default it is set to the value attribute of the HTML representation
	 */
	public void setObjectValue(Object dataObject) {
		if (getConverter() == null)
			setValue(dataObject.toString());
		else
			setValue((String)getConverter().convert(String.class, dataObject));
	}

	/**
	 * sets the converter for displaying the value. e.g. Date Converter
	 */
	public void setConverter(IConverter conv) {
		this.conv = conv;
	}

	public IConverter getConverter() {
		return conv;
	}

	/**
	 * sets the converter for displaying the value. e.g. Date Converter
	 */
	public void addValidator(IValidator validator) {
		if (validators == null)
			validators = new ArrayList();
		validators.add(validator);
		// setValidatorInfo();
	}

	public List getValidators() {
		return validators;
	}

	public void clearValidators() {
		validators = new ArrayList();
	}

	/**
	 * set the tooltip to the label for displaying a '*'
	 */
	private void setValidatorInfo() {
		if (describingLabel != null && validators != null && !validators.isEmpty()) {
			String tooltips = "";
			for (Iterator iter = validators.iterator(); iter.hasNext();) {
				IValidator val = (IValidator)iter.next();
				String tooltip = val.getTooltip();
				if (!StringUtils.isEmpty(tooltip)) {
					tooltips += tooltip;
					if (iter.hasNext())
						tooltips += "&#x0D;";
				}
			}
			((Label)describingLabel).setTooltipFromValidator(tooltips);
		}
	}

	/**
	 * saves the value to given data object
	 */
	public void saveTo(Object dataObject) {
		// check if there is a change in the context at all...
		// problems with error reporting...
		try {
			validate();
			Object value = getConvertedValue();
			PropertyAccessor.storeProperty(dataObject, getProperty(), value);
		}
		catch (ValidationException e) {
			((Compound)surroundingCompound()).addError(e.getMessage(), getProperty());
		}
	}

	/**
	 * retrieve the surrounding compound
	 */
	public ICompound surroundingCompound() {
		if (compound == null) {
			BaseControl sb = this;
			while (!(sb instanceof ICompound) && !(sb instanceof Table)) {
				sb = sb.getParent();
				if (sb == null)
					break;
			}
			if (sb instanceof ITable)
				compound = ((Table)sb).getRowCompound();
			else
				compound = (Compound)sb;
		}
		return compound;
	}

	/**
	 * use with care. only for flyweight pattern
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setParent(BaseControl father) {
		super.setParent(father);
		ICompound surrounding = surroundingCompound();
		// may be null, then it must be set afterwards
		if (surrounding != null) {
			if (father != null) {
				surrounding.addElement(this);
			}
			else {
				((Compound)surrounding).removeElement(this);
			}
		}
	}

	protected void init() {
		getContext().add(getId(), "", IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
	}

	/**
	 * sets the focus to this FormControl element.
	 */
	public void focus() {
		getContext().add(getId() + ".focus", "", IContext.TYPE_FOCUS, IContext.STATUS_NOT_EDITABLE);
	}

	public void setCompound(ICompound compound) {
		this.compound = compound;
	}

	/**
	 * get the currrent value for this formelement from the context
	 * 
	 * @return
	 */
	public String getValue() {
		return getContext().getValue(getId());
	}

	/**
	 * This function is useful if there is a converter is set and the control is not used in
	 * 
	 * @return
	 */
	public Object getConvertedValue() throws ConversionException {
		if (getConverter() != null) {
			return getConverter().parse(getValue());
		}
		return getValue();
	}

	/**
	 * default behavour of formcontrols to change their input mode.
	 * 
	 * @param mode
	 */
	public void changeMode(int mode) {
		// do we want ot participate in the mode change?
		if (mode == Compound.MODE_EDIT && isFinal()) {
			return; // no, good bye
		}
		IContext ctx = getContext();

		// tell component to switch mode.
		ctx.add(getId() + DOT_RO, getId(), ((mode == Compound.MODE_READONLY) ? IContext.TYPE_READONLY : IContext.TYPE_READWRITE),
				IContext.STATUS_NOT_EDITABLE);
		clearError();
	}

	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.form.IFormControl#clearError()
	 */
	public void clearError() {
		if (getContext().getValue(getId() + ".addClass") != null && getContext().getValue(getId() + ".addClass").equals(ERROR_STYLE_CLASS))
			getContext().removeClass(getId(), getContext().processValue(getId() + ".addClass"));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#showError()
	 */
	public void showError() {
		getContext().addClass(getId(), FormControl.ERROR_STYLE_CLASS);
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	// here a reference to the label can be stored, used for error messages on validation
	/**
	 * set the component for which we are labeling directly This method can only be used in dynamic programming for example by the
	 * IFactory method createLabel(...)
	 * 
	 * @param labeledComp
	 */
	public ILabel getDescribingLabel() {
		return describingLabel;
	}

	public void setDescribingLabel(ILabel describingLabel) {
		describingLabel.setLabelForFormControl(true);
		this.describingLabel = describingLabel;
		// setValidatorInfo();
	}

	public void drawFormControl(PrintWriter out) {
		super.drawInternal(out);
	}

	public void validate() throws ValidationException {
		String value = getValue();
		if (validators != null) {
			for (Iterator iter = validators.iterator(); iter.hasNext();)
				((IValidator)iter.next()).validate(value);
		}
	}

	/**
	 * returns the tabindex value of a html element.
	 * 
	 * @return
	 */
	public int getTabindex() {
		return tabindex;
	}

	/**
	 * Set the tabindex of a html element. The developer has to make sure that he does not use the same tabindex more than once on
	 * the page Changing the tabindex after rendering the html element has no effect until the element is redrawn
	 * 
	 * @param tabindex
	 */
	public void setTabindex(int tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * Set the tabindex of a html element using the tabindex of another FormControl Element adding 1. The developer has to make
	 * sure that he does not use the same tabindex more than once on the page Changing the tabindex after rendering the html
	 * element has no effect until the element is redrawn
	 * 
	 * @param predecessor
	 */
	public void setTabindexPredecessor(IFormControl predecessor) {
		int newTabindex = 0;
		if (predecessor.getTabindex() >= 0) {
			newTabindex = predecessor.getTabindex() + 1;
			this.tabindex = newTabindex;
		}
	}
}
