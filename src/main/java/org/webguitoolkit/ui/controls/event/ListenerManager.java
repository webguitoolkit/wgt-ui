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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * this manages listener and event firing for another class.
 * 
 * @author arno
 * 
 */
public class ListenerManager implements Serializable {

	protected List listener;

	public void registerListener(int eventtype, IServerEventListener liz) {
		if (listener == null)
			listener = new ArrayList(2);// sparsam
		listener.add(new Object[] { new Integer(eventtype), liz });
	}

	public void fireServerEvent(ServerEvent event) {
		if (listener == null)
			return;
		// create a copy, as the listener array may be change during the event
		List myListener = new ArrayList();
		myListener.addAll(listener);

		Object[] l;
		for (int i = 0; i < myListener.size(); i++) {
			l = (Object[])myListener.get(i);
			if (((Integer)l[0]).intValue() == event.getTypeAsInt()) {
				((IServerEventListener)l[1]).handle(event);
			}
		}
	}

	/**
	 * remove a the listener liz of type eventtype from the registered listeners. Or if liz==null remove all listeners of that
	 * type.
	 * 
	 * @param eventtype
	 * @param liz
	 */
	public void removeListener(int eventtype, IServerEventListener liz) {
		if (listener == null)
			return;
		Object[] l;
		for (int i = listener.size() - 1; i >= 0; i--) {
			l = (Object[])listener.get(i);
			if ((liz == null || l[1] == liz) && ((Integer)l[0]).intValue() == eventtype) {
				listener.remove(i);
				break;
			}
		}
	}

}
