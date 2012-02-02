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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.table.renderer.CollectionToStringRenderer;
import org.webguitoolkit.ui.controls.table.renderer.ImageColumnRenderer;
import org.webguitoolkit.ui.controls.table.renderer.PlainHtmlColumnRenderer;
import org.webguitoolkit.ui.controls.util.TextService;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * converts ISGUIControl Table to HSSF Excel Sheet Assumption: the Default Table Model is in use
 *
 * @author Thorsten Springhart & Lars Brößler
 *
 */
public class PDFTableExport extends TableExport {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PDFTableExport.class);

	// constructor
	public PDFTableExport() {
		super(EXPORT_TYPE_PDF);
	}

	/**
	 * @param table
	 * @param footer
	 * @param header
	 * @return
	 */
	public PdfPTable pdfExport(Table table) {
		TableExportOptions exportOptions = table.getExportOptions();
		boolean showOnlyDisplayed = exportOptions.isShowOnlyDisplayedColumns();
		Font headfont = new Font(Font.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD);

		Font bodyfont = new Font(exportOptions.getPdfFontSize(), exportOptions.getPdfFontSize(), Font.NORMAL);
		if (exportOptions.getPdfFontColour() != null) {
			Color fontColor = exportOptions.getPdfFontColour();
			bodyfont.setColor(fontColor);
			headfont.setColor(fontColor);
		}

		List<ITableColumn> columns = getTableColumns(showOnlyDisplayed, table);
		int columnCount = 0;
		for (int i = 0; i < columns.size(); i++) {
			TableColumn c = (TableColumn) columns.get(i);
			//hide select checkbox column
			if (!c.isExporatble()) 
				continue;
			
			columnCount++;

		}
		PdfPTable resulttable = new PdfPTable(columnCount);
		resulttable.setWidthPercentage(100f);

		if (StringUtils.isNotEmpty(exportOptions.getTableHeadline())) {
			PdfPCell cell = new PdfPCell(new Paragraph(exportOptions.getTableHeadline()));
			cell.setColspan(columns.size());
			cell.setBackgroundColor(Color.lightGray);
			resulttable.addCell(cell);
		}

		for (int i = 0; i < columns.size(); i++) {
			TableColumn c = (TableColumn) columns.get(i);
			// hide select checkbox column
			if (!c.isExporatble())
				continue;

			String cellData = TextService.getString(c.getTitle());
			if (cellData.contains("<br/>") || cellData.contains("<br>")) {
				cellData = cellData.replaceAll("<br\\/>", "\\/");
				cellData = cellData.replaceAll("<br>", "\\/");
			}
			PdfPCell cell = new PdfPCell(new Paragraph((cellData), headfont));
			cell.setBackgroundColor(Color.lightGray);
			resulttable.addCell(cell);

		}
		
		List tabledata = table.getDefaultModel().getFilteredList();
		if (tabledata.isEmpty()) {
			for (int i = 0; i < columns.size(); i++) {
				resulttable.addCell(new PdfPCell((new Phrase(""))));
			}			
		}
		
		for (Iterator it = tabledata.iterator(); it.hasNext();) {
			DataBag dbag = (DataBag)it.next();
			for (int i = 0; i < columns.size(); i++) {
				TableColumn tableColumn = (TableColumn)columns.get(i);
				if (!tableColumn.isExporatble()) 
					continue;
				logger.debug("property: " + tableColumn.getProperty());
				IColumnRenderer renderer = tableColumn.getRenderer();
				Converter converter = tableColumn.getConverter();
				
				addObjectCell(bodyfont, resulttable, dbag, tableColumn, renderer, converter);
			}
		}

		if (StringUtils.isNotEmpty(exportOptions.getTableFooter())) {
			PdfPCell cell = new PdfPCell(new Paragraph(exportOptions.getTableFooter()));
			cell.setColspan(columnCount);
			cell.setBackgroundColor(Color.lightGray);
			resulttable.addCell(cell);
		}

		resulttable.setHeaderRows(1);
		return resulttable;

	}


	protected void addObjectCell(Font bodyfont, PdfPTable resulttable, DataBag dbag, TableColumn tableColumn, IColumnRenderer renderer,
			Converter converter) {
		PdfPCell cell;
		Object obj = null;
		if (renderer != null && renderer instanceof ImageColumnRenderer) {
			// load image title, if available
			obj = dbag.get(tableColumn.getProperty() + ".title");
		}
		else if (renderer != null && renderer instanceof CollectionToStringRenderer) {
			String objString = "";
			try {
				ArrayList<String> listOfStrings = (ArrayList<String>)dbag.get(tableColumn.getProperty());
				for (String string : listOfStrings) {
					objString += string + ", ";
				}
			}
			catch (ClassCastException e) { // ignore
			}

			// remove comma and blank from end of the string
			objString = StringUtils.removeEnd(objString, ", ");

			obj = objString;
		}
		else if (renderer != null && renderer instanceof PlainHtmlColumnRenderer) {
			obj = dbag.get(tableColumn.getProperty() + ".title");
		}

		if (obj == null)
			obj = dbag.get(tableColumn.getProperty());
		String cellData = "";
		if (obj != null) {
			cellData = obj.toString();
			// fetch converter from table and use it to format the date/timestamp value
			if (converter != null) {
				if (obj instanceof Timestamp) {
					// Timestamp time = (Timestamp)obj;
					cellData = (String)converter.convert(String.class, obj);
				}
				else if (obj instanceof Date) {
					// Date date = (Date) obj;
					cellData = (String)converter.convert(String.class, obj);
				}
				else if (obj instanceof java.sql.Date) {
					// java.sql.Date date = (java.sql.Date) obj;
					cellData = (String)converter.convert(String.class, obj);
				}
				else if (obj instanceof String) {
					cellData = obj.toString();
				}
				else {
					cellData = obj.toString();
				}
			}

			if (cellData.contains("<br/>") || cellData.contains("<br>")) {
				cellData = cellData.replace("<br\\/>", "\\/");
				cellData = cellData.replace("<br>", "\\/");
			}
			// cell = new PdfPCell(new Paragraph((cellData),headfont));
			cell = new PdfPCell(new Paragraph((cellData), bodyfont));
		}
		else {
			cell = new PdfPCell(new Phrase(""));
		}
		resulttable.addCell(cell);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.util.export.ITableExport#writeTo(org.webguitoolkit.ui.controls.table.Table, javax.servlet.http.HttpServletResponse)
	 */
	public void writeTo(Table table, HttpServletResponse response) {
		
		TableExportOptions exportOptions = table.getExportOptions();
		
		try {
			String filename = exportOptions.getFileName();
			if (StringUtils.isEmpty(filename)) {
				filename = StringUtils.isNotEmpty(table.getTitle()) ? table.getTitle() : "pdf";
			}

			response.setContentType("application/pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + ".pdf\"");
			ServletOutputStream out = response.getOutputStream();
			try{
				writeTo(table, out);
			}
			catch( RuntimeException ex ){
				logger.error("Error writing table to stream!",ex);
				response.sendError(500, ex.getMessage());
			}
			out.close();
		}
		catch (IOException e) {
			logger.error("Error writing table to stream!",e);
		}
		finally {

		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.util.export.ITableExport#writeTo(org.webguitoolkit.ui.controls.table.Table, java.io.OutputStream)
	 */
	public void writeTo(Table table, OutputStream out) {
		TableExportOptions exportOptions = table.getExportOptions();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// set the page orientation Din-A4 Portrait or Landscape
		Rectangle page = PageSize.A4;
		if (exportOptions.getPdfPosition() != null && exportOptions.getPdfPosition().equals(TableExportOptions.PDF_LANDSCAPE)) {
			// landscape position
			page = PageSize.A4.rotate();
		}
		else if (exportOptions.getPdfPosition() != null && exportOptions.getPdfPosition().equals(TableExportOptions.PDF_PORTRAIT)) {
			// portrait position
			page = PageSize.A4;
		}
		if (exportOptions.getPdfPageColour() != null) {
			// page.setBackgroundColor(exportOptions.getPdfPageColour());
		}

		Document document = new Document(page);
		document.setMargins(36f, 36f, 50f, 40f);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(new PDFEvent(table));
			document.open();
			if (StringUtils.isNotEmpty(exportOptions.getHeadline())) {
				Font titleFont = new Font(Font.UNDEFINED, 18, Font.BOLD);
				Paragraph paragraph = new Paragraph(exportOptions.getHeadline(), titleFont);
				paragraph.setAlignment(Element.ALIGN_CENTER);
				document.add(paragraph);
				document.add(new Paragraph(" "));
				document.add(new Paragraph(" "));
			}

			PdfPTable pdftable = pdfExport(table);
			document.add(pdftable);
			document.newPage();
			document.close();

			baos.writeTo(out);
			out.flush();
			baos.close();
		}
		catch (DocumentException e) {
			logger.error("Error creating document!",e);
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			logger.error("Error creating document!",e);
			throw new RuntimeException(e);
		}
		finally {

		}		
	}
}
