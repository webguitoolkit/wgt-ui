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
public class JSONSimpleValue implements IJSONValue {

	private String stringValue = null;
	private Long longValue = null;
	private Integer intValue = null;
	private boolean booleanValue;
	private final TYPE type;

	enum TYPE {
		STRING, INTEGER, LONG, BOOLEAN, NULL
	}

	public JSONSimpleValue(String stringValue) {
		this.stringValue = stringValue;
		type = TYPE.STRING;
	}

	public JSONSimpleValue(Long longValue) {
		this.longValue = longValue;
		type = TYPE.LONG;
	}

	public JSONSimpleValue(Integer intValue) {
		this.intValue = intValue;
		type = TYPE.INTEGER;
	}

	public JSONSimpleValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
		type = TYPE.BOOLEAN;
	}

	public JSONSimpleValue() {
		type = TYPE.NULL;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.util.json.IJSONValue#writeTo(java.lang.StringBuffer)
	 */
	public void writeTo(StringBuffer out) {
		if( type == TYPE.STRING )
			out.append("\""+ stringValue + "\"" );
		else if( type == TYPE.INTEGER )
			out.append( intValue );
		else if( type == TYPE.LONG )
			out.append( longValue );
		else if( type == TYPE.BOOLEAN )
			out.append( booleanValue );
		else if( type == TYPE.NULL)
			out.append( "null" );
			
	}
	public String toString(){
		StringBuffer buff = new StringBuffer();
		writeTo(buff);
		return buff.toString();
	}

	
}
