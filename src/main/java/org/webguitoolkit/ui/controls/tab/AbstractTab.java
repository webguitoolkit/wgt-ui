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
package org.webguitoolkit.ui.controls.tab;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * 
 * @author i102415
 * @deprecated
 */
@Deprecated
public abstract class AbstractTab implements ITabListener {

	private final boolean redraw = false;
	private Tab theTab = null;
	private final WebGuiFactory factory;
	private final StandardTabStrip tabStrip;
	protected ListenerManager listenerManager = new ListenerManager();

	public AbstractTab(WebGuiFactory factory, StandardTabStrip tabStrip, String labelKey, String icon) {
		this.factory = factory;
		this.tabStrip = tabStrip;
		theTab = factory.newTab(tabStrip, labelKey);
		theTab.setTabIconSrc(icon);
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
		return tabStrip.getPage();
	}

	/**
	 * @return the canvas on witch the controls are drawn
	 */
	protected Tab getTab() {
		return theTab;
	}

	protected StandardTabStrip getTabStrip() {
		return tabStrip;
	}

	/**
	 * Create your controls within here
	 * 
	 * @param factory the control factory
	 * @param viewConnector the canvas on witch the controles are rendered
	 */
	protected abstract void createControls(WebGuiFactory factory, Tab tab);

	/**
	 * load is called when tab is selected
	 */
	protected abstract boolean load();

	/**
	 * leave is called when yu leave the tab
	 */
	public boolean leave() {
		return true;
	}

	public boolean onTabChange(ITab old, ITab selected, ClientEvent event) {
		if (redraw)
			show();
		else
			load();
		return true;
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
	 * @param eventtype
	 * @param liz
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

	/**
	 * This method will ensure the standard view life-cycle for GUI components. It will be called from onAction()
	 */
	protected void show() {
		if (getTab().isDrawn()) {
			// prepare view
			clear();

			// TODO remove this redraw.
			getTab().redraw();
		}

		// create controls
		createControls(factory, getTab());
	}

	public void clear() {
		getTab().removeAllChildren();
	}

	public void setDisplay(String icon, String label) {
		int tabIndex = getTabStrip().getTabIndex(getTab());
		getTabStrip().setTabLabel(tabIndex, label, icon, null);
	}

	public void setDisplayKey(String icon, String labelKey) {
		setDisplay(icon, TextService.getString(labelKey));
	}

}
