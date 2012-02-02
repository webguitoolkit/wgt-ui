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
package org.webguitoolkit.ui.controls.util.json;

/**
 * @author i102415
 *
 */
public class JSONAttribute {
	private String key;
	private IJSONValue value;
	
	public JSONAttribute( String key, IJSONValue value ){
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(IJSONValue value) {
		this.value = value;
	}
	/**
	 * @return the value
	 */
	public IJSONValue getValue() {
		return value;
	}
	
	void writeTo( StringBuffer out ){
		out.append( "\"" + key + "\": " );
		value.writeTo(out);
	}
	public String toString(){
		StringBuffer buff = new StringBuffer();
		writeTo(buff);
		return buff.toString();
	}

}
