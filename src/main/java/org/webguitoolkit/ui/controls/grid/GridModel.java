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
package org.webguitoolkit.ui.controls.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;

/**
 * @author i102415
 * 
 */
public class GridModel implements IGridModel {
	private List<IDataBag> tableData = new ArrayList<IDataBag>();
	private List<IDataBag> filteredData = new ArrayList<IDataBag>();

	/**
	 * @param data
	 */
	public void setTableData(List<IDataBag> data) {
		this.tableData = data;
		this.filteredData = data;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.grid.IGridModel#getData(java.lang.String, java.lang.String, java.util.Hashtable, int, int)
	 */
	public List<IDataBag> getData(String sortColumn, String sortDirection, Hashtable<String, String> parameters, int start, int rows) {
		applySorting( sortColumn,  sortDirection);
		int end = filteredData.size() < (start+rows) ? filteredData.size() : (start+rows);
		return filteredData.subList(start, end);
	}

	/**
	 * @param sortColumn
	 * @param sortDirection
	 */
	private void applySorting(String sortColumn, String sortDirection) {
		Collections.sort(filteredData, new BagComparator(sortColumn, sortDirection) );
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.grid.IGridModel#getCount(java.util.Hashtable)
	 */
	public int getCount(Hashtable<String, String> parameters) {
		applyFilter(parameters);
		return filteredData.size();
	}

	/**
	 * @param parameters
	 */
	private void applyFilter(Hashtable<String, String> parameters) {
		List<IDataBag> filtered = new ArrayList<IDataBag>( tableData );
		for( String key : parameters.keySet() ){
			String value = parameters.get(key);
			for(Iterator<IDataBag> iter = filtered.iterator(); iter.hasNext();){
				IDataBag bag = iter.next();
				if( !matches(bag, key, value))
					iter.remove();
			}
		}
		filteredData = filtered;
	}
	
	private boolean matches(IDataBag bag, String field, String value){
		Object o = bag.get(field);
		if( o == null )
			return false;
		if( o instanceof String ){
			return ((String)o).startsWith(value);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.grid.IGridModel#getBag(java.lang.String)
	 */
	public IDataBag getBag(int rowId) {
		return filteredData.get(rowId);
	}
	
	class BagComparator implements Comparator<IDataBag>{
		
		private final String sortCol;
		private final String direction;

		public BagComparator( String sortCol, String direction ){
			this.sortCol = sortCol;
			this.direction = direction;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(IDataBag o1, IDataBag o2) {
			Object v1 = o1.get(sortCol);
			Object v2 = o2.get(sortCol);
			int result=0;
			if( v1 == null && v2 == null )
				result=0;
			else if( v1 != null && v2 == null )
				result=1;
			else if( v1 == null && v2 != null )
				result=-1;
			else if(v1 instanceof Comparable<?> && v2.getClass().isAssignableFrom(v1.getClass()))
				result= ((Comparable)v1).compareTo(v2);
			
			if("desc".equals(direction))
				result = result*-1;
			return result;
		}
		
	}
}
