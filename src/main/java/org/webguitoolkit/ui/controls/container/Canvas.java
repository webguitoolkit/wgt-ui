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
package org.webguitoolkit.ui.controls.container;

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.layout.ILayout;
import org.webguitoolkit.ui.controls.layout.ILayoutable;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * The Canvas is a area to place your components, it can be displayed in various ways.<br><br>
 * 
 * DISPLAY_MODE_INLINE - displayed as part of the parent component<br>
 * DISPLAY_MODE_INLINE_TRAYABLE - displayed with a border and buttons to minimise the canvas<br>
 * DISPLAY_MODE_POPUP - displayed as a simple popup<br>
 * DISPLAY_MODE_POPUP_MODAL - displayed as a simple popup, no interaction on the rest of the page<br>
 * DISPLAY_MODE_WINDOW - displayed as a window with close button<br>
 * DISPLAY_MODE_WINDOW_MODAL - displayed as a window with close button, no interaction on the rest of the page<br>
 */
public class Canvas extends BaseControl implements IComposite, ICanvas, ILayoutable {
	private static final long serialVersionUID = 1L;

	protected boolean dragable = false;
	protected String displayMode = DISPLAY_MODE_INLINE;
	protected int vPosition = 0;
	protected int hPosition = 0;
	protected int width = 300;
	protected int height = 200;
	protected int zIndex = 1000;
	protected boolean isModal = false;
	protected boolean isPopup = false;
	protected boolean isWindow = false;
	protected boolean isTrayable = false;
	protected boolean isScrollable = false;

	protected String scrollable;
	protected String heightAuto;
	// only for window canvas
	protected ICanvasWindowListener windowActionListener = new DefaultCanvasWindowListener();

	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public Canvas() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Canvas(String id) {
		super(id);
	}

	public String getId4Title() {
		return getId() + ".title";
	}

