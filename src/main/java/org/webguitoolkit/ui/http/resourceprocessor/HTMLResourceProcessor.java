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
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author i102415
 * 
 */
public class HTMLResourceProcessor extends AbstractResourceProcessor {

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor#canHandle(java.lang.String)
	 */
	public boolean canHandle(String fileName) {
		// TODO Auto-generated method stub
		return fileName.endsWith(".html");
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor#send(java.lang.String, java.io.PrintWriter)
	 */
	public void send(String filename, PrintWriter out) {
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.http.resourceprocessor.IResourceProcessor#send(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void send(String filename, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		try {
			resp.setHeader("Content-Type", "text/html");
			sendToWriter("html/" + filename, out);
		}
		finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

}
