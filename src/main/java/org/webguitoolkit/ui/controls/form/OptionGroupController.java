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
package org.webguitoolkit.ui.controls.form;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.conversion.StringConverter;
import org.webguitoolkit.ui.controls.util.validation.IValidator;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * @author Martin
 *
 */
public class OptionGroupController implements ICompoundLifecycleElement {

	private static final String DEFAUL_DELIM = ",";
	
	private Set optionControls = new HashSet();
	private String delim = DEFAUL_DELIM;
	private String property;
	private IConverter converter = new StringConverter();
	private List validators;
	private ICompound surroundingCompound;
	
	public OptionGroupController( String property ) {
		super();
		this.property = property;
	}

	public String getValue() {
		String result = "";
        for (Iterator ocIter = optionControls.iterator(); ocIter.hasNext();) {
			OptionControl control = (OptionControl) ocIter.next();
			if( control.isSelected() ){
				if( !result.equals("") )
					result+=delim;
				result += control.getProperty();
			}
		}
        return result;
	}

	public void loadFrom(Object data) {
        // get the property fro the data objecrt
        String dataForOptions = (data==null)? "" : PropertyAccessor.retrieveString(data, getProperty()
                , getConverter());
        
        // move to contxt
        if (dataForOptions==null) dataForOptions = "";
        
        String[] options = dataForOptions.split( delim );
        Set selected = new HashSet( Arrays.asList(options) );
        
        for (Iterator ocIter = optionControls.iterator(); ocIter.hasNext();) {
			OptionControl control = (OptionControl) ocIter.next();
			control.setSelected( selected.contains( control.getProperty() ) );
		}
	}

	/**
	 * @return the converter
	 */
	private IConverter getConverter() {
		return converter;
	}
	/**
	 * @param converter the converter
	 */
	public void setConverter( IConverter converter ) {
		this.converter = converter;
	}


	/**
	 * @return the property
	 */
	protected String getProperty() {
		return property;
	}


	/**
	 * @param dataObject the object where the value has to be saved to
	 */
	public void saveTo(Object dataObject) {
    	// check if there is a change in the context at all...
        // problems with error reporting...
        try {
        	validate();
        	Object value = getConvertedValue();
			PropertyAccessor.storeProperty(dataObject, getProperty(), value );
		} catch (ValidationException e) {
			((Compound)surroundingCompound()).addError(e.getMessage(),getProperty());
		}
	}

	/**
	 * @throws ValidationException if the validation fails
	 */
	public void validate() throws ValidationException{
		String value = getValue(); 
	    if ( validators != null ) {
	    	for( Iterator iter = validators.iterator(); iter.hasNext(); )
	    		((IValidator)iter.next()).validate( value );
	    }
	}


	/**
	 * This function is useful if there is a converter is set and the control is not used in 
     * @return the converted value
     * @throws ConversionException if there is a error on conversion the string to a object
     */
    public Object getConvertedValue() throws ConversionException{
		return getConverter().parse( getValue() );
    }


	/**
	 * @see org.webguitoolkit.ui.controls.form.IOptionControlGroup#addOptionControl(org.webguitoolkit.ui.controls.form.IOptionControl)
	 * @param control the option control to add
	 */
	public void addOptionControl(IOptionControl control) {
		// set the group for the radio
		if( control instanceof IRadio )
			((IRadio)control).setGroup( getProperty() );
		// take the compound of the first element and register self for life cycle events
		if( optionControls.size() == 0 && control != null ){
			surroundingCompound = control.surroundingCompound();
			surroundingCompound.addElement( this );
		}
		optionControls.add( control );
		((OptionControl)control).setManagedByGroup(true);
	}


	/**
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#changeMode(int)
	 * @param mode the display mode
	 */
	public void changeMode(int mode) {
		// nothing to do
	}


	/**
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#clearError()
	 */
	public void clearError() {
		// nothing to do
		
	}


	/**
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#showError()
	 */
	public void showError() {
		// nothing to do
		
	}


	/**
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#surroundingCompound()
	 * @return the compound
	 */
	public ICompound surroundingCompound() {
		return surroundingCompound;
	}
}
