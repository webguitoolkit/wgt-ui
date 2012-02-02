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
package org.webguitoolkit.ui.controls.table;

/**
 * Interface for Simplified Table. <br>
 * CSS classes : wgtTable, wgtTableHeader, wgtTableColumnHeaderCell, rowCssClass, rowCssClassAlternate, wgtTDNormal
 * 
 * @author Peter
 * 
 */
public interface ISimpleTable extends ITable {

	/**
	 * set if view port should be fixed
	 * 
	 * @param fixedViewPort true = fixed
	 */
	void setFixedViewPort(boolean fixedViewPort);

	/**
	 * height of the view port, only used if view port is fixed
	 * 
	 * @param viewPortHeight value
	 */
	void setViewPortHeight(String viewPortHeight);

	/**
	 * width of the view port, only used if view port is fixed
	 * 
	 * @param viewPortWidth value for width
	 */
	void setViewPortWidth(String viewPortWidth);

	/**
	 * view port id is the id of the surrounding div, can be used for css styling
	 * 
	 * @param viewPortID
	 *            of the view port
	 */
	void setViewPortID(String viewPortID);

}

