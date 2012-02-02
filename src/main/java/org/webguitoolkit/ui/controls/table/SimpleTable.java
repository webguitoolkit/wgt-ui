package org.webguitoolkit.ui.controls.table;

import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Col;
import org.apache.ecs.html.ColGroup;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.NOBR;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TBody;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenu;
import org.webguitoolkit.ui.controls.form.DefaultCompoundModel;

/**
 * <pre>
 * This represents a simple table. There are no interactive features like sorting or filtering available.
 * All data in the model is loaded and rendered into the view.
 * Data handling works like in normal WGT tables
 * The viewport of the table can be defined to a fixed with and height
 * </pre>
 * 
 * @author Thorsten Springhart
 */
public class SimpleTable extends Table implements ISimpleTable {

	private boolean fixedViewPort;
	private String viewPortHeight;
	private String viewPortWidth;
	private String viewPortID;
	
    /**
     * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl()
     */
	public SimpleTable() {
		super();
		super.setEditable(false);
		setCssClass( "wgtTable" );

	}
	/**
	 * @see org.webguitoolkit.ui.controls.BaseControl#BaseControl(String)
	 * @param id unique HTML id
	 */
	public SimpleTable(String id) {
		super(id);
		super.setEditable(false);
		setCssClass( "wgtTable" );
	}
	
