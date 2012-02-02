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
package org.webguitoolkit.ui.controls.tab;

import java.util.ArrayList;
import java.util.List;

import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * <pre>
 * if you use an actionListener, you will be informed of every change of the
 * selected tab. You can get the selected tab from the first event parameter.
 * 
 * NOTE: that you have to actually call selectTab-to change to the newly
 * selected tab. An implementation which does the standard behavior would look
 * like:
 * 
 * public void onAction(Event event) { // which tab is being selected int
 * tabNumber = Integer.parseInt(event.getParameter(0)); ((TabStrip)
 * event.getComponent()).selectTab(tabNumber); }
 * </pre>
 * 
 * @author Arno
 * 
 */
public abstract class TabStrip extends BaseControl implements ITabStrip {

	private ITabListener listener;
	private int selected = 0;

	final List<ITab> tabs = new ArrayList<ITab>();

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public TabStrip() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public TabStrip(String id) {
		super(id);
	}

	@Override
	protected void init() {
	}

	/**
	 * get the tab with given index. All tab are accessed via index.
	 */
	public ITab getTab(int i) {
		if (i >= 0)
			return tabs.get(i);
		return null;
	}

	/**
	 * get the index of given tab.
	 */
	public int getTabIndex(ITab tab) {
		if (tabs != null) {
			return tabs.indexOf(tab);
		}
		else {
			return -1;
		}
	}

	/**
	 * @return number of available tabs
	 */
	public int getTabCount() {
		return tabs.size();
	}

	/**
	 * override this method to manipulate the JavaScript generated for changing the tabs. You can safely add your JavaScript and
	 * append the super.getTabChangeJS(), but don't forget to separate the js-commands by ';'.
	 * 
	 * @param tabNo - the tab we want to change to.
	 * @return
	 */
	protected String getTabChangeJS(int tabNo) {
		return JSUtil.jsEventParam(getId(), new String[] { "" + tabNo }) + JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION);
	}

	/**
	 * Add a tab on the right to the TabStrip.
	 * 
	 * @param tab
	 */
	@Override
	protected void add(IBaseControl child) {
		if (child instanceof ITab)
			tabs.add((ITab)child);
		super.add(child);
	}

	public ITabListener getListener() {
		return listener;
	}

	public void setListener(ITabListener listener) {
		this.listener = listener;
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#getSelectedTabIndex()
	 */
	public int getSelectedIndex() {
		return selected;
	}

	protected void setSelectedIndex(int newSelected) {
		this.selected = newSelected;
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.AbstractTabStrip#getSelectedTab()
	 */
	public ITab getSelected() {
		return getTab(getSelectedIndex());
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#addTab(org.webguitoolkit.ui.controls.tab.ITab)
	 * @deprecated
	 */
	@Deprecated
	public void addTab(ITab tab) {
		add(tab);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#getSelectedTab()
	 * @deprecated
	 */
	@Deprecated
	public int getSelectedTab() {
		return getSelectedIndex();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#hideTab(int)
	 * @deprecated
	 */
	@Deprecated
	public void hideTab(int tabId) {
		hideTab(getTab(tabId));

	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#selectTab(int)
	 * @deprecated
	 */
	@Deprecated
	public boolean selectTab(int tabId) {
		return selectTab(getTab(tabId));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setDisabled(int)
	 * @deprecated
	 */
	@Deprecated
	public void setDisabled(int tabId) {
		setDisabled(getTab(tabId));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setDisplayMode(java.lang.String)
	 */
	public void setDisplayMode(String displayMode) {
		// not needed in this version
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setEnabled(int)
	 * @deprecated
	 */
	@Deprecated
	public void setEnabled(int tabId) {
		setEnabled(getTab(tabId));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setTabLabel(int, java.lang.String, java.lang.String, java.lang.String)
	 * @deprecated
	 */
	@Deprecated
	public void setTabLabel(int tabId, String label, String icon, String toolTip) {
		getTab(tabId).setHeaderLabel(label);
		getTab(tabId).setHeaderImage(icon);
		getTab(tabId).setHeaderTooltip(toolTip);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setTabLabelKey(int, java.lang.String, java.lang.String, java.lang.String)
	 * @deprecated
	 */
	@Deprecated
	public void setTabLabelKey(int tabId, String labelKey, String icon, String toolTipKey) {
		getTab(tabId).setHeaderLabelKey(labelKey);
		getTab(tabId).setHeaderImage(icon);
		getTab(tabId).setHeaderTooltipKey(toolTipKey);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setToggleAccordion(boolean)
	 */
	public void setToggleAccordion(boolean toggleAccordion) {
		// not needed
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#showTab(int)
	 * @deprecated
	 */
	@Deprecated
	public void showTab(int tabId) {
		showTab(getTab(tabId));
	}

	/**
	 * @param tab
	 * @param headerLabel
	 */
	protected abstract void setHeaderLabel(Tab tab, String headerLabel);

	/**
	 * @param tab
	 * @param headerStyle
	 */
	protected abstract void setHeaderStyle(Tab tab, Style headerStyle);

	/**
	 * @param tab
	 * @param headerCSSClass
	 */
	protected abstract void addHeaderCSSClass(Tab tab, String headerCSSClass);

	/**
	 * @param tab
	 * @param headerTooltip
	 */
	protected abstract void setHeaderTooltip(Tab tab, String headerTooltip);

	/**
	 * @param tab
	 * @param headerImage
	 */
	protected abstract void setHeaderImage(Tab tab, String headerImage);

}
