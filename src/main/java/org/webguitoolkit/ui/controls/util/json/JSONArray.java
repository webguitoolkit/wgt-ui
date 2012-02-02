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
import java.util.Arrays;
import java.util.List;

/**
 * @author i102415
 *
 */
public class JSONArray implements IJSONValue{
	
	private List<IJSONValue> values = new ArrayList<IJSONValue>();
	
	public JSONArray(){
	}
	public JSONArray( List<IJSONValue> values ){
		this.values = values;
	}
	public JSONArray( IJSONValue[] values ){
		this.values = Arrays.asList(values);
	}
	public JSONArray( String[] values ){
		for( String value : values )
			addValue(new JSONSimpleValue(value));
	}
	public JSONArray( int[] values ){
		for( int value : values )
			addValue(new JSONSimpleValue(value));
	}
	public JSONArray( long[] values ){
		for( long value : values )
			addValue(new JSONSimpleValue(value));
	}
	
	public void addValue(IJSONValue value){
		values.add(value);
	}
	public void setValues(List<IJSONValue> values ){
		this.values = values;
	}
	public void setValues(IJSONValue[] values ){
		this.values = Arrays.asList(values);
	}

	public void writeTo(StringBuffer out) {
		out.append("[ ");
		boolean first = true;
		for( IJSONValue value : values ){
			if( first )
				first = false;
			else
				out.append(", ");
			
			value.writeTo(out);
		}
		out.append(" ]");
	}
	public String toString(){
		StringBuffer buff = new StringBuffer();
		writeTo(buff);
		return buff.toString();
	}

}
