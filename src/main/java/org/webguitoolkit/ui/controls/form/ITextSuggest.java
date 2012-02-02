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

import org.webguitoolkit.ui.controls.event.ISuggestListener;

/**
 * 
 * <b>Interface for the TextSuggest.</b>
 * <p/>
 * The TextSuggest is an input field with a suggest function which is triggered when a
 * key is pressed (currently the suggest is triggered after at least 3 characters are in the input field).<br/>
 * <p/>
 * HowTo: Insert the TextSuggest element into your page and implement a new ISuggestListener
 * (onRequestSuggenstion) where you can build the suggested values. Return them as String[]
 * <p/>
 * <b>Creation of a suggest input field</b><p/>
 * <pre>
 * // suggest input field
 * ITextSuggest suggest = factory.createTextSuggest(layout, "PROPERTYNAME");
 * suggest.setSuggestListener(new SuggestListener());
 * </pre>
 * <br>
 * <b>Event handling</b><p/>
 * TextSuggest fields trigger events when typing a value into the field, this event can be handled by action listeners.<br>
 * <pre>
 * //the action listener code
 * 	public String[] onRequestSuggenstion(ClientEvent event, String suggestString) {
 * 		//the suggest box just makes sense if you fetch the suggest values from a database. so here stand should be your 
 * 		//database query with the suggestString as where clause.
 * 		String[] result =  {"Hello", "Hello Friend", "Hello friendly user", "Hello World", "hellwhatafunction"};
 * 		return result;
 * 	}
 * } 
 * 
 * //code where the text field is created
 * //default event (onKeyDown)
 * suggest.setSuggestListener(new SuggestListener())
 * </pre>
 * <p/>
 * <b>CSS classes:</b> suggestBox, wgtReadonly,suggestBox
 * 
 * @author Lars
 * 
 */
public interface ITextSuggest extends IFormControl {
	/**
	 * <p>The Listener provides the suggest values when a key is pressed. The listener will be triggered after the 3rd entered character<p/>
	 * 
	 * @param listener Sets the keylistern for the suggest function. 
	 */
	void setSuggestListener(ISuggestListener listener);

	void refreshSuggestData(String[] data);

}
