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
package org.webguitoolkit.ui.http;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
/**
 * <pre>
 * RemoteUserFilter, gets the user from the requests RemoteUser and sets 
 * a request attribute with the key: IUserSessionFilter.ATTRIBUTE_KEY_USER
 * 
 *  <filter>
 *    <filter-name>User-Filter</filter-name>
 *    <filter-class>com.endress.infoserve.wgt.http.RemoteUserFilter</filter-class>
 *  </filter>
 *  <filter-mapping>
 *    <filter-name>User-Filter</filter-name>
 *    <url-pattern>/page/*</url-pattern>
 *  </filter-mapping>
 * </pre>
 */
public class RemoteUserFilter implements IUserFilter {

	public void destroy() {
		// nothing to do
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
        
		String user = ((HttpServletRequest)req).getRemoteUser();
		
		if( user != null )
			req.setAttribute(ATTRIBUTE_KEY_USER, user);

		chain.doFilter(req, res);
	}

	public void init(FilterConfig conf) throws ServletException {
		// nothing to do
	}

}
