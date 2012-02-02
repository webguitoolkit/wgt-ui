// Depends: wgt.controller.base.js
/**
 * ################### Table #############################
 *
 * Scripts used by the table
 * 
 * File: wgt.controller.table.js
 * 
 * Author: Martin Hermann, Arno Schatz
 */

/** 
 * register a function for applying the context that comes from the server
 * 
 * context.type - type of the context element
 * context.value - value to be set
 * context.cssId - id of the element
 * context.status - transfer state of the context element
 */ 
registerApplyer( function( context ){
	switch( context.type ) {
		case "lgh": // highlite in table
			highlite(byId(aktContext.value),aktContext.cssId);
			return true;
		default:
			return false;
	}
});

/** 
 * register a function that updates the context to send it back to the server
 * 
 * context.type - type of the context element
 * context.value - value to be set
 * context.cssId - id of the element
 * context.status - transfer state of the context element

registerChangeCalculator( 
	function( context ){
		
	}
)
 */ 
function tableUp( nodeId ){
	var currentRow = jQueryById( nodeId+"_conf");
	var tbody = currentRow.parent().get(0);
	var prev = currentRow.prev().get(0);
	if( tbody.childNodes[1] != currentRow.get(0) )
		tbody.insertBefore(currentRow.get(0),prev);
}
function tableDown( nodeId ){
	var currentRow = jQueryById( nodeId+"_conf");
	var tbody = currentRow.parent().get(0);
	var next = currentRow.next().get(0);
	if( tbody.rows[ tbody.rows.length-3 ] != currentRow.get(0) )
		tbody.insertBefore( next, currentRow.get(0) );
}

var currentConfig = new Array();

function toggleTableEditPopup( tableId ){
	var table = jQueryById( tableId+"_editTableDiv");
	table.toggle();
	if( table.is(':visible') )
		getTableConfiguration( tableId );
}

function editTable( tableId ){
	var table = jQueryById( tableId+"_cols");

	eventParam( tableId, getTableConfiguration(tableId) );
    fireWGTEvent( tableId, '11' );
	
	jQuery(table.parent().get(0)).css( 'display', 'none');
}

function getTableConfiguration( tableId ){
	var rows = jQueryById( tableId+"_cols > tbody > tr");
	var toServer = "";
	for(i=1; i < (rows.length-2); i++ ){
		var rowId = rows.get(i).id;
		var inputs = jQueryById( rowId + " > td > input");
		if(inputs.get(1).checked){
			toServer += inputs.get(0).value + "," +inputs.get(2).value+ ";" ;
		}
	}
	var rowInput = jQuery( jQuery(rows.get( rows.length-2 )).children().get(1)).children("input").get(0);
	
	// prevent input of values smaller than 0
	if (rowInput.value < 1) {
		rowInput.value = 1;
	}	
	var config = new Array(  rowInput.value, toServer );
	currentConfig[ tableId ] = config;
	return config;
}

function onCancelTableEdit( tableId){
	
	jQuery('#'+tableId+'_editTableDiv').css( 'display', 'none'); 
	
	var rows = jQueryById( tableId+"_cols > tbody > tr");
	var rowInput = jQuery( jQuery(rows.get( rows.length-2 )).children().get(1)).children("input").get(0);
	rowInput.value = currentConfig[tableId][0];
	var splited = currentConfig[tableId][1].split(';');
	var beforeRow = rows.get(1);
	rows.find("input:checkbox").attr('checked',false);
	
	for(var i = splited.length-1; i >= 0; i-- ){
		parts = splited[i].split(',');
		if( parts == "" )
			continue;
		var selected = rows.find("input[ value='"+parts[0]+"' ]").parent().parent();;
		selected.find("input:checkbox").attr('checked',true);
		var currentRow = selected.get(0);
		var tbody = selected.parent().get(0);
		tbody.insertBefore( currentRow,beforeRow );
		beforeRow = currentRow;
	}
	return false;
}

function onCheckAll( id, colId ) {
	var check = jQueryById( colId+".selAll" )[0].checked;
	var displayedRows =  getTableRows( id );
	
	var $cols = jQuery( "#"+id+" .wgtTableColumnHeaderCell" );
	
	var collId = 0;
	for( var i = 0; i < $cols.size(); i++ ){
		if( $j( $cols.get(i) ).find( "#"+jQueryEscaped(colId+".selAll") ).size() == 1 ){
			collId = i;
			break;
		}
	}

	for( var i = 0; i < displayedRows; i++ ){
		var checkbox = $j( "#" + id + "\\.c" + collId + "\\.r" + i + ">input" )[0];
		if( checkbox.style.display != "none" ){
			checkbox.checked = check;
		}
	}
	
    eventParam( id, new Array( ""+check , colId ) );
	fireWGTEvent( id,'14' );
}

