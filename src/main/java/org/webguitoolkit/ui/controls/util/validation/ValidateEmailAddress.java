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
 * (c) 2008, Endress&Hauser InfoServe GmbH & Co KG
 * 
 */
package org.webguitoolkit.ui.controls.util.validation;

import java.util.regex.Pattern;

import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Description Validator for email addresses
 * 
 * Checks for valid email addresses with a regular expression pattern.
 * There are different methods for different email address restrictions. Primarily concerning the email domain.
 * All methods expects the email address and the Matching Pattern as input parameter, and an error message as optional parameter.
 * </pre>
 *  
 * @author Lars Broessler
 */
public class ValidateEmailAddress implements IValidator{
	
	public static final String basicEmailPattern			= "^[\\w-]+(?:\\.[\\w-]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,4}$";
	public static final String infoserveEmailPattern		= "^[\\w-]+(?:\\.[\\w-]+)*@infoserve.endress.com$";
	public static final String endressEmailPattern			= "^[\\w-]+(?:\\.[\\w-]+)*@(?:[\\w-]+\\.)?endress\\.+(?:com)";
	
	private String selectedPattern;
	private String message;
	
	public ValidateEmailAddress(String newPattern) {
		super();
		this.selectedPattern = newPattern;
		setMessage(TextService.getString("validator.ValidateEmailAddress.message@The entered email address is not valid. {1}",getSelectedPattern()));
	}
	
	public ValidateEmailAddress() {
		super();
		setMessage(TextService.getString("validator.ValidateEmailAddress.message@The entered email address is not valid. {1}",getSelectedPattern()));
	}

	public ValidateEmailAddress( String newPattern, String newMessage) {
		super();
		this.selectedPattern = newPattern;
		this.message 		 = newMessage;
	}
	
	public void validate(String value) throws ValidationException {
		String pattern = basicEmailPattern;
		if (value != null && getSelectedPattern() != null){
			pattern = getSelectedPattern();
		}	
		
		if (value != null){
			boolean regExMatch = Pattern.matches(pattern, value);
			if(!regExMatch){
				throw new ValidationException(getMessage());	
			}
		}
	}
	
	public String getSelectedPattern() {
		return selectedPattern;
	}

	public void setSelectedPattern(String selectedPattern) {
		this.selectedPattern = selectedPattern;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
	
	public String getTooltip() {
		return TextService.getString("validator.ValidateEmailAddress.tooltip" );
	}

}
