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
package org.webguitoolkit.ui.controls.util.style;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.style.selector.IStyleSelector;

/**
 * Base functionality for the Style class
 * 
 * @author Benjamin Klug
 * 
 */
public abstract class BaseStyle implements Serializable {

	private IStyleSelector selector = null;
	private TreeMap attributes = null;
	private List styleChangedListeners = new ArrayList();

	/**
	 * default constructor
	 */
	protected BaseStyle() {
		super();
	}

	/**
	 * 
	 * use this constructor to pass over the selector
	 * 
	 * @param newSelector
	 */
	protected BaseStyle(IStyleSelector newSelector) {
		super();
		this.selector = newSelector;
	}

	/**
	 * get current selector
	 * 
	 * @return the selector
	 */
	public IStyleSelector getSelector() {
		return selector;
	}

	/**
	 * set selector
	 * 
	 * @param selector the selector to set
	 */
	public void setSelector(IStyleSelector selector) {
		this.selector = selector;
	}

	/**
	 * this method renders the Style as String.
	 * 
	 * @return Style as String
	 */
	public String getOutput() {
		StringBuffer out = new StringBuffer();
		String lineBreake = "";
		int attCount = -1;
		if (this.selector.hasLineBreakes()) {
			lineBreake = "\n";
		}
		out.append(selector.getOutput());
		if (selector.hasBrackets()) {
			out.append(" {" + lineBreake);
		}
		else if (selector.hasQuotes()) {
			out.append("\"");
		}
		if (attributes != null) {
			Iterator attribKeys = attributes.keySet().iterator();
			while (attribKeys.hasNext()) {
				StyleAttribute att = ((StyleAttribute)this.attributes.get((String)attribKeys.next()));
				attCount++;
				if (this.selector.hasLineBreakes()) {
					out.append("\t");
				}
				else {
					if (attCount > 0)
						out.append(" ");
				}
				out.append(att.getAttributeType() + ": " + att.getAttributeValue() + ";" + lineBreake);
			}
		}
		else {
			if (selector.hasBrackets())
				out.append("\n");
		}
		if (selector.hasBrackets()) {
			out.append("}" + lineBreake);
		}
		else if (selector.hasQuotes()) {
			out.append("\"");
		}

		return out.toString();
	}

	/**
	 * Add new StyleAttribute
	 * 
	 * @param newAttribute
	 */
	public void addStyleAttribute(StyleAttribute newAttribute) {
		if (attributes == null)
			attributes = new TreeMap();
		attributes.put(newAttribute.getAttributeType(), newAttribute);
		fireEvent(IStyleChangeListener.ATTRIBUTE_ADDED, newAttribute.getAttributeType());
	}

	/**
	 * Add attributes by String: "name1:value1;name2:value2";
	 * 
	 * @param newAttributes
	 */
	public void addStyleAttributes(String newAttributes) {
		String[] newAtts = null;
		if (newAttributes != null) {
			newAtts = StringUtils.split(newAttributes, ';');
			for (int i = 0; i < newAtts.length; i++) {
				if (newAtts[i] != null)
					try {
						this.addStyleAttribute(new StyleAttribute(StringUtils.trimToEmpty(newAtts[i])));
					}
					catch (WGTException e) {
						e.printStackTrace();
					}
			}
		}
	}

	/**
	 * remove a attribute by type i.e. "width"
	 * 
	 * @param newAttributeType
	 */
	public void removeAttributeByType(String newAttributeType) {
		if (this.attributes != null && this.attributes.get(newAttributeType) != null) {
			this.attributes.remove(newAttributeType);
			fireEvent(IStyleChangeListener.ATTRIBUTE_REMOVED, newAttributeType);
		}
	}

	/**
	 * get a attribute by type i.e. "width"
	 * 
	 * @param newAttributeType
	 */
	public StyleAttribute getAttributeByType(String newAttributeType) {
		if (this.attributes != null && this.attributes.get(newAttributeType) != null) {
			return (StyleAttribute)this.attributes.get(newAttributeType);
		}
		else {
			return null;
		}
	}

	/**
	 * clear all attributes from the style.
	 */
	public void clearAttributes() {
		this.attributes = null;
		fireEvent(IStyleChangeListener.ATTRIBUTE_CLEARED, null);
	}

	/**
	 * Adds a listener that can do actions on style change, e.g. controls can send an update command for the style to the browser
	 * 
	 * @param listener the listener implementation
	 */
	public void registerStyleChangedListener(IStyleChangeListener listener) {
		styleChangedListeners.add(listener);
	}

	protected void fireEvent(int eventType, String attribute) {
		for (Iterator iterator = styleChangedListeners.iterator(); iterator.hasNext();) {
			IStyleChangeListener listener = (IStyleChangeListener)iterator.next();
			listener.handle(eventType, attribute);
		}
	}

	public void unregisterStyleChangedListener(String id) {
		for (Iterator iterator = styleChangedListeners.iterator(); iterator.hasNext();) {
			IStyleChangeListener listener = (IStyleChangeListener)iterator.next();
			if (listener.getListenerId().equals(id))
				iterator.remove();
		}
	}

}
