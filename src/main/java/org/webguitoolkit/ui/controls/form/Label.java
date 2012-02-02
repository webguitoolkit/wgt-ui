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

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.util.TextService;


/**
 * <pre>
 * The Label is used to display text from the resource bundle.
 * 
 * The text of a label can also be loaded from a property of a object by setting the property attribute.
 * 
 * If the labelFor attribute points to a FormControl, the text is 
 * used to create a error message on input validation of the FormControl.
 * 
 * If the labelFor attribute is set the standard style of the label is "wgtLabelFor", otherwise it is "wgtLabel"
 * </pre>
 * 
 * © by endress + hauser infoserve 2007
 */
public class Label extends BaseControl implements ILabel {

	private boolean isLabelForFormControl;

	private String tooltipFromValidator;

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public Label() {
		super();
	}
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Label(String id) {
		super(id);
	}
    
    /**
     * generating the place holder for the text to be displayed
     */
	protected void endHTML(PrintWriter out) {
		Span span = new Span();
//		HTMLTag span = XHTMLTagFactory.getInstance().newSpan(null, null);
		if( isLabelForFormControl ){
			setDefaultCssClass("wgtLabelFor");
			stdParameter(span);
		}else{
			setDefaultCssClass("wgtLabel");
			stdParameter(span);
		}
		String text = getContext().processValue( getId() );
		
		span.addElement( text );
//		span.setContent(text);
		if( !StringUtils.isEmpty(tooltipFromValidator) ){
			Span marker = new Span();
//			HTMLTag marker = XHTMLTagFactory.getInstance().newSpan(span, null);
			marker.setClass( "wgtValidatorIcon" );
			marker.addElement( "*" );
//			marker.setContent("*");
			marker.setTitle( tooltipFromValidator );
			span.addElement( marker );
		}
//		String output = span.output();
		span.output( out );
	}
	/**
	 * init will send the text to be displayed to the client, can't make it earlier since the other component
	 * may not be initialised prior to this
	 */
	protected void init() {
	}
    
    /**
     * same as setValue, but translated
     */
    public void setTextKey(String textKey) {
    	setText( TextService.getString(textKey) );
    }
    
    /**
     * same as setValue
     */
    public void setText(String text) {
    	getContext().add(getId(), text, 
    			IContext.TYPE_HTML, IContext.STATUS_IDEM);
    }
    public String getText() {
		return getContext().getValue(getId());
	}

	public void setLabelForFormControl(boolean isLabelForFormControl) {
		this.isLabelForFormControl = isLabelForFormControl;
	}
	
	public String getTooltipFromValidator() {
		return tooltipFromValidator;
	}
	
	public void setTooltipFromValidator(String tooltipFromValidator) {
		this.tooltipFromValidator = tooltipFromValidator;
	}
}
