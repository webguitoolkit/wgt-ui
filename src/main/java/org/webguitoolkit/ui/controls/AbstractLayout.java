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
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.ecs.html.Div;
import org.apache.ecs.html.TD;
import org.webguitoolkit.ui.controls.layout.ILayout;
import org.webguitoolkit.ui.html.HTMLTag;

/**
 * @author i102415
 * 
 */
public abstract class AbstractLayout implements ILayout {

	protected void drawCell(TD cell, List controls) {
		for (Iterator it = controls.iterator(); it.hasNext();) {
			BaseControl child = (BaseControl)it.next();
			drawCell(cell, child);
		}
	}

	protected void drawCell(HTMLTag cell, BaseControl control) {
		StringWriter writer = new StringWriter();
		control.drawInternal(new PrintWriter(writer));
		cell.setContent(writer.getBuffer().toString());
	}
	protected void drawCell(TD cell, BaseControl control) {
		StringWriter writer = new StringWriter();
		control.drawInternal(new PrintWriter(writer));
		cell.addElement(writer.getBuffer().toString());
	}

	protected void drawCell(Div div, List controls) {
		for (Iterator it = controls.iterator(); it.hasNext();) {
			BaseControl child = (BaseControl)it.next();
			drawCell(div, child);
		}
	}

	protected void drawCell(Div div, BaseControl control) {
		StringWriter writer = new StringWriter();
		control.drawInternal(new PrintWriter(writer));
		div.addElement(writer.getBuffer().toString());
	}

	protected void draw(BaseControl control, PrintWriter out) {
		control.drawInternal(out);
	}

}
