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

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.layout.ILayoutable;

/**
 * <h1>Interface for a Canvas</h1>
 * <p>
 * A Canvas is a container where you can "pin" other controls on. A canvas can have different display modes to control
 * how the canvas is rendered into the page. DISPLAY_MODE_INLINE - displayed as part of the parent component<br>
 * DISPLAY_MODE_INLINE_TRAYABLE - displayed with a border and buttons to minimize the canvas<br>
 * DISPLAY_MODE_POPUP - displayed as a simple popup<br>
 * DISPLAY_MODE_POPUP_MODAL - displayed as a simple popup, no interaction on the rest of the page<br>
 * DISPLAY_MODE_WINDOW - displayed as a window with close button<br>
 * DISPLAY_MODE_WINDOW_MODAL - displayed as a window with close button, no interaction on the rest of the page<br>
 * </p>
 * <b>Creation of a Canvas</b><br>
 * <pre>
 * 	ICanvas canvas = factory.createCanvas( layout);
 * 	//canvas is displayed as a window with close button
 * 	canvas.setDisplayMode(Canvas.DISPLAY_MODE_WINDOW);
 * 	//canvas can be dragged within the browser 
 * 	canvas.setDragable(true);
 * 	//the canvas is scrollable means if the content is larger than the canvas height scrollbars are displayed  
 * 	canvas.setScrollable(true);
 * 	//sets a title in the canvas headline
 * 	canvas.setTitleKey("I am a Canvas Title");
 * 	//add an ICanvasWindowListener to the canvas for the following events onClose,onMinimize, onMaximize and onResize
 * 	canvas.setWindowActionListener(new CanvasWindowListener());
 * 	//add text field to canvas
 * 	IText firstnameField = factory.createText(canvas, "firstName" )
 *
 * 	//the action listener code
 * 	class CanvasWindowListener implements ICanvasWindowListener{
 *   	public void onClose(ClientEvent event){
 *     		//Do Something
 *   	}
 *   	public void onMinimize(ClientEvent event){
 *     		//Do Something
 *   	}
 *   	public void onMaximize(ClientEvent event){
 *     		//Do Something
 *   	}
 *   	public void onResize(ClientEvent event){
 *     		//Do Something
 *   	}
 * 	}
 * 
 *	//or with manually defined id "Canvas1"
 * 	ICanvas canvas1 = factory.createCanvas( layout, "Canvas1" );
 * </pre>

 * <p>
 * Used CSS classes : wgtCanvasTraybody,wgtCanvasWindowTitelBar, wgtCanvasWindowTitel, wgtCanvasWindowButton, wgtPointerCursor,
 * wgtCanvasTraylineTable, wgtCanvasTrayline
 * </p>
 * @author Peter
 * @author Lars
 * 
 */
public interface ICanvas extends IBaseControl, IComposite, ILayoutable {

	String DISPLAY_MODE_INLINE = "DISPLAY_MODE_INLINE";
	String DISPLAY_MODE_INLINE_TRAYABLE = "DISPLAY_MODE_INLINE_TRAYABLE";
	// frame
	String DISPLAY_MODE_POPUP = "DISPLAY_MODE_POPUP";
	String DISPLAY_MODE_POPUP_MODAL = "DISPLAY_MODE_POPUP_MODAL";
	String DISPLAY_MODE_WINDOW = "DISPLAY_MODE_WINDOW";
	String DISPLAY_MODE_WINDOW_MODAL = "DISPLAY_MODE_WINDOW_MODAL";
	int EVENT_CLOSE = 0;
	int EVENT_TOGGLE = 1;
	int TRAY_STATUS_MINIMIZED = 1;
	int TRAY_STATUS_MAXIMIZED = 2;
	String SCROLLX = "overflow-x";
	String SCROLLY = "overflow-y";

	/**
	 * 
	 * @param displayMode
	 *            use one of our constants
	 */
	void setDisplayMode(String displayMode);

	/**
	 * @return true if dragable
	 */
	boolean isDragable();

	/**
	 * @param dragable
	 *            true = can be dragged 
	 */
	void setDragable(boolean dragable);

	/**
	 * There is always the default listener assigned unless you set another one.
	 * 
	 * @param windowActionListener
	 *            must not be null.
	 */
	void setWindowActionListener(ICanvasWindowListener windowActionListener);

	/**
	 * runtime method to change title
	 * 
	 * @param title
	 *            the text
	 */
	void setTitle(String title);

	/**
	 * runtime method to change title by key
	 * 
	 * @param titleKey
	 *            the resource key for the title
	 */
	void setTitleKey(String titleKey);

	/**
	 * 
	 * @return true if you can scroll
	 */
	boolean isScrollable();

	/**
	 * Control the scrolling
	 * 
	 * @param isScrollable
	 *            true enables scrolling
	 */
	void setScrollable(boolean isScrollable);

	/**
	 * @return the current ActionListener that handles the windows events
	 */
	ICanvasWindowListener getWindowActionListener();

	/**
	 * @param trayStatusMinimized
	 *            true = is minimized
	 */
	void setTrayStatus(int trayStatusMinimized);

	/**
	 * @return true if minimized
	 */
	int getTrayStatus();
	
	/**
	 * removes this component from the tree of components. It also remove all subcomponents. After this operation all handles to this component should
	 * be released (from the framework) and the objects are subject to GC.
	 * 
	 */
	void remove();

	/**
	 * sets the height of the canvas, especially for required for Dialogs. Use CSS classes if possible
	 * 
	 * @param height
	 *            height in pixels
	 */
	void setHeight(int height);

	/**
	 * sets the horizontal position of the canvas, especially for required for Dialogs. Use CSS classes if possible
	 * 
	 * @param hPos
	 *            horizontal position in pixels
	 */
	void setHPosition(int hPos);
	
	/**
	 * sets the vertical position of the canvas, especially for required for Dialogs. Use CSS classes if possible
	 * 
	 * @param vPos
	 *            vertical position in pixels
	 */
	void setVPosition(int vPos);

	/**
	 * sets the width of the canvas, especially for required for Dialogs. Use CSS classes if possible
	 * 
	 * @param width
	 *            width in pixels
	 */
	void setWidth(int width);

}