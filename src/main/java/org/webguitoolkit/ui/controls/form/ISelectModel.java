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
package org.webguitoolkit.ui.controls.form;

import java.io.Serializable;
import java.util.Collection;

import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * used in Select and Table Filter for representing the values that are displayed in the select.
 * 
 * @author Martin
 */
public interface ISelectModel extends Serializable {
	/**
	 * the list of key-value pairs for the select box to be filled.
	 * 
	 * @return List of String[2] first string is the key.
	 */
	Collection<String[]> getOptions();

	/**
	 * @return the converter in use.
	 */
	IConverter getConverter();
}
