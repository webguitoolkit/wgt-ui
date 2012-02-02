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
package org.webguitoolkit.ui.controls.form.popupselect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <h1>Implemantation of Popup for IPopupselect</h1> Use {@link IPopupSelect}
 * 
 * @author Ben
 * 
 */
public class Popup extends AbstractPopup {

	/**
	 * 
	 */
	public static final String BUTTON_APPLY = "BUTTON_APPLY";

	/**
	 * 
	 */
	public static final String BUTTON_APPLY_AND_CONTINUE = "BUTTON_APPLY_AC";

	/**
	 * 
	 */
	public static final String BUTTON_RESET = "BUTTON_RESET";

	private ITable valueTable;
	private IButton apply, applyAndContinue, cancel;
	private PopupModel model;
	private IPopupSearch search;
	private boolean resetSearch = false; // default
	private boolean showNoResultsFound = false; // default

	public static final int SELECTION_EVENT = 8451;
	public static final int SEARCH_EVENT = 1548;
	public static final int EDIT_TABLE_CONFIG_EVENT = 20090316;
	public static final String SELECTION_EVENT_PARAMETER = "selectiondata";
	public static final String SELECTION_EVENT_PARAMETER_CLOSED = "selectionpopupclosed";
	public static final String SEARCH_EVENT_PARAMETER_BAG = "searchbag";
	public static final String SEARCH_EVENT_PARAMETER_HELPER = "thisHelper";
	public static final String EDIT_TABLE_CONFIG_EVENT_PARAMETER = "edittableconfig";
	public static final String EDIT_TABLE_CONFIG_ROWS_EVENT_PARAMETER = "edittableconfigrows";
	public static final String SELECTION_CHECKBOX_PROPERTY = "selchkbx";
	public static final String SELECTION_CHECKBOX_TABLE_CONFIG = SELECTION_CHECKBOX_PROPERTY + ",20px;";
	private static final int DEFAULT_WIDTH = 480;
	private static final int DEFAULT_HEIGHT = 330;

	/**
	 * For automated GUI testing HTML IDs are necessary. An AbstractView is often a container for nested HTML elements which need
	 * their unique ID. However, it is possible that more than one AbstractView is shown at the same time. Thus, the nested HTML
	 * elements must be distinct. By giving the view an ID the nested HTML elements can use this parent ID together with the own
	 * element ID to create a truly unique ID from their element hierarchy (e.g. a combination like
	 * PERSON_VIEW_ID.SUBMIT_BUTTON_ID)
	 */
	private String id;

	public Popup(WebGuiFactory factory, Page page, PopupModel model) {
		this(factory, page, model, null);
	}

	public Popup(WebGuiFactory factory, Page page, PopupModel model, int width, int height) {
		this(factory, page, model, width, height, null);
	}

	public Popup(WebGuiFactory factory, Page page, PopupModel model, String newId) {
		super(
				factory,
				page,
				model.getWindowTitle(),
				model.getPopupWidth() == -1 ? DEFAULT_WIDTH : model.getPopupWidth(),
				model.getPopupHeight() == -1 ? (model.getSelectionMode() == PopupModel.SELECTION_MODE_SINGLE ? DEFAULT_HEIGHT : DEFAULT_HEIGHT + 30) : model.getPopupHeight());
		setId(newId);
		this.model = model;
	}

	public Popup(WebGuiFactory factory, Page page, PopupModel model, int width, int height, String newId) {
		super(factory, page, model.getWindowTitle(), width, height);
		setId(newId);
		this.model = model;
	}

	private class SearchListener implements IActionListener {
		public void onAction(ClientEvent event) {
			triggerSearch();
		}
	}

	/**
	 * manually trigger search action
	 */
	public void triggerSearch() {
		List result = search.performSearch();
		valueTable.getDefaultModel().setTableData(wrapWithDataBag(result));
		valueTable.reload();
		if (showNoResultsFound && (result == null || result.isEmpty()))
			getPage().sendWarn(TextService.getString("default.noResultsMessage@No results found"));

	}

	private class ResetListener implements IActionListener {
		public void onAction(ClientEvent event) {
			search.resetSearch();
			valueTable.getDefaultModel().setTableData(new ArrayList());
			valueTable.reload();
		}
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		this.getViewConnector().addCssClass("wgtPopupSelectPopup");

		ITableLayout grid = getFactory().createTableLayout(viewConnector);
		grid.getEcsTable().setStyle("width: 100%;");

