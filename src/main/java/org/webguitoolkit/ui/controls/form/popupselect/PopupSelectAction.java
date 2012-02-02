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
import java.util.List;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;


/**
 * <h1>Use a {@link IPopupSelect} without a ICompound</h1>
 * <p>
 * Use a PopupSelectAction to use an IPopupSelect on any control
 * that is able to fire an onAction event (i.e. Button)
 * </p>
 * <br />
 * <b>Usage</b>
 * <pre>
 * // PopupSelectAction
 * PopupSelectAction actionlistener = new PopupSelectAction(getPage(), false, new String[] { "Number", "Name" }, new String[] { "Customer Number",
 *		"Customer Name" }, "Select", new Listener4PopupSelectAction());
 *
 *	//set options business objects
 *	actionlistener.setAvailableObjects(customers);
 *	
 *	//create control and register Listener
 *	getFactory().createButton(layout, null, "Show Options", "Show Options", actionlistener);
 * </pre>
 * <pre>
 * 	//thle listener
 * 	private class Listener4PopupSelectAction implements IServerEventListener {
 * 	
 * 		public void handle(ServerEvent event) {
 * 			Object selection = event.getParameter(Popup.SELECTION_EVENT_PARAMETER);
 * 			if (selection instanceof IDataBag) {
 * 				IDataBag cust = (IDataBag) selection; 
 * 				getPage().sendInfo("You selected " + cust.getString("Name") );
 * 			}
 * 			else {
 * 				getPage().sendError("No Selection or some kind of failure");
 * 			}
 * 		}
 * 	}
 * </pre>
 * @author Ben
 * 
 */
public class PopupSelectAction implements IActionListener,IPopupSelectable {

	private PopupModel currentModel;
	private IPopupSearch popupSearch;
	private String windowTitle;
	private String[] columns;
	private String[] columnTitles;
	private List availableObjects = new ArrayList();
	private boolean showSearch = false;
	private boolean resetSearch = false;
	private boolean isMultiSelect;
	private Page page;
	private boolean showNoResultsFound = false;
	protected ListenerManager listenerManager = new ListenerManager();

	public class SelectListener implements IServerEventListener {
		public void handle(ServerEvent event) {
			Object result = event.getParameter(Popup.SELECTION_EVENT_PARAMETER);

			// fire back
			// ServerEvent se = new ServerEvent(Popup.SELECTION_EVENT);
			// se.putParameter(Popup.SELECTION_EVENT_PARAMETER, result);
			fireServerEvent(event);
		}
	}

	public PopupSelectAction(Page page) {
		this.page = page;
	}

	public PopupSelectAction(Page page,boolean isMultiselect, String[] columns, String[] columnTitles, String windowTitle, IServerEventListener listener) {
		this.setColumns(columns);
		this.setColumnTitles(columnTitles);
		this.setIsMultiselect(isMultiselect);
		this.setWindowTitle(windowTitle);
		this.setListener(listener);
		this.page = page;
	}

	/**
	 * create a {@link PopupModel}
	 * 
	 * @return
	 */
	private PopupModel createPopupModel() {
		currentModel = new PopupModel();
		currentModel.setRows(12);
		currentModel.setWindowTitle(windowTitle);
		currentModel.setColumns(columns);
		currentModel.setResourceKeys(columnTitles);
		return currentModel;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#getPopupModel()
	 */
	public PopupModel getPopupModel() {
		if (currentModel == null)
			createPopupModel();
		return currentModel;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setWindowTitle(java.lang.String)
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setPopupSearch(org.webguitoolkit.ui.controls.form.popupselect.IPopupSearch)
	 */
	public void setPopupSearch(IPopupSearch search) {
		this.popupSearch = search;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setColumns(java.lang.String[])
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setColumnTitles(java.lang.String[])
	 */
	public void setColumnTitles(String[] columnTitles) {
		this.columnTitles = columnTitles;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setAvailableObjects(java.util.List)
	 */
	public void setAvailableObjects(List availableObjects) {
		this.availableObjects = availableObjects;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setShowSearch(boolean)
	 */
	public void setShowSearch(boolean showSearch) {
		this.showSearch = showSearch;
	}
	

	
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setShowNoResultsFoundMessage(boolean)
	 */
	public void setShowNoResultsFoundMessage(boolean showNoResultsFound){
		this.showNoResultsFound = showNoResultsFound;
	}

	/**
	 * 
	 * @param eventtype
	 * @param liz
	 */
	private void registerListener(int eventtype, IServerEventListener liz) {
		listenerManager.registerListener(eventtype, liz);
	}

	/**
	 * fires back Popup.SELECTION_EVENT for given Listener
	 * 
	 * @param liz
	 */
	public void setListener(IServerEventListener liz) {
		registerListener(Popup.SELECTION_EVENT, liz);
	}
	
	/**
	 * fires back Popup.EDIT_TABLE_CONFIG_EVENT for given Listener
	 * @param liz
	 */
	public void setEditTableConfigListener(IServerEventListener liz) {
		registerListener(Popup.EDIT_TABLE_CONFIG_EVENT, liz);
	}

	/**
	 * 
	 * @param eventtype
	 * @param liz
	 */
	private void removeListener(int eventtype, IServerEventListener liz) {
		listenerManager.removeListener(eventtype, liz);
	}

	/**
	 * 
	 * @param event
	 */
	public void fireServerEvent(ServerEvent event) {
		listenerManager.fireServerEvent(event);
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setIsMultiselect(boolean)
	 */
	public void setIsMultiselect(boolean isMultiSelect) {
		this.isMultiSelect = isMultiSelect;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.event.IActionListener#onAction(org.webguitoolkit.ui.controls.event.ClientEvent)
	 */
	public void onAction(ClientEvent event) {
		// PopupModel model = createPopupModel();
		PopupModel model = getPopupModel();
		List toAdd = new ArrayList(availableObjects);
		// toAdd.removeAll(selectedObjects);
		model.setData(toAdd);
		if (isMultiSelect)
			model.setSelectionMode(PopupModel.SELECTION_MODE_MULTIPLE);
		Popup sh = new Popup(WebGuiFactory.getInstance(), page, model);
		SelectListener sl = new SelectListener();
		sh.registerListener(Popup.SELECTION_EVENT, sl);
		sh.registerListener(Popup.EDIT_TABLE_CONFIG_EVENT, sl);
		sh.setPopupSearch(popupSearch);
		sh.setResetSearch(resetSearch);
		sh.setShowNoResultsFoundMessage(showNoResultsFound);
		sh.show();
	}

	/**
	 * @return true if reset is enabled
	 */
	public boolean isResetSearch() {
		return resetSearch;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#setResetSearch(boolean)
	 */
	public void setResetSearch(boolean resetSearch) {
		this.resetSearch = resetSearch;
	}

}
