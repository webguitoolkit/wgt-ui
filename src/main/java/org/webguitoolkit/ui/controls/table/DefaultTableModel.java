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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.form.ISelectModel;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.util.comparator.PropertyComparator;
import org.webguitoolkit.ui.util.comparator.ToStringComparator;


/**
 * <pre>
 * Description of DefaultTableModel: <br>
 * The default implementation of a table model backed by a list of "table rows"
 * that the caller provides vie <code>setTableData(List)</code> <br>
 * The usage may look like this: <br>
 * <tt>
 * 	List table = new ArrayList();
 * table.add(new TestBean("AAA"));
 * table.add(new TestBean("BBB"));
 * table.add(new TestBean("CCC"));
 * 
 * ITableModel tm = new DefaultTableModel();
 * tm.setTableData(table);
 * tm.applyFilterAndSorting(Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, null, "name");
 * List result = tm.navigate(0,5);
 * </tt>
 * </pre>
 * 
 * @author Arno Schatz, Peter Zaretzke
 * 
 */
public class DefaultTableModel implements ITableModel {
	// data to be displayed
	protected List allRows = new ArrayList(0);
	protected List resultRows = new ArrayList(0);
	protected static final String ISREGULAREXPRESSION = "regex: ";
	public List getFilteredList() {
		return resultRows;
	}

	public List getTableData() {
		return allRows;
	}

	public void setTableData(List allRows) {
		this.allRows = allRows;
		resultRows = allRows;
	}

	/**
	 * This method combines all filtering and sorting that can be done for a
	 * table. The sequence is: 1. apply filters 2.apply wildcards 3.apply sort.
	 * The operration will be performed on all rows in the table. Therefore
	 * multiple calls to this method are not "accumulated".
	 * 
	 * @param filterModels
	 *          a map containing the filter names and the ISelectModel instances.
	 *          The filter will be applied if the instances are of the
	 *          AssociationFilterModel type.
	 * @param filterSettings
	 *          contains the name and the value of the selected
	 *          filters. The name is taken from the JSP name-attribute.
	 * @param propertyRenderer 
	 * 			contains renderer for columns, if available. property = key
	 * @param wildcards
	 *          are the filters for the column (textinputs). The Map contains for
	 *          each column (for which the user wants to search) one entry. Each
	 *          entry has the properties name and the pattern to filter.
	 * @param sortColumn
	 *          is only != null, if the user wants to sort. Then is contains the
	 *          column name as defined in the JSP.
     * @param sortProperty
     *          the name of the property by which the result shall be sorted.
     * @param sortAsc
     *          ascending or descending ordering
	 * @return
	 */
	public int applyFilterAndSorting(Map filterModels, Map filterSettings, 
            Map wildcards,Map propertyRenderer, String sortProperty, boolean sortAsc) {

		resultRows = new ArrayList(allRows);

		if (filterSettings != null && !filterSettings.isEmpty())
			applyFilters(filterModels, filterSettings);

		if (wildcards != null && !wildcards.isEmpty())
			applyWildcards(wildcards, propertyRenderer);

		// now do the sorting to the filterdata
		if (sortProperty != null) {
			applySorting(sortProperty, sortAsc);
		}

		return resultRows.size();
	}