	public void startHTML(PrintWriter out) {

		if( isVisible() )
			initDiv( true );
			
        if (dragable) {
    		getContext().sendJavaScript(getId()+".drag", "draginit();");
    	}
        out.println("<div " );
        if (dragable && isPopup ) {
        	out.print("onmousedown='dragstart(this);' ");
        }
                   	
        if( isPopup||isWindow ) {
    		this.getStyle().addPostition(Style.POSITION_ABSOLUTE);
    		this.getStyle().addZIndex(zIndex);
    		this.getStyle().addTop(vPosition, Style.PIXEL);
    		this.getStyle().addLeft(hPosition, Style.PIXEL);
    		this.getStyle().addWidth(width, Style.PIXEL);
    		if(heightAuto == null){
    			this.getStyle().addHeight(height, Style.PIXEL);
    		}else{
    			this.getStyle().addHeight(heightAuto);
    		}
        }
        
        
        if( isPopup ){
        	stdParameter( out);
        	setDefaultCssClass("wgtPopup");
        	makeScrollable();
        } else if( isWindow ){
        	setDefaultCssClass("wgtPopup wgtPopupWindow");
        	stdParameter( out);
        } else if ( isTrayable ){
        	setDefaultCssClass("wgtCanvasTray");
			stdParameter(out);
        } else{
        	setDefaultCssClass("wgtCanvas");
        	stdParameter( out );
       		makeScrollable();
        }
        
        out.print(" >" );
        
		if ( isTrayable ) {
			// add div for header
//			int trayStatus = getContext().processInt(
//					getId() + "_toggTarget");
//			if(trayStatus==Integer.MIN_VALUE){
//				//default
//				trayStatus = TRAY_STATUS_MAXIMIZED;
//			}
			int trayStatus = this.getTrayStatus();
			boolean trayResizable = getContext().processBool(getId()+"_resize",false);
			boolean visible = getContext().processBool(getId()+ "_toggTarget.vis" );
			
			out.print(this.getTrayableHeader(trayStatus, trayResizable).toString());
			out.println("<div class=\"wgtCanvasTraybody\" id=\"" + getId()
					+ "_toggTarget\" ");
			if ( visible )
				out.print("style=\"display:block;\" ");
			else
				out.print("style=\"display:none;\" ");
			out.println("> ");
		}     
        
        if(isWindow){
        	
        	// write title bar
        	
        	Div div = new Div();
        	div.setClass( "wgtCanvasWindowTitelBar" );
            if (dragable )
            	div.setOnMouseDown( "dragstart(this.parentNode);" );
            
            Table table = new Table();
            table.setWidth( "100%" );
            table.setCellSpacing( 0 );
            table.setCellPadding( 0 );
            div.addElement( table );
            
            TR tr = new TR();
            table.addElement(tr);
            
            TD td = new TD();
            td.setClass( "wgtCanvasWindowTitel" );
            tr.addElement(td);
            
            String title = getContext().processValue( getId4Title() );
            if( title == null ) title = "";

            Span span = new Span();
            span.setID( getId4Title() );
            span.addElement( StringUtils.trimToEmpty(TextService.getString( title ) ) );
            td.addElement( span );
            
            td = new TD();
            td.setAlign( "right" );
            tr.addElement(td);

            A a = new A();
            td.addElement(a);
            a.setOnClick( JSUtil.jsFireEvent( getId(), EVENT_CLOSE )+"return false;" );
            
            IMG image = new IMG();
//            image.setType("image");
            image.setClass( "wgtCanvasWindowButton" );
            image.setSrc( "images/wgt/icons/close.gif" );
            image.setTitle( TextService.getString("button.close@Close") );
            a.addElement( image );
            
            div.output( out );
    		if (isScrollable){
    			String overflow = "overflow:auto;";
    			if(getScrollable() != null && getScrollable().equals(SCROLLY)){
    				overflow = "overflow-y:auto;";
    			}else if(getScrollable() != null && getScrollable().equals(SCROLLX)){
    				overflow = "overflow-x:auto;";
    			}
    			
    			out.print("<div style='"+overflow+"height:"+(height-25)+";'>");
    		}
        }
	}
	
	private void makeScrollable() {
		if (isScrollable || getScrollable() != null){
			String overflow = "overflow:auto !important;";
			if(getScrollable() != null && getScrollable().equals(SCROLLY)){
				overflow = "overflow-y:auto !important;";
			}else if(getScrollable() != null && getScrollable().equals(SCROLLX)){
				overflow = "overflow-x:auto !important;";
			}
			this.getStyle().addStyleAttributes(overflow);
		}
	}

	protected void endHTML( PrintWriter out ){

		// no shadow today, ie sucks
		// if (isWindow) {
		// out.print("<div class=\"topright\"></div>");
		// out.print("<div class=\"bottomleft\"></div>");
		// out.print("<div class=\"bottomright\"></div>");
		// out.print("</div>");
		// }
		if (isScrollable)
			out.print("</div>");
		if (isTrayable)
			out.print("</div>");
		out.print("</div>");
		out.flush();
    }

