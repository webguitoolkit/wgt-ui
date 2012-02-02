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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.IDataBag;


/**
 * <pre>
 * The GenericTreeNode is used by the GenericTreeModel.
 * </pre>
 */
public class GenericTreeNode extends SimpleTreeNode implements ITreeNode {

	private ITreeNodeHandler treeNodeHandler = null;
	private IDataBag dataObject = null;

	public GenericTreeNode(ITreeNodeHandler treeNodeHandler, IDataBag dataObject) {
		this.treeNodeHandler = treeNodeHandler;
		this.dataObject = dataObject;
	}

	public GenericTreeNode(ITreeNodeHandler treeNodeHandler, IDataBag o, ITreeNode treeNode) {
		this(treeNodeHandler, o);
		parent = treeNode;
	}

	@Override
	public String getCaption() {
		return dataObject.getString(treeNodeHandler.getDisplayProperty());
	}
	@Override
	public String getTooltip() {
		if( StringUtils.isNotEmpty( treeNodeHandler.getTooltipProperty() ) )
			return dataObject.getString(treeNodeHandler.getTooltipProperty());
		return null;
	}

	public List getChildObjects() {
		List returnList = new ArrayList();
		String[] childSelectors = treeNodeHandler.getChildSelectors();
		if (childSelectors != null) {
			for (int i = 0; i < childSelectors.length; i++) {
				Object o = dataObject.get(childSelectors[i]);
				if (o == null)
					continue;
				if (o instanceof Collection ) {
					returnList.addAll((Collection) o);
				} else
					returnList.add(o);
			}
		}
		return returnList;
	}

	@Override
	public String[] getIconSrc() {
		return treeNodeHandler.getIconSrc();
	}

	@Override
	public ITreeListener getListener() {
		return treeNodeHandler.getListener();
	}

	@Override
	public boolean hasCheckBox() {
		return treeNodeHandler.isHasCheckboxes();
	}

	public boolean hasChildren() {
		String[] childSelectors = treeNodeHandler.getChildSelectors();
		if (childSelectors != null) {
			for (int i = 0; i < childSelectors.length; i++) {
				Object o = dataObject.get(childSelectors[i]);
				if (o == null)
					continue;
				if (o instanceof Collection && ((Collection) o).size() == 0)
					continue;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isChecked() {
		return dataObject.getBool(treeNodeHandler.getCheckboxParameter());
	}

	@Override
	public void setChecked(boolean checked) {
		dataObject.addProperty(treeNodeHandler.getCheckboxParameter(), new Boolean(checked));
	}

	@Override
	public String getContextMenuId() {
		if (treeNodeHandler.getContextMenu() != null)
			return treeNodeHandler.getContextMenu().getId();
		return null;
	}

	public IDataBag getDataObject() {
		return dataObject;
	}
	
	public ITreeNodeHandler getTreeNodeHandler() {
		return treeNodeHandler;
	}
	
	protected void setTreeNodeHandler(ITreeNodeHandler newHandler) {
		this.treeNodeHandler = newHandler;
	}

	@Override
	public boolean isDraggable() {
		return treeNodeHandler.isDraggable();
	}

	@Override
	public boolean isDroppable() {
		return treeNodeHandler.isDroppable();
	}

	@Override
	public String getStyle() {
		return treeNodeHandler.getStyle();
	}


}
