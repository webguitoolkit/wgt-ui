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
 * <h1>Interface for a radio button control</h1>
 * <p>
 * A radio button can be assigned to a group. A group is just a string constant. Radio buttons within a group act as a 1
 * out of many choice. 
 * Radio buttons are primarily used for yes/no selections or for selection one option from a limited 
 * small set of options (for larger selection sets a ISelect should be used).
 * For a N out of M selection ICheckBox or IMultiselect should be used.
 *
 * </p>
 * <b>Creation of Radio buttons</b><br>
 * <pre>
 * 	IRadio r1 = factory.createRadio(layout, "radiogroup1", "resource.bundle.key.yes@Yes");
 * 	//add an action listener to the first radio button
 *  r1.setActionListener(new MyActionListener());
 *	IRadio r2 = factory.createRadio(layout, "radiogroup1", "resource.bundle.key.no@No");
 * 	//add an action listener to the second radio button
 *  r2.setActionListener(new MyActionListener());
 *	//set the first radio button to checked 
 *  r1.setSelected(true);
 *  //set the second radio button to checked and unchecks first radio button 
 *  r2.setSelected(true);
 * </pre>
 * <p/>
 * <b>Event handling</b><br>
 * Radio button can trigger events, this events can be handled by action listeners.<br>
 * <pre>
 * //the action listener code
 * class MyActionListener implements IActionListener{
 *   public void onAction(ClientEvent e){
 *     //do something
 *   }
 * }
 * 
 * //code where the Radio button is created
 * //default event (onChange)
 * r1.setActionListener( new MyActionListener() );
 *  <p/>
 * <b>CSS classes:</b> wgtInputRadio
 * 
 * @author Peter
 * @author Lars
 */
public interface IRadio extends IOptionControl {

	/**
	 * @return the groups name
	 */
	String getGroup();

	/**
	 * 
	 * @param group
	 *            the groups name
	 */
	void setGroup(String group);

	/**
	 * @param label
	 *            the label of the radio
	 */
	void setLabel(String label);

	/**
	 * @return the label of the radio
	 */
	String getLabel();

}