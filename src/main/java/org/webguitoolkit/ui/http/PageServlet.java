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
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.DWRController;
import org.webguitoolkit.ui.controls.Page;


/**
 * <p>
 * This is the servlet you can use instea of JSPs. Using this servlet you will
 * need to create subclasses of the Page class to actually implement your
 * webpage. To actualy call your Page, the URL will dependent on your web.xml as
 * well. Considering you have the below entry in your web.xml and your Page
 * class FQN is com.endress.infoserve.portalconf.po.main.entrypages.MainPage,
 * then the URL is http://<yourserver>:<port>:/<rotContext>/page/entrypages/MainPage
 * </p>
 * <p>
 * It is possible to mix servlet base page and JSPs in one Application (an they
 * can call each other using normal http-request parameters.
 * </p>
 * 
 * <pre>
 * <!--  PageServlet -->
 *  <servlet>
 *  	<servlet-name>PageServlet</servlet-name>
 *  	<servlet-class>com.endress.infoserve.base.PageServlet</servlet-class>
 *  	<init-param>
 *  		<description>import or prefixes of the Page objects to be loaded</description>
 *  		<param-name>import</param-name>
 *  		<param-value>	
 *  			com.endress.infoserve.portalconf.po.main;
 *  			com.endress.gui.test.sample
 *  		</param-value>
 *  	</init-param>
 *  </servlet>
 *  
 *  <servlet-mapping>
 *  	<servlet-name>PageServlet</servlet-name>
 *  	<url-pattern>/page/*</url-pattern>
 *  </servlet-mapping>
 * 
 * </pre>
 * 
 * @author Arno
 * 
 */
public class PageServlet extends HttpServlet {

	public static final String IMPORT = "import";
	protected String[] impArray;

	// page-mapping variables (i102455/20080220)
	protected Hashtable pageMapppingMap;
	public static final String PAGE_MAPPING = "page-mapping@";

	public PageServlet() {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		work(request, response);
	}

	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		String imports = sc.getInitParameter(IMPORT);
		// parse string imports statements are seperated by ;
		if (StringUtils.isNotEmpty(imports)) {
			impArray = StringUtils.split(imports, ';');
			// need to remove all the whitespace before and after
			if (impArray != null) {
				for (int i = 0; i < impArray.length; i++) {
					impArray[i] = StringUtils.trimToEmpty(impArray[i]);
				}
			}
		} else {
			impArray = null;
		}
		this.initPageMappings(sc);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		work(request, response);
	}

	protected void work(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// fixes for form login - START
		// 15.01.2009: WGTFilter will not be called before web.xml form login page is called.
		// Without WGTFilter we get a NullPointerException in BaseControl constructor.
		
		// set contenttype to utf-8
		response.setContentType("text/html; charset=utf-8");

        // lets see if there is a session. if not create one...
        DWRController aCompTree = (DWRController) request.getSession(true).getAttribute(DWRController.class.getName());
        if (aCompTree==null) {
        	//the dwrcontrool may have created a new tree by now.
        	aCompTree = new DWRController();
        	request.getSession().setAttribute(DWRController.class.getName(), 
        			aCompTree);        	        	       	
        }
        DWRController.setInstance(aCompTree); 
        // fixes for form login - END
		
		// Tried to fix problems with client cookies 13.11.2008 
		Page.setServletRequest( request );
		Page.setServletResponse( response );
		
//		response.setContentType("text/html");
		response.setBufferSize(8192);
		PrintWriter out = response.getWriter();
		// how do we determin which Page to instanciate and call?
		// create an import mechanism here.
		String pageName = request.getPathInfo();
		if (pageName == null || pageName.length() <= 1) {
			pageNotFound(request, response);
			return;
		}
		if (pageName.startsWith("/")) {
			pageName = pageName.substring(1);
		}

		// check for page-mappings (i102455/20080220)
		if (pageMapppingMap != null && pageMapppingMap.get(pageName) != null) {
			pageName = (String) pageMapppingMap.get(pageName);
		}

		// prefix with imports....
		Page p = checkPage(pageName);
		for (int i = 0; impArray != null && i < impArray.length && p == null; i++) {
			p = checkPage(impArray[i] + pageName);
			if (p != null)
				break;
			p = checkPage(impArray[i] + '.' + pageName);
		}
		if (p == null) {
			pageNotFound(request, response);
			return;
		}
		// in our current programming model, no more than one thread is allowed
		// to access the tree.
		// with dynamic programming this would be difficult anyway.
		// it seems to be strange to sync this instance, as it is newly created
		// in method checkPage,
		// however, there will be more requests to it from ajax calls and it
		// must be sync there too.
		synchronized (p) {
			p.writePage(out);
		}
		out.close();
	}

	/**
	 * check if a Page exist with the name pageName. If so load the class and
	 * instanciate it.
	 * 
	 * @param pageName the name of the page
	 * @return the Page object if the className in pageName was found (and was
	 *         of type Page)
	 * @throws ServletException thrown if page not exists
	 */
	public Page checkPage(String pageName) throws ServletException {
		pageName = pageName.replace('/', '.');
		Page p;
		try {
			p = (Page) Class.forName(pageName).newInstance();
		} catch (InstantiationException e) {
			throw new ServletException(e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e);
		} catch (ClassNotFoundException e) {
			// will happen if more than one import in servlet config is used.
			p = null;
		} catch (ClassCastException e) {
			throw new ServletException(e);
		}
		return p;
	}

	public void pageNotFound(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
	/**
	 * // init page-mappings (i102455/20080220)
	 * requires mapping in web-xml
	 * 
	 * Example: <init-param> <description> page mapping to prevent long page
	 * names </description> <param-name>page-mapping@NameOfPageClass</param-name>
	 * <param-value> abbriviation;index;test;index.htm;whatever
	 * </param-value> </init-param>
	 * 
	 * Description: param-name: page-mapping@ followed by the PageClassName.
	 * Packages must be defined by the import init-param of this servlet
	 * param-value: add short names for the PageClassName
	 */
	private void initPageMappings(ServletConfig sc) throws ServletException {

		// get all initParamsNames
		Enumeration initParamNames = sc.getInitParameterNames();
		if (initParamNames != null) {
			// loop through initParamsNames and check for start page-mapping@
			while (initParamNames.hasMoreElements()) {
				String initParamName = (String) initParamNames.nextElement();
				if (initParamName != null
						&& initParamName.startsWith(PAGE_MAPPING)
						&& initParamName.indexOf("@") < initParamName.length()) {
					// get parameter
					String mapping = sc.getInitParameter(initParamName);
					if (StringUtils.isNotEmpty(mapping)) {
						String[] mapArray = StringUtils.split(mapping, ';');
						if (mapArray != null) {
							for (int i = 0; i < mapArray.length; i++) {
								if (pageMapppingMap == null)
									pageMapppingMap = new Hashtable();
								// add mapping -> page entry to table
								try {
									if(pageMapppingMap.get(StringUtils
											.trimToEmpty(mapArray[i]))==null){
										pageMapppingMap.put(StringUtils
												.trimToEmpty(mapArray[i]), StringUtils
												.substringAfter(initParamName, "@"));
									} else {
										throw new Exception("Duplicate page mapping for '"
																	+ StringUtils.trimToEmpty(mapArray[i]) 
																	+ "', check web.xml");
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	
}
