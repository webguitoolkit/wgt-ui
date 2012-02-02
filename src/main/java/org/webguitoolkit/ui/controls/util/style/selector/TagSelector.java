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
 * style="width: 100%; color: red;"
 * </code>
 *  
 * @author Benjamin Klug
 *
 */
public class TagSelector extends AStyleSelector implements IStyleSelector {

	/**
	 * Private initialization. define behavoiur of selector here
	 */
	{
		super.setSelector("style="); // none
		super.setBrackets(false);
		super.setLineBreaks(false);
		super.setQuotes(true);
	}
	
	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.controls.util.style.selector.IStyleSelector#getOutput()
	 */
	public String getOutput() {
		return this.getSelector();
	}

}
