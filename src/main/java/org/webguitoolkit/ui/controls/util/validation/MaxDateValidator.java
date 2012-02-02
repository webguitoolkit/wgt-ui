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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * Validates if the input date of a datepicker field is before a maximum date. By default, the maximum date is 30.12.9999.
 * 
 * @author sattlera
 */
public class MaxDateValidator implements IValidator {

	private static final Log log = LogFactory.getLog(MaxDateValidator.class);

	private static final int MAX_YEARS = 9999;

	private Date maxDate;
	private IText dateField;

	public MaxDateValidator(IText dateField) {
		this.dateField = dateField;

		Calendar maxCal = Calendar.getInstance();
		maxCal.set(MAX_YEARS, 11, 30); // should be 30.12.9999 because timezone-offset could cause wrapping the day to 31
		this.maxDate = maxCal.getTime();
	}

	public MaxDateValidator(IText dateField, Date maxDate) {
		this.dateField = dateField;
		this.maxDate = maxDate;
	}

	public void validate(String value) throws ValidationException {
		try {
			Date inputDate = (Date)dateField.getConvertedValue();

			if (inputDate != null && inputDate.after(maxDate)) {
				throw new ValidationException(
						TextService.getString("validator.MaxDateValidator.message@The date mustn't be after the year " + MAX_YEARS + "."));
			}
		}
		catch (ConversionException e) {
			log.warn("Could not convert user input date");
		}
	}

	public String getTooltip() {
		return TextService.getString("validator.MaxDateValidator.tooltip");
	}
}
