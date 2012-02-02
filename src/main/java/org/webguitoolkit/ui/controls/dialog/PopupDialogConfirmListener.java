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


/**
 * Wrapper class for IPopupDialogListener. Most often the caller is just interested to perform an action 
 * when the user confirms a popup dialog with either Yes or Ok.
 * @author i102492
 *
 */
public abstract class PopupDialogConfirmListener implements IPopupDialogListener {

	public void onPopupCancel() { }

	public void onPopupNo(Object rslt) { }

	public void onPopupOk(Object rslt) {
		onPopupConfirm(rslt);
	}
	public void onPopupYes(Object rslt) {
		onPopupConfirm(rslt);
	}

	public abstract void onPopupConfirm(Object rslt);
}
