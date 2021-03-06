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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.Tooltip;

/**
 * This Renderer combines the elements of a Collection to a single String. You can specify
 * a separator that is used to split the single items of the Collection. Also, you can 
 * limit the content length to a certain number of digits and set individual teaser dots. 
 * 
 * @author i102487
 *
 */
public class CollectionToStringRenderer extends AbstractColumRenderer implements IColumnRenderer {

	private static final Logger logger = Logger.getLogger(TeaserTextRenderer.class);

	private static final String DOT_SPAN = ".span";

	private int teaserCharacters = 0;
	private String teaserDots = "...";
	private String separator = ",";
	
	/**
	 * Use this constructor if no abbreviation of the cell content is required
	 * 
	 * @param separator
	 */
	public CollectionToStringRenderer(String separator) {
		this.separator = separator;
	}
	
	/**
	 * 
	 * @param teaserCharacters - must be greater than or equal to 4
	 */
	public CollectionToStringRenderer(int teaserCharacters) {
		super();
		if (teaserCharacters >= 4) {
			this.teaserCharacters = teaserCharacters;
		} else {
			logger.warn("teaserCharacters must be greater than or equal to 4 but is " + teaserCharacters);
			this.teaserCharacters = 4;
		}
	}

	public CollectionToStringRenderer(int teaserCharacters, String teaserDots){
		this(teaserCharacters);
		this.teaserDots = teaserDots;
		
	}
	
	public CollectionToStringRenderer(int teaserCharacters, String teaserDots, String separator){
		this(teaserCharacters);
		if (teaserDots != null) {
			this.teaserDots = teaserDots;
		}
		this.separator = separator;
		
	}
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.table.IColumnRenderer#generateHTML(org.webguitoolkit.ui.controls.table.TableColumn, java.lang.String, int, int)
	 */
	public String generateHTML(ITableColumn col, String cellId, int idxRow,int idxCol) {
		IContext ctx = col.getPage().getContext();
		String value = ctx.processValue(cellId + DOT_SPAN + ".tooltipText1");
		String shortValue = ctx.processValue(cellId + DOT_SPAN);

		Tooltip tip = new Tooltip(value);
		tip.setTrackMouseMovement(true);
		String js = "jQuery("+JSUtil.jQuery( cellId+ DOT_SPAN )+").tooltip("+tip.getJQueryParameter()+");"; 
		ctx.sendJavaScript(cellId+".tooltip", js);
		
		return "<span " + JSUtil.atId(cellId + DOT_SPAN) + " title=\"" + value
				+ "\">" + shortValue + "</span>";
		
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.table.IColumnRenderer#load(java.lang.String, java.lang.Object, org.webguitoolkit.ui.controls.table.TableColumn, org.webguitoolkit.ui.ajax.IContext, int, int)
	 */
	public void load(String cellId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
//		String value = StringUtils.defaultString(PropertyAccessor.retrieveString(data, col.getProperty()));
		Collection valueList = new ArrayList();
		valueList = (Collection) PropertyAccessor.retrieveProperty(data, col.getProperty());
		
		String value = StringUtils.join(valueList, this.separator);
		String shortValue;
		
		if (teaserCharacters >= 4) {
			shortValue = abbreviate(value, 0, teaserCharacters);
		}
		else {
			shortValue = value;
		}

		// disable tooltip if there is no need to abbreviate the value 
		if (value.equals(shortValue)) {
			value = "";
		}
		
		ctx.innerHtml(cellId + DOT_SPAN, shortValue);
		ctx.setAttribute(cellId + DOT_SPAN, "tooltipText1", value);
		ctx.sendJavaScript(cellId+ ".tooltipText", "jQuery("+JSUtil.jQuery(cellId+DOT_SPAN)+").get(0).tooltipText='"+StringEscapeUtils.escapeJavaScript(value)+"';" );
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.table.IColumnRenderer#clear(java.lang.String, org.webguitoolkit.ui.controls.table.TableColumn, org.webguitoolkit.ui.ajax.IContext, int, int)
	 */
	public void clear(String cellId, ITableColumn col, IContext ctx, int idxRow,int idxCol) {
		ctx.innerHtml(cellId + DOT_SPAN, "");
	}
	
    public String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return "";
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if ((str.length() - offset) < (maxWidth - 3)) {
            offset = str.length() - (maxWidth - 3);
        }
        if (offset <= 4) {
            return (str.substring(0, maxWidth - 3) + teaserDots);
        }
        if (maxWidth < 7) {
           // throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if ((offset + (maxWidth - 3)) < str.length()) {
           // return (teaserDots + abbreviate(str.substring(offset),0, maxWidth - 3));
        }
        return (teaserDots + str.substring(str.length() - (maxWidth - 3)));
    }
}
