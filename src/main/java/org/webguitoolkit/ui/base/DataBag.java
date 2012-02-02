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
package org.webguitoolkit.ui.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;

/**
 * Standard implementation of IDataBag
 * 
 * @see com.endress.infoserve.wgt.base.IDataBag
 */
public class DataBag implements IDataBag, Serializable {

	private static final long serialVersionUID = 1L;
	protected Object delegate;
	protected final Map<String, Object> properties = new HashMap<String, Object>();
	protected Map<String, Object> bag = new HashMap<String, Object>();

	/**
	 * @param delegate the wrapped object to save to and load values from
	 */
	public DataBag(Object delegate) {
		setDelegate(delegate);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#addProperty(java.lang.String, java.lang.Object)
	 */
	public void addProperty(String key, Object o) {
		properties.put(key, o);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#get(java.lang.String)
	 */
	public Object get(String key) {
		return getObject(key);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getBool(java.lang.String)
	 */
	public boolean getBool(String key) {
		Object val = getObject(key);
		if (val instanceof String) {
			return "true".equals(val);
		}
		else {
			return ((Boolean)val).booleanValue();
		}
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getDate(java.lang.String)
	 */
	public Date getDate(String key) {
		return (Date)get(key);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getFloat(java.lang.String)
	 */
	public float getFloat(String key) {
		return ((Float)get(key)).floatValue();
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getDouble(java.lang.String)
	 */
	public double getDouble(String key) {
		return ((Double)get(key)).doubleValue();
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getInt(java.lang.String)
	 */
	public int getInt(String key) {
		return ((Integer)get(key)).intValue();
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String key) {
		return (Timestamp)get(key);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getString(java.lang.String)
	 */
	public String getString(String key) {
		return (String)get(key);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getDelegate()
	 */
	public Object getDelegate() {
		return delegate;
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getObject(java.lang.String)
	 */
	public Object getObject(String key) {
		Object result = bag.get(key);
		// if bag containsKey and value is null then we want
		// null as result, do not query properties then
		if (!bag.containsKey(key)) {
			result = properties.get(key);
			if (result == null && delegate != null) {
				result = PropertyAccessor.retrieveProperty(delegate, key);
			}
		}
		return result;
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#save()
	 */
	public void save() {
		for (Entry<String, Object> bagElement : bag.entrySet()) {
			String key = bagElement.getKey();
			Object value = bagElement.getValue();

			try {
				Object o = PropertyUtils.getProperty(delegate, key);
				if (o == null || !o.equals(value))
					BeanUtils.setProperty(delegate, key, value);
			}
			catch (Exception e) {
				properties.put(key, value);
			}
		}
		bag = new HashMap<String, Object>(); // clear the cache
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#setDelegate(java.lang.Object)
	 */
	public void setDelegate(Object delegate) {
		if (delegate instanceof ArrayList)
			throw new RuntimeException("IS THIS REALLY WHAT YOU WANT TO SET AN ARRAYLIST AS DELEGATE ??");
		this.delegate = delegate;
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#setObject(java.lang.String, java.lang.Object)
	 */

	public void setObject(String key, Object value) {
		if (key == null)
			throw new IllegalArgumentException("key must not be null");
		bag.put(key, value);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#undo()
	 */
	public void undo() {
		bag = new HashMap<String, Object>();
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#clearProperties()
	 */
	public void clearProperties() {
		properties.clear();
	}
	/**
	 * this returns the unsaved value, this is the value you would get if you call undo()
	 * on this DataBag.
	 * (use this to detect isDirty- functionality)
	 * 
	 */
	public Object previousValue(String key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		}
		if (delegate != null) {
			return PropertyAccessor.retrieveProperty(delegate, key);
		}
		// hmm, strange one of the other chases should have been true
		// this may indicate a programming error
		return null;

	}

	/**
	 * check if the value of key has change since last save() (or the original value)
	 * and returns true if so.
	 * Note: this implementation does not determine between empty string "" and null,
	 * because the gui form element can't distinguish when getValue() is called.
	 * @param key
	 * @return
	 */
	public boolean isChanged(String key) {
		if (!bag.containsKey(key)) return false;
		
		Object prev = previousValue(key);
		Object now = getObject(key);
		if (("".equals(prev) &&  now == null) || ("".equals(now) && prev == null)) {
			return false;
		}
		return !ObjectUtils.equals(prev, now);
	}

	/**
	 * @see com.endress.infoserve.wgt.base.IDataBag#getProperties()
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.base.IDataBag#remove(java.lang.String)
	 */
	public void remove(String key) {
		bag.remove(key);
	}
}
