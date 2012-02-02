package org.webguitoolkit.ui.errorpages;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.HtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.Label;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.layout.TableLayout;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * Error403 may be used as user defined http 403 error page.
 * 
 * Error403 has to be configured in web.xml as follows:
 * 
 * <pre>
 * <code>
 * <error-page>
 * 	<error-code>403</error-code>
 * 	<location>/page/errorpages/Error403</location>
 * </error-page>
 * </code>
 * </pre>
 * 
 * Texts can be customized and translated in ApplicationResources.properties. Defaults are provided. Keys: "error403.title"
 * (Default: "Error 403 - No Authorization") "error403.unauthorizied" (Default:
 * "You are not authorized to see the requested page!") "error403.login" (Default: "Login")
 * 
 * The use css, favicon and teaser image may be customized through extending Error403 page in your project. Then you only have to
 * override the bean methods getCssLink(), getFavIcon() and getTeaserImg() and return the new values like constants.
 * 
 * <pre>
 * <code>
 * 	public String getCssLink() {
 * 	return "my_theme.css";
 * }
 * </code>
 * </pre>
 */
public class Error403 extends Page {

	private WebGuiFactory factory = WebGuiFactory.getInstance();;
	private Text usernameText;
	private Text passwordText;

	private String cssLink = "standard_theme.css";
	private String favIcon = "./images/wgt/iconw.gif";
	private String teaserImg = "./images/wgt/logo300w.gif";

	private boolean showLoginLink = true;

	protected void pageInit() {
		// WGTFilter parameter <param-name>textservice.resource.bundle</param-name> is not considered
		// --> this will not work with not default resource bundle names
		TextService.setLocale(getServletRequest().getLocale());

		addWgtCSS(getCssLink());
		addFavicon(getFavIcon());

		TableLayout headLayout = factory.newTableLayout(this);
		headLayout.getStyle().addWidth("100%");

		factory.newLabel(headLayout, "");
		headLayout.getCurrentCell().setColSpan(2);
		headLayout.getCurrentCell().setHeight("50px");

		headLayout.newLine();

		TableLayout frameLayout = factory.newTableLayout(headLayout);
		headLayout.getCurrentCell().setColSpan(2);
		headLayout.getCurrentCell().setAlign("center");
		headLayout.getCurrentCell().setVAlign("Top");

		frameLayout.getEcsTable().setBgColor("#FFFFFF");
		frameLayout.getEcsTable().setCellSpacing(0);
		frameLayout.getEcsTable().setCellPadding(0);
		frameLayout.getStyle().add("border-color", "#BEBEBE");

		TableLayout formLayout = factory.newTableLayout(frameLayout);
		frameLayout.getCurrentCell().setStyle("padding-top:10px; padding-left:10px; border-color:#FFFFFF");
		frameLayout.getCurrentCell().setAlign("left");
		frameLayout.getCurrentCell().setVAlign("top");
		frameLayout.getCurrentCell().setBgColor("#E9E9E9");

		formLayout.getEcsTable().setBorder(0);
		formLayout.getEcsTable().setWidth("301");
		formLayout.getEcsTable().setHeight("100");
		formLayout.getEcsTable().setAlign("left");
		formLayout.getEcsTable().setCellSpacing(0);

		Label headerLabel = factory.newLabel(formLayout, "error403.unauthorizied@You are not authorized to see the requested page!");
		headerLabel.getStyle().add("font-size", "12pt");
		headerLabel.getStyle().add("color", "#0099FF");
		headerLabel.getStyle().add("font-weight", "bold");
		formLayout.getCurrentCell().setColSpan(2);
		formLayout.getCurrentCell().setVAlign("Top");
		formLayout.getCurrentCell().setWidth(300);
		formLayout.getCurrentCell().setHeight(24);
		formLayout.getCurrentCell().setStyle("padding-bottom: 30px; padding-top: 25px; vertical-align: top;");

		formLayout.newLine();

		if (isShowLoginLink()) {
			Button submitButton = factory.newLinkButton(formLayout, null, "error403.login@Login", "", new LoginListener());
			formLayout.getCurrentCell().setColSpan(2);
		}

		TableLayout imageTable = factory.newTableLayout(frameLayout);
		imageTable.getEcsTable().setBorder(0);
		imageTable.getEcsTable().setCellSpacing(0);
		imageTable.getEcsTable().setCellPadding(0);
		frameLayout.getCurrentCell().setStyle("border-color:#FFFFFF");

		HtmlElement image = factory.newHtmlElement(imageTable);
		image.setTagName("img");
		image.setAttribute("src", getTeaserImg());
		image.setAttribute("border", "0");
	}

	protected String title() {
		return TextService.getString("error403.title@Error 403 - No Authorization");
	}

	private class LoginListener implements IActionListener {
		public void onAction(ClientEvent event) {
			doLogout();
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

	public void setShowLoginLink(boolean showLoginLink) {
		this.showLoginLink = showLoginLink;
	}

	public boolean isShowLoginLink() {
		return showLoginLink;
	}

	public void doLogout() {
		getServletRequest().getSession().invalidate();
		gotoApplicationPage("");
	}
}
