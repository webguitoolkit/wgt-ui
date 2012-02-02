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

import org.apache.commons.lang.StringEscapeUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverterPrecise;

/**
 * <pre>
 * Standard renderer for columns, value is displayed as text string.
 * </pre>
 */
public class PlainTextColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {

	private static final long serialVersionUID = 1L;

	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		if (col.getConverter() instanceof NumberConverter || col.getConverter() instanceof NumberConverterPrecise) {
			// Number shall be formated right
			ctx.add(cellId + ".align", "right", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
		return StringEscapeUtils.escapeHtml( ctx.processValue(cellId) );// load read data if available
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId, col.propertyFrom(data), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId, "", IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

}
