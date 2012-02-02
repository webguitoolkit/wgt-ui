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
import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;

/**
 * <pre>
 * DefaultTreeNode keeps the state around and offers setters for all the properties.
 * Not, that after generating the html, the tree node are not accessed by the tree class any more.
 * So you could discard after rendering...
 * </pre>
 * @author arno@schatz.to
 */
public class DefaultTreeNode extends SimpleTreeNode implements ITreeNode {
	// List of children which are ItreeNodes.
	protected List children;
	// reference to the associated object
	protected Object dataObject;
	// parent in the hierarchie
	// protected DefaultTreeNode parent;
	
	// parameter of the dataObjects field that indicates if the checkbox is checked
	protected String checkboxParameter = "checked";
	/**
	 *
	 */
	public DefaultTreeNode() {
		super();
	}
	/**
	 * test if this node has children. This method is more efficient than
	 * calling  getChildren().size()>0, as it doesn't create 
	 * an array just to get the size. 
	 *
	 * @return true iff there are children.
	 */
	public boolean hasChildren() {
		return children != null && getChildren().size()>0; 
	}

	public List getChildren() {
		if (children==null) {
			// ensure that our list will update the parent reference
			children = new ArrayList() {

				public Object set(int index, Object element) {
					((DefaultTreeNode) element).setParent(DefaultTreeNode.this);
					return super.set(index, element);
				}

				public void add(int index, Object element) {
					super.add(index, element);
					((DefaultTreeNode) element).setParent(DefaultTreeNode.this);
				}

				public boolean add(Object o) {
					((DefaultTreeNode) o).setParent(DefaultTreeNode.this);
					return super.add(o);
				}

				public boolean addAll(Collection c) {
					for (Iterator it = c.iterator(); it.hasNext();) {
						((DefaultTreeNode) it.next())
								.setParent(DefaultTreeNode.this);
					}					
					return super.addAll(c);
				}

				public boolean addAll(int index, Collection c) {
					for (Iterator it = c.iterator(); it.hasNext();) {
						((DefaultTreeNode) it.next())
								.setParent(DefaultTreeNode.this);
					}					
					return super.addAll(index, c);
				}
				
			};
		}
		return children;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}

	public void setParent(DefaultTreeNode parent) {
		this.parent = parent;
	}
	
	public boolean isChecked() {
		if( dataObject != null ){
			Boolean checked = (Boolean)PropertyAccessor.retrieveProperty( dataObject, checkboxParameter );
			if( checked!=null )
				return checked.booleanValue();
		}
		return false;
	}

	public void setCheckboxParameter( String newCheckboxParameter){
		this.checkboxParameter = newCheckboxParameter;
	}
	public void setChecked( boolean checked ) {
		try{
			PropertyAccessor.storeProperty( dataObject, checkboxParameter, new Boolean(checked) );
		}
		catch( ValidationException ex ){
			// should not happen, we have no converter;
		}
	}
}
