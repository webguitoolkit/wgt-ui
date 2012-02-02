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
/*
 */
package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * <pre>
 * Option controls can have group and value attribute.
 * If the value is not null, it is assumed, that there are other options with ALL other possible 
 * values for the same property!
 * if value attribute is not set, it is assumed, that the property is of type boolean.
 * </pre>
 * 
 * @author Arno Schatz
 * 
 */
public abstract class OptionControl extends FormControl implements IOptionControl {

	protected String label;
	protected boolean managedByGroup = false;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public OptionControl() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id
	 *            unique HTML id
	 */
	public OptionControl(String id) {
		super(id);
	}

	// if this property if not owned by this control value will have the value
	// on which we react.
	protected String optionValue;

	public void loadFrom(Object data) {
		if( managedByGroup )
			return;
		
		boolean val;
		if (data == null) {
			val = false;
		} else {
			Object fromModel = PropertyAccessor.retrieveProperty(data, getProperty());
			if (fromModel == null) {
				val = false;
			} else {
				if (getOptionValue() != null) {
					val = getOptionValue().equals(fromModel.toString());
				} else {
					val = BooleanUtils.toBoolean((fromModel.toString()));
				}
			}
		}
		// The type must be boolean indicating if this thingi is set or not
		setSelected(val);
	}

	/**
	 * set the state of this radio
	 */
	public void setSelected(boolean newState) {
		getContext().add(getId(), Boolean.toString(newState), IContext.TYPE_SEL, IContext.STATUS_EDITABLE);
	}

	/**
	 * read the state of the radio, where no state is like turned off
	 * 
	 * @return true if radio is selected
	 */
	public boolean isSelected() {
		return getContext().getValueAsBool(getId(), false);
	}

	public void saveTo(Object data) {
		if( managedByGroup )
			return;
		// problems with error reporting...
		// if (!getBody().clientChange(getId())) return;
		try {
			if (getOptionValue() != null) {
				if (isSelected()) {// if not selected there must be a option
					// control which is selected in the same
					// group
					PropertyAccessor.storeProperty(data, getProperty(), getOptionValue());
				}
			} else {
				PropertyAccessor.storeProperty(data, getProperty(), Boolean.valueOf(isSelected()));
			}
		} catch (ValidationException e) {
			// cannot happen
			e.printStackTrace();
		}
	}

	protected void init() {
		if (getContext().getValue(getId()) == null)
			setSelected(false);
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optValue) {
		this.optionValue = optValue;
	}

	protected void makeLabel(PrintWriter out) {
		if (StringUtils.isNotBlank(getLabel())) {
			Span span = new Span();
			span.setClass("wgtLabel");
			span.setID(getId() + "_label");
			span.addElement(getLabel());
			span.output(out);
		}
	}

	public void setVisible(boolean vis) {

		super.setVisible(vis);

		if (StringUtils.isNotBlank(getLabel()))
			getContext().makeVisible(getId() + "_label", vis);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		if (isDrawn())
			getContext().innerHtml(getId() + "_label", label);
	}

	public void setLabelKey(String labelKey) {
		setLabel(TextService.getString(labelKey));
	}

	/**
	 * @param managedByGroup true if a OptionControlGroup is responsible for handling save and load functions
	 */
	public void setManagedByGroup(boolean managedByGroup) {
		this.managedByGroup = managedByGroup;
	}
}
