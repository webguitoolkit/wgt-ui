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
package org.webguitoolkit.ui.controls.table;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.webguitoolkit.ui.controls.form.AssociationSelectModel;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;


/**
 * <pre>
 * The association filter model knows about the objects reference to another
 * Type. It assumes, that the given list, contains references to the named
 * association. The associated Object must be of type IPersistable and
 * INameable.
 * 
 * The filter displays the all Names found in the list, from the reference.
 * </pre>
 * 
 * @author Arno Schatz
 */
public class AssociationFilterModel extends AssociationSelectModel implements IFilterModel{
	private String referencedProperty;

	/**
	 * @param referencedProperty
	 *          the name of the attribute in the depending objects leading to the
	 *          filter object, e.g. "master" if the "detail" has a getMaster()
	 *          method.
	 */
	public AssociationFilterModel(String referencedProperty) {
		this(referencedProperty, false);
	}
	
	/**
	 * @param referencedProperty
	 *          the name of the attribute in the depending objects leading to the
	 *          filter object, e.g. "master" if the "detail" has a getMaster()
	 *          method.
	 * @param sort
	 * 			if filter should be sorted
	 */
	public AssociationFilterModel(String referencedProperty, boolean sort) {
		this.referencedProperty = referencedProperty;
		this.sort = sort;
	}

	/**
	 * Fill the selection model in order to enable filtering on a table.
	 * 
	 * @param col
	 *          the collection of depending objects, e.g. the "details" in a
	 *          "master-detail" relationship
	 */
	public void fill(Collection col) {
		Object fObj;
		Set myBOs = new HashSet();
		for (Iterator it = col.iterator(); it.hasNext();) {
			Object tabBo = it.next();
			fObj = PropertyAccessor.retrieveProperty(tabBo, referencedProperty);
			if (fObj != null) {
				// collect the filter choices
				if (fObj instanceof Collection) {
					for (Iterator itc = ((Collection) fObj).iterator(); itc.hasNext();) {
						myBOs.add(itc.next());
					}
				}
				else {
					myBOs.add(fObj);
				}
			}
		}
		setOptions(myBOs);
	}

	/**
	 * 
	 * Check that an object identified by the oid does not match the filter.
	 * 
	 * Lets assume the table contains tanks and we want to filter for Locations the
	 * oid is the oid of the Location we want to filter for (the value of the
	 * select box). In the selection of the drop down we get an oid,
	 * 
	 * @param oid
	 *          the objects id to be checked. Must be convertible to Long.
	 * @return true if the entry with oid is NOT getting filtered out. Looking at
	 *         Object bo.
	 */
	public boolean checkFilter(String oid, Object bo) {
		 // filterObject
		Object fObj = PropertyAccessor.retrieveProperty(bo, referencedProperty);
		if (fObj == null)
			return false;
		// if reference object is collection search in all members
		// this means that there is a many to many association
		if (fObj instanceof Collection) {
			for (Iterator it = ((Collection) fObj).iterator(); it.hasNext();) {
				boolean check = oid.equals(""+PropertyAccessor.retrieveProperty(it.next(), getIdentProperty()));
				if (check) {
					return true;
				}
			}
			return false;
		}

		return oid.equals(""+PropertyAccessor.retrieveProperty(fObj, getIdentProperty()));
	}
	
	public void init( Collection tableData ){
	}
}
