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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.Element;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * <pre>
 * The select field like the HTML select, 
 * it has a select Model which provides the key value pairs.&lt;br&gt;
 * </pre>
 * 
 */
public class Select extends FormControl implements ISelect {

	public static final String DOT_BUTTON = ".button";
	public static final String DOT_LOAD_LIST = ".loadList";
	private static final String DOT_TEXTVIEW_RO = ".tro";
	public static final String EMPTY = "";
	// selectionModel
	protected ISelectModel model;
	// default entry for nothing selected
	// if it is null no entry for 'unselected' will be displayed
	protected String prompt;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public Select() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Select(String id) {
		super(id);
		setCssClass("wgtComboTable wgtComboText");
	}

	@Override
	protected void endHTML(PrintWriter out) {
		// using ComboBox JavaScript
		internalGenHtml(out);
	}

	public List createHtmlElements() {
		// TODO FIXME_MH breite bzw. stype weiterreichen , rausrendern.
		List elements = new ArrayList();
		ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
		boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
		String imgSrc = "./images/wgt/select_arrow.gif";
		ContextElement ceImg = getContext().processContextElement(getId() + DOT_BUTTON + ".src");
		if (ceImg != null && StringUtils.isNotEmpty(ce.getValue()))
			imgSrc = ceImg.getValue();

		if (ro) {
			if (StringUtils.isEmpty(getCssClass())) {
				setDefaultCssClass("wgtComboText wgtReadonly");
			}
			else {
				addCssClass("wgtReadonly");
			}
		}

		Table table = new Table();
		table.setCellSpacing(0);
		table.setCellPadding(0);
		table.setClass("wgtComboTable");
		table.setID(getId() + "_table");

		TR tr = new TR();
		table.addElement(tr);

		TD td = new TD();
		tr.addElement(td);

		// text field
		Input input = new Input();
		input.setType("text");
		input.setReadOnly(ro);
		input.setID(getId());
		input.setClass(StringUtils.isEmpty(getCssClass()) ? "wgtComboText" : getCssClass());

		// add tabindex to html element if the tabindex is greater than 0
		if (tabindex >= 0) {
			input.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
		}

		if (this.hasStyle()) {
			input.setStyle(this.getStyle().getOutput());
		}

		td.addElement(input);

		td = new TD();
		tr.addElement(td);

		// button
		// MH changed to image because IE executed click event of first select when pressing return
		IMG image = new IMG();
		image.setSrc(imgSrc);
		image.setID(getId() + DOT_BUTTON);
		if (getTooltip() != null)
			input.setTitle(getTooltip());
		td.addElement(image);
		elements.add(table);

		// hidden result
		input = new Input();
		input.setType("hidden");
		input.setValue("");
		input.setID(getId() + "_hidden");
		elements.add(input);

		createSelect();

		return elements;
	}

