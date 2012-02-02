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

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Checks the given value for valid entries, separated by the separator. 
 * i.e. "123,456,789" or  "123;456;789".
 * Default separator is comma ","
 * </pre>
 * @author Ben
 *
 */
public class ListOfNumbersValidator implements IValidator {
	private String errorMessage;
	private String separator;

	public ListOfNumbersValidator(String newErrorMessageKey, String newSeparator) {
		this.errorMessage = newErrorMessageKey;
		this.separator = newSeparator;
		if (StringUtils.isBlank(this.errorMessage)) {
			this.errorMessage = "Expected a list of numbers separated by '{1}'.";
		}
		if (StringUtils.isBlank(this.separator)) {
			this.separator = ",";
		}
	}

	public void validate(String value) throws ValidationException {
		if (!StringUtils.isBlank(value)) {
			// validate input
			StringTokenizer st = new StringTokenizer(value, separator, false);
			while (st.hasMoreElements()) {
				// check if long
				String check = st.nextToken();
				if (StringUtils.isNotBlank(check)) {
					try {
						Long.parseLong(check.trim());
					}
					catch (NumberFormatException e) {
						throw new ValidationException(TextService.getString(errorMessage, separator ));
					}
				}
			}
		}
	}

	public String getTooltip() {
		return TextService.getString("validator.ListOfNumbersValidator.tooltip", separator );
	}

}
