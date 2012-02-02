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
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.Label;
import org.webguitoolkit.ui.controls.form.Select;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.form.Textarea;
import org.webguitoolkit.ui.controls.layout.TableLayout;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.tab.Tab;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.util.conversion.ConvertUtil;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.controls.util.validation.ValidatorUtil;
import org.webguitoolkit.ui.http.ResourceServlet;


/**
 * <pre>
 * The LogViewer is used to show the output of the DWRAppender.
 * </pre>
 */
public class LogViewer extends Page implements IActionListener{

	public static final int maxEntries = 100;
	
	static List<IDataBag> theLog = Collections.synchronizedList(new ArrayList<IDataBag>());	

	private Table logTable;
	private Button buttonReload;
	private Button buttonClear;
	private Compound detailCompound;
	private Select selectLevel;
	private Compound logCompound;	
	private static final String ALL = "ALL";
	private static final String DEBUG = "DEBUG";
	private static final String INFO = "INFO";
	private static final String WARN = "WARN";
	private static final String ERROR = "ERROR";
	private static final String FATAL = "FATAL";
	private static final String OFF = "OFF";
	private static final String TRACE="TRACE";
	


	protected void pageInit() {		
		addHeaderCSS("./"+ ResourceServlet.SERVLET_URL_PATTERN+"/standard_theme.css");
		
		TableLayout layoutLog=getFactory().newTableLayout(this);
		Canvas canvas=getFactory().newCanvas(layoutLog);
		logCompound = getFactory().newCompound(canvas);
		TableLayout logLayout=getFactory().newTableLayout(logCompound);	
		Label lb=getFactory().newLabel(logLayout, "Log-Category:");		
		Text text=getFactory().newText(logLayout, "logCategory",lb);
		text.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		logLayout.newLine();
		
		Label lblLevel=getFactory().newLabel(logLayout, "Log-Level:");		
		selectLevel=getFactory().newSelect(logLayout, "logLevel");
		selectLevel.setDescribingLabel(lblLevel);
		selectLevel.getDefaultModel().setSingleValueList(new String[]{OFF,INFO,DEBUG,WARN,ERROR,FATAL,TRACE,ALL});
		selectLevel.loadList();
		logLayout.newLine();
		getFactory().newButton(logLayout, null, "Save", "save", new SaveLogLevelListener());
		
		
		logTable = getFactory().newTable(this, "Log", 20 );		
		getFactory().newTableColumn( logTable, "Thread Name", "threadName", true);
		TableColumn logLevelCol = getFactory().newTableColumn( logTable, "Log Level", "loglevel", false);
		logLevelCol.setWidth("70px");
		logLevelCol.setConverter( ConvertUtil.NUM0_CONVERTER );
		TableColumn loggerNameCol = getFactory().newTableColumn( logTable, "Logger Name", "loggerName", true);
		loggerNameCol.setWidth("200px");
		getFactory().newTableColumn( logTable, "Message", "renderedMessage", true);

		buttonReload = getFactory().newButton(this, null, "reload", "reload", this);
		buttonClear = getFactory().newButton(this, null, "clear", "clear", this);

		StandardTabStrip tabStrip = getFactory().newTabStrip( this );
		//tabStrip.setStyle("width: 1400px;");
		tabStrip.getStyle().addWidth(1400, Style.PIXEL);

		
		Tab detailTab = getFactory().newTab( tabStrip, "Details");
		
		detailCompound = getFactory().newCompound( detailTab );
		
		TableLayout layout = getFactory().newTableLayout( detailCompound );
		
		Textarea messageArea = getFactory().newTextarea( layout, "renderedMessage" );
		//messageArea.setStyle( "width: 1000px;heigth: 500px;" );
		messageArea.getStyle().addWidth(1000, Style.PIXEL);
		messageArea.getStyle().addHeight(500, Style.PIXEL);

		messageArea.setColumns( 600 );
		messageArea.setRows( 20 );

		layout.newLine();
		
		Textarea traceArea = getFactory().newTextarea( layout, "trace" );
		//traceArea.setStyle( "width: 1000px;" );
		traceArea.getStyle().addWidth(1000, Style.PIXEL);

		traceArea.setColumns( 200 );
		traceArea.setRows( 20 );
		pageLoad();
	}
	

	protected void pageLoad() {
		logTable.getDefaultModel().setTableData(theLog);

		logTable.setListener(new AbstractTableListener() {
			public void onRowSelection(ITable table, int row) {
				detailCompound.setBag(table.getSelectedRow());
				detailCompound.load();
			}
		});
		
		WGTAppender.getInstance().setDelegate(this);
		
		logTable.reload();
	}

	protected String title() {
		return "LogViewer";
	}

	protected void append(LoggingEvent le) {
		while (theLog.size()>maxEntries) {
			// make space..
			theLog.remove(theLog.size()-1);
		}
		
			IDataBag bag = WebGuiFactory.getInstance().createDataBag(le);
			theLog.add(0, bag);
			
			// convert additional data...
			bag.addProperty("loglevel", le.getLevel().toString());
			bag.addProperty("trace", StringUtils.join(le.getThrowableStrRep(), "\r\n" ));
			//logTable.reload();
	
	}
	
	protected void close(){
		theLog.clear();
	}

	public void onAction(ClientEvent event) {
		IBaseControl control = event.getSource();
		if (control == buttonClear) {
			theLog.clear();
		}
		if (control == buttonReload) {
			logTable.reload();			
		}
	}
	
	private class SaveLogLevelListener implements IActionListener{
		

		public void onAction(ClientEvent event) {
			LogDTO log=new LogDTO();
			
			logCompound.setBag(WebGuiFactory.getInstance().createDataBag(log));
			logCompound.save();
			IDataBag bag=logCompound.getBag();
			bag.save();			
			String categoryName=log.getLogCategory();			
			if(StringUtils.isBlank(categoryName)){				
				return;
			}
			Logger logger=LogManager.exists(categoryName);
			
			if(logger==null){
				logger=Logger.getLogger(categoryName);
			}			
			String level=selectLevel.getValue();
			if(StringUtils.equals(ALL, level)){
				logger.setLevel(Level.ALL);	
			}else if(StringUtils.equals(DEBUG, level)){
				logger.setLevel(Level.DEBUG);
			}else if(StringUtils.equals(INFO, level)){
				logger.setLevel(Level.INFO);
			}else if(StringUtils.equals(WARN, level)){
				logger.setLevel(Level.WARN);
			}else if(StringUtils.equals(ERROR,level)){
				logger.setLevel(Level.ERROR);
			}else if(StringUtils.equals(FATAL , level)){
				logger.setLevel(Level.FATAL);
			}else if(StringUtils.equals(OFF, level)){
				logger.setLevel(Level.OFF);
			}else{
				logger.setLevel(Level.TRACE);
			}
			
		logCompound.getPage().sendInfo("Save successed.");	
			
		}
		
	}
	

	public Compound getLogCompound() {
		return logCompound;
	}
	

}
