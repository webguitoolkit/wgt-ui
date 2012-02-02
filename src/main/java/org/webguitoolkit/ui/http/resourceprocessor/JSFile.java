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
package org.webguitoolkit.ui.http.resourceprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author i102415 Builds a dependency tree for jsFiles for optimized js loading
 */
public class JSFile {
	private List<String> children = null;
	private String filename;

	private String compressedName;

	public JSFile(String filename) {
		this.filename = filename;
	}

	public List<String> getDependencies(Collection<String> loaded) {
		List<String> result = new ArrayList<String>();
		if (children == null) {
			loadChildren();
		}
		for (String file : children) {
			if (!loaded.contains(file)) {
				result.add(file);
			}
		}
		loaded.addAll(result);
		return result;
	}

	public void loadChildren(){
		children = new ArrayList<String>();
		InputStream resourceAsStream = this.getClass().getResourceAsStream("/pool/scripts/" + getFilename() );
		if (resourceAsStream == null) {
			Logger.getLogger(this.getClass()).warn("File not found: " + getFilename());
			return;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream));
		try {
			String line = in.readLine();
			// collect dependencies from first line in controller
			if (line.startsWith("// Depends:")) {
				String includes = line.substring("// Depends:".length());
				String[] incs = includes.split(",");
				for( String child : incs )
					children.add( child.trim() );
			}
		}
		catch( IOException ex ){
			Logger.getLogger(this.getClass()).error("IO Exception on loading dependencies for " + getFilename(), ex );
		}
		finally {
			if (in != null) {
				try {
					resourceAsStream.close();
					in.close();
				}
				catch (IOException e) {
					;
				}
			}
		}
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @return the filename
	 */
	public String getCompressedFilename() {

		if (compressedName == null) {
			if (filename.endsWith(".min.js")) {
				compressedName = filename;
			}
			else {
				InputStream in = null;
				try {
					compressedName = filename.replaceAll("\\.js", ".min.js");
					in = this.getClass().getResourceAsStream("/pool/scripts/" + compressedName);
					if (in == null) {
						return compressedName = filename;
					}
				}
				finally {
					if (in != null) {
						try {
							in.close();
						}
						catch (IOException e) {
							;
						}
					}
				}
			}
		}
		return compressedName;
	}
}
