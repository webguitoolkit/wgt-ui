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
package org.webguitoolkit.ui.controls.util.style.selector;


/**
 * creates styles in format
 * <code>
 * .myClass{
 * 			width:100%;
 * 			color:red;
 * }
 * </code>
 * 
 * where selectorValue = "myclass"
 * 
 * 
 * @author Benjamin Klug
 *
 */
public class ClassSelector extends AStyleSelector implements IStyleSelector {

	/**
	 * Private initialization. define behavoiur of selector here
	 */
	{
		super.setSelector(".");
		super.setBrackets(true);
		super.setLineBreaks(true);
		super.setQuotes(false);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.controls.util.style.selector.IStyleSelector#getOutput()
	 */
	public String getOutput() {
		return this.getSelector() + this.getSelectorValue();
	}

	/**
	 * set css-class
	 * 
	 * @param cssClass
	 * @return
	 */
	public ClassSelector setClass(String cssClass) {
		this.setSelectorValue(cssClass);
		return this;
	}

	/**
	 * crete an new instance passing the css-classname 
	 * 
	 * @param cssClass
	 */
	public ClassSelector(String cssClass) {
		super();
		this.setSelectorValue(cssClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserver.cssck.ISelector#hasBrackets()
	 */
	public boolean hasBrackets() {
		return super.isBrackets();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserver.cssck.ISelector#hasLineBreakes()
	 */
	public boolean hasLineBreakes() {
		return super.isLineBreaks();
	}



}