// Drag Drop
function initTableDroppable( tableId ){
	// init all rows of the table -> id = TaX.rY
	jQuery( '#'+tableId+' tr[id*=\''+tableId+'.r\']' ).droppable( { 
			accept: 'tr', 
			hoverClass: 'wgtDroppableHover',
			drop: function(ev, ui) {
				eventParam( tableId, new Array( ui.draggable.get(0).id, this.id ) );
		    	fireWGTEvent( tableId, '13' );
		    }
	    });
}
function initTableDraggable( tableId ){
	// init all rows of the table -> id = TaX.rY
	jQuery( '#'+tableId+' tr[id*=\''+tableId+'.r\']' ).draggable( {
			helper: function(ev, ui) {
				return jQuery('#dragObject').clone().attr( 'id', 'dragObject_clone' ).show();
		    }, 
			cursor: 'move',
			cursorAt: {top:5, left:5},
			appendTo: 'body',
			zIndex: 20,
			start: function(ev, ui) {
				eventParam( tableId, new Array( this.id ) );
		    	fireWGTEvent( tableId, '15' );
		    }
		} );
}

// Scrolling
var currentRow = new Array();

function initScrollbar( tableId ){
	console.debug("initScrollbar(" + tableId + ")" );
	var $drag = jQuery( '#'+tableId+'_drag');
/*
	$drag.css({
			"position": "absolute"
		});
*/
   	$drag.draggable({
   			axis: 'y',
   			containment: 'parent',
   			cursor: 'n-resize',
   			stop: function(ev, ui) {
					scrollToRelative(
						jQuery(this).parent().get(0), 
						jQuery(this).parents( 'table' ).get(1).id ,
						ui.position.top );
				}    			
   			});
   	resizeDrag( tableId, 0 );
}

function resizeDrag( tableId, rowNr ){
	currentRow[tableId] = rowNr;

	var $drag = jQuery( '#'+tableId+'_drag');
	if( $drag.size()==0 ) return;
	
	var $dragBar = jQuery( '#'+tableId+'_dragbar');
	var height = $dragBar.height();
	var allData = jQuery( '#'+tableId+'\\.size' ).html();
	var rowCount = getTableRows( tableId );
	
	var dragHeight = height; 
	if( allData > rowCount )
		dragHeight = height / ( allData/rowCount );
	if( dragHeight < 10 )
		dragHeight = 10;
	$drag.height( dragHeight );
	
	var newPos = rowNr / ( allData - rowCount ) * getScrollMaxPos(tableId);
	if( !newPos )
		newPos = 0;
	jQuery( $drag ).css({
			"top": newPos
		});
}
function calculateLineChange( dragBar, tableId, ereignis ){

	console.debug("calculateLineChange(" + tableId + " " + ereignis.clientY + ")" );
	if (!ereignis)
   		ereignis = window.event;

	var top = ereignis.clientY;
	var offset = jQuery(dragBar).offset().top;
	var winOffset = jQuery(document).scrollTop();
	var halfDrag = jQuery( '#'+tableId+'_drag' ).height()/2;
	var scrollMax = getScrollMaxPos(tableId);
	
	var newPos = top+winOffset-halfDrag;
	if( offset > newPos)
		newPos = offset;
	else if( newPos > ( scrollMax + offset ) )
		newPos = scrollMax + offset;
		
	
	jQuery( '#'+tableId+'_drag').css({
			"top": newPos
		});


	var allRows = byId( tableId+".size" ).innerHTML;
	var rowNr = ( ( newPos - offset ) / scrollMax * ( allRows - getTableRows( tableId ) ) ) | 0;
	currentRow[tableId] = rowNr;

//	console.debug( " top " + top + " offset " + offset + " winOffset " + winOffset + " halfDrag " + halfDrag + " newPos " + newPos + " scrollMax " + scrollMax + " rowNr " + rowNr );

	eventParam( tableId, new Array( ''+rowNr ) );
	fireWGTEvent( tableId ,'12');

}

function scrollTo(dragBar, tableId, newPos ){
	console.debug("scrollTo(" + tableId + " " + newPos + ")" );

	var offset = jQuery(dragBar).offset().top;

	scrollToRelative( dragBar, tableId, ( newPos - offset ) );
}
function scrollToRelative( dragBar, tableId, newPos ){
	console.debug("scrollToRelative(" + tableId + " " + newPos + ")" );
	var dragBarHeight = getScrollMaxPos(tableId);
	var allData = jQuery( '#'+tableId+'\\.size' ).html();
	var rowCount = getTableRows( tableId );
	
	var rowNr = ( newPos / dragBarHeight * (allData - rowCount) ) | 0;

	currentRow[tableId] = rowNr;

	eventParam( tableId, new Array( ''+rowNr ) );
	fireWGTEvent( tableId ,'12');
}

function getTableRows( tableId ){
	return jQuery( '#'+tableId+'>tbody>tr[id]' ).size();
}
function getScrollMaxPos( tableId ){
	return ( jQuery( '#'+tableId+'_dragbar').height()-jQuery( '#'+tableId+'_drag').height() );
}

