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
/**
 * (c) 2007, Endress&Hauser InfoServe GmbH & Co KG
 * Created on 25.07.2007
 */
package org.webguitoolkit.ui.controls.table.renderer;

import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.table.ITableColumn;

/**
 * <pre>
 * Renders content of cell as html if any tags are present
 * </pre>
 * @author Thorsten Springhart
 *
 */
public class HtmlColumnRenderer extends PlainTextColumnRenderer {

	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		return ctx.processValue(cellId);// load read data if available
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId, col.propertyFrom(data), IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
	}
	
	/*
	 * Need to override super-method, because clear only works correctly with
	 * IContext.TYPE_HTML.
	 */
	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId, "", IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
	}
}
