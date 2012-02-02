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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ColumnConfig implements Serializable {
	private static final String COLUMN_SEPARATOR = ";";
	private static final String ROWCOUNT_SEPARATOR = "#";

	private int rowCount = -1;
	List<ColumnSetting> allCols = null;

	public ColumnConfig() {
		allCols = new ArrayList<ColumnSetting>();
	}

	public ColumnConfig(String asString) {
		if (asString.indexOf(ROWCOUNT_SEPARATOR) >= 0) {
			String rowCountStirng = asString.substring(asString.indexOf(ROWCOUNT_SEPARATOR) + 1);
			try {
				setRowCount(Integer.parseInt(rowCountStirng));
			}
			catch (Exception e) {// if Numberformat exception is thrown do nothing.
				Logger.getLogger(this.getClass()).warn("error parsing rowcount: " + rowCountStirng);
			}
			asString = asString.substring(0, asString.indexOf(ROWCOUNT_SEPARATOR));
		}
		String[] cols = asString.split(COLUMN_SEPARATOR);
		allCols = new ArrayList<ColumnSetting>();
		for (int i = 0; i < cols.length; i++) {
			allCols.add(new ColumnSetting(cols[i]));
		}
	}

	@Override
	public String toString() {
		String asString = "";
		for (Iterator iter = allCols.iterator(); iter.hasNext();) {
			asString += ((ColumnSetting)iter.next()).toString();
			if (iter.hasNext())
				asString += COLUMN_SEPARATOR;
		}
		if (rowCount > 0) {
			asString += (ROWCOUNT_SEPARATOR + rowCount);
		}
		return asString;
	}

	public int getSize() {
		return allCols.size();
	}

	public ColumnSetting getSetting(int index) {
		return allCols.get(index);
	}

	public void remove(int index) {
		allCols.remove(index);
	}

	public void addSetting(int index, String property, String width) {
		allCols.add(index, new ColumnSetting(property, width));
	}

	public void addSetting(String property, String width) {
		allCols.add(new ColumnSetting(property, width));
	}

	/**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

}