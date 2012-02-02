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
 * does no conversion other than checking that the entered string is shorter than length.
 * number of chars is given in the constructor
 * </pre>
 */
public class MaxLengthValidator implements IValidator {
	protected int maxLen;
	protected String message;
	
	public MaxLengthValidator(int exLen) {
		super();
		this.maxLen = exLen;
		this.message = TextService.getString("validator.MaxLengthValidator.message@The field must not have more than {1} characters.", String.valueOf(maxLen));
	}
	
	public MaxLengthValidator(int exLen, String errorMessage) {
		super();
		this.maxLen = exLen;
		this.message = errorMessage;
	}
	
	public void validate(String value) throws ValidationException {
		if (value!=null && value.length() > maxLen) {
			throw new ValidationException(message);
		}
	}
	public String getTooltip() {
		return TextService.getString("validator.MaxLengthValidator.tooltip" );
	}

}
