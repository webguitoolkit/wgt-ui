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
package org.webguitoolkit.ui.tools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.DWRCallback;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.dialog.DialogUtil;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabListener;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.tab.Tab;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.DefaultTableModel;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.ITableListener;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.tree.DefaultTreeModel;
import org.webguitoolkit.ui.controls.tree.DefaultTreeNode;
import org.webguitoolkit.ui.controls.tree.Tree;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.http.ResourceServlet;
import org.webguitoolkit.ui.util.Guid;

/**
 * <pre>
 * The Log page is used to monitor the DWR calls.
 * </pre>
 */
public class Log extends Page implements IActionListener, ITabListener {

	private final WebGuiFactory factory = WebGuiFactory.getInstance();
	private Table requestTable;
	private Tree componentTree;
	private Text searchField;
	private Compound compOutDetail;
	private Table outboundTable;
	private Table inboundTable;
	private StandardTabStrip detailStrip;

	private Button reloadButton;
	private Button zeroButton;
	private Button recreateDB;
	private Button debugOn;
	private Button prop;
	private Button id4t;
	private Button searchButton;

	@Override
	protected void pageInit() {
		addHeaderCSS("./" + ResourceServlet.SERVLET_URL_PATTERN + "/standard_theme.css");

		// List of Ajax requests
		requestTable = factory.newTable(this, "Ajax Requests", 5);
		requestTable.setDisplayMode(Table.DISPLAY_MODE_SCROLL_BUTTONS);

		requestTable.setWidth("1000px");
		ITableListener tableListener = new DWRLogListener();
		requestTable.setListener(tableListener);

		factory.newTableColumn(requestTable, "time", "time", true);
		factory.newTableColumn(requestTable, "duration", "duration", true);
		factory.newTableColumn(requestTable, "source", "source", true);
		factory.newTableColumn(requestTable, "type", "type", true);
		factory.newTableColumn(requestTable, "page.name", "page name", true);
		factory.newTableColumn(requestTable, "name", "name", true);

		// Buttons
		reloadButton = factory.newButton(this, null, "reload", "reloadButton", this);
		zeroButton = factory.newButton(this, null, "Zero Sequences", "zeroButton", this);
		recreateDB = factory.newButton(this, null, "recreate Database", "recreateDB", this);
		debugOn = factory.newButton(this, null, "debug on/off", "debugOn", this);
		prop = factory.newButton(this, null, "properties as Tooltip", "prop", this);
		id4t = factory.newButton(this, null, "id as Tooltip", "id4t", this);

		// TabStrip
		detailStrip = factory.newTabStrip(this);
		detailStrip.setListener(this);
		// detailStrip.setStyle("width: 800px; height: 800px");
		detailStrip.getStyle().addWidth(800, Style.PIXEL);
		detailStrip.getStyle().addHeight(800, Style.PIXEL);

		// inbound tab
		Tab inboundTab = factory.newTab(detailStrip, "inbound Context");

		inboundTable = factory.newTable(inboundTab, "Inbound Context", 25);
		inboundTable.setDisplayMode(Table.DISPLAY_MODE_SCROLL_BUTTONS);

		inboundTable.setWidth("1000px");

		factory.newTableColumn(inboundTable, "cssId", "cssId", true);
		factory.newTableColumn(inboundTable, "value", "value", true);
		factory.newTableColumn(inboundTable, "type", "type", true);
		factory.newTableColumn(inboundTable, "cssId", "cssId", true);

		// outbound tab
		Tab outboundTab = factory.newTab(detailStrip, "outbound Context");

		outboundTable = factory.newTable(outboundTab, "Outbound Context", 25);
		outboundTable.setDisplayMode(Table.DISPLAY_MODE_SCROLL_BUTTONS);

		outboundTable.setWidth("1000px");
		outboundTable.setListener(tableListener);

		factory.newTableColumn(outboundTable, "cssId", "cssId", true);
		factory.newTableColumn(outboundTable, "value", "value", true);
		factory.newTableColumn(outboundTable, "type", "type", true);
		factory.newTableColumn(outboundTable, "cssId", "cssId", true);

		compOutDetail = factory.newCompound(outboundTab);

		Text stacktrace = factory.newText(compOutDetail, "stacktrace");
		stacktrace.setEditable(false);

		// tree tab
		Tab componentTreeTab = factory.newTab(detailStrip, "Component Tree");

		searchField = factory.newText(componentTreeTab, "cSearch");

		searchButton = factory.newButton(componentTreeTab, null, "Search", "Search", this);

		componentTree = factory.newTree(componentTreeTab);
		// componentTree.setStyle("height: 100%;");
		componentTree.getStyle().addHeight(100, Style.PERCENT);

		reload();
	}

	public void reload() {
		List con = DWRCallback.contextLog;
		((DefaultTableModel)requestTable.getModel()).setTableData(con);
		requestTable.reload(0);
	}

	@Override
	protected String title() {
		return "Control Log";
	}

