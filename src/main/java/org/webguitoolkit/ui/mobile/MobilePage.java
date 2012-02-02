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

import org.directwebremoting.util.Logger;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * @author i01002415
 *
 */
public abstract class MobilePage extends Page{

	private static final long serialVersionUID = 1L;
	
	protected abstract String getIcon();
	protected abstract boolean isFullScreen();
	protected abstract String getStartupImage();
	
	private static final String STORE_INITIAL = "_wgt_initial";
	public static final String CHECK_BROWSER_STORE = "checkC";

	private IMobilePageListener listener = null;
	
	@Override
	protected void pageDefaults() {
		super.pageDefaults();
		defIPad();
		
		addWgtCSS("jquery/jquery.mobile-1.0a3.css");
		addWgtJS("wgt.controller.jquerymobile.js");
	}
	
	protected void defIPad(){
//		set the view port width
		addHeaderLine("<meta name=\"viewport\" content=\"user-scalable=no, width=device-width\" />");
//		setting the link icon
		addHeaderLine("<link rel=\"apple-touch-icon\" href=\""+getIcon()+"\" />");
		if(isFullScreen()){
//			setting full screen
			addHeaderLine("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		}
//		setting the status bar to black
		addHeaderLine("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
//		setting startup image
		addHeaderLine("<link rel=\"apple-touch-startup-image\" href=\""+getStartupImage()+"\" />");
	}
	
	public WGTMobileFactory getMobileFactory(){
		return new WGTMobileFactory();
	}
	
	/**
	 * function that sets a check flag for the client to do a new call that can be handled by the IMobileListener.
	 */
	public void checkBrowserStrores(){
		getContext().add(getId()+".check", "check", "checkC", org.webguitoolkit.ui.ajax.IContext.STATUS_COMMAND);
	}
	
	public void initMobileLocalKeys( String... keys ){
		for( String key : keys )
			setMobileLocalKey( key, STORE_INITIAL );
	}
	public void initMobileSessionKeys( String... keys ){
		for( String key : keys )
			setMobileSessionKey( key, STORE_INITIAL );
	}
	public void setMobileLocalKey( String key, String value ){
		getContext().add(getId()+"."+key, value, "localC", org.webguitoolkit.ui.ajax.IContext.STATUS_EDITABLE);
	}
	public void setMobileSessionKey( String key, String value ){
		getContext().add(getId()+"."+key, value, "sessionC", org.webguitoolkit.ui.ajax.IContext.STATUS_EDITABLE);
	}
	public String getMobileLocalKey( String key ){
		return getContext().getValue(getId()+"."+key );
	}
	public String getMobileSessionKey( String key ){
		return getContext().getValue(getId()+"."+key );
	}
	
	@Override
	public void dispatch(ClientEvent event) {
		super.dispatch(event);
		if (CHECK_BROWSER_STORE.equals(event.getType())) {
			// jet nothing to do
			if( getListener() == null )
				Logger.getLogger(this.getClass()).warn("No listener defined for check loacal store");
			else
				getListener().onCheckBrowserStores();
		}
	}
	public void setListener(IMobilePageListener listener) {
		this.listener = listener;
	}
	public IMobilePageListener getListener() {
		return listener;
	}
}
