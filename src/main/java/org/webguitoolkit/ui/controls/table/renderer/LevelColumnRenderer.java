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

import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;


/**
 * <pre>
 * Renders the column as level where the property is the percentage value. (e.g. 50 )
 * 
 * -------------------------------
 * |########|     30%             | 
 * -------------------------------
 * |##############63%#|           | 
 * -------------------------------
 * |#############100%#############| 
 * -------------------------------
 * |#############|50%             | 
 * -------------------------------
 * </pre>
 *
 */
public class LevelColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {

	public static final String DOT_LEVELBOX = ".levelbox";
	public static final String DOT_LEVELTXT = ".leveltxt";
	public static final String DOT_LEVELRIGHT = ".levelright";
	public static final String DOT_LEVELLEFT = ".levelleft";

	public LevelColumnRenderer() {
		super();
	}

	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		// generate for leveltext and width as well.
		
		String vis = ctx.processValue(cellId+ DOT_LEVELBOX + IContext.DOT_VIS);
		
		String html = "<div class=\"wgtPBar\"" + JSUtil.atId(cellId+DOT_LEVELBOX) + JSUtil.atStyleVisible("", vis) +
				"><div class=\"wgtPBarTxt\"" + JSUtil.atId(cellId+DOT_LEVELTXT) +
				" >";
		html += ctx.processValue(cellId+DOT_LEVELTXT) + "</div><img src=\"./images/wgt/1.gif\" class=\"wgtPBarLeft\" " 
			 + JSUtil.atId(cellId+DOT_LEVELLEFT) 
			 + JSUtil.atNotEmpty("style", ctx.processValue(cellId+DOT_LEVELLEFT+IContext.DOT_STYLE)  )+ ">";
		html += "</div>";
		return html;
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		// may be we have to make the html visible again...
		String visVal = ctx.getValue(whereId+ DOT_LEVELBOX + IContext.DOT_VIS);
		if ("false".equals(visVal)) {
			ctx.add(whereId+ DOT_LEVELBOX + IContext.DOT_VIS, "true", IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);			
		}
		// data must be of type number to view as ProgressIndicator	
		Object percent = PropertyAccessor.retrieveProperty(data,col.getProperty());
		if (percent==null) throw new WGTException("level not set");
		int pc = ((Number) percent).intValue();
		
		ctx.add(whereId+DOT_LEVELTXT, ""+pc+"%", IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE );
		// set the attribute fr the style with correct width
		// Javascript will set the corect width for the 'image'
		ctx.add(whereId+DOT_LEVELLEFT+IContext.DOT_STYLE, "width: "+pc+"%;", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		//ctx.add(whereId+DOT_LEVELRIGHT+BaseControl.DOT_STYLE, "width: "+(100-pc)+"%;", BaseControl.TYPE_ATT, BaseControl.STATUS_NOT_EDITABLE);
	}

	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		// ausknipsen der inhalte
		ctx.add(whereId+ DOT_LEVELBOX + IContext.DOT_VIS, "false", IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);
	}

}
