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
package org.webguitoolkit.ui.controls.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.TD;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.layout.TableLayout;

/**
 * Creates a dialog containing a option image a text and several buttons.
 * All buttons must be created by the user using buttons().add(...)
 * or addButton(...)
 * Either way you may access the last added button by convenience method lastButton()
 * 
 * Typical use:
 * 
 * <code>
  		ConfirmationDialog dialog = new ConfirmationDialog(comp.getBody());
		dialog.setTitle("Concurrency Conflict.");
		dialog.setMsg("Another user has changed the data you have been working on.\nDo you want to override the changes or abort your editing?");
		dialog.addButton("Label of Button", "tooltip goes here.", new IActionListener() {

			public void onAction(Event event) {
				....
				dialog.close();
			}
		});
		dialog.addButton("Abort", "Your changes are lost, data reloaded.", new IActionListener() {

			public void onAction(Event event) {
				....
				dialog.close();
			}			
		});
		dialog.show();

 * </code>
 * @author arno
 *
 */
public class ConfirmationDialog extends DynamicDialog {
	protected String image;
	protected String msg;
	protected String markerString;
	protected List buttons = new ArrayList();
	protected TableLayout layout;
	/**
	 * The only constructor pass  in the page class so we know on which page the confirmation dialog should
	 * appear.
	 * @param page
	 */
	public ConfirmationDialog(Page body) {
		super(body);
		layout = getFactory().newTableLayout(getWindow());
		// do not layout the components until all are added from the user.
		layout.setLayoutMode(false);
	}
	/**
	 * creates the dialog and transfers it into the context, so it will be shown.
	 * All configuring of the dialog must happen between construction and this method.
	 * Any change after show, will only be display if close is called and
	 * afterwards show() again.
	 *
	 */
	public void show() {
		// ok,	now lets add the stuff as layout
		layout.setLayoutMode(true);
		
		final TD headerCell = new TD();
		layout.addCell(headerCell);
		headerCell.setColSpan(99);
		final Div headerContent = (Div) new Div().setClass("wgtConfMessage");
		headerCell.addElement(headerContent);
		if (image!=null) {
			headerContent.addElement(new IMG().setSrc(image));
		}
		if (msg != null) {
			headerContent.addElement(msg);
		}
		// add the buttons as a bar
		layout.newLine();
		for (Iterator it = buttons.iterator(); it.hasNext();) {
			layout.add((BaseControl) it.next());
			layout.getCurrentCell().setAlign("center");
		}
		// redraw?
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = StringEscapeUtils.escapeHtml(msg);
	}
	/**
	 * the setMsg method will escape all HTML entities. If you really want to send HTML code use this method.
	 * Keep in mind that this method is soly for fixed string i the program code, as user entered string may
	 * have malicious HTML code.
	 * @param msg
	 */
	public void setMsgUnescaped(String msg) {
		this.msg = msg;
	}

	public List getButtons() {
		return buttons;
	}
	
	public IButton lastButton() {
		return (IButton) buttons.get(buttons.size()-1);
	}
	/**
	 * convenience method is creating a button and setting the label and tooltip
	 * @param label Label on the button, may be a textservice string.
	 * @param tooltip tooltip of the button , may be a textservice string. (...@...)
	 * @param listener
	 */
	public void addButton(String titleKey, String tooltipKey, IActionListener listener) {
		IButton button = getFactory().createButton(layout, null, titleKey, tooltipKey, listener);
		buttons.add(button);
	}
	
	public void addButton(String titleKey, String tooltipKey, IActionListener listener, String id) {
		IButton button = getFactory().createButton(layout, null, titleKey, tooltipKey, listener, id);
		buttons.add(button);
	}
	
}
