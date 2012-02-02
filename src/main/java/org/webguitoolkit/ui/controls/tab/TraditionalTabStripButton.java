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
package org.webguitoolkit.ui.controls.tab;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * @author i102415
 * 
 */
public class TraditionalTabStripButton extends Button {

	private boolean isDisabled = false;

	public TraditionalTabStripButton() {
		super();
		setDefaultCssClass("wgtTabTraditional");
	}

	public TraditionalTabStripButton(String id) {
		super(id);
		setDefaultCssClass("wgtTabTraditional");
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#endHTML(java.io.PrintWriter, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3d) {
		A link = new A();
		link.setHref("#");
		stdParameter(link);
		String action = getOnClickJS();
		link.setOnClick(action);
		link.setOnKeyDown("if (event.keyCode==13) {" + action + "};");
		if (tabindex >= 0) {
			link.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
		}

		Div left = new Div("&nbsp;");
		link.addElement(left);
		left.setClass("wgtTabButtonLeft");

		Div content = new Div();
		link.addElement(content);
		content.setClass("wgtTabButtonContent");
		// if source is specified render the link as image
		if (StringUtils.isNotEmpty(imgSrc)) {
			IMG img = new IMG(imgSrc, 0);
			img.setID(getId() + ".icon");
			content.addElement(img);
		}
		if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(getLabel())) {
			IMG img = new IMG("./images/wgt/1.gif", 0);
			img.setWidth(3);
			content.addElement(img);
		}
		if (StringUtils.isNotEmpty(text)) {
			Div label = new Div();
			label.setID(getId() + ".text");
			label.setClass("text");
			label.addElement(text);
			content.addElement(label);
		}

		Div right = new Div("&nbsp;");
		link.addElement(right);
		right.setClass("wgtTabButtonRight");

		link.output(out);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.form.Button#isDisabled()
	 */
	@Override
	public boolean isDisabled() {
		return isDisabled;
	}

	/**
	 * Not implemented
	 * 
	 * @see org.webguitoolkit.ui.controls.form.IButton#setAlignment(int)
	 */
	public void setAlignment(int pos) {
	}

	/**
	 * @see org.webguitoolkit.ui.controls.form.Button#setDisabled(boolean)
	 */
	@Override
	public void setDisabled(boolean disabled) {
		if (disabled) {
			addCssClass("tabDisabled");
		}
		else {
			removeCssClass("tabDisabled");
		}

		// auto change image of image button
		// convention: "_disabled"
		if (StringUtils.isNotEmpty(getSrc())) {
			String icon = getSrc();
			if (disabled) {
				int filetyppoint = icon.lastIndexOf('.');
				if (icon.indexOf("_disabled.") == -1) {
					setSrc(icon.substring(0, filetyppoint) + "_disabled" + icon.substring(filetyppoint, icon.length()));
				}
			}
			else {
				setSrc(icon.replaceAll("_disabled", ""));
			}
		}

		String cursorStyle = "pointer";
		if (disabled)
			cursorStyle = "default";

		// keep styles assigned to this button
		if (hasStyle())
			getContext().setAttribute(getId(), "style", "cursor: " + cursorStyle + ";" + this.getStyleAsString());
		else
			getContext().setAttribute(getId(), "style", "cursor: " + cursorStyle);

		this.isDisabled = disabled;
		getContext().setAttribute(getId(), "onclick", "");
		getContext().sendJavaScript(
				getId() + ".jsbind",
				JSUtil.jQuery(getId()) + ".unbind('click');" + JSUtil.jQuery(getId()) + ".bind('click', function(e){" + getOnClickJS()
						+ "});");
	}

	private String getOnClickJS() {
		String js = "";
		if (isDisabled) {
			js += "return false;";
		}
		js += JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + " return false;";
		return js;
	}

	@Override
	public void setLabel(String label) {
		getContext().add(id4Text(), label, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

}
