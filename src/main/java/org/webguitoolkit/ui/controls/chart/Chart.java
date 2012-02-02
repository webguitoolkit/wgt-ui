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
package org.webguitoolkit.ui.controls.chart;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.imagemap.ImageMapUtilities;
import org.jfree.chart.imagemap.OverLIBToolTipTagFragmentGenerator;
import org.jfree.chart.servlet.ServletUtilities;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.util.JSUtil;


/**
 * <pre>
 * Displays a chart diagram generated by JFeeChart.
 * </pre>
 */
public class Chart extends BaseControl implements IChart {
	// height of picture
    protected int height;
    // width of picture
    protected int width;
    // JFreechart model
    protected IChartModel model;

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public Chart() {
		super();
	}
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
     * @param id unique HTML id
     */
	public Chart( String id ) {
		super( id );
	}
	/**
	 * only generating the HTML to hold the picture and the image map
	 */
	protected void endHTML(PrintWriter out) {
		out.print("<DIV "+JSUtil.atId(id4Map()) +">");
		out.print("</DIV>");
		// here goes the picture, src will be set later
		// MH 06.05.2009: do not leave src empty
		// empty src caused problems in SupplyCare and double initial load
		out.print("<IMG "+JSUtil.atId(id4Img())+ " src='images/1.gif' border=\"0\" />");

	}
	/**
	 * the context id of the image element
	 */
	protected String id4Img() {
		return getId()+".img";
	}
	
	/**
	 * the context id of the map element
	 */
	protected String id4Map() {
		return getId()+".map";
	}

	protected void init() {
		ServletUtilities.setTempFilePrefix("tmp");
		load();
	}
	
	/**
	 * generate the image an send the url to the client
	 */
	public void load() {
		HttpSession session = Page.getServletRequest().getSession(true);
        ChartRenderingInfo info = new ChartRenderingInfo();
        JFreeChart chart = getModel().getChart();
        if (chart==null) return; // happen for example in the init process
        
		try {
			String filename = ServletUtilities.saveChartAsPNG(chart, getWidth(), getHeight(), info, session);
			filename = URLEncoder.encode(filename, "UTF-8");
	        String mapName = id4Map() +filename+ ".name";
			String imagemap = ImageMapUtilities.getImageMap(mapName, info, new OverLIBToolTipTagFragmentGenerator(), null); // no urls	

			// transfer image map to clinet
			getContext().add(id4Map(), imagemap, IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
			// the img-tag to use the map
			getContext().add(id4Img()+".usemap", "#"+mapName, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
			// transfer src- atribute to client

			String imageUrl = "Chart/DisplayChart?filename="+filename;
			getContext().add(id4Img() + ".src", imageUrl, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WGTException(e);
		}
	}

	/**
	 * image height in px
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * image height in px
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * image width in px
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * image width in px
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.chart.IChart#setModel(org.webguitoolkit.ui.controls.chart.IChartModel)
	 */
	public void setModel(IChartModel model) {
		this.model = model;
	}
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.chart.IChart#getModel()
	 */
	public IChartModel getModel() {
		if (model == null) {
			model = new DefaultChartModel();
		}
		return model;
	}
	
	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.chart.IChart#setVisible(boolean)
	 */
	public void setVisible(boolean vis) {
        getContext().add(id4Img()+IContext.DOT_VIS, Boolean.toString(vis), IContext.TYPE_VIS, IContext.STATUS_NOT_EDITABLE);
		getContext().add(id4Map(), "", IContext.TYPE_HTML, IContext.STATUS_NOT_EDITABLE);
	}

}