	   /**
     * @param out
     */
    public void draw(PrintWriter out){

    	if( columnConfig == null ){
    		initColumnConfig();
    	}
    	
    	IContext ctx = getContext();
    	ctx.restoreState(id4highlite());
    	ctx.restoreState(id4RowInput());
        int colCount = columnConfig.getSize();
        Div div = new Div();
        if (StringUtils.isBlank(viewPortID)) {
        	div.setID(getId()+".viewPort");
        } else {
        	div.setID(viewPortID);
        }
        String divstyle = "";
        if (isFixedViewPort()) {
        	divstyle += "overflow: auto; ";
        	if (StringUtils.isNotBlank(getViewPortWidth())) {
        		divstyle += "width: " + getViewPortWidth() + ";";
        	}
        	if (StringUtils.isNotBlank(getViewPortHeight())) {
        		divstyle += "height: " + getViewPortHeight() + ";";
        	}
        }
       	div.setStyle(divstyle);

        org.apache.ecs.html.Table table = new org.apache.ecs.html.Table();
        div.addElement(table);
        stdParameter( table );
        
        ColGroup colGroup = new ColGroup();
        for( int i = 0; i < colCount; i++ ){
        	Col col = new Col();
        	colGroup.addElement( col );
        }
        table.addElement( colGroup );
        TBody tableBody = new TBody();
        table.addElement( tableBody );
        
    	// ###########    TITLE   ############        
		String title = getContext().processValue( id4Header() );
		
        if ( title!=null) {
        	TR tr = new TR();
        	TH th = new TH();
        	th.setColSpan(99);
        	th.setStyle("padding:0;");
        	//create title bar
        	org.apache.ecs.html.Table titleTable = new org.apache.ecs.html.Table();
        	ColGroup titleColGroup = new ColGroup();
        	titleTable.setCellPadding(1);
        	titleTable.setCellSpacing(0);
        	titleTable.setWidth("100%");
        	TR titleTr = new TR();
        	
    		TD titleTd = new TD();
    		titleTd.setID( id4Header() );
    		titleTd.setClass( "wgtTableHeader" );
    		titleTd.setWidth("*");
       		titleTd.setStyle("white-space: nowrap;");
       		titleTd.addElement( title );
    		titleTr.addElement( titleTd );
//    		TD colorflow = new TD();
//    		colorflow.setID( id4Header()+"cf" );
//    		colorflow.addElement("<img src='./images/wgt/1.gif' width='25px' height='1px'>");
//    		colorflow.setClass("wgtTableHeaderLeftofButtons");
//    		colorflow.setStyle("display: none;");
//    		titleTr.addElement(colorflow);
        	titleTable.addElement(titleTr);
        	th.addElement(titleTable);
        	tr.addElement( th );
        	tableBody.addElement(tr);   
        }
        // ###########    COLUMN HEADER   ############
        
        TR tr = new TR();
        for (int i=0;i<colCount;i++) {
        	TableColumn column = getColumn(i);
        	TD td = new TD();
        	td.setClass("wgtTableColumnHeaderCell");
        	if( StringUtils.isNotEmpty( column.getWidth() ) )
        		td.setWidth( column.getWidth() );
        	if(column.hasStyle())
        		td.setStyle(column.getStyleAsString());
            if (StringUtils.isNotBlank( column.getTooltip()))
            	td.setTitle( StringEscapeUtils.escapeHtml(column.getTooltip()) );

            tr.addElement( td );
            NOBR nobr = new NOBR();
            td.addElement( nobr );
           	nobr.addElement( column.textForTitle() );

        }
    	tableBody.addElement(tr);

        // ############   CONTENT AREA   ####################
        
        // darstellen mit inhalt, daten aus dem Context nehmen.
        String allEntries = getContext().processValue(id4size());
        for (int i=0;i<getDefaultModel().getTableData().size();i++) {

            String style  = getContext().processValue( id4Row(i)+".style");
            String styleClass = ((i % 2)==0) ? rowCssClass : rowCssClassAlternate;
            tr = new TR();
            tr.setClass(styleClass );
            if( style != null )
            	tr.setStyle( style );
            tr.setID( id4Row(i) );
            
            for (int j=0;j<colCount;j++) {            	
	        	TableColumn column = getColumn(j);;
            	//String cellStyle = !StringUtils.isEmpty( columns[j].getStyle() ) ? columns[j].getStyle() : "wgtTDNormal";
            	String cellStyle = "";
            	//if (!StringUtils.isEmpty( column.getStyleAsString() )) {
            	//	cellStyle = column.getStyle();
            	//}
            	if(column.hasStyle()){
            		cellStyle = column.getStyleAsString();
            	}
            	String cellClass = "";
            	if (!StringUtils.isEmpty( column.getCssClass() )) {
            		cellClass = column.getCssClass();
            	} else {
            		cellClass = "wgtTDNormal";
            	}
            	
            	// give the renderer a chance to produce the content of the cell.
                String cellContent = column.getRenderer().generateHTML(column, id4TD(i,j), i, j);
                // eat the following context item if possible
                // it is part of the textColumnrendere and appear often
                //col.getBody().getContext().add(cellId + ".align","right", Context.TYPE_ATT, Context.STATUS_NOT_EDITABLE );

                TD td = new TD();
                td.setID( id4TD(i,j) );
                if( StringUtils.isNotEmpty( cellStyle ) )
                	td.setStyle(cellStyle);
                if( StringUtils.isNotEmpty( cellClass ) )
                	td.setClass(cellClass);
                String align = getContext().processValue(id4TD(i,j) + ".align");
                if( StringUtils.isNotEmpty( align ) )
                	td.setAlign(align);
                td.addElement( cellContent );
                tr.addElement( td );
            }
        	tableBody.addElement(tr);
        }
        
        // ###########    FOOTER    ###############
    	
        	div.output(out);
            Input rowInput = new Input();
        	rowInput.setID( id4RowInput() );
        	rowInput.setType( "hidden" );
        	String value = getContext().processValue(id4RowInput());
        	rowInput.setValue( StringUtils.isNotEmpty( value ) ? value : "" );
        	rowInput.output( out );
        	
            Span span = new Span();
        	span.setID( id4size() );
        	span.setStyle( "display: none;" );
        	span.addElement( allEntries );
        	span.output( out );
    }
    
