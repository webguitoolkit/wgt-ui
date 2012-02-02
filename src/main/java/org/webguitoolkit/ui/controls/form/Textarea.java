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
package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;

import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * Textarea is a text field with more than one line.&lt;br&gt;
 * </pre>
 *
 * © by endress + hauser infoserve 2007
 */
public class Textarea extends FormControl implements ITextarea {

	private final static String STATUS_LABEL_KEY = "textarea.length@Available characters";
	private final static String STATUS_MESSAGE_LABEL_KEY = "textarea.length.msg@Text longer than ";

	// shall this textarea come with an popup to edit?
	protected boolean popup = false;
	// rows visible on the screen
	protected int rows;
	// columns visible on the screen
	protected int columns;
	//max number of chars
	protected int maxlength = -1;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public Textarea() {
		super();
		setDefaultCssClass("wgtInputTextarea");
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Textarea(String id) {
		super(id);
		setDefaultCssClass("wgtInputTextarea");
	}

	public int getColumns() {
		return columns;
	}

	public boolean isPopup() {
		return popup;
	}

	public int getRows() {
		return rows;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void setPopup(boolean popup) {
		this.popup = popup;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}



	private String getId4Status() {
		return this.getId()+"sts";
	}
	private String getId4StatusContainer() {
		return this.getId()+"stscntr";
	}

	public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.FormControl#changeMode(int)
	 */
	@Override
	public void changeMode(int mode) {
		super.changeMode(mode);
		if (maxlength > -1 && !isPopup()) {
			if (mode == Compound.MODE_EDIT && isFinal()) {
				return; // no, good bye
			}
			if (mode == Compound.MODE_EDIT || mode == Compound.MODE_NEW) {
				getContext().sendJavaScript(
						getId4Status(),
						"setTextAreaStatus('" + getId() + "','" + getId4Status() + "'," + maxlength + ",'"+TextService.getString(STATUS_MESSAGE_LABEL_KEY)+"');document.getElementById('"
								+ getId4StatusContainer() + "').style.display = 'block';");
			}
			else {
				getContext().sendJavaScript(getId4Status(),
						"document.getElementById('" + getId4StatusContainer() + "').style.display = 'none';");
			}
		}
	}

	@Override
	protected void endHTML(PrintWriter out) {
		// eating the read only settings
		// eating the read only settings
		ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
		boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
		if (isPopup()) {
			addCssClass(" wgtInputTextareaPopup");
		}
		out.print("<textarea ");
		if(maxlength>-1 && !isPopup()){
			out.print(" onkeyup=\"setTextAreaStatus('"+getId()+"','"+getId4Status()+"',"+maxlength+ ",'"+TextService.getString(STATUS_MESSAGE_LABEL_KEY)+"')\" ");
		}

		out.print(JSUtil.atBool("readonly", ro));
		if (ro && getCssClass().indexOf(" wgtReadonly") == -1) {
			addCssClass("wgtReadonly");
		}
		stdParameter(out);
		if (getColumns() != 0) {
			out.print(" cols=" + getColumns());
		}
		if (getRows() != 0) {
			out.print(" rows=" + getRows());
		}

		// add tabindex to html element if the tabindex is greater than 0
		if (tabindex >= 0) {
			out.print(" " + TABINDEX + "=\"" + Integer.valueOf(tabindex).toString() + "\" ");
		}

		// action listener
		writeOnActionCommands(out, JSUtil.ACTION_ON_RETURN);

		// pop up is not taken into account right now...
		out.print(">" + getContext().processValue(getId()) + "</textarea>");
		//print Status if Maxlength >-1 and this is no popup
		if(maxlength>-1 && !isPopup()){
			out.print("<div id=\""+getId4StatusContainer()+"\" style=\"display:none\">");
			out.print("<span class=\"wgtStatus\">"+TextService.getString(STATUS_LABEL_KEY)+": </span><span class=\"wgtStatus\" id=\""+getId4Status()+"\"></span>");
			out.print("</div>");
		}
		if (isPopup()) {
			out.print("<img src=\"./images/wgt/expand.gif\" class=\"wgtPointerCursor\" onclick=\"txtareapopup('" + getId() + "');\">");
		}

	}

}
