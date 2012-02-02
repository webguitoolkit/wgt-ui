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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * <pre>
 * This retrieves the entries of the select box from the properties, The
 * keys are taken from the defaults you pass in. So we are constructing the
 * translationkeys by using the prefix and the key from the
 * defaults. Also the key from the defaults is the actual string which is stored in the
 * property of the Select.
 *
 * In the properties is for example the entries:
 * MySelect.key1 = myvalue
 * MySelect.key2 = thyValue
 * 
 * When you create a Model with prefix 'MySelect.' then you end up with
 * a select which will display two options: 'myvalue' and 'thyValue'.
 * The keys stored in the property of the compound bean will be
 * 'key1' and 'key2' for the options respectively.
 * (Note don't forget the dot at the end of the prefix!)
 * </pre>
 * 
 * @see TextService.prefixKey(..)
 * 
 * @author arno@schatz.to
 *
 */
public class TranslatableSelectModel implements ISelectModel, Comparator {
	
	protected String prefix;
	protected String[][] defaults; 
	
	public TranslatableSelectModel(){}
	/**
	 * convenience constructor to pass in the required parameters.
	 * @param prefix
	 * @param defaults
	 */
	public TranslatableSelectModel(String prefix, String[][] defaults) {
		super();
		this.prefix = prefix;
		this.defaults = defaults;
	}
	public TranslatableSelectModel(String prefix) {
		super();
		this.prefix = prefix;
		this.defaults = null;
	}

	public java.util.Collection getOptions() {
		List dd = TextService.prefixKey(prefix, defaults);
		// lets sort it.
		Collections.sort(dd, this);
		return dd;
	}

	public String[][] getDefaults() {
		return defaults;
	}

	public void setDefaults(String[][] defaults) {
		this.defaults = defaults;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * this method sorts the entries in the list.
	 * However, you can override it to change the sorting.
	 * Sort by alphabetic value is implemented.
	 * @param o1 is of type String[2] (key,value)
	 * @param o2
	 * @return
	 */
	public int compare(Object o1, Object o2) {
		return ((String[]) o1)[1].compareToIgnoreCase(((String[]) o2)[1]);
	}
	public IConverter getConverter() {
		return null;
	}
}