		if (search != null) {
			// render search
			ITableLayout searchGrid = getFactory().createTableLayout(grid);

			// call search class to render search controls
			search.generateControls(searchGrid, factory);

			String searchkey = model.getReourceKey(".search");
			String searchtt = model.getReourceKey(".search.tt");
			if (searchkey == null)
				searchkey = "Search";
			if (searchtt == null)
				searchtt = "Search values";

			ITableLayout buttonGrid = getFactory().createTableLayout(searchGrid);
			buttonGrid.getEcsTable().setCellPadding(0);
			buttonGrid.getEcsTable().setCellSpacing(0);
			getFactory().createButton(buttonGrid, null, searchkey, searchtt, new SearchListener());

			if (resetSearch) {
				String resetkey = model.getReourceKey(".reset");
				String resettt = model.getReourceKey(".reset.tt");
				if (resetkey == null)
					resetkey = "Reset";
				if (resettt == null)
					resettt = "Reset values";

				getFactory().createButton(buttonGrid, null, resetkey, resettt, new ResetListener(), getId() + "." + BUTTON_RESET);

			}
			else {

			}
			searchGrid.getCurrentCell().setColSpan(2);
			grid.newRow();
		}

		if (model != null && model.isValid()) {
			valueTable = getFactory().createTable(grid, "", model.getRows(), "TABLE_" + getId());
			valueTable.setDisplayMode(model.getTableDisplayMode());
			valueTable.setEditable(model.isTableEditable());
			valueTable.setListener(new SelectionListener());
			valueTable.getStyle().addWidth(model.getTableWidth());
			if (model.getSelectionMode() == PopupModel.SELECTION_MODE_MULTIPLE)
				valueTable.addCheckboxColumn(SELECTION_CHECKBOX_PROPERTY, "");
			for (int i = 0; i < model.getColumns().length; i++) {
				if (model.getReourceKey(model.getColumns()[i]) != null) {
					getFactory().createTableColumn(valueTable, TextService.getString(model.getReourceKey(model.getColumns()[i])),
							model.getColumns()[i], model.isFilterEnabled());
				}
				else {
					getFactory().createTableColumn(valueTable, Popup.createName(model.getColumns()[i]), model.getColumns()[i],
							model.isFilterEnabled());
				}
			}
			if (StringUtils.isNotBlank(model.getTableConfig())) {
				if (model.getSelectionMode() == PopupModel.SELECTION_MODE_MULTIPLE) {
					valueTable.loadColumnConfig(SELECTION_CHECKBOX_TABLE_CONFIG + model.getTableConfig());
				}
				else {
					valueTable.loadColumnConfig(model.getTableConfig());
				}
			}
			this.reload();
			if (model.getSortByColumn() > -1 && model.getSortByColumn() < model.getColumns().length)
				valueTable.sort(model.getSortByColumn());

			if (model.getSelectionMode() == PopupModel.SELECTION_MODE_MULTIPLE) {
				grid.newRow();
				String aKey = model.getReourceKey(".apply");
				String aTt = model.getReourceKey(".apply.tt");
				if (aKey == null)
					aKey = "Apply";
				if (aTt == null)
					aTt = "Apply selection";

				ITableLayout buttons = getFactory().createTableLayout(grid);
				apply = getFactory().createButton(buttons, null, aKey, aTt, new SelectionListener(), getId() + "." + BUTTON_APPLY);
				if (model.isShowApplyAndContinue()) {
					String aACKey = model.getReourceKey(".applyac");
					String aACTt = model.getReourceKey(".applyac.tt");
					if (aACKey == null)
						aACKey = "Apply and continue";
					if (aACTt == null)
						aACTt = "Apply selection and keep this window open";
					applyAndContinue = getFactory().createButton(buttons, null, aACKey, aACTt, new SelectionListener(),
							getId() + "." + BUTTON_APPLY_AND_CONTINUE);
				}
			}
		}
		else {
			getFactory().createLabel(grid, "No data available");
		}
	}

	public void reload() {
		if (model.getData() != null) {
			valueTable.getDefaultModel().setTableData(wrapWithDataBag(model.getData()));
			valueTable.reload();
		}
	}

	private List wrapWithDataBag(List data) {
		if (data != null) {
			List result = new ArrayList(data.size());
			for (Iterator it = data.iterator(); it.hasNext();) {
				Object o = it.next();
				if (!(o instanceof IDataBag))
					result.add(WebGuiFactory.getInstance().createDataBag(o));
				else
					result.add(o);
			}
			return result;
		}
		else {
			return new ArrayList();
		}
	}

