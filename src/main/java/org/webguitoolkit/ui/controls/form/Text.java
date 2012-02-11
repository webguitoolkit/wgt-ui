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
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.DateTimeConverter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverterPrecise;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.conversion.IDatePickerConverter;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.http.ResourceServlet;
import org.webguitoolkit.ui.http.resourceprocessor.JSResourceProcessor;

/**
 * <pre>
 * The text control is like the input type text in html.
 * 
 * It can be displayed as calendar by setting the format to datatime or date
 * 
 * By setting the password flag the characters of the text are displayed as *
 * </pre>
 * 
 * © by endress + hauser infoserve 2007
 */
public class Text extends FormControl implements IText {

	public static final String DOT_BUTTON = "_button";
	// size of the input box on the screen
	protected int size;
	// maxlength of the input
	protected int maxlength = 0;
	// is this control a password control?
	protected boolean password = false;
	// is this text editable at all? non-editable renders as span
	protected boolean editable = true;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public Text() {
		super();
		setDefaultCssClass("wgtInputText");
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Text(String id) {
		super(id);
		setDefaultCssClass("wgtInputText");
	}

	public void setValue(String dataForText) {
		if (isEditable()) {
			super.setValue(dataForText);
			// same as
			// getBody().getContext().add(getId(), dataForText, Context.TYPE_VAL, Context.STATUS_EDITABLE);
		}
		else {
			// send as txt and not editable
			if (getMaxlength() != 0) {
				// restrict output to maxlen?
				dataForText = StringUtils.substring(dataForText, 0, getMaxlength() - 1);
			}
			getContext().add(getId(), dataForText, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		}
	}

	/**
	 * @param dataObject object to be saved to
	 */
	public void saveTo(Object dataObject) {
		if (isEditable()) {
			super.saveTo(dataObject);
		}
	}

	protected void init() {
		// if nothing is loaded from the model in the init phase, then the javascript will
		// not recognise the text field, assomething where it should gather the data from
		// and send it to the serve, hence we must ensure, that the JS is informed about the
		// editable field.
		if (isEditable())
			getContext().add(getId(), "", IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
	}

	protected void endHTML(PrintWriter out) {
		// If the field is not editable at all render as span
		if (isEditable() && hasExecutePermission())
			inputFieldHTML(out);
		else if (hasReadPermission())
			nonEditableHTML(out);
		else
			out.println("&nbsp;");
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
		// TODO FIXME_MH berechnung von width (-18, wenn Datepicker da ist) ???
		if (conv instanceof NumberConverter || conv instanceof NumberConverterPrecise) {
			// theStyle += "text-align: right;";
			// should check if align is already used??
			this.getStyle().addTextAlign(Style.TEXT_ALIGN_RIGHT);
		}
		if (maxlength != 0) {
			out.print(" maxlength=" + getMaxlength());
		}
		// will be rendered with a definite state on the beginning.
		out.print(" value=" + JSUtil.wr2(StringEscapeUtils.escapeHtml(getContext().processValue(getId()))));

		// action listener
		writeOnActionCommands(out, JSUtil.ACTION_ON_RETURN);

		// setStyle(theStyle);
		// eating the read only settings
		ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
		boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
		out.print(JSUtil.atBool("readonly", ro));

		// TODO-CSS trennen default css und date picker css

		if (hasDatePicker()) {
			addCssClass("wgtInputTextWithButton");
		}

		if (ro) {
			addCssClass("wgtReadonly");
		}

		stdParameter(out);

		// experimental
		// if( getValidators()!=null && getValidators().size()>0 ){
		// String tooltip = "";
		// for (Iterator iter = getValidators().iterator(); iter.hasNext();) {
		// IValidator validator = (IValidator) iter.next();
		// tooltip += validator.getTooltip()+"&#10;";
		// }
		// out.print(" title=\""+tooltip+"\" ");
		// }

		// add tabindex to html element if the tabindex is greater than 0
		if (tabindex >= 0) {
			out.print(" " + TABINDEX + "=\"" + Integer.valueOf(tabindex).toString() + "\" ");
		}

		out.print(">");
		// print calendar button
		if (hasDatePicker())
			out.print(buttonHTML());

		// shall we load the calendar libraries?
		loadCalendar(out);
	}

	protected void nonEditableHTML(PrintWriter out) {
		// for non editable display mode we don't want the default css
		setCssClass("");
		// eat readonly context element as this is not editable anyway...
		getContext().processContextElement(getId() + DOT_RO);
		out.print("<span ");

		// action listener
		writeOnActionCommands(out, JSUtil.ACTION_ON_RETURN);// TODO change default action

		// String theStyle = getStyle()==null ? "" : getStyle();
		// check for number right align
		if (conv instanceof NumberConverter || conv instanceof NumberConverterPrecise) {
			// theStyle += "text-align: right;";
			this.getStyle().addTextAlign(Style.TEXT_ALIGN_RIGHT);
		}
		// setStyle(theStyle);
		// generic properties from action...
		stdParameter(out);
		out.print(">" + getContext().processValue(getId()) + "</span>");
	}

	/**
	 * @param out
	 */
	protected void loadCalendar(PrintWriter out) {
		boolean calLib = hasDatePicker();

		/*
		 * lets get the calendar started
		 * 
		 * <script type="text/javascript"> Calendar.setup( { inputField : "data", // ID of the input field ifFormat :
		 * "%m %d, %Y", // the date format button : "trigger" // ID of the button } ); </script>
		 */
		if (conv instanceof DateTimeConverter) {
			if (is24HourMode()) {
				getContext().sendJavaScript(
						getId() + "_init",
						"Calendar.setup(" + "{" + "inputField  : '" + getId() + "',   " + "button      : '" + getId() + DOT_BUTTON + "', "
								+ "ifFormat    : " + getDateFormatforCalendar(TextService.getLocale()) + ",   "
								+ "showsTime      :    true," + "timeFormat     :    '24'" + "}" + ");");
			}
			else {
				getContext().sendJavaScript(
						getId() + "_init",
						"Calendar.setup(" + "{" + "inputField  : '" + getId() + "',   " + "button      : '" + getId() + DOT_BUTTON + "', "
								+ "ifFormat    : " + getDateFormatforCalendar(TextService.getLocale()) + ",   "
								+ "showsTime      :    true," + "timeFormat     :    '12'" + "}" + ");");
			}
		}
		else if (calLib) {
			getContext().sendJavaScript(
					getId() + "_init",
					"Calendar.setup(" + "{" + "inputField  : '" + getId() + "',   " + "button      : '" + getId() + DOT_BUTTON + "', "
							+ "ifFormat    : " + getDateFormatforCalendar(TextService.getLocale()) + " " + "}" + ");");
		}
	}

	protected String buttonHTML() {
		IContext ctx = getContext();
		String idVis = getId() + DOT_BUTTON + IContext.DOT_VIS;
		String stringVis = "";
		if( !ctx.processBool(idVis) ){
			stringVis = "display: none;";
		}
//		ctx.add(buttonVis, "true", IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);
		return "<img border=\"0\" src=\"images/wgt/calendar.gif\" class=\"wgtPointerCursor\" " + JSUtil.atId(getId() + DOT_BUTTON)
				+ "style=\"vertical-align:middle; " + stringVis + "\"> ";
	}

	public int getMaxlength() {
		return maxlength;
	}

	public boolean isPassword() {
		return password;
	}

	public int getSize() {
		return size;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void changeMode(int mode) {
		super.changeMode(mode);
		// also switch the visibility of the button for the calenndar
		IContext ctx = getContext();
		if (hasDatePicker() && isEditable()) {
			// is there a button at all?
			// do we want ot participate in the mode change?
			if (mode == Compound.MODE_EDIT && isFinal()) {
				return; // no, good bye
			}
			String imageName = "images/wgt/calendar_disabled.gif";
			if (mode != Compound.MODE_READONLY)
				imageName = "images/wgt/calendar.gif";
			ctx.add(getId() + DOT_BUTTON + ".src", imageName, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
			// ctx.add(getId()+ DOT_BUTTON+Context.DOT_VIS, Boolean.toString(mode!=Compound.MODE_READONLY),
			// Context.TYPE_VIS, Context.STATUS_NOT_EDITABLE);
		}
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		getContext().add(getId(), "", IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
	}

	private String getDateFormatforCalendar(Locale locale) {
		return "'" + ((IDatePickerConverter)conv).getDatePickerPattern() + "'";
	}

	public boolean hasDatePicker() {
		return conv instanceof IDatePickerConverter;
	}

	public void setConverter(IConverter conv) {
		super.setConverter(conv);
		if (conv instanceof IDatePickerConverter) {
			getPage().addHeaderCSS("./" + ResourceServlet.SERVLET_URL_PATTERN + "/eh/calendar.css");
			getPage().addWgtJS( JSResourceProcessor.CALENDAR_JS + "_" + TextService.getLocale().getLanguage() + ".js");
		}
	}

	/**
	 * This method determines the time mode.<br>
	 * It is checked if a 24 or a 12 (am/pm) hour mode is used by the locale
	 * 
	 * @return true if the hour mode is 24
	 */
	private boolean is24HourMode() {
		// A bit a dirty way to check the hour mode...a Date is converted an checked...
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TextService.getLocale());
		String testDate = df.format(new Date());
		if (StringUtils.containsIgnoreCase(testDate, "am") || StringUtils.containsIgnoreCase(testDate, "pm")) {
			return false;
		}
		else {
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.IText#setHTMLName(java.lang.String)
	 */
	public void setHTMLName(String name) {
		setAttribute("name", name);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.IText#mark()
	 */
	public void mark() {
        getContext().add(getId()+".selecta", "", IContext.TYPE_FOCUS, IContext.STATUS_NOT_EDITABLE);    	
        getContext().sendJavaScriptState(getId()+".selectall", "byId('"+getId()+"').select();");
	}

	@Override
	public void setVisible(boolean vis) {
		if( hasDatePicker() )
			getContext().makeVisible(getId()+DOT_BUTTON, vis);
		super.setVisible(vis);
	}
}
