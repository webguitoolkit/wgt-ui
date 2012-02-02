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
 * <pre>
 * Provides base functionality for StyleSelectors
 * </pre>
 * 
 * @author Benjamin Klug
 */
public abstract class AStyleSelector {

	private String selector = null;
	private String selectorValue = null;
	private boolean lineBreaks = false;
	private boolean brackets = false;
	private boolean quotes = false;

	/**
	 * Selector for style element. i.e. "." for ClassSelector
	 * 
	 * @return the selector
	 */
	public String getSelector() {
		return selector;
	}

	/**
	 * returns the name of the style, not in use now, required for implementing a Stylesheet Class
	 * 
	 * @return
	 */
	public String getName() {
		return getSelectorValue();
	}

	/**
	 * @param selector the selector to set
	 */
	protected void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * @return the lineBreaks
	 */
	public boolean isLineBreaks() {
		return lineBreaks;
	}

	/**
	 * @param lineBreaks the lineBreaks to set
	 */
	protected void setLineBreaks(boolean lineBreaks) {
		this.lineBreaks = lineBreaks;
	}

	/**
	 * @return the brackets
	 */
	public boolean isBrackets() {
		return brackets;
	}

	/**
	 * @param brackets the brackets to set
	 */
	protected void setBrackets(boolean brackets) {
		this.brackets = brackets;
	}

	/**
	 * @return the quotes
	 */
	public boolean isQuotes() {
		return quotes;
	}

	/**
	 * @param quotes the quotes to set
	 */
	public void setQuotes(boolean quotes) {
		this.quotes = quotes;
	}

	/**
	 * @return true when selector requires quotes else false
	 * 
	 * @return
	 */
	public boolean hasQuotes() {
		return this.isQuotes();
	}

	/**
	 * @return true when selector line brakes, else false
	 * 
	 * @return
	 */
	public boolean hasLineBreakes() {

		return this.isLineBreaks();
	}

	/**
	 * @return true when selector requires brackets, else false
	 */
	public boolean hasBrackets() {
		return this.isBrackets();
	}

	/**
	 * Selector for style element. i.e. "myclass" of ".myclass" for ClassSelector
	 * 
	 * @return the selectorValue
	 */
	public String getSelectorValue() {
		return selectorValue;
	}

	/**
	 * @param selectorValue the selectorValue to set
	 */
	protected void setSelectorValue(String selectorValue) {
		this.selectorValue = selectorValue;
	}

}
