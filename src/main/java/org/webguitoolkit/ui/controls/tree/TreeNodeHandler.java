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
package org.webguitoolkit.ui.controls.tree;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;

/**
 * <pre>
 * Implementation of the ITreeNodeHandler,
 * dependent on if there is a matchClass or a matchProperty is set the handler is applied.
 * </pre>
 */
public class TreeNodeHandler implements ITreeNodeHandler {

	public final static String DEFAULT_TYPE = "default";

	protected Class matchClass = null;
	protected String matchProperty = null;
	protected Object matchValue = null;

	protected String displayProperty = null; // get the caption from the dataObject's display property
	protected String tooltipProperty = null; // get the tooltip from the dataObject's tooltip property
	protected String[] images = null; // set of three images that are used for this type of node
	protected IContextMenu contextMenu = null; // sets the context menu for this nodeType
	protected boolean hasCheckboxes = false; // indicates if there are checkboxes for this node type
	protected String checkboxParameter = null; // if default tree node we can get the checkbox Parameter from the
												// dataObject
	protected ITreeListener listener = null;
	protected String[] childSelectors = null;
	protected String style = null;

	private boolean droppable;

	private boolean draggable;

	/**
	 * @param className
	 */
	public TreeNodeHandler() {
	}

	/**
	 * Handles object identified by its class
	 *
	 * @param matchClass
	 */
	public TreeNodeHandler(Class matchClass) {
		this.matchClass = matchClass;
	}

	/**
	 * Handles object identified by a property and its value
	 *
	 * @param property
	 * @param value
	 */
	public TreeNodeHandler(String property, Object value) {
		this.matchProperty = property;
		this.matchValue = value;
	}

	/**
	 *
	 * Handles object identified by its class and a property and its value
	 *
	 * @param property
	 * @param value
	 * @param matchClass
	 */
	public TreeNodeHandler(String property, Object value,Class matchClass) {
		this.matchProperty = property;
		this.matchValue = value;
		this.matchClass = matchClass;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#getDisplayProperty()
	 */
	public String getDisplayProperty() {
		return displayProperty;
	}

	public void setDisplayProperty(String displayProperty) {
		this.displayProperty = displayProperty;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#getIconSrc()
	 */
	public String[] getIconSrc() {
		return images;
	}

	public void setIconSrc(String folderClosed, String folderOpenend, String document) {
		this.images = new String[] { folderClosed, folderOpenend, document };
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#getContextMenu()
	 */
	public IContextMenu getContextMenu() {
		return contextMenu;
	}

	public void setContextMenu(IContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#isHasCheckboxes()
	 */
	public boolean isHasCheckboxes() {
		return hasCheckboxes;
	}

	public void setHasCheckboxes(boolean hasCheckboxes) {
		this.hasCheckboxes = hasCheckboxes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#getCheckboxParameter()
	 */
	public String getCheckboxParameter() {
		return checkboxParameter;
	}

	public void setCheckboxParameter(String checkboxParameter) {
		this.checkboxParameter = checkboxParameter;
	}

	public void setListener(ITreeListener listener) {
		this.listener = listener;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#getListener()
	 */
	public ITreeListener getListener() {
		return listener;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#getChildSelectors()
	 */
	public String[] getChildSelectors() {
		return childSelectors;
	}

	public void setChildSelectors(String[] childSelectors) {
		this.childSelectors = childSelectors;
	}

	public void setDroppable(boolean droppable) {
		this.droppable = droppable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#isDraggable()
	 */
	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.endress.infoserve.wgt.controls.tree.ITreeNodeHandler#isDroppable()
	 */
	public boolean isDroppable() {
		return droppable;
	}

	public boolean canHandle(Object o) {
		if (matchClass != null && matchProperty==null) {
			if (o instanceof IDataBag && ((IDataBag) o).getDelegate() != null)
				o = ((IDataBag) o).getDelegate();
			return matchClass.isAssignableFrom(o.getClass());
		} else if (matchProperty != null && matchClass==null) {
			if (matchValue instanceof String) {
				return ((String) matchValue).equals(PropertyAccessor.retrieveString(o, matchProperty));
			}
			Object property = PropertyAccessor.retrieveProperty(o, matchProperty);
			if (property != null)
				return property.equals(matchValue);
			else
				return false;
		} else if(matchClass != null && matchProperty!=null){
			boolean clazzMatch = false;
			if (o instanceof IDataBag && ((IDataBag) o).getDelegate() != null)
				o = ((IDataBag) o).getDelegate();
			clazzMatch= matchClass.isAssignableFrom(o.getClass());
			if(clazzMatch){
				if (matchValue instanceof String) {
					return ((String) matchValue).equals(PropertyAccessor.retrieveString(o, matchProperty));
				}
				Object property = PropertyAccessor.retrieveProperty(o, matchProperty);
				if (property != null)
					return property.equals(matchValue);
				else
					return false;
			} else{
				return false;
			}
		}
		return false;
	}

	public String getTooltipProperty() {
		return tooltipProperty;
	}

	public void setTooltipProperty(String tooltipProperty) {
		this.tooltipProperty = tooltipProperty;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
