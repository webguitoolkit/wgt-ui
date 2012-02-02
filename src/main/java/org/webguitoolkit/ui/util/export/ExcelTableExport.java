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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.renderer.CollectionToStringRenderer;
import org.webguitoolkit.ui.controls.table.renderer.ImageColumnRenderer;
import org.webguitoolkit.ui.controls.table.renderer.PlainHtmlColumnRenderer;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;

/**
 * converts ISGUIControl Table to HSSF Excel Sheet Assumption: the Default Table Model is in use
 * 
 * @author Thorsten Springhart & Lars Brößler
 * 
 */
public class ExcelTableExport extends TableExport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExcelTableExport.class);

	private HSSFCellStyle excelheadstyle;
	private HSSFCellStyle excelContentstyle;
	private HSSFCellStyle excelDateStyle;

	// constructor
	public ExcelTableExport() {
		super(EXPORT_TYPE_EXCEL);
	}

	/**
	 * @param table
	 * @param sheet
	 * @param footer
	 * @param header
	 * @param headline
	 * @return
	 */
	public HSSFSheet excelExport(Table table, HSSFSheet sheet) {
		HSSFRow row;
		HSSFCell cell;
		TableExportOptions exportOptions = table.getExportOptions();
		boolean showOnlyDisplayed = exportOptions.isShowOnlyDisplayedColumns();
		List<ITableColumn> columns = getTableColumns(showOnlyDisplayed, table);

		int colNr = columns.size();
		// first we add the headers
		int rownr = 0;

		if (StringUtils.isNotBlank(exportOptions.getHeadline())) {
			row = sheet.createRow(rownr);
			if (getExcelheadstyle() != null) {
				getExcelheadstyle().setWrapText(true);
			}

			double divider = 2;
			double putcol = Math.floor(colNr / divider) - 1;
			short shortCol = (short)putcol;
			cell = row.createCell(shortCol);
			if (getExcelheadstyle() != null) {
				cell.setCellStyle(getExcelheadstyle());
			}
			cell.setCellValue(new HSSFRichTextString(exportOptions.getHeadline()));

			rownr++;
			row = sheet.createRow(rownr);
			cell = row.createCell((short)0);
			cell.setCellValue(new HSSFRichTextString(""));
			rownr++;
		}

		if (StringUtils.isNotBlank(exportOptions.getTableHeadline())) {
			row = sheet.createRow(rownr);
			getExcelheadstyle().setWrapText(true);
			cell = row.createCell((short)0);
			cell.setCellValue(new HSSFRichTextString(exportOptions.getTableHeadline()));
			if (getExcelContentstyle() != null) {
				cell.setCellStyle(getExcelContentstyle());
			}
			rownr++;
		}

		row = sheet.createRow(rownr);
		for (int i = 0; i < columns.size(); i++) {
			cell = row.createCell((short)i);
			// remove html linebreaks from headline
			String tableTitle = TextService.getString(columns.get(i).getTitle());
			if (tableTitle.contains("<br/>") || tableTitle.contains("<br>")) {
				tableTitle = tableTitle.replaceAll("<br/>", " ");
				tableTitle = tableTitle.replaceAll("<br>", " ");
			}
			cell.setCellValue(new HSSFRichTextString(tableTitle));
			if (getExcelheadstyle() != null) {
				cell.setCellStyle(getExcelheadstyle());
			}
		}
		rownr++;
		List tabledata = table.getDefaultModel().getFilteredList();
		for (Iterator it = tabledata.iterator(); it.hasNext();) {
			row = sheet.createRow(rownr);
			DataBag dbag = (DataBag)it.next();
			for (int i = 0; i < columns.size(); i++) {
				logger.debug("property: " + columns.get(i).getProperty());
				IColumnRenderer renderer = columns.get(i).getRenderer();
				IConverter converter = columns.get(i).getConverter();
				Object obj = null;
				if (renderer != null && renderer instanceof ImageColumnRenderer) {
					// load image title, if available
					obj = dbag.get(columns.get(i).getProperty() + ".title");
				}
				else if (renderer != null && renderer instanceof CollectionToStringRenderer) {
					String objString = "";
					try {
						ArrayList<String> listOfStrings = (ArrayList<String>)dbag.get(columns.get(i).getProperty());
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
					obj = dbag.get(columns.get(i).getProperty() + ".title");
				}
				else {
					obj = dbag.get(columns.get(i).getProperty());
				}

				if (obj != null) {
					cell = row.createCell((short)i);
					try {
						if (obj instanceof String) {
							writeStringCell(cell, converter, obj, dbag);
						}
						else if (obj instanceof Number) {
							writeNumberCell(cell, converter, obj, dbag);
						}
						else if (obj instanceof Date || obj instanceof java.sql.Date || obj instanceof Timestamp) {
							writeDateCell(cell, converter, obj, dbag);
						}
						else {
							wirteObjectCell(cell, converter, obj, dbag);
						}
					}
					catch (Exception e) {
						logger.error("row = " + rownr + ", column = " + i + ", error " + e.getMessage(), e);
						cell.setCellValue(new HSSFRichTextString(""));
					}
				}
			}
			rownr++;
		}

		if (StringUtils.isNotBlank(exportOptions.getTableFooter())) {
			row = sheet.createRow(rownr);
			cell = row.createCell((short)0);
			cell.setCellValue(new HSSFRichTextString(""));
			rownr++;
			row = sheet.createRow(rownr);
			getExcelheadstyle().setWrapText(true);
			cell = row.createCell((short)0);

			cell.setCellValue(new HSSFRichTextString(exportOptions.getTableFooter()));
			if (getExcelContentstyle() != null) {
				cell.setCellStyle(getExcelContentstyle());
			}
		}
		return sheet;
	}

	protected void writeStringCell(HSSFCell cell, IConverter converter, Object obj, IDataBag dbag) {
		// most are string catch them first to save instance checks
		// remove html linebreaks from tabledate
		String tableData = obj.toString();
		if (converter != null) {
			tableData = (String)converter.convert(String.class, obj);
		}

		// replace html-line-breaks
		if (tableData.contains("<br/>") || tableData.contains("<br>")) {
			tableData = tableData.replaceAll("<br\\/>", " ");
			tableData = tableData.replaceAll("<br>", " ");
		}

		// replace html-blanks
		tableData = StringUtils.replace(tableData, "&nbsp;", " ");

		// image filter - image tags will be deleted
		String imgOpenTag = "<img";
		String imgCloseTag = ">";
		while (StringUtils.contains(tableData, imgOpenTag) && StringUtils.contains(tableData, imgCloseTag)) {
			tableData = StringUtils.remove(tableData, imgOpenTag + StringUtils.substringBetween(tableData, imgOpenTag, imgCloseTag)
					+ imgCloseTag);

		}

		cell.setCellValue(new HSSFRichTextString(tableData));
		if (getExcelContentstyle() != null) {
			cell.setCellStyle(getExcelContentstyle());
		}
	}

	protected void writeNumberCell(HSSFCell cell, IConverter converter, Object obj, IDataBag dbag) {
		// format numbers
		Double number = null;
		if (obj instanceof Integer) {
			number = new Double(((Integer)obj).doubleValue());
		}
		else if (obj instanceof Long) {
			number = new Double(((Long)obj).doubleValue());
		}
		else if (obj instanceof Short) {
			number = new Double(((Short)obj).doubleValue());
		}
		else if (obj instanceof Float) {
			number = new Double(((Float)obj).doubleValue());
		}
		else if (obj instanceof Double) {
			number = (Double)obj;
		}
		else if (obj instanceof BigDecimal) {
			number = ((BigDecimal)obj).doubleValue();
		}
		else if (obj instanceof BigInteger) {
			number = new Double(((BigInteger)obj).doubleValue());
		}
		if (number != null)
			cell.setCellValue(number.doubleValue());
	}

	protected void wirteObjectCell(HSSFCell cell, IConverter converter, Object obj, IDataBag dbag) {
		String cellData = obj.toString();

		if (converter != null) {
			cellData = (String)converter.convert(String.class, obj);
		}

		if (cellData == null) {
			cellData = "";
		}
		cell.setCellValue(new HSSFRichTextString(cellData));
		if (getExcelContentstyle() != null) {
			cell.setCellStyle(getExcelContentstyle());
		}
	}

	protected void writeDateCell(HSSFCell cell, IConverter converter, Object obj, IDataBag dbag) {
		if (converter != null) {
			String tobj = (String)converter.convert(String.class, obj);
			cell.setCellValue(tobj);
		}
		else {
			cell.setCellValue((Date)obj);
		}

		// format dates
		if (getExcelDateStyle() != null) {
			cell.setCellStyle(getExcelDateStyle());
		}
	}

	public HSSFCellStyle getExcelheadstyle() {
		return excelheadstyle;
	}

	public void setExcelheadstyle(HSSFCellStyle headstyle) {
		this.excelheadstyle = headstyle;
	}

	public HSSFCellStyle getExcelContentstyle() {
		return excelContentstyle;
	}

	public void setExcelContentstyle(HSSFCellStyle contentstyle) {
		this.excelContentstyle = contentstyle;
	}

	public void setExcelDateStyle(HSSFCellStyle excelDateStyle) {
		this.excelDateStyle = excelDateStyle;
	}

	public HSSFCellStyle getExcelDateStyle() {
		return excelDateStyle;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.util.export.ITableExport#writeTo(org.webguitoolkit.ui.controls.table.ITable, javax.servlet.http.HttpServletResponse)
	 */
	public void writeTo(Table table, HttpServletResponse response) {
		TableExportOptions exportOptions = table.getExportOptions();
		try {
			String filename = exportOptions.getFileName();
			if (StringUtils.isEmpty(filename)) {
				filename = StringUtils.isNotEmpty(table.getTitle()) ? table.getTitle() : "sheet";
			}
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + ".xls\"");
			try {
				writeTo(table, out);
			}
			catch (RuntimeException e1) {
				logger.error(e1);
				response.sendError(500, e1.getMessage());
				return;
			}
			finally {
				out.close();
			}
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.util.export.ITableExport#writeTo(org.webguitoolkit.ui.controls.table.ITable, java.io.OutputStream)
	 */
	public void writeTo(Table table, OutputStream out) {
		TableExportOptions exportOptions = table.getExportOptions();

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		HSSFFont fontbold = wb.createFont();
		fontbold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headstyle = wb.createCellStyle();
		headstyle.setFont(fontbold);
		setExcelheadstyle(headstyle);

		// create dateStyle
		HSSFCellStyle cellStyleDate = wb.createCellStyle();
		if (StringUtils.isNotEmpty(exportOptions.getExcelDateFormat())) {
			cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat(exportOptions.getExcelDateFormat()));
		}
		else {
			cellStyleDate.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		}
		setExcelDateStyle(cellStyleDate);

		sheet = excelExport(table, sheet);
		String sheetName = exportOptions.getExcelSheetName();
		if (StringUtils.isEmpty(sheetName)) {
			sheetName = StringUtils.isNotEmpty(table.getTitle()) ? table.getTitle() : "sheet";
		}
		if (sheetName.length() > 30) {
			sheetName = sheetName.substring(0, 30);
		}

		// DM: 19.11.2010: Slashes in Sheetname are not allowed, e.g. "Planned deliveries / disposals" did throw
		// IllegalArgumentException.
		// --> catch Exception and set 'Sheet1' as default.
		try {
			wb.setSheetName(0, sheetName);
		}
		catch (IllegalArgumentException e) {
			logger.error("Sheetname is not valid:" + sheetName + " using Sheet1 as default.", e);
			wb.setSheetName(0, "Sheet1");
		}
		try {
			wb.write(out);
		}
		catch (IOException e) {
			logger.error(e);
		}
	}
}
