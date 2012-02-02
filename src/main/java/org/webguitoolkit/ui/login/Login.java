package org.webguitoolkit.ui.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;
import org.webguitoolkit.ui.controls.util.validation.ValidatorUtil;

/**
 * Login may be used for form authentication.
 *
 * Login has to be configured in web.xml as follows:
 * <pre><code>
 *	<login-config>
 *		<auth-method>FORM</auth-method>
 *		<realm-name>realm-name</realm-name>
 *		<form-login-config>
 *			<form-login-page>/page/login/Login</form-login-page>
 *			<form-error-page>/page/login/Login?success=false</form-error-page>
 *		</form-login-config>
 *	</login-config>
 * </code></pre>
 * 
 * Texts can be customized and translated in ApplicationResources.properties.
 * Defaults are provided.
 * Keys:
 * "formLogin.title" (Default: "Login")
 * "formLogin.headerText" (Default: "Please enter your login information!")
 * "formLogin.username" (Default: "Username")
 * "formLogin.password" (Default: "Password")
 * "formLogin.submit" (Default: "Login")
 * "formLogin.loginFailed" (Default: "Your login information are not correct.")
 * 
 * The use css, favicon and teaser image may be customized
 * through extending Login page in your project.
 * Then you only have to override the bean methods 
 * getCssLink(), getFavIcon() and getTeaserImg() and return the new values
 * like constants. 
 * 
 * <pre><code>
 * 	public String getCssLink() {
 *		return "my_theme.css";
 *	}
 *	</code></pre>
 */
public class Login extends Page {

	public static final String BUTTON_ID = "LOGIN_BUTTON_ID";
	public static final String PASSWORD_ID = "LOGIN_PASSWORD_ID";
	public static final String USERNAME_ID = "LOGIN_USERNAME_ID";
	
	private WebGuiFactory factory = WebGuiFactory.getInstance();;
	private IText usernameText;
	private IText passwordText;
	
	private String cssLink = "standard_theme.css";
	private String favIcon = "./images/wgt/iconw.gif";
	private String teaserImg = "./images/wgt/logo300w.gif";

