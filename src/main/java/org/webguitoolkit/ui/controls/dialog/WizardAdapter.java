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
package org.webguitoolkit.ui.controls.dialog;

import org.webguitoolkit.ui.controls.event.ClientEvent;
/**
 * This class adapts the interface IWizardListener and implements the 
 * default behaviour. By calling the default... Methods on the Wizard.
 * @author arno
 *
 */
public class WizardAdapter implements IWizardListener {

	public void onBack(ClientEvent event, IWizard wiz, int currentTabNo) {
		int newTab = wiz.currentTab() - 1;
		wiz.selectTab(newTab);
	}

	public void onCancel(ClientEvent event, IWizard wiz, int currentTabNo) {
		wiz.close();
	}

	public void onFinish(ClientEvent event, IWizard wiz, int currentTabNo) {
		wiz.close();
	}

	public void onNext(ClientEvent event, IWizard wiz, int currentTabNo) {
		wiz.selectTab(wiz.currentTab() + 1);
	}

}
