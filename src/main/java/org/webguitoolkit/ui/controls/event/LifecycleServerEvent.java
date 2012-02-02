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
package org.webguitoolkit.ui.controls.event;

import org.webguitoolkit.ui.controls.BaseControl;
/**
 * Server event which can give access to the client event.
 * There are two Lifecycle events:
 *    EVENT_PREDISPATCH:
 *      This event is fire before the client event is dispatched to the component 
 *      which is registered for the event. But all other initialisation are already been done.
 *      Especialy: The component to receive the client event is determined, The changed context from the client 
 *      has been applied, and the page is synchronized. 
 *    EVENT_POSTDISPATCH:
 *      This event is being fired right after the client event has been dispatched and the 
 *      components and application has returned from their work. The framework is ready to
 *      renderer the changes out to the client. The context change have not been calculated yet, so
 *      you are allowed to make context changes in the postdispatch-listener.
 *      
 * Keep in mind, that client events are only fired in case of ajax events. The inital http request to render
 * the page is not a client event. 
 *
 * @author arno
 *
 */
public class LifecycleServerEvent extends ServerEvent {

	ClientEvent clientEvent;
	
	public LifecycleServerEvent() {
		super();
	}

	public LifecycleServerEvent(BaseControl source, int type, ClientEvent clientEvent) {
		super(source, type);
		this.clientEvent = clientEvent;		
	}

	public ClientEvent getClientEvent() {
		return clientEvent;
	}

	public void setClientEvent(ClientEvent clientEvent) {
		this.clientEvent = clientEvent;
	}

}
