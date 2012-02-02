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
package org.webguitoolkit.ui.controls.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.util.comparator.SelectOptionsComparator;
/**
 * <pre>
 * this implements the callbacks for ListModel, but it saves the data, so 
 * the content must be passed into this model before it is being used.
 * </pre>
 * 
 * @author arno
 */
public class DefaultSelectModel implements ISelectModel {
    // the list of String[2] for populating the control
	protected Collection options;
    public DefaultSelectModel() {
        super();
    }
    
    /**
     * constructor with single value list
     */
    public DefaultSelectModel(List svl) {
        super();
        setSingleValueList(svl);
    }

    /**
     * constructor with single value array
     */
    public DefaultSelectModel(String[] svl) {
        super();
        setSingleValueList(svl);
    }

    /**
     * returns the list of String[2] that are poputated to the control
     */
    public Collection getOptions() {
        return options;
    }
    /**
     * list must consist of String[2].
     * First String is the key (non-visible) , second is the text visible to the user.
     * @param list
     */
    public void setOptions(Collection list) {
        this.options = list;
    }
    
    /**
     * creates the List for the parameter. It expects a List of String
     * and assumes that key and text are the same.
     */
    public void setSingleValueList(List svl) {
    	setSingleValueList(svl, false);
    }
    
    /**
     * creates the List for the parameter. It expects a List of String
     * and assumes that key and text are the same.
     * with parameter valueToUpperCase set to true, the value
     * will be the element.toUpperCase()
     */
    public void setSingleValueList(List svl, boolean valueToUpperCase) {
    	if (svl==null) {
    		options = null;
    		return;
    	}
    	options = new ArrayList(svl.size());
    	for (Iterator it = svl.iterator(); it.hasNext();) {
			String elem = (String) it.next();
			if (valueToUpperCase)
			{
				options.add(new String[] {elem,elem.toUpperCase()});
			}
			else
			{
				options.add(new String[] {elem,elem});
			}
		}
    }
    
    /**
     * creates the List for the parameter. It assumes that key and text are the same.
     */
    public void setSingleValueList(String[] svl) {
    	setSingleValueList(svl, false);
    }
    /**
     * creates the List for the parameter. It assumes that key and text are the same.
     * with parameter valueToUpperCase set to true, the value
     * will be the element.toUpperCase()
     */
    public void setSingleValueList(String[] svl, boolean valueToUpperCase) {
    	if (svl==null) {
    		svl = new String[]{};
    	}
    	setSingleValueList(Arrays.asList(svl), valueToUpperCase);
    }
    
    /**
     * @param selectedKey
     * @return name of the Display Value corresponding to the key
     */
    
    public String getDisplayName(String selectedKey) {
    	String result = null;
    	if (options!=null && selectedKey!=null) {
    		for (Iterator it = options.iterator(); it.hasNext();) {
    			String[] test = (String[])it.next();
    			if (selectedKey.equals(test[0])) {
    				return test[1];
    			}
    		}
    	}
    	return result;
    }
    /**
     * sort the options by value, i.e. second String-Value of options-Array.
     * Works only when called before loadList().
     * Works for all Selects with DefaultSelectModel.
     */
    public void sort()
    {
    	if(options!=null)
        	Collections.sort((List) options, new SelectOptionsComparator());
    }

	public IConverter getConverter() {
		return null;
	}
}
