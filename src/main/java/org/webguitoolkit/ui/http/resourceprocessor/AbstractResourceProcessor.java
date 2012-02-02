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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

/**
 * @author i102415
 *
 */
public abstract class AbstractResourceProcessor implements IResourceProcessor{


	protected boolean sendToWriter(String requestedFile, PrintWriter out) throws IOException {
		Logger.getLogger(this.getClass()).debug("Sending File: " + requestedFile);
		InputStreamReader in = null;
		InputStream inputStream = null;
		try {
			inputStream = this.getClass().getResourceAsStream("/pool/" + requestedFile);
			if (inputStream == null) {
				Logger.getLogger(this.getClass()).error("requested File does not exist: " + requestedFile);
				return false;
			}
			in = new InputStreamReader(inputStream);
			char[] buffer = new char[4096];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1)
				out.write(buffer, 0, bytesRead); // write
		}
		finally {
			if (in != null) {
				try {
					inputStream.close();
					in.close();
				}
				catch (IOException e) {
					;
				}
			}
		}
		return true;
	}

	/**
	 * @param requestedFile
	 * @param outputStream
	 */
	protected void sendToStream(String requestedFile, ServletOutputStream out) throws IOException {
		Logger.getLogger(this.getClass()).debug("Sending File: " + requestedFile);
		InputStream in = null;
		try {
			in = this.getClass().getResourceAsStream("/pool/" + requestedFile);
			if (in == null) {
				Logger.getLogger(this.getClass()).error("requested File does not exist: " + requestedFile);
				return;
			}
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1)
				out.write(buffer, 0, bytesRead); // write
		}
		finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
				if (in != null) {
					in.close();
				}
			}
			catch (IOException e) {
				;
			}
		}
	}

}
