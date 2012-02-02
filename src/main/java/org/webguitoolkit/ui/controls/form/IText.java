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
 * <h1>Interface for a text field</h1>
 * <p>
 * A Text is a form control for text input. It is mainly used as a part of a Compound, 
 * this makes it easy to load and save data from and to business objects.<br>
 * </p>
 * <p>
 * A Text can have a list of Validators for input validation and a Converter to convert the input String to an Object.<br>
 * </p>
 * <p>
 * If a DateConverter or DateTimeConverter is set as converter, a DatePicker icon is added to the text field.<br>
 * </p>
 * <b>Creation of a Text field</b><br>
 * <pre>
 * // simple text field for the business object field "firstName" when used in a compound
 * IText firstnameField = factory.createText(layout, "firstName" );
 * 
 * // the label is displayed in the style wgtLabelFor
 * ILabel lastnameLabel = factory.createLabel(layout, "resource.bundle.key.lastName");
 * // add the label to the text field
 * IText lastnameField = factory.createText(layout, "lastName", lastnameLabel );
 * // alternatively call the lastnameField.setDecribingLabel() after creation of the text field
 * 
 * // date field
 * IText birthDateField = factory.createText(layout, "birthDate" );
 * // add converter to displays the DatePicker icon
 * bithDateFile.setConverter(ConvertUtil.DATE_CONVERTER);
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
 * //code where the text field is created
 * //default event (onReturn)
 * firstnameField.setActionListener( new MyActionListener() );
 * 
 * //special events
 * lastnameField.setActionListener( new MyActionListener() );
 * lastnameField.setRegisteredActions( JSUtil.ACTION_ON_KEYDOWN + "," + JSUtil.ACTION_ON_KEYUP );
 * </pre>
 * <p>
 * <b>CSS classes:</b> wgtInputText, wgtInputTextWithButton, wgtReadonly
 * </p>
 * 
 * @author Peter
 * @author Martin
 */
public interface IText extends IFormControl {
	/**
	 * <p>
	 * The the HTML attribute '<code>maxlenth</code>' on the text field. This
	 * means that you can only type the specified number of characters into the
	 * text field.
	 * </p>
	 * This method can only be called in the creation phase.<br>
	 * 
	 * @param maxlength
	 *            the maximum number of characters
	 */
	void setMaxlength(int maxlength);

	/**
	 * <p>
	 * Flag that indicates that the text field contains a password.
	 * This means that the input is shown as a series of stars or dots in the text field.
	 * </p>
	 * This method can only be called in the creation phase.<br>
	 * 
	 * @param password true if the field is a password field
	 */
	void setPassword(boolean password);

	/**
	 * <p>
	 * Sets the width of the text field.
	 * </p>
	 * This method can only be called in the creation phase.<br>
	 *  
	 * @param size the width of the text field
	 * @deprecated use styles instead
	 */
	void setSize(int size);

	/**
	 * <p>
	 * Sets the display mode of the text field.
	 * If not editable, the text field is rendered as label.
	 * </p>
	 * This method can only be called in the creation phase.<br>
	 * 
	 * @param editable if the content of the text field can change
	 */
	void setEditable(boolean editable);

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
	 * set the name attribute to allow the browser to cache the input for suggesting Texts as for example "username, country, ..."
	 * @param name the html name attribut
	 */
	void setHTMLName( String name );

	/**
	 * marks all text in the text box (on the browser)
	 */
	void mark();

}
