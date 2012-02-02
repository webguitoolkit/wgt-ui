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
 * (c) 2005, Endress&Hauser InfoServe GmbH & Co KG
 */
package org.webguitoolkit.ui.controls.table;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.form.ISelectModel;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil;
import org.webguitoolkit.ui.util.comparator.PropertyComparator;
import org.webguitoolkit.ui.util.comparator.ToStringComparator;


/**
 * Description of DefaultTableModel: <br>
 * The default implementation of a table model backed by a list of "table rows"
 * that the caller provides vie <code>setTableData(List)</code> <br>
 * The usage may look like this: <br>
 * <tt>
 * 	List table = new ArrayList();
 table.add(new TestBean("AAA"));
 table.add(new TestBean("BBB"));
 table.add(new TestBean("CCC"));
 
 ITableModel tm = new DefaultTableModel();
 tm.setTableData(table);
 tm.applyFilterAndSorting(Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, null, "name");
 List result = tm.navigate(0,5);
 * </tt>
 * 
 * @author Arno Schatz, Peter Zaretzke
 * 
 */
public class DefaultTreeTableModel extends DefaultTableModel {
	// data to be displayed
	protected Class groupBy;
	
	public DefaultTreeTableModel( Class groupBy ){
		this.groupBy = groupBy;
	}

	/**
	 * Perform a pattern matching on the specified columns. Remove all resultRows
	 * that do not match with the specified property.
	 * 
	 * @param wildcards
	 *          is a Map with pairs of "property name" and "pattern", i.e. "name",
	 *          "Ta*". For the Patterns check java.util.regex
	 */
	@Override
	protected void applyWildcards(Map wildcards, Map columsMap) {

		for (Iterator it = wildcards.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			String property = (String) e.getKey();
			String pattern = (String) e.getValue();
			if (StringUtils.isEmpty(property) || StringUtils.isEmpty(pattern)) {
				continue;
			}
			Pattern p = compilePattern(pattern);
			// check all rows column-wise against the patterns
			boolean removeChildren = false;
			for (Iterator rowit = resultRows.iterator(); rowit.hasNext();) {
				Object row = rowit.next();
				if( row instanceof IDataBag ){
					//if( ((IDataBag)row).getDelegate().getClass().isAssignableFrom( groupBy )  ){
					if( groupBy.isAssignableFrom( ((IDataBag)row).getDelegate().getClass() )){
						Object value = PropertyAccessor.retrieveProperty(row, property);
						if (value instanceof String) {
							// filter string columns
							Matcher m = p.matcher((String) value);
							if (!m.matches()) {
								removeChildren = true;
								rowit.remove();
								continue;
							}
						}
						else if (value instanceof Long || value instanceof Integer || value instanceof Short) {
							if (!matchNumeric(pattern, value, p)) {
								removeChildren = true;
								rowit.remove();
								continue;
							}
						}
						else if (value instanceof Double || value instanceof Float) {
							if (!matchNumericFloating(pattern, value, p)) {
								removeChildren = true;
								rowit.remove();
								continue;
							}
						}
						else if (value instanceof Timestamp || value instanceof Date) {
							String sDate = (String) ConvertUtil.DATE_TIME_CONVERTER.convert(String.class, value);
							Matcher m = p.matcher(String.valueOf(sDate));
							if (!m.matches()) {
								removeChildren = true;
								rowit.remove();
								continue;
							}
						}
						else if (value == null) {
							removeChildren = true;
							rowit.remove();
							continue;
						}
						else {
							// try to get renderer
							if (columsMap.get(property) != null) {
								TableColumn col = (TableColumn) columsMap.get(property);
								IColumnRenderer renderer = col.getRenderer();
								value = renderer.renderAsPlainText(row, col);
								Matcher m = p.matcher(String.valueOf(value));
								if (!m.matches()) {
									removeChildren = true;
									rowit.remove();
									continue;
								}
							}
							else {
								// try default string representation
								// works well for Long, Int ...
								Matcher m = p.matcher(String.valueOf(value));
								if (!m.matches()) {
									removeChildren = true;
									rowit.remove();
									continue;
								}
							}
						}
						removeChildren = false;
					}
					else if( removeChildren ){
						rowit.remove();
					}
				}
			}
		}
	}


