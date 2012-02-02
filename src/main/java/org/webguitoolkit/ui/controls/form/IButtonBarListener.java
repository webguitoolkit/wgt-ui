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

import java.io.Serializable;

import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * <p>
 * <b>Listener interface for the ButtonBar.</b><br>
 * This defines mainly the call back for each button in the bar. Via configuration it is possible that not all button actually
 * appear on the screen. Just leave the methods for the used buttons empty, they will not be called.
 * </p>
 * <p>
 * Note that you need to call a method on the ButtonBar itself to implement the default behavior.
 * </p>
 * 
 * @author arno@schatz.to
 * 
 * @see org.webguitoolkit.ui.controls.form.IButtonBar
 */
public interface IButtonBarListener extends Serializable {

	/**
	 * save button pressed
	 * 
	 * @param event the client event provided by the framework
	 */
	void onSave(ClientEvent event);

	/**
	 * new button pressed
	 * 
	 * @param event the client event provided by the framework
	 */
	void onNew(ClientEvent event);

	/**
	 * cancel button pressed
	 * 
	 * @param event the client event provided by the framework
	 */
	void onCancel(ClientEvent event);

	/**
	 * delete button pressed
	 * 
	 * @param event the client event provided by the framework
	 */
	void onDelete(ClientEvent event);

	/**
	 * edit button pressed
	 * 
	 * @param event the client event provided by the framework
	 */
	void onEdit(ClientEvent event);

}
