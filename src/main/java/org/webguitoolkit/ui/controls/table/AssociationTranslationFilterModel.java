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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.AssociationConverter;


/**
 * 
 * <pre>
 * The association filter model knows about the objects reference to another
 * Type. It assumes, that the given list, contains references to the named
 * association. The associated Object must be of type IPersistable and
 * INameable.
 * 
 * The filter displays the all Names found in the list, from the reference.
 * </pre>
 * 
 * @author Arno Schatz
 */
public class AssociationTranslationFilterModel extends AssociationFilterModel implements IFilterModel{
	private String resourcePrefix = "";

	/**
	 * @param referencedProperty
	 *          the name of the attribute in the depending objects leading to the
	 *          filter object, e.g. "master" if the "detail" has a getMaster()
	 *          method.
	 */
	public AssociationTranslationFilterModel(String referencedProperty) {
		super(referencedProperty);
	}
	public AssociationTranslationFilterModel(String referencedProperty, String resourcePrefix ) {
		super(referencedProperty);
		this.resourcePrefix = resourcePrefix;
	}

    /**
     * members of the collection have to IPersistable and INameable
     * @param bos
     */
    public void setOptions(Collection bos) {
    	converter = new AssociationConverter( bos, identProperty );
        List theList = new ArrayList(bos.size());
        options = theList;
        for (Iterator it=bos.iterator(); it.hasNext(); ) {
            Object bo = it.next();
            String id = PropertyAccessor.retrieveString(bo, identProperty);
            String propValue = PropertyAccessor.retrieveString(bo, displayProperty) ;
			String name = TextService.getString( resourcePrefix + propValue + "@" +propValue );
            String[] ddentry = new String[]{ id, name};
            theList.add(ddentry);
        }
    }
}
