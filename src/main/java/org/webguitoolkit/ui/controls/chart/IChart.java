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

import org.webguitoolkit.ui.controls.IBaseControl;

/**
 * <h1>Interface for the Chart Control</h1>
 * <p>
 * Defines the interface for the Chart control. This Chart is based on a
 * JFreeChart therefore the JFreeChart model has to be set.
 * </p>
 * <b>Creation of a Chart Control</b><br>
 * <pre>
 * 	// create a dataset for JFreeChart...
 *	DefaultPieDataset dataset = new DefaultPieDataset();
 *	dataset.setValue("WGT", 30);
 *	dataset.setValue("Rap", 19);
 *	dataset.setValue("GWT", 51);
 *		
 *	// create pie chart 
 *	JFreeChart jfchart = ChartFactory.createPieChart(
 *		"GUI Toolkits",	dataset,
 *		true, // legend
 *		true, // tooltips
 *		false // URLs
 *	);		
 *		
 *	IChart chart = factory.createChart(layout);
 *	IChartModel model = chart.getModel();
 *	((DefaultChartModel) model).setChart(jfchart);
 *
 *	// set the width and heights of the chart image
 *	chart.setHeight(250);
 *	chart.setWidth(250);
 *		
 *	// send the chart url to the frontend
 *	chart.load();
 * <pre>
 * 
 * <b>Event handling</b><br>
 * IChart elements can't trigger events, therefore no listeners can be added.<br>
 * <p/>
 * <b>CSS classes :</b> none
 * 
 * @author Peter
 * @author Lars 
 */
public interface IChart extends IBaseControl {

	/**
	 * sets the model that provides the chart
	 * @param model the model of the chart
	 */
	void setModel(IChartModel model);

	/**
	 * get the model in a lazy mode, create from class modelClass or use
	 * DefaultChartModel
	 * @return the model
	 */
	IChartModel getModel();

	/**
	 * image height in pixel
	 * 
	 * @param height pixel heights of the chart image
	 */
	void setHeight(int height);

	/**
	 * image width in pixel
	 * 
	 * @param width pixel widths of the chart image	 
	 */
	void setWidth(int width);

	/**
	 * generate the image an send the URL to the client
	 */
	void load();

}
