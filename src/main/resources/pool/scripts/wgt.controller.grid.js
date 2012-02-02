// Depends: wgt.controller.base.js, wgt.util.json.js, jquery/ui/1.8.1/jquery.ui.core.js, jquery/ui/1.8.1/jquery.ui.widget.js, jquery/ui/1.8.1/jquery.ui.mouse.js, jquery/ui/1.8.1/jquery.ui.sortable.js, jquery/jqgrid/i18n/grid.locale-en.js, jquery/jqgrid/jquery.jqGrid.js

/**
 * ################### Grid #############################
 * 
 * Scripts used for the grid control
 * 
 * File: wgt.controller.grid.js
 * 
 * Author: Martin Hermann
 */
registerApplyer(function(aktContext) {
	switch (aktContext.type) {
	case "gridI": // init grid
		gridInit(toLastDot( aktContext.cssId ), aktContext.value);
		break;
	case "gridL": // load data
		gridLoad(toLastDot( aktContext.cssId ), aktContext.value);
		break;
	case "gridLL": // load lazy data
		gridLoadLazy(toLastDot( aktContext.cssId ), aktContext.value);
		break;
	default:
		return false;
	}
	return true;
});
var wgtGridLastSelect = {};

function gridInit( gridId, value ) {
	eval( "var val="+value );
	jQueryById( gridId ).jqGrid( val );
	jQueryById( gridId ).jqGrid('navGrid','#' + gridId + '_pager',{del:false,add:false,edit:false,search:true,refresh:false}); 
	//jQueryById( gridId ).jqGrid('filterToolbar'); 
	//jQueryById( gridId ).jqGrid('sortableRows'); 
	jQueryById( gridId ).jqGrid('navButtonAdd','#' + gridId + '_pager',{ 
		caption: "Columns", 
		title: "Reorder Columns", 
		onClickButton : function (){ 
			jQueryById( gridId ).jqGrid('columnChooser'); 
		}
	}); 
	wgtGridLastSelect[gridId] = -1;
}

function gridLoadLazy( gridId, value ){
	
	var theGrid = jQueryById( gridId );
	
	var val = JSON.parse(value);
//	var val = eval('(' + value + ')');
	
	theGrid[0].addJSONData(val);
	
//	eval( "var val="+value );
//	var mydata = val.rows;
//	for ( var i = 0; i <= mydata.length; i++) {
//		theGrid.jqGrid('addRowData', i + 1, mydata[i]);
//	}
//	theGrid.setGridParam({lastpage: val.total});  
//	theGrid.each(function() {  
//		if (this.grid) this.updatepager();  
//	});  
}

function gridLoad( gridId, value ){
	eval( "var mydata="+value );
	for ( var i = 0; i <= mydata.length; i++) {
		jQueryById( gridId ).jqGrid('addRowData', i + 1, mydata[i]);
	}
}
function gridEditEvent( gridId, rowId ){
	var parameter = jQuery('#'+gridId ).getRowData(rowId);
	var para = new Array();
	para[0]=rowId;
	var i = 0;
	for (a in parameter){
		para[(i*2)+1] = a;
		para[(i*2)+2] = parameter[a];
		i++;
	}
	eventParam(gridId,para );
	fireWGTEvent(gridId, 'item_edited');
}
function gridLoadEvent( gridId, postdata ){
	var para = new Array();
	var i = 0;
	for (a in postdata){
		para[(i*2)+0] = a;
		para[(i*2)+1] = postdata[a];
		i++;
	}
	eventParam(gridId,para );
	fireWGTEvent(gridId, 'gll');
}
