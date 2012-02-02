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
package org.webguitoolkit.ui.controls.grid;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.ecs.html.Div;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.json.JSONArray;
import org.webguitoolkit.ui.controls.util.json.JSONFunction;
import org.webguitoolkit.ui.controls.util.json.JSONObject;
import org.webguitoolkit.ui.controls.util.json.JSONSimpleValue;
import org.webguitoolkit.ui.http.resourceprocessor.JSResourceProcessor;

/**
 * @author i102415
 * 
 */
public class Grid extends ActionControl {

	/**
		 * 
		 */
	private static final String CONTEXT_TYPE_INIT_GRID = "gridI";
	private static final String CONTEXT_TYPE_LOAD_GRID = "gridL";
	private static final String CONTEXT_TYPE_LOADLAZY_GRID = "gridLL";
	private static final String DOT_LOAD = ".load";
	private static final String DOT_INIT = ".init";
	private static final String DOT_LOAD_LAZY = ".loadlazy";
	
	private GridListener listener;
	private ArrayList<GridColumn> columns;
	private IGridModel model;
	private String title;
	private int rows;

	/**
	 * Default constructor
	 */
	public Grid() {
		super();
		setCssClass("wgtGrid");
		columns = new ArrayList<GridColumn>();
		listener = new GridListener();
		rows=10;
	}

