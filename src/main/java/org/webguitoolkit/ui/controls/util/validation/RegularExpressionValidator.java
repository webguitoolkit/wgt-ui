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

import java.util.regex.Pattern;

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * checks that the entered string has the exact length.
 * number of chars is given in the constructor
 * </pre>
 */
public class RegularExpressionValidator implements IValidator {
	//GENIERIC REG EXP
	public static final String UPPERCHARSTANDARDTEXTPATTERN	= "^[A-Z]{1}[\\w\\-\\.\\s]{2,19}$";
	public static final String STANDARDTEXTPATTERN			= "^[\\w\\-\\.\\s]{3,20}$";
	public static final String ISNUMBER						= "^[0-9]{1,}$";
	public static final String ISNOTANUMBER					= "^[\\D]{1,}$";
	public static final String ISACHAR						= "^[\\w]{1,}$";
	public static final String ISNOTACHAR					= "^[\\W]{1,}$";
	public static final String VERSIONNUMBER				= "^[0-9\\.]{1,6}$";
	public static final String HEXVAL						= "^0x([\\u]{1,}$";
	public static final String HEXNUMBERS					= "^([\\u]{1,}$";
	public static final String LONGSTANDARDTEXTPATTERN		= "^[\\w\\-\\.\\s]{1,}$";
	public static final String LONGTEXTPATTERN				= "^[a-zA-Z\\_\\-\\.\\s]{1,}$";
	public static final String MEDIUMTEXTPATTERN			= "^[a-zA-Z\\_\\-\\.\\s]{1,40}$";
	public static final String TEXTONLYPATTERN				= "^[a-zA-Z]{1,}$";
	public static final String LONGTEXTONLYPATTERN			= "^[a-zA-Z\\s]{5,25}$";
	public static final String SHORTNUMBERONLY				= "^[0-9\\-]{1,4}$";
	public static final String TEXTWITHSPACEPATTERN			= "^[a-zA-Z\\s]{1,}$";

	// all URLs allow port numbers
	public static final String ISURL = "^(http+(s){0,1}:\\/\\/){0,1}(www){0,1}(?:[\\w-]+\\.)+[a-zA-Z]{2,4}$";
	public static final String ISHTTPURL_WITHPATH = "^(http+(s){0,1}:\\/\\/){0,1}(www){0,1}(?:[\\w-]+\\.)+[a-zA-Z]{2,4}(\\:[\\d]{2,5}){0,1}(\\/[A-Za-z0-9_.]+){0,}$";

	// doesn't allow % and -
	public static final String ISHTTPURL_WITHPARAMETERS = "^(http+(s){0,1}:\\/\\/){0,1}(www){0,1}(?:[\\w-]+\\.)+[a-zA-Z]{2,4}(\\:[\\d]{2,5}){0,1}(\\/[A-Za-z0-9_.]*){0,}(\\?){0,1}(\\w+=\\w+){0,1}(&\\w+=\\w+){0,}$";
	public static final String ISFTPURL = "^(ftp+(s){0,1}:\\/\\/){0,1}(?:[\\w-]+\\.)+[a-zA-Z]{2,4}(\\:[\\d]{2,5}){0,1}$";
	public static final String ISIP = "^(http+(s){0,1}:\\/\\/){0,1}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	public static final String ISIP_HTTPURL_WITHPARAMETERS = "^(http+(s){0,1}:\\/\\/){0,1}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\:[\\d]{2,5}){0,1}(\\/[A-Za-z0-9_.]*){0,}(\\?){0,1}(\\w+=\\w+){0,1}(&\\w+=\\w+){0,}$";
	public static final String ISIP_FTPURL = "^(ftp+(s){0,1}:\\/\\/){0,1}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\:[\\d]{2,5}){0,1}$";
	private String customMessage;
	
	
	private String selectedPattern;
	private IBaseControl controlToBeVisible;
	
	public RegularExpressionValidator() {
	}

	/**
	 * validation should only be fired if the given basecontrol is visible
	 * 
	 * @param newPattern
	 * @param controlToBeVisible
	 */
	public RegularExpressionValidator(String newPattern, IBaseControl controlToBeVisible) {
		this(newPattern,
				TextService.getString("validator.RegularExpressionValidator.message@The entered value does not met the constraints."),
				controlToBeVisible);
	}

	public RegularExpressionValidator(String newPattern) {
		this(newPattern,
				TextService.getString("validator.RegularExpressionValidator.message@The entered value does not met the constraints."));
	}
	
	public RegularExpressionValidator(String newPattern, String customMessage) {
		this(newPattern, customMessage, null);
	}

	/**
	 * validation should only be fired if the given basecontrol is visible
	 * 
	 * @param newPattern
	 * @param customMessage
	 * @param controlToBeVisible
	 */
	public RegularExpressionValidator(String newPattern, String customMessage, IBaseControl controlToBeVisible) {
		this.selectedPattern = newPattern;
		this.customMessage = customMessage;
		this.controlToBeVisible = controlToBeVisible;
	}
	
	public void validate(String value) throws ValidationException {
		if (value != null && getSelectedPattern() != null && (controlToBeVisible == null || controlToBeVisible.isVisible())) {

		    boolean regExMatch = Pattern.matches(getSelectedPattern(), value);
			if(!regExMatch){
				throw new ValidationException(getCustomMessage());	
			}
		}
	}

	public String getSelectedPattern() {
		return selectedPattern;
	}

	public void setSelectedPattern(String selectedPattern) {
		this.selectedPattern = selectedPattern;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}
	
	public String getTooltip() {
		return TextService.getString("validator.RegularExpressionValidator.tooltip" );
	}

}
