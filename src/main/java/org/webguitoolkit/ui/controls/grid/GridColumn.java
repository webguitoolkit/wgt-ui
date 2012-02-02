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
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.validation.IValidator;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * @author i102415
 * 
 */
public class GridColumn {


	private String property;
	private String width;
	private String title;
	private boolean editable;
	private boolean sortable;
	private IConverter converter;
	private List<IValidator> validators = new ArrayList<IValidator>();
	
	public GridColumn(String property, String title, String width) {
		this(property, title, width, false, false);
	}

	public GridColumn( String property, String title, String width, boolean editable ) {
		this(property, title, width, editable, false);
	}

	public GridColumn( String property, String title, String width, boolean editable, boolean sortable ) {
		super();
		this.property = property;
		this.title = title;
		this.width = width;
		this.editable = editable;
		this.sortable = sortable;
	}
	
	public String getProperty() {
		return property;
	}

	public String getWidth() {
		return width;
	}

	public String getTitle() {
		return title;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleKey(String titleKey) {
		this.title = TextService.getString(titleKey);
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	protected String loadFrom( IDataBag bag ){
        // get the property from the data object
        String dataForText = (bag==null)? "" : PropertyAccessor.retrieveString(bag, getProperty(), converter);
        if (dataForText==null) dataForText = "";
		return dataForText;
	}
	
	protected void saveTo( String value, IDataBag bag ) throws ValidationException{
    	validate(value);
    	Object valueAsObject;
    	if( converter != null )
    		valueAsObject = converter.parse( value );
    	else
    		valueAsObject = value;
		PropertyAccessor.storeProperty(bag, getProperty(), valueAsObject );
	}
	public void validate(String value) throws ValidationException{
	    if ( validators != null ) {
	    	for( Iterator iter = validators.iterator(); iter.hasNext(); )
	    		((IValidator)iter.next()).validate( value );
	    }
	}

	/**
	 * @return
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

}
