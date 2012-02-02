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
 * <h1>Interface for a Button</h1>
 * <p>
 * A Button that can have different representations; classical Button or as Link. May be combined with a
 * image as well.
 * </p>
 * <b>Creation of a button</b><br>
 * <pre>
 * IButton buttonWithText = factory.createButton(layout, null, "button.click@Click", "button.click.tooltip@Click Me", new ButtonListener());
 * IButton buttonWithImage = factory.createButton(layout, "./images/wgt/icons/msg_icon_error.gif", null, null, new ButtonListener());
 * IButton buttonWithTestAndImage =  = factory.createButton(layout, "./images/wgt/icons/msg_icon_error.gif", "Click Me!", null, new ButtonListener());
 * 
 * IButton linkWithText = factory.createLinkButton(layout, null, "Click Me!", null, new ButtonListener());
 * IButton linkWithImage = factory.createLinkButton(layout, "./images/wgt/icons/msg_icon_error.gif", null, null, new ButtonListener());
 * IButton linkWithTestAndImage	= factory.createLinkButton(layout, "./images/wgt/icons/msg_icon_error.gif", "Click Me!", null, new ButtonListener());
 * IButton lintToURL = factory.createLink(layout, null, "Load Google!", null, "http://www.google.de", "_blank");
 * 
 * IButton buttonLeft = factory.createButton(layout, null, "button.left@Left", "button.click.tooltip@Click Me", new ButtonListener());
 * buttonLeft.setAlignment(IButton.POSITION_LEFT);
 * IButton buttonMiddel = factory.createButton(layout, null, "button.middle@Middle", "button.click.tooltip@Click Me", new ButtonListener());
 * buttonMiddel.setAlignment(IButton.POSITION_MIDDLE);
 * IButton buttonRight = factory.createButton(layout, null, "button.right@Right", "button.click.tooltip@Click Me", new ButtonListener());
 * buttonRight.setAlignment(IButton.POSITION_RIGHT);
 * </pre>
 * <br>
 * <b>Event handling</b><br>
 * Buttons trigger events when clicked, this events can be handled by action listeners.<br>
 * <pre>
 * //the action listener code
 * class MyActionListener implements IActionListener{
 *   public void onAction(ClientEvent e){
 *     e.getSource().getPage().sendInfo("Button Clicked!" );
 *   }
 * }
 * 
 * //code where the button is created
 * factory.createButton( layout, "", "button.click@Click", "button.click.tooltip@Click Me",  new MyActionListener() );
 * </pre>
 * <p>
 * <b>CSS classes:</b> wgtButton2, wgtButton2-l, wgtButton2-c, wgtButton2-r
 * </p>
 * 
 * @author Peter
 * @author Martin
 */
public interface IButton extends IFormControl {

	int POSITION_LEFT = 1;
	int POSITION_MIDDLE = 2;
	int POSITION_RIGHT = 3;

	/**
	 * Set the path to the image. If set an image will be displayed with the Button. The image shall have a size of
	 * 16x16 pixel to avoid overlaps with the surrounding stuff.
	 * 
	 * @param src
	 *            the URL of the image
	 */
	void setSrc(String src);

	/**
	 * @return true if the button is disabled.
	 */
	boolean isDisabled();

	/**
	 * Sets the visibility of the button.
	 * 
	 * @param vis
	 *            true to make the Button visible
	 */
	void setVisible(boolean vis);

	/**
	 * The button can be rendered with a 3D border that changes on mouse over (Like buttons in windows applications)
	 * 
	 * @param displayMode3D
	 *            true to have it in 3D
	 */
	void setDisplayMode3D(boolean displayMode3D);

	/**
	 * Sets the key for the confirm message of the button, if no value is set there is no confirm message
	 * @param confirmMsg the key for the confirm message
	 */
	void setConfirmMsg(String confirmMsg);

	/**
	 * Set the Label shown on the Button
	 * 
	 * @param label
	 *            simply the Label text
	 */
	void setLabel(String label);

	/**
	 * Set a reference to a resource bundle.
	 * 
	 * @param labelKey
	 *            e.g. "buttons.one@ClickMe"
	 */
	void setLabelKey(String labelKey);

	/**
	 * Used to prevent from unwanted event processing if the Button is part of another action control, e.g. rendered in
	 * a Table cell. If the Button is clicked and cancelBubble is set the row listener of the table will not be notified
	 * about the click.
	 * 
	 * @param eventBubble
	 *            if set to false the event delegation will be avoided.
	 */
	void setEventBubbling(boolean eventBubble);

	/**
	 * A target URL for the button - if this is set, the button creates a simple link, no server round-trip will be
	 * initiated on click.
	 * 
	 * @param url
	 *            the URL string
	 */
	void setLinkURL(String url);

	/**
	 * Set target name (only relevant for buttons with URL used for links)
	 * 
	 * @param target the frame or window where the URL should be opened
	 */
	void setTarget(String target);

	/**
	 * this function can be used to display more then one Button in a row.
	 * 
	 * <pre>
	 *  -------------------------------------------------------------------- 
	 * | POSITION_LEFT | POSITION_MIDDLE | POSITION_MIDDLE | POSITION_RIGHT | 
	 *  --------------------------------------------------------------------
	 * </pre>
	 * 
	 * @param pos the position
	 */
	void setAlignment(int pos);

	/**
	 * Set the button to disabled mode
	 * 
	 * @param disabled true for disabled
	 */
	void setDisabled(boolean disabled);

}
