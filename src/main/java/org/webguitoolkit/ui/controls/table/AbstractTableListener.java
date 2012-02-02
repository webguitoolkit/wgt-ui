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
package org.webguitoolkit.ui.controls.table;

import java.util.Iterator;
import java.util.List;

import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.popupselect.Popup.SelectionListener;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;


/**
 * <pre>
 * Default implementation for the most common table functions.
 * </pre>
 *
 */
public abstract class AbstractTableListener implements ITableListener {

	/**
     * goto row will change the selection to the row in the input
     * it may load the table with rows if the wanted row is not visible
     */
     public void onGotoRow(ClientEvent event) {
         Table table = (Table) event.getSource();
         // if there is a least one row...
         if (table.getRowsLoaded() <=  0) {
        	 table.selectionChange(-1, false);
        	 return;
         }
         int userRow = table.getPage().getContext().getValueAsInt(table.id4RowInput()) -1;
         // set userrowinto bounds
         userRow = Math.max(0, Math.min(userRow, table.getPage().getContext().getValueAsInt(table.id4size()) -1 ));
         // check if selected in visible
         if (userRow < table.getFirstRow() || userRow >= table.getFirstRow() + table.getRowsLoaded()) {
             table.load(userRow);       
             // in case this is an editable table abort on error
             if (table.hasErrors()) return;
         }
         table.selectionChange(userRow, false);
         // call row change event 
		// 2011-01-04 no longer wanted to fire onRowSelection for
		// Popupselect(ticket: 988)
		if (!(this instanceof SelectionListener))
			onRowSelection(table, userRow);    	 
    }

 	/**
      * scroll to row
      */
      public void onScrollTo(ClientEvent event) {
    	  int userRow = Integer.parseInt( event.getParameter(0) );
          Table table = (Table) event.getSource();
          // if there is a least one row...
          if (table.getRowsLoaded() <=  0) {
         	 table.selectionChange(-1, false);
         	 return;
          }
          int firstRow = table.getFirstRow();
          if( userRow != table.getFirstRow() ) {
              table.load(userRow);       
              // in case this is an editable table abort on error
              if (table.hasErrors()) return;
          }
     }

    public void onSort(ClientEvent event) {
         Table table = (Table) event.getSource();
         table.save2Bag();
         table.sort(event.getParameter(0));
    }

    public void onGotoTop(ClientEvent event) {
         Table table = (Table) event.getSource();
         table.load(0);
     }

    public void onPageUp(ClientEvent event) {
        Table table = (Table) event.getSource();
        int firstRow = table.getPage().getContext().getValueAsInt(table.id4FirstRow());
		if (firstRow == Integer.MIN_VALUE) {
			firstRow = table.getRows(); // if table is empty the context entry is not found and the value is set to
										// Integer.MIN_VALUE
		}
        table.load(firstRow - table.getRows());
   }

    public void onClickUp(ClientEvent event) {
        Table table = (Table) event.getSource();
        int firstRow = table.getPage().getContext().getValueAsInt(table.id4FirstRow());
		if (firstRow == Integer.MIN_VALUE) {
			firstRow = 1; // if table is empty the context entry is not found and the value is set to Integer.MIN_VALUE
		}
        table.load(firstRow - 1);
    }

    public void onClickDown(ClientEvent event) {
        Table table = (Table) event.getSource();
        int firstRow = table.getPage().getContext().getValueAsInt(table.id4FirstRow());
		if (firstRow == Integer.MIN_VALUE) {
			firstRow = -1; // if table is empty the context entry is not found and the value is set to Integer.MIN_VALUE
		}
		table.load(firstRow + 1);
    }

    public void onPageDown(ClientEvent event) {
        Table table = (Table) event.getSource();
        int firstRow = table.getPage().getContext().getValueAsInt(table.id4FirstRow());
		if (firstRow == Integer.MIN_VALUE) {
			firstRow = table.getRows() * -1; // if table is empty the context entry is not found and the value is set to
												// Integer.MIN_VALUE
		}
		table.load(firstRow + table.getRows());
    }

    public void onGotoBottom(ClientEvent event) {
        Table table = (Table) event.getSource();
        int size = table.getPage().getContext().getValueAsInt(table.id4size());
		if (size == Integer.MIN_VALUE) {
			size = table.getRows(); // if table is empty the context entry is not found and the value is set to Integer.MIN_VALUE
		}
        table.load( size - table.getRows());
    }

    public void onRowSelected(ClientEvent event) {
        // Reference to the Table is always good
        Table table = (Table) event.getSource();
        // we do the controlling of the highlite
        int rowSelected = Integer.parseInt(event.getParameter(0));
        // there are some misguided events, when the table size is smaller than
        // the viewable size
        int size = table.getPage().getContext().getValueAsInt(table.id4size());
        if (rowSelected >= size) {
            // remove highlite
            table.selectionChange(-1, false);
        } else {
            int firstRow = table.getPage().getContext().getValueAsInt(table.id4FirstRow());
            int absSelection = firstRow + rowSelected;
            // set the highlight
            table.selectionChange(absSelection, false);
            onRowSelection(table, absSelection );
        }
    }

    public abstract void onRowSelection(ITable table, int row);

    public void onImplicitFilter(ClientEvent event) {
        Table table = (Table) event.getSource();
		table.save2Bag();
        table.reload(0);
    }

    public void onDDFilter(ClientEvent event) {
        Table table = (Table) event.getSource();
		table.save2Bag();
        table.reload(0);
   }

	public void onEditTableLayout(ClientEvent event, int rowCount, String tableSetting) {
        Table table = (Table) event.getSource();
        table.setRows( rowCount );
        table.loadColumnConfig( tableSetting );
        table.redraw();
        table.reload();
	}

	public void onDropped(ClientEvent event, String draggableId, String droppableId) {
        Table table = (Table) event.getSource();
        table.addAndReload( table.getPage().getDraggable().getDataObject() );
	}
	public void onCheckAll( ClientEvent event ) {
		String check = event.getParameter(0);
		String colID = event.getParameter(1);
		try {
	        Table table = (Table) event.getSource();
			TableColumn col = null;	        
			for( Iterator iter = table.getColumns().iterator(); iter.hasNext(); ){
				TableColumn temp = (TableColumn) iter.next();
				if( temp.getId().equals( colID ) ){
					col=temp;
					break;
				}
			}
			if( col == null ){
				// should not happen
				return;
			}
	        int all = table.getPage().getContext().getValueAsInt(table.id4size());
			List allRows = table.getModel().navigate(0, all);
			for( Iterator iter = allRows.iterator(); iter.hasNext(); ){
				Object o = iter.next();
				PropertyAccessor.storeProperty(o, col.getMappedProperty(o) , new Boolean( check ) );
			}
		} catch ( ValidationException e ) {
			// should not happen cause there is no converter specified;
		}
	}

	public void onDragStart(ClientEvent event, String draggableId) {
    	int extensionIndex = draggableId.indexOf(".r");
    	if( extensionIndex > 0 ){
            int row = Integer.parseInt( draggableId.substring( extensionIndex+2 ) );
	        Table table = (Table) event.getSource();
	        IDataBag dragged = table.getRow( row+table.getFirstRow() );
	        table.getPage().getDraggable().setDataObject( dragged );
	        table.getPage().getDraggable().setLabel( dragged.getString( table.getDraggableProperty() ));
    	}
	}
}
