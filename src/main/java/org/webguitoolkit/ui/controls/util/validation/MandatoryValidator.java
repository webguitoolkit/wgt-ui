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
 * This validator checks if the field is empty. This throws an exception.
 * 
 * @author arno
 */

public class MandatoryValidator implements IValidator {

	String errorMessage;
	/**
	 * create a converter which check for a mandatory field with
	 * your custom error message.
	 * @param errorMessage -  the string should be translated already
	 */
	public MandatoryValidator(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	public MandatoryValidator() {
		super();
		this.errorMessage = "validator.MandatoryValidator.message@This field is mandatory.";
	}
	
	/**
	 * check if there is an empty entry...
	 */
	public void validate(String value) throws ValidationException {
		if ( StringUtils.isBlank(value) )
			throw new ValidationException(errorMessage);
	}
	
	public String getTooltip() {
		return TextService.getString("validator.MandatoryValidator.tooltip" );
	}

}
