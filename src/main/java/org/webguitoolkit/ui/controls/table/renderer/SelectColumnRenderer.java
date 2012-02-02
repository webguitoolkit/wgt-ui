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

import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.ISelectModel;
import org.webguitoolkit.ui.controls.form.Select;
import org.webguitoolkit.ui.controls.table.ITableColumn;

/**
 * <pre>
 * Renderer for select boxes for the column's cells 
 * </pre>
 */
public class SelectColumnRenderer extends ChildColumnRenderer {

	private final ISelectModel model;

	public SelectColumnRenderer(ISelectModel model) {
		super();
		this.model = model;
	}
	/**
	 * @deprecated
	 */
	public SelectColumnRenderer(ITableColumn col, ISelectModel model) {
		super();
		this.model = model;
	}
	
	protected FormControl[] getFormControls() {
		TableSelect select = new TableSelect();
		select.setCssClass( "wgtTableComboText" );
		select.setParent( table );
		select.setProperty( tableColumn.getProperty() );
		select.setCompound( compound );
		select.setModel( model );
		if( tableColumn.getIsDisplayed() )
			compound.addElement(select);
		return new FormControl[]{ select };
	}

	public Select getTextField(){
		return (Select) myControls[0];
	}
	public void move2Row(int row) {
		super.move2Row(row);
		((Select)myControls[0]).loadList();
	}
	
	class TableSelect extends Select{
		public void internalGenHtml(PrintWriter out) {
	        ContextElement ce = getContext().processContextElement( getId() + DOT_RO );
	        boolean ro = ce != null && IContext.TYPE_READONLY.equals( ce.getType() );
	        String imgSrc = "./images/wgt/select_arrow.gif";
	        ContextElement ceImg = getContext().processContextElement( getId() + DOT_BUTTON + ".src" );
	        if( ceImg != null && StringUtils.isNotEmpty(ce.getValue() ) )
	        	imgSrc = ceImg.getValue();
			
	        if (ro) {
	        	if (StringUtils.isEmpty(getCssClass())) {
	        		setCssClass("wgtComboText wgtReadonly");
	        	} else {
	        		addCssClass("wgtReadonly");
	        	}
	        }
	        
	        // hidden result
	        Input input = new Input();
	        input.setType( "hidden" );
	        input.setValue( "" );
	        input.setID( getId()+"_hidden"  );
	        input.output( out );

	        org.apache.ecs.html.Table table = new org.apache.ecs.html.Table();
	        table.setCellSpacing( 0 );
	        table.setCellPadding( 0 );
	        table.setClass( "wgtComboTable" );
	        table.setID( getId() + "_table" );
	        boolean isVisible = getContext().processBool(getId()+"_table"+IContext.DOT_VIS, true );
	        if(!isVisible)
	        	table.setStyle("display:none;");
	        
	        TR tr = new TR();
	        table.addElement( tr );
	        
	        TD td = new TD();
	        tr.addElement( td );
	        
	        // text field
	        input = new Input();
	        input.setType( "text" );
	        input.setReadOnly( ro );
	        input.setOnFocus( "onRowIn(this);" );
//	        input.setOnBlur( "onRowOut(this);" );
	        input.setID( getId() );
	        input.setClass( StringUtils.isEmpty(getCssClass()) ? "wgtComboText" : getCssClass() );
	        td.addElement( input );
	        
	        td = new TD();
	        tr.addElement( td );

	        // button
	        input = new Input();
	        input.setType( "image" );
	        input.setSrc( imgSrc );
	        input.setStyle( "display: none;"  );
	        input.setOnFocus( "onRowIn(this);" );
	        input.setOnBlur( "onRowOut(this);" );
	        input.setOnKeyDown( "if (event.keyCode==9){onTab(this)}else if(event.keyCode==38){onArrowUp(this)}else if(event.keyCode==40){onArrowDown(this)};" );

	        input.setID( getId()+DOT_BUTTON  );
	        if( getTooltip()!=null )
	        	input.setTitle( getTooltip() );
	        td.addElement( input );
	        table.output( out );

			createSelect();
		}
	}
}

