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
package org.webguitoolkit.ui.controls.contextmenu;

import java.io.PrintWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.LI;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.tree.ITree;
import org.webguitoolkit.ui.controls.util.TextService;


/**
 * A ContextMenuItem is a single entry in a context menu, it consists of a image and a label.
 */
public class ContextMenuItem extends BaseControl implements IContextMenuItem{

	protected String imgSrc = null;
	protected String label = null;

	protected IContextMenuListener listener;
	private boolean isDisabled;

    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public ContextMenuItem() {
		super();
	}
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public ContextMenuItem(String id) {
		super(id);
	}

	@Override
	protected void endHTML(PrintWriter out) {

		LI li = new LI();
		if( imgSrc != null )
			li.addElement( "<img src=\""+getImgSrc()+"\" /> "+getLabel() );
		else
			li.addElement( getLabel() );

		String css = getCssClass();
		if(isDisabled) {
			css = css+" context-menu-item-disabled";
		}
		if(StringUtils.isNotBlank(css)){
			li.setClass(css);
		}

		li.setID( getId() );
		li.output( out );
	}

	@Override
	protected void init() {
	}

	public String getImgSrc() {
		String src = imgSrc;
		if(isDisabled) {
			String fileName = FilenameUtils.removeExtension(imgSrc);
			String fileType = FilenameUtils.getExtension(imgSrc);
			if(StringUtils.isNotEmpty(fileType)) {
				fileType = "."+fileType;
			}
			src = fileName + "_disabled" + fileType;
		}
		return src;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setLabelKey(String labelKey) {
		this.label = TextService.getString( labelKey );
	}
	@Override
	public void dispatch(ClientEvent event) {
		if(isDisabled) {
			return;
		}
		if( hasExecutePermission() ){

			IContextMenuListener listener = getListener();
			if( listener == null ){
				listener = new IContextMenuListener(){
					public void onAction(ClientEvent event, IBaseControl control) {
					}
					public void onAction(ClientEvent event, ITable table, int row) {
					}
					public void onAction(ClientEvent event, ITree tree, String nodeId) {
					}
				};
			}
			String objectId = event.getParameter( 0 );
			BaseControl control = ((BaseControl)event.getSource()).getParent().getParent();
			if( control instanceof Table ){
				Table table = (Table)control;
				String rowAsString = objectId.substring( objectId.lastIndexOf(".r")+2 );
				int row = Integer.parseInt( rowAsString );
				int firstRow = table.getFirstRow();
				listener.onAction( event, table, row+firstRow );
			}
			else if( control instanceof ITree ){
				listener.onAction( event, (ITree)control, objectId );
			}
			else
				listener.onAction( event, control );

		}
	}
	public IContextMenuListener getListener() {
		return listener;
	}
	public void setListener(IContextMenuListener listener) {
		this.listener = listener;
	}
	public boolean hasListener(){
		return listener != null;
	}
	
	public void setDisabled(boolean disabled) {
		isDisabled = disabled;
	}
	
	public boolean isDisabled() {
		return isDisabled;
	}
}
