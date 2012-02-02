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

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.http.ResourceServlet;


/**
 * <pre>
 * The FileUpload displays a FileUpload field an a submit button for uploading 
 * files to the upload servlet in a hidden iframe
 * </pre>
 * @author Martin Hermann
 */
public class FileUpload extends FormControl implements IFileUpload {

	public static final String DOT_BUTTON = ".button";
    public static final String DIV_UPLOADED = "_uploaded";
    public static final String DIV_UPLOAD_FIELD = "_uploadField";
    public static final String DIV_PROGRESS_BAR = "_progressBar";

    public static final String FIELD_FILE_INPUT = "_fileInput";
    
    public static final int EVENT_UPLOAD_INFO = 99;
    public static final int EVENT_UPLOAD_ERROR = 90;
    public static final int EVENT_FILE_TO_LARGE = 91;
    public static final int EVENT_NO_FILE = 92;

    private IFileHandler fileHandler = null;
    private String submitButtonSrc = null;
    private String submitLabel = null;
    private String submitStyleClass = null;
	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
    public FileUpload() {
		super();
		setCssClass("wgtInputFile");
	}

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
     * @param id unique HTML id
     */
	public FileUpload(String id) {
		super(id);
		setCssClass("wgtInputFile");
	}

	/**
     * starts the rendering
     */
	protected void endHTML(PrintWriter out){
		
		// display upload fields
		boolean upFieldVis = getContext().processBool( getId()+DIV_UPLOAD_FIELD + IContext.DOT_VIS );
		
    	out.println("<div id=\""+getId()+DIV_UPLOAD_FIELD+"\" " + JSUtil.atStyleDisplay(upFieldVis) +
    			">");
		out.println("	<form style=\"margin: 0px;\"  action=\"FileUpload?cssId="+getId()+"\" target=\""+getId()+".iframe\" method=\"POST\" enctype=\"multipart/form-data\" onsubmit=\"startUploadProgress('"+getId()+"')\" >");
		if( getFileHandler()!= null )
			out.println("		<input name=\"fileHandler\" value=\""+getFileHandler().getClass().getName()+"\" type=\"hidden\">");
		out.println("		<input name=\"cssId\" value=\""+getId()+"\" type=\"hidden\">");
		out.print("<table cellspacing=\"0\" cellpadding=\"0\" ><tr><td><input name=\"file1\" type=\"file\" style=\"float:left;\" ");
	    out.print(JSUtil.atId( getId()+FIELD_FILE_INPUT ) );
	    out.print(JSUtil.atNotEmpty("style", getStyleAsString()));
		// out.print(JSUtil.at("class", getCssClass(), "wgtInputFile")); AS: would overwrite the browser's file upload style
        out.println("></td>");
        if( StringUtils.isNotEmpty( submitButtonSrc ) ){
		    out.println("<td height=25 width=25 align=center>");
    		out.print("		<input type=\"image\" src=\""+ submitButtonSrc +"\" ");
    	    out.print( JSUtil.at("class", getCssClass(), "wgtIconButton" ));
    	    out.print( JSUtil.atId( getId()+DOT_BUTTON ) );
    	    out.println(">");
		    out.println("</td>");
        }else{
    		out.print("<td><input type=\"submit\" ");
    	    out.print( JSUtil.at("class", getSubmitStyleClass(), "wgtInputFile" ));
    	    out.print( JSUtil.at("value", TextService.getString(getSubmitLabel() ), "Submit" ));
    	    out.print( JSUtil.atId( getId()+DOT_BUTTON ) );
    	    out.println("></td>");
        }
		out.println("</tr></table></form>");
		out.println("</div>");
		
		// display progress bar
		boolean pBarVis = getContext().processBool(getId()+DIV_PROGRESS_BAR+IContext.DOT_VIS);
    	out.println("<div id=\""+getId()+DIV_PROGRESS_BAR+"\" class=\"wgtUploadProgressBar\" " +
    			JSUtil.atStyleDisplay(pBarVis) +
    			">");
		out.println("	<div id=\""+getId()+"_progressBarBox\" class=\"wgtUploadProgressBarBox\">");
		out.println("		<div id=\""+getId()+"_progressBarText\" class=\"wgtUploadProgressText\"></div>");
		out.println("		<div id=\""+getId()+"_progressBarBoxContent\" class=\"wgtUploadProgressBarBoxContent\"></div>");
		out.println("	</div>");
		out.println("</div>");

		// display filename
		boolean uploadVis = getContext().processBool(getId()+DIV_UPLOADED+IContext.DOT_VIS);
		out.println("<div id=\""+getId()+DIV_UPLOADED+"\" class=\"\" " +
				JSUtil.atStyleDisplay(uploadVis) +
				">");
		out.println("	<input type=\"text\" readonly id=\""+getId()+"\" class=\"wgtInputText wgtReadonly\" style=\"\" value=\"" + getContext().processValue(getId()) + "\" />" );
		out.println("</div>");

		// hidden iframe
		out.println("<iframe src=\"./"+ResourceServlet.SERVLET_URL_PATTERN+"/empty.html\" width=\"0\" height=\"0\" frameborder=\"0\" name=\""+getId()+".iframe\"></iframe>");
	}

	/**
	 * class that handles the file upload, the class must extend the AbstractUploadFileHandler
	 */
	public IFileHandler getFileHandler(){
		return fileHandler;
	}

	/**
	 * class that handles the file upload, the class must extend the AbstractUploadFileHandler
	 */
	public void setFileHandler( IFileHandler fileHandler){
		this.fileHandler = fileHandler;
	}

