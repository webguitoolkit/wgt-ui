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


import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.ICanvasWindowListener;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.util.Window2ActionAdapter;


/**
 * @author i102492
 *
 */
public class PopupDialog extends AbstractPopup {
	
	private static final long serialVersionUID = 1136841938415760059L;
	
	public static final int NO_OPTIONS = 0;
	public static final int YES_NO_OPTION  = 1;
	public static final int YES_NO_CANCEL_OPTION = 2;
	public static final int OK_CANCEL_OPTION = 3;
	
	public static final int PLAIN_MESSAGE       = 0;
	public static final int ERROR_MESSAGE       = 1;
	public static final int WARNING_MESSAGE     = 2;
	public static final int QUESTION_MESSAGE    = 3;
	public static final int INFORMATION_MESSAGE = 4;
	
	public static final int CANCEL_OPTION = 10;
	public static final int OK_OPTION = 11;
	public static final int YES_OPTION = 12;
	public static final int NO_OPTION = 13;
	
	public static final int POPUP_RUNNING = -1;
	
	private String message;
	private int optionType;
	private int messageType;
	private String customIcon;
	private IPopupDialogListener confirmListener;
	// private ICanvas viewConnector;
	private int popupState = POPUP_RUNNING;
	
	
	public static final String ERROR_ICON = "/images/wgt/icons/msg_icon_error.gif";
	public static final String WARN_ICON = "./images/wgt/icons/msg_icon_warn.gif";
	public static final String INFO_ICON = "./images/wgt/icons/msg_icon_info.gif";
	public static final String QUESTION_ICON = "./images/wgt/icons/question_icon.PNG";
	
	
	/**
	 * Constructs a default popup
	 * @param factory
	 * @param page
	 * @param titel
	 * @param width
	 * @param height
	 */
//	public PopupDialog(WebGuiFactory factory, Page page, String titel, int width, int height, String message) {
//		this(factory,page,titel,width,height,message,QUESTION_MESSAGE,DEFAULT_OPTION,null);
//	}
//	public PopupDialog(WebGuiFactory factory, Page page, String titel, int width, int height,
//			String message, int messageType, int optionType, String customIcon) 
//	{
//		super(factory, page, titel, width, height);
//		this.message = message;
//		this.messageType = messageType;
//		this.optionType  = optionType;
//		this.customIcon = customIcon;
//		
//		this.setWindowActionListener(new WINDOW_CLOSE_HANDLER(this));
//		this.registerListener(DialogEvent.EVENT_DIALOG_STATE, new POPUP_EVENT_LISTENER());
//	}
//	public PopupDialog(WebGuiFactory factory, Page page, String titel, int width, int height,
//			String message, int messageType, int optionType) 
//	{
//		this(factory,page,titel,width,height,message,messageType,optionType,null);
//	}


