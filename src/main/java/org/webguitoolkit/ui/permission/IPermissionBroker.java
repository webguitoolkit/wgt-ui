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
package org.webguitoolkit.ui.permission;

import java.io.Serializable;

/**
 * <pre>
 * The IPermissionBroker is an interface to provide the state of a control.
 * 
 * There are three states:
 * 
 * 	NO_PERMISSION = control is not displayed
 *  READ_PERMISSION = control is displayed but not active
 *  EXECUTE_PERMISSION = full permission
 * 
 * if there is no permission set we assume that the control has full permission (EXECUTE_PERMISSION)
 * </pre>
 * 
 * @author i102415
 */
public interface IPermissionBroker extends Serializable {

	/**
	 * Control is not displayed
	 */
	int NO_PERMISSION = 0;
	/**
	 * Control is displayed
	 */
	int READ_PERMISSION = 1;
	/**
	 * Control is executable
	 */
	int EXECUTE_PERMISSION = 2;

	/**
	 * Function to get the permission flag of a control
	 * 
	 * @param controlId id of the control
	 * @return the permission flag
	 */
	int getPermission(String controlId);

	/**
	 * checks if the control has read permission
	 * 
	 * @param controlId id of the control
	 * @return true if READ_PERMISSION, EXECUTE_PERMISSION or no entry found
	 */
	boolean hasReadPermission(String controlId);

	/**
	 * checks if the control has execute permission
	 * 
	 * @param controlId id of the control
	 * @return true if EXECUTE_PERMISSION or no entry found
	 */
	boolean hasExecutePermission(String controlId);
}
