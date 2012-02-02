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
package org.webguitoolkit.ui.controls.tab;

import java.io.Serializable;

import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * <pre>
 * The TabListener provides an extension point to on a TabStrips change event.
 * </pre>
 */
public interface ITabListener extends Serializable {

	/**
	 * 
	 * @param old the currently selected tab
	 * @param selected the tab to be shown
	 * @param event the event object
	 * @return true if the tab was initialized correctly. The tab change shall happen.
	 */
	boolean onTabChange(ITab old, ITab selected, ClientEvent event);

}
