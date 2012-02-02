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

import junit.framework.TestCase;

/**
 * @author i102415
 *
 */
public class JSONTest extends TestCase{

	public void testJSON() throws Exception{
		
		JSONObject object = new JSONObject();
		object.addAttribute("boolean", true);
		object.addAttribute("stringArray", new String[]{"s1","s2","s3"});
		object.addAttribute("null", new JSONSimpleValue());
		
		JSONObject root = new JSONObject();
		root.addAttribute( "object", object );

		StringBuffer buff = new StringBuffer();
		root.writeTo(buff);
		System.out.println( buff.toString() );
	}
}
