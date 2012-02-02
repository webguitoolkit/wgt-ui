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
package org.webguitoolkit.ui.util.comparator;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * This converter converts a text to enumeration object and backwards
 * 
 * @author Martin
 */
public class EnumerationConverter implements IConverter{

	private Class<?> enumClass;
	
	/**
	 * @param enumClass the enum class of the object
	 */
	public EnumerationConverter( Class<?> enumClass ){
		this.enumClass = enumClass;
	}
	
	/**
	 * @param textRep the text representation of the value
	 * @return the enumeration object
	 * @throws ConversionException if there is a error on converstion
	 */
	public Object parse(String textRep) throws ConversionException {
		enumClass.getEnumConstants();
		Object[] values = enumClass.getEnumConstants();
		for (Object value : values)
			if (value.toString().equals(textRep))
				return value;
		return null;
	}

	/**
	 * @param type the type (String.class)
	 * @param value the enumeration object
	 * @return the text representation of the value
	 */
	public Object convert(Class type, Object value) {
		if (type == String.class) {
			if (value == null)
				return null;
			return ((Enum) value).toString();
		} else
			throw new WGTException("can only convert into string");
	}

}
