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
package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.button.StandardButton;
import org.webguitoolkit.ui.controls.layout.ILayout;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * The ButtonBar provides a clear workflow in editing data:
 * - go to edit mode
 * - edit data
 * - save data or cancel
 * 
 * It can be used as an instrument to manage transaction on a compound.
 * When the state is changed to edit mode, the onEdit() event on the listener is
 * fired and you can lock the so that nobody can edit the values at the same time.
 * 
 * Buttons: - save, delete, new, edit, cancel
 * </pre>
 * 
 * @author Arno
 * 
 */

public class ButtonBar extends FormControl implements IButtonBar {

	/**
	 * 
	 */
	public static final String ID_SEPARATOR = "_";

	// true if the buttons
	private int buttonDisplayMode = BUTTON_DISPLAY_MODE_IMAGE;

	private IButton editButton, newButton, cancelButton, saveButton, deleteButton;

	// Listener instance
	protected IButtonBarListener listener;

	// render as 3D Button
	protected boolean displayMode3D = true;

	// the key of the message to be displayed iff delete button was pressed
	// set to null or empty to disable
	protected String deleteKey;

	// comma seperated list of the name of elements which not move into
	// edit-mode (remain ro)
	protected String noedit;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public ButtonBar() {
		super();
		setCssClass("wgtButtonBar");
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public ButtonBar(String id) {
		super(id);
		setCssClass("wgtButtonBar");
	}

	/**
	 * children will be rendered at the end of the bar, after the 'normal' buttons
	 */
	public void add(IBaseControl child) {
		super.add(child);
	}

	/**
	 * generieren des HTML für die Button bar, Die button kommen jeweils in ein div tag umrundet. Es gibt kein html element, das
	 * den id selbst trägt.
	 */
	protected void startHTML(PrintWriter out) {

		ICompound comp = surroundingCompound();
		if (comp == null)
			Logger.getLogger(this.getClass()).error("Missing surrounding compound for Buttonbar id:" + getId());
		else {
			int mode = surroundingCompound().getMode();
			if (mode == Integer.MIN_VALUE)
				surroundingCompound().changeElementMode(Compound.MODE_READONLY);
		}

		// attributes class and style go on both divs in paralell
		out.println("<div " + JSUtil.at("class", getCssClass(), "wgtButtonBar") + JSUtil.atNotEmpty("style", getStyleAsString())
				+ JSUtil.atId(getId()) + ">");
	}

	protected void endHTML(PrintWriter out) {
		// close main div
		out.println("</div>");
	}

	/**
	 * tests if this bar has a particular button. The parameter must be a valid name of a button.
	 * 
	 * @see constants beginning with BUTTON_
	 * @param but
	 * @return
	 */
	public boolean hasButton(String but) {
		return getButton(but) != null;
	}

	/**
	 * Buttonbar has no model and therefor does not do load and save
	 */
	public void loadFrom(Object data) {
	}

	/**
	 * Buttonbar has no model and therefor does do load and save
	 */
	public void saveTo(Object dataObject) {
	}

	/**
	 * this changes the operating mode of the button bar.
	 */
	public void changeMode(int mode) {
		IContext ctx = getContext(); // convenience
		switch (mode) {
			case Compound.MODE_READONLY:
				// switch to read-only mode
				setVisible(BUTTON_NEW, true);
				setVisible(BUTTON_EDIT, true);
				setVisible(BUTTON_DELETE, true);
				setVisible(BUTTON_CANCEL, false);
				setVisible(BUTTON_SAVE, false);
				break;
			case Compound.MODE_EDIT:
			case Compound.MODE_NEW:
				// switch to edit mode
				setVisible(BUTTON_NEW, false);
				setVisible(BUTTON_EDIT, false);
				setVisible(BUTTON_DELETE, false);
				setVisible(BUTTON_CANCEL, true);
				setVisible(BUTTON_SAVE, true);
				break;
			default:
				throw new WGTException("invalid mode: " + mode);
		}
	}

	/**
	 * init, right after rendering. here we define our default view
	 */
	protected void init() {
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String[] getButtons() {
		ArrayList buttons = new ArrayList();
		if (hasButton(BUTTON_NEW))
			buttons.add(BUTTON_NEW);
		if (hasButton(BUTTON_EDIT))
			buttons.add(BUTTON_EDIT);
		if (hasButton(BUTTON_DELETE))
			buttons.add(BUTTON_DELETE);
		if (hasButton(BUTTON_SAVE))
			buttons.add(BUTTON_SAVE);
		if (hasButton(BUTTON_CANCEL))
			buttons.add(BUTTON_CANCEL);
		String[] buttonStrings = new String[buttons.size()];
		buttonStrings = (String[])buttons.toArray(buttonStrings);
		return buttonStrings;
	}

	public void setButtons(String buttons) {
		if (StringUtils.isEmpty(buttons)) {
			throw new WGTException("button attribute of tag ButtonBar is not allowed to be empty");
		}
		// remove any white space which might exist in the configuration string
		StringUtils.deleteWhitespace(buttons);
		setButtons(buttons.split(","));
	}

	/**
	 * @deprecated use setButtons(String)
	 */
	public void setButtons(String[] buttons) {
		List buttonList = new ArrayList(Arrays.asList(buttons));
		if (buttonList.contains(BUTTON_NEW)) {
			newButton = newButton(BUTTON_NEW, true, !(buttonList.contains(BUTTON_EDIT) || buttonList.contains(BUTTON_DELETE)));
		}
		if (buttonList.contains(BUTTON_EDIT)) {
			editButton = newButton(BUTTON_EDIT, !buttonList.contains(BUTTON_NEW), !buttonList.contains(BUTTON_DELETE));
		}
		if (buttonList.contains(BUTTON_DELETE)) {
			deleteButton = newButton(BUTTON_DELETE, !(buttonList.contains(BUTTON_EDIT) || buttonList.contains(BUTTON_NEW)), true);
			deleteButton.setConfirmMsg(TextService.getString(getDeleteKey()));
		}
		if (buttonList.contains(BUTTON_SAVE)) {
			saveButton = newButton(BUTTON_SAVE, true, !buttonList.contains(BUTTON_CANCEL));
			saveButton.setVisible(false);
		}
		if (buttonList.contains(BUTTON_CANCEL)) {
			cancelButton = newButton(BUTTON_CANCEL, !buttonList.contains(BUTTON_SAVE), true);
			cancelButton.setVisible(false);
		}
	}

	/**
	 * @param name
	 * @param first
	 * @param last
	 * @return
	 */
	private IButton newButton( String name, boolean first, boolean last){
		String buttonId = null;
		buttonId = getButtonId(getId(), name);
		StandardButton button = new StandardButton(buttonId);
		add(button);
		button.setTooltipKey(name);
		if (getButtonDisplayMode() == BUTTON_DISPLAY_MODE_IMAGE_TEXT || getButtonDisplayMode() == BUTTON_DISPLAY_MODE_IMAGE)
			button.setSrc("images/wgt/icons/" + name + ".gif");
		if (getButtonDisplayMode() == BUTTON_DISPLAY_MODE_IMAGE_TEXT || getButtonDisplayMode() == BUTTON_DISPLAY_MODE_TEXT)
			button.setLabelKey(name);
		if (first && !last) {
			button.setAlignment(StandardButton.POSITION_LEFT);
		}
		else if (!first && !last) {
			button.setAlignment(StandardButton.POSITION_MIDDLE);
		}
		else if (!first && last) {
			button.setAlignment(StandardButton.POSITION_RIGHT);
		}
		button.setActionListener(new ButtonListener(name));
		return button;
	}

	public static String getButtonId(String buttonBarId, String buttonName) {
		String buttonId = null;
		// make sure that we get a valid id which we can use before we construct our own id for the button
		if (StringUtils.isNotEmpty(buttonBarId)) {
			buttonId = buttonBarId + ID_SEPARATOR + buttonName.toUpperCase();
		}
		return buttonId;
	}

	public IButtonBarListener getListener() {
		return listener;
	}

	public void setListener(IButtonBarListener listener) {
		this.listener = listener;
	}

	public String getValue() {
		return null;
	}

	public String getDeleteKey() {
		if (deleteKey == null) {
			// user omitted deleteKey attribute so use default message
			return "default.deleteMessage";
		}
		return deleteKey;
	}

	public void setDeleteKey(String deleteKey) {
		this.deleteKey = deleteKey;
		if (deleteButton != null)
			deleteButton.setConfirmMsg(TextService.getString(deleteKey));
	}

	/**
	 * set individual buttons to visible invisible use the Constants for this class for buttonnames.
	 */
	public void setVisible(String button, boolean vis) {
		if (getButton(button) != null)
			getButton(button).setVisible(vis);
	}

	public boolean isVisible(String button) { // visibility is true unless set to false
		if (getButton(button) != null)
			return getButton(button).isVisible();
		return false;
	}

	/**
	 * sets the buttons to disabled mode
	 * 
	 * @param disabled true if the buttons are disabled false to enabele the buttons
	 * @param buttons Array of the buttons to disable/enable if null all buttons will change mode
	 */
	public void setDisabled(boolean disabled, String[] buttons) {
		if (buttons == null) {
			buttons = new String[] { BUTTON_SAVE, BUTTON_CANCEL, BUTTON_NEW, BUTTON_EDIT, BUTTON_DELETE };
		}
		for (int i = 0; i < buttons.length; i++) {
			if (getButton(buttons[i]) != null)
				getButton(buttons[i]).setDisabled(disabled);
		}
	}

	public void setDisplayMode3D(boolean displayMode3D) {
		this.displayMode3D = displayMode3D;
	}

	public boolean getDisplayMode3D() {
		return displayMode3D;
	}

	protected IButton getButton(String buttonName) {
		if (BUTTON_SAVE.equals(buttonName))
			return saveButton;
		else if (BUTTON_CANCEL.equals(buttonName))
			return cancelButton;
		else if (BUTTON_NEW.equals(buttonName))
			return newButton;
		else if (BUTTON_EDIT.equals(buttonName))
			return editButton;
		else if (BUTTON_DELETE.equals(buttonName))
			return deleteButton;
		else
			throw new WGTException("unknown button name, got " + buttonName);
	}

	public void setButtonDisplayMode(int buttonDisplayMode) {
		this.buttonDisplayMode = buttonDisplayMode;
	}

	public int getButtonDisplayMode() {
		return buttonDisplayMode;
	}

	/**
	 * @author Arno
	 * 
	 */
	public class ButtonListener implements IActionListener {
		String name;

		public ButtonListener(String name) {
			this.name = name;
		}

		public void onAction(ClientEvent event) {
			if (BUTTON_SAVE.equals(name))
				getListener().onSave(event);
			else if (BUTTON_CANCEL.equals(name))
				getListener().onCancel(event);
			else if (BUTTON_NEW.equals(name))
				getListener().onNew(event);
			else if (BUTTON_EDIT.equals(name))
				getListener().onEdit(event);
			else if (BUTTON_DELETE.equals(name))
				getListener().onDelete(event);
			else
				throw new WGTException("unknown button name, got " + name);
		}
	}

	/**
	 * sets the layout for the controls displayed in the tab (GridLayout, BorderLayout, ... )
	 * 
	 * @param layout the layout to be set
	 */
	public void setLayout(ILayout layout) {
		super.setLayout(layout);
	}

	public IButton getEditButton() {
		return editButton;
	}

	public IButton getNewButton() {
		return newButton;
	}

	public IButton getCancelButton() {
		return cancelButton;
	}

	public IButton getSaveButton() {
		return saveButton;
	}
	public IButton getDeleteButton() {
		return deleteButton;
	}
}
