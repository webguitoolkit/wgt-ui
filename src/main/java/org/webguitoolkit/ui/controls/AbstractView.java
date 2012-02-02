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
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;

/**
 * <pre>
 * This is the base for dynamic components that a user can implement in order to
 * create a (standard) function for his application (we call it a view). It is
 * designed for a standard life cycle that view is being created when the user
 * navigates in his application (e.g. click a button), i.e. he causes an event
 * that will be passed to the onAction() method.
 * 
 * Within the createControls() method the programmer has to create the GUI
 * component (or take them from somewhere). This component tree has to be added
 * to the viewConnector of this class that is by default a canvas. A very simple
 * view could look like this:
 * 
 * <code>
 * void  createControls( WebGuiFactory factory, Canvas viewConnector ) {
 *   TableLayout layout = factory.newTableLayout(viewConnector);
 *   factory.newLabel(layout, "My label");
 * }
 * </code>
 * 
 * It just creates a Label and attaches it to the viewConnector. <br>
 * In order to staff the view with data to be processed we assume to pass it
 * with the constructor. The "result" of the view (if any) is assumed to be
 * passed via an event to the call back listener that can be registered.
 * 
 * To inform the calling program about a state change (for example that a button
 * has been pressed) you can use:
 * 
 * <code>
 * 	fireServerEvent(new DialogEvent(AbstractView.this, 
 *  DialogEvent.EVENT_DIALOG_STATE, DialogEvent.STATE_SAVED));
 * </code>
 * 
 * 
 * Previous to this the calling program must register a listener <code>
 * 		registerListener(DialogEvent.EVENT_DIALOG_STATE, new IEventListener() {
 * 		public void handle(ServerEvent event) {
 * 			// do something	
 * 		}		
 * 	});
 * </code>
 * 
 * </pre>
 * 
 * @author peter
 */

public abstract class AbstractView implements IActionListener {

	private WebGuiFactory factory;
	private Canvas viewConnector;
	protected ClientEvent event;
	protected ListenerManager listenerManager = new ListenerManager();
	private Page page;
	
	/**
	 * 
	 * @param factory
	 * @param viewConnector
	 */
	public AbstractView(WebGuiFactory factory, ICanvas viewConnector) {
		this.factory = factory;
		this.viewConnector = (Canvas)viewConnector;
		if (viewConnector != null) {
			page = viewConnector.getPage();
		}
	}
	
	/**
	 * This method will ensure the standard view life-cycle for GUI components. It will be called from onAction()
	 */
	public void show() {
		destroy();

		// create controls
		createControls(factory, viewConnector);
	}

	/**
	 * Destroy will be called internally from within show() in order to clean up.
	 */
	public void destroy() {
		if (viewConnector.isDrawn()) {
			// prepare view
			clear();

			// TODO remove this redraw.
			viewConnector.redraw();
		}
	}

	/**
	 * @return the factory for the generating new controls
	 */
	protected WebGuiFactory getFactory() {
		return factory;
	}

	/**
	 * @return the current page
	 */
	protected Page getPage() {
		return page;
	}

	/**
	 * @return the canvas on witch the controls are drawn
	 */
	protected ICanvas getViewConnector() {
		return viewConnector;
	}

	protected void setPage(Page page) {
		this.page = page;

	}

	protected void setViewConnector(ICanvas viewConnector) {
		this.viewConnector = (Canvas)viewConnector;
	}

	public void clear() {
		viewConnector.removeAllChildren();
		viewConnector.setLayout(null);
	}

	/**
	 * Create your controls within here
	 * 
	 * @param factory the control factory
	 * @param viewConnector the canvas on witch the controles are rendered
	 */
	protected abstract void createControls(WebGuiFactory factory, ICanvas viewConnector);

	/**
	 * Standard onAction method keeps the event in local field event and calls show();
	 */
	public void onAction(ClientEvent event) {
		this.event = event;
		show();
	}

	/**
	 * 
	 * @param event
	 */
	public void fireServerEvent(ServerEvent event) {
		listenerManager.fireServerEvent(event);
	}

	/**
	 * 
	 * @param eventtype the event type for the listener
	 * @param liz the listener
	 */
	public void registerListener(int eventtype, IServerEventListener liz) {
		listenerManager.registerListener(eventtype, liz);
	}

	/**
	 * 
	 * @param eventtype
	 * @param liz
	 */
	public void removeListener(int eventtype, IServerEventListener liz) {
		listenerManager.removeListener(eventtype, liz);
	}

}
