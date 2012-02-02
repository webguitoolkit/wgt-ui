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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <pre>
 * Base class for user filter. 
 * Provides the static field for the attribute of the session.S
 * </pre>
 */
public interface IUserFilter extends Filter {
	String ATTRIBUTE_KEY_USER = "_user";
	
	/**
	 * User is read from the filter specific source and user name is set to the request parameter IUserFilter.ATTRIBUTE_KEY_USER
	 * 
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     * 
     * @throws ServletException see the interface definition
     * @throws IOException see the interface definition
	 */
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;

}
