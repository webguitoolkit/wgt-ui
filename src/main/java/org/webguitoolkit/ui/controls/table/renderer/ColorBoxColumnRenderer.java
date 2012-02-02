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

/**
 * <pre>
 * renderer class that displays a box filled in the colour of properties value,
 * can be used to display state, critically information, ...
 * usage:
 * 
 * set the background colour by:
 * <code>
 *    bag.addproperty("prop1", "white");
 *    bag.addproperty("prop1", "#ffffff");
 * </code>
 * Note: as colour is everything allowed which will be accepted in Cascading Style Sheets:
 *  white,blue,green,...
 *  #22ee44
 *  rgb(...)
 * </pre>
 * 
 */

import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;


public class ColorBoxColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {

	public static final String DOT_DIV = ".div";
	
	public ColorBoxColumnRenderer() {
		super();
	}

	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		String style = col.getPage().getContext().processValue(	cellId+DOT_DIV+IContext.DOT_STYLE );
		String html = "<div align=\"center\" " + JSUtil.atId(cellId+DOT_DIV) + " class='wgtColorBox' " +
				JSUtil.at("style", style, "" ) + ">";
		html += "</div>";
		return html;
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		//ctx.add(whereId+DOT_DIV+ Context.DOT_VIS, "true", Context.DOT_VIS, Context.STATUS_NOT_EDITABLE );
		ctx.add(whereId+".align", "center", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE );
		ctx.add(whereId+DOT_DIV+IContext.DOT_STYLE, "background: "+col.propertyFrom(data)+"; display: block;", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE );
	}

	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		//ctx.add(whereId+DOT_DIV+ Context.DOT_VIS, "false", Context.DOT_VIS, Context.STATUS_NOT_EDITABLE );
		ctx.add(whereId+DOT_DIV+IContext.DOT_STYLE, "background:#ffffff; display: none;", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
	}
}
