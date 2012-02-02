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
/*
 */
package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * <pre>
 * a radio button can be assigned to a group.
 * a group is just a string constant. Radio buttons within a 
 * group act as a 1 out of many choice.
 * </pre>
 * @author arno
 */
public class Radio extends OptionControl implements IRadio {

	// indicates it the event bubbles upwards in the DOM tree
	private boolean eventBubble = true;

	/**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
    public Radio() {
        super();
    }
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
     * @param id unique HTML id
     */
    public Radio(String id) {
		super(id);
	}
    
	// the group this radio button belongs to, may be null
    protected String group;
    
    protected void endHTML(PrintWriter out) {
        // a radio button can have a label in front of it.
    	String theName = "";
    	String fromContext = getContext().processValue(getId() + "_ss.name");
    	if( StringUtils.isNotEmpty(fromContext) )
    		theName = "name=\""+fromContext+"\"";
		out.print("<span id=\"" + getId() + "_ss" + "\" "+theName+">");
        out.print("<input type=radio ");
        if( StringUtils.isNotBlank(getLabel()) )
        	getStyle().add("float", "left");
        setDefaultCssClass("wgtInputRadio");
        stdParameter(out);
        if (StringUtils.isNotEmpty(getGroup())) {
            out.print(" name=\""+group+ "\" ");
        }
        if (hasActionListener()) {
        	if( eventBubble )
        		out.print(JSUtil.onClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)));
        	else
        		out.print(JSUtil.onClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)+"stopEvent(event);" ));
        }
        if (hasActionListener()) {
        }
        out.print(JSUtil.atBool("checked", getContext().processBool(getId())));
        // take care of mode (read-only editable
        // eating the read only settings
        ContextElement ce = getContext().processContextElement( getId() + DOT_RO);
        boolean ro = ce != null && IContext.TYPE_READONLY.equals( ce.getType() );
        out.print(JSUtil.atBool("disabled", ro));
		
        //add tabindex to html element if the tabindex is greater than 0
        if(tabindex >= 0){
        	out.print(" "+TABINDEX+"=\""+Integer.valueOf(tabindex).toString()+ "\" ");
        }
        
        out.print(">");
        out.print("</span>");
        
        makeLabel( out );
    }
    
	public void clearError() {
		if (getContext().getValue(getId() + "_ss.addClass") != null && getContext().getValue(getId() + "_ss.addClass").equals(ERROR_STYLE_CLASS))
			getContext().removeClass(getId(), getContext().processValue(getId() + "_ss.addClass"));
	}
	public void showError() {
		getContext().addClass(getId()+"_ss", FormControl.ERROR_STYLE_CLASS);					
	}

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

	public void setName(String name) {
		getContext().setAttribute( getId() + "_ss", "name", name);
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

}
