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

import java.io.PrintWriter;

import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.ICanvasWindowListener;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.style.IStyleChangeListener;
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
public class Wizard extends BaseControl implements IWizard {

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
	private String displayMode = Canvas.DISPLAY_MODE_WINDOW_MODAL;
	protected ITabStrip content;
	protected IButton[] navButtons;
	protected ICanvas wzDialog = new Canvas();
	protected ICanvas footer;
	// the listener for events of this component
	protected IWizardListener listener;
	// width & height of overall structure (outer measures) in pixel
	protected int width = 800;
	protected int height = 300;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public Wizard() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Wizard(String id) {
		super(id);
	}

	/**
	 * in the init method we will create our subcomponents TODO FIXME_AS rewrite with new dynamic API.
	 */
	@Override
	protected void init() {
		WebGuiFactory factory = WebGuiFactory.getInstance();
		wzDialog = new Canvas();
		this.add(wzDialog);
		wzDialog.setWindowActionListener(new ICanvasWindowListener() {
			public void onClose(ClientEvent event) {
				// Window close is equivalent to cancel button
				getListener().onCancel(event, Wizard.this, currentTab());
			}

			public void onMinimize(ClientEvent event) {
				// TODO Auto-generated method stub

			}

			public void onMaximize(ClientEvent event) {
				// TODO Auto-generated method stub

			}

			public void onResize(ClientEvent event) {
				// TODO Auto-generated method stub

			}
		});
		wzDialog.setDisplayMode(displayMode);
		wzDialog.setHeight(height);
		wzDialog.setWidth(width);
		wzDialog.setVisible(true);
		wzDialog.addCssClass(getCssClass());

		content = factory.createTabStrip(wzDialog);
		content.addCssClass("wgtWzTabPane");
		content.setDisplayMode(StandardTabStrip.DISPLAY_MODE_TAB_NO_STRIP);

		footer = factory.createCanvas(wzDialog);
		footer.addCssClass("wgtWzFooter");
		int yPos = height - 30;
		// footer.setStyle("position: absolute; top: "+yPos+"px;");
		footer.getStyle().addPostition(Style.POSITION_ABSOLUTE);
		footer.getStyle().addTop(yPos, Style.PIXEL);
		// the buttons inside the footer:
		IButton back = factory.createButton(footer, null, "wizard.back@Back", "wizard.back@Back", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onBack(event, Wizard.this, currentTab());
			}
		}, getId() + "_" +WIZARD_BACK_BUTTON_ID);

		IButton next = factory.createButton( footer, null, "wizard.next@Next", "wizard.next@Next", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onNext(event, Wizard.this, currentTab());
			}
		}, getId() + "_" + WIZARD_NEXT_BUTTON_ID );

		IButton cancel = factory.createButton(footer, null, "wizard.cancel@Cancel", "wizard.cancel@Cancel", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onCancel(event, Wizard.this, currentTab());
			}
		}, getId() + "_" +WIZARD_CANCEL_BUTTON_ID);

		IButton finish = factory.createButton(footer, null, "wizard.finish@Finish", "wizard.finish@Finish", new IActionListener() {
			public void onAction(ClientEvent event) {
				getListener().onFinish(event, Wizard.this, currentTab());
			}
		}, getId() + "_" +WIZARD_FINISH_BUTTON_ID);

		// remember the button, they can be disabled in the enableButton method
		navButtons = new IButton[] { back, next, finish, cancel };
	}

	public void addTab(ITab tab) {
		content.addTab(tab);
	}

	@Override
	protected void endHTML(PrintWriter out) {
	}

	@Override
	public void draw(PrintWriter out) {
		((Canvas)wzDialog).drawCanvas(out);
		selectTab(0);
	}

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	public void formulateSubTitle() {
		int tabNumber = content.getSelectedTab();
		String text = TextService.getString("wizard.step@Step") + " " + (tabNumber + 1) + " "
				+ TextService.getString("wizard.step.outof@out of") + " " + content.getTabCount() + " : "
				+ TextService.getString(content.getTab(tabNumber).getLabel());
		wzDialog.setTitle(text);
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
		setVisible(false);
	}

	/**
	 * default cancel behavior. just closes the dialog
	 * 
	 */
	public void defaultCancel() {
		setVisible(false);
	}

	/**
	 * sets the dialog to visible / invisible
	 * 
	 * @param butNo
	 * @return
	 */
	@Override
	public void setVisible(boolean vis) {
		wzDialog.setVisible(vis);
	}

	public IWizardListener getListener() {
		if (listener == null) {
			// default listener just moves tab back and forth
			listener = new WizardAdapter();
		}
		return listener;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		wzDialog.setHeight(height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		wzDialog.setWidth(width);
	}

	public void setListener(IWizardListener listener) {
		this.listener = listener;
	}

	@Override
	public String getTooltip() {
		return wzDialog.getTooltip();
	}

	public boolean isDragable() {
		return wzDialog.isDragable();
	}

	@Override
	public boolean isVisible() {
		return wzDialog.isVisible();
	}

	public void setDragable(boolean dragable) {
		wzDialog.setDragable(dragable);
	}

	public void setHPosition(int hPos) {
		wzDialog.setHPosition(hPos);
	}

	@Override
	public void setTooltip(String tooltip) {
		wzDialog.setTooltip(tooltip);
	}

	public void setVPosition(int vPos) {
		wzDialog.setVPosition(vPos);
	}

	public void setWindowTitel(String windowTitle) {
		wzDialog.setTitle(windowTitle);
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

	@Override
	protected void registerStyleChangeListener() {
		style.registerStyleChangedListener(new WizardChangedListener());
	}

	class WizardChangedListener implements IStyleChangeListener {
		public void handle(int eventType, String attribute) {
		}

		public String getListenerId() {
			return getId();
		}
	}

	@Override
	public void addCssClass(String cssClass) {
		super.addCssClass(cssClass);
		wzDialog.addCssClass(cssClass);
	}

	public void close(){
		setVisible(false);
	}
	
	public ICanvas getCanvas(){
		return wzDialog;
	}
}
