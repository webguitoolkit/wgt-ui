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

import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverterPrecise;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.controls.util.style.StyleAttribute;

/**
 * <pre>
 * Renderer for TextFields for the column's cells
 * </pre>
 */
public class TextColumnRenderer extends ChildColumnRenderer {

	public TextColumnRenderer() {
		super();
	}

	/**
	 * @deprecated
	 */
	public TextColumnRenderer(ITableColumn col) {
		super();
	}

	protected FormControl[] getFormControls() {
		TableText text = new TableText();
		text.setParent(table);
		text.setProperty(tableColumn.getProperty());
		text.setCompound(compound);
		if (tableColumn.getIsDisplayed())
			compound.addElement(text);
		return new FormControl[] { text };
	}

	public Text getTextField() {
		return (Text)myControls[0];
	}

	public class TableText extends org.webguitoolkit.ui.controls.form.Text {

		protected void init() {
			// send bug report to martin on 8.7.08
			// super.init();
		}

		/**
		 * @param out
		 */
		protected void inputFieldHTML(PrintWriter out) {
			out.print("<input type=");
			if (isPassword()) {
				out.print("password");
			}
			else {
				out.print("text");
			}
			if (size != 0) {
				out.print(" size=" + getSize());
			}
			// String theStyle = StringUtils.trimToEmpty(getStyle());
			if (getConverter() instanceof NumberConverter || getConverter() instanceof NumberConverterPrecise) {
				// theStyle += "text-align: right;";
				// should check if align is already used??
				this.getStyle().addTextAlign(Style.TEXT_ALIGN_RIGHT);
			}
			if (maxlength != 0) {
				out.print(" maxlength=" + getMaxlength());
			}
			// will be rendered with a definite state on the beginning.
			String content = JSUtil.wr2(getContext().processValue(getId()));
			out.print(" value=" + content);

			// action listener
			writeOnActionCommands(out, JSUtil.ACTION_ON_RETURN);

			// theStyle += "background: transparent;";
			this.getStyle().addStyleAttribute(new StyleAttribute("background", "transparent"));
			// setStyle(theStyle);
			// eating the read only settings
			ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
			boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
			out.print(JSUtil.atBool("readonly", ro));

			addCssClass("wgtTableInputText");
			if (hasDatePicker())
				addCssClass("wgtInputTextWithButton");

			if (ro) {
				addCssClass("wgtReadonly");
			}
			out.print(JSUtil.onAction("onfocus", "onRowIn(this);"));
			out.print(JSUtil.onAction("onblur", "onRowOut(this);"));
			out.print(" onkeydown=\"if (event.keyCode==9){onTab(this)}else if(event.keyCode==38){onArrowUp(this)}else if(event.keyCode==40){onArrowDown(this)};\" ");
			// generic properties from action...

			stdParameter(out);

			out.print(">");
			// print calendar button
			if (hasDatePicker())
				out.print(buttonHTML());

			// shall we load the calendar libraries?
			loadCalendar(out);
		}

		public void setVisible(boolean vis) {
			getContext().setVisible(getId(), vis);
		}
	}

}
