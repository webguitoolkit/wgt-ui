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

package org.webguitoolkit.ui.controls.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WGTException;

/**
 * Javascript and HTML Utility class
 */
public final class JSUtil {
	
	public static final String ACTION_ON_KEYDOWN = "onKeydown";
	public static final String ACTION_ON_RETURN = "onReturn";
	public static final String ACTION_ON_MOUSEOVER = "onMouseover";
	public static final String ACTION_ON_MOUSEOUT = "onMouseout";
	public static final String ACTION_ON_CHANGE = "onChange";
	public static final String ACTION_ON_CLICK = "onClick";
	public static final String ACTION_ON_KEYUP = "onKeyup";
	public static final String ACTION_ON_FOCUS = "onFocus";
	public static final String ACTION_ON_BLUR = "onBlur";
	public static final String ACTION_ON_DOUBLECLICK = "ondblclick";
	 
	/**
	 *  don't create instances of this utility class
	 */
	private JSUtil(){}
	
	/**
	 * returns the javascript fragment to fire a AJAX event
	 * @param cssId the id of the control that handles the event
	 * @param eventName the event name is evaluated by the component if there are more then one event that can be handled by the component
	 * @return a javascript command that calls the AJAX framework
	 */
    public static String jsFireEvent(String cssId, String eventName) {
        return "fireWGTEvent('"+cssId+"','"+eventName+"');";
    }
    
	/**
	 * returns the javascript fragment to fire a AJAX event
	 * @param cssId the id of the control that handles the event
	 * @param eventnr the event number is evaluated by the component if there are more then one event that can be handled by the component
	 * @return a javascript command that calls the AJAX framework
	 */
    public static String jsFireEvent(String cssId, int eventnr) {
        return "fireWGTEvent('"+cssId+"','"+eventnr+"');";
    }
    
	/**
	 * returns the javascript fragment to fire a AJAX event
	 * @param cssId the id of the control that handles the event
	 * @param eventnr the event number is evaluated by the component if there are more then one event that can be handled by the component
	 * @param action additional parameter send to the backend
	 * @return a javascript command that calls the AJAX framework
	 */
    public static String jsFireEvent(String cssId, int eventnr, String action ) {
        return JSUtil.jsEventParam( cssId, new String[]{action}) + "fireWGTEvent('"+cssId+"','"+eventnr+"');";
    }
    
    /**
     * add <i>javascript:</i> and wrap with double quote
     * @param wrap the javascript code to be executed
     * @return a String that looks like <i>"javascript:[wrap]"</i>
     */
    public static String wrJSColon(String wrap) {
        return "\"javascript:" + wrap +"\"";
    }
    /**
     * wrap with single quote and spaces
     * @param wrap the string to be wrapped
     * @return wrapped string
     */
    public static String wrApo(String wrap) {
        return " '" + wrap +"' ";
    }
    /**
     * wrap with single quote and escape for javascript
     * @param wrap the string to be wrapped
     * @return wrapped string
     */
    public static String wrApoEsc(String wrap) {
        return " '" + StringEscapeUtils.escapeJavaScript(wrap) +"' ";
    }
    /**
     * wrap with double quote
     * @param wrap the string to be wrapped
     * @return wrapped string
     */
    public static String wr2(String wrap) {
        return "\""+ wrap + "\"";
    }
    /**
     * calls the highlight function that adds a highlight style to the node with the given id
     * @param lightId the id of the dom node to be highlighted
     * @return the javascript code
     */
    public static String jsHighlite(String lightId ) {
        return "highlite(this,'"+lightId+"');";
    }

    /**
     * generates the HTML string for the id attribute
     * @param id the id
     * @return the HTML code fragment: <i> id="[id]" </i>
     */
    public static String atId(String id) {
    	return " id="+wr2(id)+" ";
    }
    
    /**
     * generates the HTML string for the a attribute if not empty
     * @param atName the attributes name
     * @param atValue the attributes value
     * @return the HTML code fragment: <i> [atName]="[atValue]" </i> or empty string
     */
    public static String atNotEmpty(String atName, String atValue) {
        if (StringUtils.isEmpty(atValue)) {
            return "";
        } else {
            return " "+atName+ "="+wr2(atValue)+" ";
        }
    }
    
