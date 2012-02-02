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

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;

/**
 * <pre>
 * Dependent on the class the row represents the renderer is used.
 * </pre>
 *
 */
public class ClassDependentRowHandler implements IRowHandler {

	private Class matchClass;
	private Hashtable propertyMap;
	private String style;
	private IContextMenu contextMenu;
	
	public ClassDependentRowHandler(Class matchClass, String style) {
		super();
		this.matchClass = matchClass;
		this.style = style;
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

	public boolean canHandle(Object o) {
		if( o instanceof IDataBag ){
			if( ((IDataBag)o).getDelegate() != null )
				o = ((IDataBag)o).getDelegate();
		}
		return (matchClass.isAssignableFrom(o.getClass()) || o.getClass().isInstance(matchClass));
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
