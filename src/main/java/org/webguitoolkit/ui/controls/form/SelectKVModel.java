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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * Simple Select model which uses a key value pair to fill the select model
 * Usage: selectComponent.setModel( new SelectKVModel().loadList(myMap) );
 * @author arno
 *
 */
public class SelectKVModel implements ISelectModel {

	protected Collection<String[]> optlist;

	public Collection<String[]> getOptions() {
		return optlist;
	}

	// this shouldn't be in a model
	public IConverter getConverter() {
		return null;
	}

	public SelectKVModel loadList(Map<String, String> list) {
		optlist = new ArrayList<String[]>(list.size());
		for (Entry<String, String> it : list.entrySet()) {
			optlist.add(new String[]{ it.getKey(), it.getValue() });
		}
		return this;
	}

}
