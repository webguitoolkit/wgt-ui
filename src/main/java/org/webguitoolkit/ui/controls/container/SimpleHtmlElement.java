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

import java.io.PrintWriter;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.layout.ILayout;

/**
 * This component is basically a placeholder for simple html elements like hr and br
 */
public class SimpleHtmlElement extends BaseControl implements IHtmlElement {

	// the name of the tag this should render
	protected String tagName = "br";

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public SimpleHtmlElement() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public SimpleHtmlElement(String id) {
		super(id);
	}

	protected void startHTML(PrintWriter out) {
		out.print("<" + getTagName() + " ");
		stdParameter(out);
		out.print("/>");
	}

	protected void endHTML(PrintWriter out) {
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.IHtmlElement#getTagName()
	 */
	public String getTagName() {
		return tagName;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.IHtmlElement#setTagName(java.lang.String)
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void add(IBaseControl child) {
		super.add(child);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.IHtmlElement#innerHtml(java.lang.String)
	 */
	public void innerHtml(String html) {
		throw new WGTException("It is not posible to set inner HTML for a simple HTMLElement!");
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.IHtmlElement#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String attributeName, String attributeValue) {
		super.setAttribute(attributeName, attributeValue);
	}

	protected void init() {
	}

	/**
	 * sets the layout for the controls displayed in the HtmlElement (GridLayout, BorderLayout, ... )
	 * 
	 * @param layout the layout to be set
	 */
	public void setLayout(ILayout layout) {
		super.setLayout(layout);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.IHtmlElement#getAttributeValue(java.lang.String)
	 */
	public String getAttributeValue(String attributeName) {
		return super.getAttribute(attributeName);
	}
}
