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

import org.apache.log4j.Logger;
/**
 * Filter for finding cross side scripting (XSS)<br>
 * 
 * @author Martin Hermann
 */
public class XSSFilter implements IStringFilter{

	public static final String REG_XSS_EXPR = ".*<\\/?([sS][cC][rR][iI][pP][tT]|[jJ][aA][vV][aA][sS][cC][rR][iI][pP][tT]).*";
	
	/**
	 * processing the filter
	 * 
	 * @param value the string value
	 * @return the filtered value
	 */
	public String parseValue(String value) {
		if( value == null  ) return null;
		if( value.matches(REG_XSS_EXPR) ){
			Logger.getLogger(this.getClass()).warn( "XSS violation: " + value );
			return "";
		}
		return value;
	}
}
