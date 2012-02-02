package org.webguitoolkit.ui.controls.form.button;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

public class StandardButton extends Button implements IButton {

	private int position = 0;
	private boolean isDisabled = false;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public StandardButton() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public StandardButton(String id) {
		super(id);
	}

	protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D) {
		// TODO Auto-generated method stub
		// <button type="button" class="btn"><span><span>longer button text</span></span></button>
		org.apache.ecs.html.A button = new org.apache.ecs.html.A();
		button.setHref("#");

		String standardStyle = "wgtButton2";
		switch (position) {
			case POSITION_LEFT:
				standardStyle += " wgtButton2-l";
				break;
			case POSITION_MIDDLE:
				standardStyle += " wgtButton2-c";
				break;
			case POSITION_RIGHT:
				standardStyle += " wgtButton2-r";
				break;
		}
		setDefaultCssClass(standardStyle);
		stdParameter(button);
		button.setOnClick(getOnClickJS());
		button.setOnKeyDown(getOnKeyDownJS());

		Span outher = new Span();
		button.addElement(outher);
		outher.setID(getId() + ".outherspan");

		Span inner = new Span();
		outher.addElement(inner);
		inner.setID(getId() + ".innerspan");

		// if source is specified render the link as image
		if (StringUtils.isNotEmpty(imgSrc)) {
			IMG img = new IMG(imgSrc, 0);
			img.setID(getId() + ".icon");
			inner.addElement(img);
		}
		if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(text)) {
			IMG img = new IMG("./images/wgt/1.gif", 0);
			img.setWidth(3);
			inner.addElement(img);
		}
		if (StringUtils.isNotEmpty(text)) {
			Div label = new Div();
			label.setID(getId() + ".text");
			label.setClass("text");
			label.addElement(text);
			inner.addElement(label);
		}
		// add tabindex to html element if the tabindex is greater than 0
		if (tabindex >= 0) {
			button.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
		}

		button.output(out);

	}

	private String getOnClickJS() {
		String js = "";
		if (isCancelBubble()) {
			js += "event.cancelBubble=true;";
		}
		if (isDisabled) {
			js += "return false;";
		}
		else if (!isSimpleLink()) {
			if (getConfirmMsg() != null) {
				js += JSUtil.jsConfirm(TextService.getString(getConfirmMsg()), JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)
						+ " return false;");
			}
			else {
				js += JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + " return false;";
			}
		}
		else {
			if (StringUtils.isBlank(getTarget())) {
				js += "location='" + getLinkURL() + "'";
			}
			else {
				js += "window.open('" + getLinkURL() + "','" + getTarget() + "')";
			}
		}
		return js;
	}

	private String getOnKeyDownJS() {
		String js = "if (event.keyCode==13) {" + getOnClickJS() + "};";
		return js;
	}

	public void setDisabled(boolean disabled) {
		if (disabled) {
			getContext().setAttribute(getId() + ".outherspan", "class", "disabled");
			getContext().setAttribute(getId() + ".innerspan", "class", "disabled");
		}
		else {
			getContext().setAttribute(getId() + ".outherspan", "class", "");
			getContext().setAttribute(getId() + ".innerspan", "class", "");
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
		getContext().setAttribute(getId(), "onkeydown", "");
		getContext().sendJavaScript(
				getId() + ".jsbind",
				JSUtil.jQuery(getId()) + ".unbind('click').unbind('keydown').bind('click', function(event){" + getOnClickJS()
						+ "}).bind('keydown', function(event){" + getOnKeyDownJS() + "});");
	}

	public void setLabel(String label) {
		getContext().add(id4Text(), label, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

	/**
	 * this function can be used to display more then one Button in a row.
	 * 
	 * -------------------------------------------------------------------- | POSITION_LEFT | POSITION_CENTER | POSITION_CENTER |
	 * POSITION_RIGHT | --------------------------------------------------------------------
	 * 
	 * @param position the alignment of the button
	 */
	public void setAlignment(int position) {
		this.position = position;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

}
