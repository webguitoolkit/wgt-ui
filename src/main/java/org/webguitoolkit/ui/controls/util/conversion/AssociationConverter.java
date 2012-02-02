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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * <b>Converter id to Object</b>
 * <p>
 * The association converter converts a id to a object. Therefore the converter has to know
 * the list of possible results.
 * </p>
 * <p>
 * The converter is used in the AssociationSelectModel.
 * </p>
 * 
 * @see org.webguitoolkit.ui.controls.form.AssociationSelectModel
 * @see org.webguitoolkit.ui.controls.form.OptionGroupController
 */
public class AssociationConverter implements IConverter{

	private String idProperty  = "id";
	private Map posibleValues = new HashMap();
	
	/**
	 * Constructor with the possible values as parameter, assumes that the ident field is 'id'
	 * @param values all possible values
	 */
	public AssociationConverter( Collection values ){
		for (Iterator iter = values.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			Object id = PropertyAccessor.retrieveString(o, idProperty );
			posibleValues.put(id, o);
		}
	}
	/**
	 * Constructor with possible values and ident field as parameters
	 * @param values all possible values
	 * @param idProperty ident field of the values used for conversion
	 */
	public AssociationConverter( Collection values, String idProperty ){
		for (Iterator iter = values.iterator(); iter.hasNext();) {
			Object o = (Object) iter.next();
			Object id = PropertyAccessor.retrieveString(o, idProperty );
			posibleValues.put(id, o);
		}
		this.idProperty = idProperty;
	}
	
	/**
	 * @see org.webguitoolkit.ui.controls.util.conversion.IConverter#parse(String)
	 * @param textRep textual representation of the object
	 * @return the associated object
	 * @throws ConversionException on error during converstion
	 */
	public Object parse(String textRep) throws ConversionException {
		return posibleValues.get( textRep );
	}

	/**
	 * @see org.apache.commons.beanutils.Converter#convert(Class, Object)
	 * @return the String representation
	 * @param type has to be String.class
	 * @param value the object that has to be parsed to string
	 */
	public Object convert(Class type, Object value) {
		if( type == String.class ){
			if( value == null )
				return null;
			return PropertyAccessor.retrieveString(value, idProperty );
		}
		else
			throw new WGTException("can only convert into string");
	}
}
