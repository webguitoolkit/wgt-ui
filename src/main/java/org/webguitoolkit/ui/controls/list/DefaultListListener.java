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

import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * Default implementation of the IListListener. Intended to be extended by the application programmer to perform back
 * end tasks.
 * 
 * @author Peter
 * 
 */
public class DefaultListListener implements IListListener {
	/**
	 * set the parent of the passed item to the other list.
	 * 
	 * @see org.webguitoolkit.ui.controls.list.IListListener#onDrop(org.webguitoolkit.ui.controls.list.ISortableList,
	 *      org.webguitoolkit.ui.controls.IBaseControl, org.webguitoolkit.ui.controls.list.ISortableList)
	 */
	public void onDrop(ISortableList list, IBaseControl item, int pos, ISortableList connectedList) {
		// do the integration into list. Will be added at the end
		((BaseControl) item).setParent((BaseControl) list);
		// now move the item to the right position
		List listItems = list.getListItems();
		listItems.add(pos,item);
		listItems.remove(listItems.size()-1);
	}

	/**
	 * Does nothing at all. Provides a ability to perform back end function on data by overwriting
	 * 
	 * @see org.webguitoolkit.ui.controls.list.IListListener#onDrag(org.webguitoolkit.ui.controls.list.ISortableList,
	 *      org.webguitoolkit.ui.controls.IBaseControl)
	 */
	public void onDrag(ISortableList list, IBaseControl item) {
		// intentionally left empty since the remove is done in the onReceive
	}

	/**
	 * Performs the re-ordering of the list items.
	 * 
	 * @see org.webguitoolkit.ui.controls.list.IListListener#onSort(org.webguitoolkit.ui.controls.list.ISortableList,
	 *      org.webguitoolkit.ui.controls.IBaseControl, int, int)
	 */
	public void onSort(ISortableList list, IBaseControl sortedItem, int oldPos, int newPos) {
		List listItems = list.getListItems();
		listItems.remove(oldPos);
		listItems.add(newPos, sortedItem);
	}

}
