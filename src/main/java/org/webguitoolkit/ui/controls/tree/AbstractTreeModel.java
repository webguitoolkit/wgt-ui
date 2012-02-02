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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.webguitoolkit.ui.util.Guid;

/**
 * <pre>
 * Provides some base functionality for the TreeModel like caching tree nodes.
 * </pre>
 */
public abstract class AbstractTreeModel implements ITreeModel {

	protected ITreeNode rootElement;
	protected boolean rootVisible;
	protected boolean loadLazy = false;
	protected boolean hasCheckboxes = false;
	protected boolean hasDragDrop = false;
	// noelist of id to node-instances
	protected Map nodeList = new HashMap();
	protected String treeId = null;
	protected Guid nodeSeq = new Guid();

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public AbstractTreeModel( boolean rootVisible, boolean loadLazy, boolean hasCheckboxes, boolean hasDragDrop ) {
		super();
		this.rootVisible = rootVisible;
		this.loadLazy = loadLazy;
		this.hasCheckboxes = hasCheckboxes;
		this.hasDragDrop = hasDragDrop;
	}


	public final List getChildren(ITreeNode treeNode){
		List children = getChildNodes( treeNode );
		int id = 0;
		for(Iterator iter = children.iterator(); iter.hasNext(); ){
			ITreeNode o = (ITreeNode)iter.next();
			if( o instanceof SimpleTreeNode ){
				SimpleTreeNode tn = (SimpleTreeNode)o;
				if(tn.getId() == null )
					tn.setId(  generateNodeId( treeNode, id )  );
			}
			nodeList.put(o.getId(), o );
			id ++;
		}
		return children;
	}

	public abstract List getChildNodes(ITreeNode treeNode);

	/**
	 * clear isLoaded for all loaded nodes, needed for lazy loading
	 */
	public void clearLoaded(){
		clearNode( rootElement );
		rootElement.setLoaded( true );
	}
	/**
	 * clear isLoaded for children, needed for lazy loading
	 */
	public void clearLoaded( ITreeNode parent ){
		clearNode( parent );
		parent.setLoaded( true );
	}
	
	protected void clearNode( ITreeNode node ){
		if( node.isLoaded() && hasChildren( node ) ){
			for (Iterator it = getChildren( node ).iterator(); it.hasNext();){
				clearNode( (ITreeNode)it.next() );
			}
		}
		node.setLoaded( false );
	}

	public List getAllLoaded() {
		return new ArrayList(nodeList.values());
	}

	public ITreeNode getRootElement() {
		if( rootElement instanceof SimpleTreeNode ){
			SimpleTreeNode tn = (SimpleTreeNode)rootElement;
			// only generate id if none exists yet
			if (tn.getId() == null ) {
				tn.setId( generateNodeId() );
			}
		}
		if(rootElement!=null)
			nodeList.put(rootElement.getId(), rootElement );
		
		return rootElement;
	}

	public ITreeNode getTreeNode(String id) {
		return (ITreeNode) nodeList.get( id );
	}

	public boolean hasCheckBoxes() {
		return hasCheckboxes;
	}

	public boolean hasDragDrop() {
		return hasDragDrop;
	}

	public boolean isLoadLazy() {
		return loadLazy;
	}

	public boolean isRootVisible() {
		return rootVisible;
	}

	public void setLoadLazy(boolean loadLazy) {
		this.loadLazy = loadLazy;
	}

	protected String generateNodeId(){
		return treeId + ".n0";		
	}
	protected String generateNodeId( ITreeNode parent, int id ){
		return parent.getId() + "." + id;		
	}
	
}
