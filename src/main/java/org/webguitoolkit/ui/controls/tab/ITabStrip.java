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

import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * <h1>Interface for TabStrip</h1>
 * <p>
 * This is the Interface for a TabStrip. A TabStrip is intended to control the associated {@link ITab}s. It has three display
 * modes:
 * </p>
 * <ul>
 * <li>DISPLAY_MODE_TAB: default, normal TabStrip</li>
 * <li>DISPLAY_MODE_TAB_NO_STRIP: hides TabStrip, only the Tab is shown</li>
 * <li>DISPLAY_MODE_ACCORDION: Show tabs in a vertical order.</li>
 * </ul>
 * <b>Creation of a TabStrip</b><br>
 * 
 * <pre>
 * //create ITabStrip
 * ITabStrip tabStrip = factory.createTabStrip(viewConnector);
 * //add some ITabs
 * ITab firstTab = factory.createTab(tabStrip, &quot;Tab 1&quot;);
 * ITab secondTab = factory.createTab(tabStrip, &quot;Tab 2&quot;);
 * ITab lastTab = factory.createTab(tabStrip, &quot;Tab n&quot;);
 * //Fill the Tabs
 * factory.createLabel(firstTab, &quot;Some text/content for Tab 1&quot;);
 * factory.createLabel(secondTab, &quot;Some text/content for Tab 2&quot;);
 * factory.createLabel(lastTab, &quot;Some text/content for Tab n&quot;);
 * //optional call: DISPLAY_MODE
 * tabStrip.setDisplayMode(TabStrip.DISPLAY_MODE_ACCORDION);
 * </pre>
 * 
 * <br>
 * <b>Event handling</b><br>
 * ITabStrip can trigger events for Tab changes. By default, a simple {@link ITabListener} is assigned to the TabStrip. It is
 * executed onTabChange. Create your own ITabListener to extend function when changing Tabs<br>
 * 
 * <pre>
 * //the ITabListener code
 * private class MyTabListener implements ITabListener {
 * 	public boolean onTabChange(ITab old, ITab selected, ClientEvent event) {
 * 		//do something with the Tabs here
 * 		return false;
 * 	}
 * }
 * //add the listener
 * tabStrip.setListener(new MyTabListener());
 * 
 * 
 * </pre>
 * 
 * <br>
 * CSS classes : wgtTabStrip, wgtTabBetweenUnselected, wgtTabFirstUnselected, wgtTabUnselected, wgtTabLabel, wgtTabLastUnselected,
 * accordion
 * 
 * @author Peter
 * @author Ben
 * 
 */
public interface ITabStrip extends IBaseControl {

	String DISPLAY_MODE_TAB = "DISPLAY_MODE_TAB"; // default
	String DISPLAY_MODE_TAB_NO_STRIP = "DISPLAY_MODE_TAB_NO_STRIP";
	String DISPLAY_MODE_ACCORDION = "DISPLAY_MODE_ACCORDION";

	/**
	 * @param displayMode use the DISPLAY_MODE_... constants
	 */
	void setDisplayMode(String displayMode);

	/**
	 * Add a label for a tab
	 * 
	 * @param tabId index of tab to label
	 * @param label label text
	 * @param icon optional icon
	 * @param toolTip optional tooltip
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	void setTabLabel(int tabId, String label, String icon, String toolTip);

	/**
	 * Add a label for a tab by key
	 * 
	 * @param tabId index of tab to label
	 * @param labelKey key of label
	 * @param icon optional icon
	 * @param toolTipKey optional tool tip key
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	void setTabLabelKey(int tabId, String labelKey, String icon, String toolTipKey);

	/**
	 * select the visible tab, will also deselect any other tab
	 * 
	 * @param tindex index of tab to select
	 * @return false if the tab is disabled
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	boolean selectTab(int tindex);

	/**
	 * function for dynamic disabling of a tab
	 * 
	 * @param tabId f the tab to disable
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	void setDisabled(int tabId);

	/**
	 * function for dynamic enable of a tab
	 * 
	 * @param tabId f the tab to enable
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	void setEnabled(int tabId);

	/**
	 * Hides a tab from TabStrip, if tab is active, the tab will not be hidden.
	 * 
	 * @param tabId of the tab to hide
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	void hideTab(int tabId);

	/**
	 * show a tab in TabStrip
	 * 
	 * @param tabId f the tab to show
	 * @deprecated use function with ITab as parameter
	 */
	@Deprecated
	void showTab(int tabId);

	/**
	 * function for dynamic enabling of all tabs
	 */
	void enableAll();

	/**
	 * Add a tab on the right to the TabStrip.
	 * 
	 * @param tab the new tab
	 * @deprecated use add function
	 */
	@Deprecated
	void addTab(ITab tab);

	/**
	 * Use a implementation of ITabListener to perform some action onTabChange. Simple tab changing is implemented by default.
	 * 
	 * @param listener Your implementation of {@link ITabListener}
	 */
	void setListener(ITabListener listener);

	/**
	 * change behavior of accordion. true=default (toggles folds)
	 * 
	 * @param toggleAccordion true toggle folds
	 */
	void setToggleAccordion(boolean toggleAccordion);

	/**
	 * @param selectedTab get the tab with this index
	 * @return the tab
	 */
	ITab getTab(int selectedTab);

	/**
	 * find the ID of a Tab
	 * 
	 * @param tab the tab
	 * @return index of tab
	 */
	int getTabIndex(ITab tab);

	/**
	 * Returns the index of the currently selected tab. Starting with 0 for the first tab.
	 * 
	 * @deprecated Use getSelectedIndex or getSelected
	 * 
	 * @return index of selected tab
	 */
	@Deprecated
	int getSelectedTab();

	/**
	 * @return number of available tabs
	 */
	int getTabCount();

	/**
	 * select the visible tab, will also deselect any other tab
	 * 
	 * @param tindex index of tab to select
	 * @return false if the tab is disabled
	 */
	boolean selectTab(ITab tab);

	/**
	 * function for dynamic enable of a tab
	 * 
	 * @param tab to enable
	 */
	void setEnabled(ITab tab);

	/**
	 * function for dynamic disabling of a tab
	 * 
	 * @param tab to disable
	 */
	void setDisabled(ITab tab);

	/**
	 * Hides a tab from TabStrip, if tab is active, the tab will not be hidden.
	 * 
	 * @param tab to hide
	 */

	void hideTab(ITab tab);

	/**
	 * show a tab in TabStrip
	 * 
	 * @param tab to show
	 */
	void showTab(ITab tab);

	/**
	 * Returns the index of the currently selected tab. Starting with 0 for the first tab.
	 * 
	 * @return index of selected tab
	 */
	int getSelectedIndex();

	/**
	 * Returns the index of the currently selected tab. Starting with 0 for the first tab.
	 * 
	 * @return index of selected tab
	 */
	ITab getSelected();

}
