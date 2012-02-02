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

import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * @author i01002415
 *
 */
public class MobileCanvas extends Canvas implements IMobileControl {

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
	
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#draw(java.io.PrintWriter)
	 */
	protected void draw(PrintWriter out) {
		super.draw(out);
		String dataRole = getContext().getValue( getId()+".data-role");
		if( getPage().isDrawn() && "page".equals( dataRole) ){
			getContext().sendJavaScript(getId()+"_init", "jQuery.mobile.initializePage();");
		}
		else{
			getContext().sendJavaScript(getId()+"_init", JSUtil.jQuery(getId())+".page();");
		}
	}

}
