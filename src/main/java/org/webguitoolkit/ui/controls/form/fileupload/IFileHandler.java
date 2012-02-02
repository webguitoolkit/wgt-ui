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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

/**
 * <p>
 * handles the file upload, e.g. saves the FileItems to the file system.
 * </p>
 */
public interface IFileHandler extends Serializable {

	/**
	 * 
	 * @param fileItems the fileItems to store
	 * @param parameters the other parameters
	 * @param request the request object
	 * @throws FileUploadException exception when initialization fails
	 */
	void init(List<FileItem> fileItems, Map<String, String> parameters, HttpServletRequest request) throws FileUploadException;
	
	/**
	 * @return a list of parameters for sending to the action listener of the fileUpload, e.g. Filename, fileId, ...
	 */
	List<String> getEventParameters();

	/**
	 * overwrite this method to store the file (FileItem) where ever you want
	 * 
	 * @throws Exception exception on upload
	 */
	void processUpload() throws Exception;
}
