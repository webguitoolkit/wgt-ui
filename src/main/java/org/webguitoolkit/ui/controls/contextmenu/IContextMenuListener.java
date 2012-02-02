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

import java.io.Serializable;

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.tree.ITree;

/**
 * <p>
 * The context menu listener handles events that occur when clicking on a context menu item.
 * </p>
 * there are three methods depending on the control that was used.
 * 
 * @author Martin
 */
public interface IContextMenuListener extends Serializable {

	int EVENT_TYPE_ACTION = 1;
	int EVENT_TYPE_TABLE_ACTION = 2;
	int EVENT_TYPE_TREE_ENTRY_ACTION = 3;

	/**
	 * 
	 * @param event the event
	 * @param control the control raisig the event
	 */
	void onAction(ClientEvent event, IBaseControl control);

	/**
	 * 
	 * @param event the event
	 * @param table the table raising the event
	 * @param row the row number in the table where the event was created
	 */
	void onAction(ClientEvent event, ITable table, int row);

	/**
	 * 
	 * @param event the event
	 * @param tree the tree raising the event
	 * @param nodeId the node on which the event was created
	 */
	void onAction(ClientEvent event, ITree tree, String nodeId);

}
