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
 * The AbstractTreeListener provides some default behaviour for the Trees events.
 * </pre>
 */
public abstract class AbstractTreeListener implements ITreeListener {

	/**
	 * has to be overwritten reacting on checkbox clicks
	 */
	public void onTreeNodeChecked(ITree tree, String nodeId) {}

	public abstract void onTreeNodeClicked(ITree tree, String nodeId);

	/**
	 * has to be overwritten when drop is enabled
	 */
	public void onTreeNodeDropped( ITree tree, String nodeId, String droppedId ) {
		ITreeNode droppedTo = tree.getModel().getTreeNode( nodeId );

		if( droppedTo instanceof DefaultTreeNode ){
	        Object dataObject = tree.getPage().getDraggable().getDataObject();

	    	// create new tree node
	    	DefaultTreeNode node = new DefaultTreeNode();
	    	node.setCaption( tree.getPage().getDraggable().getLabel() );
			node.setDataObject( dataObject );
			node.setDraggable( true );
			node.setListener( this );

			// add to the tree node where it is dropped
			((DefaultTreeNode)droppedTo).getChildren().add( node );
			tree.reloadChildren(droppedTo);
		}
	}

	public void onTreeNodeDragged( ITree tree, String nodeId ){
		ITreeNode node = tree.getModel().getTreeNode( nodeId );

		Object dragged = null;
		if( node instanceof DefaultTreeNode ){
			dragged = ((DefaultTreeNode)node).getDataObject();
		}
		else if( node instanceof GenericTreeNode ){
			dragged = ((GenericTreeNode)node).getDataObject();
		}
        tree.getPage().getDraggable().setDataObject( dragged );
        tree.getPage().getDraggable().setLabel( node.getCaption() );
	}

	public void onTreeNodeOpen(ITree tree, String nodeId) {
	}
	public void onTreeNodeClose(ITree tree, String nodeId) {
	}

}
