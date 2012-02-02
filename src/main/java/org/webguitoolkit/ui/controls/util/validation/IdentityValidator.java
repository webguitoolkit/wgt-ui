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
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * this checks for identity content with a second Text -field. Especially useful when you want to 
 * make sure that two password fields are identical.
 * set this converter to one of the passwords and pass the reference to the other password field in
 * the constructor.
 * </pre>
 * 
 * @author e100061
 * 
 */
public class IdentityValidator implements IValidator {

	private static final long serialVersionUID = 1L;
	protected IText otherPassword;
	protected String message;

	public IdentityValidator(IText otherPassword, String message) {
		super();
		this.otherPassword = otherPassword;
		this.message = message;
	}

	public IdentityValidator(IText otherPassword) {
		super();
		this.otherPassword = otherPassword;
	}

	public void validate(String value) throws ValidationException {
		// must check against value of other text-field, since the conversion to the bag
		// may not have taken place yet.
		if (!StringUtils.equals(value, otherPassword.getValue())) {
			// should have a message from user, otherwise the user doesn#T know which fields are meant
			if (message == null) {
				message = TextService.getString("validator.IdentityValidator.message@The two fields are not identical.");
			}
			throw new ValidationException(message);
		}
	}

	public String getTooltip() {
		return TextService.getString("validator.IdentityValidator.tooltip");
	}

}
