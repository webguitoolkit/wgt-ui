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



/**
 * <h1>Interface for a Textarea</h1>
 * <p>
 * Textarea is a text field with more than one line. It is mainly used as a part of a Compound,
 * this makes it easy to load and save data from and to business objects.<br>
 * </p>
 * <p>
 * A Textarea can have a list of Validators for input validation and a Converter to convert the input String to an Object.<br>
 * </p>
 * <b>Creation of a Textarea</b><br>
 * <pre>
 * // simple textarea for the business object field "description" when used in a compound
 * ITextarea descriptionField = factory.createTextarea(layout, "description" );
 * </pre>
 * <br>
 * <b>Event handling</b><br>
 * Text fields can trigger events, this events can be handled by action listeners.<br>
 * <pre>
 * //the action listener code
 * class MyActionListener implements IActionListener{
 *   public void onAction(ClientEvent e){
 *     e.getSource().getPage().sendInfo("Event happened! " + e.getParameter(0) );
 *   }
 * }
 *
 * //code where the textarea is created
 * //default event (onReturn)
 * descriptionField.setActionListener( new MyActionListener() );
 *
 * //or special events
 * descriptionField.setActionListener( new MyActionListener() );
 * descriptionField.setRegisteredActions( JSUtil.ACTION_ON_KEYDOWN + "," + JSUtil.ACTION_ON_KEYUP );
 * </pre>
 * <p>
 * <b>CSS classes:</b> wgtInputTextarea, wgtInputTextareaPopup, wgtReadonly, wgtPointerCursor
 * </p>
 *
 * @author Peter
 * @author Martin
 */
public interface ITextarea extends IFormControl {

	/**
	 *
	 * @param columns how many columns the Textarea should have
	 */
	void setColumns(int columns);


	/**
	 *
	 * @param popup display as text field with popup for more text
	 */
	void setPopup(boolean popup);

	/**
	 *
	 * @param rows how many rows to display
	 */
	void setRows(int rows);

	/**
	 * <p>
	 * sets the actions on witch the text field should react.
	 * If there is more then on action the action type can be found in the first parameter of the event.
	 * </p>
	 * <pre>
	 * //code when creating control
	 * lastnameField.setRegisteredActions( JSUtil.ACTION_ON_KEYDOWN + "," + JSUtil.ACTION_ON_KEYUP );
	 *
	 * //code in the action listener
	 * public void onAction( ClientEvent event ){
	 *   if( JSUtil.ACTION_ON_KEYDOWN.equals( e.getParameter(0) ) ){
	 *     e.getSource().getPage().sendInfo("Key Down" );
	 *   }
	 * }
	 * </pre>
	 *
	 * @param string the actions as comma separated string
	 */
	void setRegisteredActions(String string);

	/**
	 * get an optional length for this field.
	 *
	 * @return -1 is default
	 */
	int getMaxlength();

	/**
	 * set an optional length for this field. This length will be controlled by JS while entering characters
	 *
	 * @param maxlength number of characters allowed
	 */
	void setMaxlength(int maxlength);



}
