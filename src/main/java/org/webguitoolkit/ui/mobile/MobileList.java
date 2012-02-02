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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.ecs.html.LI;
import org.apache.ecs.html.UL;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.list.SimpleList;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * @author i01002415
 *
 */
public class MobileList extends SimpleList implements IMobileControl {
	
	public MobileList(){
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#draw(java.io.PrintWriter)
	 */
	protected void draw(PrintWriter out) {
		UL ul = new UL();
		stdParameter(ul);
		int i = 0;
		for (Iterator<BaseControl> it = getChildren().iterator(); it.hasNext();) {
			BaseControl control = it.next();
			LI li = new LI();
			li.setClass(liCSS);
			String ident = getId() + "_" + i;
			li.setID(ident);
			ul.addElement(li);
			if(liAttributs.containsKey(control)){
				HashMap<String, String> hashMap = liAttributs.get(control);
				for( String key : hashMap.keySet())
					li.addAttribute(key, hashMap.get(key));
			}
			li.addElement(new EcsAdapter(control));
			i++;
		}
		ul.output(out); // write the stream
		if( getPage().isDrawn() ){
			getContext().sendJavaScript(getId()+"_init", JSUtil.jQuery(getId()) + ".listview();");
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.mobile.IMobileControl#setDataRole(java.lang.String)
	 */
	public void setDataRole(String role) {
		setAttribute("data-role", role);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.mobile.IMobileControl#setDataInset(java.lang.String)
	 */
	public void setDataInset(String role) {
		setAttribute("data-inset", role);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.mobile.IMobileControl#setDataTheme(java.lang.String)
	 */
	public void setDataTheme(String role) {
		setAttribute("data-theme", role);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.mobile.IMobileControl#setDataDividertheme(java.lang.String)
	 */
	public void setDataDividertheme(String role) {
		setAttribute("data-dividertheme", role);
	}

}
