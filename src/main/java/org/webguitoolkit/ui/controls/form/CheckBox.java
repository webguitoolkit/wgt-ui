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
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Span;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;


/**
 * <pre>
 * CheckBox control:
 * 
 * // create CheckBox
 * CheckBox checkbox = factory.newCheckBox( parent, "isSelected" );
 * checkbox.setSelected( true );
 *
 * </pre>
 */
public class CheckBox extends OptionControl implements ICheckBox {

	// indicates it the event bubbles upwards in the DOM tree
	private boolean eventBubble = true;
	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
    public CheckBox() {
        super();
        setCssClass("wgtInputCheckbox");
    }
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
     * @param id unique HTML id
     */
    public CheckBox(String id) {
		super(id);
		setCssClass("wgtInputCheckbox");
	}

    /**
     * generating HTML for CheckBox
     */
    protected void endHTML(PrintWriter out) {
    	Input input = new Input();
    	input.setType( "checkbox" );

    	if( StringUtils.isNotBlank(getLabel()) )
        	getStyle().add("float", "left");
    	
    	stdParameter( input);
        if (hasActionListener()) {
        	if( eventBubble )
        		input.setOnClick( JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION ));
        	else
        		input.setOnClick( JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)+";stopEvent(event);" );
        }
        
		//add tabindex to html element if the tabindex is greater than 0
        if(tabindex >= 0){
        	input.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
        }
        
        input.setChecked( getContext().processBool(getId()) );
        // eating the read only settings
        ContextElement ce = getContext().processContextElement( getId() + DOT_RO);
        boolean ro = ce != null && IContext.TYPE_READONLY.equals( ce.getType() );
        input.setDisabled( ro );
        Span surroundigSpan = new Span();
        surroundigSpan.setID(getId()+"_ss");
        surroundigSpan.addElement(input);
        surroundigSpan.output(out);
        //input.output(out);
        makeLabel( out );
    }
    
    /**
     * set the event bubbling to false to avoid the bubbling of the event in the DOM tree,
     * useful for tables where you don't want to select the row when clicking the CheckBox
     * 
     * @param eventBubble false to stop event bubbling
     */
    public void setEventBubbling(boolean eventBubble){
    	this.eventBubble = eventBubble;
    }
    
	public void clearError() {
		if (getContext().getValue(getId() + "_ss.addClass") != null && getContext().getValue(getId() + "_ss.addClass").equals(ERROR_STYLE_CLASS))
			getContext().removeClass(getId(), getContext().processValue(getId() + "_ss.addClass"));
	}
	public void showError() {
		getContext().addClass(getId()+"_ss", FormControl.ERROR_STYLE_CLASS);					
	}

}
