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
package org.webguitoolkit.ui.controls.container;

import java.io.Serializable;

import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * Listener that processes the different events fired by the canvas's buttons.
 * 
 * @author Martin
 */
public interface ICanvasWindowListener extends Serializable {

	/**
	 * Called if close button was pressed
	 * 
	 * @param event the event
	 */
	void onClose(ClientEvent event);

	/**
	 * Called if mMinimize button was pressed
	 * 
	 * @param event the event
	 */
	void onMinimize(ClientEvent event);

	/**
	 * Called if maximize button was pressed
	 * 
	 * @param event the event
	 */
	void onMaximize(ClientEvent event);

}
