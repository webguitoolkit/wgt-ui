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
package org.webguitoolkit.ui.controls.grid;

import java.util.Hashtable;
import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;

/**
 * @author i102415
 *
 */
public interface IGridModel {

	/**
	 * @param sortColumn
	 * @param sortDirection
	 * @param parameters
	 * @param start
	 * @param rows
	 * @return
	 */
	public abstract List<IDataBag> getData(String sortColumn, String sortDirection, Hashtable<String, String> parameters, int start,
			int rows);

	/**
	 * @param parameters
	 * @return
	 */
	public abstract int getCount(Hashtable<String, String> parameters);

	/**
	 * @param rowId
	 */
	public abstract IDataBag getBag(int rowId);

}
