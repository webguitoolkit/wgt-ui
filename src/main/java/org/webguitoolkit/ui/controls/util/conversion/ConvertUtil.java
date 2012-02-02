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
package org.webguitoolkit.ui.controls.util.conversion;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * <pre>
 * Utility class that provides a set of converters.
 * </pre>
 */
public class ConvertUtil {

	public static final ResourceBundleConverter RESOURCE_BUNDLE_CONVERTER = new ResourceBundleConverter();

	public static final NonEmptyConverter NON_EMPTY_CONVERTER = new NonEmptyConverter();

	public static final CalendarConverter CALENDAR_CONVERTER = new CalendarConverter();

	public static final DateTimeConverter DATE_TIME_CONVERTER = new DateTimeConverter();

	public static final TimeConverter TIME_CONVERTER = new TimeConverter();

	public static final TimestampConverter TIMESTAMP_CONVERTER = new TimestampConverter();

	public static final TimestampTimeConverter TIMESTAMP_TIME_CONVERTER = new TimestampTimeConverter();

	public static final NumberConverter NUMBER_CONVERTER = new NumberConverter("###0.####");

	public static final NumberConverter NUM0_CONVERTER = new NumberConverter("###0");

	public static final NumberConverter NUM1_CONVERTER = new NumberConverter("###0.0");

	public static final NumberConverter NUM2_CONVERTER = new NumberConverter("###0.00");

	public static final NumberConverter NUM3_CONVERTER = new NumberConverter("###0.000");

	public static final NumberConverter NUM4_CONVERTER = new NumberConverter("###0.0000");

	public static final NumberConverter PERRCENT_CONVERTER = new NumberConverter("###0.00%");

	public static final NumberConverterPrecise NUMBER_CONVERTER_PRECISE = new NumberConverterPrecise("###0.####");
	public static final NumberConverterPrecise NUM0_CONVERTER_PRECISE = new NumberConverterPrecise("###0");
	public static final NumberConverterPrecise NUM1_CONVERTER_PRECISE = new NumberConverterPrecise("###0.0");
	public static final NumberConverterPrecise NUM2_CONVERTER_PRECISE = new NumberConverterPrecise("###0.00");
	public static final NumberConverterPrecise NUM3_CONVERTER_PRECISE = new NumberConverterPrecise("###0.000");
	public static final NumberConverterPrecise NUM4_CONVERTER_PRECISE = new NumberConverterPrecise("###0.0000");
	public static final NumberConverterPrecise PERCENT_CONVERTER_PRECISE = new NumberConverterPrecise("###0.00%");

	public static final DateConverter DATE_CONVERTER = new DateConverter();

	public static final String UNI_NBSP = "\u00A0";

	// all public converters
	// holds as key the format string wich can be in any Tag in the JSP format attribute
	protected static Map<String, IConverter> pubConverter;

	public static void addConverter(String key, IConverter conv) {
		pubConverter.put(key, conv);
	}

	static {
		pubConverter = new HashMap<String, IConverter>();
		pubConverter.put("num4", NUM4_CONVERTER);
		pubConverter.put("num3", NUM3_CONVERTER);
		pubConverter.put("num2", NUM2_CONVERTER);
		pubConverter.put("num1", NUM1_CONVERTER);
		pubConverter.put("num0", NUM0_CONVERTER);
		pubConverter.put("percent", PERRCENT_CONVERTER);
		pubConverter.put("Number", NUMBER_CONVERTER);
		pubConverter.put("Timestamp", TIMESTAMP_CONVERTER);
		pubConverter.put("Date", DATE_CONVERTER);
		pubConverter.put("Time", TIME_CONVERTER);
		pubConverter.put("DateTime", DATE_TIME_CONVERTER);
		pubConverter.put("Calendar", CALENDAR_CONVERTER);
		pubConverter.put("nonEmpty", NON_EMPTY_CONVERTER);
		pubConverter.put("resourceBundle", RESOURCE_BUNDLE_CONVERTER);
	}

