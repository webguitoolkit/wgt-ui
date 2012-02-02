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

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <pre>
 * This class is for solving the problem of deploying. You may now have all properties in one file and the 
 * settings which are used depend on the localhostname on which the application is running.
 * loads a config file. When asked for a protery it will search first for <pre-fix>.<prop-name>
 * which enables you to have configurations for multiple hosts (or other discriminators) in 
 * one file. So When you're deployed on a different system, It will draw different 
 * settings from the property file. If the prefix of the hostname is not found, then we use just <prop-name> as key,
 *  which means that is the default and should always be present.
 *  
 *  tipical call:
 *  <code>loadConfig(this.getClass(), "/RepConfip.properties", localHostname());</code>
 * </pre>
 */
public class WGTProperties extends Properties {
	protected static WGTProperties instance;
	protected String preFix;

	
	/**
	 * loads a config file. When asked for a protery it will search first for <pre-fix>.<prop-name>
	 * which enables you to have configurations for multiple hosts (or other discriminators) in 
	 * one file. So When you're deployed on a different system, It will draw different 
	 * settings from the property file. If the prefix of the hostname is not found, then we use just <prop-name> as key,
	 *  which means that is the default and should always be present.
	 *  
	 *  tipical call:
	 *  <code>loadConfig(this.getClass(), "/RepConfip.properties", localHostname());</code>
	 *  
	 * @param loadFrom you must give a class, so we use the same classloader as this class
	 * this determines from which jar file we can read.
	 * @param confName
	 * @param preFix use localHostname()
	 */
	public WGTProperties(Class loadFrom, String confName, String thePreFix) {
		InputStream stream = null;
		try {
			stream = loadFrom.getResourceAsStream(confName);
			load(stream);
			this.preFix = thePreFix;
		} catch (IOException e) {
			this.preFix = null;
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
		finally{
			if( stream != null ){
				try {
					stream.close();
				}
				catch (IOException e) {
					Logger.getLogger(getClass()).error(e.getMessage(), e);
				}
			}
		}
	}
	/**
 * get a property from the config file whioch was loaded earlier with loadConfig.
 * @param property
 * @return
 */
	public String getProperty( String property) {
		String value = null;
		if (preFix!=null) {
			value = super.getProperty(preFix + "." +  property);			
		}
		if (value==null) {
			value = super.getProperty(property );
		}
		return value;
	}
	
	public static String localHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static WGTProperties getInstance() {
		return instance;
	}

	public static void setInstance(WGTProperties instance) {
		WGTProperties.instance = instance;
	}

}
