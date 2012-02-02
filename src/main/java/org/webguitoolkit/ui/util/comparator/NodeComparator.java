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
package org.webguitoolkit.ui.util.comparator;

import java.util.Comparator;

import org.webguitoolkit.ui.controls.tree.DefaultTreeNode;

/**
 * @author i102454
 *
 */
public class NodeComparator implements Comparator {
	private String sortProperty;
	private boolean isAscending;

	public NodeComparator(String sortProperty, boolean isAscending) {
		this.sortProperty = sortProperty;
		this.isAscending = isAscending;
	}
	
	public NodeComparator() {
	}
	
	public int compare(Object o1, Object o2) {
		if (o1!=null && o2!=null) {
			String cap1 = ((DefaultTreeNode) o1).getCaption();
			String cap2 = ((DefaultTreeNode) o2).getCaption();
			if (cap1 != null && cap2 != null) {
				return cap1.compareToIgnoreCase(cap2);
			} else {
				if (cap1==cap2) return 0;
				if (cap1==null) return -1;
				else return +1;
			}
		} else {
			if (o1==o2) return 0;
			if (o1==null) return -1;
			else return +1;
		}
	}
}
