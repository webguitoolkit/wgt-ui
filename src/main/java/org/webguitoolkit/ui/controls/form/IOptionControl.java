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

/**
 * Common interface for Radio and CheckBox controls.
 * 
 * @author Peter
 * 
 */
public interface IOptionControl extends IFormControl {

	/**
	 * set the state of this radio
	 * @param state true for selected
	 */
	 void setSelected(boolean state);

	/**
	 * read the state of the radio, where no state is like turned off
	 * 
	 * @return true if radio is selected
	 */
	 boolean isSelected();

	/**
	 * 
	 * @return the options value
	 */
	 String getOptionValue();

	/**
	 * 
	 * @param optValue the options value
	 */
	 void setOptionValue(String optValue);
	 
	 /**
	  * if bubble is enabled javascript events are propagated to the underlying elements
	  * @param bubble true is default
	  */
	  void setEventBubbling(boolean eventBubble);

}