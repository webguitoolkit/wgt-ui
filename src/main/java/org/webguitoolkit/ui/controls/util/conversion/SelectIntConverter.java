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

import java.text.ParseException;

/**
 * <pre>
 * This class is develop for Select boxes which are bound to Integer Properties. They have 
 * a special requirement as the select box only checks against literal equality for selecting a entry in the
 * select box.
 * </pre>
 */
public class SelectIntConverter extends SelectFloatConverter {

	public SelectIntConverter() {
		super(null, 0);
	}

	public SelectIntConverter(int emptyValue, int defValue) {
		super(new Float(emptyValue), 0, defValue);
	}

	protected Object parse2Type(String textRep) throws ParseException {
		Number num = formatter.parse(textRep);
		if (num instanceof Integer)
			return num;
		
		return new Integer(num.intValue());
	}

}
