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

import org.webguitoolkit.ui.controls.IActionControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;

/**
 * This control provides a vertical list of controls with the capability of sorting them with drag and drop. The ability to sort
 * the items can be switched off and on. <br>
 * A functionality to move items between list is provided as well as the ability to add and remove items dynamically.
 * 
 * <pre>
 * ISortableList simpleList = factory.createList(viewConnector);
 * for (int i = 0; i &lt; 5; i++) {
 * 	simpleList.addListItem(factory.createLabel(simpleList, &quot;Item &quot; + i));
 * }
 * </pre>
 * 
 * Usually you will use ICanvas as list items. But in principal it does not matter. You can provide a Listener with will be called
 * whenever sort was performed. <code>The onSort()</code> will get the ordered list of items. <br>
 * <br>
 * <b>CSS classes:</b> wgtList, wgtListItem
 * 
 * @author Peter
 * 
 */
public interface ISortableList extends IComposite, IActionControl {

	/**
	 * Add a ICanvas
	 * 
	 * @param child the Canvas
	 */
	void add(IBaseControl child);

	/**
	 * Set the listener that will be called after a sort was performed.
	 * 
	 * @param listener the callback
	 */
	void setListener(IListListener listener);

	/**
	 * @return the current list of controls in the current order.
	 */
	List getListItems();

	/**
	 * @param id the id of the control you are loooking for
	 * @return the list item with the id or NULL.
	 * 
	 */
	IBaseControl getListItem(String id);

	/**
	 * Add an item to the end of the list. Should be used to add dynamically item to the list. Will perform a redraw if the list
	 * is already drawn.
	 * 
	 * @param item add this item to the list
	 */
	void addListItem(IBaseControl item);

	/**
	 * Remove an item from the list. Should be used to remove dynamically item to the list. Will perform a redraw if the list is
	 * already drawn.
	 * 
	 * @param item to be removed item.
	 */
	void removeListItem(IBaseControl item);

	/**
	 * Enable sorting within the list by means of drag and drop.
	 */
	void enable();

	/**
	 * Disable sorting.
	 */
	void disable();

	/**
	 * Connect this list with another one to be able to drag and drop items between lists. You can connect multiple list with each
	 * other. <br>
	 * Note that you have to call this on the other list as well.
	 * 
	 * @param other the list to connect with
	 */
	void connectWith(ISortableList other);

	/**
	 * Make list items with a specific id not sortable to introduce something like sections in your list
	 * 
	 * @param id the element id that shall be not sortable
	 */
	void setUnsortable(String id);

	/**
	 * Make list items only sortable at a specific handle
	 * 
	 * @param id the element id that shall be the handle to sort
	 */
	void setHandle(String id);
}
