package org.webguitoolkit.ui.controls.form.button;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

public class LinkButton extends Button implements IButton {

	private boolean isDisabled = false;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public LinkButton() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public LinkButton(String id) {
		super(id);
	}

	protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D) {
		A link = new A("#");

		String standardStyle = mode3D ? "wgtButtonLink3D" : "wgtButtonLink";
		setDefaultCssClass(standardStyle);
		// add cssClass from basecontrol
		if (StringUtils.isNotBlank(getCssClass())) {
			standardStyle += " " + getCssClass().trim();
			setDefaultCssClass(standardStyle);
		}

		stdParameter(link);
		if (!isSimpleLink()) {
			if (getConfirmMsg() != null) {
				if (cancelBubble)
					link.setOnClick(JSUtil.jsConfirm(TextService.getString(getConfirmMsg()),
							JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + "stopEvent(event); return false;"));
				else
					link.setOnClick(JSUtil.jsConfirm(TextService.getString(getConfirmMsg()),
							JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + "return false;"));
			}
			else {
				if (cancelBubble)
					link.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + "stopEvent(event);return false;");
				else
					link.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + "return false;");
			}
		}
		else {
			link.setHref(getLinkURL());
			if (StringUtils.isNotBlank(getTarget())) {
				link.setTarget(getTarget());
			}
		}

		if (mode3D) {
			link.setOnMouseOver("classAdd( this, '" + standardStyle + "_over' );");
			link.setOnMouseOut("classRemove( this, '" + standardStyle + "_over' );");
		}

		// if source is specified render the link as image
		if (StringUtils.isNotEmpty(imgSrc)) {
			IMG img = new IMG(imgSrc, 0);
			img.setID(getId() + ".icon");

			if (StringUtils.isNotEmpty(getLabel()))
				img.setStyle("float: left;");

			link.addElement(img);
		}
		if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(getLabel())) {
			IMG img = new IMG("./images/wgt/1.gif", 0);
			img.setStyle("float: left;");
			img.setWidth(3);
			link.addElement(img);
		}
		if (StringUtils.isNotEmpty(text)) {
			Span label = new Span();
			label.setID(getId() + ".text");
			// MH: removed due to problems with FF8
			// label.setStyle("vertical-align:top;");
			label.addElement(text);
			link.addElement(label);
		}
		// add tabindex to html element if the tabindex is greater than 0
		if (tabindex >= 0) {
			link.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
		}

		link.output(out);
	}

	public void setDisabled(boolean disabled) {
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
		if (StringUtils.isNotEmpty(getLabel())) {
			if (disabled)
				addCssClass("wgtLinkButtonDisabled");
			else
				removeCssClass("wgtLinkButtonDisabled");
		}

		String cursorStyle = disabled ? "default" : "pointer";
		// keep styles assigned to this button
		if (hasStyle())
			getContext().setAttribute(getId(), "style", "cursor: " + cursorStyle + ";" + this.getStyleAsString());
		else
			getContext().setAttribute(getId(), "style", "cursor: " + cursorStyle);

		this.isDisabled = disabled;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setAlignment(int pos) {
		// nothing to do here
	}

	@Override
	public void dispatch(ClientEvent event) {
		if (!isDisabled)
			super.dispatch(event);
	}
}
