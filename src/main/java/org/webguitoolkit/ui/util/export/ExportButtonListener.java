package org.webguitoolkit.ui.util.export;

import java.util.HashMap;
import java.util.Map;

import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.table.ITable;

/**
 * @author i102454 default ButtonListener for PDF or Excel Exports of tables
 */
public class ExportButtonListener implements IActionListener {
	private String exportType;
	private ITable exportTable;
	public final static String SERVLETURL = "/ExportServlet";

	public ExportButtonListener(ITable table, String type) {
		this.exportType = type;
		this.exportTable = table;
	}

	public void onAction(ClientEvent event) {
		Map params = new HashMap();
		String format = exportType;
		String componentName = exportTable.getId();
		// StringBuffer urlParameter = new StringBuffer();
		// urlParameter.append("?componentname="+componentName);
		params.put("componentname", componentName);
		// urlParameter.append("&type="+format);
		params.put("type", format);
		String basePath = Page.getServletRequest().getScheme() + "://" + Page.getServletRequest().getServerName() + ":"
				+ Page.getServletRequest().getServerPort() + Page.getServletRequest().getContextPath();
		// String title = exportTable.getTitle();
		// if(StringUtils.isEmpty(title)){
		// title = "Export Window";
		// }else if(title.contains(" ")){
		// title = title.replaceAll(" ", "_");
		// }
		// exportTable.getPage().getContext().doPost(basePath+SERVLETURL, params, null);
		exportTable.getPage().getContext().doGet(basePath + SERVLETURL, params, "_self");

		// causes error in IE!
		// exportTable.getPage().getContext().sendJavaScript(exportTable.getId(),
		// "window.open('"+basePath+SERVLETURL+urlParameter.toString()+"','"+title+"');");
	}
}
