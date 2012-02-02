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
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.webguitoolkit.ui.ajax.DWRController;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <b> Generic Filter. there are several steps to initialise the application before each request can be worked on. </b> place
 * insert following in your web.xml:
 * 
 * <pre>
 *   <filter>
 *     <filter-name>WGTFilter</filter-name>
 *     <filter-class>com.endress.infoserve.wgt.http.WGTFilter</filter-class>
 *      <init-param>
 *          <param-name>textservice.resource.bundle</param-name>
 *          <param-value>SampleResources</param-value>
 *      </init-param>
 *   </filter>
 *   
 *   <filter-mapping>
 *     <filter-name>WGTFilter</filter-name>
 *     <url-pattern>/jsp/*</url-pattern>
 *   </filter-mapping>
 *   <filter-mapping>
 *     <filter-name>WGTFilter</filter-name>
 *     <url-pattern>/dwr/*</url-pattern>
 *   </filter-mapping>
 *   <filter-mapping>
 *     <filter-name>WGTFilter</filter-name>
 *     <url-pattern>/Chart/*</url-pattern>
 *   </filter-mapping>
 * 
 * </pre>
 * 
 * @author Arno
 */
public class WGTFilter implements Filter, HttpSessionListener {
	// subclasses may use the filterconfig later
	protected FilterConfig filterConfig;
	protected String resourceBundleName = null;

	/**
	 * initialization of the filter done once when application is started.
	 * 
	 * @param filterConfig parameter provided by the servlet container
	 * 
	 * @throws ServletException see the interface definition
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		// initialize logging
		URL logging = this.getClass().getResource("/logging.xml");
		if (null != logging)
			DOMConfigurator.configure(logging);

		resourceBundleName = filterConfig.getInitParameter("textservice.resource.bundle");
		if (resourceBundleName == null)
			resourceBundleName = TextService.DEFAULT_RESOURCE_BUNDLE_NAME;
	}

	/**
	 * each requests passes through here. This method just structures the flow. The individual method (xxxFilter) should be
	 * overwritten if necessary).
	 * 
	 * @param request the servlet request
	 * @param response the servlet response
	 * @param chain the filter chain
	 * 
	 * @throws ServletException see the interface definition
	 * @throws IOException see the interface definition
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;

		preFilter(req, res);
		try {
			HttpSession session = req.getSession();
			// jsp create a session right away.
			// so if this request doesn't have a session it is a ajax called
			// where the session has timeout (or destroyed otherwise)
			if (session == null) {
				res.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Session expired.");
				return;
			}
			if (!allSessions.containsKey(session.getId())) {
				allSessions.put(session.getId(), new WeakReference(session));
			}
			chain.doFilter(req, res);
			postFilter(req, res);
		}
		catch (Exception e) {
			catchFilter(req, res, e);
			throw new ServletException(e);
		}
		catch (Error e) {
			catchFilter(req, res, e);
			throw e;
		}
		finally {
			finallyFilter(req, res);
		}
	}

	/**
	 * executed in any case if exception or not. does some standard cleanup, setting free of memory.
	 * 
	 * @param req servlet request
	 * @param res servlet response
	 */
	protected void finallyFilter(HttpServletRequest req, HttpServletResponse res) {
		// clean up tree reference
		DWRController.setInstance(null);
		Page.setServletRequest(null);
		Page.setServletResponse(null);
	}

	/**
	 * handles exceptions
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @param e exception to handle
	 */
	protected void catchFilter(HttpServletRequest request, HttpServletResponse response, Throwable e) {
		// at least log the stuff.
		Logger.getLogger(this.getClass()).log(Level.ERROR, e.getMessage(), e);
	}

	/**
	 * done at the end of the filter
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 */
	protected void postFilter(HttpServletRequest request, HttpServletResponse response) {
		DWRController aCompTree = (DWRController)request.getSession(true).getAttribute(DWRController.class.getName());
		if (aCompTree != null) {
			// the dwrcontrool may have created a new tree by now.
			request.getSession().setAttribute(DWRController.class.getName(), aCompTree);
		}
	}

	/**
	 * done at beginning of the filter
	 * 
	 * @param req servlet request
	 * @param res servlet response
	 */
	protected void preFilter(HttpServletRequest req, HttpServletResponse res) {

		TextService.setLocale(req.getLocale());
		TextService.setResourceBundleName(resourceBundleName);
		Page.setServletRequest(req);
		Page.setServletResponse(res);
		// lets see if there is a session. if not create one...
		DWRController aCompTree = (DWRController)req.getSession(true).getAttribute(DWRController.class.getName());
		if (aCompTree == null) {
			// the dwrcontrool may have created a new tree by now.
			aCompTree = new DWRController();
			req.getSession().setAttribute(DWRController.class.getName(), aCompTree);
		}
		DWRController.setInstance(aCompTree);
	}

	/**
	 * finally method of the filter
	 */
	public void destroy() {
	}

	/**
	 * this returns all sessions which this filters knows off (went through here) Couldn't find a appripriate method in the API.
	 * Somethime these sun-api seem to be very akward.
	 * 
	 * @return list of all sessions
	 */
	public static List getAllSessions() {
		List as = new ArrayList(allSessions.size());
		for (Iterator it = allSessions.entrySet().iterator(); it.hasNext();) {
			Map.Entry se = (Map.Entry)it.next();
			WeakReference wr = (WeakReference)se.getValue();
			if (wr.get() == null) {
				// already deleted.remove weakref object.
				it.remove();
			}
			else {
				as.add(wr.get());
			}
		}
		return as;
	}

	// all sessions indexed by their ID.

	protected static Map allSessions = Collections.synchronizedMap(new HashMap());

	/**
	 * called on session create
	 * 
	 * @param event the create event
	 */
	public void sessionCreated(HttpSessionEvent event) {
		// new session created
	}

	/**
	 * can be added as session filter to be sure that all components are removed
	 * 
	 * @param sessionEvent the destroy event
	 */
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		DWRController compTree = (DWRController)sessionEvent.getSession().getAttribute(DWRController.class.getName());
		if (compTree != null) {
			List components = new ArrayList();
			for (Iterator iter = compTree.getComponents().values().iterator(); iter.hasNext();) {
				components.add(iter.next());
			}
			for (Iterator iter = components.iterator(); iter.hasNext();) {
				((BaseControl)components).remove();
			}
		}
	}
}
