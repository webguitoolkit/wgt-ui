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
import java.util.Hashtable;

import org.apache.ecs.html.Div;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.button.StandardButton;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * <pre>
 * </pre>
 * 
 * @author Martin
 * 
 */
public class ButtonTabStrip extends TabStrip {

	protected Hashtable<ITab, IButton> buttons = new Hashtable<ITab, IButton>();
	private Class<? extends Button> buttonClass = StandardButton.class;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public ButtonTabStrip() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public ButtonTabStrip(String id) {
		super(id);
	}

	@Override
	public void draw(PrintWriter out) {
		boolean isFirst = true;
		Div div = new Div();
		for (ITab tab : tabs) {
			Button button = (Button)buttons.get(tab);
			if (isFirst) {
				button.addCssClass("selectedButton");
				isFirst = false;
			}
			button.setLabel(tab.getHeaderLabel());
			button.setSrc(tab.getHeaderImage());
			button.setTooltip(tab.getHeaderTooltip());
			div.addElement(new EcsAdapter((BaseControl)buttons.get(tab)));
		}

		Table table = new Table();
		table.setClass("wgtTabPane wgtTabBox");
		table.setCellPadding(0);
		table.setCellSpacing(0);

		TR contentRow = new TR();
		table.addElement(contentRow);

		TD td = new TD();
		contentRow.addElement(td);
		td.setClass("wgtTabContentBorder");
		td.setColSpan(3);

		isFirst = true;
		for (ITab tab : tabs) {
			tab.setVisible(isFirst);
			tab.addCssClass("wgtTabContent");
			td.addElement(new EcsAdapter((BaseControl)tab));
			if (isFirst)
				isFirst = false;
		}

		TR footerRow = new TR();
		table.addElement(footerRow);

		td = new TD("&nbsp;");
		footerRow.addElement(td);
		td.setClass("wgtLBFooter");

		td = new TD("&nbsp;");
		footerRow.addElement(td);
		td.setClass("wgtCFooter");

		td = new TD("&nbsp;");
		footerRow.addElement(td);
		td.setClass("wgtRBFooter");

		div.addElement(table);
		div.output(out);
	}

	@Override
	protected void init() {
	}

	public class TabChangeListener implements IActionListener {
		private final ITab tab;

		public TabChangeListener(ITab tab) {
			this.tab = tab;
		}

		/**
		 * @see org.webguitoolkit.ui.controls.event.IActionListener#onAction(org.webguitoolkit.ui.controls.event.ClientEvent)
		 */
		public void onAction(ClientEvent event) {
			if (getListener() != null) {
				if (getListener().onTabChange(getSelected(), tab, event)) {
					selectTab(tab);
				}
			}
			else
				selectTab(tab);
		}
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#enableAll()
	 */
	public void enableAll() {
		for (ITab tab : tabs) {
			buttons.get(tab).setDisabled(false);
		}
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#hideTab(org.webguitoolkit.ui.controls.AbstractView)
	 */
	public void hideTab(ITab tab) {
		buttons.get(tab).setVisible(false);
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#selectTab(org.webguitoolkit.ui.controls.AbstractView)
	 */
	public boolean selectTab(ITab tab) {
		if (buttons.get(tab).isDisabled())
			return false;
		ITab oldTab = getSelected();

		if (oldTab == tab)
			return false;

		IButton button = buttons.get(tab);
		button.addCssClass("selectedButton");

		if (oldTab != null) {
			IButton oldbutton = buttons.get(oldTab);
			oldbutton.removeCssClass("selectedButton");
		}

		tab.setVisible(true);
		setSelectedIndex(tabs.indexOf(tab));
		if (oldTab != null)
			oldTab.setVisible(false);
		return true;
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#setDisabled(org.webguitoolkit.ui.controls.AbstractView)
	 */
	public void setDisabled(ITab tab) {
		buttons.get(tab).setDisabled(true);

	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#setEnabled(org.webguitoolkit.ui.controls.AbstractView)
	 */
	public void setEnabled(ITab tab) {
		buttons.get(tab).setDisabled(false);
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#setTabButtonLabel(org.webguitoolkit.ui.controls.AbstractView,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setTabButtonLabel(ITab tab, String label, String icon, String toolTip) {
		IButton button = buttons.get(tab);
		button.setLabel(label);
		button.setTooltip(toolTip);
		button.setSrc(icon);
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#setTabButtonLabelKey(org.webguitoolkit.ui.controls.AbstractView,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setTabButtonLabelKey(ITab tab, String labelKey, String icon, String tooltipKey) {
		IButton button = buttons.get(tab);
		button.setLabelKey(labelKey);
		button.setTooltipKey(tooltipKey);
		button.setSrc(icon);
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#showTab(org.webguitoolkit.ui.controls.AbstractView)
	 */
	public void showTab(ITab tab) {
		buttons.get(tab).setVisible(false);
	}

	/**
	 * @see org.webguitoolkit.ui.sandbox.tab.ITabStrip#addTab(org.webguitoolkit.ui.sandbox.tab.ITab)
	 */
	@Override
	protected void add(IBaseControl control) {
		super.add(control);
		if (control instanceof ITab) {
			ITab tab = (ITab)control;
			Button button = createButton(tab);
			buttons.put(tab, button);
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#endHTML(java.io.PrintWriter)
	 */
	@Override
	protected void endHTML(PrintWriter out) {
		// TODO Auto-generated method stub

	}

	public void add(ITab tab) {
		super.add((IBaseControl)tab);
		Button button = createButton(tab);
		buttons.put(tab, button);
	}

	protected Button createButton(ITab tab) {
		Button button;
		try {
			button = buttonClass.newInstance();
		}
		catch (InstantiationException e) {
			Logger.getLogger(this.getClass()).error("Error creating tab button", e);
			button = new StandardButton();
		}
		catch (IllegalAccessException e) {
			Logger.getLogger(this.getClass()).error("Error creating tab button", e);
			button = new StandardButton();
		}
		add(button);
		button.addCssClass("tabStripButton");
		button.setLabel(tab.getHeaderLabel());
		button.setTooltip(tab.getHeaderTooltip());
		button.setSrc(tab.getHeaderImage());
		button.setActionListener(new TabChangeListener(tab));
		return button;
	}

	public void setButtonImpl(Class<? extends Button> buttonClass) {
		this.buttonClass = buttonClass;
	}

	/**
	 * @param tab
	 * @param headerLabel
	 */
	protected void setHeaderLabel(Tab tab, String headerLabel) {
		if (buttons.get(tab) != null)
			buttons.get(tab).setLabel(headerLabel);
	}

	/**
	 * @param tab
	 * @param headerStyle
	 */
	protected void setHeaderStyle(Tab tab, Style headerStyle) {
		if (buttons.get(tab) != null)
			buttons.get(tab).setStyle(headerStyle);
	}

	/**
	 * @param tab
	 * @param headerCSSClass
	 */
	protected void addHeaderCSSClass(Tab tab, String headerCSSClass) {
		if (buttons.get(tab) != null)
			buttons.get(tab).addCssClass(headerCSSClass);
	}

	/**
	 * @param tab
	 * @param headerTooltip
	 */
	protected void setHeaderTooltip(Tab tab, String headerTooltip) {
		if (buttons.get(tab) != null)
			buttons.get(tab).setTooltip(headerTooltip);
	}

	/**
	 * @param tab
	 * @param headerImage
	 */
	protected void setHeaderImage(Tab tab, String headerImage) {
		if (buttons.get(tab) != null)
			buttons.get(tab).setSrc(headerImage);
	}
}
