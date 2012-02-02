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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.ICanvasWindowListener;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * The Wizard goes through several steps and (Dialogs) and provides hooks to add some functionality between those steps.
 * 
 * This component consists of several other components: - a canvas to include all other items (surrounding canvas) - a canvas for
 * the title - a tabstring for the content (the steps in the wizard) - a canvas including buttons for navigation in the steps.
 * 
 * @author Arno
 * 
 */
public abstract class WizardPopup extends AbstractPopup implements IWizard {

	private final int height2;

	/**
	 * @param factory
	 * @param page
	 * @param titel
	 * @param width
	 * @param height
	 */
	public WizardPopup(WebGuiFactory factory, Page page, String titel, int width, int height) {
		super(factory, page, titel, width, height);
		height2 = height;
	}

	public static final String WIZARD_NEXT_BUTTON_ID = "WIZARD_NEXT_BUTTON_ID";
	public static final String WIZARD_FINISH_BUTTON_ID = "WIZARD_FINISH_BUTTON_ID";
	public static final String WIZARD_CANCEL_BUTTON_ID = "WIZARD_CANCEL_BUTTON_ID";
	public static final String WIZARD_BACK_BUTTON_ID = "WIZARD_BACK_BUTTON_ID";
	// Constant for the array of navButtons
	public static final int BACK_BUTTON = 0;
	public static final int NEXT_BUTTON = 1;
	public static final int FINISH_BUTTON = 2;
	public static final int CANCEL_BUTTON = 3;

	// display mode like Canvas displayModes
	// should be modal
	// default to model window (with window closer)
	protected ITabStrip content;
	protected IButton[] navButtons;
	protected ICanvas footer;
	// the listener for events of this component
	protected IWizardListener listener;
	protected List<ITab> tabs = new ArrayList<ITab>();
	private Map<String, IButton> additionalButtons;

	public void formulateSubTitle() {
		int tabNumber = content.getSelectedTab();
		String text = TextService.getString("wizard.step@Step") + " " + (tabNumber + 1) + " "
				+ TextService.getString("wizard.step.outof@out of") + " " + content.getTabCount() + " : "
				+ TextService.getString(content.getTab(tabNumber).getLabel());
		getViewConnector().setTitle(text);
	}

	/**
	 * the current tab (orStep in the wizard process) the use is in.
	 */
	public int currentTab() {
		return content.getSelectedTab();
	}

	/**
	 * Select a new Tab (= Step) in the wizard, show the new tab. Also controlling the enable-state of the buttons: - back is only
	 * enabled if not on first tab. - next is only enabled if not on last tab. - finish only enabled on last tab.
	 * 
	 * to override this, it is recommended to first call this method for switching to a new tab, then call setBackEnabled(..) as
	 * you like.
	 * 
	 * Also if it is necessary to jump over certain steps, just call this method with any new tabNumber.
	 * 
	 * Changing the number of tabs on the fly is not recommended. (wie auch)
	 * 
	 * @param newTab we are switching to which tab now
	 */
	public void selectTab(int newTab) {
		newTab = Math.max(0, Math.min(content.getTabCount() - 1, newTab));
		content.selectTab(newTab);
		formulateSubTitle();
		enableButton(BACK_BUTTON, newTab != 0);
		enableButton(NEXT_BUTTON, newTab < content.getTabCount() - 1);
		enableButton(FINISH_BUTTON, newTab == content.getTabCount() - 1);
	}

	/**
	 * enables or disables buttons
	 * 
	 * @param butNo the button
	 * @param enable enable = true
	 */
	public void enableButton(int butNo, boolean enable) {
		if (navButtons == null) {
			throw new WGTException("The Wizard must be initialized to call this method.");
		}
		navButtons[butNo].setDisabled(!enable);
	}

	/**
	 * checks if a button is enabled
	 * 
	 * @param butNo
	 * @return
	 */
	public boolean isButtonEnabled(int butNo) {
		if (navButtons == null) {
			throw new WGTException("The Wizard must be initialized to call this method.");
		}
		return !navButtons[butNo].isDisabled();
	}

	/**
	 * default behavior for back button just goes one tab back and sets the enable state of the button accordingly.
	 * 
	 */
	public void defaultBack() {
		int newTab = currentTab() - 1;
		// the new tab number to client
		selectTab(newTab);
	}

	/**
	 * default behavior for back button just goes one tab back and sets the enable state of the button accordingly.
	 * 
	 */
	public void defaultNext() {
		selectTab(currentTab() + 1);
	}

	/**
	 * default finish behavior. just closes the dialog
	 * 
	 */
	public void defaultFinish() {
		close();
	}

