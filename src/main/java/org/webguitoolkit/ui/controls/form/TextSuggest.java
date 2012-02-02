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
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.ISuggestListener;
import org.webguitoolkit.ui.controls.util.JSUtil;
/**
 * <pre>
 * Input field with a suggest function which is triggered when a key ist pressed.
 * 
 * uses the suggest.autocomplete.js library
 * 
 * Insert the TextSuggest element into your page and implement a new ISuggestListener (onRequestSuggenstion)  
 * where you can build the suggested values. Return them as String[]
 * </pre>
 */
public class TextSuggest extends FormControl implements ITextSuggest {

	private ISuggestListener suggestListener;
	private int startCharacters;
	private String[] data;
	private static final String DOT_TEXTVIEW_ROW = ".sug";

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public TextSuggest(){
		super();
	}
	
	public TextSuggest(int startCharacters){
		super();
		setDefaultCssClass("suggestBox");
		this.startCharacters = startCharacters;
	}	

	public TextSuggest(int startCharacters, String id){
		super(id);
		setDefaultCssClass("suggestBox");
		this.startCharacters = startCharacters;
		data = new String[] { "" };
	}
	
	public TextSuggest(String[] data) {
		super();
		setDefaultCssClass("suggestBox");
		if (data == null) {
			data = new String[] { "" };
		}
		else {
			this.data = data;
		}
	}

	
	protected void createSuggest() {
		String options = "";
		String urlOrDataString = "''";
		if(startCharacters > 0){
			options = ",{ minChars: "+startCharacters+ "}"; 
		}
		if (data != null) { 
			urlOrDataString = "[";
			for (String s : data) {
				urlOrDataString += "'" + s + "',";

			}
			urlOrDataString = urlOrDataString.substring(0, urlOrDataString.length() - 1);
			urlOrDataString += "]";
			options = "";
		} 
		getPage().getContext().sendJavaScript(getPage().getId(),
				JSUtil.jQuery(getId()) + ".autocomplete(" + urlOrDataString + options + ")");
	}
	
    protected void init() {
		// load list may be called already from external
    	getPage().addWgtCSS("standard/jquery.autocomplete.css");
		if (data != null) {
			getPage().addWgtJS("jquery/jquery.autocomplete.js");
		}else {
			getPage().addWgtJS("wgt.controller.textsuggest.js");
		}
		getContext().add(getId(), "", IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
	}

	protected void endHTML(PrintWriter out) {
        ContextElement ce = getContext().processContextElement( getId() + DOT_RO );
        boolean ro = ce != null && IContext.TYPE_READONLY.equals( ce.getType() );
		
        if (ro) {
        	if (StringUtils.isEmpty(getCssClass())) {
        		setDefaultCssClass("suggestBox wgtReadonly");
        	} else {
        		setDefaultCssClass(getCssClass() + " wgtReadonly");
        	}
        }
        
        // text field
        Input input = new Input();
        input.setType( "text" );
        input.setReadOnly( ro );
        stdParameter(input);

        input.setValue(getContext().processValue(getId()));

		if (hasActionListener())
			input.setOnKeyUp(getOnKeyUpJS());

		if (this.hasStyle()) {
			input.setStyle(this.getStyle().getOutput());
		}     
                
        if( getTooltip()!=null )
        	input.setTitle( getTooltip() );

        createSuggest();
		
        //add tabindex to html element if the tabindex is greater than 0
        if(tabindex >= 0){
        	input.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
        }
        
        input.output(out);
	}

    public void dispatch(ClientEvent event) {
		if (actionListener != null) {
			actionListener.onAction(event);
			return;
		}
    	if( !hasExecutePermission() )
    		return;
		if (event.getType().equals("suggest")) {
        	String query = event.getParameter(0);
            String[] result = getSuggestListener().onRequestSuggenstion( event, query );
            String resultString = "";
            for (int i = 0; i < result.length; i++) {
            	resultString += result[i]+"\n";
			}
            getContext().add( getId()+".suggest", resultString, Context.TYPE_SUGGEST_RESULT, Context.STATUS_COMMAND );
        }
    }

	private ISuggestListener getSuggestListener() {
		return suggestListener;
	}

	public void setSuggestListener( ISuggestListener listener ) {
		this.suggestListener = listener;
	}
	
	private String getOnClickJS() {
		String js = "";
		js += JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION) + " return false;";
		return js;
	}

	private String getOnKeyUpJS() {
		String js = "if (event.keyCode==13) {" + getOnClickJS() + "};";
		return js;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.ITextSuggest#refreshSuggestData(java.lang.String[])
	 */
	public void refreshSuggestData(String[] data) {
		this.data = data;
		createSuggest();
	}

	/**
	 * @return
	 */
	public static String getNameForInput(String textSuggestName) {
		return textSuggestName + ".INPUT";
	}
}
