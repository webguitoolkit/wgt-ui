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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.style.IStyleChangeListener;
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
public class StandardTabStrip extends TabStrip implements ITabStrip {

	private static final Logger logger = Logger.getLogger(StandardTabStrip.class);
	private Map<Integer, Boolean> accordeonStatus = null;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public StandardTabStrip() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public StandardTabStrip(String id) {
		super(id);
	}

	// protected int tabCount; depreciated with direktHtml not needed any more
	// if the strip should be visible (or only tabs selecting on/off)
	// protected boolean noStrip; // use diplayMode for this.
	protected String displayMode = DISPLAY_MODE_TAB;
	// this is the mode in which the tabs redraw themselves when user klicks on
	// it
	// protected int tabMode;
	protected Set disabledTabs = new HashSet();

	// true to keep on accordion fold open (default)
	private boolean toggleAccordion = true;

	public void drawAccordeon(PrintWriter out) {
		Div div = new Div();
		div.setID(getId());
		div.setClass("accordion");
		if (this.hasStyle())
			div.setStyle(this.getStyleAsString());
		setDefaultCssClass("accordion");
		for (int i = 0; i < getTabCount(); i++) {
			ITab tab = getTab(i);
			H3 head = new H3(tab.getHeaderLabel());
			head.setID(id4Label(i));
			head.setClass("accordion-head");

			if (tab.getHeaderStyle() != null)
				head.setStyle(tab.getHeaderStyle().getOutput());
			if (tab.getHeaderCSSClass() != null)
				head.setClass(tab.getHeaderCSSClass());

			head.setOnClick(JSUtil.jsEventParam(getId(), new String[] { String.valueOf(i) })
					+ JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION));
			div.addElement(head);

			// body
			Div inner = new Div();
			inner.setID(getTabId(i));
			if (this.hasStyle()) {
				String style = getContext().processValue(getId() + IContext.DOT_STYLE);
				inner.setStyle(style);
			}
			inner.setClass("accordion-body");

			inner.addElement(new EcsAdapter((BaseControl)tab));
			div.addElement(inner);
		}
		div.output(out);
	}

	public void drawNoStrip(PrintWriter out) {
		for (int i = 0; i < getTabCount(); i++) {
			ITab tab = getTab(i);
			// cssClass only set when it is a visible tab, we are using be default
			// tabPane as class
			setDefaultCssClass("wgtTabPane wgtTabBox");

			int sel = getContext().processInt(getId());
			// #FB 071121 debug info
			String s = "<table class='"
					+ getCssClass()
					+ "' "
					+ JSUtil.atId(getTabId(i))
					// style is taken from the father, some settings must be done.
					+ JSUtil.atNotEmpty(
							"style",
							"margin-top: 0;"
									+ ((sel != Integer.MIN_VALUE && tab == getTab(sel) ? "" : "display: none;") + StringUtils.trimToEmpty(getStyleAsString())))
					+ "cellpadding='0' cellspacing='0'>" + "<tr><td class='wgtTabContentBorder' colspan='3'>";
			out.println(s);
			out.println("<div class='wgtTabContent'>");

			new EcsAdapter((BaseControl)tab).output(out);

			out.println("  </div>");
			out.println("</td></tr>");
			out.println("	<tr><td class='wgtLBFooter'>&nbsp;</td><td class='wgtCFooter'>&nbsp;</td><td  class='wgtRBFooter'>&nbsp;</td></tr>");
			out.println("</table>");

		}
	}

	public void drawDefault(PrintWriter out) {
		setDefaultCssClass("wgtTabStrip");
		Table table = new Table();
		table.setClass("wgtTabStrip");
		table.setCellPadding(0);
		table.setCellSpacing(0);
		table.setID(getId());

		TR tr = new TR();
		table.addElement(tr);

		for (int i = 0; i < getTabCount(); i++) {

			TD td = new TD();
			td.setID(getId() + ".tri" + i);
			if (i != 0) {
				td.setClass("wgtTabBetweenUnselected");
			}
			else {
				td.setClass("wgtTabFirstUnselected");
			}
			tr.addElement(td);

			Tab tab = (Tab)getTab(i);
			td = new TD();
			td.setID(id4Label(i));
			td.setClass("wgtTabUnselected");
			if (StringUtils.isNotEmpty(tab.getTooltip()))
				td.setTitle(tab.getTooltip());
			td.setOnClick(getTabChangeJS(i));
			tr.addElement(td);

			Table innerTable = new Table();
			innerTable.setCellSpacing(1);
			innerTable.setCellPadding(1);
			td.addElement(innerTable);

			TR innerTr = new TR();
			innerTable.addElement(innerTr);
			if (StringUtils.isNotEmpty(tab.getTabIconSrc())) {
				// tab image
				TD innerTd = new TD();
				innerTr.addElement(innerTd);

				IMG image = new IMG();
				image.setSrc(tab.getTabIconSrc());
				innerTd.addElement(image);
			}
			if (StringUtils.isNotEmpty(tab.getLabel())) {
				// tab label
				TD innerTd = new TD();
				innerTd.setClass("wgtTabLabel");
				innerTd.addElement(tab.getLabel());
				innerTr.addElement(innerTd);
			}
		}
		TD td = new TD();
		td.setID(getId() + ".last");
		td.setClass("wgtTabLastUnselected");
		tr.addElement(td);

		table.output(out);

		for (int i = 0; i < getTabCount(); i++) {
			ITab tab = getTab(i);
			// cssClass only set when it is a visible tab, we are using be default
			// tabPane as class
			setDefaultCssClass("wgtTabPane wgtTabBox");

			int sel = getContext().processInt(getId());
			// #FB 071121 debug info
			String s = "<table class='"
					+ getCssClass()
					+ "' "
					+ JSUtil.atId(getTabId(i))
					// style is taken from the father, some settings must be done.
					+ JSUtil.atNotEmpty(
							"style",
							"margin-top: 0;"
									+ ((sel != Integer.MIN_VALUE && tab == getTab(sel) ? "" : "display: none;") + StringUtils.trimToEmpty(getStyleAsString())))
					+ "cellpadding='0' cellspacing='0'>" + "<tr><td class='wgtTabContentBorder' colspan='3'>";
			out.println(s);
			out.println("<div class='wgtTabContent'>");

			new EcsAdapter((BaseControl)tab).output(out);

			out.println("  </div>");
			out.println("</td></tr>");
			out.println("	<tr><td class='wgtLBFooter'>&nbsp;</td><td class='wgtCFooter'>&nbsp;</td><td  class='wgtRBFooter'>&nbsp;</td></tr>");
			out.println("</table>");

		}

	}

	@Override
	public void draw(PrintWriter out) {
		if (isAccordion())
			drawAccordeon(out);
		else if (isNoStrip())
			drawNoStrip(out);
		else if (isDefaultTab())
			drawDefault(out);
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	@Override
	protected void endHTML(PrintWriter out) {
		if (this.isAccordion()) {
			out.print("</div>");
		}
	}

	@Override
	protected void init() {
		getPage().addControllerJS( "tabstrip.js");
	}

	/**
	 * Add a label for a tab
	 * 
	 * @param tabId index of tab to label
	 * @param label label text
	 * @param icon optional icon
	 * @param toolTip optional tooltip
	 */
	public void setTabLabel(int tabId, String label, String icon, String toolTip) {

		StringBuffer tabStripContent = new StringBuffer("<table cellspacing=\"1\" cellpadding=\"1\" ><tr>");
		if (StringUtils.isNotEmpty(icon))
			tabStripContent.append("<td><img src=\"" + icon + "\"></td>");

		if (StringUtils.isNotEmpty(label))
			tabStripContent.append("<td>" + label + "</td>");
		tabStripContent.append("</tr></table>");

		getContext().add(id4Label(tabId), tabStripContent.toString(), IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
		// also get the tooltips from the tabs
		if (StringUtils.isNotEmpty(toolTip)) {
			getContext().add(id4Label(tabId) + ".title", toolTip, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
	}

	/**
	 * Add a label for a tab by key
	 * 
	 * @param tabId index of tab to label
	 * @param labelKey key of label
	 * @param icon optional icon
	 * @param toolTipKey optional tooltipkey
	 */
	public void setTabLabelKey(int tabId, String labelKey, String icon, String toolTipKey) {
		this.setTabLabel(tabId, TextService.getString(labelKey), icon, TextService.getString(toolTipKey));
	}

	public String id4Label(int i) {
		return getId() + ".lab" + i;
	}

	public String id4Triangle(int i) {
		return getId() + ".tri" + i;
	}

	/**
	 * select the visible tab, will also make invisible any other tab
	 * 
	 * @param tindex
	 * @return false if the tab is disabled
	 */
	public boolean selectTab(int tindex) {
		if (this.isAccordion()) {
			if (isDrawn())
				getContext().add(getId(), String.valueOf(tindex) + "_" + String.valueOf(isToggleAccordion()), IContext.TYPE_ACCORDION,
						IContext.STATUS_COMMAND);
			else
				getContext().add(getId(), String.valueOf(tindex) + "_" + String.valueOf(true), IContext.TYPE_ACCORDION,
						IContext.STATUS_COMMAND);
			if(accordeonStatus==null)
				accordeonStatus = new HashMap<Integer, Boolean>();
			if(tindex>-1)
			if(accordeonStatus.get(tindex)!=null &&accordeonStatus.get(tindex))
				accordeonStatus.put(tindex, false);
			else
				accordeonStatus.put(tindex, true);
				
		}
		else {
			// catch out of bounds
			tindex = Math.min(Math.max(tindex, 0), getTabCount());
			// special JS will adjust the gifs in the tabstrip
			if (disabledTabs.contains(new Integer(tindex)))
				return false;
			int oldindex = getSelectedTab();
			String oldstatus = "";
			try {
				getTab(oldindex);
				oldstatus = getTabStatus(oldindex);
			}
			catch (RuntimeException e) {
				oldindex = -1;
				oldstatus = "";
			}
			String tstatus = getTabStatus(tindex);
			getContext().add(getId(), tindex, IContext.TYPE_TAB, IContext.STATUS_SERVER_ONLY);
			String type = "h";
			if (isNoStrip()) {
				type = "n";
			}
			getContext().add(getId() + ".tabswitch",
					"new Array(\"" + tindex + "\", \"" + tstatus + "\", \"" + oldindex + "\", \"" + oldstatus + "\", \"" + type + "\")",
					IContext.TYPE_TAB, IContext.STATUS_COMMAND);
			((BaseControl)getTab(tindex)).processSetVisible(true);
			if (oldindex != -1)
				((BaseControl)getTab(getSelectedTab())).processSetVisible(false);
		}
		return true;
	}

	/**
	 * @return state of tab first = first tab in strip, last = last tab, only if just one tab, mid else
	 * @param tindex
	 */
	private String getTabStatus(int tindex) {
		String tstatus;
		Tab tab = (Tab)getTab(tindex);
		if (tab.isFirstTab() && tab.isLastTab()) {
			return "only";
		}
		if (tab.isFirstTab()) {
			return "first";
		}
		else if (tab.isLastTab()) {
			return "last";
		}
		return "mid";
	}

	/**
	 * @return index of selected tab
	 */
	public int getSelectedTab() {
		return getContext().getValueAsInt(getId());
	}

	/**
	 * get the tab with given index. All tab are accessed via index.
	 */
	public ITab getTab(int i) {
		return (ITab)getChildren().get(i);
	}

	/**
	 * get the index of given tab.
	 */
	public int getTabIndex(ITab tab) {
		if (this.getChildren() != null) {
			return this.getChildren().indexOf(tab);
		}
		else {
			return -1;
		}
	}

	/**
	 * @deprecated use getTabIndex
	 * @param tab
	 * @return
	 */
	@Deprecated
	public int indexOfTab(Tab tab) {
		return getChildren().indexOf(tab);
	}

	/**
	 * @return number of available tabs
	 */
	public int getTabCount() {
		return getChildren().size();
	}

	public boolean isNoStrip() {
		return DISPLAY_MODE_TAB_NO_STRIP.equals(displayMode);
	}

	public boolean isAccordion() {
		return DISPLAY_MODE_ACCORDION.equals(displayMode);
	}
	
	public boolean getAccordeonStatus(int foldIndex) {
		if(accordeonStatus!=null && accordeonStatus.get(foldIndex)!=null){
			return accordeonStatus.get(foldIndex);
		} else {
			return false;
		}
	}

	public boolean isDefaultTab() {
		return DISPLAY_MODE_TAB.equals(displayMode);
	}

	/**
	 * function for dynamic disabling of a tab
	 */
	public void setDisabled(int tabId) {
		if (isTabEnabled(tabId)) {
			disabledTabs.add(new Integer(tabId));
			getContext().add(id4Label(tabId) + ".class", "wgtTabDisabled", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
	}

	/**
	 * function for dynamic enable of a tab
	 */
	public void setEnabled(int tabId) {
		if (!isTabEnabled(tabId)) {
			disabledTabs.remove(new Integer(tabId));
			getContext().add(id4Label(tabId) + ".class", "wgtTabUnselected", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
	}

	/**
	 * hides a tab from TabStrip, if tab is active, the tab will not be hidden
	 * 
	 * @param tabId of the tab to hide
	 */

	public void hideTab(int tabId) {
		if (getSelectedTab() == tabId) {
			logger.warn("cannot hide active tab");
			return;
		}
		Tab tab = (Tab)getTab(tabId);
		if (tab.isVisible()) {
			tab.setVisible(false);
			if (tab.isFirstTab() && !tab.isLastTab()) {
				// we must set the right neighbor as first
				Tab neighbour = (Tab)getTab(tabId + 1);
				neighbour.setFirstTab(true);
			}
			if (tab.isLastTab() && !tab.isFirstTab()) {
				// we must set the left neighbor as last
				Tab neighbour = (Tab)getTab(tabId - 1);
				neighbour.setLastTab(true);
			}
			getContext().add(id4Label(tabId), "false", IContext.TYPE_TABVISI, IContext.STATUS_EDITABLE);
			getContext().add(id4Triangle(tabId), "false", IContext.TYPE_TABVISI, IContext.STATUS_EDITABLE);
			getContext().add(getId(), getSelectedTab(), "trd", IContext.STATUS_EDITABLE);
			selectTab(getSelectedTab());
		}
	}

	/**
	 * show a tab in TabStrip
	 * 
	 * @param tabId
	 */
	public void showTab(int tabId) {
		Tab tab = (Tab)getTab(tabId);
		if (!tab.isVisible()) {
			tab.setVisible(true);
			if (tab.isFirstTab() && !tab.isLastTab()) {
				// we must set the right neighbor as first
				Tab neighbour = (Tab)getTab(tabId + 1);
				neighbour.setFirstTab(false);
			}
			if (tab.isLastTab() && !tab.isFirstTab()) {
				// we must set the left neighbor as last
				Tab neighbour = (Tab)getTab(tabId - 1);
				neighbour.setLastTab(false);
			}
			getContext().add(id4Label(tabId), "true", IContext.TYPE_TABVISI, IContext.STATUS_EDITABLE);
			getContext().add(id4Triangle(tabId), "true", IContext.TYPE_TABVISI, IContext.STATUS_EDITABLE);
			getContext().add(getId(), getSelectedTab(), IContext.TYPE_TABREDRAW, IContext.STATUS_EDITABLE);
			selectTab(getSelectedTab());
		}
	}

	/**
	 * @param tabId
	 * @return if tab is enabled
	 */
	public boolean isTabEnabled(int tabId) {
		return !disabledTabs.contains(new Integer(tabId));
	}

	/**
	 * function for dynamic enabling of all tabs
	 */
	public void enableAll() {
		for (Iterator allDisabled = disabledTabs.iterator(); allDisabled.hasNext();) {
			int disabledIndex = ((Integer)allDisabled.next()).intValue();
			getContext().add(id4Label(disabledIndex) + ".class", "wgtTabUnselected", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
			allDisabled.remove();
		}
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
	public void addTab(ITab tab) {
		add(tab);
		if (!this.isAccordion()) {
			if (getTabCount() == 1) {
				// choose first one if no other is selected (later)
				selectTab(0);
			}
		}
		else {
			// default: all closed
			selectTab(-1);
		}
	}

	@Override
	public void dispatch(ClientEvent event) {
		if (!hasExecutePermission())
			return;
		if (event.getTypeAsInt() == ClientEvent.TYPE_ACTION) {
			int newTab = Integer.parseInt(event.getParameter(0));
			StandardTabStrip tabstrip = (StandardTabStrip)event.getSource();
			int selectedTab = tabstrip.getSelectedTab();
			if (getListener() != null) {
				if (getListener().onTabChange(tabstrip.getTab(selectedTab), tabstrip.getTab(newTab), event))
					tabstrip.selectTab(newTab);
			}
			else
				tabstrip.selectTab(newTab);
		}
	}

	@Override
	protected void registerStyleChangeListener() {
		style.registerStyleChangedListener(new TabStyleChangedListener());
	}

	public void setToggleAccordion(boolean toggleAccordion) {
		this.toggleAccordion = toggleAccordion;
	}

	public boolean isToggleAccordion() {
		return toggleAccordion;
	}

	class TabStyleChangedListener implements IStyleChangeListener {
		public void handle(int eventType, String attribute) {
			if (getParent() != null) {
				if (isAccordion()) {
					getContext().add(getId() + Context.DOT_STYLE, getStyleAsString(), Context.TYPE_ATT, Context.STATUS_NOT_EDITABLE);
				}
				else {
					for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
						BaseControl control = (BaseControl)iter.next();
						if (control instanceof Tab)
							getContext().add(getTabId(getTabIndex(((Tab)control))) + Context.DOT_STYLE, getStyleAsString(),
									Context.TYPE_ATT, Context.STATUS_NOT_EDITABLE);
					}
				}
			}
		}

		public String getListenerId() {
			return getId();
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#getSelected()
	 */
	public ITab getSelected() {
		return getTab(getSelectedTab());
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#getSelectedIndex()
	 */
	public int getSelectedIndex() {
		return getSelectedTab();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#hideTab(org.webguitoolkit.ui.controls.tab.ITab)
	 */
	public void hideTab(ITab tab) {
		hideTab(getTabIndex(tab));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#selectTab(org.webguitoolkit.ui.controls.tab.ITab)
	 */
	public boolean selectTab(ITab tab) {
		return selectTab(getTabIndex(tab));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setDisabled(org.webguitoolkit.ui.controls.tab.ITab)
	 */
	public void setDisabled(ITab tab) {
		setDisabled(getTabIndex(tab));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#setEnabled(org.webguitoolkit.ui.controls.tab.ITab)
	 */
	public void setEnabled(ITab tab) {
		setEnabled(getTabIndex(tab));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITabStrip#showTab(org.webguitoolkit.ui.controls.tab.ITab)
	 */
	public void showTab(ITab tab) {
		showTab(getTabIndex(tab));
	}

	/**
	 * @return
	 */
	protected String getTabId(int index) {
		if (isAccordion()) {
			return getId() + ".tab" + String.valueOf(index);
		}
		else {
			StringBuffer jsName = new StringBuffer(getId());
			jsName.append(".pane");
			jsName.append(index);
			return jsName.toString();
		}
	}

	/**
	 * @param tab
	 * @param headerLabel
	 */
	protected void setHeaderLabel(Tab tab, String headerLabel) {
		if (isAccordion()) {
			getContext().add(id4Label(getTabIndex(tab)), headerLabel, IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
		}
		else if (isDefaultTab()) {
			StringBuffer tabStripContent = new StringBuffer("<table cellspacing=\"1\" cellpadding=\"1\" ><tr>");
			if (StringUtils.isNotEmpty(tab.getHeaderImage()))
				tabStripContent.append("<td><img src=\"" + tab.getHeaderImage() + "\"></td>");

			if (StringUtils.isNotEmpty(headerLabel))
				tabStripContent.append("<td>" + headerLabel + "</td>");
			tabStripContent.append("</tr></table>");

			getContext().add(id4Label(getTabIndex(tab)), tabStripContent.toString(), IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
		}
	}

	/**
	 * @param tab
	 * @param headerStyle
	 */
	protected void setHeaderStyle(Tab tab, Style headerStyle) {
		// not implemented
	}

	/**
	 * @param tab
	 * @param headerCSSClass
	 */
	protected void addHeaderCSSClass(Tab tab, String headerCSSClass) {
		// not implemented
	}

	/**
	 * @param tab
	 * @param headerTooltip
	 */
	protected void setHeaderTooltip(Tab tab, String headerTooltip) {
		if (!isAccordion() && !isNoStrip()) {
			getContext().add(id4Label(getTabIndex(tab)) + ".title", headerTooltip, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		}
	}

	/**
	 * @param tab
	 * @param headerImage
	 */
	protected void setHeaderImage(Tab tab, String headerImage) {
		if (!isAccordion() && !isNoStrip()) {
			StringBuffer tabStripContent = new StringBuffer("<table cellspacing=\"1\" cellpadding=\"1\" ><tr>");
			if (StringUtils.isNotEmpty(headerImage))
				tabStripContent.append("<td><img src=\"" + headerImage + "\"></td>");

			if (StringUtils.isNotEmpty(tab.getLabel()))
				tabStripContent.append("<td>" + tab.getLabel() + "</td>");
			tabStripContent.append("</tr></table>");

			getContext().add(id4Label(getTabIndex(tab)), tabStripContent.toString(), IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
		}
	}
}
