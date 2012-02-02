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

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ServerEvent;

/**
 * Dialog event is a serverevent. Additionally it has a state. This
 * is the state the Dialog wants to communicate to its registered listeners.
 * Usually, this is  like 'dialog succesfully close (save pressed and validation ok)'
 * or 'dialog cancel (cacel pressed)'.
 * 
 * @author arno
 */
public class DialogEvent extends ServerEvent {
	
	public static final int EVENT_DIALOG_STATE = 2; 
	
	// these states are predefined, but always can be extended by the dialog 
	// itself. thderefore the number above 1000 are reserved
	protected static int numSeq = 1000;
	public static final int STATE_SAVED = numSeq++;
	public static final int STATE_OK = numSeq++;
	public static final int STATE_CANCELLED = numSeq++;
	

	protected int state;

	public DialogEvent() {
	}

	public DialogEvent(IBaseControl source, int type, int state) {
		super(source, type);
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
