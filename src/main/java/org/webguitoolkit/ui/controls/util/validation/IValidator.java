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

import java.io.Serializable;

/**
 * <pre>
 * Base class for validators.
 * </pre>
 */
public interface IValidator extends Serializable {
	/**
	 * Does the validation of a string, throws a validation exception if the validation fails
	 * 
	 * @param textRep the text to validate.
	 * @throws ValidationException if something went wrng.
	 */
	void validate(String textRep) throws ValidationException;

	/**
	 * @return a tool tip that can be rendered to the effected FormControl
	 */
	String getTooltip();
}