    /**
     * generates the HTML string for the a attribute with default value
     * @param atName the attributes name
     * @param atValue the attributes value
     * @param def the default attributes value
     * @return the HTML code fragment: <i> [atName]="[atValue]" </i> or if atValue is empty <i> [atName]="[def]" </i>
     */
   public static String at(String atName, String atValue, String def) {
        if (StringUtils.isEmpty(atValue)) {
             atValue = def;
        } 
        return atNotEmpty(atName,atValue);
        
    }
    /**
     * the attribute is a boolean in html-spec
     * and must not be present if false, or readonly="readonly" 
     * if true.
     * @param atName the attribute's name
     * @param val boolean value
     * @return the created string
     */
    public static String atBool(String atName, boolean val) {
    	if( val )
    		return " " + atName + " ";
    	else
    		return " ";
    }
    /**
     * this produces javascript to associate the event which is send next
     * to have parameter values, as given in the stingarray.
     * After dispatching the event, the parameters can be accessed
     * via event.getParameter(i)
     * DONT forget to place the eventParam IN FRONT OF the fire
     * 
     * @param id the id of the control
     * @param parameters the parameters to be send with the next fireEvent call
     * @return the generated string
     */
    public static String jsEventParam(String id, String[] parameters) {
        if (parameters==null) throw new WGTException("Invalid parameter null");
        StringBuffer ep = new StringBuffer("eventParam('"+id+"', new Array(");
        for (int i=0;i<parameters.length;i++) {
            if (i>0) ep.append(',');
            ep.append('\'');
            ep.append(parameters[i]);
            ep.append('\'');
        }
        ep.append("));");
        return ep.toString();
    }
    /**
     * return an event handler for executing an event if an input element is pressed return.
     * @see jsFireOnReturn
     * @param js the JavaScript Statement to be executed when return key is pressed
     * @return the generated on.... HTML attribute for the action
     */
    public static String onReturn(String js) {
        if (StringUtils.isBlank(js)) return "";
        return " onkeydown=\"if (event.keyCode==13) {"+js +" return false;}; \" ";
    }
    /**
     * builds a HTML string for a key down event for the specified key code
     * @param keyCode key code of the pressed key for the code to be executed
     * @param js javascript code to execute if key is pressed
     * @return the HTML code fragment: <i>  onkeydown="if ( event.keyCode==[keyCode] ) {[js]}; " </i>
     */
    public static String onKeyCode( String keyCode, String js ) {
        if (StringUtils.isBlank(js)) return "";
        return " onkeydown=\"if (event.keyCode=="+keyCode+") {"+js +"}; \" ";
    }
    /**
     * builds a HTML string for a on change event
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> onchange="[js]" </i>
     */
    public static String onChange(String js) {
    	return onAction( ACTION_ON_CHANGE, js );
    }
    /**
     * builds a HTML string for a on click event
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> onclick="[js]" </i>
     */
    public static String onClick(String js) {
    	return onAction( ACTION_ON_CLICK, js );
    }
    /**
     * builds a HTML string for a on key down event
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> onkeydown="[js]" </i>
     */
    public static String onKeydown(String js) {
    	return onAction( ACTION_ON_KEYDOWN, js );
    }
    /**
     * builds a HTML string for a on key up event
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> onkeyup="[js]" </i>
     */
    public static String onKeyup(String js) {
    	return onAction( ACTION_ON_KEYUP, js );
    }
    /**
     * builds a HTML string for a on mouse over event
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> onmouseover="[js]" </i>
     */
    public static String onMouseover(String js) {
    	return onAction( ACTION_ON_MOUSEOVER, js );
    }
    /**
     * builds a HTML string for a on mouse out event
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> onmouseout="[js]" </i>
     */
    public static String onMouseout(String js) {
    	return onAction( ACTION_ON_MOUSEOUT, js );
    }

    /**
     * builds a HTML string for a event, for the special event on return the key code 13 is evaluated
     * @param action the event e.g. onsubmit, ...
     * @param js javascript code to execute
     * @return the HTML code fragment: <i> [action]="[js]" </i>
     */
    public static String onAction( String action, String js ) {
        if (StringUtils.isBlank(js)) return "";
        if ( ACTION_ON_RETURN.equals( action ) )
            return " onkeydown=\"if (event.keyCode==13) {"+js +" stopEvent(event); return false;}; \" ";
        else
        	return " " + action + "=\"" + js +"\" ";        
    }

    
    /**
     * returns a JavaScript Statement to be used in a onKeydown event.
     * executes the passed in JavaScript statement if return is pressed.
     * should be the first statement in the onKexdown handler.
     * @param js the JavaScript that is executed on return
     * @return the JavaScript code with evaluation of the key event
     */
    public static String jsReturn(String js) {
        return "if (event.keyCode==13) {"+js +" return false;}; ";       
    }
    /**
     * creates a javascript confirm dialog ansking the given string. the second parameter
     * is the javascript statement getting executed iff the question was confirmed by the user.
     * The confirm dialog will only be presented iff the question is not null or empty.
     * @param confirmedStmt the JavaScript that is executed on confirmation = true
     * @param question the question that is ask
     * @return the JavaScript code with the confirmation question
     */
    public static String jsConfirm(String question, String confirmedStmt) {
    	if (StringUtils.isNotEmpty(question)) {
    		return "wgtQ = confirm( "+ wrApoEsc(question)+" );if (wgtQ) {" + confirmedStmt +"} else {return false;}";
    	} else {
    		return confirmedStmt;
    	}
    }
    /**
     * prints the style display=none if the boolean is true
     * @param vis true if visible
     * @return a HTML fragment representing the style attribute
     */
    public static String atStyleDisplay( boolean vis) {
    	if( vis )
    		return "";
    	else
    		return " style='display: none;'";
    }
    /**
     * adds the style display=none to the other styles if the boolean is true
     * @param visValue 'false' if not visible
     * @param otherStyles additional style
     * @return a HTML fragment representing the style attribute
     */
    public static String atStyleVisible(String otherStyles, String visValue) {
    	boolean vis = !"false".equals(visValue);
    	if( vis )
    		return JSUtil.atNotEmpty("style", StringUtils.trimToEmpty(otherStyles));
    	else
    		return JSUtil.atNotEmpty("style", "display: none;" + StringUtils.trimToEmpty(otherStyles));
    }
    
    
    /**
     * generates code for accessing DOM nodes by id via jQuery
     * @param id the id of the DOM node to be accessed
     * @return the javascript code fragment for accessing a DOM node by id <i>jQuery('#[escaped id]')</i>
     */
    public static String jQuery( String id ){
    	return "jQuery('#" + id.replaceAll("\\.", "\\\\\\\\.") + "')";
    }
}
