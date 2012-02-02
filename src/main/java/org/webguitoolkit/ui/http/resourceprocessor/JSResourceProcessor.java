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
package org.webguitoolkit.ui.http.resourceprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * @author i102415
 * 
 */
public class JSResourceProcessor extends AbstractResourceProcessor implements IResourceProcessor {

	public static final String CONTEXT_MENU_JS = "contextmenu.js";
	public static final String FULL_JS = "full.js";
	public static final String BASE_JS = "base.js";
	public static final String TREE_JS = "tree.js";
	public static final String SELECT_JS = "select.js";
	public static final String GLOSSBUTTON_JS = "glossbutton.js";
	public static final String CALENDAR_JS = "datepicker";
	public static final String TABLE_JS = "table.js";
	public static final String MENU_JS = "menu.js";
	public static final String GRID_JS = "grid.js";
	public static final String DRAG_DROP_JS = "dragdrop.js";

	private static final String REQ_PARA_INCLUDE = "include";

	public static final String PREFIX_WGT_CONTROLLER = "wgt.controller.";

	public static final String WGT_BASE = "wgt_base.js";
	public static Collection<String> WGT_BASE_FILES = null;

	public static boolean isStatic = false;
	public static boolean isBundleScripts = true;

	public static Hashtable<String, JSFile> jsFiles = new Hashtable<String, JSFile>();

