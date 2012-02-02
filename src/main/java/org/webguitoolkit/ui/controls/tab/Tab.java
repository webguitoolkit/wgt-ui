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

import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.layout.ILayout;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.style.Style;

/**
 * <pre>
 * A single tab.
 * 
 * It is like an inline canvas that is displayed when the tab is selected.
 * </pre>
 */
public class Tab extends Canvas implements IComposite, ITab {

	private String headerImage = null;
	private String headerTooltip = null;
	private String headerLabel = null;
	private String headerCSSClass = null;
	private Style headerStyle = null;

	@Deprecated
	private boolean firstTab = false;
	@Deprecated
	private boolean lastTab = false;
	@Deprecated
	private boolean visible = true;

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
	 */
	public Tab() {
		super();
	}

	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public Tab(String id) {
		super(id);
	}

	/**
	 * @return parent TabStrip
	 */
	public TabStrip getTabStrip() {
		return ((TabStrip)getParent());
	}

	// @Override
	// public void redraw() {
	// super.redraw();
	// }

	/**
	 * @return source path of optional icon image
	 */
	public String getTabIconSrc() {
		return getHeaderImage();
	}

	public void setTabIconSrc(String tabIconSrc) {
		setHeaderImage(tabIconSrc);
	}

	public boolean isFirstTab() {
		return firstTab;
	}

	public boolean isLastTab() {
		return lastTab;
	}

	public void setFirstTab(boolean firstTab) {
		this.firstTab = firstTab;
	}

	public void setLastTab(boolean lastTab) {
		this.lastTab = lastTab;
	}

	/**
	 * @see com.endress.infoserve.wgt.controls.BaseControl#add(com.endress.infoserve.wgt.controls.BaseControl)
	 */
	@Override
	public void add(IBaseControl child) {
		super.add(child);
	}

	/**
	 * @return label of the tab
	 */
	public String getLabel() {
		return getHeaderLabel();
	}

	public void setLabel(String label) {
		setHeaderLabel(label);
	}

	public void setLabelKey(String labelKey) {
		setHeaderLabelKey(labelKey);
	}

	/**
	 * @param accordionHeaderSyle the accordionHeaderSyle to set
	 */
	public void setAccordionHeaderSyle(Style accordionHeaderSyle) {
		if (accordionHeaderSyle == null)
			setHeaderStyle(null);
		else
			setHeaderStyle(accordionHeaderSyle);
	}

	@Override
	public void setLayout(ILayout layout) {
		super.setLayout(layout);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#getImage()
	 */
	public String getHeaderImage() {
		return headerImage;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#setImage(java.lang.String)
	 */
	public void setHeaderImage(String image) {
		headerImage = image;
		if (getTabStrip() != null)
			getTabStrip().setHeaderImage(this, headerImage);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#getHeaderLabel()
	 */
	public String getHeaderLabel() {
		return headerLabel;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#getHeaderTooltip()
	 */
	public String getHeaderTooltip() {
		return headerTooltip;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#setHeaderLabel(java.lang.String)
	 */
	public void setHeaderLabel(String label) {
		headerLabel = label;
		if (getTabStrip() != null)
			getTabStrip().setHeaderLabel(this, headerLabel);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#setHeaderLabelKey(java.lang.String)
	 */
	public void setHeaderLabelKey(String labelKey) {
		setHeaderLabel(TextService.getString(labelKey));
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#setHeaderTooltip(java.lang.String)
	 */
	public void setHeaderTooltip(String tooltip) {
		headerTooltip = tooltip;
		if (getTabStrip() != null)
			getTabStrip().setHeaderTooltip(this, headerTooltip);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#setHeaderTooltipKey(java.lang.String)
	 */
	public void setHeaderTooltipKey(String tooltipKey) {
		setHeaderTooltip(TextService.getString(tooltipKey));
	}

	public void setHeaderCSSClass(String headerCSSClass) {
		this.headerCSSClass = headerCSSClass;
		if (getTabStrip() != null)
			getTabStrip().addHeaderCSSClass(this, headerCSSClass);
	}

	public void setHeaderStyle(Style headerStyle) {
		this.headerStyle = headerStyle;
		if (getTabStrip() != null)
			getTabStrip().setHeaderStyle(this, headerStyle);
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#getHeaderCSSClass()
	 */
	public String getHeaderCSSClass() {
		return headerCSSClass;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.tab.ITab#setHeaderStyle()
	 */
	public Style getHeaderStyle() {
		return headerStyle;
	}

}