    public void load(int firstRow, boolean isReload) {
        firstRow = boxIndex(firstRow);
        // write old value into model.
        if (!isReload) {
        	// reload means we don't want the screen data to
            // be unloaded and produce errors again.
            save2Bag();
            // TODO optimize to unload only those line which actually must be
            // unloaded.
            if (hasErrors()) {
                sendErrors(); // error found no moving to other row.
                return;
            }       
        }
        if (isReload && isDrawn()) {
        	redraw();
        }
         
        IContext ctx = getContext();
		ctx.add(id4FirstRow(), firstRow, IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
        List dataList = getModel().navigate(firstRow, getDefaultModel().getTableData().size());
        
        // check sanity
        if (!isReload && dataList.size() > ctx.getValueAsInt(id4size())) {
        	// just a simple check if the rows have changed without reload
        	throw new WGTException("change of underliing data found, please use reload!");
        }
        ctx.add(id4RowsLoaded(), dataList.size(), IContext.TYPE_VAL, IContext.STATUS_SERVER_ONLY);
        for (int i=0;i<dataList.size();i++) {
            Object dataBean = dataList.get(i);
            IRowHandler rowHandler = getRowHandler( dataBean );
            if( rowHandler.getStyle() != null )
            	getContext().setAttribute( id4Row(i), "style", rowHandler.getStyle() );
            if( rowHandler.getContextMenu() != null )
            	rowHandler.getContextMenu().bindTo( id4Row(i) );
            else
    			ContextMenu.unbind( id4Row(i), getContext() );
            	
            moveCompound2Row(i);
    		((DefaultCompoundModel) getRowCompound().getModel()).setData(dataBean);
    		getRowCompound().load();
            if( columnConfig == null ) initColumnConfig();
            for (int j=0;j<columnConfig.getSize();j++) {
            	TableColumn col = getColumn(j);
            	if( col.getIsDisplayed() )
            		col.getRenderer().load( id4TD(i,j),dataBean, col, ctx, i, j );
           }
        }
        // we may need to delete all table cells, which are not shown
        for (int i=dataList.size();i<getDefaultModel().getTableData().size();i++) {
    		moveCompound2Row(i);
    		((DefaultCompoundModel) getRowCompound().getModel()).setData(null);
    		getRowCompound().load();
    		if( defaultContextMenu != null )
    			defaultContextMenu.unbind( id4Row(i) );
            for (int j=0;j<columnConfig.getSize();j++) {
            	//rowHandler.getContextMenu().bindTo( id4Row(i) );
            	TableColumn col = getColumn(j);
            	if( col.getIsDisplayed() )
            		col.getRenderer().clear(id4TD(i,j), col, ctx, i, j);
            }            
        }
        // since we have changed the visible data selection, we may need to move the highlight
        int lead = ctx.getValueAsInt(id4lead());
        selectionChange(lead, false);
        ctx.add( getId(), firstRow, IContext.TYPE_SCROLL_RELOAD, IContext.STATUS_COMMAND );
    }
    /**
     * @return true if the viewport is fixed
     */
    public boolean isFixedViewPort() {
		return fixedViewPort;
	}
    
    /**
     * set if viewport should be fixed
     * @param fixedViewPort
     */
	public void setFixedViewPort(boolean fixedViewPort) {
		this.fixedViewPort = fixedViewPort;
	}

	/**
	 * @return the height of the viewport
	 */
	public String getViewPortHeight() {
		return viewPortHeight;
	}

	/**
	 * height of the viewport, only used if viewport is fixed
	 * @param viewPortHeight
	 */
	public void setViewPortHeight(String viewPortHeight) {
		this.viewPortHeight = viewPortHeight;
	}
	
	/**
	 * @return the width of the viewport
	 */
	public String getViewPortWidth() {
		return viewPortWidth;
	}
	
	/**
	 * width of the viewport, only used if viewport is fixed
	 * @param viewPortWidth
	 */
	public void setViewPortWidth(String viewPortWidth) {
		this.viewPortWidth = viewPortWidth;
	}
	
	/**
	 *  viewport id is the id of the surrounding div, can be used for css styling 
	 * @return id of the viewport
	 */
	public String getViewPortID() {
      if (StringUtils.isBlank(viewPortID)) {
        	return getId()+".viewPort";
        } else {
        	return viewPortID;
        }
	}
	
	/**
	 *  viewport id is the id of the surrounding div, can be used for css styling 
	 * @param id of the viewport
	 */
	public void setViewPortID(String viewPortID) {
		this.viewPortID = viewPortID;
	}
}