	// list of all known Converters
	public static IConverter lookup(String[] formats) {
		if (formats == null)
			return null;
		List<IConverter> conis = new ArrayList<IConverter>();
		IConverter conv = null;
		for (int i = 0; i < formats.length; i++) {
			conv = (IConverter)pubConverter.get(formats[i]);
			if (conv != null) { // add them all
				conis.add(conv);
			}
		}
		// if there is more than one converter, we must chain them
		if (conis.size() > 1) {
			conv = new ChainedConverter((IConverter[])conis.toArray(new IConverter[conis.size()]));
		}
		// otherwise conv is still the last converter
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
	 * Converts a Number into Float and formats it. Locale aware.
	 * 
	 * @author e100061
	 * 
	 */
	public static class NumberConverter implements IConverter {
		String pattern;
		DecimalFormat formatter;

		public NumberConverter(String pattern) {
			this.pattern = pattern;
		}

		public Object convert(Class type, Object value) {
			if (value == null)
				return "";
			if (value instanceof String)
				return value; 
			if (type != String.class || !(value instanceof Number)) {
				throw new WGTException("can only convert into string");
			}
			formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));

			return formatter.format(value);
		}

		public Object parse(String textRep) throws ConversionException {
			// no mandatory field, empty means value zero.
			if (StringUtils.isEmpty(textRep))
				return new Float(0f);
			formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));
			ParsePosition pos = new ParsePosition(0);
			Number num = formatter.parse(textRep, pos);
			// to avoid null pointer -return float with 0
			if (num == null)
				throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number."));
			if (pos.getIndex() < textRep.length()) {
				throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number."));
			}
			if (num instanceof Float)
				return num;

			return new Float(num.floatValue());
		}
	}

	/**
	 * Converts a Number into a more precise Double (in comparison to Float) and formats it. Locale aware.
	 * 
	 * @author e100061
	 * 
	 */
	public static class NumberConverterPrecise implements IConverter {
		String pattern;
		DecimalFormat formatter;

		public NumberConverterPrecise(String pattern) {
			this.pattern = pattern;
		}

		public Object convert(Class type, Object value) {
			if (value == null)
				return "";
			if (value instanceof String)
				return value; 
			if (type != String.class || !(value instanceof Number)) {
				throw new WGTException("can only convert into string");
			}
			formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));

			return formatter.format(value);
		}

		public Object parse(String textRep) throws ConversionException {
			// no mandatory field, empty means value zero.
			if (StringUtils.isEmpty(textRep))
				return new Double(0d);
			formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));
			ParsePosition pos = new ParsePosition(0);
			Number num = formatter.parse(textRep, pos);
			// to avoid null pointer -return double with 0
			if (num == null)
				throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number."));
			if (pos.getIndex() < textRep.length()) {
				throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number."));
			}
			if (num instanceof Double)
				return num;

			return new Double(num.doubleValue());
		}
	}

	public static class DateConverter implements IConverter, IDatePickerConverter {

		private TimeZone timezone = null;
		public static final int DATEPATTERN_FULL = DateFormat.FULL;
		public static final int DATEPATTERN_LONG = DateFormat.LONG;
		public static final int DATEPATTERN_MEDIUM = DateFormat.MEDIUM;
		public static final int DATEPATTERN_SHORT = DateFormat.SHORT;
		public static final int DATEPATTERN_SHORT_4YEAR = 99;
		public static final int DEFAULTDATEPATTERN = DATEPATTERN_SHORT_4YEAR;
		public int dateConverterPattern = DEFAULTDATEPATTERN;

		public DateConverter() {
		}

		// With timezone set the time is automatically
		// shown with the right value.
		// When parsing a user input date it is
		// interpreted as timezone date.
		public DateConverter(TimeZone newTimezone) {
			timezone = newTimezone;
		}

		public DateConverter(TimeZone newTimezone, int newDateConverterPattern) {
			this.dateConverterPattern = newDateConverterPattern;
			timezone = newTimezone;
		}

		public DateConverter(int newDateConverterPattern) {
			this.dateConverterPattern = newDateConverterPattern;
		}

		public Object convert(Class type, Object value) {
			if (value == null)
				return "";
			if (value instanceof String)
				return value;
			if (type != String.class)
				throw new WGTException("can only convert into string");
			DateFormat df = getDateFormat();
			if (timezone != null)
				df.setTimeZone(timezone);
			return df.format(value);
		}

		public Object parse(String textRep) throws ConversionException {
			if (StringUtils.isEmpty(textRep))
				return null;
			DateFormat df = getDateFormat();
			if (timezone != null)
				df.setTimeZone(timezone);
			try {
				// MH removed this because the converter failed on 08/09/09 vs. 8/9/09
				// //prevent date parsing like '45.04.09' to '15.05.09' or '29.02.09' to '01.03.09'
				// //and throw ConversionException instead
				// String dateBeforeConversion, dateAfterConversion;
				Object parsedDate;

				// dateBeforeConversion = textRep;
				parsedDate = df.parseObject(textRep);
				// dateAfterConversion = df.format(parsedDate);

				// if (!dateBeforeConversion.equals(dateAfterConversion)) {
				// throw new
				// ConversionException(TextService.getString("converter.DateConverter.message.invalid@This is no valid date, please enter a new one."));
				// }
				return parsedDate;

			}
			catch (ParseException e) {
				throw new ConversionException(TextService.getString(
						"converter.DateConverter.message@Date has the wrong Format, please enter according to today's date: {1}",
						df.format(new Date())), e);
			}
		}

		protected DateFormat getDateFormat() {
			DateFormat df;
			if (dateConverterPattern == DATEPATTERN_SHORT_4YEAR) {
				SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateInstance(DATEPATTERN_SHORT, TextService.getLocale());
				String pattern = sdf.toPattern();
				if( pattern.indexOf("yyyy") < 0 )
					df = new SimpleDateFormat(pattern.replaceAll("yy", "yyyy"), TextService.getLocale());
				else
					df = sdf;
			}
			else {
				df = DateFormat.getDateInstance(dateConverterPattern, TextService.getLocale());
			}
			return df;
		}

		/**
		 * @see org.webguitoolkit.ui.controls.util.conversion.IDatePickerConverter#getDatePattern()
		 */
		public String getDatePattern() {
			return ((SimpleDateFormat)getDateFormat()).toPattern();
		}

		/**
		 * @see org.webguitoolkit.ui.controls.util.conversion.IDatePickerConverter#getDatePickerPattern()
		 */
		public String getDatePickerPattern() {
			return DatePickerPatternConverter.convertDateFormat(getDatePattern());
		}
	}

	public static class TimestampConverter extends DateConverter {

		public Object parse(String textRep) throws ConversionException {
			if (StringUtils.isEmpty(textRep))
				return null;
			return new Timestamp(((Date)super.parse(textRep)).getTime());
		}
	}

	public static class TimestampTimeConverter extends DateTimeConverter {
		public Object parse(String textRep) throws ConversionException {
			if (StringUtils.isEmpty(textRep))
				return null;
			return new Timestamp(((Date)super.parse(textRep)).getTime());
		}
	}

	public static class CalendarConverter extends DateConverter {

		public Object parse(String textRep) throws ConversionException {
			if (StringUtils.isEmpty(textRep))
				return null;
			Date date = ((Date)super.parse(textRep));
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		}

		public Object convert(Class type, Object value) {
			if (value == null)
				return "";
			if (value instanceof String)
				return value;
			if (type != String.class)
				throw new WGTException("can only convert into string");
			DateFormat df = getDateFormat();
			return df.format(((Calendar)value).getTime());
		}

	}

	public static class NonEmptyConverter implements IConverter {

		public Object parse(String textRep) throws ConversionException {
			return textRep;
		}

		public Object convert(Class type, Object value) {
			if (StringUtils.isBlank(((String)value)))
				return UNI_NBSP; // unicode: non-breaking space
			return value;
		}

	}

	public static class DateTimeConverter implements IConverter, IDatePickerConverter {

		private TimeZone timezone = null;

		public DateTimeConverter() {
		}

		// With timezone set the time is automatically
		// shown with the right value.
		// When parsing a user input date it is
		// interpreted as timezone date.
		public DateTimeConverter(TimeZone newTimezone) {
			timezone = newTimezone;
		}

		public Object convert(Class type, Object value) {
			if (type != String.class)
				throw new WGTException("can only convert into string");
			if (value == null)
				return "";
			DateFormat df = getDateFormat();
			if (timezone != null)
				df.setTimeZone(timezone);
			return df.format(value);
		}

		public Object parse(String textRep) throws ConversionException {
			if (StringUtils.isEmpty(textRep))
				return null;
			DateFormat df = getDateFormat();
			if (timezone != null)
				df.setTimeZone(timezone);
			try {
				return df.parseObject(textRep);
			}
			catch (ParseException e) {
				throw new ConversionException(TextService.getString(
						"converter.DateTimeConverter.message@Date has the wrong Format, please enter accoring to todays date: {1}",
						df.format(new Date())), e);
			}
		}

		private DateFormat getDateFormat() {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TextService.getLocale());
			return df;
		}

		/**
		 * @see org.webguitoolkit.ui.controls.util.conversion.IDatePickerConverter#getDatePattern()
		 */
		public String getDatePattern() {
			return ((SimpleDateFormat)getDateFormat()).toPattern();
		}

		/**
		 * @see org.webguitoolkit.ui.controls.util.conversion.IDatePickerConverter#getDatePickerPattern()
		 */
		public String getDatePickerPattern() {
			return DatePickerPatternConverter.convertDateFormat(getDatePattern());
		}

	}

	public static class TimeConverter implements IConverter {

		public Object convert(Class type, Object value) {
			if (type != String.class)
				throw new WGTException("can only convert into string");
			if (value == null)
				return "";
			DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, TextService.getLocale());
			return df.format(value);
		}

		public Object parse(String textRep) {
			return textRep;// leave conversion to beanutils
		}

	}

	public static class ResourceBundleConverter implements IConverter {

		public Object parse(String textRep) throws ConversionException {
			return textRep;
		}

		public Object convert(Class type, Object value) {
			if (type != String.class)
				throw new WGTException("can only convert into string");
			if (value == null || StringUtils.isEmpty((String)value))
				return "";
			return TextService.getString((String)value);
		}

	}

	public static class ConversionException extends ValidationException {

		public ConversionException() {
			super();
		}

		public ConversionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ConversionException(String message) {
			super(message);
		}

	}

	protected static class DatePickerPatternConverter {
		private static String[][] conversionTable = new String[][] { { "A", "%P" }, { "a", "%p" }, { "yyyy", "%Y" }, { "yy", "%y" },
				{ "MMMMM", "%B" }, { "MMMM", "%B" }, { "MMM", "%b" }, { "MM", "%m" }, { "M", "%o" }, { "EEEEEE", "%A" }, { "EEEEE", "%A" },
				{ "EEEE", "%A" }, { "EEE", "%a" }, { "dd", "%d" }, { "d", "%e" }, { "HH", "%H" }, { "H", "%k" }, { "mm", "%M" },
				{ "m", "%M" }, { "ss", "%S" }, { "s", "%S" }, { "hh", "%I" }, { "h", "%l" }, { "S", "%s" } };

		/**
		 * Converts java date format (used in e.g. GridPanel) to PHP date format (used in DateField)
		 * 
		 * @param javaFormat e.g. dd-MM-yyyy
		 * @return e.g. d-m-y
		 */
		public static String convertDateFormat(String javaFormat) {
			String in = javaFormat;
			String out = "";
			for (int i = 0; in.length() > 0; i++) {
				boolean found = false;
				for (int j = 0; j < conversionTable.length; j++) {
					if (in.startsWith(conversionTable[j][0])) {
						out += conversionTable[j][1];
						in = in.substring(conversionTable[j][0].length());
						found = true;
						break;
					}
				}
				if (!found) {
					out += in.substring(0, 1);
					in = in.substring(1);
				}
			}
			return out;
		}
	}

}
