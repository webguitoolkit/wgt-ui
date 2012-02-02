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
package org.webguitoolkit.ui.controls.form.button;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.util.json.JSONObject;

/**
 * @author i102415
 *
 */
public class JQueryButton extends Button implements IButton {

	private static final String CONTEXT_TYPE_INIT ="buttonI";
	private static final String CONTEXT_TYPE_OPTION ="buttonO";
	private static final String CONTEXT_TYPE_ENABLE ="buttonE";

	public static final String DISPLAY_MODE_BUTTON ="DISPLAY_MODE_BUTTON";
	public static final String DISPLAY_MODE_LINK ="DISPLAY_MODE_LINK";
	public static final String DISPLAY_MODE_INPUT ="DISPLAY_MODE_INPUT";
	
	public String displayMode = DISPLAY_MODE_BUTTON;
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#endHTML(java.io.PrintWriter, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3d) {
		
		if( DISPLAY_MODE_BUTTON.equals(displayMode)){
			org.apache.ecs.html.Button button = new org.apache.ecs.html.Button();
			stdParameter(button);
			button.addElement( text );
			button.output( out );
		}
		else if( DISPLAY_MODE_LINK.equals(displayMode)){
			org.apache.ecs.html.A button = new org.apache.ecs.html.A();
			stdParameter(button);
			button.addElement( text );
			button.setHref("#");
			button.output( out );
		}
		else if( DISPLAY_MODE_INPUT.equals(displayMode)){
			org.apache.ecs.html.Input button = new org.apache.ecs.html.Input();
			stdParameter(button);
			button.setValue( text );
			button.output( out );
		}
		else{
			throw new RuntimeException("No valid displayMode: "+ displayMode );
		}
		
		getContext().add(getId(), getInitOptions(), CONTEXT_TYPE_INIT, IContext.STATUS_COMMAND);
	}

	private String getInitOptions(){
		JSONObject object = new JSONObject();
		if( getSrc() != null ){
			JSONObject icons = new JSONObject();
			icons.addAttribute("primary", getSrc() );
			object.addAttribute("icons", icons);
		}
		if( StringUtils.isEmpty( getLabel() ) )
			object.addAttribute("text", false);
		return object.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#isDisabled()
	 */
	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#setDisabled(boolean)
	 */
	@Override
	public void setDisabled(boolean disabled) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.IButton#setAlignment(int)
	 */
	public void setAlignment(int pos) {
		// TODO Auto-generated method stub
		
	}

	protected void init() {
		getPage().addWgtCSS("standard/jquery-ui.css");
		getPage().addControllerJS("button.js");

	}

}
