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
package org.webguitoolkit.ui.controls.util.validation;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Checks if the entered value is empty or an integer.
 * With parameters can check if Integer is positive, greater than 0 or than a minimum value.
 * Checks are only made if value is not empty.
 * </pre>
 */
public class IntegerValidator implements IValidator {

	private boolean checkPositive = false;
	private boolean checkGreaterThan0 = false;
	private boolean checkMinValue = false;
	private boolean checkMaxValue = false;
	private int minValue, maxValue;
	
	public void validate(String value) throws ValidationException {
		// empty is ok
		if (StringUtils.isEmpty(value))
			return;
		try {
			Integer aInteger = new Integer(value);
			if (isCheckPositive() && aInteger.intValue() < 0)
			{
				throw new ValidationException(TextService.getString(
				"validator.IntegerValidator.message.positive@The value must be a positive integer."));
			}
			if (isCheckGreaterThan0() && aInteger.intValue() <= 0)
			{
				throw new ValidationException(TextService.getString(
				"validator.IntegerValidator.message.greater0@The value must be a greater than 0."));
			}
			if (isCheckMinValue() && aInteger.intValue() < getMinValue())
			{
				throw new ValidationException(TextService.getString(
				"validator.IntegerValidator.message.greaterMinValue@The value must be greater than or equal {1}.", getMinValue() + ""));
			}
			if (isCheckMaxValue() && aInteger.intValue() > getMaxValue())
			{
				throw new ValidationException(TextService.getString(
				"validator.IntegerValidator.message.lessMaxValue@The value must be less than or equal {1}.", getMaxValue() + ""));
			}
		} catch (NumberFormatException e) {
			throw new ValidationException(TextService.getString(
					"validator.IntegerValidator.message@The value must be an integer."));
		}		
	}
	
	public boolean isCheckGreaterThan0() {
		return checkGreaterThan0;
	}
	
	public void setCheckGreaterThan0(boolean checkGreaterThan0) {
		this.checkGreaterThan0 = checkGreaterThan0;
	}
	
	public boolean isCheckPositive() {
		return checkPositive;
	}
	
	public void setCheckPositive(boolean checkPositive) {
		this.checkPositive = checkPositive;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
		setCheckMinValue(true);
	}

	public int getMinValue() {
		return minValue;
	}

	private boolean isCheckMinValue() {
		return checkMinValue;
	}
	
	private void setCheckMinValue(boolean checkMinValue) {
		this.checkMinValue = checkMinValue;
	}

	public void setCheckMaxValue(boolean checkMaxValue) {
		this.checkMaxValue = checkMaxValue;
	}

	public boolean isCheckMaxValue() {
		return checkMaxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		setCheckMaxValue(true);
	}

	public int getMaxValue() {
		return maxValue;
	}

	public String getTooltip() {
		return TextService.getString("validator.IntegerValidator.tooltip" );
	}
}
