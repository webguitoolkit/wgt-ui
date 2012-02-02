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
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.TextService;

public class BooleanColumnRenderer extends PlainTextColumnRenderer implements IColumnRenderer {

	protected String trueRepresentation = "true";
	protected String falseRepresentation = "false";

	/**
	 * @param trueRepresentation
	 *            e.g. Yes, On, Valid, 1 ....
	 * @param falseRepresentation
	 *            e.g. No, Off, invalid, 0 ....
	 *
	 */
	public BooleanColumnRenderer(String trueRepresentationKey, String falseRepresentationKey) {
		super();
		if (trueRepresentation != null)
			this.trueRepresentation = TextService.getString(trueRepresentationKey);
		if (falseRepresentation != null)
			this.falseRepresentation = TextService.getString(falseRepresentationKey);
	}

	@Override
	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		boolean value = false, empty = false;
		if (data instanceof IDataBag) {
			if (((IDataBag)data).get(col.getProperty()) != null) {
				value = ((IDataBag)data).getBool(col.getProperty());
			}
			else {
				empty = true;
			}
		}
		if (empty)
			ctx.add(whereId, "", IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		else if (value)
			ctx.add(whereId, trueRepresentation, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		else
			ctx.add(whereId, falseRepresentation, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

	@Override
	public String renderAsPlainText(Object data, ITableColumn col) {
		boolean value = false;
		if (data instanceof IDataBag) {
			value = ((IDataBag) data).getBool(col.getProperty());
		}
		return value ? trueRepresentation : falseRepresentation;
	}

}
