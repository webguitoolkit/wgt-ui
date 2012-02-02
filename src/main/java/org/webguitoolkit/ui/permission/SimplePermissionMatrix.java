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

import java.util.Hashtable;

/**
 * <pre>
 * The SimplePermissionMatrix has to be filled initially with all object permissions.
 * </pre>
 */
public class SimplePermissionMatrix implements IPermissionBroker {

	private Hashtable permissions = new Hashtable();
	public int getPermission(String controlId) {
		Integer permission = (Integer) permissions.get( controlId );
		if( permission != null )
			return permission.intValue();
		return EXECUTE_PERMISSION;
	}
	public void addPermission( String objectId, int permission ){
		permissions.put( objectId, new Integer(permission) );
	}

	public boolean hasExecutePermission(String controlId) {
		return getPermission(controlId) == IPermissionBroker.EXECUTE_PERMISSION;
	}

	public boolean hasReadPermission(String controlId) {
		return getPermission(controlId) >= IPermissionBroker.READ_PERMISSION;
	}
	
}
