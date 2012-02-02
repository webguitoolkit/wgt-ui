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
package org.webguitoolkit.ui.controls.util;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabListener;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.ITable;


/**
 * <pre>
 * Abstract table and tab listener class for master detail views with tabs.
 * </pre>
 */
public abstract class AbstractMasterDetailListener extends AbstractTableListener implements ITabListener {
	private ITable table;
	private ITabStrip tabstrip;

	public AbstractMasterDetailListener(ITable table, ITabStrip tabstrip) {
		super();
		this.table = table;
		this.tabstrip = tabstrip;
	}

	public void onRowSelection(ITable table, int row) {
		loadTab(table.getRow(row), tabstrip.getTab(((StandardTabStrip) tabstrip).getSelectedTab()));
	}

	public boolean onTabChange(ITab old, ITab selected, ClientEvent event) {
		if (leaveTab(table.getSelectedRow(), selected )) {
			loadTab(table.getSelectedRow(), selected );
			return true;
		}
		return false;
	}

	public abstract void loadTab(IDataBag row, ITab tab);

	public abstract boolean leaveTab(IDataBag row, ITab tab);

}
