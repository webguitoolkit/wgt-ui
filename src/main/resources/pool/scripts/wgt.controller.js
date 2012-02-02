// Depends: jsconsole.js, jquery/jquery-1.5.1.js, loadIndicator.js
/**
 * ################### Controller #############################
 * 
 * WebGuiToolkit - Base JavaScript
 *
 * Copyright (c) 2008 Endress+Hauser (www.infoserve.endress.com)
 *
 * $Date: 2008-04-02
 * 
 * File: wgt.controller.js
 */
document.write("<div id='constructionZone' style='display: none;'></div>");

jQuery.noConflict();
var $j = jQuery;

var busy = false;
var queue = new Array(); 
var callStart;
var ctxApplyer = new Array();
var ctxChangeCalculator = new Array();

dwr.engine.setAsync(true);

// Events ------------------------------
function fireWGTEvent(source, eventName) {

	if( busy ){
		queue.push(source);
		queue.push(eventName);
	    var chg = calculateContextChanges();
		queue.push(chg);
	}
	else{
	    busy = true;

		callStart = new Date().getTime();
		//DWREngine.setMethod(DWREngine.IFrame);
	    var chg = calculateContextChanges();
		DWRCaller.event(source, eventName, chg, fireWGTEventBack);

//      for performance debuging comment this line in
//      console.debug( "fireWGTEvent" + new Date().getTime() );
//		console.time("fireWGTEvent");
	}
}
function processeQueue() {
	if( queue.length>1 ){
		// check if the last call cleaned up the iframe before doing new call
		if(  document.getElementById('dwr-if-0') ){
			console.debug( "iframe exists" );
			window.setTimeout( "processeQueue()",50 ); 
			return;
		}
		var source = queue.shift();
		var eventName = queue.shift();
	    var chg = queue.shift();
		
		callStart = new Date().getTime();
		DWRCaller.event(source, eventName, chg, fireWGTEventBack);
	}
	else
    	busy = false;
}

function fireWGTEventBack(retContext) {
// measure time for applying the context

//  for performance debuging comment this line in
//  console.timeEnd("fireWGTEvent");
//  console.debug( "fireWGTEventBack" + new Date().getTime() );
	var msstart = new Date().getTime();
	// disable loadIndicator if we are usng it
	disableLoadImage();
//	window.setTimeout( "processeQueue()",50 ); 
    applyContext(retContext);
    var elContext = (new Date().getTime()) - msstart;
    var callTime = msstart - callStart;
    toServer('Time for applying the context: ', elContext);
    toServer('Time for the call: ', callTime + ' ms.');
    processeQueue();
}

function toServer(who,what) {
	var ce = new Object();
	ce.cssId = who;
	ce.value = what;
	ce.type = "timing";
	ce.status = "s";
	changes2server[changes2server.length] = ce;
}
/**
* eventParam put an array of string into the context
* using a special key, so they can be retrieved on serverside 
* as parameters of the event.
* see Event.java
*/
function eventParam(id, params) {
    if ( params ) {
    	// add parameters
	    for (var i in params) {
	    	var p = createContextElement( id + ".ev.p" + i, params[i], "val", "s" );
	    	changes2server[changes2server.length] = p;
	    }
	    
	    // add size
    	var p = createContextElement( id + ".ev.size", params.length, "val", "s" );
    	changes2server[changes2server.length] = p;
    } 
}

function createContextElement( id, value, type, status ){
	var p = new Object();
	p.cssId = id;
	p.value = value;
	p.type = type;
	p.status = status;
	return p;
}

// the client side context
var context = new Array();
// context additions to be send to the server
var changes2server = new Array();

function jQueryEscaped( id ){
	return id.replace(/\./g, "\\.");
}
function jQueryById( id ){
	return jQuery( "#"+jQueryEscaped( id ) );
}

function putContext(ce){
	// let figure out if there is an element already with that id
	for (j=0;j<context.length;j++) {
		if (ce.cssId === context[j].cssId) {
			break;
		}
	}
	context[j] = ce;
}

function removeFromContext( id ){

	// select positions of context elements that have to be deleted
	var toDelete = new Array();
	for (i=0;i<context.length;i++) {
		if ( context[i].cssId.indexOf( id+"." ) == 0 || context[i].cssId == id  ) {
			toDelete.push( i );
		}
	}

	// remove all collected context elements (highest first)
	toDelete.reverse();
	for (i=0;i<toDelete.length;i++) {
		context.splice(toDelete[i], 1 );
	}
}
/**
 * this calculates the change in the context and returns the 
 * change as array in the return parameter
 * the context variable is changed as well.
 */
function calculateContextChanges() {
	var chgContext = new Array();
	chgContext = chgContext.concat(changes2server);
	changes2server = new Array();
	// now add the changes you find in the dom
	for (var j in context) {
		var ce = context[j];
		for( var i = 0; i < ctxChangeCalculator.length; i++ ){
			done = ctxChangeCalculator[i]( ce, chgContext );
			if( done )
				break;
		}
	}
	return chgContext;
}


/**
 * This applies the context returned from the request on the DOM Tree.
 */
var aktContext ;
function applyContext(con, offset) {
	if( !offset )
		offset = 0;

	var stopApplyingContext = false;
	
	// if null return, nothing to apply
	if( !con )
		return;
	
	for (var j = offset; j < con.length; j++ ) {
		aktContext = con[j];

		var done = false;
		for( var i = 0; i < ctxApplyer.length; i++ ){
			if( aktContext.type == 'llj' ){
				// in the callback function we continue to apply the context
				jQuery.getScript( aktContext.value, 
						function(){ 
//							console.debug('loaded script: ' + aktContext.value ); 
							// process the context starting at the next value the next value
							applyContext(con, j+1); 
						} ); 
				stopApplyingContext = true;
				break;
			}	
			done = ctxApplyer[i]( aktContext );
			if( done )
				break;
		}

		// if we have to lazy load a javascript, break here because the needed script is not loaded jet
		if( stopApplyingContext )
			break;
		
		if (aktContext.status=="e") {
			putContext( aktContext );
		}
		
		if(!done){
			console.warn( "Context not applied! "+ aktContext.type );
		}
	}
}

function registerApplyer( contextApplyer ){
	ctxApplyer[ ctxApplyer.length ] = contextApplyer;
}
function registerChangeCalculator( contextChangeCalculator ){
	ctxChangeCalculator[ ctxChangeCalculator.length ] = contextChangeCalculator;
}

/*
 * ################### END Controller #############################
 */ 
