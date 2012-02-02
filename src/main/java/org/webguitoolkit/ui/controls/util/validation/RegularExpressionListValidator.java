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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Validates a string against the regular expressions given in a list. By default, a disjunction of all patterns is made. Change matchType to use conjunction.
 * </pre>
 */
public class RegularExpressionListValidator implements IValidator {

	public static final int MATCHTYPE_OR = 0;
	public static final int MATCHTYPE_AND = 1;

	private String customMessage;
	private List<String> patternList;
	private IBaseControl controlToBeVisible;
	private int matchType = MATCHTYPE_OR;

	public RegularExpressionListValidator() {
		this(TextService.getString("validator.RegularExpressionValidator.message@The entered value does not met the constraints."),
				null);
	}

	public RegularExpressionListValidator(String customMessage) {
		this(customMessage, null);
	}

	/**
	 * validation should only be fired if the given basecontrol is visible
	 * 
	 * @param controlToBeVisible
	 */
	public RegularExpressionListValidator(IBaseControl controlToBeVisible) {
		this(TextService.getString("validator.RegularExpressionValidator.message@The entered value does not met the constraints."),
				controlToBeVisible);
	}

	/**
	 * validation should only be fired if the given basecontrol is visible
	 * 
	 * @param customMessage
	 * @param controlToBeVisible
	 */
	public RegularExpressionListValidator(String customMessage, IBaseControl controlToBeVisible) {
		this.patternList = new ArrayList<String>();
		this.customMessage = customMessage;
		this.controlToBeVisible = controlToBeVisible;
	}

	public void validate(String value) throws ValidationException {
		if (value != null && patternList != null && !patternList.isEmpty()
				&& (controlToBeVisible == null || controlToBeVisible.isVisible())) {
			boolean matchConsideringMatchtype = false;

			for (String pattern : patternList) {
				boolean regExMatch = Pattern.matches(pattern, value);

				if (matchType == MATCHTYPE_OR) { // valid, if one pattern matches
					if (regExMatch) {
						matchConsideringMatchtype = true;
						break;
					}
				}
				else if (matchType == MATCHTYPE_AND) { // only valid, if all patterns match
					if (!regExMatch) {
						matchConsideringMatchtype = false;
						break;
					}
					else {
						matchConsideringMatchtype = true;
					}
				}
			}

			if (!matchConsideringMatchtype) {
				throw new ValidationException(getCustomMessage());
			}
		}
	}

	public void addPattern(String pattern) {
		patternList.add(pattern);
	}

	public boolean removePattern(String pattern) {
		return patternList.remove(pattern);
	}

	public void addAllPatterns(List<String> patternList) {
		patternList.addAll(patternList);
	}

	public boolean removeAllPatterns(List<String> patternList) {
		return patternList.removeAll(patternList);
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public List<String> getPatternList() {
		return patternList;
	}

	public void setPatternList(List<String> patternList) {
		this.patternList = patternList;
	}

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}

	public String getTooltip() {
		return TextService.getString("validator.RegularExpressionListValidator.tooltip");
	}
}
