package org.webguitoolkit.ui.controls.form;


import java.util.ArrayList;
import java.util.List;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.util.comparator.EnumerationConverter;

/**
 * This class populates a Selectbox. it assumes the collection you pass in with
 * selectionList is an enumeration, using the parameters valueOf, and name.
 * 
 * @author Martin Hermann
 *
 */
public class EnumerationSelectModel extends DefaultSelectModel {

	protected String resouceBundlePrefix = "";
	protected Class enumClass = null;

    public EnumerationSelectModel( Class enumClass) {
    	super();
    	setList( enumClass );
    }

    public EnumerationSelectModel( Class enumClass, String resouceBundlePrefix ) {
    	super();
    	this.resouceBundlePrefix = resouceBundlePrefix;
    	setList( enumClass );
    }

    /**
     * members of the enumeration are used to load select box 
     * @param bos
     */
    public void setList( Class enumeration ) {
    	if( !enumeration.isEnum() )
    		throw new WGTException("Class is no enumeration");
        enumClass = enumeration;
        Object[] values = enumeration.getEnumConstants();
        List<String[]> theList = new ArrayList<String[]>( values.length );
        super.setOptions( theList );
        for ( Object value : values ) {
            String[] ddentry = new String[]{ value.toString(), TextService.getString( resouceBundlePrefix+ value.toString() ) };
            theList.add( ddentry );
        }
    }

	
	/**
	 * return the key for for the Object given.
	 * use to call Select.selectKey()
	 */
	public String key4Object(Object o) {
		return o.toString();
	}
	
	@Override
	public IConverter getConverter() {
		return new EnumerationConverter( enumClass );
	}
}
