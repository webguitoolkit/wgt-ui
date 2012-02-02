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
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;


/**
 * <pre>
 * Cells of a column where the ImageColumnRenderer is assigned display images.
 * 
 * The source of the image is the property of the data object. e.g.: ./images/someimage.gif
 * 
 * The title of the image can be set only if DataBags are used. The property + ImageColumnRenderer.DOT_TITLE 
 * contains the title.
 * eg.: dataBag.addProperty("image"+ ImageColumnRenderer.DOT_TITLE, "Nice Image Title" );
 * </pre>
 */
public class ImageColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {

	private static final Logger LOGGER = Logger.getLogger(ImageColumnRenderer.class);
	
	public static final String BLANK_IMG = "./images/wgt/1.gif";
	
	public static final String DOT_SRC = ".src";
	public static final String DOT_TITLE = ".title"; // attribute for title of image-element
	public static final String DOT_IMG = ".img"; // id of img element
	
	/**
	 * @see org.webguitoolkit.ui.controls.table.IColumnRenderer#generateHTML(ITableColumn, String, int, int)
	 */
	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		return "<img " + JSUtil.atId(cellId + DOT_IMG)+ 
				JSUtil.at("src", ctx.processValue(cellId + DOT_IMG + DOT_SRC ), BLANK_IMG ) +
				JSUtil.atNotEmpty("title", ctx.processValue(cellId + DOT_IMG + DOT_TITLE)) +
				JSUtil.atStyleVisible("", ctx.processValue(cellId+ DOT_IMG + IContext.DOT_VIS)) +
				">";
	}

	/**
	 * @see org.webguitoolkit.ui.controls.table.IColumnRenderer#load(String, Object, ITableColumn, IContext, int, int)
	 */
	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		// may be we have to make the html visible again...
		String visVal = ctx.getValue(whereId+ DOT_IMG + IContext.DOT_VIS);
		if ("false".equals(visVal)) {
			ctx.add(whereId+ DOT_IMG + IContext.DOT_VIS, "true", IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);			
		}
		// data must be of type string
		Object src = PropertyAccessor.retrieveProperty(data,col.getProperty());
		if (src==null) {
			//throw new WGTException("img src not set");
			LOGGER.warn("img src not set, using ./images/wgt/1.gif");
			src = "./images/wgt/1.gif";
		}
		ctx.add(whereId+DOT_IMG+ DOT_SRC, ""+src, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);

		if( data instanceof IDataBag ){
			Object srcTitle = ((IDataBag)data).get( col.getProperty()+DOT_TITLE );
			if( !StringUtils.isEmpty( (String)srcTitle ) )
				ctx.add(whereId+DOT_IMG+ DOT_TITLE, ""+srcTitle, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
		
	}

	/**
	 * @see com.endress.infoserve.wgt.controls.table.IColumnRenderer#clear(java.lang.String, com.endress.infoserve.wgt.controls.table.TableColumn, com.endress.infoserve.wgt.ajax.Context, int, int)
	 */
	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		ctx.add(whereId+ DOT_IMG + IContext.DOT_VIS, "false", IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);
	}

}
