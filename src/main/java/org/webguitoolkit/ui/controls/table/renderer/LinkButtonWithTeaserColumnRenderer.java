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
package org.webguitoolkit.ui.controls.table.renderer;

import java.io.StringWriter;

import org.apache.ecs.html.Div;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * This renderer depends on the jquery shorten plugin, if mozilla is used. <br>
 * You have to define a css class threedots_elements like: <br><code>
 * 		.threedots_element { <br>
 * 			white-space: nowrap; <br>
 * 		    overflow: hidden; <br>
 * 		    -o-text-overflow: ellipsis;    (Opera 9-10) <br>
 * 		    text-overflow:    ellipsis;    /IE, WebKit (Safari, Chrome), Opera 11) <br>
 * 		    margin: 0; <br>
 * 		    cursor: pointer; <br>
 * 		} <br>
 * </code>
 * <br>
 * For all browser != mozilla it used the text-overflow: ellipsis attribute. This attribute is<br>
 * not available for mozilla, therefore we use a jquery call. <br>
 * 
 * @author Timo Dreier (i102497)
 * 
 */
public class LinkButtonWithTeaserColumnRenderer extends LinkButtonColumnRenderer {
	private static final Logger logger = Logger.getLogger(LinkButtonWithTeaserColumnRenderer.class);
	private String DOT_DIV = ".div";
	private int width = -1;

	/**
	 * @param newLabelKey pass null to use a value from a property
	 * @param newListener listener
	 */
	public LinkButtonWithTeaserColumnRenderer(String newLabelKey, ITableButtonActionListener newListener, int width) {
		super(newLabelKey, null, newListener);
		this.width = width;
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		if (isStatic) {
			super.load(whereId, data, col, ctx, idxRow, idxCol);
		}
		else {
			init(col);
		}
		String oldLabel = getButton().getLabel();

		Div div = new Div();
		div.setClass("threedots_element");

		// width is necessary to calculate width for jquery plugin, also ie needs a with for the
		// text-overflow: ellipsis - attribute.
		div.setStyle("width: " + String.valueOf(width));

		Div innerDiv = new Div();
		div.addElement(innerDiv);
		innerDiv.setClass("threedots");
		innerDiv.setID(whereId + DOT_DIV);
		innerDiv.setTitle(oldLabel);

		innerDiv.addElement(oldLabel);

		StringWriter sw = new StringWriter();
		div.output(sw);

		getButton().setLabel(sw.toString());

		// show advanced tooltip
		String js = JSUtil.jQuery(whereId + DOT_DIV) + ".tooltip();";
		ctx.sendJavaScript(whereId + DOT_DIV + ".tooltip", js);

		// only call jquery plugin for mozilla ... in ie, opera, google chrome use text-overflow attribute in css, because its
		// faster.
		String id = whereId + DOT_DIV;
		id = id.replaceAll("\\.", "\\\\\\\\.");
		String javascript = "if (jQuery.browser.mozilla) {" + "jQuery('" + "#" + id
				+ "').shorten({tail: '...',  tooltip: false});" + "}";
		ctx.sendJavaScript(whereId + DOT_DIV + "_threedots", javascript);
	}
}
