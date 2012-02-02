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
package org.webguitoolkit.ui.controls.util;

import org.webguitoolkit.ui.controls.container.ICanvasWindowListener;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;

/**
 * <pre>
 * converts an event listener from ICanvasWindowListener to an
 * IActionListener. Can be used with canvas
 * </pre>
 */
public class Window2ActionAdapter implements ICanvasWindowListener {
	IActionListener adaptee;

	public Window2ActionAdapter(IActionListener adaptee) {
		super();
		this.adaptee = adaptee;
	}
	public void onClose(ClientEvent event) {
		adaptee.onAction(event);	
	}
	public void onMinimize(ClientEvent event) {
		// TODO Auto-generated method stub
		
	}
	public void onMaximize(ClientEvent event) {
		// TODO Auto-generated method stub
		
	}
	public void onResize(ClientEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
