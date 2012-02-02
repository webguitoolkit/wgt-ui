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

import java.util.Collection;

/**
 * <b>Interface for the SelectBox control.</b><br>
 * <p>
 * The select box is the same like a select tag in HTML. The user can pick one entry from a list of possible values.
 * </p>
 * <p>
 * To add an empty value or a request for input message the setPrompt() or setPromptKey() method can be used.
 * </p>
 * Creation of a select box
 * 
 * <pre>
 * // create the select box, &quot;selectItem&quot; is the property of the field
 * ISelect select = factory.createSelect(layout, &quot;selectItem&quot;);
 * 
 * // set the model to the select field
 * select.setModel(theModel);
 * // laod the models data to the frontend
 * select.loadList();
 * </pre>
 * 
 * <br>
 * <b>Event handling</b><br>
 * Select boxes can trigger events when the value has changed, this events can be handled by action listeners.<br>
 * 
 * <pre>
 * // the action listener code
 * class MyActionListener implements IActionListener {
 * 	public void onAction(ClientEvent e) {
 * 		ISelect select = (ISelect)e.getSource();
 * 		select.getPage().sendInfo(&quot;Value changed! New value: &quot; + select.getValue());
 * 	}
 * }
 * 
 * // code where the select box is created
 * select.setActionListener(new MyActionListener());
 * </pre>
 * 
 * <br>
 * <b>The Select Model</b><br>
 * To load data into the select box a SelectModel is needed. Following Models are available in the Framework<br>
 * <br>
 * Model with key value pairs:<br>
 * 
 * <pre>
 * // key value pairs
 * List keyValues = new ArrayList();
 * keyValues.add(new String[] { &quot;1&quot;, &quot;one&quot; });
 * keyValues.add(new String[] { &quot;2&quot;, &quot;two&quot; });
 * keyValues.add(new String[] { &quot;3&quot;, &quot;three&quot; });
 * keyValues.add(new String[] { &quot;4&quot;, &quot;four&quot; });
 * keyValues.add(new String[] { &quot;5&quot;, &quot;five&quot; });
 * 
 * // create the select model
 * DefaultSelectModel keyValueModel = new DefaultSelectModel();
 * keyValueModel.setOptions(keyValues);
 * 
 * // set the model to the select field
 * select.setModel(keyValueModel);
 * // Load the models data to the frontend
 * select.loadList();
 * </pre>
 * 
 * <br>
 * Model with single values:<br>
 * 
 * <pre>
 * // simple value
 * List&lt;String&gt; singleValues = new ArrayList&lt;String&gt;();
 * singleValues.add(&quot;one&quot;);
 * singleValues.add(&quot;two&quot;);
 * singleValues.add(&quot;three&quot;);
 * singleValues.add(&quot;four&quot;);
 * singleValues.add(&quot;five&quot;);
 * 
 * // create model
 * DefaultSelectModel singleValueModel = new DefaultSelectModel();
 * singleValueModel.setSingleValueList(singleValues);
 * 
 * // create controls
 * select.setModel(singleValueModel);
 * select.loadList();
 * </pre>
 * 
 * <br>
 * Association select model:<br>
 * 
 * <pre>
 * 	// load list from db
 * 	List&lt;SampleObject&gt; associationValues = new ArrayList&lt;SampleObject&gt;();
 * 
 * 	// create model
 * 	AssociationSelectModel associationModel = new AssociationSelectModel( true );
 * 	// set property that is used for display in the select box. Default is name
 * 	associationModel.setDisplayProperty("displayName");
 * 	// set property that is used as identifier in the select box. Default is id
 * 	associationModel.setIdentProperty("objectId");
 * 	// set the values
 * 	associationModel.setOptions( associationValues );
 * 
 * 	...
 * </pre>
 * 
 * Enumeration select model (JDK 1.5 Extension):
 * 
 * <pre>
 * 		// the enumeration
 * 		enum Type{ STANDARD, LIGHT, ENTERPRISE, PROFESSIONAML }
 * 
 * 		// constructing the model with the enumeration class and a prefix for the resource bundle for translation.
 * 		// For this example the key in the resource bundle has to be like: myapp.type.STANDARD = standard
 * 	EnumerationSelectModel enumSelectModel = new EnumerationSelectModel( Type.class, "myapp.type." );
 * 
 * 	// set model and load list
 * 	select.setModel( enumSelectModel );
 * 	select.loadList();
 * </pre>
 * 
 * <br>
 * CSS classes : wgtComboText, wgtReadonly, wgtComboTable, wgtComboText
 * 
 * @author Peter
 * @author Martin
 * 
 */
public interface ISelect extends IFormControl {

	/**
	 * return the model of this select list. Usually this is an DefaultSelectMode or AssociationSelectModel. There are various
	 * methods witch actually create the model for you. There is little use to call this method from an application.
	 * 
	 * @return model.
	 */
	ISelectModel getModel();

	/**
	 * <b>sets the model of this select box.</b><br>
	 * Most common select models are DefaultSelectMode and AssociationSelectModel.
	 * 
	 * @param model the select model
	 */
	void setModel(ISelectModel model);

	/**
	 * @return set prompt string
	 */
	String getPrompt();

	/**
	 * Convenience method if using the default model
	 * 
	 * @return the default model
	 */
	DefaultSelectModel getDefaultModel();

	/**
	 * sort the options by value, i.e. second String-Value of options-Array. Works only when called before loadList(). Works for
	 * all Selects with a model of type DefaultSelectModel.
	 */
	void sortForDefaultModel();

	/**
	 * loads the values to the browser
	 */
	void loadList();

	/**
	 * sets the prompt that is initially displayed in the select box, if no prompt is set the first value is displayed
	 * 
	 * @param string the prompt
	 */
	void setPrompt(String string);

	/**
	 * sets the prompt that is initially displayed in the select box as key of the resource bundle, if no prompt is set the first
	 * value is displayed.
	 * 
	 * @param promptKey the resource bundle key
	 */
	void setPromptKey(String promptKey);

	/**
	 * CALL this method only if you are using IAssociatedModel you can set the selection of the SelectBox to the entry which is
	 * representation for the Object given (out of the list in the Model.
	 * 
	 * @param o
	 */
	void setValueAsObject(Object o);

	/**
	 * loads an association. The collection with the Objects of the association must be provided. We will load the select with the
	 * names of the objects in the collection. Additional boolean parameter to sort model.
	 * 
	 * @param asso
	 * @param sort
	 */
	void loadAssociation(Collection asso, boolean sort);

	/**
	 * load the list into the client. Also sets the list in a newly created DefaultSelectModel.
	 * 
	 * collection must consist of String[2]. First String is the key (non-visible) , second is the text visible to the user.
	 * 
	 * @param options
	 */
	void loadList(Collection<String[]> options);
}
