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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.form.Select;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * <pre>
 * This class is develop for Select boxes which are bound to Numerical Properties. They have 
 * a special requirement as the select box only checks against literal equality for selecting a entry in the
 * select box.
 * Therefore, the convert and convert algorithm must ensure that entered can be literally equal
 * to the key from the options-list. The parse mechanism must be the same.
 * For Example if the options-list looks like:
 *  {{ "50.0", "fifty"}
 *   { "60.0", "sixty"}}
 * the convert procedure must convert using Locale.ENGLISH and one fraction-digit.
 * Locale.ENGLISH is always used in this Converter, you must specify how many fraction digits to 
 * use.
 * </pre>
 * @author Arno Schatz
 *
 */
public class SelectFloatConverter implements IConverter {
	
	protected Float emptyValue;
	// formatter to do the defined conversion.
	protected DecimalFormat formatter;
	// default value if empty
	protected Float defValue = null;
	
	public SelectFloatConverter(Float emptyValue, int fractionDigit) {
		this.emptyValue = emptyValue;
		// formatter with defined conversion rules, not depending on the Locale.getdeault(), which is dependent 
		// on the installation.
		formatter = new DecimalFormat("#", new DecimalFormatSymbols(Locale.ENGLISH));
		formatter.setMaximumFractionDigits(fractionDigit);
		formatter.setMinimumFractionDigits(fractionDigit);
	}
	/**
	 * it doesn't really make sense to have emptyValue and defValue and setPrompt on the select component.
	 * But you may set emptyValue and defValue to automaticall cconvert the emptyValue in the databean to def
	 * defValue. Only defValue will be shown if either the data provided if null or equal emptyValue.
	 * @param emptyValue    the value were we recognise that the provided data is empty
	 * @param fractionDigit converting the numbers with hoe many digits after the point. We always convert using Locale.ENGLISH
	 * @param defValue      if we detect an empty value (see above) the we will convert to defaultValue and save defValue in Databean.
	 */
	public SelectFloatConverter(Float emptyValue, int fractionDigit, float defValue) {
		this(emptyValue, fractionDigit);
		this.defValue = new Float(defValue);
	}
	/**
	 * convenience constructor, see 
	 * @param emptyValue
	 * @param fractionDigit
	 */
	public SelectFloatConverter(float emptyValue, int fractionDigit) {
		this(new Float(emptyValue), fractionDigit);
	}
	public Object parse(String textRep) throws ConversionException {
		// this is coming from a select box, so we don't need special 
		// language and number parsing as in NumberConverter
		
		// empty in parsing is coming from select.setPrompt() using emptystring as key.
		if (Select.EMPTY.equals(textRep) ||  StringUtils.isEmpty(textRep)) {
			// default specified?
			if (defValue!=null) {
				return defValue;
			} else {
				// use emptyValue even if null
				return emptyValue;
			}
		}
		try {
			return parse2Type(textRep);
		} catch (ParseException e) {
			throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number: " + textRep));
		}
	}
	/**
	 * does the actualyy pparsing into the target type, you ca
	 * override this method to parse into other type.
	 * Note this is neccessary because of a bug in BeanUtils, which will be solved with
	 * version 1.8.0 when it is released.
	 * @param textRep
	 * @return
	 * @throws ParseException
	 */
	protected Object parse2Type(String textRep) throws ParseException {
		Number num = formatter.parse(textRep);
		if (num instanceof Float)
			return num;
		
		return new Float(num.floatValue());
	}

	public Object convert(Class type, Object value) {
		// must use our formatter so the property converted to a string can actually match
		// an option from the option-list
		// if we are using emptyValue-mechanism, convert it to Selects- empty entry.
		if (equalNumber(emptyValue, value) || value==null) {
			// if we use default take take otherwise show it to select as being empty.
			if (defValue!=null) {
				value = defValue;
			} else {
				return Select.EMPTY;
			}
		}
		// no defined emptyValue for null, so return just null to protect formatter.
		if (value==null) return null;
		
		return formatter.format(value);
	}
	public DecimalFormat getFormatter() {
		return formatter;
	}
	/**
	 * compare two parameter if they are numbers, their floatValues will be compared. If 
	 * they are not number just equals is being called. Where both null leads to false.
	 * @param n1
	 * @param n2
	 * @return
	 */
	public boolean equalNumber(Object n1, Object n2) {
		if (n1==null || n2==null) return false;
		if (!(n1 instanceof Number && n2 instanceof Number)) {
			return n1.equals(n2);
		}
		return ((Number) n1).floatValue() == ((Number) n2).floatValue();
	}
	/**
	 * you can role your own formatter if you want. Of course this will make the fractionDigits
	 * obsolete.
	 *
	 * @param formatter
	 */
	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
	}

}
