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

import java.util.ArrayList;
import java.util.List;

/**
 * @author i102415
 *
 */
public class JSONObject implements IJSONValue {
	List<JSONAttribute> attributes = new ArrayList<JSONAttribute>();
	
	public void addAttribute(String key, String value){
		attributes.add( new JSONAttribute(key, new JSONSimpleValue(value)));
	}
	public void addAttribute(String key, int value){
		attributes.add( new JSONAttribute(key, new JSONSimpleValue(value)));
	}
	public void addAttribute(String key, long value){
		attributes.add( new JSONAttribute(key, new JSONSimpleValue(value)));
	}
	public void addAttribute(String key, boolean value){
		attributes.add( new JSONAttribute(key, new JSONSimpleValue(value)));
	}
	public void addAttribute(String key, String[] values){
		attributes.add( new JSONAttribute(key, new JSONArray(values)));
	}
	public void addAttribute(String key, int[] values){
		attributes.add( new JSONAttribute(key, new JSONArray(values)));
	}
	public void addAttribute(String key, long[] values){
		attributes.add( new JSONAttribute(key, new JSONArray(values)));
	}
	public void addAttribute(String key, IJSONValue value ){
		attributes.add( new JSONAttribute(key, value));
	}

	public void writeTo(StringBuffer out) {
		boolean first = true;
		out.append("{ ");
		for( JSONAttribute attr : attributes ){
			if( !first )
				out.append(", ");
			else
				first = false;
			
			attr.writeTo(out);
		}
		out.append(" }");
	}
	
	public String toString(){
		StringBuffer buff = new StringBuffer();
		writeTo(buff);
		return buff.toString();
	}
}
