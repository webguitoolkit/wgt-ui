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

import java.io.Serializable;

import org.jfree.chart.JFreeChart;

/**
 * <p>
 * The chart component is only calling this one method to get the chart. It is triggered if load() is called. To get a tooltip
 * JFreechart must be setup according to Chapter 11 in the JFreeChart documentation.
 * </p>
 * 
 * @author Arno
 */
public interface IChartModel extends Serializable {
	/**
	 * Get the chart fully loaded with data and options. the component will only call the draw method.
	 * 
	 * @return
	 */
	public JFreeChart getChart();

}
