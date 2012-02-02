package org.webguitoolkit.ui.util.export;

import java.awt.Color;
import java.io.Serializable;

import org.webguitoolkit.ui.controls.event.IActionListener;

import com.lowagie.text.Font;

/**
 * @author i102454
 *
 */
/**
 * @author i102454
 *
 */
/**
 * @author i102454
 * 
 */
public class TableExportOptions implements Serializable {

	/**
	 * 
	 */
	public TableExportOptions() {
		super();
		this.pdfFontSize = Font.DEFAULTSIZE;
		this.pdfFontType = FONT_DEFAULT;
	}

	public TableExportOptions(boolean pdf, boolean excel) {
		super();
		this.setExportAsExcel(excel);
		this.setExportAsPDF(pdf);
		this.pdfFontSize = Font.DEFAULTSIZE;
		this.pdfFontType = FONT_DEFAULT;
	}

	// constants for table export
	public static final String PDFEXPORT = "pdf";
	public static final String EXCELEXPORT = "xls";
	public static final String PDF_LANDSCAPE = "landscape";
	public static final String PDF_PORTRAIT = "portait";

	// default E+H colours
	public static final Color COLOURS_WHITE = Color.WHITE;
	public static final Color COLOURS_GREY = new Color(153, 153, 153);
	public static final Color COLOURS_DARKGREY = new Color(102, 102, 102);
	public static final Color COLOURS_LIGHTGREY = new Color(204, 204, 204);
	public static final Color COLOURS_BLACK = Color.BLACK;
	public static final Color COLOURS_EH_BLUE = new Color(0, 153, 255);
	public static final Color COLOURS_MAGENTA = new Color(255, 0, 153);

	// default font types
	public static final int FONT_HELVETIVA = Font.HELVETICA;
	public static final int FONT_COURIER = Font.COURIER;
	public static final int FONT_TIMES_ROMAN = Font.TIMES_ROMAN;
	public static final int FONT_DEFAULT = Font.UNDEFINED;

	// PDF exclusive parameter
	private String headerImage;
	private String footerText;
	private String pdfButtonLabelText;
	private String pdfButtonTooltip;
	private boolean showDefaultHeader;
	private boolean showDefaultFooter;
	private boolean showPageNumber;
	private String pdfPosition;
	private Color pdfPageColour;
	private Color pdfFontColour;

	private int pdfFontSize;
	private int pdfFontType;

	// Excel exclusive parameter
	private String excelDateFormat;
	private String excelSheetName;
	private String excelButtonLabelText;
	private String excelButtonTooltip;

	// general parameter
	private boolean containsHTMLTags;
	private String fileName;
	private String tableHeadline;
	private String headline;
	private String tableFooter;

	private boolean exportAsPDF;
	private boolean exportAsExcel;
	private IActionListener pdfButtonListener;
	private IActionListener excelButtonListener;

	private boolean showOnlyDisplayedColumns = false;

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getTableFooter() {
		return tableFooter;
	}

	public void setTableFooter(String tableFooter) {
		this.tableFooter = tableFooter;
	}

	public String getExcelSheetName() {
		return excelSheetName;
	}

	public void setExcelSheetName(String excelSheetName) {
		this.excelSheetName = excelSheetName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHeaderImage() {
		return headerImage;
	}

	/**
	 * Image size muﬂ be height:40px width:200px
	 * 
	 * @param headerImage
	 */
	public void setHeaderImage(String headerImage) {
		this.headerImage = headerImage;
	}

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public boolean isShowDefaultHeader() {
		return showDefaultHeader;
	}

	public void setShowDefaultHeader(boolean showDefaultHeader) {
		this.showDefaultHeader = showDefaultHeader;
	}

	public boolean isShowDefaultFooter() {
		return showDefaultFooter;
	}

	public void setShowDefaultFooter(boolean showDefaultFooter) {
		this.showDefaultFooter = showDefaultFooter;
	}

	public boolean isShowPageNumber() {
		return showPageNumber;
	}

	public void setShowPageNumber(boolean showPageNumber) {
		this.showPageNumber = showPageNumber;
	}

	public String getPdfPosition() {
		return pdfPosition;
	}

	public void setPdfPosition(String pdfPosition) {
		this.pdfPosition = pdfPosition;
	}

	public Color getPdfPageColour() {
		return pdfPageColour;
	}

	public void setPdfPageColour(Color pdfPageColour) {
		this.pdfPageColour = pdfPageColour;
	}

	public Color getPdfFontColour() {
		return pdfFontColour;
	}

	public void setPdfFontColour(Color pdfFontColour) {
		this.pdfFontColour = pdfFontColour;
	}

	public int getPdfFontSize() {
		return pdfFontSize;
	}

	public void setPdfFontSize(int pdfFontSize) {
		this.pdfFontSize = pdfFontSize;
	}

	public boolean isExportAsPDF() {
		return exportAsPDF;
	}

	public void setExportAsPDF(boolean exportAsPDF) {
		this.exportAsPDF = exportAsPDF;
	}

	public String getExcelDateFormat() {
		return excelDateFormat;
	}

	public void setExcelDateFormat(String excelDateFormat) {
		this.excelDateFormat = excelDateFormat;
	}

	public boolean isExportAsExcel() {
		return exportAsExcel;
	}

	public void setExportAsExcel(boolean exportAsExcel) {
		this.exportAsExcel = exportAsExcel;
	}

	public boolean isContainsHTMLTags() {
		return containsHTMLTags;
	}

	public void setContainsHTMLTags(boolean containsHTMLTags) {
		this.containsHTMLTags = containsHTMLTags;
	}

	public String getTableHeadline() {
		return tableHeadline;
	}

	public void setTableHeadline(String tableHeadline) {
		this.tableHeadline = tableHeadline;
	}

	public int getPdfFontType() {
		return pdfFontType;
	}

	public void setPdfFontType(int pdfFontType) {
		this.pdfFontType = pdfFontType;
	}

	public String getPdfButtonLabelText() {
		return pdfButtonLabelText;
	}

	public void setPdfButtonLabelText(String pdfButtonLabelText) {
		this.pdfButtonLabelText = pdfButtonLabelText;
	}

	public String getPdfButtonTooltip() {
		return pdfButtonTooltip;
	}

	public void setPdfButtonTooltip(String pdfButtonTooltip) {
		this.pdfButtonTooltip = pdfButtonTooltip;
	}

	public String getExcelButtonLabelText() {
		return excelButtonLabelText;
	}

	public void setExcelButtonLabelText(String excelButtonLabelText) {
		this.excelButtonLabelText = excelButtonLabelText;
	}

	public String getExcelButtonTooltip() {
		return excelButtonTooltip;
	}

	public void setExcelButtonTooltip(String excelButtonTooltip) {
		this.excelButtonTooltip = excelButtonTooltip;
	}

	public IActionListener getPdfButtonListener() {
		return pdfButtonListener;
	}

	public void setPdfButtonListener(IActionListener pdfButtonListener) {
		this.pdfButtonListener = pdfButtonListener;
	}

	public IActionListener getExcelButtonListener() {
		return excelButtonListener;
	}

	public void setExcelButtonListener(IActionListener excelButtonListener) {
		this.excelButtonListener = excelButtonListener;
	}

	public boolean isShowOnlyDisplayedColumns() {
		return showOnlyDisplayedColumns;
	}

	public void setShowOnlyDisplayedColumns(boolean showOnlyDisplayedColumns) {
		this.showOnlyDisplayedColumns = showOnlyDisplayedColumns;
	}
}
