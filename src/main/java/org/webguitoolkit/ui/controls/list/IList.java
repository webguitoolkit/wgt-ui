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
package org.webguitoolkit.ui.controls.list;

import java.util.List;

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;

/**
 * Simple representation of a ul li list, this can be used for some jQuery plugins or 
 * the jQuery mobile edition.
 * 
 * @author i01002415
 */
public interface IList extends IComposite {

	/**
	 * sets the style of the surrounding List element (Ul)
	 * 
	 * @param ulCSS the style sheet class name
	 */
	void setListCSS(String ulCSS);

	/**
	 * sets the style of the single list items
	 * 
	 * @param liCSS the style sheet class name
	 */
	void setListItemCSS(String liCSS);

	/**
	 * removes a list item
	 * 
	 * @param item
	 */
	void removeListItem(IBaseControl item);

	/**
	 * @return the html ids of the list items
	 */
	List<String> getIds();

	/**
	 * @return all enties of the list
	 */
	List<? extends IBaseControl> getListItems();

	/**
	 * @param id the HTML id
	 * @return the control placed in the list item
	 */
	IBaseControl getListItem(String id);

}
