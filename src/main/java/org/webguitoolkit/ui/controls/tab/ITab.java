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
package org.webguitoolkit.ui.controls.tab;

import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * <h1>Interface for a Tab</h1>
 * <p>
 * The Tab is always associated with a {@link ITabStrip} to control its appearance. Its a one (TabStrip) to many (Tabs)
 * relationship. It is like an inline canvas that is displayed when the tab is selected on TabStrip.
 * </p>
 * <p>
 * <b>Creation of a TabStrip</b><br>
 * Please see {@link ITabStrip} for an example.
 * </p>
 * <p>
 * CSS classes : accordion-head, accordion-body, wgtTabPane, box, content, inner, footer
 * </p>
 * 
 * @author Peter
 * 
 */
public interface ITab extends ICanvas {
	/**
	 * Control if an Icon shall be shown with the Tab.
	 * 
	 * @param tabIconSrc path to image
	 * @deprecated
	 */
	@Deprecated
	void setTabIconSrc(String tabIconSrc);

	/**
	 * @param firstTab indicator for first Tab in TabStrip
	 * @deprecated
	 */
	@Deprecated
	void setFirstTab(boolean firstTab);

	/**
	 * @param lastTab indicator for last Tab in TabStrip
	 * @deprecated
	 */
	@Deprecated
	void setLastTab(boolean lastTab);

	/**
	 * @param label label text
	 * @deprecated
	 */
	@Deprecated
	void setLabel(String label);

	/**
	 * @param labelKey resource key
	 * @deprecated
	 */
	@Deprecated
	void setLabelKey(String labelKey);

	/**
	 * @param accordionHeaderSyle the accordionHeaderSyle to set
	 * @deprecated
	 */
	@Deprecated
	void setAccordionHeaderSyle(Style accordionHeaderSyle);

	/**
	 * @return the current label of the tab
	 * @deprecated
	 */
	@Deprecated
	String getLabel();

	/**
	 * @param label label text
	 */
	void setHeaderLabel(String label);

	/**
	 * @param labelKey resource key
	 */
	void setHeaderLabelKey(String labelKey);

	/**
	 * @return the current label of the tab
	 */
	String getHeaderLabel();

	/**
	 * get parental TabStrip
	 * 
	 * @return parent TabStrip
	 */
	ITabStrip getTabStrip();

	/**
	 * @param tooltip tooltip resource key
	 */
	void setHeaderTooltipKey(String tooltipKey);

	/**
	 * @param tooltip tooltip text
	 */
	void setHeaderTooltip(String tooltip);

	/**
	 * @param image image source
	 */
	void setHeaderImage(String image);

	/**
	 * @return the image source
	 */
	String getHeaderImage();

	/**
	 * @return the tooltip text
	 */
	String getHeaderTooltip();

	/**
	 * @param headerCSSClass css class of the header
	 */
	void setHeaderCSSClass(String headerCSSClass);

	/**
	 * @return the css class of the header
	 */
	String getHeaderCSSClass();

	/**
	 * @param headerStyle style class of the header
	 */
	void setHeaderStyle(Style headerStyle);

	/**
	 * @return the style class of the header
	 */
	Style getHeaderStyle();

}
