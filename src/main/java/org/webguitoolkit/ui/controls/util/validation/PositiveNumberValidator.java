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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Checks for positive Doubles
 * </pre>
 */
public class PositiveNumberValidator implements IValidator {
	private String pattern;

	public PositiveNumberValidator(String pattern) {
		this.pattern = pattern;
	}

	public void validate(String object) throws ValidationException {
		try {
			// no mandatory field: empty means value zero.
			if (StringUtils.isEmpty(object)) {
				return;
			}
			else {
				DecimalFormat formatter;
				formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));

				ParsePosition pos = new ParsePosition(0);
				Number num = formatter.parse(object, pos);

				if (num == null)
					throw new ValidationException(
							TextService.getString("converter.PositiveNumberValidator.message.conversion@Cannot convert into number."));
				if (pos.getIndex() < object.length()) {
					throw new ValidationException(
							TextService.getString("converter.PositiveNumberValidator.message.conversion@Cannot convert into number."));
				}

				Double f = new Double(num.doubleValue());
				if (f.doubleValue() < 0) {
					throw new ValidationException(
							TextService.getString("validator.PositiveNumberValidator.message@The entered number must be positive."));
				}
			}
		}
		catch (NumberFormatException e) {
			throw new ValidationException(
					TextService.getString("validator.PositiveNumberValidator.message.conversion@Cannot convert into number."));
		}
	}

	public String getTooltip() {
		return TextService.getString("validator.PositiveNumberValidator.tooltip");
	}

}
