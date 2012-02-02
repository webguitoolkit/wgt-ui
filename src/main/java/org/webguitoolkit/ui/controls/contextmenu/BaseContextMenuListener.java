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
package org.webguitoolkit.ui.controls.contextmenu;

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.tree.ITree;

/**
 * Base class for ActionListeners placed on a context menu. It takes care on<br>
 * handling the different types of components that can have a context menu.
 * 
 * The client only needs to overwrite the onAction( ClientEvent, ... ) instead<br>
 * of finding out on witch component the action is called.
 */
public class BaseContextMenuListener implements IContextMenuListener {

	/**
	 * @see com.endress.infoserve.wgt.controls.contextmenu.IContextMenuListener#onAction(com.endress.infoserve.wgt.controls.event.ClientEvent,
	 *      com.endress.infoserve.wgt.controls.IBaseControl)
	 * @param event the event object
	 * @param control the control
	 */
	public void onAction(ClientEvent event, IBaseControl control) {
		event.getSource().getPage().sendWarn(
				"Method not implemented: " + this.getClass().getName() + ".onAction( ClientEvent event, BaseControl control )");
	}

	/**
	 * @see com.endress.infoserve.wgt.controls.contextmenu.IContextMenuListener#onAction(com.endress.infoserve.wgt.controls.event.ClientEvent,
	 *      com.endress.infoserve.wgt.controls.table.ITable, int)
	 * 
	 * @param event the event object
	 * @param table the table
	 * @param row the row id of the table where the context menu was assigned to
	 */
	public void onAction(ClientEvent event, ITable table, int row) {
		event.getSource().getPage().sendWarn(
				"Method not implemented: " + this.getClass().getName() + ".onAction( ClientEvent event, Table table, int row )");
	}

	/**
	 * @see com.endress.infoserve.wgt.controls.contextmenu.IContextMenuListener#onAction(com.endress.infoserve.wgt.controls.event.ClientEvent,
	 *      com.endress.infoserve.wgt.controls.tree.ITree, java.lang.String)
	 * @param event the event object
	 * @param tree the tree
	 * @param nodeId the tree node id
	 */
	public void onAction(ClientEvent event, ITree tree, String nodeId) {
		event.getSource().getPage().sendWarn(
				"Method not implemented: " + this.getClass().getName() + ".onAction( ClientEvent event, Tree tree, String nodeId )");
	}
}
