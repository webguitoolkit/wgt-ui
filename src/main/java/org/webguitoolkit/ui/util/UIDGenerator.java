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
package org.webguitoolkit.ui.util;

/*
 * File: UIDGenerator.java
 * Copyright (c) 2006, Endress+Hauser Infoserve GmbH & Co KG.
 */

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Random;

/**
 * <pre>
 * A UID object is available in the java.util package for JDK 1.5 and in the
 * java.rmi package for JDK 1.4. The java.rmi.server.UID has a default
 * constructor which creates a unique object. However, this constructor pauses
 * the application for 1 second for each UID generated. If many UIDs should be
 * generated then this time delay may become cumbersome. Thus, this UIDGenerator
 * tries to generate a - very probably - unique ID using a similar approach as
 * the java.rmi.server.UID class.
 * </pre>
 * 
 * @author Wolfram Kaiser
 */
public class UIDGenerator implements Serializable {

	private short counter = 0;

	private Random randomizer = new Random();

	private static final UIDGenerator singleton = new UIDGenerator();

	private static final long classTime = System.currentTimeMillis();

	private int ip;

	/**
	 * 
	 */
	private UIDGenerator() {
		try {
			final byte[] address = InetAddress.getLocalHost().getAddress();
			ip = address[0] << 24 + address[1] << 16 + address[2] << 8 + address[3];
		}
		catch (Exception e) {
			ip = 0;
		}
	}

	public static UIDGenerator getInstance() {
		return singleton;
	}

	public synchronized long getUID() {
		return (System.currentTimeMillis() << 20) + (ip << 32) + (classTime << 24) + counter++ + randomizer.nextLong();
	}

	public synchronized String getUIDAsString() {
		return String.valueOf(getUID());
	}
}
