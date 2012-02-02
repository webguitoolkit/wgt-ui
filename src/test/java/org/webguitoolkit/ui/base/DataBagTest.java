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
package org.webguitoolkit.ui.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

public class DataBagTest extends TestCase {

	public final void testUndo() {

		IDataBag bag = WebGuiFactory.getInstance().createDataBag(new TestDelegate("Peter", 50));
		assertTrue(bag.get("name").equals("Peter"));
		bag.addProperty("surname", "Zaretzke");
		assertTrue(bag.get("surname").equals("Zaretzke"));

		bag.setObject("name", "Paul");
		assertTrue(bag.get("name").equals("Paul"));

		bag.setObject("surname", "Panther");
		assertTrue(bag.get("surname").equals("Panther"));

		bag.undo();
		assertTrue(bag.get("name").equals("Peter"));
		assertTrue(bag.get("surname").equals("Zaretzke"));

	}

	public final void testUndoNew() {
		IDataBag bag = WebGuiFactory.getInstance().createDataBag(null);

		// setObject is called by the framework whenever data is transfered from the GUI to the DataBag.
		bag.setObject("name", "Paul");
		assertTrue(bag.get("name").equals("Paul"));

		bag.setObject("surname", "Panther");
		assertTrue(bag.get("surname").equals("Panther"));

		bag.setDelegate(new TestDelegate());
		bag.save();

		bag.undo();
		assertTrue(bag.get("name").equals("Paul"));
		assertTrue(bag.get("surname").equals("Panther"));
	}

	public final void testSave2Delegate() {
		TestDelegate testDelegate = new TestDelegate("Peter", 50);

		IDataBag bag = WebGuiFactory.getInstance().createDataBag(testDelegate);

		bag.setObject("name", "Paul");
		assertTrue(bag.get("name").equals("Paul"));
		assertTrue(testDelegate.getName().equals("Peter"));

		bag.setObject("surname", "Panther");
		assertTrue(bag.get("surname").equals("Panther"));

		bag.save();
		assertTrue(testDelegate.getName().equals("Paul"));
		assertTrue(bag.get("surname").equals("Panther"));

	}

	public final void testSave2NewDelegate() {

		IDataBag bag = WebGuiFactory.getInstance().createDataBag(null);

		bag.setObject("name", "Paul");
		assertTrue(bag.get("name").equals("Paul"));

		TestDelegate testDelegate = new TestDelegate();
		bag.setDelegate(testDelegate);
		bag.save();

		assertTrue(testDelegate.getName().equals("Paul"));

	}


	public final void testListAccess() {
		TestDelegate testDelegate = new TestDelegate("Peter", 50);
		IDataBag bag = WebGuiFactory.getInstance().createDataBag(testDelegate);

		Object name = bag.getObject( "children[0].name" );

		assertEquals("Accessed object does not fit ", testDelegate.getName()+"1", name );
	}
	public final void testMapAccess() {
		TestDelegate testDelegate = new TestDelegate("Peter", 50);
		IDataBag bag = WebGuiFactory.getInstance().createDataBag(testDelegate);

		Object name = bag.getObject( "mapped(first).name" );

		assertEquals("Accessed object does not fit ", testDelegate.getName()+"1", name );

		name = bag.getObject( "mapped(test).name" );

		assertNull("Accessed object should not exist", name );
	}
/**
 * Test object
 * @author i102415
 */
	public class TestDelegate {
		private String name;
		private int age;

		private TestDelegate nested;
		private List children;
		private HashMap mapped;

		public TestDelegate () {
			super();
		}

		public TestDelegate(final String name, final int age) {
			this.name = name;
			this.age = age;
			nested = new TestDelegate();
			nested.setName(name + "1");
			nested.setAge( age+1 );
			mapped = new HashMap();
			mapped.put("first", nested);
			children = new ArrayList();
			children.add( nested );
		}

		public final String getName() {
			return name;
		}

		public final void setName(final String name) {
			this.name = name;
		}

		public final int getAge() {
			return age;
		}

		public final void setAge(final int age) {
			this.age = age;
		}

		public final TestDelegate getNested() {
			return nested;
		}

		public final List getChildren() {
			return children;
		}

		public final TestDelegate getMapped( final String key ) {
			return (TestDelegate) mapped.get(key);
		}
	}
}