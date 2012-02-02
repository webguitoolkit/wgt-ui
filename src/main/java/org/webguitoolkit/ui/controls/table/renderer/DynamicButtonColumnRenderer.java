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
package org.webguitoolkit.ui.controls.table.renderer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.button.StandardButton;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.Tooltip;

/**
 * The DynamicButtonColumnRenderer is an easy way to render 
 * buttons into a table and handle events with a special action listener.
 * 
 * It's a new version of the ButtonColumnRenderer where you can
 * change the images or the text of the button dependent on the property.
 * 
 * <code>
 * createTable(){
 *  ...
 * 	DynamicButtonColumnRenderer bcr = new DynamicButtonColumnRenderer(col,"img/edit.gif", "Edit", new ButtonListener() );
 *  col.setRenderer(bcr);
 * }
 * 
 * class ButtonListener implements ITableButtonActionListener{
 *   public void onAction(ClientEvent event, Table table, int row ){
 *    	table.getPage().sendInfo("Selected row " + row );
 *    }
 * }
 * </code>
 * 
 * @author i102454
 *
 */
public class DynamicButtonColumnRenderer extends ChildColumnRenderer {

	private static final Logger logger = Logger.getLogger(ChildColumnRenderer.class);

	private static final String DOT_TITLE = ".title";

	private Button button;
	
	private boolean isImage = false;
	protected String labelKey; 
	protected String imageSource; 
	protected Tooltip tooltipAdvanced;
	protected String tooltip; 
	protected boolean cancelBubble;
	protected ITableButtonActionListener listener;


	/**
	 * @deprecated
	 */
	public DynamicButtonColumnRenderer(final ITableColumn col, String imageSource, String tooltip, final ITableButtonActionListener listener ) {
		this( col, null, imageSource, tooltip,false, listener );
	}
	
	/**
	 * @deprecated
	 */
	public DynamicButtonColumnRenderer(final ITableColumn col, String imageSource, String tooltip, final ITableButtonActionListener listener , boolean cancelBubble) {
		this( col, null, imageSource, tooltip, cancelBubble,listener );
	}
	
	/**
	 * @deprecated
	 */
	public DynamicButtonColumnRenderer(final ITableColumn col, String imageSource, Tooltip tooltip, final ITableButtonActionListener listener ) {
		this( col, null, imageSource, tooltip, null, false,  listener );
	}

	/**
	 * @deprecated
	 */
	public DynamicButtonColumnRenderer(final ITableColumn col, String labelKey, final ITableButtonActionListener listener ) {
		this( col, labelKey, null, null,false, listener );
	}
	
	/**
	 * @deprecated
	 */
	public DynamicButtonColumnRenderer(final ITableColumn col, String labelKey, final ITableButtonActionListener listener , boolean cancelBubble) {
		this( col, labelKey, null, null,null, cancelBubble ,listener );
	}

	/**
	 * @deprecated
	 */
	private DynamicButtonColumnRenderer(final ITableColumn col, String labelKey, String imageSource, String tooltip,boolean cancelBubble , final ITableButtonActionListener listener ) {
		this( col, null, imageSource, null, tooltip,  cancelBubble, listener );
	}
	
	/**
	 * @deprecated
	 */
	private DynamicButtonColumnRenderer(final ITableColumn col, String labelKey, String imageSource, Tooltip tooltipAdvanced, String tooltip, boolean cancelBubble ,final ITableButtonActionListener listener ) {
		this(labelKey, imageSource, tooltipAdvanced, tooltip, cancelBubble ,listener);
	}
	public DynamicButtonColumnRenderer(String imageSource, String tooltip, final ITableButtonActionListener listener ) {
		this( null, imageSource, tooltip,false, listener );
	}
	
	public DynamicButtonColumnRenderer(String imageSource, String tooltip, final ITableButtonActionListener listener , boolean cancelBubble) {
		this( null, imageSource, tooltip, cancelBubble,listener );
	}
	
