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
package org.webguitoolkit.ui.controls.layout;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import org.webguitoolkit.ui.controls.BaseControl;

/**
 * Interface for layout managers.
 * 
 * @author Peter
 * 
 */
public interface ILayout extends Serializable {
	/**
	 * the draw function has to be implemented by the layout manager, it is called by the framework
	 * 
	 * @param controls the controls to draw
	 * @param out the writer to draw the controls to
	 */
	void draw(List<BaseControl> controls, PrintWriter out);
}
