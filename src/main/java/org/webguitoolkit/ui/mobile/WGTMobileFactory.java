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
package org.webguitoolkit.ui.mobile;

import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.list.IList;

/**
 * @author i01002415
 *
 */
public class WGTMobileFactory {
	public IList createList( IComposite parent ){
		return createList(parent, "listview", "true", "c" , "b");
	}
	public IList createList( IComposite parent, String dataRole, String dataInset, String dataTheme, String dataDividertheme ){
		MobileList list = new MobileList();
		parent.add(list);
		list.setDataRole(dataRole);
		list.setDataTheme(dataTheme);
		list.setDataInset(dataInset);
		list.setDataDividertheme(dataDividertheme);
		return list;
	}
	public ICanvas createCanvas( IComposite parent ){
		MobileCanvas canvas = new MobileCanvas();
		parent.add(canvas);
		return canvas;
	}
	public ICanvas createCanvas( IComposite parent, String dataRole, String dataInset, String dataTheme, String dataDividertheme ){
		MobileCanvas canvas = new MobileCanvas();
		parent.add(canvas);
		canvas.setDataRole(dataRole);
		canvas.setDataTheme(dataTheme);
		canvas.setDataInset(dataInset);
		canvas.setDataDividertheme(dataDividertheme);
		return canvas;
	}
}
