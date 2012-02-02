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
/*
 */
package org.webguitoolkit.ui.controls.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.AssociationConverter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * <pre>
 * This class populates a SelectBox. it assumes the collection you pass in with
 * selectionList is an association, using the parameters id, and name.
 * </pre>
 * 
 * @author Arno Schatz
 * 
 */
public class AssociationSelectModel extends DefaultSelectModel implements ISelectModel {
	// Select box will use this property for HTML: option value=".identProperty."
	protected String identProperty = "id";
	// Select box will use this property in HTML: <option>.displayProperty.</option>
	protected String displayProperty = "name";
	// sort option by name
	protected boolean sort = false;

	protected IConverter converter;

	public AssociationSelectModel(boolean newSort) {
		super();
		this.sort = newSort;
	}

	public AssociationSelectModel(String newIdentProperty, String newDisplayProperty, boolean newSort) {
		super();
		this.sort = newSort;
		this.identProperty = newIdentProperty;
		this.displayProperty = newDisplayProperty;
	}

	/**
	 * set properties, leave unsorted
	 * 
	 * @param newIdentProperty
	 * @param newDisplayProperty
	 */
	public AssociationSelectModel(String newIdentProperty, String newDisplayProperty) {
		super();
		this.identProperty = newIdentProperty;
		this.displayProperty = newDisplayProperty;
	}

	public AssociationSelectModel() {
		super();
	}

	/**
	 * members of the collection have to IPersistable and INameable
	 * 
	 * @param bos
	 */
	@Override
	public void setOptions(Collection bos) {
		converter = new AssociationConverter(bos, identProperty);
		List theList = new ArrayList(bos.size());
		super.setOptions(theList);
		for (Iterator it = bos.iterator(); it.hasNext();) {
			Object bo = it.next();
			String id = PropertyAccessor.retrieveString(bo, identProperty);
			String name = getDispalyName(bo);
			String[] ddentry = new String[] { id, name };
			theList.add(ddentry);
		}
		if (sort) {
			sort();
		}
	}

	/**
	 * get an Object for the givn key. The key is returned by the AJAX call. get it through Select.getSelectedKey(), then call
	 * this method to get the Object which was selected.
	 * 
	 * @deprecated use getConverter().parse( key );
	 */
	@Deprecated
	public Object associatedObject(String key) {
		try {
			return getConverter().parse(key);
		}
		catch (ConversionException e) {
			throw new WGTException("Error converting key!", e);
		}
	}

	public String getDisplayProperty() {
		return displayProperty;
	}

	/**
	 * sets the displayed property, by default it is "name"
	 */
	public void setDisplayProperty(String displayProperty) {
		this.displayProperty = displayProperty;
	}

	public String getIdentProperty() {
		return identProperty;
	}

	/**
	 * sets the ident property (the value of the option tag), by default it is "id"
	 */
	public void setIdentProperty(String identProperty) {
		this.identProperty = identProperty;
	}

	/**
	 * return the key for for the Object given. use to call Select.selectKey()
	 * 
	 * @deprecated use (String)getConverter().convert( String.class, o );
	 */
	@Deprecated
	public String key4Object(Object o) {
		return (String)getConverter().convert(String.class, o);
	}

	@Override
	public IConverter getConverter() {
		return converter;
	}

	protected String getDispalyName(Object bo) {
		String name = PropertyAccessor.retrieveString(bo, displayProperty);
		return name;
	}
}
