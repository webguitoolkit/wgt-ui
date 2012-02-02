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
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.IBaseControl;

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
public class SimpleList extends BaseControl implements IList{

	/**
	 * 
	 */
	protected static final String LIST_ITEM_CSS = "wgtListItem";
	protected static final String LIST_CSS = "wgtList";
	protected String liCSS;

	protected HashMap<IBaseControl, HashMap<String, String>> liAttributs = new HashMap<IBaseControl, HashMap<String, String>>();
	
	/**
	 * Default constructor
	 */
	public SimpleList() {
		super();
		setListCSS(LIST_CSS);
		setListItemCSS(LIST_ITEM_CSS);
	}

	/**
	 * @param id Provided id for the list.
	 */
	public SimpleList(String id) {
		super(id);
		setListCSS(LIST_CSS);
		setListItemCSS(LIST_ITEM_CSS);
	}
	
	public void setListCSS( String ulCSS ){
		setDefaultCssClass("wgtList");
	}
	public void setListItemCSS( String liCSS ){
		this.liCSS = liCSS;
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

	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#draw(java.io.PrintWriter)
	 */
	protected void draw(PrintWriter out) {
		UL ul = new UL();
		stdParameter(ul);
		int i = 0;
		for (Iterator<BaseControl> it = getChildren().iterator(); it.hasNext();) {
			BaseControl control = it.next();
			LI li = new LI();
			li.setClass(liCSS);
			String ident = getId() + "_" + i;
			li.setID(ident);
			ul.addElement(li);
			if(liAttributs.containsKey(control)){
				HashMap<String, String> hashMap = liAttributs.get(control);
				for( String key : hashMap.keySet())
					li.addAttribute(key, hashMap.get(key));
			}
			li.addElement(new EcsAdapter(control));
			i++;
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
	 * @return a Map with the Composites and their id's as key.
	 */
	private Map<String, IBaseControl> getControlMap() {
		Map<String, IBaseControl> result = new HashMap<String, IBaseControl>();
		for (Iterator<BaseControl> it = getChildren().iterator(); it.hasNext();) {
			BaseControl li = it.next();
			result.put(li.getId(), li);
		}
		return result;
	}

	/**
	 * @return a list with the item id's
	 */
	public List<String> getIds() {
		List<String> result = new ArrayList<String>(getChildren().size());
		for (Iterator<BaseControl> it = getChildren().iterator(); it.hasNext();) {
			IBaseControl bc = it.next();
			result.add(bc.getId());
		}
		return result;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#getOrderedControls()
	 */
	public List<? extends IBaseControl> getListItems() {
		return (List<? extends IBaseControl>)getChildren();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#getListItem(java.lang.String)
	 */
	public IBaseControl getListItem(String id) {
		return (IBaseControl)getControlMap().get(id);
	}

	@Override
	public void setAttribute(String attributeName, String attributeValue) {
		super.setAttribute(attributeName, attributeValue);
	}

	public void setAttribute( IBaseControl bc, String attributeName, String attributeValue) {
		HashMap<String, String> attributes = liAttributs.get(bc);
		if(attributes == null ){
			attributes = new HashMap<String, String>();
			liAttributs.put(bc, attributes);
		}
		attributes.put(attributeName, attributeValue);
	}
	
	
}