	public DynamicButtonColumnRenderer(String imageSource, Tooltip tooltip, final ITableButtonActionListener listener ) {
		this( null, imageSource, tooltip, null, false,  listener );
	}

	
	public DynamicButtonColumnRenderer( String labelKey, final ITableButtonActionListener listener ) {
		this( labelKey, null, null,false, listener );
	}
	
	public DynamicButtonColumnRenderer(String labelKey, final ITableButtonActionListener listener , boolean cancelBubble) {
		this( labelKey, null, null,null, cancelBubble ,listener );
	}


	private DynamicButtonColumnRenderer(String labelKey, String imageSource, String tooltip,boolean cancelBubble , final ITableButtonActionListener listener ) {
		this( (String)null, imageSource, null, tooltip,  cancelBubble, listener );
	}
	
	private DynamicButtonColumnRenderer(String labelKey, String imageSource, Tooltip tooltipAdvanced, String tooltip, boolean cancelBubble ,final ITableButtonActionListener listener ) {
		super();
		this.labelKey = labelKey; 
		this.imageSource = imageSource; 
		this.tooltipAdvanced = tooltipAdvanced;
		this.tooltip = tooltip; 
		this.cancelBubble = cancelBubble;
		this.listener = listener;
	}
	
	protected FormControl[] getFormControls() {
		button = new StandardButton(){
			public void dispatch( ClientEvent event ){
		    	if( !hasExecutePermission() )
		    		return;
		        if( event.getTypeAsInt()==ClientEvent.TYPE_ACTION) {

		        	// id should look like: "Ta296.r1.fcnull"
		        	// we have to find the row number between ".r" and ".fc"
		        	String id = event.getSourceId();
 
		        	id = id.substring(id.indexOf(".r")+2, id.indexOf(".fc") );

		        	// if row is numeric parse to int
		        	int row = 0;
		        	int rowOffset = table.getFirstRow();
		        	if(StringUtils.isNumeric(id))
		        		row = Integer.parseInt( id );
			        	//row offset is added to the currently returned row id. when a table is scrolled down it's row numbers stay the same as bevor.  
			        	//therefore the offset must be added to the current row number!
		        		row += rowOffset;
		        	// call listener
		        	listener.onAction(event, table, row );
		        }
			}
		};
		button.setId( id4FormControl(0, tableColumn.getProperty() ));
		button.setProperty( tableColumn.getProperty() );
		button.setCancelBubble(cancelBubble);
//		String tmp = col.getId() + "_" + col.getProperty();
//		button.setId(tmp);
		if( labelKey != null ){
			isImage = false;
			button.setLabelKey(labelKey);
		}
		else{
			isImage = true;
			button.setSrc( imageSource );
			if(tooltip != null){
				button.setTooltip( tooltip );
			}
			if(tooltipAdvanced != null){
				button.setTooltip( tooltipAdvanced );
			}
		}
		button.setParent( ((TableColumn)tableColumn).getParent() );
		button.setCompound( compound );
		if( tableColumn.getIsDisplayed() )
			compound.addElement(button);
		return new FormControl[]{ button };
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		// may be we have to make the html visible again...
		// data must be of type string
		Object content = PropertyAccessor.retrieveProperty(data,col.getProperty());
		if (content==null ) {
			if( isImage ){
				logger.warn("img src not set, using ./images/wgt/1.gif");
				content = "./images/wgt/1.gif";
			}
			else
				content = "";
		}
		
		if( isImage ){
			button.changeIcon( (String)content );
			if( data instanceof IDataBag )
			{
				Object srcTitle = ((IDataBag)data).get( col.getProperty()+DOT_TITLE );
				if( !StringUtils.isEmpty( (String)srcTitle ) ){
			    	ctx.add( button.getId()+".icon.title", (String)srcTitle, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE );
				}
			}
		}
		else{
			button.changeDisplay(null, (String)content );
		}

		
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.controls.table.IColumnRenderer#clear(java.lang.String, com.endress.infoserve.wgt.controls.table.TableColumn, com.endress.infoserve.wgt.ajax.Context, int, int)
	 */
	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		button.setVisible( false );
	}

}
