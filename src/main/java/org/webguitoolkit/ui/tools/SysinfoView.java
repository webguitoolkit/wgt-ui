package org.webguitoolkit.ui.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.jar.Manifest;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.SequentialTableLayout;

public class SysinfoView extends AbstractView {

	private static final long serialVersionUID = 4622515955022534216L;

	private final boolean showServerInfo;
	private final boolean showClassPath;

	public SysinfoView(WebGuiFactory factory, ICanvas viewConnector) {
		super(factory, viewConnector);
		this.showClassPath = false;
		this.showServerInfo = false;
	}
	public SysinfoView(WebGuiFactory factory, ICanvas viewConnector, boolean showClassPath, boolean showServerInfo ) {
		super(factory, viewConnector);
		this.showClassPath = showClassPath;
		this.showServerInfo = showServerInfo;
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		viewConnector.setLayout(new SequentialTableLayout());
		ILabel label = factory.createLabel(viewConnector, "sysinfo.header@System Information");
		label.addCssClass("wgtLabelFor");
		label.addCssClass("wgtHeader2");
		label.setLayoutData(SequentialTableLayout.getLastInRow().setCellColSpan(2));

		try {
			String path = Page.getServletRequest().getSession().getServletContext().getRealPath("/META-INF/MANIFEST.MF");
			Manifest mf = new Manifest(new FileInputStream(path));

			label = factory.createLabel(viewConnector, "sysinfo.titel@Implementation-Title:");
			label.addCssClass("wgtLabelFor");
			label = factory.createLabel(viewConnector, "");
			label.setText(mf.getMainAttributes().getValue("Implementation-Title"));
			label.setLayoutData(SequentialTableLayout.getLastInRow());

			label = factory.createLabel(viewConnector, "sysinfo.version@Implementation-Version:");
			label.addCssClass("wgtLabelFor");
			label = factory.createLabel(viewConnector, "");
			label.setText(mf.getMainAttributes().getValue("Implementation-Version"));
			label.setLayoutData(SequentialTableLayout.getLastInRow());

			label = factory.createLabel(viewConnector, "sysinfo.vendor@Implementation-Vendor:");
			label.addCssClass("wgtLabelFor");
			label = factory.createLabel(viewConnector, "");
			label.setText(mf.getMainAttributes().getValue("Implementation-Vendor"));
			label.setLayoutData(SequentialTableLayout.getLastInRow());

			label = factory.createLabel(viewConnector, "sysinfo.build@Implementation-Build:");
			label.addCssClass("wgtLabelFor");
			label = factory.createLabel(viewConnector, "");
			label.setText(mf.getMainAttributes().getValue("Implementation-Build"));
			label.setLayoutData(SequentialTableLayout.getLastInRow());

			if (showServerInfo) {
				label = factory.createLabel(viewConnector, "sysinfo.server@Server:");
				label.addCssClass("wgtLabelFor");
				label = factory.createLabel(viewConnector, "");
				label.setText(InetAddress.getLocalHost().getHostName());
				label.setLayoutData(SequentialTableLayout.getLastInRow());
			}

			if (showClassPath) {
				label = factory.createLabel(viewConnector, "sysinfo.classpath@Class-Path:");
				label.addCssClass("wgtLabelFor");
				label.setLayoutData(SequentialTableLayout.getLastInRow());
				label = factory.createLabel(viewConnector, "");
				label.setText(mf.getMainAttributes().getValue("Class-Path"));
				label.setLayoutData(SequentialTableLayout.getLastInRow().setCellColSpan(2));
			}
		}
		catch (FileNotFoundException e) {
			getPage().sendWarn("Error loading system infos from Manifest file!");
		}
		catch (IOException e) {
			getPage().sendWarn("Error loading system infos!");
		}
	}

}
