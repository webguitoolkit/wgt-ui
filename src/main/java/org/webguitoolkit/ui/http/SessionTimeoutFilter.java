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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * SessionTimeoutFilter, for handling timeouts on AJAX requests via guiding to
 * a redirect site witch reloads the page
 * 
 *  <filter>
 *    <filter-name>Timeout-Filter</filter-name>
 *    <filter-class>com.endress.infoserve.wgt.http.SessionTimeoutFilter</filter-class>
 *    <init-param>
 *    	<param-name>DWR_MODE</param-name>
 *      <param-value>iframe/XML</param-value>
 *    </init-param>
 *  </filter>
 *  <filter-mapping>
 *    <filter-name>Timeout-Filter</filter-name>
 *    <url-pattern>/page/*</url-pattern>
 *  </filter-mapping>
 *  <filter-mapping>
 *    <filter-name>Timeout-Filter</filter-name>
 *    <url-pattern>/dwr/*</url-pattern>
 *  </filter-mapping>
 *  <filter-mapping>
 *    <filter-name>Timeout-Filter</filter-name>
 *    <url-pattern>*.event</url-pattern>
 *  </filter-mapping>
 * </pre>
 */
public class SessionTimeoutFilter implements Filter {

	private String mode = null;
	/**
	 * method from interface
	 */
	public void destroy() {
	}

	/**
	 * check for timeout and redirect to timeout page or to login
	 * 
     * @param req the servlet request
     * @param res the servlet response
     * @param chain the filter chain
     * 
     * @throws ServletException see the interface definition
     * @throws IOException see the interface definition
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
			ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		if ( request.getSession(false) == null ) { 
        	// timeout or new session
            String requestedUri = request.getRequestURI();
            if( requestedUri.endsWith("DWRCaller.event.dwr") ) {
            	// timeout
            	String path = request.getContextPath();
            	String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path+"/";
            	if( mode.equals("XML" ) )
            		((HttpServletResponse)res).sendRedirect( basePath + ResourceServlet.SERVLET_URL_PATTERN+ "/timeout_xml.html" );
            	else
            		((HttpServletResponse)res).sendRedirect( basePath + ResourceServlet.SERVLET_URL_PATTERN + "/timeout_iframe.html" );
            	return;
            }
        }
		chain.doFilter(req, res);
		
	}

	/**
	 * initialization method, read parameter form configuration
	 * 
	 * @param config the filter configuration
	 * 
	 * @throws ServletException from interface
	 */
	public void init( FilterConfig config ) throws ServletException {
		mode = config.getInitParameter("DWR_MODE");
	}

}
