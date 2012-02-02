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
package org.webguitoolkit.ui.tools;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;


/**
 * <pre>
 * Log4J appender for the WGT Framework
 * </pre>
 */
public class WGTAppender extends AppenderSkeleton {
	
	protected static WGTAppender instance;
	protected LogViewer delegate;
	
	
	public WGTAppender() {
		instance = this;
	}

	public static WGTAppender getInstance() {
		if (instance==null) new WGTAppender();
		return instance;
	}

	public void close() {
		if (delegate!=null)
		delegate.close();
	}

	public void append(LoggingEvent event) {		
		if (delegate!=null)			
		delegate.append(event);
	}

	public boolean requiresLayout() {
		return false;
	}

	public LogViewer getDelegate() {
		return delegate;
	}

	public void setDelegate(LogViewer delegate) {
		this.delegate = delegate;
	}

}
