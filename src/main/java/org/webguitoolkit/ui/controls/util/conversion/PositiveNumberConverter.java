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

import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverter;


/**
 * <pre>
 * Converter for positive numbers (Float)
 * </pre>
 */
public class PositiveNumberConverter extends NumberConverter {

	public static IConverter pn0 = new PositiveNumberConverter("###0");
	public static IConverter pn1 = new PositiveNumberConverter("###0.0");
	public static IConverter pn2 = new PositiveNumberConverter("###0.00");

	public PositiveNumberConverter(String pattern) {
		super(pattern);
	}

	public Object parse(String textRep) throws ConversionException {
		Float f = (Float) super.parse(textRep);
		if (f.floatValue() < 0) {
			throw new ConversionException(TextService.getString(
					"converter.PositiveNumberConverter.message@The entered Number must be positive."));
		}
		return f;
	}

}
