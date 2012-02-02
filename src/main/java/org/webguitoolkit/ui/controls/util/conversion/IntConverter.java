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
/**
 * 
 */
package org.webguitoolkit.ui.controls.util.conversion;

import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * <pre>
 * Converter for integer conversion
 * </pre>
 */
public class IntConverter implements IConverter {

	/**
	 * convert the entry into an int and check if it is positive
	 */
	public static IConverter pint = new IntConverter() {
	
		public Object parse(String textRep) throws ConversionException {
			final Integer theInt = (Integer) super.parse(textRep);
			if (theInt.intValue()<0) {
				throw new ConversionException("The Number must be positive.");
			}
			return theInt;
		}
		
	};

	public Object parse(String textRep) throws ConversionException {
		try {
			return new Integer(textRep);
		} catch (NumberFormatException e) {
			throw new ConversionException("Cannot convert \"" +textRep+ "\" into an Integer");
		}
	}

	public Object convert(Class type, Object value) {
		return ""+value;
	}
	
}
