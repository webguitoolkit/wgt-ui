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


/**
 * <h1>Interface for the Wizard Control</h1>
 * <p>
 * A Wizard is a sequence of Tabs. The Wizard goes through several steps and (Dialogs)
 * and provides hooks to add some functionality between those steps.
 * </p>
 * This component consists of several other components: - a canvas to include all other items (surrounding canvas) - a
 * canvas for the title - a TabString for the content (the steps in the wizard) - a canvas including buttons for
 * navigation in the steps. <p/>
 * 
 * All Display Modes available for a ICanvas are also available for the Wizard Control.<br>  	
 * <p/>
 * <b>Creation of a Wizard Control</b><br>
 * <pre>
 * 		//creates the wizard control
 * 		IWizard wizard = factory.createWizard( layout );
 * 		//sets the display mode of the surrounding canvas of the wizard control
 * 		wizard.setDisplayMode(ICanvas.DISPLAY_MODE_WINDOW);
 * 		//sets the wizard header title
 *      wizard.setWindowTitel("My Wizard Demo");
 *      //wizard can now be dragged in the browser with the mouse
 *      wizard.setDragable(true);
 *
 * 		//adds first tab to the wizard
 * 		ITab tab = factory.createTab(wizard, "First Tab", null);
 * 		factory.createLabel(tab, "First Tab");
 * 		//adds second tab to the wizard		
 * 		tab = factory.createTab(wizard, "Second Tab", null);
 * 		factory.createLabel(tab, "Second Tab");
 * 		//adds third tab to the wizard
 * 		tab = factory.createTab(wizard, "Third Tab", null);
 * 		factory.createLabel(tab, "Third Tab");
 * 
 * 		//selects a specific (Third Tab) tab of the wizard as activ
 * 		wizard.selectTab(2);
 * </pre>
 * <p/>
 * <b>Event handling</b><br>
 * Wizard controls can trigger events, this events can be handled by wizard listeners.<br>
 * <pre>
 * 		class WizListener implements IWizardListener{
 *			@Override
 *			public void onBack(ClientEvent event, Wizard wiz, int currentTabNo) {
 *				// do something
 *			}
 *
 *			@Override
 *			public void onCancel(ClientEvent event, Wizard wiz, int currentTabNo) {
 *				// do something
 *			}
 *
 *			@Override
 *			public void onFinish(ClientEvent event, Wizard wiz, int currentTabNo) {
 *				// do something
 *			}
 *
 *			@Override
 *			public void onNext(ClientEvent event, Wizard wiz, int currentTabNo) {
 *				// do something
 *			}
 *		}
 * 
 * 		//code where the text field is created
 * 		//default event (onClick)
 * 		wizard.setListener( new WizListener() );
 * </pre>
 * <p/>
 * <b>CSS classes :</b> wgtWzFooter
 * 
 * @author Peter
 * @author Lars
 */
public interface IWizard {
	/**
	 * @param displayMode
	 *            use the ICanvas DisplayMode constants 
	 */
	void setDisplayMode(String displayMode);

	/**
	 * Select a new Tab (= Step) in the wizard, show the new tab. Also controlling the enable-state of the buttons: -
	 * back is only enabled if not on first tab. - next is only enabled if not on last tab. - finish only enabled on
	 * last tab.
	 * 
	 * to override this, it is recommended to first call this method for switching to a new tab, then call
	 * setBackEnabled(..) as you like.
	 * 
	 * Also if it is necessary to jump over certain steps, just call this method with any new tabNumber.
	 * 
	 * Changing the number of tabs on the fly is not recommended. 
	 * 
	 * @param newTab
	 *            switches to another tab 
	 */
	void selectTab(int newTab);

	/**
	 * enables or disables buttons
	 * 
	 * @param butNo
	 *            the button number
	 * @param enable
	 *            enable = true
	 */
	void enableButton(int butNo, boolean enable);

	/**
	 * 
	 * @param listener to be called 
	 */
	void setListener(IWizardListener listener);

	/**
	 * 
	 * @param dragable true = can be dragged
	 */
	void setDragable(boolean dragable);

	/**
	 * @param windowTitle how the window is named. No resource key possible.  
	 */
	void setWindowTitel(String windowTitle);
	
	/**
	 * @return the index of the current tab
	 */
	int currentTab();
	
	/**
	 * close the wizard
	 */
	void close();
}