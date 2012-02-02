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
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.renderer.AbstractColumRenderer;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;

/**
 * The CSSBackgroundColumnRenderer is an easy way to render any CCS Background-Definition into the cell.
 * 
 * set class name: bag.addProperty("prop", "classname");
 * optional set class title: bag.addProperty("prop.title", "Title");
 * 
 * @author Ben
 * 
 */

public class CSSBackgroundColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {
	private static final long serialVersionUID = -4875264855171303285L;

	public static final String BLANK_IMG = "./images/wgt/1.gif";

	public static final String DOT_CLASS = ".clazz";
	public static final String DOT_DIV = ".div";
	public static final String DOT_TITLE = ".title"; // attribute for title of image-element

	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		return "<div" + JSUtil.atId(cellId + DOT_DIV) + JSUtil.at("class", ctx.processValue(cellId + DOT_CLASS), "") + "/>";
	}

	public void load(String cellId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		// data must be of type string
		Object clazz = PropertyAccessor.retrieveProperty(data, col.getProperty());
		if (clazz == null) {
			// no class set
			clazz = "";
		}
		ctx.add(cellId + DOT_CLASS, String.valueOf(clazz), IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		ctx.addClass(cellId + DOT_DIV, ctx.processValue(cellId + DOT_CLASS));

		if (data instanceof IDataBag) {
			Object srcTitle = ((IDataBag)data).get(col.getProperty() + DOT_TITLE);
			if (!StringUtils.isEmpty((String)srcTitle))
				ctx.add(cellId + DOT_DIV + DOT_TITLE, "" + srcTitle, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
	}

	public void clear(String cellId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.removeClass(cellId + DOT_DIV, ctx.processValue(cellId + DOT_CLASS));
	}

}
