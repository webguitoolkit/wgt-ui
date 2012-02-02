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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * Validates if an input is set but only if the control or its parent or any specified control is visible.
 * 
 * @author sattlera
 */
public class MandatoryIfVisibleValidator extends MandatoryValidator implements IValidator {
	private static final Log log = LogFactory.getLog(MandatoryIfVisibleValidator.class);

	private String errorMessage;
	private IBaseControl control;

	public MandatoryIfVisibleValidator(IBaseControl control) {
		this(control, "validator.MandatoryValidator.message@This field is mandatory.");
	}

	public MandatoryIfVisibleValidator(IBaseControl control, String errorMessage) {
		super(errorMessage);
		this.control = control;
	}

	@Override
	public void validate(String value) throws ValidationException {
		if (control != null && control.isVisible()) {
			super.validate(value);
		}
	}

	@Override
	public String getTooltip() {
		return super.getTooltip();
	}
}