	/**
	 * @param id Provided id for the list.
	 */
	public Grid(String id) {
		super(id);
		setCssClass("wgtGrid");
		columns = new ArrayList<GridColumn>();
		listener = new GridListener();
		rows=10;
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
		getPage().addWgtCSS("standard/jquery-ui.css");
		getPage().addWgtCSS("standard/ui.jqgrid.css");
		getPage().addControllerJS( JSResourceProcessor.GRID_JS );
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#draw(java.io.PrintWriter)
	 */
	protected void draw(PrintWriter out) {
		Table table = new Table();
		table.setID(getId());
		Div pager = new Div();
		pager.setID(getId() + "_pager");
		table.output(out); // write the stream
		pager.output(out); // write the stream

		getContext().processContextElement(getId());

		getContext().add(id4init(), getInitString(), CONTEXT_TYPE_INIT_GRID, IContext.STATUS_COMMAND);

		getContext().moveDown(id4load());
	}

	/**
	 * @param item to be added at the end of the list.
	 */
	public void addColumn(GridColumn col) {
		columns.add(col);
		if (isDrawn()) {
			redraw();
		}
	}

	/**
	 * @param item to be removed
	 */
	public void removeColumn(GridColumn col) {
		columns.remove(col);
		if (isDrawn()) {
			redraw();
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.ActionControl#dispatch(org.webguitoolkit.ui.controls.event.ClientEvent)
	 */
	public void dispatch(ClientEvent event) {
		// list was sorted
		String eventType = event.getType();

		if ("item_select".equals(eventType)) {
			String rowId = event.getParameter(0);
			listener.onSelect(this, rowId);
		}
		else if ("item_edited".equals(eventType)) {
			int rowId = Integer.parseInt( event.getParameter(0) );
			int parameterSize = event.getParameterSize();
			IDataBag bag = model.getBag(rowId);
			for( int i = 1; (i+1) < parameterSize; i=i+2 ){
				bag.setObject( event.getParameter(i), event.getParameter(i+1) );
			}
			listener.onEditRow(this, rowId, bag );
		}
		else if ("gll".equals(eventType)) {
			Hashtable<String, String> filters = new Hashtable<String, String>();

			int parameterSize = event.getParameterSize();
			int page=0;
			int rows=0;
			String sidx=""; 
			String sord="";
			for( int i = 0; (i+1) < parameterSize; i=i+2 ){
				String key = event.getParameter(i);
				String value = event.getParameter(i+1);
				System.out.println( key + " - " + value);
				if( "page".equals(key) ){
					page = Integer.parseInt(value);
					continue;
				}
				if( "rows".equals(key) ){
					rows = Integer.parseInt(value);
					continue;
				}
				if( "sidx".equals(key) ){
					sidx = value;
					continue;
				}
				if( "sord".equals(key) ){
					sord = value;
					continue;
				}
				for( GridColumn c : columns ){
					if( key.equals( c.getProperty() ) ){
						filters.put(event.getParameter(i), event.getParameter(i+1) );
					}
				}
			}
			loadJSON( page, rows, sidx, sord, filters );
		}

	}

	/**
	 * @see org.webguitoolkit.ui.controls.list.ISortableList#setListener(org.webguitoolkit.ui.controls.list.IListListener)
	 */
	public void setListener(GridListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("Listener can not be NULL");
		this.listener = listener;
	}

	public void setModel(IGridModel model) {
		this.model = model;
	}

	public void loadJSON(int page, int rows, String sortColumn, String sortDirection, Hashtable<String, String> filters) {
		
		int start = rows * page - rows;
		
		int totalCount = model.getCount( filters );
		List<IDataBag> modelData = model.getData(sortColumn,sortDirection,filters, start , rows );

		int count = modelData.size();
		int totalPages = 0;
		if (totalCount > 0) {
			totalPages = (int)Math.ceil(totalCount / rows);
		}
		if (page > totalPages)
			page = totalPages;


		JSONObject root = new JSONObject();
		root.addAttribute("total", totalPages );
		root.addAttribute("page", page );
		root.addAttribute("records",  totalCount );
		JSONArray array = new JSONArray();
		root.addAttribute("rows", array );
		
		for (int i = 0; i < modelData.size(); i++) {
			JSONObject row = new JSONObject();
			row.addAttribute("id", i+start );
			JSONArray cellArray = new JSONArray();
			row.addAttribute("cell", cellArray );

			IDataBag bag = modelData.get(i);
			for (GridColumn col : columns)
				cellArray.addValue(new JSONSimpleValue(""+bag.get(col.getProperty())));
			array.addValue(row);
		}
		
		StringBuffer out = new StringBuffer();
		root.writeTo(out);
		getContext().add(id4loadLazy(), out.toString(), CONTEXT_TYPE_LOADLAZY_GRID, IContext.STATUS_COMMAND);
	}

	/**
	 * @return
	 */
	private String id4loadLazy() {
		return getId() + DOT_LOAD_LAZY;
	}

//	public void load() {
//		List<IDataBag> data = model.getData();
//		
//		JSONArray tData = new JSONArray();
//		for (IDataBag bag : data) {
//			JSONObject row = new JSONObject();
//			for (GridColumn col : columns) {
//				row.addAttribute(col.getProperty(), "" + bag.get(col.getProperty()) );
//			}
//			tData.addValue(row);
//		}
//
//		StringBuffer buff = new StringBuffer();
//		tData.writeTo(buff);
//		getContext().add(id4load(), buff.toString(), CONTEXT_TYPE_LOAD_GRID, IContext.STATUS_COMMAND);
//	}

	private String getInitString() {
		JSONObject init = new JSONObject();
		init.addAttribute("datatype", new JSONFunction("function(postdata) { gridLoadEvent('"+getId()+"', postdata ); }"));
		
		
		JSONArray colArray = new JSONArray();
		init.addAttribute("colNames", colArray);
		for (GridColumn col : columns) {
			colArray.addValue( new JSONSimpleValue( col.getTitle() ) );
		}
		
		JSONArray colModelArray = new JSONArray();
		init.addAttribute("colModel", colModelArray);
		boolean isEditable = false;
		for (GridColumn col : columns) {
			JSONObject modelObject = new JSONObject();
			modelObject.addAttribute("name", col.getProperty() );
			modelObject.addAttribute("index", col.getProperty() );
			modelObject.addAttribute("width", col.getWidth() );
			if( col.isEditable() ){
				modelObject.addAttribute("editable", true );
				isEditable = true;
			}
			modelObject.addAttribute("sortable", col.isSortable() );
				
			colModelArray.addValue(modelObject);
		}
		
		init.addAttribute("pager", "#" + getId() + "_pager");
		init.addAttribute("rowNum", 10);
		init.addAttribute("rowList", new int[]{10, 20, 30});
		init.addAttribute("sortname", "id");
		init.addAttribute("sortorder", "desc");
		init.addAttribute("sortable", true);
		init.addAttribute("height", "100%");
		init.addAttribute("viewrecords", true);
//		init.addAttribute("viewsortcols", true);
		init.addAttribute("caption", getTitle());
		init.addAttribute("onPaging", new JSONFunction("function( pgButton ){ console.debug('onPaging '+pgButton ) }"));
		if( !isEditable ){
			init.addAttribute("onSelectRow", new JSONFunction("function(id){ var para = new Array();para[0]=id;eventParam('" + getId() + "',para );fireWGTEvent('"
					+ getId() + "', 'item_select') }"));
		}
		else
			init.addAttribute("onSelectRow", new JSONFunction("function(id){if(id && id!==wgtGridLastSelect["+ getId() +"]){jQuery('#" + getId() + "').restoreRow(wgtGridLastSelect["+ getId() +"]); wgtGridLastSelect["+ getId() +"]=id; } jQuery('#" + getId() + "').editRow(id, true, null, null, 'clientArray', null, function(rowId){ gridEditEvent('"+getId()+"', rowId ); } );}"));
		
		StringBuffer initString = new StringBuffer();
		init.writeTo(initString);
		return initString.toString();

	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitleKey(String titleKey) {
		this.title = TextService.getString(titleKey);
	}

	private String id4init() {
		return getId() + DOT_INIT;
	}

	private String id4load() {
		return getId() + DOT_LOAD;
	}
}