	/**
	 * Perform a pattern matching on the specified columns. Remove all resultRows
	 * that do not match with the specified property.
	 * 
	 * @param wildcards
	 *          is a Map with pairs of "property name" and "pattern", i.e. "name",
	 *          "Ta*". For the Patterns check java.util.regex
	 */
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
			for (Iterator rowit = resultRows.iterator(); rowit.hasNext();) {
				Object row = rowit.next();
				Object value = PropertyAccessor.retrieveProperty(row, property);
				if (value instanceof String) {
					String val = (String) value;
					// filter string columns
					// TODO filter does not work with 
					Matcher m = p.matcher(val);
					if (!m.matches()) {
						rowit.remove();
					}
				}
				else if (value instanceof Long || value instanceof Integer || value instanceof Short) {
					if (!matchNumeric(pattern, value, p)) {
						rowit.remove();
					}
				}
				else if (value instanceof Double || value instanceof Float) {
					if (!matchNumericFloating(pattern, value, p)) {
						rowit.remove();
					}
				}
				else if (value instanceof BigDecimal) {
					if (!matchNumericFloating(pattern, value, p)) {
						rowit.remove();
					}
				}
//removed timestamp/date if clause because behaviour is already used in the else clause of this method				
//				else if (value instanceof Timestamp || value instanceof Date) {
//					if (columsMap.get(property) != null) {
//						TableColumn col = (TableColumn) columsMap.get(property);
//						IColumnRenderer renderer = col.getRenderer();
//						value = renderer.renderAsPlainText(row, col);
//						Matcher m = p.matcher(String.valueOf(value));
//						if (!m.matches()) {
//							rowit.remove();
//						}
//					}
					// try to get converter
//					Object sDate = value;
//					if (columsMap.get(property) != null) {
//						TableColumn col = (TableColumn) columsMap.get(property);
//						IConverter converter = col.getConverter();
//						if(converter != null){
//							sDate = converter.convert(String.class, value);
//						}else{
//							sDate = (String) ConvertUtil.DATE_TIME_CONVERTER.convert(String.class, value);
//						}
//					}else{
//						sDate = (String) ConvertUtil.DATE_TIME_CONVERTER.convert(String.class, value);
//					}
//
//					Matcher m = p.matcher(String.valueOf(sDate));
//					if (!m.matches()) {
//						rowit.remove();
//					}
//				}
				else if (value == null) {
					rowit.remove();
				}
				else {
					// try to get renderer
					if (columsMap.get(property) != null) {
						TableColumn col = (TableColumn) columsMap.get(property);
						IColumnRenderer renderer = col.getRenderer();
						value = renderer.renderAsPlainText(row, col);
						Matcher m = p.matcher(String.valueOf(value));
						if (!m.matches()) {
							rowit.remove();
						}
					}
					else {
						// try default string representation
						// works well for Long, Int ...
						Matcher m = p.matcher(String.valueOf(value));
						if (!m.matches()) {
							rowit.remove();
						}
					}
				}
			}
		}
	}

	/**
	 * pattern for insensitive search
	 */
	protected Pattern compilePattern(String pattern) {
		
		if(!pattern.startsWith(ISREGULAREXPRESSION)){
			pattern = escapeRegexLiterals(pattern);
			pattern = pattern.replaceAll("\\*", ".*");
			pattern = pattern.replaceAll("\\?", ".");
			if (!pattern.endsWith("*"))
				pattern += ".*";
			
		}else{
			pattern = regularExpressionFilter(pattern);
		}
		return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL );
	}
	
	
	protected String regularExpressionFilter(String regExPattern){
		//it is assumed that the pattern is a valid regular expression therefore only the regularexpression identifier is removed and
		//the pattern  is masked for java regexp use by masking the backslash
		String pattern = "";
		pattern = regExPattern.replace(ISREGULAREXPRESSION, "");
		//necessary for java masking of regexp pattern
		//pattern = StringUtils.replace(pattern, "\\", "\\\\");
		
		return pattern;
	}
	
	protected String escapeRegexLiterals(String pattern) {
		pattern = StringUtils.replace(pattern, "\\", "\\\\");
		pattern = StringUtils.replace(pattern, "+", "\\+");
		pattern = StringUtils.replace(pattern, "[", "\\[");
		pattern = StringUtils.replace(pattern, "]", "\\]");
		pattern = StringUtils.replace(pattern, "{", "\\{");
		pattern = StringUtils.replace(pattern, "}", "\\}");
		pattern = StringUtils.replace(pattern, ")", "\\)");
		pattern = StringUtils.replace(pattern, "(", "\\(");		
		pattern = StringUtils.replace(pattern, ".", "\\.");
		pattern = StringUtils.replace(pattern, "$", "\\$");
		pattern = StringUtils.replace(pattern, "^", "\\^");
		return pattern;
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
	protected void applyFilters(Map filterModels, Map filterSettings) {
		// create easy list of filter names which are to be applied
		String[] activeFilterNames = new String[filterSettings.size()];
		filterModels.keySet().toArray(activeFilterNames);

		ISelectModel filterModel; // current filter model

		// iterate over result and do the filtering
		for (Iterator rowit = resultRows.iterator(); rowit.hasNext();) {
			// try to apply the filters, go through the list of names
			Object bo = rowit.next();
			for (int j = 0; j < activeFilterNames.length; j++) {
				filterModel = (ISelectModel) filterModels.get(activeFilterNames[j]);
				if (filterModel instanceof IFilterModel ) {
					// can't use other filters in generic form
					String filterValue = (String) filterSettings.get(activeFilterNames[j]);
					if (!((IFilterModel) filterModel).checkFilter(filterValue, bo)) {
						rowit.remove();
						break;
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
	protected void applySorting(String sortProperty, boolean sortAsc) {
		
		if (resultRows.size() < 2)
			return;
		// determine how it is sorted currently
		Object firstObject = resultRows.get(0);
		Object firstProp = PropertyAccessor.retrieveProperty(firstObject, sortProperty);
		if (firstProp instanceof Comparable)
			Collections.sort(resultRows, createPropertyComparator(sortProperty, sortAsc));
		else Collections.sort(resultRows, new ToStringComparator(sortProperty, sortAsc));
	}

	/**
	 * Factory method for customization in subclasses
	 * 
	 * @param sortProperty
	 * @param sortAsc
	 * @return
	 */
	protected PropertyComparator createPropertyComparator(String sortProperty, boolean sortAsc) {
		return new PropertyComparator(sortProperty, sortAsc);
	}										  

	/**
	 * Returns a "view" of the portion of this table between the specified
	 * first row containing "rows" rows. The returned list is backed by the table,
	 * so non-structural changes in the returned list are reflected in the table,
	 * and vice-versa. The returned list supports all of the optional list
	 * operations supported by this list.
	 * 
	 * @param startrow
	 *          the first row
	 * @param rows
	 *          the number of rows
	 * @return
	 */
	public List navigate(int startrow, int rows) {
		int toIndex = Math.min(resultRows.size(), startrow + rows);
		return resultRows.subList(Math.min(startrow, toIndex), toIndex);
	}

	protected boolean matchNumeric(String pattern, Object value, Pattern patternForStrings) {
		// filter Long/Int. columns
		// try to make a number from the the pattern
		long longValue = 0;

		if (value instanceof Long) {
			longValue = ((Long) value).longValue();
		}
		else if (value instanceof Integer) {
			longValue = ((Integer)value).longValue();
		}
		else if (value instanceof Short) {
			longValue = ((Short) value).longValue();
		}
		try {
			long lPattern = Long.parseLong( pattern );
			return (lPattern == longValue);
		}
		catch (NumberFormatException nfe) {
			try {
				// try to find default operator
				if (pattern.split(">=").length == 2) {
					long lPattern = Long.parseLong(pattern.split(">=")[1].trim());
					return (longValue >= lPattern);
				}
				else if (pattern.split("<=").length == 2) {
					long lPattern = Long.parseLong(pattern.split("<=")[1].trim());
					return (longValue <= lPattern);
				}
				else if (pattern.split("<>").length == 2) {
					long lPattern = Long.parseLong(pattern.split("<>")[1].trim());
					return (longValue != lPattern);
				}
				else if (pattern.split("!").length == 2) {
					long lPattern = Long.parseLong(pattern.split("!")[1].trim());
					return (longValue != lPattern);
				}
				else if (pattern.split(">").length == 2) {
					long lPattern = Long.parseLong(pattern.split(">")[1].trim());
					return (longValue > lPattern);
				}
				else if (pattern.split("<").length == 2) {
					long lPattern = Long.parseLong(pattern.split("<")[1].trim());
					return (longValue < lPattern);
				}
				else if (pattern.split("=").length == 2) {
					long lPattern = Long.parseLong(pattern.split("=")[1].trim());
					return (longValue == lPattern);
				}
				else if (pattern.split("-").length == 2) {
					long lPattern = Long.parseLong(pattern.split("-")[0].trim());
					long rPattern = Long.parseLong(pattern.split("-")[1].trim());
					return (longValue >= lPattern && longValue <= rPattern);
				}
				else if (pattern.split("%").length == 2) {
					// find divisible for given value
					long lPattern = Long.parseLong(pattern.split("%")[1].trim());
					return ((longValue % lPattern) == 0);
				}
				else if (pattern.trim().equals("~")) {
					// find odd
					return ((longValue % 2) != 0);
				}
				else if (pattern.trim().equals("_")) {
					// find even
					return ((longValue % 2) == 0);
				}
			}
			catch (NumberFormatException nfe2) {
				// Use the String representation
				Matcher m = patternForStrings.matcher(String.valueOf(value));
				return (m.matches());
			}

			// Use the String representation
			Matcher m = patternForStrings.matcher(String.valueOf(value));
			return (m.matches());
		}
	}

	protected boolean matchNumericFloating(String pattern, Object value, Pattern patternForStrings) {
		// filter Long/Int. columns
		// try to make a number from the the pattern
		double doubleValue = 0;

		if (value instanceof Double) {
			doubleValue = ((Double) value).doubleValue();
		}
		else if (value instanceof Float) {
			doubleValue = ((Float) value).doubleValue();
		}
		else if (value instanceof BigDecimal) {
			doubleValue = ((BigDecimal) value).doubleValue();
		}		
		try {
			double dPattern = Double.parseDouble(pattern);
			return (dPattern == doubleValue);
		}
		catch (NumberFormatException nfe) {
			try {
				// try to find default operator
				if (pattern.split(">=").length == 2) {
					double rPattern = Double.parseDouble(pattern.split(">=")[1].trim());
					return (doubleValue >= rPattern);
				}
				else if (pattern.split("<=").length == 2) {
					double rPattern = Double.parseDouble(pattern.split("<=")[1].trim());
					return (doubleValue <= rPattern);
				}
				else if (pattern.split("<>").length == 2) {
					double rPattern = Double.parseDouble(pattern.split("<>")[1].trim());
					return (doubleValue != rPattern);
				}				
				else if (pattern.split("!").length == 2) {
					double rPattern = Double.parseDouble(pattern.split("!")[1].trim());
					return (doubleValue != rPattern);
				}								
				else if (pattern.split(">").length == 2) {
					double rPattern = Double.parseDouble(pattern.split(">")[1].trim());
					return (doubleValue > rPattern);
				}
				else if (pattern.split("<").length == 2) {
					double rPattern = Double.parseDouble(pattern.split("<")[1].trim());
					return (doubleValue < rPattern);
				}
				else if (pattern.split("=").length == 2) {
					double rPattern = Double.parseDouble(pattern.split("=")[1].trim());
					return (doubleValue == rPattern);
				}
				else if (pattern.split("-").length == 2) {
					double lPattern = Double.parseDouble(pattern.split("-")[0].trim());
					double rPattern = Double.parseDouble(pattern.split("-")[1].trim());
					return (doubleValue >= lPattern && doubleValue <= rPattern);
				}
			}
			catch (NumberFormatException nfe2) {
				// Use the String representation
				Matcher m = patternForStrings.matcher(String.valueOf(value));
				return (m.matches());
			}

			// Use the String representation
			Matcher m = patternForStrings.matcher(String.valueOf(value));
			return (m.matches());
		}
	}
}
