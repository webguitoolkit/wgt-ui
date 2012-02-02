package org.webguitoolkit.ui.controls.table;

import java.util.Collection;

import org.webguitoolkit.ui.controls.form.ISelectModel;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;

public class SimpleFilterModel implements IFilterModel, ISelectModel {

	
	private Collection options;
	private IConverter converter;
	private String referencedProperty;
	
	public SimpleFilterModel(IConverter converter, Collection options,
			String referencedProperty) {
		super();
		this.converter = converter;
		this.options = options;
		this.referencedProperty = referencedProperty;
	}
	
	public boolean checkFilter(String checkKey, Object bo) {
		 // filterObject
		Object fObj = PropertyAccessor.retrieveProperty(bo, referencedProperty);
		
		if(fObj==null) {
			if(bo==null) {
				return true;
			} else {
				return false;
			}
		}
		
		if( converter == null ){
			return fObj.equals( checkKey );
		}
		else{
			try {
				return fObj.equals( getConverter().parse( checkKey ) );
			} catch (ConversionException e) {
				return false;
			}
		}
	}

	public IConverter getConverter() {
		return converter;
	}

	public Collection getOptions() {
		return options;
	}

}
