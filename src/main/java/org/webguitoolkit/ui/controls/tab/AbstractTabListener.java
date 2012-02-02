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

import java.util.Hashtable;

import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * 
 * @author i102415
 * @deprecated
 */
@Deprecated
public class AbstractTabListener implements ITabListener {

	Hashtable tabs = new Hashtable();

	public boolean onTabChange(ITab old, ITab selected, ClientEvent event) {
		AbstractTab absTab = (AbstractTab)tabs.get(old);
		if (absTab != null) {
			boolean allow = absTab.leave();
			if (!allow)
				return false;
		}
		absTab = (AbstractTab)tabs.get(selected);
		if (absTab != null) {
			boolean allow = absTab.load();
			if (!allow)
				return false;
		}
		return true;
	}

	public void addTab(AbstractTab aTab) {
		tabs.put(aTab.getTab(), aTab);
	}
}
