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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil.ConversionException;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * Implementation of {@link IPopupSelect}.
 *
 * @author Ben
 *
 */
public class PopupSelect extends FormControl implements IPopupSelect {

	private static final String EVENT_TYPE_REMOVE = "remove";
	private static final String EVENT_TYPE_OPEN = "open";
	public static final String DOT_BUTTON_OPEN = ".open";
	public static final String DOT_BUTTON_REMOVE = ".remove";
	private static final int DEFAULT_ROWS_12 = 12;

	private IConverter displayConverter;
	private String displayProperty;
	private List selectedObjects = new ArrayList();
	private List availableObjects = new ArrayList();
	private boolean isMultiSelect;
	private String windowTitle;
	private String[] columns;
	private String[] columnTitles;
	private boolean readonly;
	private PopupModel currentModel;
	private boolean showSearch = false;
	private boolean resetSearch = false;
	private boolean showNoResultsFound = false; //default
	private String tableDisplayMode;
	private boolean tableEditable = false;
	private IPopupSearch popupSearch;
	private final Popup popup =null;
	protected ListenerManager listenerManager = new ListenerManager();


	public PopupSelect() {
		super();
		setDisplayConverter(new ObjectToStringConverter());
	}


	public PopupSelect(String id) {
		super(id);
		setDisplayConverter(new ObjectToStringConverter());
	}


