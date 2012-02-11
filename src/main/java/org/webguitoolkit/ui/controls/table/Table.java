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
/*
 */
package org.webguitoolkit.ui.controls.table;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.Element;
import org.apache.ecs.html.Col;
import org.apache.ecs.html.ColGroup;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenu;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.DefaultCompoundModel;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.ISelectModel;
import org.webguitoolkit.ui.controls.form.button.StandardButton;
import org.webguitoolkit.ui.controls.table.renderer.CheckboxColumnRenderer;
import org.webguitoolkit.ui.controls.table.renderer.ChildColumnRenderer;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.http.resourceprocessor.JSResourceProcessor;
import org.webguitoolkit.ui.util.export.ExportButtonListener;
import org.webguitoolkit.ui.util.export.ITableExport;
import org.webguitoolkit.ui.util.export.TableExportOptions;

/**
 * <pre>
 * This represents the standard UI Control Table.
 * It is created by the table tag. The DWRController references it
 * by the css_id which it generates itself.
 * </pre>
 * 
 * @author arno
 */
public class Table extends BaseControl implements ITable, ICompoundLifecycleElement {

	/**
	 * 
	 */
	public static final String TABLE_EDIT_BUTTON_ID = "TABLE_EDIT_BUTTON_ID";
	public static final String TABLE_PDF_BUTTON_ID = "TABLE_PDF_BUTTON_ID";
	public static final String TABLE_EXCEL_BUTTON_ID = "TABLE_EXCEL_BUTTON_ID";
	public static final String DOT_COMPOUND = ".compound";
	// constants for events ------------------------------------------
	public static final int EVENT_CLICK_DOWN = 0;
	public static final int EVENT_PAGE_DOWN = 1;
	public static final int EVENT_GOTO_BOTTOM = 2;
	public static final int EVENT_CLICK_UP = 3;
	public static final int EVENT_PAGE_UP = 4;
	public static final int EVENT_GOTO_TOP = 5;
	public static final int EVENT_ROW_SELECTED = 6;
	public static final int EVENT_IFILTER = 7;
	public static final int EVENT_DDFILTER = 8;
	public static final int EVENT_SORT = 9;
	public static final int EVENT_GOTO_ROW = 10;
	public static final int EVENT_EDIT_TABLE = 11;
	public static final int EVENT_SCROLL_TO = 12;
	public static final int EVENT_DROPPED = 13;
	public static final int EVENT_DRAG_START = 15;
	public static final int EVENT_CHECKALL = 14;

	// other constants used for creating ids
	protected static final String SORTBY_COLUMN = ".sortbyColumn";
	protected static final String SORT_ASC = ".sortasc";

	// Table filters are displayed at top of the table as drop downs
	protected List<ITableFilter> tableFilters = new ArrayList<ITableFilter>();
	protected List<ITableColumn> tableColumns = new ArrayList<ITableColumn>();
	protected List<IRowHandler> rowHandler = new ArrayList<IRowHandler>();
	protected IContextMenu defaultContextMenu = null;
	protected List<IButton> buttons = new ArrayList<IButton>();
	protected List<String> errorList;
	protected TD buttonHeaderCell = new TD();

	// compound where the table is embedded in, see ICompoundCycle
	protected Compound compound;
	protected String rowCssClass = "wgtTRNormal";
	protected String rowCssClassAlternate = "wgtTRAlternate";

	protected int rows = 5;
	protected String width;
	protected String title;
	protected boolean fireRowSelectEvent = true;
	protected ITableListener listener;
	protected ColumnConfig columnConfig = null;
	protected String displayMode = DISPLAY_MODE_SCROLLBAR;

	protected ListenerManager serverListener = new ListenerManager();

	// the compound used to make the table editable
	// Note this compound uses flightweight pattern
	protected Compound rowCompound;

	protected ITableModel model;
	protected boolean droppable;
	protected boolean draggable;
	protected String draggableProperty;
	protected boolean editable = true;

	protected TableExportOptions exportOptions;

