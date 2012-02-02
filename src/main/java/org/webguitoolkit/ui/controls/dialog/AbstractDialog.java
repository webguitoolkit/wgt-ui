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

import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;

/**
 * Abstract base class for dialog , especially popups.<br>
 * Helps to structure the most common tasks like fireEvent for the<br>
 * calling class.
 * 
 * @author arno
 * 
 */
public abstract class AbstractDialog implements Serializable {
	ListenerManager listenerManager = new ListenerManager();

	public void fireServerEvent(DialogEvent event) {
		listenerManager.fireServerEvent(event);
	}

	/**
	 * register if you want to be informed about state changes of thsi dialog.<br>
	 * the event fire in the listener can always be casted to DialogEvent.<br>
	 * query the state the Dialog fire by calling DialogEvent.getState()
	 * 
	 * @param eventtype
	 * @param listener
	 */
	public void registerListener(int eventtype, IServerEventListener listener) {
		listenerManager.registerListener(eventtype, listener);
	}

	public void removeListener(int eventtype, IServerEventListener listener) {
		listenerManager.removeListener(eventtype, listener);
	}
}
