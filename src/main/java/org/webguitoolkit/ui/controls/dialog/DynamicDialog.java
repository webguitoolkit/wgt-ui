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
package org.webguitoolkit.ui.controls.dialog;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.ICanvasWindowListener;
import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * DynamicDialog is a popup that can be created at any time in the application and<br>
 * is dynamically added to the page. <code>
 	DynamicDialog dd = new DynamicDialog(page);
		
    Canvas canvas = dd.getWindow();
	
	Compound compound = factory.newCompound(canvas);
	compound.setBag(WebGuiFactory.getInstance().createDataBag(""));
	TableLayout layout = factory.newTableLayout(compound);
 * </code>
 */
public class DynamicDialog extends AbstractDialog {
	
	private ICanvas window;
	private WebGuiFactory factory = WebGuiFactory.getInstance();

	/**
	 * Create a DynamicDialog instance
	 * 
	 * @param page target page
	 */
	public DynamicDialog(Page page) {
		this(page, null);
	}

	public DynamicDialog(Page page, String windowId) {
		window = getFactory().createCanvas(page, windowId);
		window.setDisplayMode(Canvas.DISPLAY_MODE_WINDOW_MODAL);
		window.setDragable(true);
		window.setWindowActionListener(new ICanvasWindowListener() {
			public void onClose(ClientEvent event) {
				destroy();
			}

			public void onMinimize(ClientEvent event) {
				// nothing to do
			}

			public void onMaximize(ClientEvent event) {
				// nothing to do
			}

			public void onResize(ClientEvent event) {
				// nothing to do
			}
		});

		// if initial the dialog is rendered directly,
		// otherwise the dialog has to be appended to the page dynamically
		if (page.isDrawn()) {
			page.append(window);
		}
	}
	
	/**
	 * call this to destory the dialog, all components will be remove and the dialog will disapear. it will also be removed from
	 * the clents DOM.
	 */
	public void destroy() {
		// remove from clients DOM
		window.getPage().getContext().removeNode(window.getId());
		// remove from component tree
		window.remove();
		window.getPage().disableZone(false, window.getId());
	}

	/**
	 * @return page for dialog
	 */
	public ICanvas getWindow() {
		return window;
	}

	/**
	 * add controls to the dialog
	 * 
	 * @param control contol to be added
	 */
	public void add(BaseControl control) {
		window.add(control);
	}

	/**
	 * @return tooltip of the dialog
	 */
	public String getTooltip() {
		return window.getTooltip();
	}

	/**
	 * @return the registered listener for the dialog
	 */
	public ICanvasWindowListener getWindowActionListener() {
		return window.getWindowActionListener();
	}

	/**
	 * Identify if dialog is dragable
	 * 
	 * @return true for dragable canvas
	 */
	public boolean isDragable() {
		return window.isDragable();
	}

	/**
	 * @param displayMode set {@link Canvas}.DISPLAYMODE
	 */
	public void setDisplayMode(String displayMode) {
		window.setDisplayMode(displayMode);
	}

	/**
	 * @param dragable true for dragable
	 */
	public void setDragable(boolean dragable) {
		window.setDragable(dragable);
	}

	/**
	 * 
	 * @param height specify height of dialog in pixels
	 */
	public void setHeight(int height) {
		window.setHeight(height);
	}

	/**
	 * @param hPos specify horizontal position of dialog in pixels
	 */
	public void setHPosition(int hPos) {
		window.setHPosition(hPos);
	}

	/**
	 * @param width specify width of dialog in pixels
	 */
	public void setWidth(int width) {
		window.setWidth(width);
	}

	/**
	 * @param windowActionListener Listener for this dialog
	 */
	public void setWindowActionListener(ICanvasWindowListener windowActionListener) {
		window.setWindowActionListener(windowActionListener);
	}

	/**
	 * @param windowTitle to be displayed on top of the Dialog
	 */
	public void setWindowTitle(String windowTitle) {
		window.setTitle(windowTitle);
	}

	/**
	 * @return instance of the WebGuiFactory
	 */
	public WebGuiFactory getFactory() {
		return factory;
	}
}
