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
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.http.ResourceServlet;


/**
 * @author bkl
 * 
 * add addWgtJS("glossbutton.js"); for page required!
 *
 */
public class GlossButton extends Button implements IButton {

	protected int iconWidth = -1;

	protected int iconHeight = -1;
	private int displayMode = 0;
	private boolean block = false;

	public static final int DISPLAY_MODE_HORIZONTAL = 0; // default
	public static final int DISPLAY_MODE_VERTICAL = 1;

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public GlossButton() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public GlossButton(String id) {
		super(id);
	}
	
	protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D) {
		Span span = new Span();
		Table table = new Table();
		TR top = new TR();
		TD topLeft = new TD();
		TD topCenter = new TD();
		TD topRight = new TD();
		TR middle = new TR();
		TD midLeft = new TD();
		TD midCenter = new TD();
		TD midRight = new TD();
		TR bottom = new TR();
		TD botLeft = new TD();
		TD botCenter = new TD();
		TD botRight = new TD();
		Span textContainer = new Span();
		org.apache.ecs.html.Button button = new org.apache.ecs.html.Button();
		boolean disabled = getContext().processBool(getId() + ".disabled");

		// build table
		table.addElement(top);
		top.addElement(topLeft);
		top.addElement(topCenter);
		top.addElement(topRight);

		table.addElement(middle);
		middle.addElement(midLeft);
		middle.addElement(midCenter);
		middle.addElement(midRight);

		table.addElement(bottom);
		bottom.addElement(botLeft);
		bottom.addElement(botCenter);
		bottom.addElement(botRight);

		// set pixels
		IMG spacer  = new IMG("./images/wgt/1.gif");
		spacer.setWidth(1);
		spacer.setHeight(1);
		topLeft.addElement(new IMG("./images/wgt/1.gif"));
		topCenter.addElement(new IMG("./images/wgt/1.gif"));
		topRight.addElement(new IMG("./images/wgt/1.gif"));
		midLeft.addElement(new IMG("./images/wgt/1.gif"));
		midRight.addElement(new IMG("./images/wgt/1.gif"));
		botLeft.addElement(new IMG("./images/wgt/1.gif"));
		botCenter.addElement(new IMG("./images/wgt/1.gif"));
		botRight.addElement(new IMG("./images/wgt/1.gif"));

		// format table
		table.setCellPadding(0);
		table.setCellSpacing(0);
		table.setBorder(0);

		// set classes
		topLeft.setClass("btn_lt");
		topCenter.setClass("btn_ct");
		topRight.setClass("btn_rt");
		midLeft.setClass("btn_lm");
		midCenter.setClass("btn_cm");
		midRight.setClass("btn_rm");
		botLeft.setClass("btn_lb");
		botCenter.setClass("btn_cb");
		botRight.setClass("btn_rb");

		button.setClass("btn_btn");
		button.setID( id4Image() );
		midCenter.addElement( button );
		textContainer.setID( id4Text() );
		textContainer.setClass("btn_txt");

		if (StringUtils.isNotEmpty( text )) {
			String textStyle = getContext().processValue(id4Text()+".style");
			textContainer.setStyle( textStyle );
			textContainer.addElement( text );
			button.addElement(textContainer);
		}
		// image is linked in style
		button.setStyle( imgSrc );
		
		if (this.hasStyle()) {
			table.setStyle(this.getStyleAsString());
		}

		midCenter.setID( id4CenterTD() );
		button.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)
				+ " return false;");
		
        // action goes to onClick handler
		if (getConfirmMsg()!=null) {
			button.setOnClick(JSUtil.jsConfirm(TextService.getString(getConfirmMsg()), 
					JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION)) ); 
		} else {
			button.setOnClick(JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION ) + " return false;");
		}
		
		//add tabindex to html element if the tabindex is greater than 0
        if(tabindex >= 0){
        	button.addAttribute(TABINDEX, Integer.valueOf(tabindex).toString());
        }
		//table.output(out);
		span.addElement(table);
		span.setID( getId() );
		if(block && isVisible()){
			span.setStyle("display:block;");
		}
		
		span.output(out);

		this.initJaveScript();
	}
	public void setDisabled(boolean disabled) {
		IContext ctx = getContext();
		// if button is a input with no source this would be all
		String dis = disabled ? "true" : null;
		ctx.setAttribute( getId(), "disabled", dis);
		String btnsytlye = disabled ? "btn_cm_dis" : "btn_cm";
		ctx.setAttribute( id4CenterTD(), "class", btnsytlye);
		if( StringUtils.isNotEmpty( getLabel() ) ){
			String btnTextSytlye = disabled ? "btn_txt_dis" : "btn_txt";
			ctx.setAttribute( id4Text(), "class", btnTextSytlye);
			this.initJaveScript();
		}
	}

	public void setActive(boolean active) {
		IContext ctx = getContext();
		// if button is a input with no source this would be all
		String act = active ? "true" : null;
		ctx.setAttribute(id4CenterTD(), "active", act);
		String btnsytlye = active ? "btn_cm_act" : "btn_cm";
		ctx.setAttribute(id4CenterTD(), "class", btnsytlye);
		this.initJaveScript();
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#init()
	 */
	protected void init() {
		super.init();
		getPage().addHeaderJS( ResourceServlet.SERVLET_URL_PATTERN + "/glossbutton.js");
	}

	public void initJaveScript() {
//		String js = "function initGlossButton(){"
//				+ "jQuery('#"+getId()+"').mouseover( function(){"
//				+ "jQuery(this).addClass('btn_cm_ov');" + "});"
//				+ "jQuery('#"+getId()+"').mouseout( function(){"
//				+ "jQuery(this).removeClass('btn_cm_ov');"
//				+ "jQuery(this).removeClass('btn_cm_cl');" + "});"
//				+ "jQuery('#"+getId()+"').bind('mousedown', function(){"
//				+ "jQuery(this).removeClass('btn_cm_ov');"
//				+ "jQuery(this).addClass('btn_cm_cl');" + "});"
//				+ "jQuery('#"+getId()+"').bind('mouseup', function(){"
//				+ "jQuery(this).addClass('btn_cm_ov');"
//				+ "jQuery(this).removeClass('btn_cm_cl');" + "});" + "}";
//		getContext().sendJavaScript("initGlossButton_js", js + ";initGlossButton();");
		getContext().sendJavaScript("initGlossButton_js", "initGlossButton('"+ getId()+"');");

	}
	
    public void setVisible(boolean vis) {
    	super.setVisible( vis );
        this.initJaveScript();
    }


	/**
	 * return the state of disabled of the button.
	 */
	public boolean isDisabled() {
		return getContext().getValueAsBool( getId() + ".disabled", false );
	}

	public void dispatch(ClientEvent event) {
		if ( !this.isDisabled() )
			super.dispatch(event);
	}

	/**
	 * set the path to the image and specify the offset for the label
	 */
	public void setSrc(String src, int iconWidth, int iconHeigth) {
		setSrc(src);
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeigth;
	}

    public String getSrc() {
    	String src = "";
    	// calculate from current style
    	String style = getContext().getValue( id4ImageSrc() );
    	if( StringUtils.isNotEmpty( style ) ){
        	int start = style.indexOf("background: transparent url(");
        	if( start > 0 ){
        		start = start + "background: transparent url(".length();
            	int end = style.indexOf(")");
            	src = style.substring( start, end ).trim();
        	}
    	}
    	return src;
    }
    
    /**
     * set the path to the image
     */
    public void setSrc(String src) {
    	setDisplay(src, getLabel() );
    }

    public void setLabel( String text ) {
    	super.setLabel( text );
    	setDisplay( getSrc(), text );
    }


    /**
     * calculate styles
     * @param image
     * @param text
     */
    private void setDisplay( String image, String text ){
    	String buttonStyle = "";
    	String textStyle = "";
    	if (StringUtils.isNotEmpty(image)) {
    		if ( StringUtils.isEmpty(text) ) {
    			buttonStyle = "width:" + iconWidth + "px;height:"
    					+ iconHeight + "px;background: transparent url("
    					+ image
    					+ ") no-repeat; background-position: center;";
    		} else {
    			buttonStyle = "background: transparent url(" + image
    					+ ") no-repeat;";
    		}
    	}

    	if (StringUtils.isNotEmpty( text )) {
    		if (StringUtils.isNotEmpty( image ) && iconWidth > -1) {
    			if (displayMode == DISPLAY_MODE_VERTICAL) {
    				// image top
    				buttonStyle = buttonStyle + "background-position: center top;padding-top:"
    						+ String.valueOf(iconHeight) + "px;";
    			} else {
    				// image left
    				textStyle = "padding-left: " + String.valueOf(iconWidth) + "px;";
    		    	getContext().add( id4Text()+".style", textStyle, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE );
    			}
    		}
    	}
    	getContext().add( id4ImageSrc(), buttonStyle, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE );
    }
    
	protected String id4CenterTD(){
		return getId()+".centerTD";
	}
	
	protected String id4ImageSrc() {
		return getId()+".icon.style";
	}
	
	/*
	 * Getters and setter
	 */

	/**
	 * @return the displayMode
	 */
	public int getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode
	 *            the displayMode to set
	 */
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}
	
	/**
	 * Change default behavior from display:inline to display:block
	 * 
	 * @param block
	 */
	public void setDisplayBlock(boolean block) {
		this.block = block;
	}
	
	public void setAlignment(int pos) {
		// NOTHING TO DO HERE
	}

}
