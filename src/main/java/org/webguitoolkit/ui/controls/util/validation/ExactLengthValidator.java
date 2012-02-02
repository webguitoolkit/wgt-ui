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

import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * does no conversion other than checking that the entered string has the exact length.
 * number of chars is given in the constructor
 * </pre>
 */
public class ExactLengthValidator implements IValidator {
	protected int exLen;

	public ExactLengthValidator(int exLen) {
		super();
		this.exLen = exLen;
	}

	public void validate(String value) throws ValidationException {
		if (value==null || value.length() != exLen) {
			throw new ValidationException(TextService.getString(
				"validator.ExactLengthValidator.message@The field must have exactly {1} characters.", String.valueOf(exLen)));
		}
	}
	public String getTooltip() {
		return TextService.getString("validator.ExactLengthValidator.tooltip", String.valueOf(exLen) );
	}

}