	protected void pageInit() {		
        // WGTFilter parameter <param-name>textservice.resource.bundle</param-name> is not considered
        // --> this will not work with not default resource bundle names
    	TextService.setLocale(getServletRequest().getLocale());
		
		addWgtCSS(getCssLink());
		addFavicon(getFavIcon());
		IActionListener loginListener = new LoginListener();		
		
		ITableLayout headLayout = factory.createTableLayout(this);
		headLayout.getStyle().addWidth("100%");
		
		factory.createLabel(headLayout, "");
		headLayout.getCurrentCell().setColSpan(2);
		headLayout.getCurrentCell().setHeight("50px");
		
		headLayout.newRow();
		
		ITableLayout frameLayout = factory.createTableLayout(headLayout);
		headLayout.getCurrentCell().setColSpan(2);
		headLayout.getCurrentCell().setAlign("center");
		headLayout.getCurrentCell().setVAlign("Top");
		
		frameLayout.getEcsTable().setBgColor("#FFFFFF");
		frameLayout.getEcsTable().setCellSpacing(0);
		frameLayout.getEcsTable().setCellPadding(0);
		frameLayout.getStyle().add("border-color", "#BEBEBE");
		
		ITableLayout formLayout = factory.createTableLayout(frameLayout);
		frameLayout.getCurrentCell().setStyle("padding-top:10px; padding-left:10px; border-color:#FFFFFF");
		frameLayout.getCurrentCell().setAlign("left");
		frameLayout.getCurrentCell().setVAlign("top");
		frameLayout.getCurrentCell().setBgColor("#E9E9E9");
		
		formLayout.getEcsTable().setBorder(0);		
		formLayout.getEcsTable().setWidth("301");
		formLayout.getEcsTable().setHeight("100");
		formLayout.getEcsTable().setAlign("left");
		formLayout.getEcsTable().setCellSpacing(0);
		
		ILabel headerLabel = factory.createLabel(formLayout, "formLogin.headerText@Please enter your login information!");
		headerLabel.getStyle().add("font-size", "12pt");
		headerLabel.getStyle().add("color", "#0099FF");
		headerLabel.getStyle().add("font-weight", "bold");
		formLayout.getCurrentCell().setColSpan(2);
		formLayout.getCurrentCell().setVAlign("Top");
		formLayout.getCurrentCell().setWidth(300);
		formLayout.getCurrentCell().setHeight(24);
		formLayout.getCurrentCell().setStyle("padding-bottom: 30px; padding-top: 25px; vertical-align: top;");
				
		formLayout.newRow();
		
		ILabel usernameLabel = factory.createLabel(formLayout, "formLogin.username@Username");
		formLayout.getCurrentCell().setWidth(161);
		formLayout.getCurrentCell().setHeight(20);
		
		usernameText = factory.createText(formLayout, "j_username", usernameLabel, USERNAME_ID);
		usernameText.getStyle().addWidth("21ex");
		formLayout.getCurrentCell().setWidth(183);
		formLayout.getCurrentCell().setHeight(20);
		usernameText.setActionListener( new TextReturnListener() );
		usernameText.setHTMLName("j_username");
		usernameText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		
		formLayout.newRow();
		
		ILabel passwordLabel = factory.createLabel(formLayout, "formLogin.password@Password");
		formLayout.getCurrentCell().setWidth(161);
		formLayout.getCurrentCell().setHeight(20);
		
		passwordText = factory.createText(formLayout, "j_password", passwordLabel, PASSWORD_ID);
		passwordText.getStyle().addWidth("21ex");
		formLayout.getCurrentCell().setWidth(183);
		formLayout.getCurrentCell().setHeight(20);
		passwordText.setPassword(true);
		passwordText.setActionListener(loginListener);
		passwordText.setHTMLName("j_password");
		passwordText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		
		formLayout.newRow();
				
		factory.createLabel(formLayout, "");
		formLayout.getCurrentCell().setColSpan(2);
		formLayout.getCurrentCell().setHeight("2");
		
		formLayout.newRow();
		formLayout.addEmptyCell();
		
		IButton submitButton = factory.createButton(formLayout, null, "formLogin.submit@Login", "", loginListener, BUTTON_ID);
		formLayout.getCurrentCell().setColSpan(2);	
		
		ITableLayout imageTable = factory.createTableLayout(frameLayout);
		imageTable.getEcsTable().setBorder(0);
		imageTable.getEcsTable().setCellSpacing(0);
		imageTable.getEcsTable().setCellPadding(0);
		frameLayout.getCurrentCell().setStyle("border-color:#FFFFFF");
		
		IHtmlElement image = factory.createHtmlElement(imageTable,"img");
		image.setAttribute("src", getTeaserImg());
		image.setAttribute("border", "0");
		
		 if ("false".equalsIgnoreCase(getServletRequest()
				.getParameter("success"))) {
			sendError(TextService
					.getString("formLogin.loginFailed@Your login information are not correct."));
		} else {
			usernameText.focus();
		}				
	}

	protected String title() {
		return TextService.getString("formLogin.title@Login");
	}
	
	private class TextReturnListener implements IActionListener{
		public void onAction(ClientEvent event) {
			passwordText.focus();
		}
	}

	private class LoginListener implements IActionListener
	{
		public void onAction(ClientEvent event) {
			String errorMsg = validateFormControl(usernameText);
			errorMsg += validateFormControl(passwordText);
			if (!StringUtils.isBlank(errorMsg))
			{
				sendError(errorMsg);
				return;
			}
			Map<String,String> par = new HashMap<String,String>();
			par.put("j_password", passwordText.getValue());
			par.put("j_username", usernameText.getValue());
			getContext().doPost("j_security_check", par, "_parent");
		}
	
		private String validateFormControl(IFormControl fc) {
			String errorMsg = "";
			try {
				fc.validate();
			} catch (ValidationException e) {
				errorMsg = "<i>"
						+ ((fc.getDescribingLabel() == null) ? "" : fc
								.getDescribingLabel().getText()) + "</i> "
						+ TextService.getString(e.getMessage()) + "<br>";
			}
			return errorMsg;
		}
	}
	public String getCssLink() {
		return cssLink;
	}

	public void setCssLink(String cssLink) {
		this.cssLink = cssLink;
	}

	public String getFavIcon() {
		return favIcon;
	}

	public void setFavIcon(String favIcon) {
		this.favIcon = favIcon;
	}
	public String getTeaserImg() {
		return teaserImg;
	}

	public void setTeaserImg(String teaserImg) {
		this.teaserImg = teaserImg;
	}
}
