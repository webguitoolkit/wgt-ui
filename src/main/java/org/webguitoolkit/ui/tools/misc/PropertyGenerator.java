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
package org.webguitoolkit.ui.tools.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * <pre>
 * Generates a property file where the value is the same as the key.
 * 
 * Can be used as a help for translating applications.
 * </pre>
 */
public class PropertyGenerator {

	public static String inFile = "D:\\EclipseWorkspace\\WamSchedulerGui\\src\\main\\resources\\ApplicationResources.properties";
	public static String outFile = "D:\\EclipseWorkspace\\WamSchedulerGui\\src\\main\\resources\\ApplicationResources_zu.properties";

	public static void main(String[] args) {
		try {
			new PropertyGenerator().parse();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parse() throws Exception {
		PrintWriter writer = new PrintWriter(outFile);
		BufferedReader reader = new BufferedReader(new FileReader(new File(inFile)));
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			int index = line.indexOf("=");
			if (index > 0) {
				String key = line.substring(0, index).trim();
				// System.out.println( key );
				writer.write(key + " = " + key + "\r\n");
			}
		}
		reader.close();
		writer.close();
	}
}
