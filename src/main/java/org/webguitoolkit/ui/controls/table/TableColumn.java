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
package org.webguitoolkit.ui.controls.table;

import java.io.PrintWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.NOBR;
import org.apache.ecs.html.TD;
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.table.renderer.AbstractColumRenderer;
import org.webguitoolkit.ui.controls.table.renderer.CheckboxColumnRenderer;
import org.webguitoolkit.ui.controls.table.renderer.CompositeColumnRenderer;
import org.webguitoolkit.ui.controls.table.renderer.PlainTextColumnRenderer;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverter;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.NumberConverterPrecise;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.style.IStyleChangeListener;

/**
 * <pre>
 * A table consists of several TableColumn that define the content of the cells that belong to the column.
 * </pre>
 */
public class TableColumn extends BaseControl implements ITableColumn, IComposite {

	public static final String SELECT_ALL = ".selAll";
 
	protected String align;
	protected boolean filter;
	protected boolean mandatory = false;
	protected boolean sortable = true;
	protected boolean isDisplayed = true;
	protected String property;
	protected String title;
	protected String type;
	protected String width;
	protected IColumnRenderer renderer;
	protected boolean showSelectAllCheckboxInHeaderForCheckboxes = true;
	protected boolean showTitleInHeaderForCheckboxes = false;
	boolean exporatble = true;
	String tooltip;


	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public TableColumn() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public TableColumn(String id) {
		super(id);
	}

	public String getAlign() {
		return align;
	}

	public String getProperty() {
		return property;
	}