	/**
	 * Apply the filter (drop-down boxes) to the table rows. All rows that do not
	 * match one of the applied filters are removed form the resultRows
	 * collection. The remaining rows are available via the navigate() method.
	 * 
	 * @param filterModels
	 *          a map containing the filter names and the ISelectModel instances.
	 *          The filter will be applied if the instances are of the
	 *          AssociationFilterModel type.
	 * @param filterSettings
	 *          contains the name and the value of the selected
	 *          filters. The name is taken from the JSP name-attribute.
	 */
	@Override
	protected void applyFilters(Map filterModels, Map filterSettings) {
		// create easy list of filter names which are to be applied
		String[] activeFilterNames = new String[filterSettings.size()];
		filterModels.keySet().toArray(activeFilterNames);

		ISelectModel filterModel; // current filter model

		// iterate over result and do the filtering
		for (Iterator rowit = resultRows.iterator(); rowit.hasNext();) {
			// try to apply the filters, go through the list of names
			Object bo = rowit.next();
			if( bo instanceof IDataBag ){
				boolean removeChildren = false;
				if( ((IDataBag)bo).getDelegate().getClass().isAssignableFrom( groupBy )  ){
					for (int j = 0; j < activeFilterNames.length; j++) {
						filterModel = (ISelectModel) filterModels.get(activeFilterNames[j]);
						if (filterModel instanceof IFilterModel ) {
							// can't use other filters in generic form
							String filterValue = (String) filterSettings.get(activeFilterNames[j]);
							if (!((IFilterModel) filterModel).checkFilter(filterValue, bo)) {
								rowit.remove();
								removeChildren = true;
								break;
							}
						}
						removeChildren = false;
					}
				}
				else{
					if( removeChildren ){
						rowit.remove();
					}
				}
			}
		}
	}

	/**
	 * Sort the filtered rows according to the passed property. The behavior is
	 * that if the the order is ascending the sorting will be done descending and
	 * vice versa. The actual order will be determined by comparing the property
	 * value of the first and the last entry in the table.
	 * 
	 * @param sortProperty
	 *          the name of the property by which the table shall be sorted, e.g.
	 *          "name" that assumes that the method getName() is available in the
	 *          "row object". The property must implement Comparable or provide a
	 *          case sensitive toString() result.
     * @param sortAsc
     *          ascending or descending ordering
	 */
	@Override
	protected void applySorting(String sortProperty, boolean sortAsc) {
		if (resultRows.size() < 2)
			return;
		List groupByObjects = new ArrayList();
		for( Iterator iter = resultRows.iterator(); iter.hasNext();){
			IDataBag bag = (IDataBag) iter.next();
			//if( bag.getDelegate().getClass().isAssignableFrom(groupBy) )
			if( groupBy.isAssignableFrom(bag.getDelegate().getClass()) )
				groupByObjects.add( bag );
		}
		// determine how it is sorted currently
		Object firstObject = resultRows.get(0);
		Object firstProp = PropertyAccessor.retrieveProperty(firstObject, sortProperty);
		if (firstProp instanceof Comparable)
			Collections.sort(groupByObjects, createPropertyComparator(sortProperty, sortAsc));
		else Collections.sort(groupByObjects, new ToStringComparator(sortProperty, sortAsc));

		List result = new ArrayList();
		for( Iterator iter = groupByObjects.iterator(); iter.hasNext();){
			IDataBag selected = (IDataBag) iter.next();
			result.add( selected );
			int index = resultRows.indexOf( selected );
			for( int i = index+1; i < resultRows.size(); i ++ ){
				IDataBag bag = (IDataBag) resultRows.get( i );
				//if( bag.getDelegate().getClass().isAssignableFrom(groupBy) )
				if( groupBy.isAssignableFrom(bag.getDelegate().getClass()) )
					break;
				else
					result.add(bag);
			}
		}
		resultRows = result;
	}

	public Class getGroupBy() {
		return groupBy;
	}
	
}
