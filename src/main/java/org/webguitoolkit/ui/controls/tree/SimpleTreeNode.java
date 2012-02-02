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


/**
 * <pre>
 * A basic TreeNode as a bean where all fields can be set by setter methods.
 * </pre>
 */
public class SimpleTreeNode implements ITreeNode{

	// the tree node id
	protected String id;
	// caption is the text of the node being displayed.
	protected String caption;
	// tooltip text of the node
	protected String tooltip;
	// is this node initially expandded?
	protected boolean expanded;
	// is this node initially selected? (last wins)
	protected boolean selected;
	// reference to the associated object
	protected boolean isLoaded = false;
	// parameter of the dataObjects field that indicates if the checkbox is checked
	protected boolean checkBox;
	// boolean tha indicates if checkbox is checked
	protected boolean checked;
	// list of listeners for tree actions
	protected ITreeListener listener;
	// the context menu id
	protected String cmId;
	// the tree node text style
	protected String style;

	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * inconSrc need to be an array of 3 string, for example:
	 * new String[]{ "a.gif","b.gif","c.gif"]
	 * see ITreeNode for description
	 */
	protected String[] iconSrc;
	private boolean droppable;
	private boolean draggable;
	protected ITreeNode parent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}
	public void setCaption( String caption ) {
		this.caption = caption;
	}

	public String[] getIconSrc() {
		return iconSrc;
	}
	
	public void setIconSrc( String folderClosed, String folderOpenend,  String document ) {
		this.iconSrc = new String[]{ folderClosed, folderOpenend, document };
	}

	public boolean hasCheckBox() {
		return checkBox;
	}
	public void setHasCheckBox( boolean checkBox ) {
		this.checkBox = checkBox;
	}

	public boolean isChecked() {
		return checked;
	}
	public void setChecked( boolean checked ) {
		this.checked = checked;
	}

	public boolean isLoaded() {
		return isLoaded;
	}
	public void setLoaded( boolean isLoaded ) {
		this.isLoaded = isLoaded;
	}

	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded( boolean expanded ) {
		this.expanded = expanded;
	}

	public boolean isSelected() {
		return selected;
	}
	public void setSelected( boolean isLoaded ) {
		this.isLoaded = isLoaded;
	}

	public String getContextMenuId() {
		return cmId;
	}
	public void setContextMenuId( String cmId ) {
		this.cmId = cmId;
	}
	public void setDroppable(boolean droppable) {
		this.droppable = droppable;
	}
	public boolean isDraggable() {
		return draggable;
	}
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}
	public boolean isDroppable() {
		return droppable;
	}
	public ITreeListener getListener() {
		return listener;
	}
	public void setListener(ITreeListener listener) {
		this.listener = listener;
	}
	public ITreeNode getParent() {
		return parent;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
}
