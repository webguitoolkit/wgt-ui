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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

/**
 * @author i102415
 *
 */
public class AntiSamyFilter implements IStringFilter{
	private Policy policy;
	private AntiSamy as;

	public AntiSamyFilter() {
		super();
		try {
			policy = Policy.getInstance( this.getClass().getResourceAsStream("/antisamy-wgt.xml"));
			as = new AntiSamy();
		} catch (PolicyException e) {
			Logger.getLogger( this.getClass() ).error( "Error initializing filter", e );
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.ajax.security.IStringFilter#parseValue(java.lang.String)
	 */
	public String parseValue(String value) {
		if( StringUtils.isEmpty( value ))
			return value;

		try {
			CleanResults cr = as.scan(value, policy);
			if( cr.getNumberOfErrors() > 0 ){
				List errors = cr.getErrorMessages();
				for (Iterator iter = errors.iterator(); iter.hasNext();) {
					String msg = (String) iter.next();
					Logger.getLogger( this.getClass() ).warn( "XSS attack:"+ msg );
				}
				return cr.getCleanHTML();
			}
			return value;
		} catch (PolicyException e) {
			Logger.getLogger( this.getClass() ).error( "Error scanning value", e );
		} catch (ScanException e) {
			Logger.getLogger( this.getClass() ).error( "Error scanning value", e );
		}
		return "";
	}

}
