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

import java.io.Serializable;

import org.apache.commons.beanutils.Converter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * Interface to be implemented by the format converters
 * 
 * @author Martin
 */
public interface IConverter extends Converter, Serializable {
	/**
	 * 
	 * @param textRep the string to parse
	 * @return the converted data as object (Integer, ...)
	 * @throws ConversionException if something went wrong
	 */
	Object parse(String textRep) throws ConversionException;
}
