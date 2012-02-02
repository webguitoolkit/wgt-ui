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
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.Tooltip;

/**
 * @author i102454
 *
 * Allows a tooltip which is completelz different than the cell value.
 * The tooltip must be defined as a seperate property in the databag.
 */
public class TooltipColumnRenderer  implements IColumnRenderer{

	private static final Logger logger = Logger.getLogger(TooltipColumnRenderer.class);

	private static final String DOT_SPAN = ".span";

	private String propertyName;
	private String lineSeperator;

	/**
	 * 
	 * @param propertyName name of the Tooltip property found in the DataBag
	 */
	public TooltipColumnRenderer(String propertyName) {
		super();
		if(StringUtils.isNotEmpty(propertyName)){
			this.propertyName = propertyName;
		}
	}
	
	public TooltipColumnRenderer(String propertyName, String lineSeperator) {
		
		super();
		if(StringUtils.isNotEmpty(propertyName)){
			this.propertyName = propertyName;
			this.lineSeperator = lineSeperator;
		}
	}
	
	public String generateHTML(ITableColumn col, String cellId, int idxRow,int idxCol) {
		
		IContext ctx = col.getPage().getContext();
		String title = ctx.processValue(cellId + DOT_SPAN + ".title");
		String value = ctx.processValue(cellId + DOT_SPAN);
		
		String shortValue = ctx.processValue(cellId + DOT_SPAN);
		return "<span " + JSUtil.atId(cellId + DOT_SPAN) + " title=\"" + title
				+ "\">" + value + "</span>";
	}

	private void createTooltip(String cellId, Page page) {
	}

	public void load(String cellId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		String value = PropertyAccessor.retrieveString(data, col.getProperty());
		ctx.innerHtml(cellId + DOT_SPAN, value);

		if(StringUtils.isNotEmpty(propertyName)){
			String tooltip = PropertyAccessor.retrieveString(data, propertyName);
			
			ctx.setAttribute(cellId , "title", tooltip);
			Tooltip tip = new Tooltip(tooltip);
			if(StringUtils.isNotEmpty(lineSeperator)){
				tip.setShowBody(lineSeperator);
			}
			col.setTooltip(tip);
			tip.setTrackMouseMovement(true);
			String js = "jQuery("+JSUtil.jQuery(cellId)+").tooltip("+tip.getJQueryParameter()+");";
			ctx.sendJavaScript(cellId, js);
		}
	}
	
	public void clear(String cellId, ITableColumn col, IContext ctx, int idxRow,int idxCol) {
		ctx.innerHtml(cellId + DOT_SPAN, "");
	}

	public String renderAsPlainText(Object data, ITableColumn col) {
		return null;
	}
}