	public void internalGenHtml(PrintWriter out) {
		List elements = createHtmlElements();
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			Element element = (Element)iter.next();
			element.output(out);
		}
	}

	/**
	 * tell the component to load the list into the client.
	 * 
	 */
	public void loadList() {
		Collection<String[]> options = getModel().getOptions();
		if (options == null)
			return;
		loadListInternally(options);

	}

	public void loadList(Collection options) {
		DefaultSelectModel newModel = new DefaultSelectModel();
		newModel.setOptions(options);
		setModel(newModel);
		loadListInternally(options);
	}

	private void loadListInternally(Collection<String[]> options) {
		IContext context = getContext();
		if (options == null)
			return;
		// try to preserve the old value in the SelectBox
		String oldvalue = getValue();
		boolean oldFound = false;
		// the JS to clear the SelectBox

		StringBuffer optList = new StringBuffer("[");
		if (getPrompt() != null) { // add a prompt option?
			String prompt = getPrompt();
			if (StringUtils.isEmpty(prompt))
				prompt = "";
			optList.append("{ option: " + JSUtil.wrApoEsc(prompt) + " , value: '' }");
			if (options.size() > 0) {
				optList.append(",");
			}
		}
		for (Iterator<String[]> it = options.iterator(); it.hasNext();) {
			String[] opt = (String[])it.next();
			String label = opt[1];
			// TODO handle ' right, it's weird
			if (label != null) {
				label = label.replaceAll("'", "‘");
			}
			optList.append("{ option: " + JSUtil.wrApoEsc(label) + ", value: " + JSUtil.wrApoEsc(opt[0]) + "}");
			oldFound |= StringUtils.equals(opt[0], oldvalue);
			if (!it.hasNext())
				break;
			optList.append(",");
		}
		optList.append("]");

		// send option list to ComboBox
		context.add(getId() + DOT_LOAD_LIST, optList.toString(), IContext.TYPE_COMBO_LOAD, IContext.STATUS_IDEM);

		if (!oldFound || oldvalue == null) {
			// Work around, if the value was the same before we have set a other value before so that the context element is not
			// removed by optimization
			getContext().removeContextElement(valueName());
			if (getPrompt() != null || options.size() == 0) {
				setValue(EMPTY);
			}
			else { // select first entry is best guess if no prompt
				setValue(((String[])options.iterator().next())[0]);
			}
		}
		else {
			getContext().removeContextElement(valueName());
			setValue(oldvalue);
		}
	}

	/**
	 * sets the value of the select box for Associaton select use the selectAssociation() function
	 * 
	 * to get the prompt selected call setValue("")
	 */
	@Override
	public void setValue(String value) {
		getContext().add(valueName(), value, IContext.TYPE_CBV, IContext.STATUS_EDITABLE);
		getContext().moveDown(valueName());
	}

	/**
	 * CALL this method only if you are using IAssociatedModel you can set the selection of the SelectBox to the entry which is
	 * representation for the Object given (out of the list in the Model.
	 * 
	 * @param o
	 */
	public void setValueAsObject(Object o) {
		if (getConverter() != null) {
			setValue((String)getConverter().convert(String.class, o));
		}
		else if (o instanceof String) {
			setValue((String)o);
		}
		else
			setValue(EMPTY);
	}

	/**
	 * return the mode of this select list. Usually this is an DefaultSelectMode or AssociationSelectModel. There are various
	 * methods witch actually create the model for you. There is little use to call this method from an application.
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

	public void setModel(ISelectModel model) {
		this.model = model;
	}

	/**
	 * the name of the context variable with the value in it.
	 * 
	 * @return
	 */
	private String valueName() {
		return getId() + ".value";
	}

	@Override
	public String getValue() {
		return getContext().getValue(valueName());
	}

	/**
	 * CALL this method only if you are using IAssociationModel It will return the associated Object which the user selected.
	 * 
	 * @deprecated use getConvertedValue() instead
	 * @return
	 */
	@Deprecated
	public Object getValueAsObject() {
		if (getConverter() != null) {
			try {
				return getConverter().parse(getValue());
			}
			catch (ConversionException e) {
				throw new WGTException(e);
			}
		}
		return getValue();
	}

	/**
	 * create the select box, similar to outputting the html in other components
	 */
	protected void createSelect() {
		IContext ctx = getContext();

		String values = "";
		ContextElement ce = ctx.processContextElement(getId() + DOT_LOAD_LIST);
		if (ce != null)
			values = ce.getValue();

		if (StringUtils.isBlank(values)) {
			values = "[]";
		}
		String initParams = "{ changeAction:" + (hasActionListener()) + ", opt: " + values + " }";

		ctx.add(new ContextElement(getId(), initParams, IContext.TYPE_CBI, IContext.STATUS_NOT_EDITABLE));

		String js = JSUtil.jQuery(getId()) + ".readOnly = 'readonly';";// we are a selectbox no combo
		ctx.sendJavaScriptState(getId() + DOT_TEXTVIEW_RO, js);
		if (StringUtils.isNotEmpty(ctx.getValue(valueName())))
			ctx.moveDown(valueName());
		else
			setValue("");
	}

	@Override
	protected void init() {
		// load list may be called already from external
		if (getValue() == null) {
			loadList();
		}
		getPage().addHeaderJS(ResourceServlet.SERVLET_URL_PATTERN + "/select.js");
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public void setPromptKey(String promptKey) {
		this.prompt = TextService.getString(promptKey);
	}

	/**
	 * loads an association. The collection with the Objects of the association must be provided. We will load the select with the
	 * names of the objects in the collection.
	 * 
	 * @param asso
	 */
	public void loadAssociation(Collection asso) {
		loadAssociation(asso, false);
	}

	/**
	 * loads an association. The collection with the Objects of the association must be provided. We will load the select with the
	 * names of the objects in the collection. Additional boolean parameter to sort model.
	 * 
	 * @param asso
	 * @param sort
	 */
	public void loadAssociation(Collection asso, boolean sort) {
		if (!(getModel() instanceof AssociationSelectModel)) {
			setModel(new AssociationSelectModel(sort));
		}
		((AssociationSelectModel)getModel()).setOptions(asso);
		loadList();
	}

	/**
	 * empties the list, model must be default.
	 * 
	 * @deprecated set new DefaultSelectModel instead and call loadList() afterwards
	 * 
	 */
	@Deprecated
	public void clearDefaultModel() {
		((DefaultSelectModel)getModel()).setOptions(new ArrayList(0));
		loadList();
	}

	public DefaultSelectModel getDefaultModel() {
		return (DefaultSelectModel)getModel();
	}

	@Override
	public void changeMode(int mode) {
		super.changeMode(mode);
		// also switch the visibility of the button for the calendar
		IContext ctx = getContext();
		// do we want to participate in the mode change?
		if (mode == Compound.MODE_EDIT && isFinal()) {
			return; // no, good bye
		}
		String imageName = "images/wgt/select_arrow_disabled.gif";
		if (mode != Compound.MODE_READONLY)
			imageName = "images/wgt/select_arrow.gif";
		ctx.add(getId() + DOT_BUTTON + ".src", imageName, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
	}

	@Override
	public void setVisible(boolean vis) {
		getContext().setVisible(getId() + "_table", vis);
	}

	@Override
	public boolean isVisible() { // visibility is true unless set to false
		return getContext().getValueAsBool(getId() + "_table" + IContext.DOT_VIS, true);
	}

	/**
	 * sort the options by value, i.e. second String-Value of options-Array. Works only when called before loadList(). Works for
	 * all Selects with a model of type DefaultSelectModel.
	 */
	public void sortForDefaultModel() {
		getDefaultModel().sort();
	}

	/**
	 * if there is a converter at the control, return the controls converter otherwise return the models converter (e.g. for
	 * association select model)
	 */
	@Override
	public IConverter getConverter() {
		IConverter converter = super.getConverter();
		if (converter == null && getModel() != null)
			return getModel().getConverter();
		return converter;
	}

	/**
	 * overwrites base control, because the SelectBox uses a different element id then the other GUI controls
	 * 
	 * @see org.webguitoolkit.ui.controls.BaseControl#setFancyTooltip(java.lang.String)
	 */
	@Override
	protected void draw(PrintWriter out) {
		super.draw(out);
		if (getTooltipAdvanced() != null) {
			String js = "$(" + JSUtil.jQuery(getId() + DOT_BUTTON) + ").tooltip(" + getTooltipAdvanced().getJQueryParameter() + ");";
			getContext().sendJavaScript(getId(), js);
		}
	}

	@Override
	public void removeInternal() {
		// MH fixed memory leaks! 19.11.2008
		getContext().removeNode(getId() + "_result");
		super.removeInternal();
	}
}
