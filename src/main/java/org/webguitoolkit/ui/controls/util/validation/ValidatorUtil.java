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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Utility for easy access to some much used validators.
 * </pre>
 */
public class ValidatorUtil {

	public static final EmailValidator EMAIL_VALIDATOR = new EmailValidator();

	public static final MandatoryValidator MANDATORY_VALIDATOR = new MandatoryValidator();

	public static final DateTimeValidator DATE_TIME_VALIDATOR = new DateTimeValidator();

	public static final NumberValidator NUMBER_VALIDATOR = new NumberValidator("###0.####");

	public static final NumberValidator NUM0_VALIDATOR = new NumberValidator("###0");

	public static final NumberValidator NUM1_VALIDATOR = new NumberValidator("###0.0");

	public static final NumberValidator NUM2_VALIDATOR = new NumberValidator("###0.00");

	public static final NumberValidator NUM3_VALIDATOR = new NumberValidator("###0.000");

	public static final NumberValidator NUM4_VALIDATOR = new NumberValidator("###0.0000");

	public static final DateValidator DATE_VALIDATOR = new DateValidator();

	public static final String UNI_NBSP = "\u00A0";

	// all public converters
	// holds as key the format string wich can be in any Tag in the JSP format attribute
	protected static Map publicValidator;

	public static void addValidator(String key, IValidator conv) {
		publicValidator.put(key, conv);
	}

	static {
		publicValidator = new HashMap();
		publicValidator.put("num4", NUM4_VALIDATOR);
		publicValidator.put("num3", NUM3_VALIDATOR);
		publicValidator.put("num2", NUM2_VALIDATOR);
		publicValidator.put("num1", NUM1_VALIDATOR);
		publicValidator.put("num0", NUM0_VALIDATOR);
		publicValidator.put("Number", NUMBER_VALIDATOR);
		publicValidator.put("Date", DATE_VALIDATOR);
		publicValidator.put("DateTime", DATE_TIME_VALIDATOR);
		publicValidator.put("man", MANDATORY_VALIDATOR);
		publicValidator.put("email", EMAIL_VALIDATOR);
	}

	// list of all known Converters
	public static IValidator lookup(String[] formats) {
		if (formats == null)
			return null;
		List conis = new ArrayList();
		IValidator conv = null;
		for (int i = 0; i < formats.length; i++) {
			conv = (IValidator)publicValidator.get(formats[i]);
			if (conv != null) { // add them all
				conis.add(conv);
			}
		}
		// otherwise converter is still the last converter
		// or null.
		return conv;
	}

	public static boolean containsNumberFormat(String[] formats) {
		if (formats == null)
			return false;
		for (int i = 0; i < formats.length; i++) {
			if (formats[i].startsWith("num"))
				return true;
			if (formats[i].equals("Number"))
				return true;
		}
		return false;
	}

	/**
	 * Locale aware.
	 * 
	 * @author e100061
	 * 
	 */
	public static class NumberValidator implements IValidator {
		String pattern;

		public NumberValidator(String pattern) {
			this.pattern = pattern;
		}

		public void validate(String textRep) throws ValidationException {
			DecimalFormat formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));
			ParsePosition pos = new ParsePosition(0);
			Number num = formatter.parse(textRep, pos);
			if (num == null)
				throw new ValidationException(TextService.getString("validator.NumberValidator.message@Cannot convert into number."));
			if (pos.getIndex() < textRep.length()) {
				throw new ValidationException(TextService.getString("validator.NumberValidator.message@Cannot convert into number."));
			}
		}

		public String getTooltip() {
			return TextService.getString("validator.NumberValidator.tooltip");
		}

	}

	public static class DateValidator implements IValidator {
		public void validate(String textRep) throws ValidationException {
			if (StringUtils.isEmpty(textRep))
				return;

			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, TextService.getLocale());
			try {
				df.parseObject(textRep);
			}
			catch (ParseException e) {
				throw new ValidationException(TextService.getString(
						"validator.DateValidator.message@Date has the wrong Format, please enter accoring to todays date: {1}",
						df.format(new Date())), e);
			}
		}

		public String getTooltip() {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, TextService.getLocale());
			return TextService.getString("validator.DateValidator.tooltip@Use format like {1}",
					df.format(new GregorianCalendar().getTime()));

		}

	}

	public static class DateTimeValidator implements IValidator {
		public void validate(String textRep) throws ValidationException {
			if (StringUtils.isEmpty(textRep))
				return;

			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TextService.getLocale());
			try {
				df.parseObject(textRep);
			}
			catch (ParseException e) {
				throw new ValidationException(TextService.getString(
						"validator.DateTimeValidator.message@Date has the wrong Format, please enter accoring to todays date: {1}",
						df.format(new Date())), e);
			}
		}

		public String getTooltip() {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TextService.getLocale());

			return TextService.getString("validator.DateTimeValidator.tooltip@@Use format like {1}",
					df.format(new GregorianCalendar().getTime()));
		}

	}
}
