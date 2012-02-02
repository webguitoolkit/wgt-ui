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
package org.webguitoolkit.ui.controls.list;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ecs.html.LI;
import org.apache.ecs.html.UL;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;

/**
 * Implementation of a List Control Interface that supports drag and drop to sort the items. It assumes a list of BaseControls as
 * content (the controls children). Whenever a sort was done by the user a call back "onSort" will be called and the newly ordered
 * list will be passed to the call back. It uses the jQuery sortable script.
 * 
 * Functional description @see org.webguitoolkit.ui.controls.list.ISortableList
 * 
 * @author Peter
 * 
 */
public class SortableList extends ActionControl implements ISortableList {

	/**
	 * 
	 */
	private static final String CONTEXT_TYPE_INIT_LIST = "listI";
	private IListListener listener;
	private List connectedLists = new ArrayList();

	/**
	 * Default constructor
	 */
	public SortableList() {
		super();
		setCssClass("wgtList");
		listener = new DefaultListListener();
	}

	/**
	 * @param id Provided id for the list.
	 */
	public SortableList(String id) {
		super(id);
		setCssClass("wgtList");
	}

	/**
	 * nothing happens here
	 */
	protected void endHTML(PrintWriter out) {
		// Intentionally left empty
	}

	/**
	 * Add CSS and JS files. register at context
	 */
	protected void init() {

		getPage().addWgtCSS("eh/wgtlist.css");
		getPage().addControllerJS("list.js");

		getContext().add(getId(), "", CONTEXT_TYPE_INIT_LIST, IContext.STATUS_COMMAND);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#draw(java.io.PrintWriter)
	 */
	protected void draw(PrintWriter out) {
		UL ul = new UL();
		stdParameter(ul);
		int i = 0;
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			BaseControl control = (BaseControl)it.next();
			LI li = new LI();
			li.setClass("wgtListItem");
			String ident = getId() + "_" + i;
			li.setID(ident);
			ul.addElement(li);
			li.addElement(new EcsAdapter(control));
			i++;
		}
		if (isDrawn()) {
			getContext().add(getId(), "", CONTEXT_TYPE_INIT_LIST, IContext.STATUS_COMMAND);
			for (Iterator it = connectedLists.iterator(); it.hasNext();) {
				SortableList list = (SortableList)it.next();
				String otherId = list.getId();
				getContext().add(getId() + ".link" + list.getId(), getId() + "|" + otherId, "listC", IContext.STATUS_COMMAND);
			}
		}
		ul.output(out); // write the stream
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#add(org.webguitoolkit.ui.controls.IBaseControl)
	 */
	public void add(IBaseControl child) {
		super.add(child);
	}

	/**
	 * @param item to be added at the end of the list.
	 */
	public void addListItem(IBaseControl item) {
		if (isDrawn()) {
			redraw();
		}
		add(item);
	}

	/**
	 * @param item to be removed
	 */
	public void removeListItem(IBaseControl item) {
		getChildren().remove(item);
		if (isDrawn()) {
			((BaseControl)item).remove();
			redraw();
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.ActionControl#dispatch(org.webguitoolkit.ui.controls.event.ClientEvent)
	 */
	public void dispatch(ClientEvent event) {
		// list was sorted
		String eventType = event.getType();

		if ("item_sort".equals(eventType)) {
			// order the list by id from the event
			String sortedItemId = event.getParameter(0);
			IBaseControl sortedItem = null;
			int childrenSize = getChildren().size();
			int oldPos = 0;
			int newPos = 0;
			for (Iterator it = getChildren().iterator(); it.hasNext();) {
				sortedItem = (IBaseControl)it.next();
				if (sortedItem.getId().equals(sortedItemId)) {
					break;
				}
				oldPos++;
			}
			for (int i = 0; i < childrenSize; i++) {
				String ident = event.getParameter(i + 1);
				if (ident.equals(sortedItemId)) {
					newPos = i;
					break;
				}
			}
			listener.onSort(this, sortedItem, oldPos, newPos);
		}
		else if ("item_remove".equals(eventType)) {
			String removedItemId = event.getParameter(0);
			Map listMap = getControlMap();
			IBaseControl removedItem = (IBaseControl)listMap.get(removedItemId);
			listener.onDrag(this, removedItem);
		}
		else if ("item_receive".equals(eventType)) {
			String addedItemId = event.getParameter(0);

			for (Iterator lit = connectedLists.iterator(); lit.hasNext();) {
				ISortableList sourceList = (ISortableList)lit.next();
				IBaseControl item = (IBaseControl)sourceList.getListItem(addedItemId);
				// find the position where the item was dropped
				int pos = 0;
				for (; pos < event.getParameterSize() - 1; pos++) {

					String parameter = event.getParameter(pos + 1);
					if (parameter.equals(addedItemId) || parameter == null)
						break;
				}
				if (item != null) {
					listener.onDrop(this, item, pos, sourceList);
					break;
				}
			}
		}
	}

	/**
	 * @return a Map with the Composites and their id's as key.
	 */
	private Map getControlMap() {
		Map result = new HashMap();
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			BaseControl li = (BaseControl)it.next();
			result.put(li.getId(), li);
		}
		return result;
	}

	/**
	 * @return a list with the item id's
	 */
	public List getIds() {
		List result = new ArrayList(getChildren().size());
		int i = 0;
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			IBaseControl bc = (IBaseControl)it.next();
			result.add(bc.getId());
		}
		return result;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#setListener(org.webguitoolkit.ui.controls.list.IListListener)
	 */
	public void setListener(IListListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listender can not be NULL");
		this.listener = listener;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#enable()
	 */
	public void enable() {
		getContext().add(getId() + ".state", getId(), "listE", IContext.STATUS_NOT_EDITABLE);
		removeCssClass("wgtListDisabled");
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#disable()
	 */
	public void disable() {
		getContext().add(getId() + ".state", getId(), "listD", IContext.STATUS_NOT_EDITABLE);
		addCssClass("wgtListDisabled");
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#connectWith(org.webguitoolkit.ui.controls.list.ISortableList)
	 */
	public void connectWith(ISortableList other) {
		String otherId = ((SortableList)other).getId();
		getContext().add(getId() + ".link" + otherId, getId() + "|" + otherId, "listC", IContext.STATUS_COMMAND);
		connectedLists.add(other);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#setUnsortable(java.lang.String)
	 */
	public void setUnsortable(String id) {
		getContext().add(getId() + ".cancel" + id, getId() + "|" + id, "listU", IContext.STATUS_COMMAND);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#getOrderedControls()
	 */
	public List getListItems() {
		return getChildren();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#getListItem(java.lang.String)
	 */
	public IBaseControl getListItem(String id) {
		return (IBaseControl)getControlMap().get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#setHandle(java.lang.String)
	 */
	public void setHandle(String id) {
		getContext().add(getId() + ".handle" + id, getId() + "|" + id, "listH", IContext.STATUS_COMMAND);
	}
}
