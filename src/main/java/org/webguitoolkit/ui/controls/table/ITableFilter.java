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
package org.webguitoolkit.ui.controls.table;

import org.webguitoolkit.ui.controls.form.ISelect;

/**
 * A table wide filter that can be applied.
 * 
 * @author Peter
 * 
 */
public interface ITableFilter extends ISelect {
	/**
	 * 
	 * @param ref
	 *            t.b.d.
	 */
	void setRef(String ref);

	/**
	 * 
	 * @param label
	 *            for the filter
	 */
	void setLabel(String label);

	/**
	 * 
	 * @param labelKey
	 *            resource key for the label
	 */
	void setLabelKey(String labelKey);

}