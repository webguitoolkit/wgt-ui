// Depends: wgt.controller.base.js, jquery/ui/1.8.1/jquery.ui.core.js, jquery/ui/1.8.1/jquery.ui.widget.js, jquery/ui/1.8.1/jquery.ui.mouse.js, jquery/ui/1.8.1/jquery.ui.sortable.js

/**
 * ################### Sorted List #############################
 *
 * Scripts used for the sorted list control
 * 
 * File: wgt.controller.list.js
 * 
 * Author: Peter Zaretzke, Martin Hermann
 */
registerApplyer( function(aktContext) {
	switch (aktContext.type) {
	case "listI":
		processEvent(aktContext.cssId);
		break;
	case "listE":
		enableSort(aktContext.value);
		break;
	case "listD":
		disableSort(aktContext.value);
		break;
	case "listU":
		disableSortItem(aktContext.value);
		break;
	case "listH":
		defineHandle(aktContext.value);
		break;
	case "listC":
		connectWith(aktContext.value);
		break;
	default:
		return false;
	}
	return true;
});

function processEvent(listId) {
	jQuery("#" + listId).sortable(
			{
				update : function(event, ui) {
				
				// get the lists id's as array
				var LIlist = jQuery("#" + listId).sortable('toArray');	      
							
				// add the object that was moved to the first position
				var itemId = ui.item.children().get(0).id;
				// console.info('EVENT UPDATE on ' + listId + ' ' +  itemId);
						
				// use DIV id's instead of LI id's
				var DIVlist =  getDIVids(LIlist);
				
				// Check if the item is in the list already to distinguish sort and drop
				var contains = false;
				
				for ( var i = 0; i < DIVlist.length; i++) {
					// console.info('CONTAINS : ' + itemId + ' =? ' + DIVlist[i] );
					
					if (itemId == DIVlist[i]) {
						contains = true;
						break;
					}
				}
				
				// console.info('CONTAINS = ' + contains );				
				
				// create the event parameters
				var received = new Array(itemId);
				var result = received.concat(DIVlist);
				eventParam(listId, result);
			
				if (contains) {
					// if not in list -> remove case (processing in remove event)
					// reason: the sequence of the events from jQuery is strange
					var action = jQuery("#" + listId).attr("wgtaction");
					if ( !action || action == "") {
						// if action has any value it is drag/drop processing
						// other wise sort processing
						fireWGTEvent(listId, "item_sort");
						// console.info('SORT on ' + listId  + ' [' + itemId + '] ' + result );
					}
				} 
					
				jQuery("#" + listId).attr("wgtaction", "");
				
			},
			receive : function(event, ui) {
				var itemId = ui.item.children().get(0).id;
				
				// console.info('EVENT RECEIVE on ' + listId + ' ' +  itemId);
				
				var LIlist = jQuery("#" + listId).sortable('toArray');	      
				// var DIVlist = new Array(LIlist.length);
							
			
				// use DIV id's instead of LI id's

				var DIVlist = getDIVids(LIlist);
				
				// create the event parameters
				var received = new Array(itemId);
				var result = received.concat(DIVlist);
				eventParam(listId, result);
				fireWGTEvent(listId, "item_receive");
				
				// depending on the sequence oft the JQuery events. 
				// Here we mark that receive was raised to process later 
				// Expected: 1. receive 2. update
				
				jQuery("#" + listId).attr("wgtaction", "receive");
			},
			remove : function(event, ui) {
				var itemId = ui.item.children().get(0).id;
				// console.info('EVENT REMOVE on ' + listId + ' ' + itemId);
				
				var result = new Array(itemId);
				eventParam(listId, result);
				fireWGTEvent(listId, "item_remove");
				jQuery("#" + listId).attr("wgtaction", "remove");
			}
			});
	jQuery("#" + listId).disableSelection();
}

function getDIVids ( LIlist ) {
	var DIVlist = new Array(LIlist.length);
	for ( var i = 0; i < LIlist.length; ++i) {
		DIVlist[i] = jQuery("#" + LIlist[i]).children().get(0).id;
		// console.info('LIST [' + i +'] :' + DIVlist[i] );
	}
	return DIVlist;
}

function enableSort(listId) {
	jQuery("#" + listId).sortable('enable');
}

function disableSort(listId) {
	jQuery("#" + listId).sortable('disable');
}

function connectWith(value) {
	var parts = value.split("|");
	var conector = jQuery("#" + parts[0]);
	conector.sortable('option', 'connectWith', "#" + parts[1]);
}

function disableSortItem(value) {
	var parts = value.split("|");
	// console.info('DISABLE SORT on ' + parts[1] );
	jQuery("#" + parts[0]).sortable('option', 'cancel', "#" + parts[1]);
}

function defineHandle(value) {
	var parts = value.split("|");
	// console.info('DEF HANDLE ' + parts[1] );
	jQuery("#" + parts[0]).sortable('option', 'handle', "#" + parts[1]);
}