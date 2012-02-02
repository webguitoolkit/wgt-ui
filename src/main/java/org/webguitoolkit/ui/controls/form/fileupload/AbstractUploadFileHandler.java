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
package org.webguitoolkit.ui.controls.form.fileupload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

/**
 * <pre>
 * public class SimpleUploadFileHandler extends AbstractUploadFileHandler {
 * 
 * 	public void processUpload() throws Exception {
 * 		List fileItems = getFileItems();
 * 		if (fileItems != null) {
 * 			//TODO: Store file on disk 
 * 			//e.g.:
 * 			//File file = new File( &quot;D:\\&quot; + getFileName() );
 * 			//fileItem.write( file );
 * 		}
 * 	}
 * 
 * 	public List getEventParameters() {
 * 		return getFileNames();
 * 	}
 * }
 * </pre>
 * 
 */
public abstract class AbstractUploadFileHandler implements IFileHandler {

	private List<FileItem> fileItems = new ArrayList<FileItem>();
	transient private HttpServletRequest request;
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * for interal use only, called by the upload.jsp
	 * 
	 * @param request
	 * @throws FileUploadException
	 */
	public final void init(List<FileItem> fileItems, Map<String, String> parameters, HttpServletRequest request) throws FileUploadException {
		this.fileItems = fileItems;
		this.parameters = parameters;
		this.request = request;
	}

	/**
	 * @return the HttpSession
	 */
	protected HttpSession getSession() {
		return request.getSession();
	}

	/**
	 * @return the HttpRequest
	 */
	protected HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the simple filename (without slashes)
	 */
	public List<String> getFileNames() {
		List<String> fileNames = new ArrayList<String>();
		if (fileItems == null || fileItems.size() == 0) {
			return fileNames;
		}

		for (FileItem currentFileItem : fileItems) {
			String name = currentFileItem.getName();
			if (name != null) {
				fileNames.add(name.substring(name.lastIndexOf("\\") + 1));
			}			
		}
		return fileNames;
	}

	/**
	 * @return a list of parameters for sending to the action listener of the fileUpload, e.g. Filename, fileId, ...
	 */
	public abstract List getEventParameters();

	/**
	 * @return the FileItem that contains the uploaded file
	 */
	public List<FileItem> getFileItems() {
		return fileItems;
	}

	/**
	 * @return the simple request parameters
	 */
	public String getParameter(String key) {
		if (key != null) {
			return parameters.get(key);
		}
		return null;
	}

	/**
	 * overwrite this mehod to store the file (FileItem) whereever you want
	 * 
	 * @throws Exception
	 */
	public abstract void processUpload() throws Exception;

}
