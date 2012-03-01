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

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.table.TableColumn.ALIGN;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * Interface for the table column object that can be added to the table. A column get the data to be displayed via a property name
 * from the table model. A column can be visible or hidden. The column has an optional title. The column can be rendered into
 * special appearance by means of a column renderer. The ability to sort and filter (wild card) can be switched on an off per
 * column.
 * 
 * @author Peter
 * 
 */
public interface ITableColumn extends IBaseControl {
	/**
	 * 
	 * @param align where to align
	 */
	void setAlign(ALIGN align);

	/**
	 * 
	 * @param filter true = show filter
	 */
	void setFilter(boolean filter);

	/**
	 * 
	 * @param mandatory true = column is always displayed
	 */
	void setMandatory(boolean mandatory);

	/**
	 * 
	 * @param sortable true can sort
	 */
	void setSortable(boolean sortable);

	/**
	 * 
	 * @param source property name in bean syntax
	 */
	void setProperty(String source);

	/**
	 * 
	 * @param title title text
	 */
	void setTitle(String title);

	/**
	 * 
	 * @param type ?
	 */
	void setType(String type);

	/**
	 * 
	 * @param width column width in px
	 */
	void setWidth(String width);

	/**
	 * 
	 * @param renderer renderer to use
	 */
	void setRenderer(IColumnRenderer renderer);

	/**
	 * 
	 * @param converter converter to use
	 */
	void setConverter(IConverter converter);

	/**
	 * 
	 * @param isDisplayed true -- display column
	 */
	void setIsDisplayed(boolean isDisplayed);

	/**
	 * 
	 * @param value true = show Select All Check box In Header For Check boxes
	 */
	void setShowSelectAllCheckboxInHeaderForCheckboxes(boolean value);

	void setShowTitleInHeaderForCheckboxes(boolean value);

	/**
	 * 
	 * @param data t.b.d.
	 * @return the name
	 */
	String propertyFrom(Object data);

	/**
	 * @return the columns property
	 */
	String getProperty();

	/**
	 * @return true if visible
	 */
	boolean getIsDisplayed();

	/**
	 * @param data t.b.d.
	 * @return the property name
	 */
	String getMappedProperty(Object data);

	/**
	 * @return the converter or null.
	 */
	IConverter getConverter();

	IColumnRenderer getRenderer();

	/**
	 * width of column as css-string
	 * 
	 * @return
	 */
	String getWidth();

	/**
	 * type of column , as passed in by
	 * 
	 * @return
	 */
	String getType();

	/**
	 * titel of column, translated.
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * @return the exporatble
	 */
	boolean isExporatble();

	/**
	 * @param exporatble the exporatble to set
	 */
	void setExporatble(boolean exporatble);
}
