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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;

/**
 * @author i102415
 *
 */
public class CollectionConverter implements IConverter{
	
	private static final String DEFAUL_DELIM = ",";

	private String delim = DEFAUL_DELIM;
	private Class collectionClass = ArrayList.class;
	private IConverter itemConverter = new StringConverter();
	
	/**
	 * Standard constructor for StirngConverter as item converter and ArrayList as collection class.
	 */
	public CollectionConverter() {
		this( null, null );
	}
	/**
	 * @param itemConverter the converter for the list items (default is StringConverter)
	 * @param collectionClass the collection class to be instantiated (needs empty constructor, default is ArrayList.class) 
	 */
	public CollectionConverter( IConverter itemConverter, Class collectionClass ) {
		if( collectionClass != null )
			this.collectionClass = collectionClass;
		if( itemConverter != null )
			this.itemConverter = itemConverter;
	}
	
	/**
	 * @see org.webguitoolkit.ui.controls.util.conversion.IConverter#parse(java.lang.String)
	 */
	public Object parse(String textRep) throws ConversionException {
		try {
			Collection col = (Collection) collectionClass.newInstance();
			String[] values = textRep.split( getDelim() );
			for (int i = 0; i < values.length; i++) {
				col.add( itemConverter.parse( values[i] ) );
			}
			return col;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ConversionException("Collection class could not be instanciated: ", e );
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ConversionException("No public default constructor: ", e );
		}
	}

	/**
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	public Object convert(Class type, Object value) {
		if( type == String.class ){
			if( value == null )
				return null;

			String returnStirng = "";
			Collection o = (Collection) value;
			for (Iterator iter = o.iterator(); iter.hasNext();) {
				returnStirng += itemConverter.convert(String.class, iter.next());
				if( iter.hasNext() )
					returnStirng += getDelim();
			}
			return returnStirng;
		}
		else
			throw new WGTException("can only convert into string");
	}
	
	/**
	 * The delimiter is the character that separates the String values that gets converted
	 * 
	 * @param delim the delimiter to set
	 */
	public void setDelim(String delim) {
		this.delim = delim;
	}
	/**
	 * @return the delimiter
	 */
	public String getDelim() {
		return delim;
	}
}