	protected static String createName(String locator) {
		String result = splitByCapitalLetters(locator.substring(0, 1).toUpperCase() + locator.substring(1));
		return result;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(PopupModel model) {
		this.model = model;
	}

	public static String splitByCapitalLetters(String string) {
		StringBuffer result = null;
		if (string != null) {
			int l = string.length();
			for (int i = 0; i < l; i++) {
				if (i == 0) {
					// uppercase first letter in any case
					result = new StringBuffer();
					result.append(Character.toUpperCase(string.charAt(i)));
				}
				else {
					// insert blank if and lower case if
					// is upper case and previous is lower case orfollowing is lower case
					// this customerSAPNumber - > Customer SAP Number
					if (Character.isUpperCase(string.charAt(i))
							&& (Character.isLowerCase(string.charAt(i - 1)) || (((i + 1) < l) && Character.isLowerCase(string.charAt(i + 1))))) {
						result.append(" " + Character.toLowerCase(string.charAt(i)));
						// insert blank before number
						// page123 -> Page 123
					}
					else if (Character.isDigit(string.charAt(i)) && !Character.isDigit(string.charAt(i - 1))) {
						result.append(" " + string.charAt(i));
					}
					else {
						result.append(string.charAt(i));
					}
				}
			}
			return result.toString();
		}
		return string;
	}

	public class SelectionListener extends AbstractTableListener implements IActionListener {
		public void onAction(ClientEvent event) {
			if (event.getSource() == cancel) {
				// getViewConnector().getWindowActionListener().onClose(event);
				close();
			}
			else if ((event.getSource() == apply || event.getSource() == applyAndContinue) && model != null
					&& model.getSelectionMode() == PopupModel.SELECTION_MODE_MULTIPLE) {
				if (valueTable != null) {
					List selected = new ArrayList();
					valueTable.load();
					Iterator irows = (valueTable.getDefaultModel()).getTableData().iterator();
					while (irows.hasNext()) {
						IDataBag object = (IDataBag)irows.next();
						if (object != null && object.get(SELECTION_CHECKBOX_PROPERTY) != null
								&& object.getBool(SELECTION_CHECKBOX_PROPERTY)) {
							selected.add(object);
						}
					}
					if (selected.isEmpty()) {
						getPage().sendWarn(model.getNoSelectionErrorKey());
					}
					else {
						ServerEvent serverevent = new ServerEvent(Popup.SELECTION_EVENT);
						serverevent.putParameter(Popup.SELECTION_EVENT_PARAMETER, selected);
						if (event.getSource() == apply)
							serverevent.putParameter(Popup.SELECTION_EVENT_PARAMETER_CLOSED, Boolean.TRUE);
						else
							serverevent.putParameter(Popup.SELECTION_EVENT_PARAMETER_CLOSED, Boolean.FALSE);
						fireServerEvent(serverevent);
						// getViewConnector().getWindowActionListener().onClose(event);
						if (event.getSource() == apply)
							close();
						else{
							//clear selection
							Iterator irows2 = (valueTable.getDefaultModel()).getTableData().iterator();
							while (irows2.hasNext()) {
								IDataBag object = (IDataBag)irows2.next();
								if (object != null && object.get(SELECTION_CHECKBOX_PROPERTY) != null
										&& object.getBool(SELECTION_CHECKBOX_PROPERTY)) {
									object.setObject(SELECTION_CHECKBOX_PROPERTY, Boolean.FALSE);
								}
							}							
							valueTable.reload();
						}
					}
				}
				else {
					getPage().sendWarn(model.getNoSelectionErrorKey());
				}
			}
		}

		@Override
		public void onEditTableLayout(ClientEvent event, int rowCount, String tableSetting) {
			super.onEditTableLayout(event, rowCount, tableSetting);
			ServerEvent serverevent = new ServerEvent(Popup.EDIT_TABLE_CONFIG_EVENT);

			if (tableSetting.startsWith(SELECTION_CHECKBOX_TABLE_CONFIG)) {
				tableSetting = StringUtils.substring(tableSetting, SELECTION_CHECKBOX_TABLE_CONFIG.length());
			}

			serverevent.putParameter(Popup.EDIT_TABLE_CONFIG_EVENT_PARAMETER, tableSetting);
			serverevent.putParameter(Popup.EDIT_TABLE_CONFIG_ROWS_EVENT_PARAMETER, String.valueOf(rowCount));
			fireServerEvent(serverevent);
		}

		@Override
		public void onRowSelection(ITable table, int row) {
			if (model != null && model.getSelectionMode() == PopupModel.SELECTION_MODE_SINGLE) {
				if (((Table)table).getSelectedRowIndex() > -1) {
					IDataBag bag = valueTable.getSelectedRow();
					ServerEvent serverevent = new ServerEvent(Popup.SELECTION_EVENT);
					serverevent.putParameter(Popup.SELECTION_EVENT_PARAMETER_CLOSED, Boolean.TRUE);
					serverevent.putParameter(Popup.SELECTION_EVENT_PARAMETER, bag);
					fireServerEvent(serverevent);
					// getViewConnector().getWindowActionListener().onClose(event);
					close();
				}
				else {
					getPage().sendWarn(model.getNoSelectionErrorKey());
				}
			}
		}
	}

	public void setPopupSearch(IPopupSearch popupSearch) {
		search = popupSearch;
	}

	public void setResetSearch(boolean resetSearch) {
		this.resetSearch = resetSearch;
	}

	public void setShowNoResultsFoundMessage(boolean showNoResultsFound) {
		this.showNoResultsFound = showNoResultsFound;
	}

	/**
	 * @return the valueTable
	 */
	public ITable getValueTable() {
		return valueTable;
	}

	/**
	 * @param valueTable the valueTable to set
	 */
	public void setValueTable(ITable valueTable) {
		this.valueTable = valueTable;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
