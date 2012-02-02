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
 * like textcolumnrenderer, but allow to set a background colour, by defining a property <property>_bgcolor
 * usage:
 * set the background colour by:
 * <code>
 *    bag.addproperty("prop1_bgcolor", "white");
 * </code>
 * Note: as colour is everything allowed which will be accepted in Cascading Style Sheets:
 *  white,blue,green,...
 *  #22ee44
 *  rgb(...)
 * </pre>
 * 
 */
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;


public class ColorColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {

	public static final String BGCOLOR = "_bgcolor";
	public static final String DOT_DIV = ".div";
	
	

	public ColorColumnRenderer() {
		super();
	}
	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		String html = ctx.processValue(cellId);
//		StringBuffer html = new StringBuffer("<div " + JSUtil.atId(cellId+DOT_DIV) + " class='wgtColorBox' ");
//		if (col.getConverter() instanceof NumberConverter ) {
//			// Number shall be formated rigth
//			html.append(JSUtil.at("align", "right", null));
//		} else {
//			html.append(JSUtil.at("align", "left", null));
//		}
//		String style = col.getPage().getContext().processValue(	cellId+DOT_DIV+IContext.DOT_STYLE );
//		html.append(JSUtil.atNotEmpty("style", style) + ">")// end of div opening
//			.append(ctx.processValue(cellId+DOT_DIV)) //load read data if available
//			.append("</div>");
		return html;
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId, col.propertyFrom(data), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		if( data instanceof IDataBag )
		{
			Object srcColor = ((IDataBag)data).get( col.getProperty()+BGCOLOR );
			if( !StringUtils.isEmpty( (String)srcColor ) )
				ctx.setAttribute(whereId, "style", "background: "+srcColor);
			else
				ctx.setAttribute(whereId, "style", "background: transparent");
		}
		else {
			ctx.setAttribute(whereId, "style", "background: "+col.propertyFrom(data + BGCOLOR));
		}
	}

	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId, "", IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		ctx.setAttribute(whereId, "style", "background: transparent");
	}
}
