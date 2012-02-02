/*
** Copyright 2004-2009, Endress+Hauser Infoserve GmbH & Co KG  
*/
package org.webguitoolkit.ui.controls.util.validation;

import org.webguitoolkit.ui.controls.util.TextService;

/**
 * A validator class that checks if a text input has more than a minimum 
 * and less than a maximum defined number of characters. Set minLen to 0 
 * to bypass the minimum length validation. According to this set maxLen
 * to 0 to bypass the maximum length validation.
 * 
 * @author Alexander Sattler
 *
 */
public class MinMaxLengthValidator implements IValidator {
	protected int minLen, maxLen;
	protected String messageMin, messageMax;
	
	/**
	 * 
	 * @param minLen the smallest expected text length
	 * @param maxLen the longest expected text length
	 */
	public MinMaxLengthValidator(int minLen, int maxLen) {
		this(minLen, maxLen, TextService.getString("validator.MinMaxLengthValidator.message.min@The field must not have less than {1} characters.", String.valueOf(minLen)), TextService.getString("validator.MinMaxLengthValidator.message.max@The field must not have more than {1} characters.", String.valueOf(maxLen)));
	}
	
	/**
	 * 
	 * @param minLen the smallest expected text length
	 * @param maxLen the longest expected text length
	 * @param errorMessageMin the message to be shown if the minimum length validation fails
	 * @param errorMessageMax the message to be shown if the maximum length validation fails
	 */
	public MinMaxLengthValidator(int minLen, int maxLen, String errorMessageMin, String errorMessageMax) {
		super();
		
		// avoid negative values
		if (minLen < 0) {
			minLen = 0;
		}
		if (maxLen < 0) {
			maxLen = 0;
		}
		
		// switch minimum and maximum length if maxLen is smaller than minLen 
		if (maxLen < minLen) {
			int changeTemp = maxLen;
			maxLen = minLen;
			minLen = changeTemp;
		}
		
		this.minLen = minLen;
		this.maxLen = maxLen;
		this.messageMin = errorMessageMin;
		this.messageMax = errorMessageMax;
	}
	
	/**
	 * @param value the text that has to be validated
	 */
	public void validate (final String value) throws ValidationException {
		if (value!=null && value.length() < minLen) {
			throw new ValidationException(messageMin);
		} else if (value!=null && value.length() > maxLen && maxLen != 0) {
			throw new ValidationException(messageMax);
		}
	}
	
	public final String getTooltip() {
		return TextService.getString("validator.MinMaxLengthValidator.tooltip" );
	}

}
