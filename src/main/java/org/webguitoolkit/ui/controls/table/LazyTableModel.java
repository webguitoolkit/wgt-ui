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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;

/**
 * <pre>
 * Use this Table Model if there is a lot of data loaded lazy.
 * 
 * Caution, if you are using filters on fields that have not been loaded initially this approach is not working.
 * </pre>
 */
public abstract class LazyTableModel extends DefaultTableModel {

	private static final String LOADED_FLAG = "_loadedFlag";
	
	public List navigate(int startrow, int rows) {
		List bags = super.navigate(startrow, rows);
		for( Iterator iter = bags.iterator(); iter.hasNext();){
			IDataBag bag = (IDataBag) iter.next();
			if( !"true".equals( bag.getString(LOADED_FLAG))){
				fillBag(bag);
				bag.addProperty(LOADED_FLAG, "true");
			}
		}
		return bags;
	}

	public void setTableDataAndWrapWithDataBag(Collection allRows) {
		List tableData = new ArrayList();
		for( Iterator iter = allRows.iterator(); iter.hasNext(); )
			tableData.add(WebGuiFactory.getInstance().createDataBag(iter.next()));
		super.setTableData( tableData );
	}
	
	public abstract void fillBag( IDataBag bag );
}
