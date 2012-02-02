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
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.TextService;

public class TranslatedValueRenderer extends PlainTextColumnRenderer implements IColumnRenderer {

	protected String resourceBundelPrefix = "";
	
	public TranslatedValueRenderer(){
		super();
	}
	public TranslatedValueRenderer( String resourceBundlePrefix ){
		super();
		this.resourceBundelPrefix = resourceBundlePrefix;
	}
	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		String value = col.propertyFrom(data);
		if( StringUtils.isNotEmpty( value ))
			value = resourceBundelPrefix + value;
		ctx.add(whereId, TextService.getString( value ), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

}
