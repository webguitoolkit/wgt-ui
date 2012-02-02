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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;

public class TooltipRenderer extends AbstractColumRenderer implements IColumnRenderer {

	private static final Logger logger = Logger.getLogger(TooltipRenderer.class);

	private static final String DOT_SPAN = ".span";
	public static final String TOOLTIP = "tooltip";
	private String tooltipProperty;

	/**
	 * 
	 * @param tooltipPropertyName
	 */
	public TooltipRenderer(String tooltipPropertyName) {
		super();
		this.tooltipProperty = tooltipPropertyName;
	}

	public String generateHTML(ITableColumn col, String cellId, int idxRow,int idxCol) {
		IContext ctx 	= col.getPage().getContext();
		String value 	= ctx.processValue(cellId + DOT_SPAN);
		String tooltip 	= ctx.processValue(cellId + DOT_SPAN + ".title");

		return "<span " + JSUtil.atId(cellId + DOT_SPAN) + " title=\"" + tooltip + "\">" + value + "</span>";
	}
	
	public void load(String cellId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		String value = col.propertyFrom(data);
		String tooltip = value;
		if(StringUtils.isNotEmpty(tooltipProperty) && data instanceof DataBag && ((DataBag)data).getObject(tooltipProperty) != null){
			tooltip = (String)((DataBag)data).getObject(tooltipProperty);
		}
		ctx.innerHtml(cellId + DOT_SPAN, value);
		ctx.setAttribute(cellId + DOT_SPAN, "title", tooltip);
	}
	
	public void clear(String cellId, ITableColumn col, IContext ctx, int idxRow,int idxCol) {
		ctx.innerHtml(cellId + DOT_SPAN, "");
		ctx.setAttribute(cellId + DOT_SPAN, "title", "" );
	}
}
