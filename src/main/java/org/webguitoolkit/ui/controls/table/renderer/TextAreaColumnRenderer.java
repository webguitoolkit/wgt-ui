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

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.TextArea;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.Textarea;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.style.StyleAttribute;

/**
 * <pre>
 * Renderer for TextAreas for the column's cells 
 * </pre>
 */
public class TextAreaColumnRenderer extends ChildColumnRenderer {

	public TextAreaColumnRenderer() {
		super();
	}
	/**
	 * @deprecated
	 */
	public TextAreaColumnRenderer(ITableColumn col) {
		super();
	}
	
	
	protected FormControl[] getFormControls() {
		TableTextarea text = new TableTextarea();
		text.setParent( table );
		text.setProperty( tableColumn.getProperty() );
		text.setCompound( compound );
		if( tableColumn.getIsDisplayed() )
			compound.addElement(text);
		return new FormControl[]{ text };
	}


	public Textarea getTextField(){
		return (Textarea) myControls[0];
	}
	public class TableTextarea extends Textarea {

		protected void endHTML(PrintWriter out) {
			// eating the read only settings
			// eating the read only settings
			ContextElement ce = getContext()
					.processContextElement(getId() + DOT_RO);
			boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
			if (StringUtils.isEmpty(getCssClass())) {
				setCssClass("wgtInputTextarea");
			}
			if (isPopup()) {
				addCssClass("wgtInputTextareaPopup");
			}

	        this.getStyle().addStyleAttribute(new StyleAttribute("background","transparent"));

			TextArea textArea = new TextArea();
			textArea.setReadOnly( ro );
			if (ro && getCssClass().indexOf( " wgtReadonly" )!=-1 ) {
				addCssClass("wgtReadonly");
			}
			stdParameter(textArea);
			if (getColumns() != 0) {
				textArea.setCols( getColumns() );
			}
			if (getRows() != 0) {
				textArea.setRows( getRows() );
			}

			// action listener
			writeOnActionCommands( textArea, JSUtil.ACTION_ON_RETURN );
			
			// pop up is not taken into account right now...
			textArea.addElement( getContext().processValue(getId()) );

			textArea.output( out );
			if (isPopup()) {
				out.print("<img src=\"./images/wgt/expand.gif\" class=\"wgtPointerCursor\" onclick=\"txtareapopup('"
								+ getId() + "');\">");
			}

		}
	}
}
