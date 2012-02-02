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
/**
 * <pre>
 * Container to store the PermissionBroker in a thread local variable
 * </pre>
 * 
 * @author martin hermann
 */
public class PermissionManager {

    private static ThreadLocal permissionBrokers = new ThreadLocal();

    /**
     * @return true if the PermissionManager is initialized for this thread
     */
	public static boolean isInitialized(){
		return permissionBrokers.get() != null;
	}

    /**
     * @return the PermissionBroker for the current thread
     */
	public static IPermissionBroker getPermissionBroker(){
		return (IPermissionBroker) permissionBrokers.get();
	}
	/**
	 * sets the ThreadLocal
	 * 
	 * @param permissionBroker the PermissionBroker 
	 */
	public static void setPermissionBroker( IPermissionBroker permissionBroker ){
		permissionBrokers.set( permissionBroker );
	}
}