	/**
	 * create a {@link PopupModel}
	 *
	 * @return  corresponding model
	 */
	private PopupModel createPopupModel() {
		currentModel = new PopupModel();
		currentModel.setRows(DEFAULT_ROWS_12);
		currentModel.setWindowTitle(windowTitle);
		currentModel.setColumns(columns);
		currentModel.setResourceKeys(columnTitles);
		currentModel.setTableEditable(tableEditable);
		currentModel.setTableDisplayMode(tableDisplayMode);
		return currentModel;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelectable#getPopupModel()
	 */
	public PopupModel getPopupModel() {
		if(currentModel==null)
			createPopupModel();
		return currentModel;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setWindowTitle(java.lang.String)
	 */
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setColumns(java.lang.String[])
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setColumnTitles(java.lang.String[])
	 */
	public void setColumnTitles(String[] columnTitles) {
		this.columnTitles = columnTitles;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setDisplayProperty(java.lang.String)
	 */
	public void setDisplayProperty(String displayProperty) {
		this.displayProperty = displayProperty;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.BaseControl#endHTML(java.io.PrintWriter)
	 */
	@Override
	protected void endHTML(PrintWriter out) {

		String styleclass = getContext().processValue(getId());
        boolean ro = styleclass != null && styleclass.indexOf("wgtReadonly")>-1;



		if (isMultiSelect) {
			out.print("<textarea ");
			out.print(" readonly ");
			if(ro)
				setDefaultCssClass( "wgtInputTextarea wgtReadonly" );
			else
				setDefaultCssClass( "wgtInputTextarea" );

			stdParameter( out );
			out.print(">");

			// will be rendered with a definite state on the beginning.
			out.print(getContext().processValue(getId()));

			out.print("</textarea>");
		}
		else {
			out.print("<input type=text");

			// will be rendered with a definite state on the beginning.
			out.print(" value=" + JSUtil.wr2(getContext().processValue(getId())));
			out.print(" readonly ");

			if(ro)
				setDefaultCssClass( "wgtInputTextWith2Button wgtReadonly" );
			else
				setDefaultCssClass( "wgtInputTextWith2Button" );

			stdParameter(out);
			out.print(">");
		}


		String openButtonSource = getContext().getValue(getId() + DOT_BUTTON_OPEN + ".src");
		if (StringUtils.isEmpty(openButtonSource))
			openButtonSource = "images/wgt/popupselect_select.gif";
		String deleteButtonSource = getContext().getValue(getId() + DOT_BUTTON_REMOVE + ".src");
		if (StringUtils.isEmpty(deleteButtonSource))
			deleteButtonSource = "images/wgt/popupselect_remove.gif";
		out.print("<img border=\"0\" src=\"" + openButtonSource + "\" class=\"wgtPointerCursor\" " + JSUtil.atId(getId() + DOT_BUTTON_OPEN)
				+ "style=\"vertical-align:middle;\" onclick=\"" + JSUtil.jsFireEvent(getId(), EVENT_TYPE_OPEN) + "\">");
		out.print("<img border=\"0\" src=\"" + deleteButtonSource + "\" class=\"wgtPointerCursor\" " + JSUtil.atId(getId() + DOT_BUTTON_REMOVE)
				+ "style=\"vertical-align:middle; text-nowrap: \" onclick=\"" + JSUtil.jsFireEvent(getId(), EVENT_TYPE_REMOVE) + "\"> ");
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.ActionControl#dispatch(org.webguitoolkit.ui.controls.event.ClientEvent)
	 */
	@Override
	public void dispatch(ClientEvent event) {
		if (!readonly) {
			if (EVENT_TYPE_OPEN.equals(event.getType())) {
				//PopupModel model = createPopupModel();
				PopupModel model = getPopupModel();
				List toAdd = new ArrayList(availableObjects);
				toAdd.removeAll(selectedObjects);
				model.setData(toAdd);
				if (isMultiSelect)
					model.setSelectionMode(PopupModel.SELECTION_MODE_MULTIPLE);
				Popup sh = new Popup(WebGuiFactory.getInstance(), event.getSource().getPage(), model, getId());
				SelectListener sl = new SelectListener();
				sh.registerListener(Popup.SELECTION_EVENT, sl);
				sh.registerListener(Popup.EDIT_TABLE_CONFIG_EVENT, sl);
				sh.setPopupSearch( popupSearch );
				sh.setResetSearch( resetSearch );
				sh.setShowNoResultsFoundMessage(showNoResultsFound);
				model.setPopup(sh);
				if(popupSearch!=null){
					popupSearch.setPopupModel(model);
				}
				sh.show();
			}
			else if (EVENT_TYPE_REMOVE.equals(event.getType())) {
				if (isMultiSelect) {
					//PopupModel model = createPopupModel();
					PopupModel model = getPopupModel();
					model.setData(selectedObjects);
					model.setSelectionMode(PopupModel.SELECTION_MODE_MULTIPLE);
					Popup sh = new Popup(WebGuiFactory.getInstance(), event.getSource().getPage(), model);
					sh.registerListener(Popup.SELECTION_EVENT, new RemoveListener());
					model.setPopup(sh);
					if(popupSearch!=null){
						popupSearch.setPopupModel(model);
					}
					sh.show();
				}
				else {
					setValue("");
					getSelectedObjects().clear();
					// fire action event
					if (hasActionListener())
						dispatch(new ClientEvent(PopupSelect.this, PopupSelect.this.getId(), String.valueOf(ClientEvent.TYPE_ACTION), new ContextElement[0]));

				}
				//super.dispatch(new ClientEvent(event.getSource(), event.getSourceId(), String.valueOf(ClientEvent.TYPE_ACTION), event.getChgContext()));
			}
			else {
				super.dispatch(event);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.FormControl#changeMode(int)
	 */
	@Override
	public void changeMode(int mode) {
		// do we want ot participate in the mode change?
		if (mode==Compound.MODE_EDIT && isFinal()) {
			return; //no, good bye
		}

		// super.changeMode(mode);
		//make visable, that field is editable
		// also switch the visibility of the button for the calenndar
		IContext ctx = getContext();

		String openName = "images/wgt/popupselect_select.gif";
		String removeName = "images/wgt/popupselect_remove.gif";

		if (mode == Compound.MODE_READONLY) {
			openName = "images/wgt/popupselect_select_disabled.gif";
			removeName = "images/wgt/popupselect_remove_disabled.gif";
			readonly = true;
			ctx.addClass(getId(), "wgtReadonly");
		}
		else if (mode == Compound.MODE_NEW) {
			readonly = false;
			ctx.removeClass(getId(), "wgtReadonly");
		}
		else if (mode == Compound.MODE_EDIT) {
			readonly = false;
			ctx.removeClass(getId(), "wgtReadonly");
		}

		ctx.add(getId() + DOT_BUTTON_OPEN + ".src", openName, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		ctx.add(getId() + DOT_BUTTON_REMOVE + ".src", removeName, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		clearError();
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.FormControl#clearError()
	 */
	@Override
	public void clearError() {
		getContext().removeClass(getId(), FormControl.ERROR_STYLE_CLASS);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.FormControl#saveTo(java.lang.Object)
	 */
	@Override
	public void saveTo(Object dataObject) {
		try {
			validate();
			if (isMultiSelect) {
				Collection coll = (Collection) PropertyAccessor.retrieveProperty(dataObject, getProperty());
				boolean isNew = false;
				if (coll == null || coll.size() == 0) {
					coll = new HashSet();
					isNew = true;
				}
				List tempSelected = new ArrayList(selectedObjects);
				// remove deleted
				for (Iterator iter = coll.iterator(); iter.hasNext();) {
					Object object = stripBag(iter.next());
					if (tempSelected.contains(object))
						tempSelected.remove(object);
					else
						iter.remove();
				}
				// add new
				for (Iterator iter = tempSelected.iterator(); iter.hasNext();) {
					Object object = stripBag(iter.next());
					coll.add(object);
				}
				if (isNew)
					PropertyAccessor.storeProperty(dataObject, getProperty(), coll);
			}
			else {
				Object value = null;
				if (selectedObjects != null && !selectedObjects.isEmpty()) {
					value = stripBag(selectedObjects.get(0));
				}
				PropertyAccessor.storeProperty(dataObject, getProperty(), value);
			}
		}
		catch (ValidationException e) {
			((Compound)surroundingCompound()).addError(e.getMessage(), getProperty());
		}
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.FormControl#loadFrom(java.lang.Object)
	 */
	@Override
	public void loadFrom(Object data) {
		if (isMultiSelect) {
			Collection values = (Collection) PropertyAccessor.retrieveProperty(data, getProperty());
			if (values != null) {
				selectedObjects = new ArrayList((Collection) PropertyAccessor.retrieveProperty(data, getProperty()));
				setValue(createValue(selectedObjects));
			}
			else {
				selectedObjects = new ArrayList();
				setValue(createValue(selectedObjects));
			}

		}
		else {
			selectedObjects = new ArrayList();
			Object selected = PropertyAccessor.retrieveProperty(data, getProperty());
			if (selected != null) {
				selectedObjects.add(selected);
				setValue( (String)displayConverter.convert(String.class, selected) );
			}
			else
				setValue("");
		}
	}

	/**
	 * @param selected objects
	 * @return display value for multiple selections
	 */
	private String createValue(Collection objects) {
		String value = "";
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			Object object = iter.next();
			if (object != null){
				value += displayConverter.convert(String.class, object) + "\n";
			}
		}
		return value;
	}

	/**
	 * handle new selection
	 *
	 * @author ben
	 *
	 */
	public class SelectListener implements IServerEventListener {
		public void handle(ServerEvent event) {
			if(event.getTypeAsInt()==Popup.EDIT_TABLE_CONFIG_EVENT){
				//update this
				String tableConfig = (String) event.getParameter(Popup.EDIT_TABLE_CONFIG_EVENT_PARAMETER);
				String tableConfigRows = (String) event.getParameter(Popup.EDIT_TABLE_CONFIG_ROWS_EVENT_PARAMETER);
				PopupSelect.this.getPopupModel().setRows(Integer.parseInt(tableConfigRows));
				PopupSelect.this.getPopupModel().setTableConfig(tableConfig);
				listenerManager.fireServerEvent(event);
			} else {
				Object result = event.getParameter(Popup.SELECTION_EVENT_PARAMETER);
				if (result instanceof Collection) {
					for (Iterator iter = ((Collection) result).iterator(); iter.hasNext();) {
						Object object = stripBag(iter.next());
						//Object object = iter.next();
						if (!selectedObjects.contains(object))
							selectedObjects.add(object);
					}
					PopupSelect.this.setValue(createValue(selectedObjects));
				}
				else {
					PopupSelect.this.setValue( (String)displayConverter.convert(String.class, result) );
					selectedObjects = new ArrayList();
					selectedObjects.add(stripBag(result));
				}

				// fire action event
				if (hasActionListener())
					dispatch(new ClientEvent(PopupSelect.this, PopupSelect.this.getId(), String.valueOf(ClientEvent.TYPE_ACTION), new ContextElement[0]));

				getPopupModel().setPopup(null);
			}
		}
	}

	/**
	 * handle remove selection
	 *
	 * @author Ben
	 *
	 */
	public class RemoveListener implements IServerEventListener {
		public void handle(ServerEvent event) {
			Object result = event.getParameter(Popup.SELECTION_EVENT_PARAMETER);
			if (result instanceof Collection) {
				for (Iterator iter = ((Collection) result).iterator(); iter.hasNext();) {
					Object object = stripBag(iter.next());
					//Object object = iter.next();
					selectedObjects.remove(object);
				}
				PopupSelect.this.setValue(createValue(selectedObjects));
			}
			else {
				selectedObjects.remove(stripBag(result));
				PopupSelect.this.setValue(createValue(selectedObjects));
			}

			// fire action event
			if (hasActionListener())
				dispatch(new ClientEvent(PopupSelect.this, PopupSelect.this.getId(), String.valueOf(ClientEvent.TYPE_ACTION), new ContextElement[0]));

			// fire action event
			// dispatch(new ClientEvent(PopupSelect.this,PopupSelect.this.getId(),String.valueOf(ClientEvent.TYPE_ACTION), new ContextElement[0]));

		}
	}

	/**
	 * @param o object or bag
	 * @return object
	 */
	private Object stripBag(Object o) {
		if (o instanceof DataBag)
			return ((DataBag) o).getDelegate();
		return o;
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
	private void registerListener(int eventtype, IServerEventListener liz) {
		listenerManager.registerListener(eventtype, liz);
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
	 * @return selected objects
	 */
	public List getSelectedObjects() {
		return selectedObjects;
	}

	/**
	 * @return selected object
	 */
	public Object getSelectedObject() {
		List<Object> sel = this.getSelectedObjects();
		if(sel!=null && sel.size()==1)
			return sel.get(0);
		return null;
	}


	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setSelectedObjects(java.util.List)
	 */
	public void setSelectedObjects(List selectedObjects) {
		this.selectedObjects = selectedObjects;
	}

	/**
	 * Use this method for not multiple selections
	 *
	 * @param selectedObject
	 */
	public void setSelectedObject(Object selectedObject) {
		List s = new ArrayList();
		s.add(selectedObject);
		this.setSelectedObjects(s);
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.BaseControl#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean vis) {
		getContext().makeVisible(getId() + DOT_BUTTON_OPEN, vis);
		getContext().makeVisible(getId() + DOT_BUTTON_REMOVE, vis);
		super.setVisible(vis);
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setAvailableObjects(java.util.List)
	 */
	public void setAvailableObjects(List availableObjects) {
		this.availableObjects = availableObjects;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setIsMultiselect(boolean)
	 */
	public void setIsMultiselect(boolean isMultiSelect) {
		this.isMultiSelect = isMultiSelect;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setShowSearch(boolean)
	 */
	public void setShowSearch(boolean showSearch) {
		this.showSearch = showSearch;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setPopupSearch(org.webguitoolkit.ui.controls.form.popupselect.IPopupSearch)
	 */
	public void setPopupSearch(IPopupSearch search ) {
		this.popupSearch = search;
	}

	/**
	 * @return true if reset is enabled
	 */
	public boolean isResetSearch() {
		return resetSearch;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setResetSearch(boolean)
	 */
	public void setResetSearch(boolean resetSearch) {
		this.resetSearch = resetSearch;
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect#setShowNoResultsFoundMessage(boolean)
	 */
	public void setShowNoResultsFoundMessage(boolean showNoResultsFound) {
		this.showNoResultsFound = showNoResultsFound;
	}


	/**
	 * @return the tableDisplayMode
	 */
	public String getTableDisplayMode() {
		return tableDisplayMode;
	}


	/**
	 * @param tableDisplayMode the tableDisplayMode to set
	 */
	public void setTableDisplayMode(String tableDisplayMode) {
		this.tableDisplayMode = tableDisplayMode;
	}


	/**
	 * @return the tableEditable
	 */
	public boolean isTableEditable() {
		return tableEditable;
	}


	/**
	 * @param tableEditable the tableEditable to set
	 */
	public void setTableEditable(boolean tableEditable) {
		this.tableEditable = tableEditable;
	}

	/**
	 * @param displayConverter the displayConverter to set
	 */
	public void setDisplayConverter(IConverter displayConverter) {
		this.displayConverter = displayConverter;
	}

	protected class ObjectToStringConverter implements IConverter{
		private static final long serialVersionUID = 1L;

		public ObjectToStringConverter(){
		}
		
		/* (non-Javadoc)
		 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
		 */
		public Object convert(Class type, Object value) {
			if( type == String.class ){
				return PropertyAccessor.retrieveString(value, displayProperty);
			}
			throw new WGTException("Wrong type (String expected): " + type.getName() );
		}

		public Object parse(String textRep) throws ConversionException {
			throw new NotImplementedException();
		}
		
	}

}
