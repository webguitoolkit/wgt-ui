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
package org.webguitoolkit.ui.controls;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.container.ICanvasWindowListener;
import org.webguitoolkit.ui.controls.dialog.DynamicDialog;


/**
 * <p>
 * This is the base for dynamic components that a user can implement in order to
 * create a (standard) function for his application (we call it a view). It is
 * designed for a standard life cycle that view is being created when the user
 * navigates in his application (e.g. click a button), i.e. he causes an event
 * that will be passed to the onAction() method.
 * </p>
 * <p>
 * Within the createControls() method the programmer has to create the GUI
 * component (or take them from somewhere). This component tree has to be added
 * to the viewConnector of this class that is by default a canvas. A very simple
 * view could look like this:
 * </p>
 * 
 * <pre>
 * void  createControls( WebGuiFactory factory, Canvas viewConnector ) {
 *   TableLayout layout = factory.newTableLayout(viewConnector);
 *   factory.newLabel(layout, "My label");
 * }
 * </pre>
 * 
 * <p>
 * It just creates a Label and attaches it to the viewConnector. <br>
 * The viewConnector is the canvas of the popup dialog that is displayed. 
 * The "result" of the view (if any) is assumed to be passed via an event 
 * to the call back listener that can be registered.
 * </p>
 * <p>
 * To inform the calling program about a state change (for example that a button
 * has been pressed) you can use:
 * </p>
 * 
 * <pre>
 * 	fireServerEvent(new DialogEvent(AbstractView.this, 
 *  DialogEvent.EVENT_DIALOG_STATE, DialogEvent.STATE_SAVED));
 * </pre>
 * 
 * 
 * Previous to this the calling program must register a listener<br>
 * <pre>
 * 		registerListener(DialogEvent.EVENT_DIALOG_STATE, new IEventListener() {
 *
 *			public void handle(ServerEvent event) {
 *				// do something	
 *			}		
 *		});
 * </pre>
 * 
 * @author Peter
 */

public abstract class AbstractPopup extends AbstractView {

	private String titel = "";
	private int width = 500;
	private int height = 400;
	private ICanvasWindowListener windowActionListener;
	private DynamicDialog dialog;

	/**
	 * Constructor for a dynamic popup.
	 * 
	 * @param factory the factory
	 * @param page the page
	 * @param titel of the window
	 * @param width of the window
	 * @param height of the window
	 */
	public AbstractPopup(WebGuiFactory factory, Page page, String titel, int width, int height ) {
		super( factory, null );
		this.height = height;
		this.width = width;
		this.titel = titel;
		setPage( page );
	}

	/**
	 * This method will ensure the standard view life-cycle for GUI components.
	 * It will be called from onAction()
	 */
	public final void show() {
		dialog = new DynamicDialog( getPage() );
		dialog.setWidth( width );
		dialog.setHeight( height );
		dialog.setWindowTitle( titel );
		setViewConnector( dialog.getWindow() );
		if( windowActionListener != null )
			dialog.setWindowActionListener(windowActionListener);

		super.show();
	}

	/**
	 * set an action listener for the popup window to handle close events
	 * 
	 * @param windowActionListener the popups action listener
	 */
	public void setWindowActionListener( ICanvasWindowListener windowActionListener ){
		this.windowActionListener = windowActionListener;
	}
	
	/**
	 * closes the popup and removes the components from the tree
	 */
	public final void close(){
		dialog.destroy();
	}

	/**
	 * @return the dialog
	 */
	public DynamicDialog getDialog() {
		return dialog;
	}
	
}