	/**
	 * there are three different modes
	 * 
	 * - new -> the file upload field is displayed
	 * - edit -> the file name is displayed
	 * - read only -> the file name is displayed and the buttons are disabled
	 */
	public void changeMode(int mode) {

		// do we want ot participate in the mode change?
		if (mode==Compound.MODE_EDIT && isFinal()) {
			return; //no, good bye
		}
		
		if( mode==Compound.MODE_EDIT ){
			getContext().add(getId()+DIV_UPLOAD_FIELD+IContext.DOT_VIS, Boolean.toString(false), IContext.TYPE_VIS, IContext.STATUS_COMMAND );
			getContext().add(getId()+DIV_PROGRESS_BAR+IContext.DOT_VIS, Boolean.toString(false), IContext.TYPE_VIS, IContext.STATUS_COMMAND);
			getContext().add(getId()+DIV_UPLOADED+IContext.DOT_VIS, Boolean.toString(true), IContext.TYPE_VIS, IContext.STATUS_COMMAND);
		}
		else if( mode==Compound.MODE_NEW ){
			getContext().add(getId()+DIV_UPLOAD_FIELD+IContext.DOT_VIS, Boolean.toString(true), IContext.TYPE_VIS, IContext.STATUS_COMMAND);
			getContext().add(getId()+DIV_PROGRESS_BAR+IContext.DOT_VIS, Boolean.toString(false), IContext.TYPE_VIS, IContext.STATUS_COMMAND);
			getContext().add(getId()+DIV_UPLOADED+IContext.DOT_VIS, Boolean.toString(false), IContext.TYPE_VIS, IContext.STATUS_COMMAND);
			
			getContext().add(getId()+DOT_BUTTON, "byIdReq('"+getId()+DOT_BUTTON+"').disabled=false;byIdReq('"+getId()+"').disabled=false;", IContext.TYPE_JS, IContext.STATUS_COMMAND);
		}
		else{
			// tell component to switch mode.
			boolean  disabled = (mode==Compound.MODE_READONLY) ; 
			getContext().add(getId()+DOT_BUTTON, "byIdReq('"+getId()+DOT_BUTTON+"').disabled="+disabled+";byIdReq('"+getId()+"').disabled="+disabled+";", IContext.TYPE_JS, IContext.STATUS_COMMAND);
		}
	}
	
	/**
	 * sets the displayed file name
	 */
	public void setValue(String value){
		if( value==null|| "".equals(value) )
			value="";
		super.setValue(value);
	}
	
	/**
	 * if the file name property is null the upload box is displayed otherwise the name is displayed
	 */
    public void loadFrom(Object data) {
		// get the property from the data object
        String fileName = (data==null)? "" : PropertyAccessor.retrieveString(data, getProperty(), getConverter());
        
        // move to context
        if (StringUtils.isEmpty( fileName) ){
        	changeMode( Compound.MODE_NEW );
        }
        else{
        	changeMode( Compound.MODE_EDIT );
        }
		setValue( fileName );
    }

    /**
     * path to the image for the submit button
     */
	public String getSubmitButtonSrc() {
		return submitButtonSrc;
	}

    /**
     * path to the image for the submit button
     */
	public void setSubmitButtonSrc(String submitButtonSrc) {
		this.submitButtonSrc = submitButtonSrc;
	}  

	/* (non-Javadoc)
	 * @see com.endress.infoserve.gui.BaseControl#setVisible(boolean)
	 */
	public void setVisible(boolean vis) {
		getContext().add(getId()+DIV_UPLOAD_FIELD+IContext.DOT_VIS, Boolean.toString(vis), IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);
	}

	public void dispatch(ClientEvent event) {
		if (event.getTypeAsInt() == EVENT_NO_FILE) {
			getPage().sendError(TextService.getString(event.getParameter(0)));
			changeMode(Compound.MODE_NEW);
		}
		else if (event.getTypeAsInt() == EVENT_UPLOAD_INFO) {
			Object uplInfo = Page.getServletRequest().getSession().getAttribute("uploadInfo-" + getId());
			if (null != uplInfo) {
				UploadInfo ui = (UploadInfo) uplInfo;
				getContext().add(getId() + ".progress", ui.toContextString(), IContext.TYPE_FILE_UPLOAD_INFO, IContext.STATUS_NOT_EDITABLE);
			}
			else
				getContext().add(getId() + ".progress", new UploadInfo().toContextString(), IContext.TYPE_FILE_UPLOAD_INFO, IContext.STATUS_NOT_EDITABLE);
		}
		else if (event.getTypeAsInt() == ClientEvent.TYPE_ACTION) {
			String filename = event.getParameter(0);
			setValue(filename);
			getContext().setVisible(getId() + DIV_PROGRESS_BAR, false);
			getContext().setVisible(getId() + DIV_UPLOADED, true);
			super.dispatch(event);
		}
		else {
			getPage().sendError(event.getParameter(0));
			changeMode(Compound.MODE_NEW); // always allow to upload another file if there was an error!
		}
	}
	
	public IActionListener getActionListener() {
		if( actionListener == null ){
			actionListener = new IActionListener(){
				public void onAction(ClientEvent event) {
					// noting to do
				}};
		}
		return super.getActionListener();
	}

	public String getSubmitLabel() {
		return submitLabel;
	}
	public void setSubmitLabel(String label) {
		this.submitLabel = label;
	}
	public void setSubmitLabelKey(String labelKey) {
		this.submitLabel = TextService.getString( labelKey );
	}

	public String getSubmitStyleClass() {
		return submitStyleClass;
	}

	public void setSubmitStyleClass(String submitStyleClass) {
		this.submitStyleClass = submitStyleClass;
	}

}