	public PopupDialog(WebGuiFactory factory, Page page, String titel, int width, int height) {
		super(factory, page, titel, width, height);
		this.setWindowActionListener(new WINDOW_CLOSE_HANDLER(this));
	}
	
	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) { 
		viewConnector.addCssClass("popupCanvas");
		// start designing layout
		ITableLayout layout = factory.createTableLayout(viewConnector);
		layout.getEcsTable().setCellPadding(5).setCellSpacing(0).setBorder(0);
		layout.getEcsTable().setWidth("100%");
		
		String iconPath = null;
		if (this.customIcon != null) {
			iconPath = customIcon;
		} else {
			switch(messageType) {
				case PLAIN_MESSAGE:
					break;
				case ERROR_MESSAGE:
					iconPath = "./images/wgt/icons/msg_icon_error.gif";
					break;
				case INFORMATION_MESSAGE:
					iconPath = "./images/wgt/icons/msg_icon_info.gif";
					break;
				case QUESTION_MESSAGE:
					iconPath = "./images/wgt/icons/question_icon.PNG";
					break;
				case WARNING_MESSAGE:
					iconPath = "./images/wgt/icons/msg_icon_warn.gif";
					break;
			}
		}
		
		if (!StringUtils.isEmpty(iconPath)) {
			IHtmlElement iconImg = factory.createHtmlElement(layout,"img");
			iconImg.setAttribute("src", iconPath);
			//layout.getCurrentCell().setWidth("32px");
		} else {
			layout.addEmptyCell();
		}
		
		factory.createLabel(layout, message);
		layout.getCurrentCell().setColSpan(2);
		layout.newRow();
		
		if ( PLAIN_MESSAGE == messageType ) {
			return; // Don't create any buttons
		}
		

		ITableLayout innerButtonLayout = factory.createTableLayout(layout);
		innerButtonLayout.getEcsTable().setAlign("center");
		innerButtonLayout.getStyle().add("margin", "auto");
		innerButtonLayout.getStyle().add("text-align", "center");
		layout.getCurrentCell().setColSpan(3);
		
		String confirmTitle="Ok";
		int resultOption = OK_OPTION;
		if (YES_NO_OPTION==optionType || YES_NO_CANCEL_OPTION==optionType) {
			confirmTitle = "Yes";
			resultOption = YES_OPTION;
		}
		IButton confirmButton = factory.createButton(innerButtonLayout, null, confirmTitle, "", new BUTTON_LISTENER(resultOption));
		// confirmButton.addCssClass("cml_popup_dialog_button");
		innerButtonLayout.getCurrentCell().setAlign("left");
	
		if (YES_NO_CANCEL_OPTION==optionType || YES_NO_OPTION==optionType) {
			IButton noButton = factory.createButton(innerButtonLayout, null, "No", "", new BUTTON_LISTENER(NO_OPTION));
			// noButton.addCssClass("cml_popup_dialog_button");
			innerButtonLayout.getCurrentCell().setAlign("center");
		}
		if (YES_NO_CANCEL_OPTION==optionType || OK_CANCEL_OPTION==optionType) {
			IButton cancelButton = factory.createButton(innerButtonLayout, null, "Cancel", "", new BUTTON_LISTENER(CANCEL_OPTION));
			// cancelButton.addCssClass("cml_popup_dialog_button");
			innerButtonLayout.getCurrentCell().setAlign("center");
		}
		
		layout.newRow();
	}

	public void showConfirmDialog(String message, int messageType, int optionType, String customIcon, IPopupDialogListener listener) {
		this.confirmListener = listener;
		this.message = message;
		this.messageType = messageType;
		this.optionType  = optionType;
		this.customIcon  = customIcon;
		super.show();
	}
	public void showConfirmDialog(String message, int messageType, int optionType, IPopupDialogListener listener) {
		showConfirmDialog(message, messageType, optionType, null,listener);
	}	
	
	
	public void showMessageDialog(String message, String customIcon) {
		this.message = message;
		this.messageType = PLAIN_MESSAGE;
		this.customIcon = customIcon;
		
		super.show();
	}
	public void showMessageDialog(String message) {
		showMessageDialog(message, null);
	}
	public void showErrorMessageDialog(String message) {
		showMessageDialog(message, ERROR_ICON);
	}
	
	
	public String getMessage() {
		return message;
	}
	public int getOptionType() {
		return optionType;
	}
	public int getMessageType() {
		return messageType;
	}
	public String getCustomIcon() {
		return customIcon;
	}
	
	
	class WINDOW_CLOSE_HANDLER extends Window2ActionAdapter implements ICanvasWindowListener {
		private static final long serialVersionUID = -1853583528609060283L;

		public WINDOW_CLOSE_HANDLER(IActionListener adaptee) {
			super(adaptee);
		}
		
		@Override
		public void onClose(ClientEvent event) {
			popupState = CANCEL_OPTION;
//			DialogEvent dialogEvent = new DialogEvent(viewConnector, DialogEvent.EVENT_DIALOG_STATE, DialogEvent.STATE_SAVED);
//			PopupDialog.this.fireServerEvent(dialogEvent);
			if (confirmListener != null) {
				confirmListener.onPopupCancel();
			}
			close();
		}
	}

	class BUTTON_LISTENER implements IActionListener {
		
		private static final long serialVersionUID = 960117005049313320L;
		private int resultType;
		
		public BUTTON_LISTENER(int resultType) {
			this.resultType = resultType;
		}
		
		public void onAction(ClientEvent event) {
			popupState = resultType;
			
			if (confirmListener != null) {
				switch (popupState) {
					case YES_OPTION:
						confirmListener.onPopupYes(null);
						break;
						
					case NO_OPTION:
						confirmListener.onPopupNo(null);
						break;
						
					case OK_OPTION:
						confirmListener.onPopupOk(null);
						break;
						
					case CANCEL_OPTION:
						confirmListener.onPopupCancel();
						break;
				}
			}
			
			close();
		}	
	}
	
	
//	class POPUP_EVENT_LISTENER implements IServerEventListener {
//		
//		private static final long serialVersionUID = 6669863568257120757L;
//
//		public void handle(ServerEvent event) {
//
//			switch (popupState) {
//				case YES_OPTION:
//					confirmListener.onYes(null);
//					break;
//					
//				case NO_OPTION:
//					confirmListener.onNo(null);
//					break;
//					
//				case OK_OPTION:
//					confirmListener.onOk(null);
//					break;
//					
//				case CANCEL_OPTION:
//					confirmListener.onCancel();
//					break;
//			}
//			
//			System.out.println(popupState);
//		}
//	}
}