	public void onAction(ClientEvent event) {
		IBaseControl control = event.getSource();
		if (reloadButton == control) {
			reload();
		}
		else if (zeroButton == control) {
			Guid.reset();
		}
		else if (debugOn == control) {
			DWRCallback.debug = !DWRCallback.debug;
			Context.debugTrace = !Context.debugTrace;
			getContext().add(event.getSourceId(), Context.debugTrace ? "debug off" : "debug on", IContext.TYPE_VAL,
					IContext.STATUS_NOT_EDITABLE);
		}
		else if (recreateDB == control) {
			// GeneratePresentationData.generateData();
		}
		else if (detailStrip == control) {
			StandardTabStrip strip = (StandardTabStrip)event.getSource();
			strip.selectTab(Integer.parseInt(event.getParameter(0)));
			createTree(requestTable.getSelectedRow());
		}
		else if (searchButton == control) {
			String searchText = searchField.getValue();
			if (searchText == null)
				return;
			DefaultTreeNode root = (DefaultTreeNode)((DefaultTreeModel)componentTree.getModel()).getRootElement();
			selectNodeById(searchText, root);
		}
		else if (prop == control) {
			// place every components property as tooltip where possible
			visitCurrentBody(new BaseControl.Visitor() {
				public boolean visit(BaseControl host) {
					if (host instanceof FormControl) {
						FormControl formi = (FormControl)host;
						final String property = formi.getProperty();
						if (property != null) {
							formi.getPage().getContext().setAttribute(formi.getId(), "title", property);
						}
					}
					return true;
				}

			});
		}
		else if (id4t == control) {
			visitCurrentBody(new BaseControl.Visitor() {
				public boolean visit(BaseControl host) {
					// make id tooltip
					host.setTooltip(host.getId());
					return true;
				}
			});
		}
	}

	protected void visitCurrentBody(Visitor visitor) {
		IDataBag selectedRow = requestTable.getSelectedRow();
		if (selectedRow == null) {
			DialogUtil.sendMessage(this, "Please select from the Table above.", DialogUtil.INFO, null);
			return; // send nice message...
		}
		Page page2Debug = (Page)selectedRow.get("page");
		page2Debug.registerListener(ServerEvent.EVENT_POSTDISPATCH, new BodySearch(visitor));
		DialogUtil.sendMessage(this, "After send one more request on the selected page " + "you should see the result", DialogUtil.INFO,
				null);
	}

	private void selectNodeById(String searchText, DefaultTreeNode node) {
		BaseControl cont = (BaseControl)node.getDataObject();
		if (cont != null && searchText.equalsIgnoreCase(cont.getId())) {
			// show this to the user as searchresult.
			componentTree.selectNode(node);
		}
		else {
			// lets see if we find it in the subtree.
			for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
				DefaultTreeNode child = (DefaultTreeNode)it.next();
				selectNodeById(searchText, child);
			}
		}
	}

	private void createTree(IDataBag eventGeneratingBag) {
		// load the component tree.
		// create the tree according to this structure
		DefaultTreeModel model = (DefaultTreeModel)componentTree.getModel();
		DefaultTreeNode root;
		if (eventGeneratingBag == null) {
			root = new DefaultTreeNode();
		}
		else {
			root = toTree(((BaseControl)eventGeneratingBag.getDelegate()).getPage());
		}
		model.setRootElement(root);
		model.setRootVisible(true);
		componentTree.load();
	}

	public DefaultTreeNode toTree(BaseControl cont) {
		DefaultTreeNode bridge = new DefaultTreeNode();
		String clazzName = cont.getClass().getName();
		bridge.setCaption(clazzName.substring(clazzName.lastIndexOf('.') + 1) + "," + cont.getId());
		bridge.setExpanded(false);
		bridge.setDataObject(cont);
		if (cont.getChildren() != null) {

			for (Iterator it = cont.getChildren().iterator(); it.hasNext();) {
				BaseControl contchild = (BaseControl)it.next();
				DefaultTreeNode bridgeChild = toTree(contchild);
				bridge.getChildren().add(bridgeChild);
			}
		}
		return bridge;
	}

	class DWRLogListener extends AbstractTableListener {

		protected Table reqTable;

		public DWRLogListener() {
			super();
		}

		/**
		 * line selected in master table load inoput and outbound table with contexts
		 */
		@Override
		public void onRowSelection(ITable table, int row) {
			if (table == requestTable) {
				onRowContextTable(table, row);
			}
			if (table == outboundTable) {
				onRowOutTable(table, row);
			}

		}

		private void onRowOutTable(ITable table, int row) {
			// show details to the context element
			ContextElement ce = (ContextElement)((DefaultTableModel)table.getModel()).getFilteredList().get(row);
			// reference to the context only throug other table...
			IContext con = (IContext)requestTable.getSelectedRow().get("page.context");
			String trace = con.getCETrace(ce);
			// System.out.println("Log Trace (No Error) ------------------------------");
			// System.out.println(trace);
			compOutDetail.getBag().addProperty("stacktrace", trace);
			compOutDetail.load();
		}

		private void onRowContextTable(ITable table, int row) {
			// get the dataobject form the table
			DataBag eventGeneratingBag = (DataBag)((DefaultTableModel)table.getModel()).getFilteredList().get(row);
			ContextElement[] inb = (ContextElement[])eventGeneratingBag.get("inbound");
			((DefaultTableModel)inboundTable.getModel()).setTableData(Arrays.asList(inb));
			inboundTable.reload(0);

			ContextElement[] out = (ContextElement[])eventGeneratingBag.get("outbound");
			((DefaultTableModel)outboundTable.getModel()).setTableData(Arrays.asList(out));
			outboundTable.reload(0);

			createTree(eventGeneratingBag);
		}

	}

	public static class BodySearch implements IServerEventListener {
		protected BaseControl.Visitor salesMan;

		public BodySearch(Visitor salesMan) {
			super();
			this.salesMan = salesMan;
		}

		public void handle(ServerEvent event) {
			// should be done only once...
			Page myBody = event.getSource().getPage();
			myBody.removeListener(event.getTypeAsInt(), this);
			// now we can actually do the charge...
			myBody.travelDFS(salesMan);
		}
	}

	public boolean onTabChange(ITab old, ITab selected, ClientEvent event) {
		createTree(requestTable.getSelectedRow());
		return true;
	}

}
