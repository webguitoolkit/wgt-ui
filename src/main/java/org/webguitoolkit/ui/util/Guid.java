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
package org.webguitoolkit.ui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <pre>
 * Put an instance of this in your static variable and you will get a synchronized sequence 
 * of numbers.
 * </pre>
 * 
 * @author arno
 */
public class Guid implements Serializable {

	static Collection guids = new ArrayList();

	public Guid() {
		guids.add(this);
	}

	/**
	 * for debugging purposes you can reset all created instances to zero, so it will generate the same sequence again. USE AT
	 * YOUR OWN RISK.
	 */
	public static void reset() {
		for (Iterator it = guids.iterator(); it.hasNext();) {
			Guid g = (Guid)it.next();
			synchronized (g) {
				g.seq = 0;
			}
		}
	}

	private long seq = 0;

	public synchronized long generateGuid() {
		return seq++;
	}
}
