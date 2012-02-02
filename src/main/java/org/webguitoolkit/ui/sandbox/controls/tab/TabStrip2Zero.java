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
package org.webguitoolkit.ui.sandbox.controls.tab;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.tab.Tab;
import org.webguitoolkit.ui.http.ResourceServlet;


/**
 * @author bkl
 * 
 * add addWgtJS("glossbutton.js"); for page required!
 *
 */
public class TabStrip2Zero extends StandardTabStrip {
	
	private List tabIds = null;

	public TabStrip2Zero(){
		super();
	}
	
	
	protected void startHTML(PrintWriter out) {
		
		tabIds = new ArrayList();

        Table table = new Table();
        table.setClass( "wgtTabStrip20" );
        table.setCellPadding( 0 );
        table.setCellSpacing( 0 );
        table.setID( getId() );
        
        TR contentRow = new TR();
        table.addElement( contentRow );
        
        for (int i=0;i<getTabCount();i++) {
        	
        	TD td = new TD();
        	td.setID( getId()+".tri"+i );
        	if (i!=0) {
        		//td.setClass( "wgtTabBetweenUnselected" );
        	} else {
        		//td.setClass( "wgtTabFirstUnselected" );
        	}
        	contentRow.addElement( td );
        	
            Tab tab = (Tab) getTab(i);
            td = new TD();
            td.setID( id4Label(i) );
            td.setClass( "wgtTabUnselected" );
            if( StringUtils.isNotEmpty( tab.getTooltip() ) )
            	td.setTitle( tab.getTooltip() );
            td.setOnClick( getTabChangeJS( i ) );
            contentRow.addElement( td );
            
            Table innerTable = new Table();
            innerTable.setCellSpacing( 0 );
            innerTable.setCellPadding( 0 );
            td.addElement( innerTable );
            innerTable.setBorder( 0 );

            TR topTr = new TR();
            innerTable.addElement( topTr );
            TR innerTr = new TR();
            innerTable.addElement( innerTr );


            TD leftTD = new TD();
            TD leftTopTD = new TD();
            TD topTD = new TD();
            TD rightTD = new TD();
            TD rightTopTD = new TD();

			topTr.addElement(leftTopTD);
			topTr.addElement(topTD);
			topTr.addElement(rightTopTD);
			innerTr.addElement(leftTD);
			
			leftTopTD.setClass("btn_lt");
			topTD.setClass("btn_ct");
			rightTopTD.setClass("btn_rt");
			leftTD.setClass("btn_lm");
			rightTD.setClass("btn_rm");

			if (StringUtils.isNotEmpty(tab.getTabIconSrc())) {
				// tab image
				TD innerTd = new TD();
				innerTr.addElement(innerTd);

				IMG image = new IMG();
				image.setSrc(tab.getTabIconSrc());
				innerTd.addElement(image);
			}
			if (StringUtils.isNotEmpty(tab.getLabel())) {
				// tab label
				TD innerTd = new TD();
				
				innerTd.setID(getId()+"_ts2id_" + tab.getId());
				tabIds.add(getId()+"_ts2id_" + tab.getId());
				
				//innerTd.setClass("wgtTabLabel");
				innerTd.setClass("btn_cm");
				innerTd.addElement(tab.getLabel());
				innerTr.addElement(innerTd);
				innerTd.setStyle("padding-left: 3px; padding-right: 3px; padding-bottom: 2px;");
			}
			innerTr.addElement(rightTD);
        }
        TD td = new TD();
        td.setID( getId()+".last" );
        //td.setClass( "wgtTabLastUnselected" );
        contentRow.addElement( td );
        
        table.output( out );
        initJaveScript();
	}
	
	public void initJaveScript() {
		String jscommand = "";
//		String js = "function initTS2Zero(ts2id){"
//			+ "jQuery('#'+ts2id).mouseover( function(){"
//			+ "jQuery(this).addClass('btn_cm_ov');" + "});"
//			+ "jQuery('#'+ts2id).mouseout( function(){"
//			+ "jQuery(this).removeClass('btn_cm_ov');"
//			+ "jQuery(this).removeClass('btn_cm_cl');" + "});"
//			+ "jQuery('#'+ts2id).bind('mousedown', function(){"
//			+ "jQuery(this).removeClass('btn_cm_ov');"
//			+ "jQuery(this).addClass('btn_cm_cl');" + "});"
//			+ "jQuery('#'+ts2id).bind('mouseup', function(){"
//			+ "jQuery(this).addClass('btn_cm_ov');"
//			+ "jQuery(this).removeClass('btn_cm_cl');" + "});" + "}";
		
			Iterator tabids = tabIds.iterator();
			while (tabids.hasNext()) {
				String object = (String) tabids.next();
				jscommand += "initGlossButton('"+ object+"');";
			}
		
//			getContext().sendJavaScript("initTS2Zero_js", js + ";" + jscommand);
			getContext().sendJavaScript("initTS2Zero_js", jscommand);
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.form.Button#init()
	 */
	protected void init() {
		super.init();
		getPage().addHeaderJS( ResourceServlet.SERVLET_URL_PATTERN + "/glossbutton.js");
	}
	

}
