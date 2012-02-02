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
package org.webguitoolkit.ui.util.export;

/**
 * (c) 2008, Endress&Hauser InfoServe GmbH & Co KG
 * Created on 07.10.2008
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.DWRController;
import org.webguitoolkit.ui.controls.table.Table;

/**
 * @author Thorsten Springhart & Lars Brößler
 * 
 * 	
 * <servlet>
 * 		<servlet-name>ExportServlet</servlet-name>
 * 		<servlet-class>org.webguitoolkit.ui.util.export.ExportServlet</servlet-class>
 *         <init-param>
 *             <param-name>tableExportClass</param-name>
 *             <param-value>com.test.MyExportClass</param-value>
 *         </init-param>
 * 	</servlet>
 * 
 */
public class ExportServlet extends HttpServlet {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExportServlet.class);

	public static final int XLS_DEVICESHEET = 0;
	public static final int XLS_LOCATIONSHEET = 1;
	public static final int XLS_APPLICATIONSHEET = 2;
	public static final int XLS_LOOPSHEET = 3;
	public static final int XLS_BUSSHEET = 4;

	private static final long serialVersionUID = 5937773003774623423L;

	private static final String XLSTEMPLATE = "export//exporttemplate.xls";
	private static final int XLSTEMPLATE_STARTROW = 7;
	
	private List<ITableExport> tableExports = null;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// name of the table component which will be exported
		String componentName = request.getParameter("componentname");
		String type = request.getParameter("type");
		DWRController dwrcontroller = (DWRController)request.getSession().getAttribute(DWRController.class.getName());

		Table table = (Table)dwrcontroller.getComponentById(componentName);
		exportTable(request, response, table, type);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// name of the table component which will be exported
		String componentName = request.getParameter("componentname");
		String type = request.getParameter("type");
		DWRController dwrcontroller = (DWRController)request.getSession().getAttribute(DWRController.class.getName());

		Table table = (Table)dwrcontroller.getComponentById(componentName);
		exportTable(request, response, table, type);
	}

	/**
	 * @param request
	 * @param response
	 * @param exportformat
	 * @param exporttype
	 * @param body
	 */
	private void exportTable(HttpServletRequest request, HttpServletResponse response, Table table, String exportType) throws IOException {

		for( ITableExport export : tableExports ){
			if( export.canHandle( exportType ) ){
				export.writeTo(table, response);
				return;
			}
		}
	}

	@Override
	public void init() throws ServletException {
		super.init();
		tableExports = new ArrayList<ITableExport>();
		String tableExportClass = getInitParameter("tableExportClass");
		if( tableExportClass != null ){
			String[] exports = tableExportClass.split(";");
			for( String exp : exports ){
				if( StringUtils.isNotBlank( exp )){
					try{
						Object tableExport = Class.forName(exp.trim()).newInstance();
						tableExports.add((ITableExport)tableExport);
					}
					catch(ClassCastException ex ){
						logger.error("TableExport "+exp+" has to be of type ITableExport", ex);
					}
					catch (Throwable e) {
						logger.error("Error instantiating class: "+exp, e);
					}
				}
			}
		}
		tableExports.add(new PDFTableExport());
		tableExports.add(new ExcelTableExport());
		tableExports.add(new XMLTableExport());
	}
}
