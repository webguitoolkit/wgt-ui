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
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.Table;

/**
 * converts ISGUIControl Table to HSSF Excel Sheet Assumption: the Default Table Model is in use
 * 
 * @author Thorsten Springhart & Lars Brößler
 * 
 */
public class XMLTableExport extends TableExport {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(XMLTableExport.class);

	public XMLTableExport() {
		super(EXPORT_TYPE_XML);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.util.export.ITableExport#writeTo(org.webguitoolkit.ui.controls.table.ITable, javax.servlet.http.HttpServletResponse)
	 */
	public void writeTo(Table table, HttpServletResponse response) {

		TableExportOptions exportOptions = table.getExportOptions();

		String filename = exportOptions.getFileName();
		if (StringUtils.isEmpty(filename) && table instanceof Table) {
			filename = StringUtils.isNotEmpty(((Table)table).getTitle()) ? ((Table)table).getTitle() : "sheet";
		}
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/xml");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + ".xls\"");
			try {
				writeTo(table, out);
			}
			catch (RuntimeException e1) {
				logger.error("Error in xml export", e1);
				response.sendError(500, e1.getMessage());
				return;
			}
			finally{
				out.flush();
				out.close();
			}
		}
		catch (IOException e) {
			logger.error("Error in xml export", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.util.export.ITableExport#writeTo(org.webguitoolkit.ui.controls.table.ITable, java.io.OutputStream)
	 */
	public void writeTo(Table table, OutputStream out) {
		TableExportOptions exportOptions = table.getExportOptions();
		boolean showOnlyDisplayed = exportOptions.isShowOnlyDisplayedColumns();

		try {
			XMLStreamWriter sw = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
			logger.debug("export table " + ((Table)table).getTitle() + " to xml");
			sw.writeStartElement("export");

			List<ITableColumn> columns = getTableColumns(showOnlyDisplayed, table);
			List<?> tabledata = table.getDefaultModel().getFilteredList();
			int rownr = 1;
			for (Iterator<?> it = tabledata.iterator(); it.hasNext();) {
				sw.writeStartElement("row");
				sw.writeAttribute("rownumber", String.valueOf(rownr));
				DataBag dbag = (DataBag)it.next();
				for (ITableColumn column : columns) {
					Object obj = dbag.get(column.getProperty());
					if (obj != null) {
						writeObjectTag(sw, column, obj);
					}
				}
				sw.writeEndElement();
				rownr++;
			}
			sw.writeEndElement();
			sw.flush();
			sw.close();
		}
		catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		catch (FactoryConfigurationError e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeObjectTag(XMLStreamWriter sw, ITableColumn col, Object obj) throws XMLStreamException {
		sw.writeStartElement(col.getTitle().replaceAll(" ", "_"));
		writeObject(sw, obj);
		sw.writeEndElement();
	}

	protected void writeObject(XMLStreamWriter sw, Object obj) throws XMLStreamException {
		sw.writeCharacters(obj.toString());
	}
}
