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
package org.webguitoolkit.ui.base.util;

import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;

/**
 * @author i102415
 * 
 */
public class ActionHelper {
	private final Page page;

	public ActionHelper(Page page) {
		this.page = page;
	}

	public void click(IButton button) {
		Button b = (Button)button;
		ClientEvent event = new ClientEvent(b, b.getId(), Integer.toString(ClientEvent.TYPE_ACTION), null);
		b.dispatch(event);
	}
}
