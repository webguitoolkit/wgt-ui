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
import org.apache.ecs.html.Input;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.CheckBox;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * <pre>
 * renders a simple checkbox to a table cell
 * </pre>
 * 
 * @author martin hermann
 * 
 */

public class CheckboxColumnRenderer extends ChildColumnRenderer {

	private String checkBoxId;
	private IActionListener cbActionListener = null;

	public CheckboxColumnRenderer() {
		super();
	}

	public CheckboxColumnRenderer(IActionListener addActionListener) {
		super();
		this.cbActionListener = addActionListener;
	}

	public CheckboxColumnRenderer(String newCheckBoxId) {
		setCheckBoxId(newCheckBoxId);
	}

	/**
	 * @deprecated
	 */
	public CheckboxColumnRenderer(ITableColumn col) {
		super();
	}

	protected FormControl[] getFormControls() {
		TableCheckBox cb = null;
		if (StringUtils.isEmpty(getCheckBoxId())) {
			cb = new TableCheckBox();
		}
		else {
			cb = new TableCheckBox(getCheckBoxId());
		}
		cb.setParent(table);
		cb.setProperty(tableColumn.getProperty());
		cb.setCompound(compound);

		if (cbActionListener != null) {
			cb.setActionListener(cbActionListener);
		}

		if (tableColumn.getIsDisplayed()) {
			compound.addElement(cb);
		}
		return new FormControl[] { cb };
	}

	public CheckBox getCheckBox() {
		return (CheckBox)myControls[0];
	}

	/**
	 * @return the checkBoxId
	 */
	public String getCheckBoxId() {
		return checkBoxId;
	}

	/**
	 * @param newCheckBoxId the checkBoxId to set
	 */
	public void setCheckBoxId(String newCheckBoxId) {
		checkBoxId = newCheckBoxId;
	}

	public class TableCheckBox extends CheckBox {

		public TableCheckBox() {
			super();
			setCssClass("wgtInputCheckbox");
		}

		public TableCheckBox(String id) {
			super(id);
			setCssClass("wgtInputCheckbox");
		}

		/**
		 * generating html for checkbox
		 */
		protected void endHTML(PrintWriter out) {
			Input input = new Input();
			input.setType("checkbox");
			stdParameter(input);
			if (hasActionListener()) {
				input.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + ";event.cancelBubble=true;");
			}
			else {
				input.setOnClick("event.cancelBubble=true;");
			}
			
			input.setOnFocus("onRowIn(this);");
			input.setOnBlur("onRowOut(this);");
			input.setOnKeyDown("if (event.keyCode==9){onTab(this)}else if(event.keyCode==38){onArrowUp(this)}else if(event.keyCode==40){onArrowDown(this)};");

			input.setChecked(getContext().processBool(getId()));
			// eating the read only settings
			ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
			boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
			input.setDisabled(ro);
			input.output(out);
		}
	}
}
