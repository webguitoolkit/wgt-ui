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

import org.webguitoolkit.ui.controls.form.IFormControl;

/**
 * Interface for the FileUpload. The FileUpload displays a FileUpload field an a submit button for uploading files by
 * means of a upload Servlet in a hidden iframe. <br>
 * CSS class : wgtInputFile, wgtIconButton, wgtUploadProgressBarBox, wgtUploadProgressText, wgtUploadProgressBarBoxContent,
 * gtInputText, wgtReadonly
 * 
 * @author Peter
 * 
 */
public interface IFileUpload extends IFormControl {

	/**
	 * 
	 * @param fileHandler
	 *            handler for the file upload processing, extend the AbstractUploadFileHandler
	 */
	public void setFileHandler(IFileHandler fileHandler);

	/**
	 * path to the image for the submit button
	 */
	public void setSubmitButtonSrc(String submitButtonSrc);

	/**
	 * 
	 * @param label
	 */
	public void setSubmitLabel(String label);

	/**
	 * 
	 * @param labelKey
	 */
	public void setSubmitLabelKey(String labelKey);

	/**
	 * 
	 * @param submitStyleClass
	 */
	public void setSubmitStyleClass(String submitStyleClass);

}