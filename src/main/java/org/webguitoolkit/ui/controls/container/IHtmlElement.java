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
package org.webguitoolkit.ui.controls.container;

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.layout.ILayoutable;

/**
 * <h1>Interface for a HtmlElement</h1>
 * <p>
 * Creates arbitrary HTML elements. Sometime the WGT does not provide all needed controls or it is necessary to create a plain html tag.
 * An example for the use of IHtmlElement is the HTML image tag since there is no image wgt component except the IconButton.
 * </p>
 * <b>Creation of IHtmlElements</b><br>
 * //creates an <img> tag
 *  IHtmlElement element = getFactory().createHtmlElement(layout);
 *  //setTagName defines the HTML which should be created. Any tag value is possible! Only valid HTML tag names should be used  
 *  element.setTagName("IMG");
 *  //adds any attribute to an HTML tag. The developer should be certain that the HTML he creates does support the added attribute 
 *  element.setAttribute("src", "./images/timeclock-on.gif");
 *  element.setAttribute("class", "wgtImage");
 *  
 * //this generates the following html code
 * //<img src="./images/timeclock-on.gif" class="wgtImage">
 * 
 * //creates an <b> tag with nested text
 * IHtmlElement boldtext = getFactory().createHtmlElement(layout);
 * //setTagName defines the HTML which should be created. Any tag value is possible! Only valid HTML tag names should be used  
 * boldtext.setTagName("B");
 * //adds any attribute to an HTML tag. The developer should be certain that the HTML he creates does support the added attribute 
 * boldtext.setAttribute("style", "font-size:20px;font-weight:bold;color:#ff0000;");
 * //some HTML tags allow text or HTML tags between the opening and closing tag. With the innerHTML method it is possible 
 * to add nested tags or a text to the created HTML element
 * boldtext.innerHTML("I am a large red text in bold");
 * 
 * //this generates the following html code
 * //<b style="font-size:20px;font-weight:bold;color:#ff0000;">I am a large red text in bold</b>
 * 
 * 
 * //if more than one HTML element of the same type should be created the method should look like this  
 * IHtmlElement element = createImage(layout, "./images/timeclock-on.gif", null);
 * 
 * private IHtmlElement createImage(IComposite parent, String url, String id) {
 * 	IHtmlElement html;
 * 	if (id == null)
 * 			html = getFactory().createHtmlElement(parent);
 * 		else {
 * 			html = getFactory().createHtmlElement(parent, null, id)
 * 		}
 * 		html.setTagName("IMG");
 * 		html.setAttribute("src", url);
 * 		return html;
 * 	}
 *
 * <p/>
 * <b>Event handling</b><br>
 * The HTMLElement has no WGT supported event handling. If client side events are needed, they could be defined with setAttribute like: 
 * element.setAttribute("onclick","alert('you clicked on an HTML element, stop doing this!!')")
 * <p/>
 * Used CSS classes : none
 * </p>
 * @author Lars
 * 
 */
public interface IHtmlElement extends IBaseControl, IComposite, ILayoutable {
	/**
	 * Set the HTML tag
	 * 
	 * @param tagName name of the HTML tag.
	 */
	 void setTagName(String tagName);

	/**
	 * Set attributes on the HTML element
	 * 
	 * @param attributeName
	 *            name
	 * @param attributeValue
	 *            value
	 */
	 void setAttribute(String attributeName, String attributeValue);
	
	/**
	 * Get the formerly set attribute's value
	 * 
	 * @param attributeName name
	 * @return
	 */
	String getAttributeValue(String attributeName);

	/**
	 * Sets the innerHTML value of the element
	 * @param innerHtml value to set
	 */
	void innerHtml(String innerHtml);

}