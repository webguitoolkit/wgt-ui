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

import java.io.Serializable;

import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * Wizard throws different events. the default behaviour is not executed when the listener is present. That means that the
 * programmer must call the default behaviour in each method manually. You may want to see the WizardAdapter, which is an
 * implementation of this just calling the default actions for each event.<br>
 * <br>
 * 
 * All eventmethods get teo Parameters,<br>
 * wiz - the Wizard (source of event) currentTabNo - The Step (or Tab) the user is currently on (not tha tab the user wants to get
 * to, if next or back is pressed). Tab numbering as defined in the jsp (or markerString) beginning with 0.<br>
 * <br>
 * 
 * @author arno
 * 
 */
public interface IWizardListener extends Serializable {
	/**
	 * Back Button pressed by user.
	 * 
	 * Don't forget to call the default action.
	 * 
	 * @param wiz
	 * @param currentTabNo
	 */
	void onBack(ClientEvent event, IWizard wiz, int currentTabNo);

	/**
	 * Next Button pressed by user.
	 * 
	 * Don't forget to call the default action.
	 * 
	 * @param wiz
	 * @param currentTabNo which tab are we on right now
	 */
	void onNext(ClientEvent event, IWizard wiz, int currentTabNo);

	/**
	 * Finish Button pressed by user.
	 * 
	 * Don't forget to call the default action.
	 * 
	 * @param wiz
	 * @param currentTabNo
	 */
	void onFinish(ClientEvent event, IWizard wiz, int currentTabNo);

	/**
	 * Cancel Button pressed by user. Or the Window close button has been pressed. (If this is an window dialog.)
	 * 
	 * Don't forget to call the default action.
	 * 
	 * @param wiz
	 * @param currentTabNo
	 */
	void onCancel(ClientEvent event, IWizard wiz, int currentTabNo);
}
