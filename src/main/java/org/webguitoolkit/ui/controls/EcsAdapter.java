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
package org.webguitoolkit.ui.controls;

import java.io.PrintWriter;

import org.apache.ecs.MultiPartElement;


/**
 * <pre>
 * This adapts BaseContol components to be used in ECS as Elements.
 * 
 * Usage: <code>
 * markerString = 
 *   new EcsAdapter(myComp);
 * </code>
 * </pre>
 * 
 */
public class EcsAdapter extends MultiPartElement {

	protected BaseControl adaptee;
	protected EcsAdapter currentAdapter;

	public EcsAdapter(BaseControl comp) {
		adaptee = comp;
	}

	public String createStartTag() {
		return "";
	}

	public String createEndTag() {
		return "";
	}

	public BaseControl getCurrentControl() {
		return adaptee;
	}

	public void output(PrintWriter out) {
		super.output(out);
		adaptee.drawInternal(out);
	}

}
