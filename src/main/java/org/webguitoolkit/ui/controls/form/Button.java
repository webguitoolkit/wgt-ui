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
/*
 */
package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <b>A button is an input element that fires an action when pressed.</b>
 * <p>
 * When a src - attribute is given, we assume you want to have a click-able image.
 * </p>
 * 
 * @author Arno
 * @author Martin
 */
public abstract class Button extends FormControl implements IButton {



	protected String linkURL;
	protected String target;
	
	//disable bubbling in table
	protected boolean cancelBubble= false;

	// render as 3D Button
	protected Boolean displayMode3D;

	// confirm message for the button e.g. "Are you sure?"
	protected String confirmMsg;
	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public Button() {
        super();
    }
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Button(String id) {
		super(id);
	}

	/**
     * output HTML for button
     */
	protected void endHTML(PrintWriter out) {
   	
    	boolean displMode3D = false;
	    if( displayMode3D != null ){
	    	displMode3D = displayMode3D.booleanValue();
	    }
	    else{
	    	if( getParent() instanceof ButtonBar)
	    		displMode3D = ((ButtonBar)getParent()).getDisplayMode3D();
	    	else
	    		displMode3D=false;
	    }

    	String stylePostFix = "";
    	if( displMode3D )
    		stylePostFix = "3D";
    
        // remove context item
        String src = getContext().processValue( id4ImageSrc() );
        String text = getContext().processValue( id4Text() );

        endHTML(out, src, text, displMode3D );
    }

	/**
	 * Generation of the HTML output is in every implementation of the button different.
	 * 
	 * @param out the stream to write to
	 * @param imgSrc the images source or null
	 * @param text the text or null
	 * @param mode3D if the button is rendered in 3D mode
	 */
	protected abstract void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D );
	
    /**
     * function for dynamically changing the icon of the button 
     * @deprecated use setSrc
     */
    public void changeIcon( String url ){
    	setSrc(url);
    }
    /**
     * function for dynamically changing the buttons appearance.
     * This is a redraw
     * @deprecated use setSrc and setText
     */
    public void changeDisplay( String iconUrl, String labelKey )
    {
		if( iconUrl != null ){
			setSrc(iconUrl);
 		}
		else{
			setLabelKey(labelKey);
		}
    }
    
    public String getSrc() {
    	return getContext().getValue( id4ImageSrc() );
    }

    /**
     * inherited from FormControl, makes nothing with button
     */
    public void loadFrom(Object data) {
    }
    
    /**
     * set the path to the image
     */
    public void setSrc(String src) {
    	getContext().add( id4ImageSrc(), src, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE );
    }
	/** 
	 * doing nothing here (needed to implement because of hierarchy)
	 */
    public void saveTo(Object dataObject) {
    }
    
    /**
     * inherited from FormControl, returns null
     */
	public String getValue() {
		return null;
	}
	
	/**
	 * if disabled the image of the button changes to the <image>_disabled.<type>
	 * 
	 * That means, if you want to use this function you have to provide a additional "_disabled" image.
	 */
	public abstract void setDisabled(boolean disabled );
	
	/**
	 * return the state of disabled of the button.
	 */
	public abstract boolean isDisabled();
	
	/**
	 * sets the visibility of the button
	 */
    public void setVisible(boolean vis) {
    	 getContext().makeVisible(getId(), vis);
    }
    public boolean isVisible() { // visibility is true unless set to false
        return getContext().getValueAsBool(getId()+IContext.DOT_VIS, true);
    }
	
	/**
	 * not sending an editable value to the client..
	 */
	protected void init() {
	}
	/**
	 * button do not have read-only modes.
	 */
	public void changeMode(int mode) {
	}
	
	/**
	 * button do not have error modes.
	 */
	public void clearError() {
	}
	
	/**
	 * button do not have error modes.
	 */
	public void showError() {
	}
	
	/**
	 * the button can be rendered with a 3D border that changes on mouse over 
	 * (Like buttons in windows applications)
	 * 
	 * @param displayMode3D
	 */
	public void setDisplayMode3D( boolean displayMode3D ){
		this.displayMode3D = new Boolean( displayMode3D );
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}
	/**
	 * sets the key for the confirm message of the button, if no value is set there is no confirm message
	 */
	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}
	public String getLabel() {
		return getContext().getValue( id4Text() );
	}
	public void setLabel(String label) {
    	getContext().add( id4Text(), label, IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE );
	}
	public void setLabelKey(String labelKey) {
		setLabel( TextService.getString( labelKey ) );
	}
	public boolean isCancelBubble() {
		return cancelBubble;
	}
	/**
	 * @deprecated use setEventBubbling() instead
	 * @param cancelBubble
	 */
	public void setCancelBubble(boolean cancelBubble) {
		this.cancelBubble = cancelBubble;
	}

	public void setEventBubbling(boolean eventBubble) {
		this.cancelBubble = !eventBubble;
	}
	
	protected String id4Text() {
		return getId()+".text";
	}
	protected String id4ImageSrc() {
		return getId()+".icon.src";
	}
	protected String id4Image() {
		return getId()+".icon";
	}
	
	/**
	 * get url
	 * @return
	 */
	public String getLinkURL() {
		return linkURL;
	}
	
	/**
	 * a target url for the button - if this is set, the button creates a simple link, no server roundtrip
	 * @param url
	 */
	public void setLinkURL(String url) {
		this.linkURL = url;
	}
	
	/**
	 * get the target for link
	 * @return
	 */
	public String getTarget() {
		return target;
	}
	
	/**
	 * set target name (only relevant for buttons with url used for links)
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	protected boolean isSimpleLink() {
		return StringUtils.isNotBlank(getLinkURL());
	}
}