	public IColumnRenderer defaultRenderer;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public Table() {
		super();
		setDefaultCssClass("wgtTable");
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Table(String id) {
		super(id);
		setDefaultCssClass("wgtTable");
	}

	public List<ITableColumn> getColumns() {
		return tableColumns;
	}

	public void setColumns(List<ITableColumn> columns) {
		this.tableColumns = columns;
	}

	public List<ITableFilter> getFilters() {
		return tableFilters;
	}

	public void setFilters(List<ITableFilter> filters) {
		this.tableFilters = filters;
	}

	protected void drawButtons(TD buttonHeaderCell) {
		for (Iterator it = getButtons().iterator(); it.hasNext();) {
			Button button = (Button)it.next();
			buttonHeaderCell.addElement(new EcsAdapter(button));
		}

		for (Iterator<BaseControl> it = getAdditionalTitleBaseConrols().iterator(); it.hasNext();) {
			BaseControl baseControl = it.next();
			buttonHeaderCell.addElement(new EcsAdapter(baseControl));
		}
	}

	/**
	 * @param out
	 */
	@Override
	public synchronized void draw(PrintWriter out) {

		if (columnConfig == null) {
			initColumnConfig();
		}

		IContext ctx = getContext();
		ctx.restoreState(id4highlite());
		ctx.restoreState(id4RowInput());

		int colCount = columnConfig.getSize();
		// is there at least one filter?
		boolean existFilter = false;
		for (int i = 0; !existFilter && i < colCount; i++) {
			existFilter |= getColumn(i).isFilter();
		}
		// show triggercol if filterimage is needed
		// if (!displayTrigger)
		// displayTrigger = existFilter || displayCheckboxes;

		org.apache.ecs.html.Table table = new org.apache.ecs.html.Table();
		stdParameter(table);

		ColGroup colGroup = new ColGroup();
		for (int i = 0; i < colCount; i++) {
			Col col = new Col();
			if (StringUtils.isNotEmpty(getColumn(i).getWidth())) {
				col.setStyle("width: " + getColumn(i).getWidth() + ";");
			}
			colGroup.addElement(col);
		}
		if (DISPLAY_MODE_SCROLLBAR.equals(displayMode)) {
			Col col = new Col();
			col.setWidth(12);
			colGroup.addElement(col);
		}
		table.addElement(colGroup);
		// TBody tableBody = new TBody();
		// table.addElement(tableBody);

		StringWriter tableBodyWriter = new StringWriter();

		// ########### TITLE ############
		String title = getContext().processValue(id4Header());
		boolean titelVis = getContext().processBool(id4Header() + Context.DOT_VIS);

		if (title != null || editable) {
			TR tr = new TR();
			TH th = new TH();
			th.setColSpan(99);
			th.setStyle("padding:0;");
			// create title bar
			org.apache.ecs.html.Table titleTable = new org.apache.ecs.html.Table();
			ColGroup titleColGroup = new ColGroup();
			titleTable.setCellPadding(1);
			titleTable.setCellSpacing(0);
			titleTable.setWidth("100%");
			TR titleTr = new TR();

			TD titleTd = new TD();
			titleTd.setID(id4Header());
			titleTd.setClass("wgtTableHeader");
			titleTd.setWidth("*");
			if (titelVis) {
				titleTd.setStyle("white-space: nowrap;");
				titleTd.addElement(title);
			}
			else
				titleTd.setStyle("white-space: nowrap; display: none;");
			titleTr.addElement(titleTd);

			TD colorflow = new TD();
			if (!titelVis)
				colorflow.setStyle("display: none;");
			colorflow.setID(id4Header() + "cf");
			colorflow.addElement("<img src='./images/wgt/1.gif' width='25px' height='1px'>");
			colorflow.setClass("wgtTableHeaderLeftofButtons");
			titleTr.addElement(colorflow);

			if (editable || !getButtons().isEmpty() || !getAdditionalTitleBaseConrols().isEmpty()) {
				buttonHeaderCell = new TD();
				buttonHeaderCell.setClass("wgtTableHeaderButtons");
				buttonHeaderCell.setStyle("width: 99%");
				if (!getButtons().isEmpty() || !getAdditionalTitleBaseConrols().isEmpty()) {
					drawButtons(buttonHeaderCell);
				}
				else {
					buttonHeaderCell.addElement(" ");
				}
				titleTr.addElement(buttonHeaderCell);
				if (editable) {
					titleTr.addElement(getTableEditCell());
				}
			}
			titleTable.addElement(titleTr);
			th.addElement(titleTable);
			tr.addElement(th);
			// tableBody.addElement(tr);
			tr.output(tableBodyWriter);
		}

		// ########### DROP DOWN FILTER ############

		if (tableFilters.size() > 0) {
			TR tr = new TR();
			TD td = new TD();
			td.setClass("wgtTableFilter");
			td.setColSpan(colCount + 1);
			tr.addElement(td);

			org.apache.ecs.html.Table filterTable = new org.apache.ecs.html.Table();
			filterTable.setStyle("wgtFilterTable");
			td.addElement(filterTable);

			TR filterTr = new TR();
			filterTable.addElement(filterTr);

			TableFilterListener tfl = new TableFilterListener();
			for (int i = 0; i < tableFilters.size(); i++) {
				TableFilter filter = (TableFilter)tableFilters.get(i);
				TD filterTd = new TD();
				filterTr.addElement(filterTd);

				Span span = new Span();
				span.setClass("wgtFilterText");
				span.addElement(filter.getLabel());
				filterTd.addElement(span);

				if (!filter.hasActionListener()) {
					filter.setActionListener(tfl);
				}

				filterTd = new TD();
				filterTr.addElement(filterTd);

				List elements = filter.createHtmlElements();
				for (Iterator iter = elements.iterator(); iter.hasNext();)
					filterTd.addElement((Element)iter.next());
			}
			// tableBody.addElement(tr);
			tr.output(tableBodyWriter);
		}

		// ########### COLUMN HEADER ############

		TR tr = new TR();
		for (int i = 0; i < colCount; i++) {
			String oldSortedCol = getContext().getValue(getId() + SORTBY_COLUMN);
			boolean sortASC = getContext().getValueAsBool(getId() + SORT_ASC);

			TableColumn column = getColumn(i);
			TD td = column.drawHeader(id4column(i), oldSortedCol, sortASC);
			tr.addElement(td);
		}
		if (DISPLAY_MODE_SCROLLBAR.equals(displayMode)) {
			TD td = new TD();
			td.setClass("wgtTableColumnHeaderCell");
			td.addElement("&nbsp;");
			tr.addElement(td);
		}
		tr.output(tableBodyWriter);
		// tableBody.addElement(tr);

		// ########### TEXT FILTER (IMPLICIT FILTER) ############

		if (existFilter) {
			// Filter image ----------------------------------------
			tr = new TR();
			for (int i = 0; i < colCount; i++) {
				TableColumn column = getColumn(i);
				TD td = column.drawFilter(id4iFilter(i));
				tr.addElement(td);
			}
			if (DISPLAY_MODE_SCROLLBAR.equals(displayMode)) {
				TD td = new TD();
				td.setClass("wgtTableFilterCell");
				tr.addElement(td);
			}
			tr.output(tableBodyWriter);
			// tableBody.addElement(tr);
		}

		// ############ CONTENT AREA ####################

		// darstellen mit inhalt, daten aus dem Context nehmen.
		String allEntries = getContext().processValue(id4size());
		for (int i = 0; i < rows; i++) {
			String style = getContext().processValue(id4Row(i) + ".style");
			String styleClass = ((i % 2) == 0) ? rowCssClass : rowCssClassAlternate;

			String fireevent = "";
			if (fireRowSelectEvent) {
				fireevent = JSUtil.jsEventParam(getId(), new String[] { "" + i }) + JSUtil.jsFireEvent(getId(), EVENT_ROW_SELECTED);
			}

			tr = new TR();
			tr.setClass("wgtPointerCursor " + styleClass);
			if (style != null)
				tr.setStyle(style);
			tr.setOnClick(JSUtil.jsHighlite(id4highlite()) + fireevent);
			tr.setID(id4Row(i));

			for (int j = 0; j < colCount; j++) {
				TableColumn column = getColumn(j);
				TD td = column.drawCell(id4TD(i, j), i, j);
				tr.addElement(td);
			}
			if (i == 0 && DISPLAY_MODE_SCROLLBAR.equals(displayMode)) { // feature not ready jet

				// ########### SCROLLBAR #############

				if (StringUtils.isEmpty(allEntries))
					allEntries = "1";
				int all = Integer.parseInt(allEntries);
				// add cell for scroll bar to the last column of the first row and setRowSpan
				TD td = new TD();
				td.setStyle("padding: 0px;vertical-align: top; background-color: #cccccc; width: 12px;");
				td.setOnClick("event.cancelBubble=true;");
				td.setRowSpan(getRows());

				if (DISPLAY_MODE_SCROLLBAR.equals(displayMode))
					td.addElement(getScrollTable(all));

				tr.addElement(td);
			}

			// tableBody.addElement(tr);
			tr.output(tableBodyWriter);
		}

		// ########### FOOTER ###############

		if (DISPLAY_MODE_SCROLL_BUTTONS.equals(displayMode)) {
			// tableBody.addElement(getFooter(colCount, allEntries));
			getFooter(colCount, allEntries).output(tableBodyWriter);
			table.addElement(tableBodyWriter.getBuffer().toString());
			table.output(out);
		}
		else {
			table.addElement(tableBodyWriter.getBuffer().toString());
			table.output(out);

			Input rowInput = new Input();
			rowInput.setID(id4RowInput());
			rowInput.setType("hidden");
			String value = getContext().processValue(id4RowInput());
			rowInput.setValue(StringUtils.isNotEmpty(value) ? value : "");
			rowInput.output(out);

			Span span = new Span();
			span.setID(id4size());
			span.setStyle("display: none;");
			span.addElement(allEntries);
			span.output(out);

		}

		initDraggable();
		initDroppable();
		if (DISPLAY_MODE_SCROLLBAR.equals(displayMode))
			getContext().sendJavaScript(getId() + ".js", "initScrollbar('" + getId() + "');");
		if (editable)
			getContext().sendJavaScript(getId() + ".js", "initTableEditor('" + getId() + "');");
	}

	@Override
	protected void endHTML(PrintWriter out) {
		// no need to overwrite this cause we overwrite the draw method
	}

	/**
	 * Create table footer with buttons for scrolling
	 * 
	 * @param colCount
	 * @param allEntries
	 * @return
	 */
	private TR getFooter(int colCount, String allEntries) {

		String style = "";

		TR tr = new TR();
		tr.setClass("wgtTableFooter");

		TD td = new TD();
		td.setColSpan(colCount + 1);
		tr.addElement(td);

		IMG image = new IMG();
		image.setClass("wgtScrollIcon");
		image.setBorder(0);
		image.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_GOTO_TOP));
		image.setSrc("./images/wgt/table/top.gif");
		image.setTitle(TextService.getString("table.scroll.totop@to top"));
		td.addElement(image);

