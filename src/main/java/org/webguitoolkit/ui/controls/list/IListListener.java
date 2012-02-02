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

import java.io.Serializable;

import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * Controller for the SortableList control. It provides call back methods to enable the application developer to perform back end
 * functions like sorting and moving items from one place to the other. <br>
 * There is an default implementation (DefaultListListener) that performs the operations on the underlying controls. This class
 * can be extended to do the processing on the data or back ends. Don't forget to call the super functionality. <br>
 * Example: <code>
 * class MyListListener extends DefaultListListener {
 *    void onSort(ISortableList list, IBaseControl sortedItem, int oldPos, int newPos) {
 *    // do your before sort processing here
 *    super.onSort(list, sortedItem,oldPos, newPos);
 *    // do your post sort processing here
 *    } 
 * }
 * </code>
 * 
 * @author Peter
 * 
 */
public interface IListListener extends Serializable {
	/**
	 * Will be called when a sort within the list was done.
	 * 
	 * @param list the List that was sorted
	 * @param sortedItem the item that was moved
	 * @param oldPos the old position in the list
	 * @param newPos the new position in the list
	 * 
	 */
	void onSort(ISortableList list, IBaseControl sortedItem, int oldPos, int newPos);

	/**
	 * Will be called when a item was dragged out of the list into another one
	 * 
	 * @param list the list from which the item was dragged
	 * @param item the dragged item
	 */
	void onDrag(ISortableList list, IBaseControl item);

	/**
	 * Will be called when a item was dropped into the list.
	 * 
	 * @param connectedList the list from which the item was dragged
	 * @param pos the item was dragged at that position
	 * @param item the item that was dragged and dropped
	 * @param list the list into the item was dropped
	 */
	void onDrop(ISortableList list, IBaseControl item, int pos, ISortableList connectedList);

}
