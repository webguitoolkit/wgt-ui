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
import java.util.Date;
import java.util.Map;

/**
 * 
 * <p>
 * A DataBag is a container/wrapper for objects that are used in an WGT application. By default the DataBag provides access to the
 * fields of its underlying delegate (POJO). The accessor methods of the databag can also navigate thru the hierarchy of objects,
 * e.g. to get the name of a street in an address use "address.street" what will call getAddress().getStreet().
 * </p>
 * <p>
 * Basically a DataBag has the functionality to enable the application programmer to extend existing model objects with additional
 * fields calculated from the delegates attributes in order to show them in the UI.
 * </p>
 * 
 * We use the following terms here:<br>
 * <ul>
 * <li><b>bag:</b> is the cache that the application works on. all changes are written to the bag when a set method is called. With
 * save() the data is transfered to the delegate or the properties list if the key is not resolvable in the delegate.</li>
 * <li><b>properties:</b> are transient fields which have not corresponding named field in the delegate, e.g. for calculated data </li>
 * <li><b>delegate:</b> the model object that the application is working with.</li>
 * </ul>
 * 
 * @author Peter
 * @author Martin
 * @author Arno
 * 
 */
public interface IDataBag extends Serializable {
	/**
	 * Returns the value for the key. Searches the the bag first if nothing was found lookup the properties and finally tries to
	 * access the delegate
	 * 
	 * @param key the key or field name
	 * @return the object
	 */
	Object get(String key);

	/**
	 * Removes the entry specified by the key from the internal bag.
	 * @param key
	 */
	void remove (String key);
	
	/**
	 * Convenience method for float values
	 * 
	 * @param key the key or field name
	 * @return the float value
	 */
	float getFloat(String key);

	/**
	 * Convenience method for double values
	 * 
	 * @param key the key or field name
	 * @return the double value
	 */
	double getDouble(String key);

	/**
	 * Convenience method for int
	 * 
	 * @param key the key or field name
	 * @return the int value
	 */
	int getInt(String key);

	/**
	 * Convenience method for boolean
	 * 
	 * @param key the key or field name
	 * @return the boolean value
	 */
	boolean getBool(String key);

	/**
	 * Convenience method for Timestamp
	 * 
	 * @param key the key or field name
	 * @return the Timestamp value
	 */
	Timestamp getTimestamp(String key);

	/**
	 * Convenience method for Date
	 * 
	 * @param key the key or field name
	 * @return the Date value
	 */
	Date getDate(String key);

	/**
	 * Convenience method for String
	 * 
	 * @param key the key or field name
	 * @return the String value
	 */
	String getString(String key);

	/**
	 * Only for Framework internal purposes - DO NOT CALL FROM APPLICATION
	 * 
	 * @param key the key or field name
	 * @return the object
	 */
	Object getObject(String key);

	/**
	 * Only for Framework internal purposes - DO NOT CALL FROM APPLICATION
	 * 
	 * @param key the key or field name
	 * @param value the value
	 */
	void setObject(String key, Object value);

	/**
	 * Add a transient field which have not corresponding field in the delegate, e.g. for calculated data
	 * 
	 * @param key the key or field name
	 * @param value the value
	 */
	void addProperty(String key, Object value);

	/**
	 * All properties which have a counterpart in the delegate (Java bean naming convention), are transfered to the delegate
	 * object (if present). Bag items that are not in the delegate are saved to the properties map for later use.
	 */
	void save();

	/**
	 * @return the delegate object
	 */
	Object getDelegate();

	/**
	 * 
	 * @param delegate the object to save to and load data from
	 */
	void setDelegate(Object delegate);

	/**
	 * revert changes since last call to save()
	 */
	void undo();

	/**
	 * empty the properties map
	 */
	void clearProperties();
	/**
	 * return the previous Value of this key. That is the value since the last
	 * save(), that also the same as if you would call undo() first then get the value.
	 * @param key
	 * @return
	 */
	public abstract Object previousValue(String key);

	public abstract boolean isChanged(String key);

	/**
	 * returns all explicitly set properties of the current databag
	 * 
	 * @return a map containing all databag properties
	 */
	Map<String, Object> getProperties();

}
