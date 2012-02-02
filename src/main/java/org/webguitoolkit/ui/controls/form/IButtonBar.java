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
package org.webguitoolkit.ui.controls.form;

import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.layout.ILayoutable;

/**
 * <h1>Interface for ButtonBar.</h1>
 * <p>
 * A ButtonBar is a combination of Buttons intended for standard CRUD processing in combination with a Compound.<br>
 * If a ButtonBar is set for a compound (as child in the object hierarchy), the compound and its elements are initially displayed
 * readonly.
 * </p>
 * <p>
 * The ButtonBar provides a clear work-flow in editing data: - go to edit mode - edit data - save data or cancel It can be used as
 * an instrument to manage transaction on a compound. When the state is changed to edit mode, the onEdit() event on the listener
 * is fired and you can lock the object so that nobody can edit the values at the same time.
 * </p>
 * <p>
 * Buttons: - save, delete, new, edit, cancel
 * </p>
 * <p>
 * In readonly mode following buttons are displayed: new, edit, delete<br>
 * In new or edit mode following buttons are displayed: save, cancel
 * </p>
 * <p>
 * A ButtonBar is a composite, this means that it can contain controls as children, this makes it possible to add buttons and
 * input elements to the ButtonBar. Do not try to add complex layouts, tables or trees to the ButtonBar, that will probably look
 * very ugly.
 * </p>
 * <b>Creation of a ButtonBar:</b><br>
 * 
 * <pre>
 * // creates a ButtonBar with the given parent (layout or compound) and the buttons as comma separated list
 * // and a ButtonBarListener.
 * IButtonBar buttonBar = getFactory().createButtonBar(parent, &quot;edit,save,cancel&quot;, new ButtonBarListener());
 * 
 * // creates a ButtonBar with the given parent (layout or compound), the buttons as String array,
 * // the displayMode (only icons, icons with text or only text) and a ButtonBarListener.
 * IButtonBar buttonBar = getFactory().createButtonBar(parent, new String[] { IButtonBar.BUTTON_SAVE, IButtonBar.BUTTON_EDIT },
 * 		IButtonBar.BUTTON_DISPLAY_MODE_IMAGE, new ButtonBarListener());
 * </pre>
 * 
 * <br>
 * <b>The Listener</b><br>
 * <p>
 * The Interface IButtonBarListener provides following methods: onNew(), onSave(), onEdit(), onDelete(), onCancel(). This methods
 * are called when clicking the corresponding button. If you use this interface you have to implement the interaction with the
 * compound your self (onNew -> change the compounds mode to "new", ...).
 * </p>
 * 
 * <pre>
 * public class ButtonBarListener implements IButtonBarListener {
 * 	// new button clicked
 * 	public void onNew(IClientEvent event) {
 * 		...
 * 	}
 * 
 * 	// save button clicked
 * 	public void onSave(IClientEvent event) {
 * 		...
 * 	}
 * 
 * 	// edit button clicked
 * 	public void onEdit(IClientEvent event) {
 * 		...
 * 	}
 * 
 * 	// delete button clicked
 * 	public void onDelete(IClientEvent event) {
 * 		...
 * 	}
 * 
 * 	// cancel button clicked
 * 	public void onCancel(IClientEvent event) {
 * 		...
 * 	}
 * }
 * 
 * </pre>
 * 
 * <b>The AbstractButtonBarListener</b><br>
 * <p>
 * The AbstractButtonBarListener implements the IButtonBarListener in a standard way. It is implemented for optimistic locking.
 * You just have to take care for model issues like deleting, creating, saving or refreshing the business object.
 * </p>
 * 
 * <pre>
 * public class ButtonBarListener extends AbstractButtonBarListener {
 * 	public ButtonBarListener(ICompound compound) {
 * 		super(compound);
 * 	}
 * 
 * 	// deletes the business object from the database
 * 	public boolean delete(Object delegate) {
 * 		delegate.markDeleted();
 * 		persistenceManager.commit();
 * 	}
 * 
 * 	// creates a new business object
 * 	public Object newDelegate() {
 * 		return new MyBusinessObject();
 * 	}
 * 
 * 	// commits changes to the database
 * 	public int persist() {
 * 		try {
 * 			persistenceManager.commit();
 * 			return SAVE_OK;
 * 		}
 * 		catch (StaleObjectException e) {
 * 			return SAVE_CONCURRENT_MODIFICATION;
 * 		}
 * 		catch (PersistenceException e) {
 * 			return SAVE_ERROR;
 * 		}
 * 	}
 * 
 * 	// refresh the delegates state from the database
 * 	public boolean refresh(Object delegate) {
 * 		persistenceManager.refresh(delegate);
 * 	}
 * }
 * </pre>
 * 
 * <br>
 * CSS classes : wgtButtonBar
 * 
 * @author Peter
 * 
 * @see org.webguitoolkit.ui.controls.form.ICompound
 * @see org.webguitoolkit.ui.controls.form.IButtonBarListener
 * @see org.webguitoolkit.ui.controls.form.AbstractButtonBarListener
 */
