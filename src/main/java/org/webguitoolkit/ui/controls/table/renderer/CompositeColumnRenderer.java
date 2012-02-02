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
package org.webguitoolkit.ui.controls.table.renderer;

import java.util.ArrayList;

import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.TableColumn;


/**
 * <pre>
 * we delegate the rendering to the children. This is used for mainly for
 * FormControl inside the tablecolumn tag. (editable tables).
 * 
 * This renderer must initialise the children (input fields) so that they use
 * the table's compound.
 * </pre>
 * 
 * @author arno@schatz.to
 * 
 */
public class CompositeColumnRenderer extends ChildColumnRenderer implements IColumnRenderer {
	
	public CompositeColumnRenderer() {
		super();
	}	
	
	protected FormControl[] getFormControls() {
		final ArrayList columnForms = new ArrayList();
		((TableColumn)tableColumn).travelBFS(new BaseControl.Visitor() {
			public boolean visit(BaseControl host) {
				if (host instanceof FormControl) 
					columnForms.add(host);
				return true;
			}
		});
		return (FormControl[]) columnForms.toArray(new FormControl[columnForms.size()]);
	}


}
