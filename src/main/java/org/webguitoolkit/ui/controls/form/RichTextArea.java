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

import org.apache.ecs.xhtml.textarea;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * @author i102455
 *
 */
public class RichTextArea extends Textarea implements IRichTextArea {

	public RichTextArea() {
		super();
	}

	public RichTextArea(String id) {
		super(id);
	}

	@Override
	protected void init() {
		getPage().addHeaderCSS(ResourceServlet.SERVLET_URL_PATTERN+ "/standard/jquery.rte.css");
		getPage().addHeaderJS(ResourceServlet.SERVLET_URL_PATTERN + "/jquery/jquery.rte.js");
		getPage().addHeaderJS(ResourceServlet.SERVLET_URL_PATTERN + "/jquery/jquery.rte.tb.js");
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Textarea#endHTML(java.io.PrintWriter)
	 */
	@Override
	protected void endHTML(PrintWriter out) {

		textarea t = new textarea();
		t.setID(getId());
		t.setName(getId());
		t.setRows(5);
		t.setCols(47);
//		t.setClass(getId());

		t.output(out);

		String cmd = JSUtil.jQuery(getId()) + ".rte({" + "css: ['default.css']," + "controls_rte: rte_toolbar,"
		+ "controls_html: html_toolbar" + "});";
		getPage().getContext().sendJavaScript(getId()+".js", cmd);



//		$(document).ready(function() {
//
//			jQuery('#WY1').rte({css: ['default.css'],controls_rte: rte_toolbar,controls_html: html_toolbar});
//
//			$('.rte2').rte({
//				css: ['default.css'],
//				width: 450,
//				height: 200,
//				controls_rte: rte_toolbar,
//				controls_html: html_toolbar
//			}, arr);
//		});

	}

}
