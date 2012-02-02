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
package org.webguitoolkit.ui.tools;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.contextmenu.BaseContextMenuListener;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenu;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenuItem;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.tree.AbstractTreeListener;
import org.webguitoolkit.ui.controls.tree.GenericTreeModel;
import org.webguitoolkit.ui.controls.tree.GenericTreeNode;
import org.webguitoolkit.ui.controls.tree.ITree;
import org.webguitoolkit.ui.controls.tree.Tree;
import org.webguitoolkit.ui.controls.tree.TreeNodeHandler;
import org.webguitoolkit.ui.controls.util.style.Style;


/**
 * <pre>
 * The GuiComponentExplorer can be placed on a page and shows the control tree of the page.
 * </pre>
 */
public class GuiComponentExplorer extends BaseContextMenuListener implements IActionListener{
	
	private WebGuiFactory factory = WebGuiFactory.getInstance();

	private Tree componentTree = null;
	private Canvas canvas = null;
	private ContextMenuItem cmiHide;
	private ContextMenuItem cmiShow;
	private ContextMenuItem cmiId;
	private ContextMenuItem cmiProperty;
	private ContextMenuItem cmiLabel;
	
	public void pageInit( Page parent ){
		
		// Button to show area
		Button button = factory.newButton( parent, null, "Gui Components", "Gui Components", this );

		canvas = factory.newCanvas( parent );
		canvas.setDisplayMode( Canvas.DISPLAY_MODE_WINDOW );
		canvas.setDragable( true );
		canvas.setVisible(false);
		canvas.setZIndex(499);
		canvas.setWidth( 700 );
		canvas.setHeight( 600 );
		
		// GUI Control Component tree
		componentTree = factory.newTree( canvas );
		//componentTree.setStyle("width: 700px; height: 580px; overflow: scroll;");
		componentTree.getStyle().addWidth(700, Style.PIXEL);
		componentTree.getStyle().addHeight(580, Style.PIXEL);
		componentTree.getStyle().addStyleAttributes("overflow: scroll;");

		AbstractTreeListener treeListener = new AbstractTreeListener(){
			private BaseControl lastSelected = null;
			public void onTreeNodeClicked(ITree tree, String nodeId) {
				GenericTreeNode gtn = (GenericTreeNode)tree.getModel().getTreeNode( nodeId );
				BaseControl control = (BaseControl) gtn.getDataObject().getDelegate();
				if( control instanceof TableColumn ){
					control = control.getParent();
				}
				if( !control.equals( lastSelected ) ){
					if( lastSelected !=null )
						tree.getPage().getContext().removeClass(lastSelected.getId(), "wgtRedBorder");
					lastSelected = control;
					tree.getPage().getContext().addClass(control.getId(), "wgtRedBorder");
				}
			}
			
		};

		// add context menu to tree
		ContextMenu cm = factory.newContextMenu( componentTree );
		componentTree.addContextMenu( cm );
		cmiHide = factory.newContextMenuItem(cm, "hide", this);
		cmiShow = factory.newContextMenuItem(cm, "show", this);
		cmiId = factory.newContextMenuItem(cm, "get id", this);
		cmiProperty = factory.newContextMenuItem(cm, "get property", this);
		cmiLabel = factory.newContextMenuItem(cm, "get label", this);

		// default tree node definition
		TreeNodeHandler tnh = new TreeNodeHandler();
		tnh.setChildSelectors( new String[]{"children"});
		tnh.setDisplayProperty( "class.name" );
		tnh.setHasCheckboxes( false );
		tnh.setListener(treeListener);
		tnh.setContextMenu( cm );

		// generic tree model
		GenericTreeModel gtm = new GenericTreeModel( true, true, false, false );
		gtm.setDefaultTreeNodeHandler( tnh );
		gtm.setRoot( parent.getPage() );
		componentTree.setModel(gtm);
	}

	public void onAction(ClientEvent event) {
		if( event.getSource() instanceof Button)
			canvas.setVisible( !canvas.isVisible() );
	}
	
	public void pageLoad() {
		componentTree.load();
	}
	public void onAction(ClientEvent event, Tree tree, String nodeId) {
		ContextMenuItem contextMenuItem = (ContextMenuItem) event.getSource();
		GenericTreeNode treeNode = (GenericTreeNode) tree.getModel().getTreeNode( nodeId );
		BaseControl control = (BaseControl)treeNode.getDataObject().getDelegate();
		if( control.equals( canvas ) || control.equals( componentTree ) )
			tree.getPage().sendWarn("It is not allowed to edit component explorer");
		else if( contextMenuItem == cmiShow )
			control.setVisible( true );
		else if( contextMenuItem == cmiHide )
			control.setVisible( false );
		else if( contextMenuItem == cmiId )
			tree.getPage().sendInfo( control.getId() );
		else if( contextMenuItem == cmiProperty ){
			if(control instanceof FormControl )
				tree.getPage().sendInfo( ((FormControl)control).getProperty() );
			else
				tree.getPage().sendWarn( "No FormControl" );
		}
		else if( contextMenuItem == cmiLabel && control instanceof FormControl && ((FormControl)control).getDescribingLabel() != null )
			tree.getPage().sendInfo( ((FormControl)control).getDescribingLabel().getText() );
			
	}
}
