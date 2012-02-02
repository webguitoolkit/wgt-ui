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
package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.LI;
import org.apache.ecs.html.UL;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * represents a HTML-select with rows>1
 * 
 * @author Benjamin Klug
 * 
 */
public class Multiselect extends FormControl implements IMultiselect {

	protected ISelectModel model;
	private static final int MULTISELECT_ENTRY_HEIGHT = 16;
	private Collection options;

	public Multiselect() {	
	}
	
	public Multiselect(String id) {
		super(id);
	}
	
	protected void endHTML(PrintWriter out) {

		Div container;
		int rows = this.getRows();
		ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
		boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
		Input input;
		container = new Div();
		container.addAttribute("wgttype", "multi");
		if (ro) {
			container.setClass("wgtMultiselectContainer_dis");
			setDefaultCssClass("wgtMultiselectContainer_dis");
		}
		else {
			container.setClass("wgtMultiselectContainer");
			setDefaultCssClass("wgtMultiselectContainer");
		}
		if (rows != Integer.MIN_VALUE && rows > 0) {
			this.getStyle().addHeight((MULTISELECT_ENTRY_HEIGHT * rows) + "px ;");
		}

		if (this.hasStyle()) {
			container.setStyle(this.getStyle().getOutput());
		}

		this.addHTMLSelectList(container);

		// hidden result
		input = new Input();
		input.setType("hidden");
		// input.setType("text");
		input.setValue("");
		input.setID(getId() + "_hidden");
		container.addElement(input);

		// createSelect();

		container.setID(this.getId());
		container.output(out);

	}

	public void setDefaultModel(DefaultSelectModel model) {
		this.model = model;
	}

	public DefaultSelectModel getDefaultModel() {
		return (DefaultSelectModel)getModel();
	}

	public void setModel(ISelectModel model) {
		this.model = model;
	}

	/**
	 * return the mode of this select list. Usually this is an DefaultSelectMode or AssociationSelectModel. There are various
	 * methods wich actually create the model for you. There is little use to call this method from an application.
	 * 
	 * @return model.
	 */
	public ISelectModel getModel() {
		if (model == null) {
			// by default use push mode
			model = new DefaultSelectModel();
		}
		return model;
	}

	/**
	 * return the state
	 */
	public boolean isReadOnly() {
		ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
		boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
		return ro;
	}

	public void setRows(int newRows) {
		getContext().add(getId() + ".rows", String.valueOf(newRows), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

	/**
	 * Use this parameter to set the maximum number of selection. Default behaviour is MultiSelect with no limitation
	 * (MAX_SELECTED_OPTIONS_UNLIMITED).
	 * 
	 * @param newCount number of
	 */

	public void setMaxSelectedOptions(int newCount) {
		getContext().add(getId() + ".mso", String.valueOf(newCount), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

	private int getRows() {
		int rows = getContext().processInt(getId() + ".rows");
		return rows;
	}

	private int getMaxSelectedOptions() {
		int mso = getContext().processInt(getId() + ".mso");
		if (mso == Integer.MIN_VALUE) {
			mso = MAX_SELECTED_OPTIONS_UNLIMITED; // default
		}
		return mso;
	}

	public void loadList(Collection newOptions) {
		options = newOptions;
		// IContext context = getContext();
		// if (options == null)
		// return;
		// // try to preserve the old value in the selectbox
		// String oldvalue = getValue();
		// boolean oldFound = false;
		// // the JS to clear the selectbox
		//
		// StringBuffer optList = new StringBuffer("[");
		// for (Iterator it = options.iterator(); it.hasNext();) {
		// String[] opt = (String[]) it.next();
		// String label = opt[1];
		// // TODO handle ' right, it's fucking weird
		// if (label != null) {
		// label = label.replaceAll("'", "‘");
		// }
		// optList.append("{ option: " + JSUtil.wrApoEsc(label) + ", value: "
		// + JSUtil.wrApoEsc(opt[0]) + "}");
		// oldFound |= StringUtils.equals(opt[0], oldvalue);
		// if (!it.hasNext())
		// break;
		// optList.append(",");
		// }
		// optList.append("]");
		//
		// // send option list to combobox
		// context.add(getId() + DOT_LOAD_LIST, optList.toString(),
		// IContext.TYPE_COMBO_LOAD, IContext.STATUS_IDEM);
		//
		// if (!oldFound || oldvalue == null) {
		// setValue(((String[]) options.iterator().next())[0]);
		// }
		if (getContext().getValue(getId() + ".value") == null) {
			setValue(StringUtils.EMPTY);
		}
	}

	protected void addHTMLSelectList(Div container) {
		int mso = this.getMaxSelectedOptions();
		ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
		if (options == null || options.isEmpty())
			return;
		boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
		UL list = new UL();
		list.setClass("wgtMultiselect_entries");
		list.setID(getId() + "_list");

		LI listElement = null;

		container.addElement(list);

		Iterator ioptions = options.iterator();
		while (ioptions.hasNext()) {
			String[] object = (String[])ioptions.next();
			listElement = new LI(object[1]);
			if (ro)
				listElement.setClass("wgtMultiselect_entry_dis");
			else
				listElement.setClass("wgtMultiselect_entry");
			listElement.setID(getId() + "_" + object[0]);
			if (hasActionListener()) {
				listElement.setOnClick("clickMultiselectEntry(this,'" + this.getId() + "'," + mso + ");"
						+ JSUtil.jsEventParam(getId(), object) + JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION));
			}
			else {
				listElement.setOnClick("javascript:clickMultiselectEntry(this,'" + this.getId() + "'," + mso + ");");
			}
			listElement.addAttribute("value", object[0]);
			list.addElement(listElement);
		}
	}

	public void setValue(String value) {
		getContext().add(getId() + ".value", value, IContext.TYPE_MULTI_SELECT_VALUE, IContext.STATUS_EDITABLE);
	}

	public void dispatch(ClientEvent event) {
		// TODO Auto-generated method stub
		if (this.isReadOnly()) {
			// no action allowed
			return;
		}
		else {
			super.dispatch(event);
		}
	}

	public String getValue() {
		return getContext().getValue(getId() + ".value");
	}

	@Override
	protected void init() {
		super.init();
		getPage().addControllerJS("multiselect.js");
	}

}
