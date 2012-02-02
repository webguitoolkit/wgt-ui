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

import junit.framework.TestCase;

import org.webguitoolkit.ui.controls.util.style.selector.ClassSelector;
import org.webguitoolkit.ui.controls.util.style.selector.InlineSelector;
import org.webguitoolkit.ui.controls.util.style.selector.TagSelector;

public class StyleTest extends TestCase {

	private Style s = null;

	private void initTestCase() {
		if (s == null) {
			s = new Style();
			s.add("attname", "attvalue");
			s.addBottom(10, "px");
			s.addDisplay(Style.DISPLAY_NONE);
			s.addHeight(10, "%");
			s.addLeft(9, "px");
			s.addPostition(Style.POSITION_ABSOLUTE);
			s.addStyleAttribute(new StyleAttribute("any:attribute"));
			s.addStyleAttributes("one:1;two:2;");
		}
	}

	public void testStyleInline() {
		this.initTestCase();
		s.setSelector(new InlineSelector());
		
		assertNotNull("No output available", s);
	}
	
	public void testStyleClass() {
		this.initTestCase();
		s.setSelector(new ClassSelector("clazz"));
		
		assertNotNull("No output available", s);
	}
	
	public void testStyleId() {
		this.initTestCase();
		s.setSelector(new TagSelector());
		
		assertNotNull("No output available", s);
	}

}
