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

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WGTException;

/**
 * <pre>
 * A StyleAttribute is the Object for the key-value pair representing a style
 * behaviour like "width:100%"
 * </pre>
 * 
 * @author Benjamin Klug
 */
public class StyleAttribute implements Serializable {

	private String attributeType = null;
	private String attributeValue = null;

	/**
	 * create a new behaviour/attribute using this constructor.
	 * 
	 * usage: <code>
	 *  new StyleAttribute("width","auto");
	 * </code>
	 * 
	 * @param newAttributeType
	 * @param newAttributeValue
	 */
	public StyleAttribute(String newAttributeType, String newAttributeValue) {
		super();
		newAttributeType = StringUtils.trim(newAttributeType);
		newAttributeValue = StringUtils.trim(newAttributeValue);
		this.setAttributeType(newAttributeType);
		this.setAttributeValue(newAttributeValue);
	}

	/**
	 * create a new behaviour/attribute using this constructor.
	 * 
	 * usage: <code>
	 *  new StyleAttribute("width:auto;");
	 *  new StyleAttribute("position:relative");  
	 * </code>
	 * 
	 * @param newAttributeAsString
	 * @throws StyleException on parsing errors
	 */
	public StyleAttribute(String newAttributeAsString) throws WGTException {
		super();
		this.parseInput(newAttributeAsString);
	}

	/**
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * tries to convert numbers to number,
	 * 
	 * @return -1 on failure, else value
	 */
	public int getAttributeValueAsInt() {
		int result = -1;
		String number = null;
		int extentionLength = -1;
		if (attributeValue != null) {
			if (attributeValue.endsWith(Style.PERCENT)) {
				extentionLength = 1;
			}
			else if (attributeValue.endsWith(Style.PIXEL) || attributeValue.endsWith(Style.INCH)
					|| attributeValue.endsWith(Style.MILLIMETER) || attributeValue.endsWith(Style.PICA)
					|| attributeValue.endsWith(Style.CENTIMETER) || attributeValue.endsWith(Style.POINT)
					|| attributeValue.endsWith(Style.RELATIVE_TO_FONTSIZE) || attributeValue.endsWith(Style.RELATIVE_TO_LOWER_CASE_HEIGHT)
					|| attributeValue.endsWith(Style.CENTIMETER)) {
				extentionLength = 2;
			}
			else {
				extentionLength = 0;
			}
			number = attributeValue.substring(0, attributeValue.length() - extentionLength);
			try {
				result = Integer.parseInt(number);
			}
			catch (NumberFormatException e) {
				result = -1;
			}
		}
		return result;
	}

	/**
	 * tries to convert numbers to number,
	 * 
	 * @return -1 on failure, else value
	 */
	public float getAttributeValueAsFloat() {
		float result = -1;
		String number = null;
		int extentionLength = -1;
		if (attributeValue != null) {
			if (attributeValue.endsWith(Style.PERCENT)) {
				extentionLength = 1;
			}
			else if (attributeValue.endsWith(Style.PIXEL) || attributeValue.endsWith(Style.INCH)
					|| attributeValue.endsWith(Style.MILLIMETER) || attributeValue.endsWith(Style.PICA)
					|| attributeValue.endsWith(Style.CENTIMETER) || attributeValue.endsWith(Style.POINT)
					|| attributeValue.endsWith(Style.RELATIVE_TO_FONTSIZE) || attributeValue.endsWith(Style.RELATIVE_TO_LOWER_CASE_HEIGHT)
					|| attributeValue.endsWith(Style.CENTIMETER)) {
				extentionLength = 2;
			}
			else {
				extentionLength = 0;
			}
			number = attributeValue.substring(0, attributeValue.length() - extentionLength);
			try {
				result = Float.parseFloat(number);
			}
			catch (NumberFormatException e) {
				result = -1;
			}
		}
		return result;
	}

	/**
	 * @param attributeValue the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType() {
		return attributeType;
	}

	/**
	 * @param attributeType the attributeType to set
	 */
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * parses style behaviour in common html format like "width:100;" or "width: 200"
	 * 
	 * @param newAttribute
	 * @throws StyleException on parsing errors
	 */
	private void parseInput(String newAttribute) throws WGTException {
		// check, if given attribute looks valid
		if (newAttribute != null && newAttribute.indexOf(":") > 0 && newAttribute.length() > (newAttribute.indexOf(":") + 1)) {
			this.setAttributeType(newAttribute.substring(0, newAttribute.indexOf(":")));
			this.setAttributeValue(newAttribute.substring(newAttribute.indexOf(":") + 1, newAttribute.length()));
			// remove ; and trim
			this.setAttributeType(StringUtils.trim(this.getAttributeType()));
			this.setAttributeValue(StringUtils.trim(this.getAttributeValue()));
			if (this.getAttributeValue() != null) {
				if (this.getAttributeValue().endsWith(";")) {
					this.setAttributeValue(this.getAttributeValue().substring(0, this.getAttributeValue().length() - 1));
					this.setAttributeValue(StringUtils.trim(this.getAttributeValue()));
				}
			}
		}
		else {
			throw new WGTException("could not parse input: " + newAttribute);
		}
	}

}
