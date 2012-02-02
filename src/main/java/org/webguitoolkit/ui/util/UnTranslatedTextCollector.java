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
package org.webguitoolkit.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * <pre>
 * Class for collection all resource keys that are not translated in the resource bundle file. 
 * The untranslated resource keys are stored in the WEB-INF/classes/ApplicationResourcesToTranslate.properties
 * after sutting down the web application.
 * 
 * Add to the web.xml:
 * <listener>
 * 	<listener-class>
 * 		org.webguitoolkit.ui.util.UnTranslatedTextCollector
 * 	</listener-class>
 * </listener>
 * </pre>
 * 
 * @author Martin Hermann
 *
 */
public class UnTranslatedTextCollector implements ServletContextListener{

	private static Properties toTranslate = null;
	
	/**
	 * called when the web application is shutting down
	 */
	public void contextDestroyed( ServletContextEvent scEvent ) {
		
		String path = scEvent.getServletContext().getRealPath("/");
		String propertyPath = path+ File.separator + "WEB-INF" + File.separator + "classes";
		File propertyFile = new File(propertyPath, "ApplicationResourcesToTranslate.properties");
		try {
			toTranslate.store( new FileOutputStream(propertyFile),"to translate" );
		} catch ( Exception e ) {
			Logger.getLogger(this.getClass()).error("Error storing missing resource keys: " + toTranslate.toString(), e );
		}
	}

	public void contextInitialized( ServletContextEvent scEvent ) {
		toTranslate = new Properties();
	}
	
	/**
	 * add a missing resource key and the default value to the property file
	 */
	public static void addKey( String key, String defaultValue ){
		if( toTranslate!= null )
			toTranslate.put(key, defaultValue);
	}

}
