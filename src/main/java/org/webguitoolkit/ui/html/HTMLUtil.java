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
package org.webguitoolkit.ui.html;

import org.webguitoolkit.ui.html.definitions.HTMLAttribute;
import org.webguitoolkit.ui.html.definitions.HTMLElement;

/**
 * @author i01002455
 * 
 */
public class HTMLUtil {
	private HTMLUtil() {
		// not possible
	}

	public static HTMLElement getHTMLTag(String tagName) {
		if (tagName != null)
			for (HTMLElement tag : HTMLElement.values()) {
				if (tagName.trim().equalsIgnoreCase(tag.name()))
					return tag;
			}
		return null;
	}

	public static HTMLAttribute getHTMLAttribute(String attributeName) {
		if (attributeName != null)
			for (HTMLAttribute att : HTMLAttribute.values()) {
				if (attributeName.trim().equalsIgnoreCase(att.name()))
					return att;
			}
		return null;
	}
}
