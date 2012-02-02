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

import java.io.Serializable;

/**
 * <b>The TreeListener is used as a callback for user input on tree nodes</b>
 * 
 * <pre>
 * public class MyTreeListener implements ITreeListener{
 *   public void onTreeNodeClicked( ITree tree, String nodeId ){
 *     tree.getPage().sendInfo("Tree Node Clicked: "+ nodeId );
 *   }
 *   public void on...
 *   
 *   ...
 * }
 * //set the Tree's TreeListener
 * tree.setListener( new MyTreeListener() );
 * </pre>
 * <p>
 * The AbstractTreeListener implements all methods with standard behavior except the onTreeNodeClicked(), it is recommended to use
 * the AbstractTreeListener as base class for the listener.
 * </p>
 * 
 * @author Martin
 * 
 * @see AbstractTreeListener
 */
public interface ITreeListener extends Serializable {

	/**
	 * <b>This method is called when a node is clicked.</b><br>
	 * It it's the most interesting one for applications.
	 * 
	 * @param tree the tree
	 * @param nodeId the clicked node id
	 */
	void onTreeNodeClicked(ITree tree, String nodeId);

	/**
	 * This method is called when a folder is opened.
	 * 
	 * @param tree the tree
	 * @param nodeId the node id of the folder
	 */
	void onTreeNodeOpen(ITree tree, String nodeId);

	/**
	 * This method is called when a node is dropped on an other one.
	 * 
	 * @param tree the tree
	 * @param nodeId the node on which the other dropped (target)
	 * @param droppedId the node that is dropped
	 */
	void onTreeNodeDropped(ITree tree, String nodeId, String droppedId);

	/**
	 * This method is called when the dragging of a node starts.
	 * 
	 * @param tree the tree
	 * @param nodeId the dragged node id
	 */
	void onTreeNodeDragged(ITree tree, String nodeId);

	/**
	 * This method is called when a nodes check box has been clicked.
	 * 
	 * @param tree the tree
	 * @param nodeId the checked node id
	 */
	void onTreeNodeChecked(ITree tree, String nodeId);

	/**
	 * This method is called when a folder is closed.
	 * 
	 * @param tree
	 * @param nodeId
	 */
	void onTreeNodeClose(ITree tree, String nodeId);
}
