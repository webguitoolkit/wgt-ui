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

import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.IActionControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.layout.ILayoutable;

/**
 * A Compound is a kind of controller for UI FormControls like Text, Button, Radio, etc. that are grouped in a Composite. It can
 * be compared with a FORM element in HTML. The Compound allows to perform operations on all associated controls easily, e.g. to
 * switch from input to read-only mode. The Compound also associates the controls content from an underlying data bags by means of
 * their attribute names. For example if you add a text field with the name "age" the compound it will be associated with the
 * <code>age</code> in your bean "automagically". <br>
 * The compound itself is not visible but is automatically added to all FormControls underneath in the component tree. You can
 * also add FormControls "by hand" when using formControl.setCompound(theCompound). <br>
 * A Compound can be created via the factory and will be attached to the Composite that was provided as parameter to the factory.
 * 
 * <pre>
 * // initialize view ...
 * // create a canvas at the view
 * ICanvas canvas = factory.createCanvas(myView);
 * // now add a compound to the canvas
 * ICompound compound = factory.createCompound(canvas);
 * // and assign a DataBag
 * compound.setBag(bag);
 * // create a button bar to interact with the compound. Tell the button bar to have edit, save and cancel buttons.
 * factory.createButtonBar(compound, &quot;edit,cancel,save&quot;, new ButtonBarListener(compound));
 * </pre>
 * 
 * The ButtonBar can be used very easily with the compound. <br>
 * Later on the controls are added to the Compound. <br>
 * 
 * <pre>
 * IText text = factory.createText(compound, &quot;name&quot;);
 * </pre>
 * 
 * Loading the UI controls content is done by
 * 
 * <pre>
 *   compound.load() call.
 * </pre>
 * 
 * The load() call transfers the content from the DataBag into the UI controls (e.g. the Text field). In the ButtonBarListener you
 * can implement various methods to fine control the processing. For details
 * 
 * see AbstractButtonBarListener <br>
 * <br>
 * <b>Interaction of Compound, ButtonBar and Controls</b> <br>
 * This diagram shows the interaction between control, buttons, compound, databag, ...
 * 
 * <pre>
 * T B D
 * </pre>
 * 
 * @author Peter
 * @author Martin
 * 
 * @see org.webguitoolkit.ui.controls.form.IFormControl
 * @see org.webguitoolkit.ui.base.IDataBag
 * @see org.webguitoolkit.ui.controls.form.AbstractButtonBarListener
 */

public interface ICompound extends IComposite, ILayoutable, IActionControl {

	/**
	 * FormControls are read-only. The ButtonBar shows the edit, new and delete buttons if defined.
	 */
	int MODE_READONLY = 0;
	/**
	 * All non final FormControls are edit-able. The ButtonBar shows the save and cancel buttons.
	 */
	int MODE_EDIT = 1;
	/**
	 * All FormControls are edit-able. The ButtonBar shows the save and cancel buttons.
	 */
	int MODE_NEW = 2;

	/**
	 * Event type that is fired when a compound is loaded
	 */
	int EVENT_COMPOUND_LOAD = 5;

	/**
	 * @return the underlying model. If there is no model yet a DefaultCompoundModel will be created and returned.
	 */
	ICompoundModel getModel();

	/**
	 * Method to be called to load the compound object. This initializes the fields with the values from the bean.
	 */
	void load();

	/**
	 * This must be called from the (action listener) of the associated buttons to be executed it is not called automatically. It
	 * saves the context to the dataObject(s) if possible, that a formElement is not bound to a data object not action is taken.
	 * (you must save those item by yourself)
	 * 
	 */
	void save();

	/**
	 * Saves the data in the compound elements into the given dataObject. The dataObject may have associations to other objects,
	 * which are used depending on the property-settings of the form elements.
	 * 
	 * @param dataObject the object to save the content to
	 */
	void saveTo(Object dataObject);

	/**
	 * Returns the mode which was last set using chgAllMode
	 * 
	 * @return the mode or Integer.MIN_VALUE if not set.
	 */
	int getMode();

	/**
	 * This methods is a convenience method if you use the compound in the following (recommended) way: The model is a default
	 * model which holds a DataBag instance as modelObject.
	 * 
	 * The method will return the DataBag, which in the object of the model It will create such a bag if it didn't exist.
	 * 
	 * @return the business object wrapper
	 */
	IDataBag getBag();

	/**
	 * Set the Bag of the compound.
	 * 
	 * @param bag the bag
	 */
	void setBag(IDataBag bag);

	/**
	 * @return true if there where any errors.
	 */
	boolean hasErrors();

	/**
	 * @return the error list if any
	 */
	List getErrorList();

	/**
	 * register a listener that is called when the load function of a compound is called
	 * 
	 * @param listener the load listener
	 */
	void registerLoadListener(IServerEventListener listener);

	/**
	 * changes the display mode of all elements of the compound
	 * 
	 * @param mode can be found in the constants of the interface
	 */
	void changeElementMode(int mode);

	/**
	 * adds the Element to the list of this compound to be included in the life-cycle of this compound. This is typically an
	 * FormControl.
	 * 
	 * @param lifecycleElement an element the participates of the compound life-cycle functions
	 */
	void addElement(ICompoundLifecycleElement lifecycleElement);

	/**
	 * get the Page on which this compound is rendered.
	 * 
	 * @return current Page
	 */
	Page getPage();

	/**
	 * Finds an FormControl element which was added to the compound
	 * 
	 * @param prop of element to be found
	 * @return found element
	 */
	FormControl findElementByProperty(String prop);

	/**
	 * Add an error to the compound. this will be displayed when trying to save the bag
	 * 
	 * @param msg for error
	 */
	void addError(String msg);

	/**
	 * adds an error message to the output. the property parameter can be null, if no specific element if affected. If not null
	 * the label of that element is taken to prepend the message
	 * 
	 * @param msg for error
	 * @param property for the property parameter
	 */
	void addError(String msg, String property);

	/**
	 * clears all elements in the compound
	 */
	void clearElements();

	/**
	 * A compound may have error messages which are shown in a dialog window. A name for addressing this dialog window can be
	 * given.
	 * 
	 * @return
	 */
	String getErrorDialogName();

	void setErrorDialogName(String newErrorDialogName);

	void removeListener(int eventtype, IServerEventListener liz);

	/**
	 * clear all errors on client side. This must be called from the application, as we don't know when this should take place (in
	 * absence)
	 */
	void clearErrors();
}
