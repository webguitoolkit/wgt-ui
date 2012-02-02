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
 * This does no conversion, it just check if the string follow basic email-address conventions.
 * It check if there is a '@' and a '.' in the string. THis is donw while parsing. nothing done in convert mode.
 * if the string is empty it does no check.
 * </pre>
 * @author Arno
 *
 */
public class EmailValidator implements IValidator {

	public void validate(String value) throws ValidationException {
		if (StringUtils.isNotEmpty(value) && (value.indexOf('@')<0 || value.indexOf('.')<0)) {
			throw new ValidationException(TextService.getString("validator.EmailConverter.message@" +
					"An emailaddress must contain a '@' and a dot '.'"));
		}
	}

	public String getTooltip() {
		return TextService.getString("validator.EmailValidator.tooltip");
	}
}