	/**
	 * translated title attribute
	 * 
	 * @return
	 */
	public String textForTitle() {
		return TextService.getString(title);
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getWidth() {
		return width;
	}

	public boolean isFilter() {
		return filter;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public void setProperty(String source) {
		this.property = source;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	protected void endHTML(PrintWriter out) {
	}

	/**
	 * no need for initialization - table does it.
	 */
	protected void init() {
	}

	/**
	 * determines the default renderer or load the one explicitly specified. Default renderer is TextColumnRenderer, but if the
	 * column has children, it uses ChildColumnRenderer.
	 * 
	 * @return
	 */
	public IColumnRenderer getRenderer() {
		if (renderer != null) {
			// nothing to do;
		}
		else if (getTable().getDefaultRenderer() != null) {
			renderer = getTable().getDefaultRenderer();
		}
		else {
			// if (StringUtils.isEmpty(getRendererClass())) {
			if (getChildren() != null && getChildren().size() > 0) {
				// has input fields, maks the table editable
				renderer = new CompositeColumnRenderer();
			}
			else {
				// treat the input as readonly text
				renderer = new PlainTextColumnRenderer();
			}
		}
		return renderer;
	}

	/**
	 * set the renderer for the column that is used to render the the cells in the column and load the values to the cells
	 * 
	 * @param renderer the renderer for the cells of this column
	 */
	public void setRenderer(IColumnRenderer renderer) {
		this.renderer = renderer;
		if (renderer instanceof AbstractColumRenderer)
			((AbstractColumRenderer)renderer).init(this);
	}

	/**
	 * extract data from the bean, use column at col to determine the property.
	 * 
	 * @param data the dataobject
	 * @return the value
	 */
	public String propertyFrom(Object data) {
		String value = PropertyAccessor.retrieveString(data, getMappedProperty(data), getConverter());
		return (value == null) ? "" : value;
	}

	/**
	 * Returns the property that is defined in the tables RowHandler, if there is non return the original property
	 * 
	 * @param data the data object
	 * @return the property to be used defined in the RowHandler of the table
	 */
	public String getMappedProperty(Object data) {
		IRowHandler rowHandler = ((Table)getParent()).getRowHandler(data);
		if (rowHandler != null && rowHandler.getMappedProperty(getProperty()) != null)
			return rowHandler.getMappedProperty(getProperty());
		else
			return getProperty();
	}

	protected IConverter conv;

	public IConverter getConverter() {
		return conv;
	}

	public void setConverter(IConverter con) {
		conv = con;
	}

	public boolean getIsDisplayed() {
		return isDisplayed || mandatory;
	}

	public void setIsDisplayed(boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}

	public void add(BaseControl child) {
		super.add(child);
	}

	public boolean isShowSelectAllCheckboxInHeaderForCheckboxes() {
		return showSelectAllCheckboxInHeaderForCheckboxes;
	}

	public void setShowSelectAllCheckboxInHeaderForCheckboxes(boolean showSelectAllCheckboxInHeaderForCheckboxes) {
		this.showSelectAllCheckboxInHeaderForCheckboxes = showSelectAllCheckboxInHeaderForCheckboxes;
	}

	public boolean isShowTitleInHeaderForCheckboxes() {
		return showTitleInHeaderForCheckboxes;
	}

	public void setShowTitleInHeaderForCheckboxes(boolean showTitleInHeaderForCheckboxes) {
		this.showTitleInHeaderForCheckboxes = showTitleInHeaderForCheckboxes;
	}

	/**
	 * own style changed listener for columns because there is no single column object in the front end
	 */
	protected void registerStyleChangeListener() {
		style.registerStyleChangedListener(new CollStyleChangedListener());
	}

	class CollStyleChangedListener implements IStyleChangeListener {
		public void handle(int eventType, String attribute) {
			Table table = (Table)getParent();
			int columnId = table.getColumnId(getProperty());
			if (columnId != -1) {
				for (int row = 0; row < table.getRows(); row++) {
					getContext().add(table.id4TD(row, columnId) + Context.DOT_STYLE, getStyleAsString(), Context.TYPE_ATT,
							Context.STATUS_EDITABLE);
				}
			}
		}

		public String getListenerId() {
			return getId();
		}
	}

	/**
	 * @param sortASC
	 * @param oldSortedCol
	 * 
	 */
	public TD drawHeader(String colId, String oldSortedCol, boolean sortASC) {
		TD td = new TD();
		td.setID(colId + "_head");
		if (getProperty().equals(oldSortedCol))
			td.setClass("wgtTableColumnHeaderCell wgtTableColumnHeaderCellSorted");
		else
			td.setClass("wgtTableColumnHeaderCell");

		if (StringUtils.isNotEmpty(getWidth()))
			td.setWidth(getWidth());
		if (hasStyle())
			td.setStyle(getStyleAsString());
		if (StringUtils.isNotBlank(getTooltip())){
			td.setTitle(StringEscapeUtils.escapeHtml(getTooltip()));
		}
		if (isSortable())
			td.setOnClick(JSUtil.jsEventParam(getTable().getId(), new String[] { getProperty() })
					+ JSUtil.jsFireEvent(getTable().getId(), Table.EVENT_SORT));
		NOBR nobr = new NOBR();
		td.addElement(nobr);

		if (getRenderer() instanceof CheckboxColumnRenderer && isShowSelectAllCheckboxInHeaderForCheckboxes()) {
			Input checkAll = new Input();
			checkAll.setType("checkbox");
			checkAll.setID(getId() + SELECT_ALL);
			checkAll.setValue("true");
			checkAll.setOnClick("onCheckAll('" + getTable().getId() + "','" + getId() + "');");
			nobr.addElement(checkAll);

			if (isShowTitleInHeaderForCheckboxes()) {
				nobr.addElement(textForTitle());
			}
		}
		else
			nobr.addElement(textForTitle());
		if (isSortable()) {
			IMG sortImage = new IMG();
			sortImage.setID(colId + "_sortImg");
			sortImage.setClass("wgtSortIcon");
			sortImage.setBorder(0);
			if (getProperty().equals(oldSortedCol) && sortASC)
				sortImage.setSrc("./images/wgt/table/sort_up.gif");
			else if (getProperty().equals(oldSortedCol))
				sortImage.setSrc("./images/wgt/table/sort_down.gif");
			else
				sortImage.setSrc("./images/wgt/table/sort.gif");

			nobr.addElement(sortImage);
		}

		return td;
	}

	/**
	 * 
	 */
	public TD drawFilter(String filterId) {
		TD td = new TD();
		td.setClass("wgtTableFilterCell");

		Input input = new Input();
		input.setType("text");
		input.setID(filterId);

		String oldValue = getContext().getValue(filterId);
		if (oldValue == null)
			oldValue = "";
		input.setValue(oldValue);

		if (!isFilter()) {
			input.setClass("wgtInputText");
			input.setDisabled(true);
			input.setStyle("display:none;width:1px;");
		}
		else {
			// it is enabled, the client needs to keep trakc of the changes
			String filContent = getContext().processValue(filterId);
			getContext().add(filterId, filContent, IContext.TYPE_VAL, IContext.STATUS_EDITABLE);
			input.setOnKeyDown("if (event.keyCode==13) {" + JSUtil.jsFireEvent(getTable().getId(), Table.EVENT_IFILTER)
					+ " stopEvent(event); return false;};");
			input.setStyle("width:99%;");
			input.setClass("wgtInputText wgtInlineTableFilter");
		}
		td.addElement(input);
		return td;
	}

	/**
	 * 
	 */
	public TD drawCell(String cellId, int row, int col) {
		// String cellStyle = !StringUtils.isEmpty( columns[j].getStyle() ) ? columns[j].getStyle() : "wgtTDNormal";
		String cellStyle = "";
		// if (!StringUtils.isEmpty( column.getStyleAsString() )) {
		// cellStyle = column.getStyle();
		// }
		if (hasStyle()) {
			cellStyle = getStyleAsString();
		}
		String cellClass = "";
		if (!StringUtils.isEmpty(getCssClass())) {
			cellClass = getCssClass();
		}
		else {
			cellClass = "wgtTDNormal";
		}

		if (getRenderer() instanceof PlainTextColumnRenderer
				&& (getConverter() instanceof NumberConverter || getConverter() instanceof NumberConverterPrecise)) {
			cellClass += " wgtAlignRight";
		}

		// give the renderer a chance to produce the content of the cell.
		String cellContent = getRenderer().generateHTML(this, cellId, row, col);

		// eat the following context item if possible
		// it is part of the textColumnrendere and appear often
		// col.getBody().getContext().add(cellId + ".align","right", Context.TYPE_ATT, Context.STATUS_NOT_EDITABLE );

		TD td = new TD();
		td.setID(cellId);
		if (StringUtils.isNotEmpty(cellStyle))
			td.setStyle(cellStyle);
		if (StringUtils.isNotEmpty(cellClass))
			td.setClass(cellClass);
		td.addElement(cellContent);
		return td;
	}

	/**
	 * @return the columns table
	 */
	protected Table getTable() {
		return (Table)getParent();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.table.ITableColumn#setMandatory(boolean)
	 * @param mandatory - true if the table is mandatory
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void add(IBaseControl child) {
		super.add(child);
	}
	
	/**
	 * @return the exporatble
	 */
	public boolean isExporatble() {
		return exporatble;
	}

	/**
	 * @param exporatble the exporatble to set
	 */
	public void setExporatble(boolean exporatble) {
		this.exporatble = exporatble;
	}
	
	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

}