public interface IButtonBar extends IFormControl, IComposite, ILayoutable {

	String BUTTON_SAVE = "save";
	String BUTTON_CANCEL = "cancel";
	String BUTTON_NEW = "new";
	String BUTTON_EDIT = "edit";
	String BUTTON_DELETE = "delete";

	String BUTTON_EDIT_CANCEL_SAVE = BUTTON_EDIT + "," + BUTTON_CANCEL + "," + BUTTON_SAVE;
	String BUTTON_NEW_EDIT_CANCEL_SAVE = BUTTON_NEW + "," + BUTTON_EDIT_CANCEL_SAVE;
	String BUTTON_EDIT_DELETE_CANCEL_SAVE = BUTTON_DELETE + "," + BUTTON_EDIT_CANCEL_SAVE;
	String BUTTON_NEW_EDIT_DELETE_CANCEL_SAVE = BUTTON_NEW + "," + BUTTON_EDIT_DELETE_CANCEL_SAVE;

	/**
	 * display the buttons just as images
	 */
	int BUTTON_DISPLAY_MODE_IMAGE = 0;
	/**
	 * display the buttons as images with text
	 */
	int BUTTON_DISPLAY_MODE_IMAGE_TEXT = 1;
	/**
	 * display the buttons just as text
	 */
	int BUTTON_DISPLAY_MODE_TEXT = 2;

	/**
	 * Control which buttons are in use
	 * 
	 * @param buttons comma separated list of "save,delete,new,edit,cancel"
	 */
	void setButtons(String buttons);

	/**
	 * Sets the listener for the standard buttons of the ButtonBar
	 * 
	 * @param listener the listener for the click events of the ButtonBar
	 */
	void setListener(IButtonBarListener listener);

	/**
	 * Sets resource bundle key for confirmation message when clicking the delete button.
	 * 
	 * @param deleteKey resource bundle key
	 */
	void setDeleteKey(String deleteKey);

	/**
	 * set individual buttons to visible invisible use the Constants for this class for buttonnames.
	 * 
	 * @param button the button to make visible/invisible
	 * @param vis true for visible, false for invisible
	 */
	void setVisible(String button, boolean vis);

	/**
	 * sets the buttons to disabled mode
	 * 
	 * @param disabled true if the buttons are disabled false to enabele the buttons
	 * @param buttons Array of the buttons to disable/enable if null all buttons will change mode
	 */
	void setDisabled(boolean disabled, String[] buttons);

	/**
	 * Used for the icon buttons, the displayMode3D gives the buttons a 3D look (default is true)
	 * 
	 * @param displayMode3D if the button is displayed in 3D
	 */
	void setDisplayMode3D(boolean displayMode3D);

	/**
	 * Used for the standard button, you can set the buttonbar to display its buttons as image, image with text or just text.
	 * 
	 * @param buttonDisplayMode the display mode of the buttons
	 * 
	 * @see IButtonBar#BUTTON_DISPLAY_MODE_IMAGE
	 * @see IButtonBar#BUTTON_DISPLAY_MODE_IMAGE_TEXT
	 * @see IButtonBar#BUTTON_DISPLAY_MODE_TEXT
	 */
	void setButtonDisplayMode(int buttonDisplayMode);

	IButtonBarListener getListener();

}
