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

import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * <h1>Interface for a Label element</h1>
 * <p>
 * A Label can be used to present any text on the UI.<br/> 
 * It can be associated with another FormControl. 
 * When the label is associated with another control it is displayed with the style "wgtLabelFor" 
 * and is used as validation hint when the form control has validation errors.
 * </p>
 * <b>Creation of a label element</b><br>
 * <pre>
 * 	//creates a label element
 * 	ILabel label = factory.createLabel(layout, "resource.bundle.key.lastName");
 * 	//adds a mouseover tooltip to the label
 * 	label.setTooltip("This is a tooltip on a label");
 * 	//changes the text of the label without using the resource bundle
 * 	label.setText("HelloLabelText");
 * 	//associates the label with another FormControl
 * 	IText lastnameField = factory.createText(layout, "lastName", label);
 * 	//changes the look the label with associated controll to the basic label look 
 * 	label.setLabelForFormControl(false);
 * </pre>
 * <p/>
 * <b>Event handling</b><br>
 * Label elements can't trigger events, therefore no listeners can be added.<br>
 * <p/>
 * <b>CSS classes :</b> wgtLabelFor, wgtLabel
 * 
 * @author Peter
 * @author Lars
 */
public interface ILabel extends IBaseControl {

	/**
	 * Set the resource bundle key e.g. "welcome.string@Hello my friend"
	 */
	void setTextKey(String textKey);

	/**
	 * same as setValue
	 */
	void setText(String text);

	/**
	 * @param b indicates if this is a label for another control
	 * Set this to true if you want a label without an associated control to like a label which is associated to another control.
	 * Set this to false if you want a label with an associated control to like  a label which is not associated to another control.
	 * True sets the CSS style class of the label to "wgtLabelFor", while false sets it to the default value "wgtLabel"
	 */
	void setLabelForFormControl(boolean b);

	/**
	 * @return the labels text
	 */
	String getText();

}