	protected Div getTrayableHeader(int trayStatus, boolean resizable) {
		Div header = new Div();
		Table table = new Table();
		TD left = new TD();
		TD right = new TD();
		IMG toggleImg = null;
		Span text = new Span();

		String title = getContext().processValue(getId4Title());
		if (title == null)
			title = "&#160;";
		text.addElement(title);

		String toggleSrc = getContext().processValue(getId()+"_trayToggle.src");
		
		toggleImg = new IMG();
		toggleImg.setID( getId()+"_trayToggle" );
		toggleImg.setSrc(toggleSrc);
		toggleImg.setClass("wgtPointerCursor");
		toggleImg.setAlt(TextService
				.getString("general.expandcollpse@Expand/Collapse"));
		toggleImg.setTitle(TextService
				.getString("general.expandcollpse@Expand/Collapse"));

		left.addElement(text);
		left.setClass("wgtCanvasTraylineTableCell");
		left.setVAlign("middle");
		table.addElement(left);

		right.setAlign("right");
		right.addElement(toggleImg);
		table.addElement(right);

		table.setClass("wgtCanvasTraylineTable");
		table.setCellPadding(0);
		table.setCellSpacing(0);

		header.addElement(table);
		header.setClass("wgtCanvasTrayline");
		if ( this.getWindowActionListener() != null ) {
//			toggleImg.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_TOGGLE));
			header.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_TOGGLE));
			header.setStyle("cursor: pointer;");
		}
		return header;
	}


	protected void init() {
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#add(org.webguitoolkit.ui.controls.BaseControl)
	 */
	public void add(IBaseControl child) {
		super.add(child);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setDisplayMode(java.lang.String)
	 */
	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;

		isModal = DISPLAY_MODE_WINDOW_MODAL.equals(displayMode)
				|| DISPLAY_MODE_POPUP_MODAL.equals(displayMode);
		isPopup = DISPLAY_MODE_POPUP.equals(displayMode)
				|| DISPLAY_MODE_POPUP_MODAL.equals(displayMode);
		isWindow = DISPLAY_MODE_WINDOW.equals(displayMode)
				|| DISPLAY_MODE_WINDOW_MODAL.equals(displayMode);
		isTrayable = DISPLAY_MODE_INLINE_TRAYABLE.equals(displayMode);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setVPosition(int)
	 */
	public void setVPosition(int vPos) {
		this.vPosition = vPos;
		this.getStyle().addTop(vPosition, Style.PIXEL);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setHPosition(int)
	 */
	public void setHPosition(int hPos) {
		this.hPosition = hPos;
		this.getStyle().addLeft(hPosition, Style.PIXEL);
	}

	public void setWidth(int width) {
		this.width = width;
		this.getStyle().addWidth(width, Style.PIXEL);
	}

	public void setHeight(int height) {
		this.height = height;
		this.getStyle().addHeight(height, Style.PIXEL);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setVisible(boolean)
	 */
	public void setVisible(boolean vis) {
		super.setVisible(vis);
		initDiv( vis );
	}
	
	protected void initDiv( boolean vis ){
		if (vis && vPosition == 0 && hPosition == 0 && (isPopup || isWindow)) {
			getContext().sendJavaScript(getId() + ".centerDiv",
					"centerDiv('" + getId() + "')");
		}
		if (isModal) {
			getPage().disableZone(vis, getId() );
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#isDragable()
	 */
	public boolean isDragable() {
		return dragable;
	}
	
	/**
	 * get status of try on trayable canvas
	 * 
	 * @return
	 */
	public int getTrayStatus() {
		int trayStatus = getContext().processInt(
				getId() + "_toggTarget");
		if(trayStatus==Integer.MIN_VALUE){
			//default
			trayStatus = TRAY_STATUS_MAXIMIZED;
		}
		return trayStatus;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setDragable(boolean)
	 */
	public void setDragable(boolean dragable) {
		this.dragable = dragable;
	}

	/**
	 * Use this parameter to set the default behavior of a trayable canvas
	 * (displayMode = DISPLAY_MODE_INLINE_TRAYABLE). Canvas.TRAY_STATUS_MAXIMIZED is default. No
	 * effect on other displayModes.
	 * 
	 * @param vis
	 *            the expanded to set
	 */
	public void setTrayStatus(int newTrayStatus) {
		getContext().add(getId()+"_toggTarget",String.valueOf(newTrayStatus),IContext.TYPE_TXT,IContext.STATUS_SERVER_ONLY);
		if( newTrayStatus == TRAY_STATUS_MAXIMIZED ){
			getContext().setVisible(getId()+"_toggTarget", true);
			getContext().setAttribute(getId()+"_trayToggle", "src", "images/wgt/icons/closetray.gif");
		}
		else if ( newTrayStatus == TRAY_STATUS_MINIMIZED ){
			getContext().setVisible(getId()+"_toggTarget", false);
			getContext().setAttribute(getId()+"_trayToggle", "src", "images/wgt/icons/opentray.gif");
		}
	}
	
	/**
	 * Use this parameter to set trayable canvas resizable. Not effect on 
	 * other display modes. This function has no default behaviour, the 
	 * listener method must be implemented
	 * 
	 * @param newResizable
	 */
	public void setTrayResizable(boolean newResizable) {
		getContext().add(getId()+"_resize",String.valueOf(newResizable),IContext.TYPE_TXT,IContext.STATUS_NOT_EDITABLE);
	}

	public void dispatch(ClientEvent event) {

		switch (event.getTypeAsInt()) {
		case EVENT_CLOSE:
			getWindowActionListener().onClose(event);
			break;
		case EVENT_TOGGLE:
			int trayStatus = getTrayStatus();
			if( trayStatus == TRAY_STATUS_MAXIMIZED )
				getWindowActionListener().onMinimize(event);
			if( trayStatus == TRAY_STATUS_MINIMIZED )
				getWindowActionListener().onMaximize(event);
			break;
		}
	}

	public ICanvasWindowListener getWindowActionListener() {
		return windowActionListener;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setWindowActionListener(org.webguitoolkit.ui.controls.container.ICanvasWindowListener)
	 */
	public void setWindowActionListener(
			ICanvasWindowListener windowActionListener) {
		this.windowActionListener = windowActionListener;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		getContext().add(getId4Title(), title, IContext.TYPE_TXT,
				IContext.STATUS_NOT_EDITABLE);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setTitleKey(java.lang.String)
	 */
	public void setTitleKey(String titleKey) {
		getContext().add(getId4Title(), TextService.getString(titleKey),
				IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
	}

	/**
	 * the ZIndex is the z-index of the component on the HTML page, 0 stands for
	 * background
	 * 
	 * @return
	 */
	public int getZIndex() {
		return Integer.parseInt( this.getStyle().getAttributeByType( Style.Z_INDEX ).getAttributeValue() );
	}

	public void setZIndex(int index) {
		this.zIndex = index;
		this.getStyle().addZIndex(zIndex);
	}
	public void drawCanvas(PrintWriter out){
		super.drawInternal(out);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#isScrollable()
	 */
	public boolean isScrollable() {
		return isScrollable;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setScrollable(boolean)
	 */
	public void setScrollable(boolean isScrollable) {
		this.isScrollable = isScrollable;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#getScrollable()
	 */
	public String getScrollable() {
		return scrollable;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.container.ICanvas#setScrollable(java.lang.String)
	 */
	public void setScrollable(String scrollable) {
		this.scrollable = scrollable;
	}

	public String getHeightAuto() {
		return heightAuto;
	}

	public void setHeight(String height) {
		this.heightAuto = height;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.ILayoutable#setLayoutManager(org.webguitoolkit.ui.controls.ILayoutManager)
	 */
	public void setLayout(ILayout manager) {
		super.setLayout( manager );
	}


	public void setAttribute(String attributeName, String attributeValue) {
		super.setAttribute(attributeName, attributeValue);
	}
	

}
/**
 * Default implementation for a Window listener. Does not handle resize.
 *
 */
class DefaultCanvasWindowListener implements ICanvasWindowListener {
	private static final long serialVersionUID = 1L;

	public void onClose(ClientEvent event) {
		event.getSource().setVisible(false);
	}
	public void onMinimize(ClientEvent event) {
		((Canvas) event.getSource()).setTrayStatus(Canvas.TRAY_STATUS_MINIMIZED);
	}
	
	public void onMaximize(ClientEvent event) {
		((Canvas) event.getSource()).setTrayStatus(Canvas.TRAY_STATUS_MAXIMIZED);
	}
}