var currentSelected;
function onRowIn( inputField ){
	    $row = jQuery( jQuery(inputField).parents('tr[id]').get(0) );
	    if( currentSelected && $row.attr('id') != currentSelected.attr('id') ){
	        currentSelected.find('input[type="text"]').removeClass( 'wgtTableInputText_on' );
	        currentSelected.find('input[type="image"]').hide();
	        $row.find('input[type="text"]').addClass( 'wgtTableInputText_on' );
	        $row.find('input[type="image"]').show();
	    }
	    else if( !currentSelected ){
	        $row.find('input[type="text"]').addClass( 'wgtTableInputText_on' );
	        $row.find('input[type="image"]').show();
	    }
	    currentSelected = $row;
}
function onRowOut( inputField ){
    if( inputField && currentSelected && inputField.id.indexOf(currentSelected.attr('id'))!=0 ){
	    currentSelected.find('input[type="text"]').removeClass( 'wgtTableInputText_on' );
	    currentSelected.find('input[type="image"]').hide();
    }
}
function onTab( inputField ){
	$row = jQuery( jQuery(inputField).parents('tr[id]').get(0) );
	$next = $row.next();
	if( $next.hasClass( 'wgtTableFooter' ) || $next.length == 0 ){
		$inputs = $row.find('input');
		if( $inputs.get($inputs.length-1) == inputField ){
			var tableId = toLastDot( $row.get(0).id );
			fireWGTEvent(  tableId , '0' );
			//get first input and set set focus
			var firstInput = $row.find('input[type!="hidden"]').get(0);
			firstInput.focus();
			return false;
		}
	}
	return true;
}
function onArrowDown( inputField ){
	cellId = jQuery(inputField).parents('td[id]').get(0).id;
	startC = cellId.indexOf( '.c' );
	startR = cellId.indexOf( '.r' );
	cellNo = cellId.substring( startC+2, startR );
	rowNo = cellId.substring( startR+2, cellId.length );
	type = inputField.type;
	
	newId = "#"+cellId.substring( 0,  startC ) + "\\.c" + cellNo + "\\.r" + (parseInt(rowNo)+1);
	$inputFileds = jQuery( newId + " input[type='"+type+"']");
	if( $inputFileds.length > 0 ){
		if( type == "image" )
			$inputFileds.show();
		$inputFileds.get(0).focus();
	}
	else{
		fireWGTEvent(  cellId.substring( 0, startC ) , '0' );
	}
}
function onArrowUp( inputField ){
	cellId = jQuery(inputField).parents('td[id]').get(0).id;
	startC = cellId.indexOf( '.c' );
	startR = cellId.indexOf( '.r' );
	cellNo = cellId.substring( startC+2, startR );
	rowNo = cellId.substring( startR+2, cellId.length );
	type = inputField.type;
	
	newId = "#"+cellId.substring( 0,  startC ) + "\\.c" + cellNo + "\\.r" + (parseInt(rowNo)-1);
	$inputFileds = jQuery( newId + " input[type='"+type+"']");
	if( $inputFileds.length > 0 ){
		if( type == "image" )
			$inputFileds.show();
		$inputFileds.get(0).focus();
	}
	else{
		fireWGTEvent(  cellId.substring( 0, startC ) , '3' );
	}
}

function initTableEditor( tableId ){
	// init all rows of the table -> id = TaX.rY
	jQuery( '#'+tableId+'_cols tr[id*=\'_conf\']' ).draggable( {
			helper: function( event ) {
				var theTd = this.children[1];
				return jQuery( "<div>"+theTd.innerHTML+"</div>" );
			},
			cursor: 'move',
   			containment: '#'+tableId+'_cols',
			cursorAt: {top:5, left:-10},
			appendTo: 'body',
			zIndex: 20
		} );
		
	jQuery( '#'+tableId+'_cols tr[id*=\'_conf\']' ).droppable( { 
			accept: '#'+tableId+'_cols tr[id*=\'_conf\']', 
			hoverClass: 'wgtDroppableHover',
			drop: function(ev, ui) {
				var currentRow = ui.draggable;
				var beforeRow = jQuery( this );
				var tbody = currentRow.parent().get(0);
				tbody.insertBefore( currentRow.get(0),beforeRow.get(0) );
		    }
	    });
		
}

function changeTableRowWidth( input, step ){
	console.debug("changeTableRowWidth(" + input.id + " " + input.value + ")" );
	var inputVal = input.value;
	var intVal = parseInt(input.value);
	var preFix = "";
	var postFix = "";
	if( intVal ){
		preFix = inputVal.substring( 0, inputVal.indexOf(intVal+"" ) );
		postFix = inputVal.substring( inputVal.indexOf(intVal+"") + (intVal+"").length );
		console.debug( preFix + " " + postFix );
	}
	else
		intVal = 0;
	
	newVal = intVal + ( step * 10 );
	if( newVal > 0 ){
		input.value = preFix + newVal + postFix;
	}
}

function changeTableRows( input, step ){
	console.debug("changeTableRows(" + input.id + " " + input.value + ")" );
	var newPos = parseInt(input.value) + step;
	if( newPos > 0 ){
		input.value = newPos;
	}
}
/*
################################### End Table ########################################
*/

