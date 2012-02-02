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
package org.webguitoolkit.ui.controls.event;

import java.util.HashMap;
import java.util.Map;

import org.webguitoolkit.ui.controls.IBaseControl;


/**
 * ServerEvents are fired by controls on the server.<br>
 * 
 * Known usages are:
 * pre/post dispatch for event execution
 * selection of rows in the table
 * loading of a compound
 */
public class ServerEvent implements IEvent{
	// here are all event types listed:
	public static final int EVENT_PREDISPATCH = 0;
	public static final int EVENT_POSTDISPATCH = 1;

	// events for table
	public static final int EVENT_TABLE_SELVISBLE = 3;
	public static final int EVENT_TABLE_SELHIDDEN = 4;
	
	
	protected Map parameter;
	protected IBaseControl source;
	protected int type;

	public ServerEvent() {
		super();
	}

	public ServerEvent(int type) {
		super();
		this.type = type;
	}

	public ServerEvent(IBaseControl source, int type) {
		this.source = source;
		this.type = type;
	}


	public IBaseControl getSource() {
		return source;
	}

	public void setControl(IBaseControl source) {
		this.source = source;
	}

	public int getTypeAsInt() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map getEventParameter() {
		if (parameter == null) {
			parameter = new HashMap(2);
		}
		return parameter;
	}

	public void putParameter(Object key, Object value) {
		getEventParameter().put(key, value);
	}

	public Object getParameter(Object key) {
		return getEventParameter().get(key);
	}

	public String getType() {
		return Integer.toString( type );
	}
}
