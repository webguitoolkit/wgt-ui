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
package org.webguitoolkit.ui.controls.form;

import java.io.Serializable;

/**
 * <pre>
 * Interface for non typical members inside a compound, which
 * want to be part of the events a compound generates:
 *  - managing the read only mode.
 *  - save to and loadFrom cycles.
 * </pre>
 * 
 * @author Arno
 */
public interface ICompoundLifecycleElement extends Serializable {
	/**
	 * 
	 * @param dataObject the POJO
	 */
	void saveTo(Object dataObject);

	/**
	 * 
	 * @param data the POJO
	 */
	void loadFrom(Object data);

	/**
	 * 
	 * @param mode
	 */
	void changeMode(int mode);

	/**
	 * 
	 * @return the compound in which this is embedded.
	 */
	ICompound surroundingCompound();

	/**
	 * remove the input error style from form control
	 */
	void clearError();

	/**
	 * add the input error style from form control
	 */
	void showError();
}
