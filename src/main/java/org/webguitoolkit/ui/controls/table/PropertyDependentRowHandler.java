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

import java.util.Hashtable;

import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;


/**
 * <pre>
 * Dependent on a property the row representing object contains, the renderer is used.
 * </pre>
 */

public class PropertyDependentRowHandler implements IRowHandler{


	private String property;
	private Hashtable propertyMap;
	private String style;
	private Object value;
	private IContextMenu contextMenu;
	
	public PropertyDependentRowHandler(String property, Object value, String style) {
		super();
		this.property = property;
		this.style = style;
		this.value = value;
	}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Hashtable getPropertyMap() {
		if( propertyMap == null )
			propertyMap = new Hashtable();
		return propertyMap;
	}
	public void addProperyMapping( String columnProperty, String objectProperty ) {
		getPropertyMap().put( columnProperty, objectProperty);
	}
	public void setPropertyMap(Hashtable propertyMap) {
		this.propertyMap = propertyMap;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}

	public boolean canHandle(Object data) {
		if( value instanceof String ){
			return ((String)value).equals( PropertyAccessor.retrieveString(data, property) );
		}
		return PropertyAccessor.retrieveProperty(data, property).equals( value );
	}

	public String getMappedProperty(String property) {
		return (String) getPropertyMap().get(property);
	}
	public IContextMenu getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(IContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}

}
