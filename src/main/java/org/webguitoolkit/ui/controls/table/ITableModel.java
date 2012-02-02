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
/*
 */
package org.webguitoolkit.ui.controls.table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * The table model provides the data displayed in the table dependent on the filters that have
 * to be applied.
 * 
 * The most used model is the DefaultTableModel where you load all data into the model. However this
 * can be critical when a lot of data is displayed by the table, therefore you have to write your own
 * table model where the data is only loaded per portions.
 * </pre>
 */
public interface ITableModel extends Serializable {
	/**
	 * filter is the array of the filters set for the search. It contains the name and the value of the filerDropdown in one entry
	 * of the Map. The name is taken from the JSP definition, the name-attribute.
	 * 
	 * implicitFilter are the filter for the column (textinputs). The Map contains for each column (for which the user wants to
	 * search) one entry. Each entry has the columnname and the value to filter. ImplicitFilters must be aware of wildcards search
	 * using the '*' .
	 * 
	 * sortColumn is only != null, if the user wants to sort. Then is contains the columnname as defined in the JSP.
	 * 
	 * In filterModel you are enabled to interpret the value given with the filterModel.
	 * 
	 * @param filterModel key is the name of the filter, value is the Model of the filter
	 * @param implicitFilter
	 * @param propertyRenderer contains renderer for columns, if available. property = key
	 * @param sortProperty TODO
	 * @param asc sortorder, ascending or descending
	 * @return number of Dataelements
	 */
	public int applyFilterAndSorting(Map filterModel, Map filter, Map implicitFilter, Map propertyRenderer, String sortProperty, boolean asc);

	/**
	 * return one Object per row. must comply to bean specification the property attribute of the column tag refer to the
	 * properties of these returned Objects
	 * 
	 * @param startrow
	 * @param pagelen
	 * @return
	 */
	public List navigate(int startrow, int pagelen);
}