		image = new IMG();
		image.setClass("wgtScrollIcon");
		image.setBorder(0);
		image.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_PAGE_UP));
		image.setSrc("./images/wgt/table/page_up.gif");
		image.setTitle(TextService.getString("table.scroll.pageup@page up"));
		td.addElement(image);

		image = new IMG();
		image.setClass("wgtScrollIcon");
		image.setBorder(0);
		image.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_CLICK_UP));
		image.setSrc("./images/wgt/table/up.gif");
		image.setTitle(TextService.getString("table.scroll.up@up"));
		td.addElement(image);
		td.addElement("&nbsp;");

		Input rowInput = new Input();
		rowInput.setID(id4RowInput());
		rowInput.setType("text");
		rowInput.setClass("wgtTableFooterInputText");
		rowInput.setSize(3);
		String value = getContext().processValue(id4RowInput());
		rowInput.setValue(StringUtils.isNotEmpty(value) ? value : "");
		rowInput.setOnKeyDown("if (event.keyCode==13) {" + JSUtil.jsFireEvent(getId(), EVENT_GOTO_ROW) + " return false;};");
		td.addElement(rowInput);

		Span span = new Span();
		span.setClass("wgtTableFooterText");
		span.addElement(" " + TextService.getString("table.of@of") + " ");
		td.addElement(span);

		span = new Span();
		span.setID(id4size());
		span.setClass("wgtTableFooterText");
		span.addElement(allEntries);
		td.addElement(span);

		image = new IMG();
		image.setClass("wgtScrollIcon");
		image.setBorder(0);
		image.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_CLICK_DOWN));
		image.setSrc("./images/wgt/table/down.gif");
		image.setTitle(TextService.getString("table.scroll.down@down"));
		td.addElement(image);

		image = new IMG();
		image.setClass("wgtScrollIcon");
		image.setBorder(0);
		image.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_PAGE_DOWN));
		image.setSrc("./images/wgt/table/page_down.gif");
		image.setTitle(TextService.getString("table.scroll.pagedown@page down"));
		td.addElement(image);

		image = new IMG();
		image.setClass("wgtScrollIcon");
		image.setBorder(0);
		image.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_GOTO_BOTTOM));
		image.setSrc("./images/wgt/table/bottom.gif");
		image.setTitle(TextService.getString("table.scroll.tobottom@to bottom"));
		td.addElement(image);

		return tr;
	}

	/**
	 * creates the table for the scroll bar
	 * 
	 * @param allEntries
	 * @return scroll bar table
	 */
	private org.apache.ecs.html.Table getScrollTable(int allEntries) {
		float factor = 1;
		if (getRows() < allEntries)
			factor = (float)getRows() / (float)allEntries;
		if (factor * getRows() < 0.5) // factor has to be at least a half row
			factor = (float)0.5 / getRows();

		org.apache.ecs.html.Table scrollBarTable = new org.apache.ecs.html.Table();
		scrollBarTable.setCellSpacing(0);
		scrollBarTable.setStyle("height: 100%;");

		TR tr = new TR();
		scrollBarTable.addElement(tr);

		TD td = new TD();
		td.setStyle("padding: 0px;");
		tr.addElement(td);

		IMG img = new IMG();
		img.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_CLICK_UP));
		img.setBorder(0);
		img.setSrc("./images/wgt/table/up.gif");
		img.setTitle(TextService.getString("table.scroll.up@up"));
		td.addElement(img);

		tr = new TR();
		tr.setStyle("height: 100%");
		scrollBarTable.addElement(tr);

		td = new TD();
		td.setStyle("vertical-align: top;height: 100%;");
		td.setID(getId() + "_dragbar");
		td.setOnClick("calculateLineChange(this, '" + getId() + "', event);event.cancelBubble=true;");
		tr.addElement(td);

		Div div = new Div();
		div.setID(getId() + "_drag");
		div.setClass("drag-y");
		div.setStyle("padding-top: 0px; width: 12px; height: " + (factor * 100) + "%; background-color: #ffffff;");
		div.setOnClick("event.cancelBubble=true;");
		td.addElement(div);

		tr = new TR();
		scrollBarTable.addElement(tr);

		td = new TD();
		td.setStyle("padding: 0px; vertical-align: bottom;");
		tr.addElement(td);

		img = new IMG();
		img.setOnClick(JSUtil.jsFireEvent(getId(), EVENT_CLICK_DOWN));
		img.setBorder(0);
		img.setSrc("./images/wgt/table/down.gif");
		img.setTitle(TextService.getString("table.scroll.down@down"));
		td.addElement(img);

		return scrollBarTable;
	}

	private TD getTableEditCell() {

		TD td = new TD();
		td.setClass("wgtTableHeaderButtons");
		td.setStyle("width:19px;text-align:right;");
		if (editable) {
			org.apache.ecs.html.Button buttonEdit = new org.apache.ecs.html.Button();
			buttonEdit.setID(TABLE_EDIT_BUTTON_ID);
			buttonEdit.setClass("wgtButton2");
			buttonEdit.setOnClick("toggleTableEditPopup( '" + getId() + "' );");
			buttonEdit.setTitle(TextService.getString("table.editbutton.tooltip@edit table configuration"));
			Span outher = new Span();
			buttonEdit.addElement(outher);
			outher.setID(getId() + ".outherspan");
			Span inner = new Span();
			outher.addElement(inner);
			inner.setID(getId() + ".innerspan");
			IMG editTableImg = new IMG("./images/wgt/table/edit_table.gif", "0");
			inner.addElement(editTableImg);
			td.addElement(buttonEdit);

			Div editTableDiv = new Div();
			editTableDiv.setID(getId() + "_editTableDiv");
			editTableDiv.setClass("wgtTableEditDiv");
			editTableDiv.setStyle("display: none;");
			td.addElement(editTableDiv);

			org.apache.ecs.html.Table table = new org.apache.ecs.html.Table();
			table.setID(getId() + "_cols");
			table.setClass("wgtTableEditTable");
			table.setCellSpacing(0);
			editTableDiv.addElement(table);

			TR headTr = new TR();
			table.addElement(headTr);

			TD headTd = new TD();
			headTd.setClass("wgtTableEditHeaderCell");
			headTr.addElement(headTd);
			headTd.addElement(TextService.getString("table.show.column@show"));

			TD headTd2 = new TD();
			headTd2.setClass("wgtTableEditHeaderCell");
			headTr.addElement(headTd2);
			headTd2.addElement(TextService.getString("table.column@column"));

			TD headTd4 = new TD();
			headTd4.setClass("wgtTableEditHeaderCell");
			headTr.addElement(headTd4);
			headTd4.addElement(TextService.getString("table.up@up"));

			TD headTd5 = new TD();
			headTd5.setClass("wgtTableEditHeaderCell");
			headTd5.setStyle("border-right:0px;");
			headTr.addElement(headTd5);
			headTd5.addElement(TextService.getString("table.down@down"));

			for (int i = 0; i < tableColumns.size(); i++) {
				TableColumn tableColumn = (TableColumn)tableColumns.get(i);
				TR colTr = new TR();
				colTr.setID(tableColumn.getId() + "_conf");
				table.addElement(colTr);

				TD colTd = new TD();
				colTd.setClass("wgtTableEditCell");
				colTr.addElement(colTd);

				Input hidden = new Input("hidden", "", tableColumn.getProperty());
				colTd.addElement(hidden);

				Input checkbox = new Input("checkbox", "", "true");
				checkbox.setClass("wgtInputCheckbox");
				if (tableColumn.isMandatory())
					checkbox.setDisabled(true);
				checkbox.setChecked(tableColumn.getIsDisplayed());
				colTd.addElement(checkbox);

				String width = "";
				if (tableColumn.getWidth() != null)
					width = tableColumn.getWidth();

				Input widthInput = new Input("hidden", "", width);
				widthInput.setClass("wgtInputText");
				widthInput.setStyle("width: 50px;");
				widthInput.setOnKeyDown("if (event.keyCode==38){ changeTableRowWidth( this, 1 ); } else if (event.keyCode==40 ){ changeTableRowWidth( this, -1 ); }");
				colTd.addElement(widthInput);

				TD colTd2 = new TD();
				colTd2.setClass("wgtTableEditCell");
				String title = tableColumn.textForTitle();
				if (StringUtils.isBlank(title) && !StringUtils.isEmpty(tableColumn.getTooltip()))
					title = tableColumn.getTooltip();
				colTd2.addElement(title);
				colTr.addElement(colTd2);

				TD colTd4 = new TD();
				colTd4.setClass("wgtTableEditCell");
				colTr.addElement(colTd4);

				IMG upImg = new IMG("./images/wgt/table/move_up.gif", "0");
				upImg.setTitle(TextService.getString("table.up@up"));
				upImg.setOnClick("tableUp('" + tableColumn.getId() + "');");
				upImg.setStyle("cursor: pointer;");
				colTd4.addElement(upImg);

				TD colTd5 = new TD();
				colTd5.setClass("wgtTableEditCell");
				colTr.addElement(colTd5);

				IMG downImg = new IMG("./images/wgt/table/move_down.gif", "0");
				downImg.setTitle(TextService.getString("table.down@down"));
				downImg.setOnClick("tableDown('" + tableColumn.getId() + "');");
				downImg.setStyle("cursor: pointer;");
				colTd5.addElement(downImg);
			}

			TR trRows = new TR();
			trRows.setClass("wgtTableEditRowsRow");
			table.addElement(trRows);

			TD td1 = new TD();
			td1.setColSpan(2);
			td1.setStyle("font-size:12px; font-weight:bold;");
			td1.addElement(TextService.getString("table.row.input@Rows:"));
			trRows.addElement(td1);

			TD td2 = new TD();
			td2.setColSpan(2);
			trRows.addElement(td2);

			Input rowInput = new Input("text", "", getRows());
			rowInput.setID(getId() + "_rows");
			rowInput.setClass("wgtInputText");
			rowInput.setMaxlength(2);
			rowInput.setStyle("width: 50px;");
			rowInput.setOnKeyDown("if (event.keyCode==38){ changeTableRows( this, 1 ); } else if (event.keyCode==40 ){ changeTableRows( this, -1 ); }");
			td2.addElement(rowInput);

			TR trButton = new TR();
			table.addElement(trButton);

			TD tdButton = new TD();
			tdButton.setColSpan(5);
			tdButton.setAlign("center");
			tdButton.setVAlign("bottom");
			trButton.addElement(tdButton);

			org.apache.ecs.html.Table buttonTable = new org.apache.ecs.html.Table();
			tdButton.addElement(buttonTable);

			TR trButtonTable = new TR();
			buttonTable.addElement(trButtonTable);

			TD tdButtonSave = new TD();
			tdButtonSave.setVAlign("bottom");
			trButtonTable.addElement(tdButtonSave);

			org.apache.ecs.html.A button = new org.apache.ecs.html.A();
			button.setHref("#");
			button.setOnClick("editTable('" + getId() + "'); return false;");
			button.setOnKeyDown("if (event.keyCode==13) {editTable('" + getId() + "');}; return false;");
			button.setClass("wgtButton2");
			Span outherButton = new Span();
			button.addElement(outherButton);
			Span innerButton = new Span();
			outherButton.addElement(innerButton);
			Div label = new Div();
			label.setClass("text");
			label.addElement(TextService.getString("save"));
			innerButton.addElement(label);
			tdButtonSave.addElement(button);

			TD tdButtonCancel = new TD();
			tdButtonCancel.setVAlign("bottom");
			trButtonTable.addElement(tdButtonCancel);

			button = new org.apache.ecs.html.A();
			button.setHref("#");
			button.setOnClick("onCancelTableEdit('" + getId() + "');return false;");
			button.setOnKeyDown("if (event.keyCode==13) { onCancelTableEdit('" + getId() + "');  }; return false;");
			button.setClass("wgtButton2");
			outherButton = new Span();
			button.addElement(outherButton);
			innerButton = new Span();
			outherButton.addElement(innerButton);
			label = new Div();
			label.setClass("text");
			label.addElement(TextService.getString("cancel"));
			innerButton.addElement(label);
			tdButtonCancel.addElement(button);

			Div dragCol = new Div();
			dragCol.setID(getId() + "_dragCol");
			dragCol.setStyle("display: none;");
			tdButtonCancel.addElement(dragCol);
		}
		return td;
	}

	@Override
	public void dispatch(ClientEvent event) {
		switch (event.getTypeAsInt()) {
			case EVENT_CLICK_DOWN:
				getListener().onClickDown(event);
				break;
			case EVENT_CLICK_UP:
				getListener().onClickUp(event);
				break;
			case EVENT_GOTO_BOTTOM:
				getListener().onGotoBottom(event);
				break;
			case EVENT_GOTO_ROW:
				getListener().onGotoRow(event);
				break;
			case EVENT_GOTO_TOP:
				getListener().onGotoTop(event);
				break;
			case EVENT_PAGE_DOWN:
				getListener().onPageDown(event);
				break;
			case EVENT_PAGE_UP:
				getListener().onPageUp(event);
				break;
			case EVENT_ROW_SELECTED:
				getListener().onRowSelected(event);
				break;
			case EVENT_IFILTER:
				getListener().onImplicitFilter(event);
				break;
			case EVENT_DDFILTER:
				getListener().onDDFilter(event);
				break;
			case EVENT_SORT:
				getListener().onSort(event);
				break;
			case EVENT_EDIT_TABLE:
				if (StringUtils.isNumeric(event.getParameter(0))) {
					int rowCount = Integer.parseInt(event.getParameter(0));
					String tableSetting = event.getParameter(1);
					getListener().onEditTableLayout(event, rowCount, tableSetting);
				}
				break;
			case EVENT_SCROLL_TO:
				getListener().onScrollTo(event);
				break;
			case EVENT_DROPPED:
				getListener().onDropped(event, event.getParameter(0), event.getParameter(1));
				break;
			case EVENT_DRAG_START:
				getListener().onDragStart(event, event.getParameter(0));
				break;
			case EVENT_CHECKALL:
				getListener().onCheckAll(event);
				break;
			default:
				throw new WGTException("unknown event type, got " + event.getTypeAsInt());
		}
	}

	/**
	 * reload informs the table-component, that the complete model has changed, and it should not assume that the filers stayed
	 * the same
	 */
	public void reload() {
		reload(getOffset());
	}

	public void reload(int firstRow) {
		int rownum = runFilter();

		getContext().add(id4size(), rownum, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		// remove the highlight as we cannot identify it.
		getContext().add(id4lead(), -1, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		initFilterModel(); // each dataload may bear some changes in the model result in filter changes
		load(firstRow, true);
	}

	protected int runFilter() {
		IContext context = getContext();

		// DropDownFilter --------------------------
		Map<String, String> dropDownFilters = new HashMap<String, String>();
		// also pass into the model, the models of the filter to be able to interpret the given values.
		Map<String, ISelectModel> dropDownFilterModels = new HashMap<String, ISelectModel>();
		for (int i = 0; i < tableFilters.size(); i++) {
			TableFilter filter = (TableFilter)tableFilters.get(i);
			String filterId = filter.getId();
			String val = filter.getValue();
			if (val != null && !val.equals(TableFilter.EMPTY)) {
				dropDownFilters.put(filterId, val);
				dropDownFilterModels.put(filterId, filter.getModel());
			}
		}

		// ImplicitFilter -------------------------------------------------
		Map<String, String> ifmap = new HashMap<String, String>();
		Map<String, ITableColumn> ifmapcolumns = new HashMap<String, ITableColumn>();

		if (columnConfig == null)
			initColumnConfig();
		for (int i = 0; i < columnConfig.getSize(); i++) {
			String fval = context.getValue(id4iFilter(i));
			String propper = null;
			if (StringUtils.isNotEmpty(fval)) {
				propper = columnConfig.getSetting(i).getProperty();
				if (this.getModel() instanceof DefaultTreeTableModel) {
					IRowHandler rh = this.getRowHandler(((DefaultTreeTableModel)this.getModel()).getGroupBy());
					if (rh != null && rh.getMappedProperty(propper) != null)
						propper = rh.getMappedProperty(propper);
				}
				ifmap.put(propper, fval);
				int columnNbr = getColumnId(propper);
				if (columnNbr > -1 && getColumn(columnNbr).getRenderer() != null)
					ifmapcolumns.put(propper, getColumn(getColumnId(propper)));
			}
		}
		// sort column --------------------------------------
		String sortCol = context.getValue(getId() + SORTBY_COLUMN);
		boolean asc = context.getValueAsBool(getId() + SORT_ASC);

		String sortName = null;
		String sortProperty = sortCol;
		if (sortCol != null) { // else no sorting
			if (this.getModel() instanceof DefaultTreeTableModel) {
				IRowHandler rh = this.getRowHandler(((DefaultTreeTableModel)this.getModel()).getGroupBy());
				if (rh != null && rh.getMappedProperty(sortProperty) != null)
					sortProperty = rh.getMappedProperty(sortProperty);
			}
		}
		return getModel().applyFilterAndSorting(dropDownFilterModels, dropDownFilters, ifmap, ifmapcolumns, sortProperty, asc);
	}

	/**
	 * asks the model to return the data for the view and packs the data into the context.
	 * 
	 * @param firstRow
	 */
	public void load() {
		load(getOffset(), false);
	}

	public void load(int offset) {
		load(offset, false);
	}

	public void load(int offset, boolean isReload) {
		offset = boxIndex(offset);
		// write old value into model.
		if (!isReload) {
			// reload means we don't want the screen data to
			// be unloaded and produce errors again.
			save2Bag();
			// TODO optimize to unload only those line which actually must be
			// unloaded.
			if (hasErrors()) {
				sendErrors(); // error found no moving to other row.
				return;
			}
		}

		IContext ctx = getContext();
		selectionChange(getSelectedRowIndex(), false);
		ctx.add(id4FirstRow(), offset, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		List dataList = getModel().navigate(offset, getRows());

		// check size of list -> if 0 return
		if (!isReload && dataList.size() == 0)
			return;

		// check sanity
		if (!isReload && dataList.size() > ctx.getValueAsInt(id4size())) {
			// just a simple check if the rows have changed without reload
			throw new WGTException("change of underliing data found, please use reload!");
		}
		ctx.add(id4RowsLoaded(), dataList.size(), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		for (int i = 0; i < dataList.size(); i++) {
			Object dataBean = dataList.get(i);
			IRowHandler rowHandler = getRowHandler(dataBean);
			if (rowHandler.getStyle() == null)
				getContext().setAttribute(id4Row(i), "style", "");
			else
				getContext().setAttribute(id4Row(i), "style", rowHandler.getStyle());
			if (rowHandler.getContextMenu() != null)
				rowHandler.getContextMenu().bindTo(id4Row(i));
			else
				ContextMenu.unbind(id4Row(i), getContext());

			moveCompound2Row(i);
			((DefaultCompoundModel)getRowCompound().getModel()).setData(dataBean);
			getRowCompound().load();
			if (columnConfig == null)
				initColumnConfig();
			for (int j = 0; j < columnConfig.getSize(); j++) {
				TableColumn col = getColumn(j);
				if (col.getIsDisplayed()) {
					col.getRenderer().load(id4TD(i, j), dataBean, col, ctx, i, j);
				}
			}
		}
		// we may need to delete all table cells, which are not shown
		for (int i = dataList.size(); i < rows; i++) {
			moveCompound2Row(i);
			((DefaultCompoundModel)getRowCompound().getModel()).setData(null);
			getRowCompound().load();
			ContextMenu.unbind(id4Row(i), getContext());
			for (int j = 0; j < columnConfig.getSize(); j++) {
				// rowHandler.getContextMenu().bindTo( id4Row(i) );
				TableColumn col = getColumn(j);
				if (col.getIsDisplayed())
					col.getRenderer().clear(id4TD(i, j), col, ctx, i, j);
			}
		}
		// since we have changed the visible data selection, we may need to move the highlight
		int lead = ctx.getValueAsInt(id4lead());
		selectionChange(lead, false);
		ctx.add(getId(), offset, IContext.TYPE_SCROLL_RELOAD, IContext.STATUS_COMMAND);
	}

	protected IRowHandler getRowHandler(Object dataBean) {
		// look for fitting rowHandler
		for (Iterator iter = rowHandler.iterator(); iter.hasNext();) {
			IRowHandler rowHandler = (IRowHandler)iter.next();
			if (rowHandler.canHandle(dataBean)) {
				return rowHandler;
			}
		}
		// if there are no row handlers the default row handler provides the context menu
		if (rowHandler.isEmpty()) {
			return new IRowHandler() {
				public boolean canHandle(Object o) {
					return true;
				}

				public String getMappedProperty(String property) {
					return property;
				}

				public String getStyle() {
					return null;
				}

				public IContextMenu getContextMenu() {
					return defaultContextMenu;
				}
			};
		}
		// if there are row handlers and there is no fitting row handler the default row handler has no context menu
		return new IRowHandler() {
			public boolean canHandle(Object o) {
				return true;
			}

			public String getMappedProperty(String property) {
				return property;
			}

			public String getStyle() {
				return null;
			}

			public IContextMenu getContextMenu() {
				return null;
			}
		};
	}

	/**
	 * move the embedded compound such that it operates on the row given as parameter. If you want to call any method on the
	 * compound directly, make sure, that you have called this method with the appropriate index as row.
	 * 
	 * To work with all rows, follow this code example: <code>
	 * for (int i=0;i<table.getRowsLoaded();i++) {
	 * 	table.moveCompound2Row(i);
	 * 	table.getCompound().save(); // example
	 * }
	 * </code>
	 */
	public void moveCompound2Row(int row) {
		// getCompound().setId(id4Row(row)+DOT_COMPOUND); // all formcontrol ids change automatically.
		if (tableColumns != null) {
			if (columnConfig == null)
				initColumnConfig();
			for (int i = 0; i < columnConfig.getSize(); i++) {
				TableColumn col = getColumn(i);
				IColumnRenderer renderer = col.getRenderer();
				if (renderer instanceof ChildColumnRenderer) {
					ChildColumnRenderer cr = (ChildColumnRenderer)renderer;
					cr.move2Row(row);
				}
			}
		}
	}

	/**
	 * number of rows actually on the screen may be less than the visible size since the model may not have enough data.
	 * 
	 * @return number of active rows on screen
	 */
	public int getRowsLoaded() {
		return getContext().getValueAsInt(id4RowsLoaded());
	}

	/**
	 * unload, writes all changes from the row visible on the screen back to the Model. Therefore it need to call navigate on the
	 * model to actually get the model Objects to call the setters.
	 * 
	 * This is being called whenever a load of new lines is triggered (for every scrolling).
	 * 
	 * You will need to call this manually from your save button to get the last few lines unloaded.
	 * 
	 * @deprecated use save2Bag instead
	 */
	@Deprecated
	public void internalUnload() {
		save2Bag();
	}

	protected void sendErrors() {
		if (errorList == null || errorList.isEmpty()) {
			errorList = null;
			return;
		}

		StringBuffer msg = new StringBuffer(30);
		for (int i = 0; i < errorList.size(); i++) {
			String error = errorList.get(i);
			msg.append(error + "<br>");
		}
		getPage().sendError(msg.toString());
	}

	public void addError(String msg) {
		if (errorList == null)
			errorList = new ArrayList<String>(3);
		errorList.add(msg);
	}

	/**
     *
     */
	protected void setErrors(boolean err) {
		getContext().modifyValue(
				new ContextElement(getId() + ".hasErrors", Boolean.toString(err), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY));
	}

	/**
	 * if the table is editable, this function will return true if there have been errors detected, during automatic conversion
	 * into the appropriate types.
	 */
	public boolean hasErrors() {
		return getContext().getValueAsBool(getId() + ".hasErrors");
	}

	/**
	 * converting one error message into a string to be readable by the user.
	 * 
	 * @param msg
	 * @param prop
	 * @param row
	 * @return
	 */
	protected String convertErrorLine(String msg, String prop, int row) {
		String newMsg = TextService.getString("general.line@Line", " " + (row + 1));
		if (prop != null) {
			FormControl fc = getRowCompound().findElementByProperty(prop);
			if (fc.getDescribingLabel() != null)
				newMsg += " " + fc.getDescribingLabel().getText();
		}
		newMsg += " " + TextService.getString(msg);
		return newMsg;
	}

	public Compound getRowCompound() {
		if (rowCompound == null) {
			rowCompound = new Compound();
			rowCompound.setParent(this);
		}
		return rowCompound;
	}

	/**
	 * keep the index of the table in a valid range
	 * 
	 * @return nearest value of a valid firstRow
	 */
	public int boxIndex(int f) {
		int size = getContext().getValueAsInt(id4size());
		return Math.max(0, Math.min(f, size - getRows()));

	}

	/**
	 * sort the data by column i
	 */
	public void sort(int i) {
		if (i < 0 || (columnConfig != null && i > columnConfig.getSize())) {
			// columnConfig can be null if the component is not drawn yet
			return; // very funny...
		}

		if (!getColumn(i).isSortable())
			return; // not possible!

		String oldSortedCol = getContext().getValue(getId() + SORTBY_COLUMN);
		boolean sortASC = true;
		if (oldSortedCol == null) {
			getContext().addClass(id4column(i) + "_head", "wgtTableColumnHeaderCellSorted");
		}
		else if (!oldSortedCol.equals(getColumn(i).getProperty())) {
			getContext().addClass(id4column(i) + "_head", "wgtTableColumnHeaderCellSorted");
			if (oldSortedCol != null) {
				getContext().add(id4column(getColumnId(oldSortedCol)) + "_sortImg.src", "./images/wgt/table/sort.gif", IContext.TYPE_ATT,
						IContext.STATUS_NOT_EDITABLE);
				getContext().removeClass(id4column(getColumnId(oldSortedCol)) + "_head", "wgtTableColumnHeaderCellSorted");
			}
		}
		else
			sortASC = !getContext().getValueAsBool(getId() + SORT_ASC);

		if (sortASC)
			getContext().add(id4column(i) + "_sortImg.src", "./images/wgt/table/sort_up.gif", IContext.TYPE_ATT,
					IContext.STATUS_NOT_EDITABLE);
		else
			getContext().add(id4column(i) + "_sortImg.src", "./images/wgt/table/sort_down.gif", IContext.TYPE_ATT,
					IContext.STATUS_NOT_EDITABLE);

		getContext().add(getId() + SORTBY_COLUMN, getColumn(i).getProperty(), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		getContext().add(getId() + SORT_ASC, Boolean.toString(sortASC), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		reload(0);
	}

	/**
	 * sort the data by column i with specified direction
	 */
	public void sort(int i, boolean ascending) {
		if (i < 0 || (columnConfig != null && i > columnConfig.getSize())) {
			// columnConfig can be null if the component is not drawn yet
			return; // very funny...
		}

		if (!getColumn(i).isSortable())
			return; // not possible!

		String oldSortedCol = getContext().getValue(getId() + SORTBY_COLUMN);
		boolean sortASC = ascending;
		if (oldSortedCol == null) {
			getContext().addClass(id4column(i) + "_head", "wgtTableColumnHeaderCellSorted");
		}
		else if (!oldSortedCol.equals(getColumn(i).getProperty())) {
			getContext().addClass(id4column(i) + "_head", "wgtTableColumnHeaderCellSorted");
			if (oldSortedCol != null) {
				getContext().add(id4column(getColumnId(oldSortedCol)) + "_sortImg.src", "./images/wgt/table/sort.gif", IContext.TYPE_ATT,
						IContext.STATUS_NOT_EDITABLE);
				getContext().removeClass(id4column(getColumnId(oldSortedCol)) + "_head", "wgtTableColumnHeaderCellSorted");
			}
		}
		else
		// sortASC = !getContext().getValueAsBool(getId() + SORT_ASC);

		if (sortASC)
			getContext().add(id4column(i) + "_sortImg.src", "./images/wgt/table/sort_up.gif", IContext.TYPE_ATT,
					IContext.STATUS_NOT_EDITABLE);
		else
			getContext().add(id4column(i) + "_sortImg.src", "./images/wgt/table/sort_down.gif", IContext.TYPE_ATT,
					IContext.STATUS_NOT_EDITABLE);

		getContext().add(getId() + SORTBY_COLUMN, getColumn(i).getProperty(), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		getContext().add(getId() + SORT_ASC, Boolean.toString(sortASC), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		reload(0);
	}

	public void sort(String propertyOfColumn) {
		if (propertyOfColumn != null) {
			int idOfcolumn = getColumnId(propertyOfColumn);
			if (idOfcolumn != -1)
				sort(idOfcolumn);
		}
	}

	/**
	 * this changes the selection A value of -1 deselects the highlight in the table.
	 * 
	 * @param newSelection in number of application rows
	 */
	public void selectionChange(int newSelection, boolean callListener) {
		if (callListener) {
			if (getListener() instanceof AbstractTableListener) {
				((AbstractTableListener)getListener()).onRowSelection(this, newSelection);
			}
			else
				Logger.getLogger(this.getClass()).warn("table.callListenerSelectRow() called for non AbstractTableListener");
		}
		// we need to change the index to tabl relative.
		int firstRow = getContext().getValueAsInt(id4FirstRow());
		int relRow = newSelection - firstRow;
		// save new selection position
		getContext().add(id4lead(), newSelection, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		String selDisplay = (newSelection == -1) ? "" : String.valueOf(newSelection + 1);
		getContext().add(id4RowInput(), selDisplay, IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
		if (relRow < 0 || relRow > rows) {
			// delete highlight
			getContext().add(id4highlite(), "null", IContext.TYPE_LIGHT, IContext.STATUS_IDEM);
			fireServerEvent(new ServerEvent(this, ServerEvent.EVENT_TABLE_SELHIDDEN));
		}
		else {
			// set the highlight
			getContext().add(id4highlite(), id4Row(relRow), IContext.TYPE_LIGHT, IContext.STATUS_IDEM);
			fireServerEvent(new ServerEvent(this, ServerEvent.EVENT_TABLE_SELVISBLE));
		}
	}

	/**
	 * @deprecated use selectionChange(int newSelection, boolean callListener )
	 * 
	 *             this changes the selection A value of -1 deselects the highlight in the table.
	 * 
	 * @param newSelection in number of application rows
	 */
	public void selectionChange(int newSelection) {
		selectionChange(newSelection, false);
	}

	/**
	 * @return the id for lead selection int number of row in the model
	 */
	public String id4lead() {
		return getId() + ".lead";
	}

	// conveis the id of the row where the highlite should be
	// or null for deleting
	public String id4highlite() {
		return getId() + ".highlite";
	}

	public String id4column(int i) {
		return getId() + ".c" + i;
	}

	/**
	 * @return
	 */
	public String id4size() {
		return getId() + ".size";
	}

	/**
	 * The input element under the table, where you can enter the row you want to jump to.
	 * 
	 * @return
	 */
	public String id4RowInput() {
		return getId() + ".firstRowInput";
	}

	/**
	 * @return
	 */
	public String id4FirstRow() {
		return getId() + ".firstRow";
	}

	/**
	 * id for the number of actual rows loaded on the screen This is usually the same as getRows(), however it can be less.
	 */
	public String id4RowsLoaded() {
		return getId() + ".rowsLoaded";
	}

	/**
	 * @param i
	 * @return
	 */
	public String id4iFilter(int i) {
		return getId() + ".if" + getColumn(i).getProperty() + ".value";
	}

	/**
	 * @param i
	 * @return
	 */
	public String id4DDFilter(int i) {
		return ((TableFilter)tableFilters.get(i)).getId();
	}

	/**
	 * returns the id for a datacell inside the table
	 * 
	 * @return
	 */
	public String id4TD(int i, int j) {
		return getId() + ".c" + j + ".r" + i;
	}

	public String id4Row(int i) {
		return getId() + ".r" + i;
	}

	public String id4RowPrefix() {
		return getId() + ".r";
	}

	public String id4Header() {
		return getId() + ".header";
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		if (columnConfig != null)
			columnConfig.setRowCount(rows);
		this.rows = rows;
	}

	public void changeRowCount(int newRows) {
		setRows(rows);
	}

	public ITableModel getModel() {
		// lazy initialization, can be pushed into table
		if (model == null) {
			model = new DefaultTableModel();
		}
		return model;
	}

	/**
	 * the listener for tables is either defined in the jsp as class name or directly set as instance of ITableListener with
	 * setListener, or left blank. In which case an AbstractTableListener will be instanciated, which does not react of row change
	 * events.
	 * 
	 * @return
	 */
	public ITableListener getListener() {
		if (listener == null) {
			// if (StringUtils.isBlank(listenerClass)) {
			// used noop TableListener
			listener = new AbstractTableListener() {
				@Override
				public void onRowSelection(ITable table, int row) {
					// noop
				}
			};
			// } else {
			// listener = (ITableListener) instanceFrom(listenerClass);
			// }
		}
		return listener;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * class to interact filters with the table
	 * 
	 * @author arno
	 */
	protected class TableFilterListener implements IActionListener {
		public void onAction(ClientEvent event) {
			ITableListener listener = getListener();
			listener.onDDFilter(new ClientEvent(Table.this, getId(), event.getType(), event.getChgContext()));
		}
	}

	/**
	 * on init it just reloads whole table and goes to first row
	 */
	@Override
	protected void init() {
		if (draggable || droppable || DISPLAY_MODE_SCROLLBAR.equals(displayMode)) {
			getPage().addWgtJS(JSResourceProcessor.DRAG_DROP_JS);
		}
		getPage().addControllerJS(JSResourceProcessor.TABLE_JS);
	}

	public void initFilterModel() {
		// If we have PushFilterModel and AssociationFilterModel then they can work together
		if (!(getModel() instanceof DefaultTableModel))
			return;
		List datalist = ((DefaultTableModel)getModel()).getTableData();
		for (int i = 0; i < tableFilters.size(); i++) {
			TableFilter filter = (TableFilter)tableFilters.get(i);
			if (filter.getModel() instanceof AssociationFilterModel) {
				AssociationFilterModel fmod = (AssociationFilterModel)filter.getModel();
				fmod.fill(datalist);
			}
			filter.loadList();
		}
	}

	/**
	 * convenience to get the information which is the first row shown
	 * 
	 * @deprecated use getOffset()
	 * @return the offset
	 */
	@Deprecated
	public int getFirstRow() {
		return getContext().getValueAsInt(id4FirstRow());
	}

	/**
	 * Method returns the number of the row which is currently show at the top of the table returns the offset of the tables scope
	 * 
	 * @return the offset
	 */
	public int getOffset() {
		return getContext().getValueAsInt(id4FirstRow());
	}

	/**
	 * convenience to get the information which is the lead selection of the table. This is the index in the model's list
	 * (getFilteredList, if you are using the defaultTableModel).
	 * 
	 * @return -1 if no selection
	 */
	public int getSelectedRowIndex() {
		return getContext().getValueAsInt(id4lead());
	}

	public void setModel(ITableModel model) {
		this.model = model;
	}

	public String getTitle() {
		return getContext().getValue(id4Header());
	}

	public void setTitle(String title) {
		getContext().add(id4Header(), title, IContext.TYPE_HTML, IContext.STATUS_IDEM);
		boolean vis = StringUtils.isNotEmpty(title);
		getContext().setVisible(id4Header(), vis);
		if (editable)
			getContext().setVisible(id4Header() + "cf", vis);
	}

	public void setTitleKey(String titleKey) {
		setTitle(TextService.getString(titleKey));
	}

	/**
	 * convenience method, it assumes, that you are using the (standard) DefaultTableModel and DataBags.
	 * 
	 * @return The row which has the actual selection as IDatabag or null if nothing selected
	 */
	public IDataBag getSelectedRow() {
		return getRow(getSelectedRowIndex());
	}

	/**
	 * convenience method, it assumes, that you are using the (standard) DefaultTableModel and DataBags.
	 * 
	 * @return The row which has the actual selection as IDatabag or null if nothing selected
	 */
	public IDataBag getRow(int row) {
		if (row < 0)
			return null;
		List fList = ((DefaultTableModel)getModel()).getFilteredList();
		if (fList == null || row >= fList.size())
			return null;
		return (IDataBag)fList.get(row);
	}

	/**
	 * convenience method. It is assumed, that you are using DefaultTableModel. This removes a row and reloads the table
	 * (including reordering and sorting).
	 * 
	 * @param mortem the object to be removed.
	 * @return true if the entry was found and removed from the list.
	 */
	public boolean removeAndReload(Object mortem) {
		boolean dead = ((DefaultTableModel)getModel()).getTableData().remove(mortem);
		if (dead)
			reload(getFirstRow());
		return dead;
	}

	protected void add(BaseControl ch) {
		super.add(ch);
	}

	/**
	 * convenience method. It is assumed, that you are using DefaultTableModel. This Method adds a new entry after sorting and
	 * applying filters to the table model so it is displayed at first position.
	 * 
	 */
	public void addAndReload(Object newObject) {
		IContext context = getContext();

		clearAllFilter();

		int rownum = runFilter();

		DefaultTableModel tableModel = (DefaultTableModel)getModel();
		tableModel.getTableData().add(newObject);
		tableModel.getFilteredList().add(0, newObject);

		context.add(id4size(), rownum + 1, IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
		// remove the highlight as we cannot identify it.
		getContext().add(id4lead(), -1, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
		initFilterModel(); // each dataload may bear some changes in the model result in filter changes
		load(0, true);

		selectionChange(0, false);
	}

	public DefaultTableModel getDefaultModel() {
		return (DefaultTableModel)getModel();
	}

	/**
	 * @deprecated use selectionChange(int newSelection, boolean callListener )
	 * 
	 *             convenience method. It assumes that you are using an AbstractTableListener. This lets you call the listener for
	 *             the table an execute the onRowSelection-code as if there were an user click on the row. This method also takes
	 *             care on highlithening the new selection...
	 * 
	 * @param row newly selected row number.
	 */
	public void callListenerSelectRow(int row) {
		selectionChange(row, true);
	}

	/**
	 * convenience method if you use the defaultTableModel calls undo on all DataBags
	 */
	public void undo() {
		for (Iterator it = (getDefaultModel()).getTableData().iterator(); it.hasNext();) {
			IDataBag bag = (IDataBag)it.next();
			bag.undo();
		}
		reload(getSelectedRowIndex());
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.controls.BaseControl#setParent(com.endress.infoserve.wgt.controls.BaseControl)
	 */
	@Override
	public void setParent(BaseControl father) {
		super.setParent(father);
		// may be null, then it must be set afterwards
		if (surroundingCompound() != null) {
			if (father != null) {
				surroundingCompound().addElement(this);
			}
			else {
				((Compound)surroundingCompound()).removeElement(this);
			}
		}
	}

	/**
	 * change the readOnly mode of the entire table, resulting in changing the readOnly mode of all table rows with form elements
	 */
	public void changeMode(int mode) {
		// switch the mode from one line after the other.
		for (int i = 0; i < getRows(); i++) {
			moveCompound2Row(i);
			getRowCompound().changeElementMode(mode);
		}
		for (ITableColumn tc : tableColumns) {
			IColumnRenderer renderer = ((TableColumn)tc).getRenderer();
			if (renderer instanceof CheckboxColumnRenderer) {
				String id = tc.getId() + TableColumn.SELECT_ALL;
				if (mode == Compound.MODE_READONLY) {
					getContext().sendJavaScript(id, JSUtil.jQuery(id) + ".attr('disabled', true);");
				}
				else {
					getContext().sendJavaScript(id, JSUtil.jQuery(id) + ".attr('disabled', false);");
				}
			}
		}
	}

	public void loadFrom(Object data) {
		// ignore parameter data, since we have our own
		// model
		load(); // better reload() ?
	}

	/**
	 * saves the table to its model. the parameter is ignored, you can pass null.
	 * 
	 * @deprecated use save or save2Bag
	 */
	@Deprecated
	public void saveTo(Object dataObject) {
		clearErrors();

		save2Bag();
		// addErrors of this Table to surroundingCompound
		if (errorList != null && surroundingCompound() != null) {
			for (Iterator iterator = errorList.iterator(); iterator.hasNext();) {
				String errorText = (String)iterator.next();
				surroundingCompound().addError(errorText);
			}
		}
	}

	/**
	 * this saves the information entered in the Table to the DataBag of the underlying DefaultTableMode. If you are not using the
	 * DefaultTableModel with DataBag, call internalUnload to transport the data into your model. The savXXX methods are only if
	 * you are using the DefaultTableModel and DataBags.
	 * 
	 * Keep in mind that rows may get transported into the DataBag if the table is being scrolled.
	 * 
	 * Don't forget to ask the surrounding compound for any errors.
	 */
	public void save2Bag() {
		// is this table editable at all?
		boolean edi = false;
		for (int i = 0; i < columnConfig.getSize(); i++) {
			TableColumn col = getColumn(i);
			if (col.getRenderer() instanceof ChildColumnRenderer) {
				edi = true;
				break;
			}
		}
		if (!edi)
			return;

		IContext ctx = getContext();
		setErrors(false);
		clearErrors();
		int firstRow = getFirstRow();
		if (firstRow == Integer.MIN_VALUE)
			return; // nothing loaded yet.
		int loadedRows = ctx.getValueAsInt(id4RowsLoaded());
		// load the objects we want to save our changes into...
		// TODO optimize no need to load all rows, unload only needed...
		List objects = getModel().navigate(firstRow, loadedRows);
		if (loadedRows != objects.size()) {
			// error ?!
			loadedRows = objects.size();
		}
		ICompound sc = surroundingCompound();
		// use flightweight pattern for the compound to unload.
		for (int row = 0; row < loadedRows; row++) {
			moveCompound2Row(row);
			((DefaultCompoundModel)getRowCompound().getModel()).setData(objects.get(row));
			getRowCompound().save();
			if (getRowCompound().hasErrors()) {
				for (Iterator it = getRowCompound().getErrorList().iterator(); it.hasNext();) {
					String[] err = (String[])it.next();
					String errMsg = convertErrorLine(err[0], err[1], row);
					addError(errMsg);
				}
				getRowCompound().clearErrors();
				setErrors(true);
			}
		}
		// add the error list to the surrounding compound and add
		// some info o which lines it happened.
	}

	/**
	 * this transports the data from the DataBag into the delegates. It is actually an operation on the DefaultTableModel. If you
	 * are not using DefaultTableModel with DataBags, this method is not for you.
	 * 
	 * It just runs through the list of rows and calls everywhere bag.save2Delegate().
	 */
	public void save2Delegate() {
		for (Iterator it = (getDefaultModel()).getTableData().iterator(); it.hasNext();) {
			IDataBag bag = (IDataBag)it.next();
			bag.save();
		}
	}

	/**
	 * this calls save2Bag and save2delegate to transport the edited table right into the DefaultModel. However if there were
	 * error from the transport into the Bag, the transport further into the delegates.
	 */
	public void save() {
		save2Bag();
		if (hasErrors()) {
			sendErrors();
		}
		else {
			save2Delegate();
		}
	}

	/**
	 * retrieve the surrounding compound
	 */
	public ICompound surroundingCompound() {
		if (compound == null) {
			BaseControl sb = this;
			while (!(sb instanceof ICompound)) {
				sb = sb.getParent();
				if (sb == null)
					break;
			}
			compound = (Compound)sb;
		}
		return compound;
	}

	public void setRowCssClass(String rowCssClass) {
		this.rowCssClass = rowCssClass;
	}

	public void setRowCssClassAlternate(String rowCssClassAlternate) {
		this.rowCssClassAlternate = rowCssClassAlternate;
	}

	public void clearAllFilter() {
		clearInlineFilter();
		clearDropDownFilter();
	}

	public void clearInlineFilter() {
		for (int j = 0; j < columnConfig.getSize(); j++) {
			TableColumn col = getColumn(j);
			if (col.isFilter())
				getContext().add(id4iFilter(j), "", IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
		}
	}

	public void clearDropDownFilter() {
		for (int i = 0; i < tableFilters.size(); i++) {
			TableFilter filter = (TableFilter)tableFilters.get(i);
			String val = filter.getValue();
			if (val != null && !val.equals(TableFilter.EMPTY)) {
				filter.setValue("");
			}
		}
	}

	public void clearErrors() {
		errorList = new ArrayList<String>(0); // indicates to delete all errors
	}

	/**
	 * @return the fireRowSelectEvent
	 */
	public boolean isFireRowSelectEvent() {
		return fireRowSelectEvent;
	}

	/**
	 * @param fireRowSelectEvent the fireRowSelectEvent to set
	 */
	public void setFireRowSelectEvent(boolean fireRowSelectEvent) {
		this.fireRowSelectEvent = fireRowSelectEvent;
	}

	public void setListener(ITableListener listener) {
		this.listener = listener;
	}

	public void fireServerEvent(ServerEvent event) {
		serverListener.fireServerEvent(event);
	}

	public void registerListener(int eventtype, IServerEventListener liz) {
		serverListener.registerListener(eventtype, liz);
	}

	public void removeListener(int eventtype, IServerEventListener liz) {
		serverListener.removeListener(eventtype, liz);
	}

	/**
	 * adds a column to the current list
	 * 
	 * @param column
	 */
	public void addColumn(ITableColumn column) {
		tableColumns.add(column);
		add(column);
	}

	/**
	 * add a filter dynamically.
	 */
	public void addFilter(ITableFilter filter) {
		if (tableFilters.size() == 0)
			getPage().addWgtJS(JSResourceProcessor.SELECT_JS);
		tableFilters.add(filter);
		// set the hierarchy
		((BaseControl)filter).setParent(this);
	}

	@Override
	public void addContextMenu(final IContextMenu contextMenu) {
		defaultContextMenu = contextMenu;
		super.addContextMenu(contextMenu);
	}

	public TableColumn getCurrentColumn() {
		return (TableColumn)tableColumns.get(tableColumns.size() - 1);
	}

	public void loadColumnConfig(String config) {
		int oldRowCount;
		if (columnConfig != null)
			oldRowCount = columnConfig.getRowCount();
		else
			oldRowCount = getRows();

		columnConfig = new ColumnConfig(config);
		if (tableColumns != null && tableColumns.size() > 0) {
			// all columns
			ArrayList<ITableColumn> allCols = new ArrayList<ITableColumn>();

			// displayed columns
			ArrayList<ITableColumn> displayed = new ArrayList<ITableColumn>();

			// hidden columns
			ArrayList<ITableColumn> hidden = new ArrayList<ITableColumn>();

			// errors in config
			ArrayList<Integer> invalidCols = new ArrayList<Integer>();

			// iterate over config
			for (int i = 0; i < columnConfig.getSize(); i++) {
				boolean found = false;
				for (int j = 0; j < tableColumns.size(); j++) {
					TableColumn col = (TableColumn)tableColumns.get(j);
					if (columnConfig.getSetting(i).getProperty().equals(col.getProperty())) {
						// add to displayed columns
						displayed.add(col);
						found = true;
						break;
					}
				}
				if (!found)
					invalidCols.add(0, new Integer(i)); // highest first
			}
			int mandatoryCounter = 0;
			// get hidden
			for (int j = 0; j < tableColumns.size(); j++) {
				TableColumn col = (TableColumn)tableColumns.get(j);
				if (!displayed.contains(col)) {
					if (col.isMandatory()) {
						// add to visible columns
						displayed.add(mandatoryCounter, col);
						columnConfig.addSetting(mandatoryCounter, col.getProperty(), col.getWidth());
						mandatoryCounter++;
					}
					else {
						// add to hidden columns
						hidden.add(col);
					}
				}
			}

			// remove invalid cols
			for (Iterator iter = invalidCols.iterator(); iter.hasNext();) {
				Integer i = (Integer)iter.next();
				columnConfig.remove(i.intValue());
			}

			// show displayed
			for (int i = 0; i < displayed.size(); i++) {
				TableColumn col = (TableColumn)displayed.get(i);
				col.setIsDisplayed(true);
				col.setWidth(columnConfig.getSetting(i).getWidth());
				// add displayed formcontrols to rowcompound
				if (col.getRenderer() instanceof ChildColumnRenderer) {
					FormControl[] controls = ((ChildColumnRenderer)col.getRenderer()).getMyControls();
					for (int j = 0; j < controls.length; j++) {
						getRowCompound().removeElement(controls[j]); // remove so we doesn't insert twice
						getRowCompound().addElement(controls[j]);
					}
				}
				// add displayed columns first (sorted by config)
				allCols.add(col);
			}

			// hide hidden
			for (Iterator iter = hidden.iterator(); iter.hasNext();) {
				TableColumn col = (TableColumn)iter.next();
				col.setIsDisplayed(false);
				// remove hidden formcontrols from rowcompound
				if (col.getRenderer() instanceof ChildColumnRenderer) {
					FormControl[] controls = ((ChildColumnRenderer)col.getRenderer()).getMyControls();
					for (int j = 0; j < controls.length; j++) {
						getRowCompound().removeElement(controls[j]); // remove so we doesn't insert twice
					}
				}
				// add hidden columns to the end
				allCols.add(col);
			}

			tableColumns = allCols;
			if (columnConfig.getRowCount() > 0)
				setRows(columnConfig.getRowCount());
			else
				columnConfig.setRowCount(oldRowCount);
		}
	}

	public String getColumnConfig() {
		if (columnConfig == null)
			initColumnConfig();
		return columnConfig.toString();
	}

	protected void initColumnConfig() {
		columnConfig = new ColumnConfig();
		for (int i = 0; i < tableColumns.size(); i++) {
			TableColumn col = (TableColumn)tableColumns.get(i);
			if (!col.getIsDisplayed())
				continue;
			int width = -1;
			columnConfig.addSetting(col.getProperty(), col.getWidth());
		}
		columnConfig.setRowCount(getRows());
	}

	protected TableColumn getColumn(int index) {
		if (columnConfig == null)
			initColumnConfig();
		String property = columnConfig.getSetting(index).getProperty();
		for (int i = 0; i < tableColumns.size(); i++) {
			TableColumn col = (TableColumn)tableColumns.get(i);
			if (col.getProperty().equals(property))
				return col;
		}
		return null;
	}

	int getColumnId(String property) {
		if (columnConfig == null)
			initColumnConfig();
		for (int i = 0; i < columnConfig.getSize(); i++) {
			if (columnConfig.getSetting(i).getProperty().equals(property))
				return i;
		}
		return -1;
	}

	public ITableColumn addCheckboxColumn(String checkboxProperty, String titelKey) {
		ITableColumn tc = addCheckboxColumn(checkboxProperty, titelKey, true);
		tc.setMandatory(true); // AS: why is that here?
		tc.setExporatble(false);
		return tc;

	}

	public ITableColumn addCheckboxColumn(String checkboxProperty, String titelKey, boolean chRenderer) {
		if (titelKey == null) {
			titelKey = "select.all@all";
		}
		ITableColumn tc = WebGuiFactory.getInstance().createTableColumn(this, titelKey, checkboxProperty, false);
		tc.setWidth("20px");
		tc.setSortable(false);
		tc.setExporatble(false);
		if (chRenderer)
			tc.setRenderer(new CheckboxColumnRenderer());

		return tc;
	}

	public String createNewColumnId() {
		int tableWidth = getColumns().size();
		return id4column(tableWidth);
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setDraggable(String displayProperty) {
		this.draggable = true;
		this.draggableProperty = displayProperty;
		if (getPage() != null)
			getPage().addWgtJS(JSResourceProcessor.DRAG_DROP_JS);
	}

	public void setDroppable() {
		this.droppable = true;
		if (getPage() != null)
			getPage().addWgtJS(JSResourceProcessor.DRAG_DROP_JS);
	}

	protected void initDroppable() {
		if (droppable)
			getContext().sendJavaScript(getId(), "initTableDroppable('" + getId() + "')");
	}

	protected void initDraggable() {
		if (draggable)
			getContext().sendJavaScript(getId(), "initTableDraggable('" + getId() + "')");
	}

	public String getDraggableProperty() {
		return draggableProperty;
	}

	@Override
	public void processSetVisible(boolean visible) {
		super.processSetVisible(visible);
		getContext().add(getId(), getFirstRow(), IContext.TYPE_SCROLL_RELOAD, IContext.STATUS_COMMAND);
	}

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
		if (getPage() != null && DISPLAY_MODE_SCROLLBAR.equals(displayMode))
			getPage().addWgtJS(JSResourceProcessor.DRAG_DROP_JS);
	}

	public void addRowHandler(IRowHandler rowHandler) {
		this.rowHandler.add(rowHandler);
	}

	protected List<BaseControl> getAdditionalTitleBaseConrols() {
		return Collections.EMPTY_LIST;
	}

	public List<IButton> getButtons() {
		return buttons;
	}

	/**
	 * @param button
	 */
	public void addButton(IButton button) {
		super.add(button);
		getButtons().add(button);

	}

	public void addCellControl(IFormControl formControl) {
		add(formControl);
	}

	public TableExportOptions getExportOptions() {
		return exportOptions;
	}

	public void addExportButton(boolean pdf, boolean excel) {
		TableExportOptions exportOptions = new TableExportOptions();
		exportOptions.setExportAsExcel(excel);
		exportOptions.setExcelButtonTooltip(TextService.getString("label.button.export.excel.tooltip@Excel"));
		exportOptions.setExportAsPDF(pdf);
		exportOptions.setPdfButtonTooltip(TextService.getString("label.button.export.pdf.tooltip@PDF"));
		setExportOptions(exportOptions);
	}

	public void setExportOptions(TableExportOptions exportOptions) {
		this.exportOptions = exportOptions;

		if (exportOptions.isExportAsExcel()) {
			StandardButton exportButton = new StandardButton(getId() + "." + TABLE_EXCEL_BUTTON_ID);
			exportButton.setSrc("images/wgt/icons/excel.gif");

			if (StringUtils.isNotEmpty(exportOptions.getExcelButtonTooltip())) {
				exportButton.setTooltip(exportOptions.getExcelButtonTooltip());
			}
			else {
				// show tooltip by default
				exportButton.setTooltipKey(TextService.getString("label.button.export.excel.tooltip@Excel"));
			}

			if (StringUtils.isNotEmpty(exportOptions.getExcelButtonLabelText())) {
				exportButton.setLabelKey(exportOptions.getExcelButtonLabelText());
			}

			if (exportOptions.getExcelButtonListener() == null) {
				exportButton.setActionListener(new ExportButtonListener(this, ITableExport.EXPORT_TYPE_EXCEL));
			}
			else {
				exportButton.setActionListener(exportOptions.getExcelButtonListener());
			}

			if (exportOptions.isExportAsPDF())
				exportButton.setAlignment(StandardButton.POSITION_LEFT);

			this.addButton(exportButton);
		}

		if (exportOptions.isExportAsPDF()) {
			StandardButton exportButton = new StandardButton(getId() + "." + TABLE_PDF_BUTTON_ID);
			exportButton.setSrc("images/wgt/icons/pdf.gif");

			if (StringUtils.isNotEmpty(exportOptions.getPdfButtonTooltip())) {
				exportButton.setTooltip(exportOptions.getPdfButtonTooltip());
			}
			else {
				// show tooltip by default
				exportButton.setTooltipKey(TextService.getString("label.button.export.pdf.tooltip@PDF"));
			}

			if (StringUtils.isNotEmpty(exportOptions.getPdfButtonLabelText())) {
				exportButton.setLabelKey(exportOptions.getPdfButtonLabelText());
			}

			if (exportOptions.getPdfButtonListener() == null) {
				exportButton.setActionListener(new ExportButtonListener(this, ITableExport.EXPORT_TYPE_PDF));
			}
			else {
				exportButton.setActionListener(exportOptions.getPdfButtonListener());
			}

			if (exportOptions.isExportAsExcel())
				exportButton.setAlignment(StandardButton.POSITION_RIGHT);

			this.addButton(exportButton);
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#clearError()
	 */
	public void clearError() {
		// not supported for table at the moment
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement#showError()
	 */
	public void showError() {
		// not supported for table at the moment
	}

	/**
	 * @return the defaultRenderer
	 */
	public IColumnRenderer getDefaultRenderer() {
		return defaultRenderer;
	}

	/**
	 * @param defaultRenderer the defaultRenderer to set
	 */
	public void setDefaultRenderer(IColumnRenderer addDefaultRenderer) {
		this.defaultRenderer = addDefaultRenderer;
	}

	@Override
	public void redraw() {
		loadColumnConfig(getColumnConfig());
		super.redraw();
	}

	public TD getButtonHeaderCell() {
		return buttonHeaderCell;
	}
}
