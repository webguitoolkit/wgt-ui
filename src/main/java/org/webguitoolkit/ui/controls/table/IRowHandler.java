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

import java.io.Serializable;

import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;

/**
 * <pre>
 * A RowHandler is used to change the appearance of rows with different content.
 * </pre>
 */
public interface IRowHandler extends Serializable {
	/**
	 * @param property the property of the row
	 * @return the mapped property
	 */
	public String getMappedProperty(String property);

	/**
	 * @return the style attribute of the row (tr)
	 */
	public String getStyle();

	/**
	 * @param the data object
	 * @return true if the RowHandler can be used to render the object
	 */
	public boolean canHandle(Object o);

	/**
	 * @return the context menu for the row
	 */
	public IContextMenu getContextMenu();
}
