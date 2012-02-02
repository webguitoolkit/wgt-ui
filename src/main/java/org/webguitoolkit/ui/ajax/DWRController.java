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
/*
 */
package org.webguitoolkit.ui.ajax;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.util.Guid;

/**
 * <pre>
 * This class is a manager for component trees and components, the DWRCaller uses this class
 * to get the controls that have fired an action in the browser.
 * 
 * - The DWRFilter creates the DWRController and binds it to the thread local
 * - After execution of the request the DWRController is placed in the session
 * - On the next request the DWRController is taken from the session and bind to the
 *   thread local again.
 * </pre>
 */
public class DWRController implements Serializable {

	// the instance shall be addressed via the getInstance method...
	private static ThreadLocal<DWRController> compTrees = new ThreadLocal<DWRController>();
	private Hashtable<String, IBaseControl> components = new Hashtable<String, IBaseControl>();
	// counter for generating unique ids for the components
	protected Guid seq = new Guid();

	/**
	 * live cycle of this DWRController (= component tree) to be managed by WGTFilter
	 */

	public static DWRController getInstance() {
		DWRController theTree = (DWRController)compTrees.get();
		// initialising id done here in the own method...
		if (theTree == null) {
			throw new NullPointerException();
		}
		return theTree;
	}

	/**
	 * this should be called by the WGTFilter to get the component tree from the session of the user. This must be done at the
	 * beginning of the request. including ajax requests.
	 * 
	 * @param aCompTree instance
	 */
	public static void setInstance(DWRController aCompTree) {
		compTrees.set(aCompTree);
	}

	/**
	 * this method must be used to get client client events dispatched to Components. Event for the client enter at the
	 * DWRController, then get dispatched to the Component which is register for that css-id (css-id is a mandatory parameter for
	 * client side events).The Component itself is responsible to call the Listeners
	 * 
	 */
	public synchronized void registerForEvents(String cssId, BaseControl component) {
		components.put(cssId, component);
	}

	public synchronized void deregisterForEvents(String cssId, BaseControl component) {
		IBaseControl comp = components.get(cssId);
		if (component.equals(comp))
			components.remove(cssId);

		// fix for memory leak when controls are registered under different names (e.g. ChildColumnRenderer)
		if (components.containsValue(component)) {
			HashSet<Entry<String, IBaseControl>> ces = new HashSet<Entry<String, IBaseControl>>(components.entrySet());
			for (Iterator<Entry<String, IBaseControl>> iter = ces.iterator(); iter.hasNext();) {
				Entry<String, IBaseControl> entry = iter.next();
				if (entry.getValue().equals(component))
					components.remove(entry.getKey());
			}
		}

	}

	public Map getComponents() {
		return components;
	}

	public BaseControl getComponentById(String id) {
		return (BaseControl)components.get(id);
	}

	public long generateGuid() {
		return seq.generateGuid();
	}

}
