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
import javax.servlet.http.HttpSession;

import org.directwebremoting.util.Logger;

/**
 * @author i102415
 *
 */
public class SessionDebugFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		long timeBefore = System.currentTimeMillis();
		boolean isNew = req.getSession(false) == null || req.getSession().isNew();
		chain.doFilter(request, response);
		long timeAfter = System.currentTimeMillis();
		StringBuffer info = new StringBuffer();
		info.append( "-----------------------------------------------" );
		info.append( "Request: " );
		info.append( req.getRequestURI() );
		info.append( "\n" );
		info.append( "Time in ms: " );
		info.append( ( timeAfter - timeBefore ) );
		info.append( "\n" );
		info.append( "New Session: " );
		info.append( isNew );
		info.append( "\n" );
		HttpSession session = req.getSession(false);
		if( session != null ){
			info.append( "SessionId: "  );
			info.append( session.getId() );
			info.append( "\n" );
			info.append( "SessionCreation: " );
			info.append( session.getCreationTime() );
			info.append( "\n" );
		}
		else{
			info.append( "No Session\n" );
		}
		info.append( "Content size: " );
		info.append( response.getBufferSize()  );
		info.append( "\n" );
		info.append( "Content Type: " );
		info.append( response.getContentType() );
		info.append( "\n" );
		info.append( "Character Encoding: " );		
		info.append( response.getCharacterEncoding() );		
		info.append( "\n" );
		info.append( "-----------------------------------------------" );
		Logger.getLogger( this.getClass() ).debug( info.toString() );
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}
