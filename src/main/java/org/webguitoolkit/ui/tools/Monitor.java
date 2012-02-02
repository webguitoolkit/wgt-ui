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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.webguitoolkit.ui.ajax.DWRController;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.Label;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.layout.TableLayout;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.tab.Tab;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil;
import org.webguitoolkit.ui.http.WGTFilter;


/**
 * <pre>
 * Monitor page
 * </pre>
 */
public class Monitor extends Page implements IActionListener {

	private Button bReload;
	private Button bCallGC;
	private ITable sessionTable;
	private Compound memInfoComp;
	private Compound componentsComp;
	
	protected void pageInit() {

		sessionTable = getFactory().createTable(this, "open Sessions", 10 );
		sessionTable.setListener( new SessionTableListener() );
		
		getFactory().createTableColumn(sessionTable, "Session", "id", false);
		ITableColumn creationTime = getFactory().createTableColumn(sessionTable, "Creation Time", "creationTime", false);
		creationTime.setConverter( ConvertUtil.DATE_TIME_CONVERTER );
		ITableColumn lastAccessedTime = getFactory().createTableColumn(sessionTable, "Last Accessed Time", "lastAccessedTime", false);
		lastAccessedTime.setConverter( ConvertUtil.DATE_TIME_CONVERTER );
		
		bReload = getFactory().newButton(this, null, "reload", "reload", this);
		bCallGC = getFactory().newButton(this, null, "call gc", "call gc", this);
		
		StandardTabStrip tabStrip = getFactory().newTabStrip( this );
		
		Tab memoryTab = getFactory().newTab( tabStrip, "Totals" );
		
		memInfoComp = getFactory().newCompound( memoryTab );
		
		TableLayout memInfoLayout = getFactory().newTableLayout( memInfoComp );
		
		Label labelAvailableProcessors = getFactory().newLabel(memInfoLayout, "availableProcessors@availableProcessors" );
		Text textAvailableProcessors = getFactory().newText( memInfoLayout, "availableProcessors", labelAvailableProcessors );
		memInfoLayout.newLine();
		
		Label labelMaxMemory = getFactory().newLabel(memInfoLayout, "maxMemory@maxMemory" );
		Text textMaxMemory = getFactory().newText( memInfoLayout, "maxMemory", labelMaxMemory );
		memInfoLayout.newLine();

		Label labelFreeMemory = getFactory().newLabel(memInfoLayout, "freeMemory@freeMemory" );
		Text textFreeMemory = getFactory().newText( memInfoLayout, "freeMemory", labelFreeMemory );
		memInfoLayout.newLine();

		Label labelTotalMemory = getFactory().newLabel(memInfoLayout, "totalMemory@totalMemory" );
		Text textTotalMemory = getFactory().newText( memInfoLayout, "totalMemory", labelTotalMemory );
		memInfoLayout.newLine();

		Label labelTotalNumComponents = getFactory().newLabel(memInfoLayout, "totalNumComponents@totalNumComponents" );
		Text textTotalNumComponents = getFactory().newText( memInfoLayout, "totalNumComponents", labelTotalNumComponents );
		memInfoLayout.newLine();

		Tab componentsTab = getFactory().newTab( tabStrip, "Details" );
		
		componentsComp = getFactory().newCompound( componentsTab );
		
		TableLayout componentsLayout = getFactory().newTableLayout( componentsComp );
		
		Label labelBodies = getFactory().newLabel(componentsLayout, "bodies@bodies" );
		Text textBodies = getFactory().newText( componentsLayout, "bodies", labelBodies );
		componentsLayout.newLine();
		
		Label labelComponents = getFactory().newLabel(componentsLayout, "components@Components" );
		Text textComponents = getFactory().newText( componentsLayout, "components", labelComponents );
		componentsLayout.newLine();
	}

	protected void pageLoad() {
		IDataBag bag = WebGuiFactory.getInstance().createDataBag(null);
		bag.addProperty("availableProcessors", new Integer(Runtime.getRuntime().availableProcessors()));
		bag.addProperty("maxMemory", new Long(Runtime.getRuntime().maxMemory()));
		bag.addProperty("freeMemory", new Long(Runtime.getRuntime().freeMemory()));
		bag.addProperty("totalMemory", new Long(Runtime.getRuntime().totalMemory()));
		bag.addProperty("totalNumComponents", new Integer(
				DWRController.getInstance().getComponents().size()));
		memInfoComp.setBag(bag);
		memInfoComp.load();

		List sessionBags = new ArrayList();
		for( Iterator iter = WGTFilter.getAllSessions().iterator(); iter.hasNext(); )
			sessionBags.add(WebGuiFactory.getInstance().createDataBag(iter.next()) );
		sessionTable.getDefaultModel().setTableData(sessionBags);
		sessionTable.reload();		
	}

	protected String title() {
		return "Monitor";
	}
	
	public void onAction(ClientEvent event) {
		IBaseControl control = event.getSource();
		if (control == bReload) {
			pageLoad();
		}
		else if (control == bCallGC ) {
			System.gc();
			pageLoad();
		}
	}

	class SessionTableListener extends AbstractTableListener{
		public void onRowSelection(ITable table, int row) {
			IDataBag bag = table.getRow(row);
			HttpSession userSession = (HttpSession) bag.getDelegate();
			 
			DWRController userController = (DWRController) userSession.getAttribute(DWRController.class.getName());
			// calculate bodies and component totals.
			bag.addProperty("components", new Integer(userController.getComponents().size()));
			
			// loop through map to get al bodies.
			int bodies = 0;
			for (Iterator it = userController.getComponents().values().iterator(); it.hasNext();) {
				BaseControl component = (BaseControl) it.next();
				if (component instanceof Page) {
					bodies++;
				}
			}
			bag.addProperty("bodies", new Integer(bodies));
			
			componentsComp.setBag(bag);
			componentsComp.load();
		}
	}

}
