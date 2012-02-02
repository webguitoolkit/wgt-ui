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
package org.webguitoolkit.ui.controls.layout;

import java.io.Serializable;

/**
 * 
 * @author Martin
 */
public interface ILayoutElement extends Serializable {
	/**
	 * return the layout position of the control
	 * 
	 * @return the layout position
	 */
	ILayoutData getLayoutData();

	/**
	 * sets the layout position of the control
	 * 
	 * @param position the layout position
	 */
	void setLayoutData(ILayoutData position);
}