	protected ITab createStep(String title) {
		return createStep(title, null);
	}

	protected ITab createStep(String title, String id) {
		return getFactory().createTab(content, title, id);
	}

	/**
	 * default cancel behavior. just closes the dialog
	 * 
	 */
	public void defaultCancel() {
		close();
	}

	public IWizardListener getListener() {
		if (listener == null) {
			// default listener just moves tab back and forth
			listener = new WizardAdapter();
		}
		return listener;
	}

	public ITabStrip getContentStrip() {
		return content;
	}

	/**
	 * giving access to the button which are in use for the wizard, so additional settings can be made.
	 */
	public IButton getButton(int butNo) {
		return navButtons[butNo];
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.AbstractView#createControls(org.webguitoolkit.ui.base.WebGuiFactory, org.webguitoolkit.ui.controls.container.ICanvas)
	 */
	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		viewConnector.addCssClass("wgtWizard");
		viewConnector.setWindowActionListener(new ICanvasWindowListener() {
			public void onClose(ClientEvent event) {
				// Window close is equivalent to cancel button
				getListener().onCancel(event, WizardPopup.this, currentTab());
			}

			public void onMinimize(ClientEvent event) {
				// TODO Auto-generated method stub

			}

			public void onMaximize(ClientEvent event) {
				// TODO Auto-generated method stub

			}

			public void onResize(ClientEvent event) {
				// TODO Auto-generated method stube

			}
		});
		tabs = new ArrayList<ITab>();
		content = new StandardTabStrip() {
			@Override
			public void addTab(ITab tab) {
				super.addTab(tab);
				tabs.add(tab);
			}

		};
		viewConnector.add(content);
		content.setDisplayMode(StandardTabStrip.DISPLAY_MODE_TAB_NO_STRIP);
		content.addCssClass("wgtWzTabPane");

		createTabs(factory, content);

		footer = factory.createCanvas(viewConnector);
		footer.addCssClass("wgtWzFooter");
		int yPos = height2 - 30;
		footer.getStyle().addPostition(Style.POSITION_ABSOLUTE);
		footer.getStyle().addTop(yPos, Style.PIXEL);
		// the buttons inside the footer:
		IButton back = factory.createButton(footer, null, "wizard.back@Back", "wizard.back@Back", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onBack(event, WizardPopup.this, currentTab());
			}
		}, viewConnector.getId() + "_" + WIZARD_BACK_BUTTON_ID);

		IButton next = factory.createButton(footer, null, "wizard.next@Next", "wizard.next@Next", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onNext(event, WizardPopup.this, currentTab());
			}
		}, viewConnector.getId() + "_" + WIZARD_NEXT_BUTTON_ID);

		IButton cancel = factory.createButton(footer, null, "wizard.cancel@Cancel", "wizard.cancel@Cancel", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onCancel(event, WizardPopup.this, currentTab());
			}
		}, viewConnector.getId() + "_" + WIZARD_CANCEL_BUTTON_ID);

		IButton finish = factory.createButton(footer, null, "wizard.finish@Finish", "wizard.finish@Finish", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onFinish(event, WizardPopup.this, currentTab());
			}
		}, viewConnector.getId() + "_" + WIZARD_FINISH_BUTTON_ID);

		for (Entry<String, IButton> currentButtonEntry : getAdditionalButtons().entrySet()) {
			footer.add(currentButtonEntry.getValue());
		}

		// remember the button, they can be disabled in the enableButton method
		navButtons = new IButton[] { back, next, finish, cancel };
		selectTab(0);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.dialog.IWizard#setDragable(boolean)
	 */
	public void setDragable(boolean dragable) {

	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.dialog.IWizard#setListener(org.webguitoolkit.ui.controls.dialog.IWizardListener)
	 */
	public void setListener(IWizardListener listener) {
		this.listener = listener;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.dialog.IWizard#setWindowTitel(java.lang.String)
	 */
	public void setWindowTitel(String windowTitle) {
		getViewConnector().setTitle(windowTitle);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.dialog.IWizard#setDisplayMode(java.lang.String)
	 */
	public void setDisplayMode(String displayMode) {
		// TODO Auto-generated method stub

	}

	protected abstract void createTabs(WebGuiFactory factory, ITabStrip tabStrip);

	public Map<String, IButton> getAdditionalButtons() {
		if (additionalButtons == null) {
			additionalButtons = new LinkedHashMap<String, IButton>();
		}
		return additionalButtons;
	}

	/**
	 * @param additionalButtons the additionalButtons to set
	 */
	public void setAdditionalButtons(Map<String, IButton> additionalButtons) {
		this.additionalButtons = additionalButtons;
	}
}
