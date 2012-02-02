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
package org.webguitoolkit.ui.ajax.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.ajax.ContextElement;

/**
 * Filter for the context to block dangerous context values.
 * It contains a list of Filters that can lock for different pattern in the context
 * <p/>
 * 
 * @author Martin
 */
public class ContextFilter {
	private List contextFilters = new ArrayList();
	
	/**
	 * With this method filter can be added so that there can be more aspects to check.
	 * 
	 * @param filter a filter to add
	 */
	public void addFilter( IStringFilter filter ){
		contextFilters.add( filter );
	}

	/**
	 * 
	 * @param contextElements the context elements to be filtered
	 */
	public void filterContexts( ContextElement[] contextElements ){
    	if(contextElements != null ){
	    	for( int i = 0; i < contextElements.length; i++ ){
	    		filterContext(contextElements[i]);
	    	}
    	}
	}
	/**
	 * 
	 * @param contextElement the context element to be filtered
	 */
	public void filterContext( ContextElement contextElement ){
		if( contextElement!=null )
			contextElement.setValue( parseValue( contextElement.getValue() ) );
	}
	
	/**
	 * processing the filters
	 * 
	 * @param value the string value
	 * @return the filtered value
	 */
	private String parseValue( String value ){
		for( Iterator iter = contextFilters.iterator(); iter.hasNext(); ){
			value = ((IStringFilter)iter.next()).parseValue( value );
		}
		return value;
	}
}
