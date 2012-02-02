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
 * <h1>Interface for the CheckBox.</h1>
 * <p>
 * Checkboxes are rendered as HTML <code>input type=checkbox</code>. 
 * They are option controls that means that the value is true or false.
 * </p>
 * <pre>
 * 	// simple CheckBox for field "selected" of business object
 * 	ICheckBox simpleCheckbox = factory.createCheckBox( layout, "selected" );
 * 	// select CheckBox
 * 	simpleCheckbox.setSelected( true );
 * 	layout.newRow();
 * 	
 * 	layout.newRow();
 * 
 * 	// create CheckBox with label
 * 	ICheckBox checkboxWithLabel = factory.createCheckBox( layout, "check2" );
 * 	checkboxWithLabel.setLabelKey( "label" );
 * 
 * </pre>
 * 
 * <p>
 * CheckBoxes can fire events when they are clicked, these events can be handled with a ActionListener.
 * To avoid that there is also a event on a element is fired that is underneath the CheckBox e.G. tabel row, 
 * you avoid event bubbling (setEventBubbling(false)).
 * </p>
 * <pre>
 * 	// simple CheckBox for field "selected" of business object
 * 	ICheckBox simpleCheckbox = factory.createCheckBox( layout, "selected" );
 * 	simpleCheckbox.setActionListener( new CheckListener() );
 * </pre>
 * <pre>
 * 	class CheckListener implements IActionListener{
 * 		public void onAction( IClientEvent event ){
 * 			// handle event here
 * 			ICheckBox checkBox = (ICheckBox)event.getSource();
 * 			checkBox.getPage().sendInfo("CheckBox clicked! selected: " + checkBox.isSelected() );
 * 		}
 * 	}
 * </pre>
 * 
 * <br>
 * CSS classes : wgtInputCheckbox
 * 
 * @author Peter
 *
 */
public interface ICheckBox extends IOptionControl {

	/**
	 * set the event bubbling to false to avoid the bubbling of the event in the DOM tree,
	 * useful for tables where you don't want to select the row when clicking the CheckBox
	 * 
	 * @param eventBubble false to stop event bubbling
	 */
	void setEventBubbling(boolean eventBubble);

	/**
	 * @param resourceKey CheckBox label
	 */
	void setLabelKey(String resourceKey );

}
