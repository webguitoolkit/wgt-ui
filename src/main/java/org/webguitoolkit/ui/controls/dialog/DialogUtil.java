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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.util.Window2ActionAdapter;


public class DialogUtil {

	/**
	 * 
	 */
	public static final String DIALOG_IMG_ID = "DIALOG_IMG_ID_";
	public static final String CONFIRMATION_DIALOG_OK_BUTTON_ID = "CONFIRMATION_DIALOG_OK_BUTTON_ID";
	// error message with error icon
	public static final String ERROR = "error";
	// info message with info icon
	public static final String INFO = "info";
	// warn message with warn icon
	public static final String WARN = "warn";
	// protected static Canvas infoCanvas;

	/**
	 * function for sending messages to the client.
	 * 
	 * @param page
	 *            the page element for the message
	 * @param msg
	 *            the message string
	 * @param msgType
	 *            the message type, error, info or warning
	 * @param customOk
	 *            the action listener for handle the ok button
	 */
	public static void sendMessage(Page page, String msg, String msgType, IActionListener customOk) {
		sendMessage(page, msg, msgType, customOk, null);
	}

	public static void sendMessage(Page page, String msg, String msgType, IActionListener customOk, String msgWindowId) {
		DynamicDialog dialog = new DynamicDialog(page, msgWindowId);
		ICanvas infoCanvas = dialog.getWindow();
		
		addControls(infoCanvas, msg, msgType, customOk);
		
		page.getContext().moveDown(infoCanvas.getId() + ".centerDiv");
	}
	
	/**
	 * simple info box without icon
	 */
	public static void infoBox(Page body, String msg, IActionListener customOk) {
		sendMessage(body, msg, null, customOk);
	}

	public static void infoBox(Page body, String msg, IActionListener customOk, String msgWindowId) {
		sendMessage(body, msg, null, customOk, msgWindowId);
	}
	
	/**
	 * this function places the message box directly into the context,
	 * useful for functions in the post dispatch phase.
	 * Maybe we can remove this method somehow.
	 * 
	 * @param page
	 *            the page element for the message
	 * @param msg
	 *            the message string
	 * @param msgType
	 *            the message type, error, info or warning
	 * @param customOk
	 *            the action listener for handle the ok button
	 */
	public static void sendMessageDirectly( Page page, String msg, String msgType, IActionListener customOk) {
		sendMessageDirectly(page, msg, msgType, "append", customOk);
	};
	
	/**
	 * this function places the message box directly into the context,
	 * useful for functions in the post dispatch phase.
	 * Maybe we can remove this method somehow.
	 * 
	 * @param page
	 *            the page element for the message
	 * @param msg
	 *            the message string
	 * @param msgType
	 *            the message type, error, info or warning
	 * @param customOk
	 *            the action listener for handle the ok button
	 */
	public static void sendMessageDirectly( Page page, String msg, String msgType, String idPostFix, IActionListener customOk) {
		sendMessageDirectly( page, msg, msgType, idPostFix, customOk, null);
	}
	
	public static void sendMessageDirectly( Page page, String msg, String msgType, String idPostFix, IActionListener customOk, String dialogName) {
		
		page.getContext().add(page.getId() + "."+idPostFix, StringUtils.EMPTY, IContext.TYPE_ADD_2_NODE_LAST, IContext.STATUS_IDEM );
		page.getContext().appendHtml(page.getId(), StringUtils.EMPTY );

		WebGuiFactory factory = WebGuiFactory.getInstance();
		ICanvas infoCanvas = factory.createCanvas( page );
		infoCanvas.setDisplayMode( Canvas.DISPLAY_MODE_WINDOW_MODAL );
		infoCanvas.setDragable(true);
		infoCanvas.setName(dialogName);
		
		addControls(infoCanvas, msg, msgType, customOk);

		page.getContext().moveDown(infoCanvas.getId() + ".centerDiv");
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		((Canvas)infoCanvas).drawCanvas( out );
	
		String html = sw.toString();
		page.getContext().add(page.getId() + "." + idPostFix, html, IContext.TYPE_ADD_2_NODE_LAST, IContext.STATUS_IDEM );
	}
	
	protected static void addControls( final ICanvas infoCanvas, String msgKey, String msgType, IActionListener customOk ){
		WebGuiFactory factory = WebGuiFactory.getInstance();
		
		// the canvas will be inserted manually into the page
		infoCanvas.setDragable(true);
		infoCanvas.setDisplayMode(Canvas.DISPLAY_MODE_WINDOW_MODAL);
		infoCanvas.setCssClass("wgtPopup wgtAlertBox");

		if (StringUtils.isNotEmpty(msgType))
			infoCanvas.setTitle("msgbox." + msgType + "@" + msgType.substring(0, 1).toUpperCase()
					+ msgType.substring(1));


		if (customOk == null) {
			customOk = new IActionListener() {
				public void onAction(ClientEvent event) {
					infoCanvas.setVisible( false );
					// remove from clients DOM
					infoCanvas.getPage().getContext().removeNode(infoCanvas.getId());
					// remove from component tree
					infoCanvas.remove();
				}
			};
		}
		
		infoCanvas.setWindowActionListener(new Window2ActionAdapter(customOk));

		ITableLayout tableLayout = factory.createTableLayout(infoCanvas);
		tableLayout.getEcsTable().setStyle("width: 100%;");
		
		// add image
		if( StringUtils.isNotEmpty( msgType ) ){
			IHtmlElement img = factory.createHtmlElement(tableLayout, "img", infoCanvas.getId() + "." + DIALOG_IMG_ID + msgType);
			img.setAttribute("src", "./images/wgt/icons/msg_icon_" + msgType + ".gif");
			tableLayout.getCurrentCell().setStyle("text-align: center; width: 40px;");
			
			if ( StringUtils.isNotEmpty(infoCanvas.getName())) {
				img.setName(infoCanvas.getName() + "." + DIALOG_IMG_ID + msgType);
			}
		}
		
		// add label
		ILabel infoLabel = factory.createLabel(tableLayout, msgKey);
		tableLayout.getCurrentCell().setStyle("text-align: left;");
		tableLayout.newRow();
		
		// add button
		IButton infoButton = factory.createButton(tableLayout, null, "button.ok@Ok", "button.ok@Ok", customOk, infoCanvas.getId()
				+ "." + CONFIRMATION_DIALOG_OK_BUTTON_ID);
		if ( StringUtils.isNotEmpty(infoCanvas.getName())) {
			infoButton.setName(infoCanvas.getName() + "." + CONFIRMATION_DIALOG_OK_BUTTON_ID);
		}
		//infoButton.setRegisteredActions("onReturn");
		if( StringUtils.isNotEmpty( msgType ) )
			tableLayout.getCurrentCell().setColSpan(2);
		tableLayout.getCurrentCell().setStyle("text-align: center;");
		infoButton.focus();
	}

}
