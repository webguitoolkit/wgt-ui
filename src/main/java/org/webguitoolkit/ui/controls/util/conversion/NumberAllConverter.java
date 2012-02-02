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
import java.text.ParsePosition;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;


/**
 * <pre>
 * Converts a Number into Float and formats it.
 * Locale aware.
 * <pre>
 *
 */
public class NumberAllConverter implements IConverter{
		String pattern;
		DecimalFormat formatter;
		protected Number lastNumber;
		
		public NumberAllConverter(String pattern){
            this.pattern = pattern;
		}
		/**
		 * conmvert from any number object into string using the format pattern.
		 */
		public Object convert(Class type, Object value) {
			if (value==null) return "";
			if (value instanceof String) return value; // TODO fix bug in parse
			if (type!=String.class || !(value instanceof Number)) {
				throw new WGTException("can only convert into string");
			}
            formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()) );

			return formatter.format(value);
		}
		/**
		 * check if the input string is a legal number. It does NOT return a Numberobject.
		 * Itseem that this is a bug in BeanUtilBean class (maybe we shopuld update)
		 * Try: BeanUtilBean.setProperty( Pump, "hours", new Float(24))
		 * with Pump.setHours(int value) taking an int as input.
		 * You will see, that it will store '0', because it converts the float into string,
		 * then back to int, and get an parseexception.
		 * 
		 * @return the input string
		 * @exception ConversionException if the input if not a legal number
		 */
		public Object parse(String textRep) throws ConversionException {
			// no mandatory field, empty means value zero.
			if (StringUtils.isEmpty(textRep)) return new Float(0f);
			formatter = new DecimalFormat(pattern, new DecimalFormatSymbols(TextService.getLocale()));
			ParsePosition pos = new ParsePosition(0);
			lastNumber = formatter.parse(textRep, pos);
			// to avoid null pointer -return float with 0
			if(lastNumber==null)
			throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number."));
			if (pos.getIndex() < textRep.length()) {
			throw new ConversionException(TextService.getString("converter.NumberConverter.message@Cannot convert into number."));
			}			
			// must decode into string using default locale of system
			// because it is going to be translated back!
			// why is this fucking BeanUtil converter stuff so complicated and error-prone?
			return lastNumber.toString();
		}
		public Number getLastNumber() {
			return lastNumber;
		}
		public void setLastNumber(Number lastNumber) {
			this.lastNumber = lastNumber;
		}		
	

}
