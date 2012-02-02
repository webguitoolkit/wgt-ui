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
import java.util.List;

import org.webguitoolkit.ui.controls.AbstractLayout;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.form.IButtonBar;

/**
 * @author i102415
 * 
 */
public class CompoundButtonBarLayout extends AbstractLayout {

	private final ILayout delegate;

	public CompoundButtonBarLayout(ILayout delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayout#draw(java.util.List, java.io.PrintWriter)
	 */
	public void draw(List<BaseControl> controls, PrintWriter out) {
		if (controls != null && !controls.isEmpty() && controls.get(0) instanceof IButtonBar) {
			draw(((BaseControl)controls.get(0)), out);
			delegate.draw(controls.subList(1, controls.size()), out);
		}
		else
			delegate.draw(controls, out);
		// TODO Auto-generated method stub

	}
}
