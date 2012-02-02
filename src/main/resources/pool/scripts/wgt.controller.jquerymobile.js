// Depends: wgt.controller.base.js, jquery/jquery.mobile-1.0a3.js

/**
 * ################### JQ Touch #############################
 *
 * Scripts used for the sorted list control
 * 
 * File: wgt.controller.jqtouch.js
 * 
 * Author: Peter Zaretzke, Martin Hermann
 */

registerApplyer( function(aktContext) {
	switch (aktContext.type) {
		case "checkC": // trigger sending local data to backend
			checkLocalStore(toLastDot( aktContext.cssId ), aktContext.value);
			break;
		case "localC": // trigger sending local data to backend
			toLocalCache(fromLastDot( aktContext.cssId ), aktContext.value);
			break;
		case "sessionC": // trigger sending local data to backend
			toSessionCache( fromLastDot( aktContext.cssId ), aktContext.value);
			break;
		default:
			return false;
	}
	return true;
});
registerChangeCalculator( 
		function( ce, chgContext ){
			if (ce.type == "localC") { // transfer the value of the combobox to server
				var value = fromLocalCache(fromLastDot(ce.cssId));
				if ( value!=null && ce.value != value ) {
					ce.value = value; // selection has changed
				    chgContext[chgContext.length]=ce;
				}
				return true;
			}
			else if (ce.type == "sessionC") { // transfer the value of the combobox to server
				var value = fromSessionCache(fromLastDot(ce.cssId));
				if ( value!=null && ce.value != value ) {
					ce.value = value; // selection has changed
					chgContext[chgContext.length]=ce;
				}
				return true;
			}
			return false;
		}
	);


function checkLocalStore( componentId, keys ){
//	var para = new Array();
//	para[0] = localStorage.getItem( keys );

//    eventParam( document.body.id, params );
	fireWGTEvent(componentId, "checkC" );
}

function toLocalCache( key, value ){
	if( value != '_wgt_initial' )
		localStorage.setItem(key, value);
}

function toSessionCache( key, value ){
	if( value != '_wgt_initial' )
		sessionStorage.setItem(key, value);
}
function fromLocalCache( key ){
	return localStorage.getItem( key );
}

function fromSessionCache( key ){
	return sessionStorage.getItem( key );
}

/*
# localStorage.setItem(,key‘, ,value‘);
Speichert einen Wert unter dem angegebenen Schlüssel.
# localStorage.getItem(,key‘);
Liest den Wert vom angegebenen Schlüssel ein. Existiert dieser nicht so wird „undefined“ zurückgegeben. Alternativ kann auch mit localStorage.key auf die Daten zugegriffen werden.
# localStorage.removeItem(,key‘);
Entfernt den kompletten Datensatz aus dem Localstorage. Wiederherstellen geht nicht (leider).
# localStorage.clear();
 */


/**
 * overwrite default page laoding
 */
function showindicator() {
	if (!notshow)
		jQuery.mobile.pageLoading();
}

/* this functionality is declared in loadIndicator.js
*/
function disableLoadImage() {
	jQuery.mobile.pageLoading( true );
	notshow = true;
}
