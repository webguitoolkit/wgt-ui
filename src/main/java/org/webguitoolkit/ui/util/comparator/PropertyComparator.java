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

import java.util.Comparator;

import org.webguitoolkit.ui.controls.util.PropertyAccessor;
/**
 * Helper to compare table entries where the property is identified by the
 * name.
 */
/**
 * @author i102454
 *
 */
public class PropertyComparator implements Comparator {
	private String sortProperty;
	private boolean isAscending;

	public PropertyComparator(String sortProperty, boolean isAscending) {
		this.sortProperty = sortProperty;
		this.isAscending = isAscending;
	}

	public int compare(Object o1, Object o2) {
		Object prop1 = PropertyAccessor.retrieveProperty(o1, sortProperty);
		Object prop2 = PropertyAccessor.retrieveProperty(o2, sortProperty);

		if (prop1 == null || prop2 == null)
			return (((prop1==null) && (prop2!=null)) ^ isAscending) ? +1 : -1; // sorting of null value equals empty string
		
		// ignore case while sorting if the comparable object is a string 
		if ((prop1 instanceof String) && (prop1 instanceof String)) {
			String prop1Str = (String) prop1;
			String prop2Str = (String) prop2;
			if (isAscending)
				return prop1Str.compareToIgnoreCase(prop2Str);
			else return prop2Str.compareToIgnoreCase(prop1Str);
			
		} else {
			if (isAscending)
				return ((Comparable) prop1).compareTo(prop2);
			else return ((Comparable) prop2).compareTo(prop1);
		}
	}
}
