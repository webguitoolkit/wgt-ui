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
package org.webguitoolkit.ui.ajax;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * A ContextElement is a single element in the context.
 * 
 * It consists of:
 * 
 * <b>cssId</b> - the ID of the control to be effected. Some context elements are using
 * a prefix to indicate a special operation on the control.(e.g. Ta133.vis -> visibility of table with id Ta133)
 * this mechanism is used because entries with the same id overwriting each other.
 * 
 * <b>value</b> - the content e.g. value, command, ...
 * 
 * <b>type</b> - kind of the element, used by the client to do some actions
 * 
 * <b>status</b> - information for the transport layer, not all values are send to the client
 * </pre>
 */
public class ContextElement implements Serializable {
	String cssId;
	String value;
	String type;
	String status;

	/**
	 * DWR calls this
	 * 
	 */
	public ContextElement() {
	}

	public ContextElement(String cssId, String value, String type, String status) {
		this.cssId = cssId;
		this.value = value;
		this.type = type;
		this.status = status;
	}

	public String getCssId() {
		return cssId;
	}

	public void setCssId(String cssId) {
		this.cssId = cssId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "[" + cssId + "," + value + "," + type + "," + status + "]";
	}

	/**
	 * search for doublets needs this to be exact
	 */
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ContextElement) {
			ContextElement other = (ContextElement)obj;
			return StringUtils.equals(cssId, other.cssId) && StringUtils.equals(value, other.value) && StringUtils.equals(type, other.type)
					&& StringUtils.equals(status, other.status);
		}
		return false;
	}

}
