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
package org.webguitoolkit.ui.controls.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * Helper class to access and store properties of an object, it can handle Objects 
 * wrapped with data bags and converts the values via the given converters
 */
public class PropertyAccessor {

	/**
	 * lets see if this is a we want to access a tree
	 * attribute may be specified in dot notation "obj1.att2"
	 * that means that we need to get the object1 first then get the attribute
	 * @return
	 */
	public static Object retrieveProperty(Object data, String att) {
		if (StringUtils.isBlank(att)) {
			throw new WGTException("Trying to access empty property in Bean Class "+data.getClass().getName());
		}
	   	if (data instanceof IDataBag) { // dataobject wants to do retrieving itself?
			return ((IDataBag) data).getObject(att);
		}
		try { 
			return PropertyUtils.getNestedProperty(data, att);
		} catch (NestedNullException nne) {
			return null; // if there is a bean null on the way to the property, just display as null.
		} catch (NoSuchMethodException e) {
			Logger.getLogger( PropertyAccessor.class ).warn("Warning: The property '"+ att+ "' does not exist in Bean Class "+data.getClass().getName());
			return null; // the attribute does not exist
		} catch (Exception e) {
			throw new WGTException("Exception while trying to retrieve: "+att+" from class "
					+data.getClass().getName(),e);
		}				
	}
	public static String retrieveString(Object data, String att) {
		return ConvertUtils.convert(retrieveProperty(data, att));
	}
	/**
	 * convert data using your own converter, which may be configured using the format attribut
	 * For a list of converters see ConvertUtil.
	 * @return
	 */
	public static String retrieveString(Object data, String att, IConverter conv) {
		// use standard converter or custom?
		if (conv==null) {
			return ConvertUtils.convert(retrieveProperty(data, att));			
		} else {
			return (String) conv.convert(String.class, retrieveProperty(data, att));
		}
	}

	/**
	 * convenience class to instanciate object from the specified class.
	 * Should only be called from BaseControl to actually load dependent classes.
	 * It is used to instanciate classses where the name is given in the JSP. BaseControl wants
	 * to control the lifecycles of these classes, so call BaseControls instancefrom method.
	 * 
	 * @throws WGTException
	 */
	public static Object instanceFrom(String clazz) {
	    try {
	        Object object = Class.forName(clazz).newInstance();
	        return object;
	    } catch (InstantiationException e) {
	        throw new WGTException(e);
	    } catch (IllegalAccessException e) {
	        throw new WGTException(e);
	    } catch (ClassNotFoundException e) {
	    	// TODO logging
	    	System.err.println("Looking for class: "+clazz);
	        throw new WGTException(e);
	    }
	}

	/**
	     * value must be of the class of the setter method.
	     * TODO search for setters with arguments other than string, as the context
	     * will always return strings.
	     *  
	     * @param data
	     * @param att
	     * @param value
	     */
	    public static void storeObject(Object data, String att, Object value) {
	        try {
	            BeanUtils.setProperty(data,att,value);
	        } catch (Exception e) {
	          // this line is intentionally left blank
	        }
	    }

	public static void storeProperty( Object data, String att, Object value ) throws ValidationException, ConversionException {
	    if (StringUtils.isEmpty(att)) return;
		if (data instanceof IDataBag){
			((IDataBag)data).setObject(att, value);
			return;
		}
	    String[] attribs = StringUtils.split(att, ".");
	    // loop to -1 to get the object right where we want to store
	    for (int i=0;i<attribs.length-1;i++) {
	        data = retrieveProperty(data, attribs[i]);
	        if (data==null) return;
	    }
	    storeObject(data, att, value);
	    
	}
}
