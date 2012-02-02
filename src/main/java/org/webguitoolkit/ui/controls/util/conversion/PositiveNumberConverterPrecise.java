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
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverterPrecise;

/**
 * <pre>
 * Converter for positive numbers (Double)
 * </pre>
 */
public class PositiveNumberConverterPrecise extends NumberConverterPrecise {

	public static IConverter pn0 = new PositiveNumberConverterPrecise("###0");
	public static IConverter pn1 = new PositiveNumberConverterPrecise("###0.0");
	public static IConverter pn2 = new PositiveNumberConverterPrecise("###0.00");

	public PositiveNumberConverterPrecise(String pattern) {
		super(pattern);
	}

	public Object parse(String textRep) throws ConversionException {
		Double d = (Double)super.parse(textRep);
		if (d.doubleValue() < 0) {
			throw new ConversionException(
					TextService.getString("converter.PositiveNumberConverter.message@The entered Number must be positive."));
		}
		return d;
	}

}