	public void init(ServletConfig config) {

		String bundleScript = config.getInitParameter("bundle-scripts");
		if (StringUtils.isNotEmpty(bundleScript))
			isBundleScripts = bundleScript.equals("true");

		String staticScript = config.getInitParameter("static-script");
		if (StringUtils.isNotEmpty(staticScript))
			isStatic = staticScript.equals("true");

		if (!isStatic) { // static
			WGT_BASE_FILES = getDependendFiles("wgt.controller.base.js", new ArrayList<String>());
		}
		else {
			List<String> files = getDependendFiles("wgt.controller.glossbutton.js", new ArrayList<String>());
			files.addAll(getDependendFiles("wgt.controller.list.js", files));
			files.addAll(getDependendFiles("wgt.controller.menu.js", files));
			files.addAll(getDependendFiles("wgt.controller.dragdrop.js", files));
			files.addAll(getDependendFiles("wgt.controller.multiselect.js", files));
			files.addAll(getDependendFiles("wgt.controller.select.js", files));
			files.addAll(getDependendFiles("wgt.controller.tabstrip.js", files));
			files.addAll(getDependendFiles("wgt.controller.tree.js", files));
			files.addAll(getDependendFiles("wgt.controller.textsuggest.js", files));
			WGT_BASE_FILES = files;
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor#canHandle(java.lang.String)
	 */
	public boolean canHandle(String fileName) {
		return fileName.endsWith(".js") || fileName.indexOf(".js?" + REQ_PARA_INCLUDE + "=") > 0;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor#send(java.lang.String, java.io.PrintWriter)
	 */
	public void send(String filename, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		filename = getRealName(filename);
		PrintWriter out = resp.getWriter();
		try {
			List<String> filesToLoad = new ArrayList<String>();
			if (filename.endsWith(WGT_BASE)) {
				filesToLoad.addAll(WGT_BASE_FILES);
			}
			else if (filename.startsWith(CALENDAR_JS)) {
				filesToLoad.addAll(getCalendarScripts(filename, out, req));
			}
			else {
				String inc = req.getParameter(REQ_PARA_INCLUDE);
				if( inc!=null ){
					String[] includedFiles = inc.split(",");
					for (String file : includedFiles) {
						if (StringUtils.isBlank(file))
							continue;
						filesToLoad.add(file.trim());
					}
				}
				if( !filesToLoad.contains(filename) )
					filesToLoad.add(filename);
			}

			for (String fileN : filesToLoad) {
				JSFile file = getJSFile(fileN);

				try {
					if (ResourceServlet.isDebug)
						sendToWriter("scripts/" + file.getFilename(), out);
					else
						sendToWriter("scripts/" + file.getCompressedFilename(), out);
				}
				catch (IOException ex) {
				}
			}

		}
		finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

	private List<String> getCalendarScripts(String requestedFile, PrintWriter out, HttpServletRequest req) throws IOException {
		List<String> result = new ArrayList<String>();

		String lang = req.getLocale().getLanguage();
		String country = req.getLocale().getCountry();

		if (requestedFile.lastIndexOf(CALENDAR_JS) != -1 && requestedFile.endsWith(".js")
				&& requestedFile.lastIndexOf(CALENDAR_JS) == (requestedFile.length() - CALENDAR_JS.length() - 6)) {
			lang = requestedFile.substring(requestedFile.length() - 5, requestedFile.length() - 3);
		}
		
		result.add("calendar/calendar.js");
		if (StringUtils.isNotBlank(country) && lang.equals("en")) {
			if (country.equals("US"))
				lang = lang + "_" + country;
		}
		InputStream inputStream = this.getClass().getResourceAsStream("/pool/scripts/calendar/lang/calendar-" + lang + ".js");
		if (inputStream != null){
			result.add("calendar/lang/calendar-" + lang + ".js");
			inputStream.close();
		}
		else
			result.add("calendar/lang/calendar-en.js");

		result.add("calendar/calendar-setup.js");
	
		return result;
	}

	public static List<String> getDependendFiles(String fileName, Collection<String> loaded) {
		List<String> result = new ArrayList<String>();
		for (JSFile dep : getJSFiles(fileName, loaded)) {
			result.add(dep.getFilename());
		}
		return result;
	}

	private static List<JSFile> getJSFiles(String fileName, Collection<String> loaded) {
		fileName = getRealName(fileName);
		List<JSFile> result = new ArrayList<JSFile>();
		JSFile jsFile = getJSFile(fileName);
		List<String> deps = jsFile.getDependencies(loaded);
		for (String dep : deps) {
			result.addAll(getJSFiles(dep, loaded));
		}
		result.add(jsFile);
		return result;
	}

	/**
	 * @param fileName
	 * @return
	 */
	private static JSFile getJSFile(String fileName) {
		JSFile jsFile = jsFiles.get(fileName);
		if (jsFile == null)
			jsFile = new JSFile(fileName);
		return jsFile;
	}

	private static String getRealName(String alias) {
		if (CONTEXT_MENU_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (FULL_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (BASE_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (TREE_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (SELECT_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (GLOSSBUTTON_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (TABLE_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (MENU_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (DRAG_DROP_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		else if (GRID_JS.equals(alias)) {
			return PREFIX_WGT_CONTROLLER + alias;
		}
		return alias;
	}

	public static final String getJQueryEffectLib(String effect) {
		return "jquery/ui/1.8.1/jquery.effects." + effect + ".js";
	}
	
	public static final boolean isBundleScripts() {
		return isBundleScripts;
	}

	public static void main(String[] args) {
		// List<String> files = getDependendFiles("wgt.controller.tree.js", new ArrayList<String>());

		// List<String> files = getDependendFiles("wgt.controller.glossbutton.js", new ArrayList<String>() );
		// files.addAll( getDependendFiles("wgt.controller.list.js", files));
		// files.addAll( getDependendFiles("wgt.controller.menu.js", files));
		// files.addAll( getDependendFiles("wgt.controller.dragdrop.js", files));
		// files.addAll( getDependendFiles("wgt.controller.multiselect.js", files));
		// files.addAll( getDependendFiles("wgt.controller.select.js", files));
		// files.addAll( getDependendFiles("wgt.controller.tabstrip.js", files));
		// files.addAll( getDependendFiles("wgt.controller.tree.js", files));
		// files.addAll( getDependendFiles("wgt.controller.textsuggest.js", files));
		// for( String dep : files )
		// System.out.println( dep );

		List<JSFile> jsFiles2 = getJSFiles("wgt.controller.tree.js", new HashSet<String>());

		for (JSFile file : jsFiles2)
			System.out.println(file.getCompressedFilename());
	}

}
