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
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.http.resourceprocessor.CSSResourceProcessor;
import org.webguitoolkit.ui.http.resourceprocessor.GIFResourceProcessor;
import org.webguitoolkit.ui.http.resourceprocessor.HTMLResourceProcessor;
import org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor;
import org.webguitoolkit.ui.http.resourceprocessor.JSResourceProcessor;
import org.webguitoolkit.ui.http.resourceprocessor.PNGResourceProcessor;

/**
 * <pre>
 * The ResourceServlet streams the projects JavaScript and CSS files to the client.
 * There are several aliases that choin single js files.
 * 
 * full.js all relevant js files
 * base.js minimal set of js files
 * 
 * contextmenu.js for context menu
 * tree.js for the tree
 * select.js for select boxes
 * menu.js for menus
 * dragdrop.js for drag and drop
 * 
 * &lt;code&gt;
 * 
 *  place following code in your web.xml:
 *  
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;resource_servlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;com.endress.infoserve.wgt.http.ResourceServlet&lt;/servlet-class&gt;
 *      &lt;init-param&gt;
 *  		&lt;description&gt;the url pattern for addressing js and css files (wgt-resources is default)&lt;/description&gt;
 *  		&lt;param-name&gt;debug&lt;/param-name&gt;
 *  		&lt;param-value&gt;false&lt;/param-value&gt;
 *  	&lt;/init-param&gt;
 * 	&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 * 	&lt;servlet-name&gt;resource_servlet&lt;/servlet-name&gt;
 * 	&lt;url-pattern&gt;/wgt-resources/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * 
 * &lt;/code&gt;
 * 
 * Following parameter are available:
 * 
 *  * debug 
 *  	if true the scripts will be delivered uncompressed. 
 *  	DEFAULT: false
 *  * static-script 
 *  	if true a lot of scripts are directly placed in the pages header. 
 *  	DEFAULT: false
 *  * bundle-scripts 
 *  	if true scripts are combined to one script file instead of doing on request every file. 
 *  	DEFAULT: true
 *  * activate-cache 
 *  	sets the cache header for script files and evaluates requests headers for cache settings. 
 *  	DEFAULT: true
 *  * url-pattern
 *  	sets the path of the resources (the mapping has to be adjusted too)
 *  	DEFAULT: wgt-resources
 * </pre>
 */
public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String REQ_PARA_INCLUDE = "include";
	public static final String POSTFIX_COMPRESSED = ".min";
	private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	private static final int MAXAGE = 60 * 60 * 24 * 30;

	public static String SERVLET_URL_PATTERN = "wgt-resources";

	public static boolean isDebug = false;

	public static boolean isCacheActivated = true;
	
	public long lastModifiedDate = -1;

	public List<IResourceProcessor> resourceProcessors;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initLastModified();
		String urlPattern = config.getInitParameter("url-pattern");
		if (StringUtils.isNotEmpty(urlPattern))
			SERVLET_URL_PATTERN = urlPattern;
		String debug = config.getInitParameter("debug");
		if (StringUtils.isNotEmpty(debug))
			isDebug = debug.equals("true");
		String cache = config.getInitParameter("activate-cache");
		if (StringUtils.isNotEmpty(cache))
			isCacheActivated = cache.equals("true");

		// initialize worker classes
		resourceProcessors = new ArrayList<IResourceProcessor>();
		// JS
		JSResourceProcessor jsProcessor = new JSResourceProcessor();
		jsProcessor.init(config);
		resourceProcessors.add(jsProcessor);
		// GIF
		GIFResourceProcessor gifProcessor = new GIFResourceProcessor();
		gifProcessor.init(config);
		resourceProcessors.add(gifProcessor);
		// PNG
		PNGResourceProcessor pngProcessor = new PNGResourceProcessor();
		pngProcessor.init(config);
		resourceProcessors.add(pngProcessor);
		// CSS
		CSSResourceProcessor cssProcessor = new CSSResourceProcessor();
		cssProcessor.init(config);
		resourceProcessors.add(cssProcessor);
		// HTML
		HTMLResourceProcessor htmlProcessor = new HTMLResourceProcessor();
		htmlProcessor.init(config);
		resourceProcessors.add(htmlProcessor);

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String requestedUri = req.getRequestURI();

		// get requested file name
		String requestedFile = requestedUri.substring(requestedUri.indexOf("/" + SERVLET_URL_PATTERN + "/")
				+ ("/" + SERVLET_URL_PATTERN + "/").length());

		Logger.getLogger(this.getClass()).debug("Request for: " + requestedFile);

		// set cachable info
		if( isCacheActivated ){
			if (requestedFile.endsWith(".js") || requestedFile.indexOf(".js?" + REQ_PARA_INCLUDE + "=") > 0) {
				if (isUpToDate(req)) {
					resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					return;
				}
				resp.addDateHeader("Last-Modified", lastModifiedDate);
				resp.setHeader("Cache-Control", "max-age=" + MAXAGE);
			}
		}

		for (IResourceProcessor rp : resourceProcessors) {
			if (rp.canHandle(requestedFile)) {
				rp.send(requestedFile, req, resp);
				return;
			}
		}
		Logger.getLogger(this.getClass()).error(
				"Filedownload not allowed for file extension: " + requestedFile.substring(requestedFile.lastIndexOf(".")) + ", uri: "
						+ requestedUri);

	}

	private boolean isUpToDate(HttpServletRequest req) {
		long modifiedSince = -1;
		try {
			// HACK: Websphere appears to get confused sometimes
			modifiedSince = req.getDateHeader(IF_MODIFIED_SINCE);
		}
		catch (RuntimeException ex) {
			// TODO: Check for "length" and re-parse
			// Normally clients send If-Modified-Since in rfc-compliant form
			// ("If-Modified-Since: Tue, 13 Mar 2007 13:11:09 GMT") some proxies
			// or browsers add length to this header so it comes like
			// ("If-Modified-Since: Tue, 13 Mar 2007 13:11:09 GMT; length=35946")
			// Servlet spec says container can throw IllegalArgumentException
			// if header value can not be parsed as http-date.
			// We might want to check for "; length=" and then do our own parsing
			// See: http://getahead.org/bugs/browse/DWR-20
			// And: http://www-1.ibm.com/support/docview.wss?uid=swg1PK20062
		}

		if (modifiedSince != -1) {
			// Browsers are only accurate to the second
			modifiedSince -= modifiedSince % 1000;
		}
		// There is no ETag, just go with If-Modified-Since
		if (modifiedSince >= lastModifiedDate) {
			return true;
		}
		return false;
	}

	@Override
	protected long getLastModified(HttpServletRequest req) {
		return lastModifiedDate;
	}


	public void initLastModified(){
		try {
			String classContainer = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
			URL manifestUrl = new URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(manifestUrl.openStream());
			Attributes attributes = manifest.getMainAttributes();
			if (attributes != null && !attributes.isEmpty()) {
				String buildString = attributes.getValue("Implementation-Build");
				String time = buildString.substring(buildString.indexOf("/") + 1);
				String dateString = time.substring(0, 10);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date = df.parse(dateString);
				lastModifiedDate = date.getTime();
			}
			else
				lastModifiedDate = System.currentTimeMillis();
		}
		catch (Exception ex) {
			Logger.getLogger(this.getClass()).warn("Could not read manifest file");
			lastModifiedDate = System.currentTimeMillis();
		}
	}


}
