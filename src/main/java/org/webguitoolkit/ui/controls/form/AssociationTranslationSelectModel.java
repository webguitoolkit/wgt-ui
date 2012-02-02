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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.AssociationConverter;

/**
 * <pre>
 * This class populates a select box. It assumes the collection you pass in with
 * selectionList is an association, using the parameters id, and name.
 * The name parameter is translated the key is preFix + name.
 * 
 * e.g. name = update
 * 		prefix = permission.
 * 
 * -> resource bundle key = permission.update
 * </pre>
 * @author Martin Hermann
 *
 */
public class AssociationTranslationSelectModel  extends AssociationSelectModel {

	String prefix = "";

    public AssociationTranslationSelectModel() {
        super();
    }
    public AssociationTranslationSelectModel( String preFix ) {
        super();
        this.prefix = preFix;
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
			String name = TextService.getString( prefix + propValue + "@" +propValue );
            String[] ddentry = new String[]{ id, name};
            theList.add(ddentry);
        }
    }
}
