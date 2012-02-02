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
package org.webguitoolkit.ui.controls.form.button;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * @author i01002455
 * 
 */
public class PlainCssButton extends Button implements IButton {

	private String clazz = null, clazzDis = null;
	private boolean isDisabled = false;

	public PlainCssButton(String cssClass, String cssClassDisabled) {
		this.clazz = cssClass;
		this.clazzDis = cssClassDisabled;

		if (clazzDis == null) {
			clazzDis = clazz + "-disabled";
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#endHTML(java.io.PrintWriter, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3d) {
		Div link = new Div();

		link.setClass(clazz);
		
		if(getTooltip()!=null){
			link.setTitle(getTooltip());
		}

		stdParameter(link);
		if (getConfirmMsg() != null) {
			link.setOnClick(JSUtil.jsConfirm(TextService.getString(getConfirmMsg()), JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)
					+ " return false;"));
		}
		else {
			link.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + " return false;");
		}

		// add tabindex to html element if the tabindex is greater than 0
		if (tabindex >= 0) {
			link.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
		}

		link.output(out);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#isDisabled()
	 */
	@Override
	public boolean isDisabled() {
		return isDisabled;
	}

	@Override
	public void dispatch(ClientEvent event) {
		if (!isDisabled)
			super.dispatch(event);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#setDisabled(boolean)
	 */
	@Override
	public void setDisabled(boolean disabled) {
		// auto change class of button
		// convention: "-disabled"
		if (disabled) {
			getContext().removeClass(getId(), clazz);
			getContext().addClass(getId(), clazzDis);
		}
		else {
			getContext().removeClass(getId(), clazzDis);
			getContext().addClass(getId(), clazz);
		}

		this.isDisabled = disabled;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.IButton#setAlignment(int)
	 */
	public void setAlignment(int pos) {
		// nothing to do
	}

}
