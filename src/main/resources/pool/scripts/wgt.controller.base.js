// Depends: wgt.controller.js
/**
 * ################### Base functions #############################
 * 
 * Scripts with some core functionality of the Web Gui Toolkit
 * 
 * File: wgt.controller.base.js
 * 
 * Author: Martin Hermann, Arno Schatz, ...
 */

registerApplyer( function(aktContext){
		switch(aktContext.type) {
			case "rce": // remove context elements
				removeFromContext( toLastDot(aktContext.cssId) );
				break;
			case "val": // set value of input
				byIdReq(aktContext.cssId).value = aktContext.value;
				break;
			case "txt": // set text no
				var theNode = byIdReq(aktContext.cssId);
				var fisrtChild = theNode.firstChild;
				if ((fisrtChild) && (fisrtChild.data)) {
					fisrtChild.data = aktContext.value;
				} else {
					theNode.insertBefore(document.createTextNode(aktContext.value), fisrtChild);
				}
				break;
			case "nop": // no operation
				break;
			case "alr": // show alert box
				alert(aktContext.value);
				break;
			case "cop":
				clearOption(toLastDot(aktContext.cssId), aktContext.value);
				break;
			case "sel":
				byIdReq(aktContext.cssId).checked = (aktContext.value=="true");
				break;
			case "ro":
			case "rw": // make read-only / writable
				statusChange(aktContext.value, (aktContext.type=="ro")); // multiple
																			// elements
																			// comma
																			// seperated
				break;
			case "vis":// set the visibility of an element
				show(byIdReq(toLastDot(aktContext.cssId)), "true"==aktContext.value);
				break;
			case "htm":// insert HTML into a elements
				innerHTML2DOM(byIdReq(aktContext.cssId), aktContext.value);
				break;
			case "out":// reconstruct a node with this (outer) html
				if(!byId(toLastDot(aktContext.cssId))&&fromLastDot(aktContext.cssId)=="redraw") {
					//doing nothing is okay here! (this fix is used for async view loading in iba)
				} else {
					outerHTML2DOM(byIdReq(toLastDot(aktContext.cssId)), aktContext.value);
				}
				break;
			case "att": // set an attribute of an element
				putAttribute(byIdReq(toLastDot(aktContext.cssId)), fromLastDot(aktContext.cssId), aktContext.value)
				break;
			case "url":// reload window with new url
				gotoUrl(aktContext.value);
				break;
			case "foc":// put focus on selected element
				byIdReq(toLastDot(aktContext.cssId)).focus();
				break;
			case "rpl":// repicate a DOM node n-times
				replicate(byIdReq(aktContext.cssId), aktContext.value);
				break;
			case "rem":// remove a DOM node
				remove(byId(aktContext.cssId));
				break;
			case "cad": // class add
				classAdd(byIdReq(toLastDot(aktContext.cssId)), aktContext.value);
				break;
			case "crm":// class remove
				classRemove(byIdReq(toLastDot(aktContext.cssId)), aktContext.value);
				break;
			case "cex":// class set if exists
				setClass(byIdReq(toLastDot(aktContext.cssId)), 
					fromLastDot(aktContext.cssId), aktContext.value=="true"); 
				break;
			case "js":// evaluate Java script directly
				eval( aktContext.value );
				break;
			case "ci":// clone and insert a node
				cloneInsert( byIdReq(aktContext.cssId), byIdReq(toLastDot(aktContext.value)), fromLastDot(aktContext.value) );
				break;
			case "ca":// clone and add a node, cssId: node to be cloned,
						// value: <parentNode>.<suffix to be appended to Ids in
						// new node>
				cloneAdd( byIdReq(aktContext.cssId), byIdReq(toLastDot(aktContext.value)), fromLastDot(aktContext.value) );
				break;
			case "tts":// take html snapshot (store dom node in variable)
				taksSnapshot( aktContext.cssId, byIdReq(toLastDot(aktContext.cssId)) );
				break;
			case "trs":// restore snapshot previously taken
				restoreSnapshot( aktContext.cssId, byIdReq(toLastDot(aktContext.cssId)), aktContext.value=="true");
				break;
			case "cs": // create sibling(s) from html string
				loadSibling( byIdReq( aktContext.cssId ), aktContext.value );
				break;
            case "fui": // file upload info
                var val =  eval(aktContext.value);
                updateUploadProgress( val, toLastDot(aktContext.cssId) );
                break;
            case "ext": // external event
                eval("var val= " + aktContext.value);
                callFrame( val.frame, val.parameter );
                break;
            case "ala": // add to node as last child
            	addAsChild( toLastDot( aktContext.cssId ), aktContext.value );
                break;
            case "afi": // add to node as first child
            	insertAsChild( aktContext.cssId, aktContext.value );
                break;
            case "apa": // add to node as first child
            	domAppendAfter( toLastDot( aktContext.cssId ), aktContext.value );
                break;
            case "rep": // replace node
            	replaceNode( aktContext.cssId, aktContext.value );
                break;
            case "scr": // reload scrollbar
            	resizeDrag( aktContext.cssId, aktContext.value  );
            	break;
			case "tev":// set the visibility of an tree table element
				treeRootVis( toLastDot( aktContext.cssId ), "true"==aktContext.value );
				break;
            case "cmb":// bind contexte menu to an element
                contextMenuBind( toLastDot(aktContext.cssId), aktContext.value );
                break;
            case "llcss":// bind contexte menu to an element
				jQuery.get( aktContext.value, 
						function( response ){ 
							console.debug('loaded style: ' + aktContext.value ); 
// that.target.append('<style type="text\/css" rel="stylesheet"
// data-file-id="'+fileId+'">'+response+'<\/style>');
							jQuery('head').append('<style type="text\/css" rel="stylesheet">'+response+'<\/style>');
						} ); 
            	break;
            default: 
            	return false;
            	
		}
		return true;
	});

registerChangeCalculator( 
	function( ce, chgContext ){
		// here begin the type which work on dom elements directly
		// ------------------------
		domElem = byId(ce.cssId);
		if (!domElem) return false;
		switch( ce.type ) {
			case "val":
				if (ce.value != domElem.value) {
				   // change detected!
				   ce.value = domElem.value;
				   chgContext[chgContext.length]=ce;
				   // chgContext[chgContext.length]=new
					// ContextElement(ce.cssId,ce.value.ce.type,ce.status);
				}
				break;
			case "sel":
				// context element may need type conversion
				if (typeof(ce.value)=="string") {
					ce.value = (ce.value=="true");
				}
				if (ce.value != domElem.checked) {
					ce.value = domElem.checked; // selection has changed
				    chgContext[chgContext.length]=ce;
				}
				break;
			case "tab": // get the selected tab number of the tabstrip
				var tabno = whichTab(ce.cssId);
				if (ce.value != tabno) {
					ce.value = tabno; 
				    chgContext[chgContext.length]=ce;
				}
				break;
		}
		
	}
)


function trim(mystring) {
    mystring = String(mystring);
	if (mystring.substring(0,1)==" "){ // dann f?hrende Leerzeichen entfernen
		  mystring=mystring.substring(1,mystring.length);
	};
	if (mystring.substring(mystring.length-1,mystring.length)==" "){ // u.
																		// Leerzeichen
																		// am
																		// Ende
		  mystring=mystring.substring(0,mystring.length-1);
	};
	return mystring;
}
/*
 * manage the visibility of an dom-element elem. vis is boolean
 */
function show(elem, vis) {
	elem.style.display = vis ? "" : "none";
}
function isShown(elem) {
	return !elem.style.display || elem.style.display == "";
}
function isRealyShown( elem ){
	var tempElement = jQuery( elem );
	return !(tempElement.is(':hidden') || tempElement.parents(':hidden').length);
}
function showSelect(elem, visDis) {
	// search all children
	for (chind in elem.childNodes) {
		var child = elem.childNodes[chind]; // i love js
		if (child.tagName == "SELECT" && visDis == "") {
			child.style.display = visDis;
		}
		if (child.style && !child.style.display) {
			showSelect(child, visDis);
		}
	}
}

function statusChange(sc, ro) {
	if (sc=="") return; // nothing to do
	var ids = sc.split(",");
	for (var i=0;i<ids.length;i++) {
		var elem = byIdReq(ids[i]);
		if (elem.tagName=="SELECT" || elem.type=="radio"
			 ||  elem.type=="checkbox" ) {
			elem.disabled = ro;
		}

		if ((elem.tagName=="INPUT" && (elem.type=="text" || elem.type=="password")) 
					|| elem.tagName=="TEXTAREA"
					) {
			elem.readOnly = ro;
			if (ro) {
				classAdd(elem, "wgtReadonly");
			} else {
				classRemove(elem, "wgtReadonly");
			}
		}		
		if (elem.tagName=="DIV") { 
		  if(elem.getAttribute("wgttype")!=null && elem.getAttribute("wgttype")=="multi"){
		      // multiselect
            disableAllMultiselect(ids[i], ro);    
		  }
		}
	}
}

function classAdd(elem, className) {
    jQuery(elem).addClass( className );
}

function classRemove(elem, className) {
    jQuery(elem).removeClass( className );
}

function toggleVisi(elemid){
    var elem = byId(elemid);
    var vis = (elem.style.display == 'none') ? 'block' : 'none';
    elem.style.display = vis;
    return vis;        
}

/*
 * finds the last dot of a string and return the left string from the dot
 * position
 */
function toLastDot(text) {
	if ((typeof text)== "string")
		return text.substring(0, text.lastIndexOf("."));
	else {
		alert("expected string "+text+ " is of type "+(typeof text));
		return "";
	}
}

function fromLastDot(text) {
	if ((typeof text)== "string")
		return text.substring(text.lastIndexOf(".")+1);
	else {
		alert("expected string "+text+ " is of type "+(typeof text));
		return "";
	}
}


/*
 * remove all children von the node and create children with empty value,
 * firsttextchild. (will be filled later in context)
 */
function clearOption(nodeid, newnumber) { 
	var node = byIdReq(nodeid);
	while (node.hasChildNodes()) {
		node.removeChild(node.firstChild);
	}
	var opt;
	for (var i=0;i<newnumber;i++) {
		opt = document.createElement("OPTION");
		opt.appendChild(document.createTextNode(""));
		opt.id = nodeid + ".o" + i;
		node.appendChild(opt);
	}
	// determin visibility status... BUG IE6
	/*
	 * var upperNode = node; var visDis = ""; while (upperNode != null &&
	 * upperNode.parentNode != upperNode) { if (upperNode.style &&
	 * upperNode.style.display == "none") { visDis = "none"; break; } upperNode =
	 * upperNode.parentNode; } node.style.display = visDis;
	 */
}


function byIdReq(id) {
	var elem = byId(id);
	if (elem==null) {
	    var xInfo;
	    if (aktContext) {
	    	try {
	    	   xInfo = " current context ["+aktContext.cssId+", " + aktContext.value +
	    	      ", " + aktContext.type + ", " + aktContext.status +"]";
	    	} catch (e) {
	    	}
	    }
		alert("could not find id="+id+xInfo );
	} else {
		return elem;
	}
}

var wgt_idcache = new Object();
function byId(id) {

	if( !wgt_idcache )
		wgt_idcache = new Object();
	
	var ca = wgt_idcache[id];
	if (ca) {
		if (ca==="0") {
			return null; // placeholder fr negative search results
		} else {
			return ca;
		}
	}
	
	ca = window.document.getElementById(id);
	if (ca) {
		wgt_idcache[id] = ca;
	} else {
		wgt_idcache[id] = "0"; // constant for negative results
	}
	return ca;
}

function fillIdCache() {
	var bd = window.document.body;
	subNodeCache(bd.firstChild);
	if (bd.id) wgt_idcache[bd.id] = bd;
	
}

function subNodeCache(node) {
	var nc;
	var child = node;
	do  {
		if (child.nodeType==1) {
			if (child.id) wgt_idcache[child.id] = child;
			nc = child.firstChild;
			if (nc) 
				subNodeCache(nc);
		}
	} while (child = child.nextSibling);
}

/*
 * highlight any domelemnt by adding the css class highlite. It also removes the
 * class highlite from the last element it has set highlite. for multiple
 * highligths you can pass the light as identifying string
 */
var highOldElem = new Array();
var highOldClass = new Array();
var map2Index = new Array();

function highlite(elem, light) {
	var lnr;
	// find index of existing or create
	for (lnr=0;lnr<map2Index.length;lnr++) {
		if (map2Index[lnr] == light) break;
	}
	map2Index[lnr] = light; // if doesnt exist
	
	if (highOldElem[lnr]) {
		highOldElem[lnr].className = highOldClass[lnr];
	}
	if (!elem) return;
	highOldElem[lnr] = elem;
	highOldClass[lnr] = elem.className;
	elem.className = elem.className + " wgtHighlight";
}

/*
 * check the input for an regexp. constists of two steps: 1. keydown: remember
 * the value (to restore) 2. keyup: check for regexp, restore if neccessary.
 * must be caleed in short sequence
 */ 
var oldValue;
function enterCheck(elem) {
	oldValue = elem.value;
}
function exitCheck(elem, regex) {
	re = new RegExp(regex);
	if (elem.value.match(re)==null) {
		// restore
		elem.value = oldValue;
	}
}


/*
 * putAttribute is like setAttribute, but 'style'-aware
 */
function putAttribute(theNode, att, newValue) {
if (theNode==null) alert('putAttribute theNode is null.');
	if (att == 'style') {
		var styles = newValue.split(';');
		for (var i = 0; i < styles.length; ++i) {
			if (trim(styles[i])=="") continue;
			var tstyle = styles[i].split(':');
			var styleKey = toJsStyle( trim(tstyle[0]) );
			theNode.style[styleKey] = trim(tstyle[1]);
		}
	} else if( att == 'class' ){
		theNode.className = newValue;
	} else {
		if (newValue == null) {
			theNode.removeAttribute( att );
		} else {
			theNode.setAttribute(att, newValue);
		}
	}
}
function toJsStyle( inStyle ){
	var result = "";
	var styles = inStyle.split('-');
	for( var i = 0; i < styles.length; ++i ) {
		if( i == 0 ){
			result = styles[i].toLowerCase();
		}
		else{
			result += styles[i].substring(0,1).toUpperCase();
			result += styles[i].substring(1).toLowerCase();
		}
	}
	return result;
}

/*
 * jump to a new url in this window
 */
function gotoUrl(url) {
	window.location = url;
}


/**
 * replicate -- an dom nde should be replicated n times, and the ids withinthe
 * replication get altered: the numer of the iteration will be appended to each
 * id.
 */
function replicate(node, times) {
	var father = node.parentNode;
	if (!father) alert('replicate error, node id: ' + node.id);
	var insertAt = node.nextSibling;
	for (var i=0;i<times;i++) {
		var cl = node.cloneNode(true);
	// durchgehen und ids anpassen
		appendId( cl, i );
		father.insertBefore( cl, insertAt );
	}
}

function appendId( node, num) {
	if (node.id) {
		wgt_idcache[node.id] = null; // delete cache
		node.id = node.id+num;
		wgt_idcache[node.id] = node; // fill cache with new val
	}
	for (var child=node.firstChild; child; child = child.nextSibling) {
		appendId( child, num );
	}
}

function remove(node) {
	if (!node || !node.parentNode) 
		return;
	removeIdFromCache(node);
	node.parentNode.removeChild(node);
}
/**
 * removing recursively the ids from the cache, node can be delete afterwards
 */
function removeIdFromCache(node) {
	if (node.id) {
		wgt_idcache[node.id] = null;
	}
	for (var child=node.firstChild; child; child = child.nextSibling) {
		removeIdFromCache( child );
	}
	
}

function cloneInsert(fromNode, insertNode, postfix) {
	var theClone = fromNode.cloneNode(true);
	appendId(theClone, postfix);
	insertNode.parentNode.insertBefore(theClone, insertNode);
}
function cloneAdd( fromNode, parentNode, postfix) {
	var theClone = fromNode.cloneNode(true);
	appendId(theClone, postfix);
	parentNode.appendChild(theClone);
}
/*
 * function to take a html snapshot. It makes a deep copy of the dom node, so it
 * can be restored later on.
 */
function taksSnapshot(snapkey, node) {
	snaps[snapkey] = node.cloneNode(true);
}
// associative array for snapshots
var snaps = new Array();
/*
 * restore a snapshot which was taken earlier
 */
function restoreSnapshot(snapkey, node, del) {
	var newNode = snaps[snapkey];
	if (!newNode) {
		alert('no client-snapshot found for '+snapkey);
		return;
	}
	var youngerBrother = node.nextSibling;
	var dady = node.parentNode;
	dady.removeChild( node );
	if (del==true) {
		// remove from the array
		snaps[snapkey] = null;
	} else {
		// work on copy to reuse the snapshot
		newNode = newNode.cloneNode(true);
	}
	// insert the new clone into the real DOM-tree
	if (youngerBrother==null) {
		dady.appendChild( newNode);
	} else {
		dady.insertBefore(newNode, youngerBrother);
	}
}

/**
 * drag and drop canvas
 */

// Das Objekt, das gerade bewegt wird.
var dragobjekt = null;
// Position, an der das Objekt angeklickt wurde.
var dragx = 0;
var dragy = 0;
// Mausposition
var posx = 0;
var posy = 0;

function draginit() {
 // Initialisierung der �berwachung der Events
  document.onmousemove = drag;
  document.onmouseup = dragstop;
}

function dragstart(element) {
   // Wird aufgerufen, wenn ein Objekt bewegt werden soll.
  dragobjekt = element;
  document.onselectstart = new Function("return false");
  document.body.style.MozUserSelect = "none";
  dragx = posx - dragobjekt.offsetLeft;
  dragy = posy - dragobjekt.offsetTop;
  dragobjekt.style['cursor'] = 'move';
}

function dragstop() {
  // Wird aufgerufen, wenn ein Objekt nicht mehr bewegt werden soll.
  if (dragobjekt) {
	  dragobjekt.style['cursor'] = 'auto';
}
  dragobjekt=null;
  document.onselectstart = null;
  document.body.style.MozUserSelect = "";
}

function drag(ereignis) {
  // Wird aufgerufen, wenn die Maus bewegt wird und bewegt bei Bedarf das
	// Objekt.
  posx = document.all ? window.event.clientX : ereignis.pageX;
  posy = document.all ? window.event.clientY : ereignis.pageY;
  if(dragobjekt != null) {
    dragobjekt.style.marginTop = 0;
	dragobjekt.style.marginLeft = 0;  
	if (posx - dragx > 0) {
   		dragobjekt.style.left = (posx - dragx) + "px";
   	 }
  	 if (posy - dragy > 0) {
    	dragobjekt.style.top = (posy - dragy) + "px";
    }
  }
}

// Returns array with x,y page scroll values.
// Core code from - quirksmode.org
function getPageScroll(){

	var yScroll;
	var xScroll;
	
	if (self.pageYOffset) {
		yScroll = self.pageYOffset;
	} else if (document.documentElement && document.documentElement.scrollTop){	 // Explorer
																					// 6
																					// Strict
		yScroll = document.documentElement.scrollTop;
	} else if (document.body) {// all other Explorers
		yScroll = document.body.scrollTop;
	}

	if (self.pageXOffset) {
		xScroll = self.pageXOffset;
	} else if (document.documentElement && document.documentElement.scrollLeft){ // Explorer
																					// 6
																					// Strict
		xScroll = document.documentElement.scrollLeft;
	} else if (document.body) {// all other Explorers
		xScroll = document.body.scrollLeft;
	}
	arrayPageScroll = new Array(xScroll,yScroll);
	return arrayPageScroll;
}



// Returns array with page width, height and window width, height
// Core code from - quirksmode.org
function getPageSize(){
	var xScroll, yScroll;
	if (window.innerHeight && window.scrollMaxY) {	
		xScroll = document.body.scrollWidth;
		yScroll = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all
																			// but
																			// Explorer
																			// Mac
		xScroll = document.body.scrollWidth;
		yScroll = document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla
				// and Safari
		xScroll = document.body.offsetWidth;
		yScroll = document.body.offsetHeight;
	}
   	var windowWidth, windowHeight;
	if (self.innerHeight) {	// all except Explorer
		windowWidth = self.innerWidth;
		windowHeight = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer
																					// 6
																					// Strict
																					// Mode
		windowWidth = document.documentElement.clientWidth;
		windowHeight = document.documentElement.clientHeight;
	} else if (document.body) { // other Explorers
		windowWidth = document.body.clientWidth;
		windowHeight = document.body.clientHeight;
	}	
	// for small pages with total height less then height of the viewport
	if(yScroll < windowHeight){
		pageHeight = windowHeight;
	} else { 
		pageHeight = yScroll;
	}

	// for small pages with total width less then width of the viewport
	if(xScroll < windowWidth){	
		pageWidth = windowWidth;
	} else {
		pageWidth = xScroll;
	}
	arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight);
	return arrayPageSize;
}

function centerDiv(canvasid) {
// console.time("centerDiv Time");
    
    var theCanvas = jQueryById(canvasid);
        
    if ( theCanvas.length != 0 ) {
	   	var arrayPageSize = getPageSize();
		var arrayPageScroll = getPageScroll();
		
		// make sure that the height and width is set or use default values
    	var canvasHeight = theCanvas.height();
    	var canvasWidth = theCanvas.width();
		
		// center canvas and make sure that the top and left values are not
		// negative
		var canvasTop = arrayPageScroll[1] + ((arrayPageSize[3] - 35 - parseInt(canvasHeight)) / 2);
		var canvasLeft = ((arrayPageSize[2] - 20 - parseInt(canvasWidth)) / 2);
		canvasLeft = canvasLeft + (arrayPageScroll[0]);
		// fucking IE
		if (!isNaN(canvasTop))
			theCanvas.css("top",(canvasTop < 0) ? "0px" : canvasTop + "px");
		if (!isNaN(canvasLeft))			
			theCanvas.css("left",(canvasLeft < 0) ? "0px" : canvasLeft + "px");
	}
// console.timeEnd("centerDiv Time");
}

function disableZone(show, objectId ) {
	var dZone = 	jQueryById('disabledZone');
	var dialog = 	jQueryById(objectId);
	if (show) {
		// if there is a popup in a popup, do not move the disabled zone because
		// the z-index of the parent is relevant
		var parentPopup = getFirstParentWithZIndex(dialog);
		var parentZindex = -1;		
		if( parentPopup ){
			var parentZindex = parentPopup.css('z-index');
			return;
		}

		var arrayPageSize = getPageSize();	  
		if( dZone.length == 0 ){
			if( parentZindex == -1 )
				parentZindex = 1001;
			dZone = jQuery("<div>&nbsp;<div>");
			dZone.attr( "id", "disabledZone" );
			dZone.data( "handler", new Array( new Array( objectId, parentZindex-1 ) ) );
			dZone.css('position', "absolute");
			dZone.css('zIndex', parentZindex-1 );
			dZone.css('left', "0px");
			dZone.css('top',"0px");
			dZone.css('width',"100%");
			dZone.css('height', (arrayPageSize[1] + 'px') );
			dZone.css('backgroundImage', "url(./images/wgt/1.gif)");
			dZone.css('backgroundRepeat', "no-repeat");
			dialog.css('zIndex', 1001 );
			jQuery( "body" ).append( dZone );
		}
		else{
			var handlers = dZone.data("handler");

			var newZindex = 1001 + (handlers.length*2);

			if( parentZindex == -1 )
				parentZindex = newZindex;

			var pos = findInHandlerArray(handlers,objectId);
			if( pos == -1 ){
				handlers[ handlers.length ] = new Array( objectId, parentZindex-1 );
				dZone.data("handler", handlers );
				dZone.css('zIndex', parentZindex-1 );
				dialog.css('zIndex', newZindex );
			}
			else{
				dZone.css('zIndex', handlers[pos][1] );
			}
			if( dZone.is(':hidden') ){
				window.setTimeout("checkDisableZone()", 2000 );
			}
			dZone.show();
	  }
	} else {
   	  	if( dZone.length > 0 ){
   	  		var handlers = dZone.data("handler");
   	  		var index = findInHandlerArray(handlers,objectId);
	      	if( index != -1 ){
	      		handlers.splice( index, 1 );
	        	dZone.data("handler", handlers );
	        	if( index == handlers.length && index != 0 )
	  		  		dZone.css('zIndex', handlers[handlers.length-1][1] );
	      	}
	      	if( handlers.length == 0 )
	      		dZone.hide();   	  		
   	  	}
    }
}
function getZIndex( objectId ){
	var zIndex = jQueryById( objectId ).css('z-index');
	return zIndex;
}
function getFirstParentWithZIndex( element ){
	var parentPopup = element.parent('[style*="z-index"]');
	if(parentPopup.get(0)){
		return parentPopup.first();
	}
	return null;
}

function findInHandlerArray( array, toBeFound ){
    for( var i = 0; i<array.length; i++ ){
        if( array[i][0] == toBeFound )
            return i;
    }
    return -1;
}
/**
 * function to observe disabled zone and hide it if some js code hides the
 * assigned object
 */
function checkDisableZone(){
	var dZone = jQueryById('disabledZone');
	if( dZone.length = 1 && !dZone.is(':hidden') ){
		var handlers = dZone.data("handler")
	    for( var i = 0; i<handlers.length; i++ ){
	    	var handler = jQueryById( handlers[i][0] );
	    	if( handler.length == 0 || handler.is(':hidden') ){
	    		handlers.splice( i, 1 );
	    		dZone.data("handler", handlers );
	    		if( handlers.length == 0 ){
	    			dZone.hide();
	    			return;
	    		}
	    	}
	    }
		window.setTimeout("checkDisableZone()", 2000 );
	}
}

/*
 * innerHTML with workaround for M$ great Table Object Model don't we all love
 * IE? see
 * http://www.tutorials.de/forum/javascript/137407-unbekannter-laufzeitfehler-bei-innerhtml.html
 * http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/innerhtml.asp
 * don't call this while loading the document *
 */

function innerHTML2DOM( node,  newHtml) {
	removeIdFromCache(node); // remove old ids...

	if (document.all && node.tagName == "TR") {
		var oldId = node.id;
		var cZone = byIdReq("constructionZone");
		cZone.innerHTML =
		"<table><tr>" + newHtml +
		"</tr></table>";
		var tr = cZone.getElementsByTagName("TR")[0];
		var parent = node.parentNode;
		
		parent.replaceChild( tr, node );
		tr.id = oldId;
		// fix idcache
		subNodeCache(tr);
		if (tr.id) wgt_idcache[tr.id] = tr;
	
	} else {
		node.innerHTML = newHtml;
		// fix idcache
		subNodeCache(node);
		// accidently removed..
		if (node.id) wgt_idcache[node.id] = node;
	}
}

/*
 * reconstruct a node with as outerhtml
 */
function outerHTML2DOM( node, newHtml) {
	removeIdFromCache(node); // remove old ids...
	// does not work as well http://webfx.eae.net/dhtml/ieemu/htmlmodel.html
	// was will ich denn mit einem DocumentFragement?
	var cz = document.createElement("DIV");
	cz.innerHTML = newHtml;
	var newNode = cz.firstChild; // can only be one child
	node.parentNode.replaceChild( newNode, node);
	node = newNode;
	// fix idcache
	subNodeCache(node);
	// readd the node itself
	if (node.id) wgt_idcache[node.id] = node;
}

// no more background image flicker in ie 6
// http://evil.che.lu/2006/9/25/no-more-ie6-background-flicker
try {
  document.execCommand('BackgroundImageCache', false, true);
} catch(e) {}
/*
 * load this html as sibling to an existing node
 */
function loadSibling(node, newHtml) {

// console.time("loadSibling Time");
	var cZone = byIdReq("constructionZone");
	var cRoot;
	if (node.tagName == "TR") { // M$ fix see inner2DOM()
		cZone.innerHTML =
		"<table>" + newHtml +
		"</table>";
		if (!cZone.getElementsByTagName("TR") || cZone.getElementsByTagName("TR").length==0) {
			return;
		}
		cRoot = cZone.getElementsByTagName("TR")[0].parentNode;
	
	} else {
		// root is the zone itself
		cRoot = cZone;
		
	}
	subNodeCache(cRoot);
	// move node to new position
	var dady = node.parentNode;
	var youngerBrother = node.nextSibling;
	while (cRoot.firstChild) {
		var childNode = cRoot.firstChild;
		cRoot.removeChild(childNode);
		
		// insert the new clone into the real DOM-tree
		if (youngerBrother==null) {
			dady.appendChild( childNode );
		} else {
			dady.insertBefore(childNode, youngerBrother);
		}
	}
	
	cZone.innerHTML = "";
// console.timeEnd("loadSibling Time");
}

// creates a textareapopup and syncronize it with target inputfield
function txtareapopup(txtid) {
	draginit();
	disableZone( true, txtid );
	if (document.getElementById('txtareapopup')) {
		document.getElementById('txtareapopup').style['visibility']='';
	}
	else {
		var div1=document.createElement('DIV');
		div1.setAttribute('id','txtareapopup');
		div1.style['position'] = "absolute";
		div1.style['zIndex'] = "5010";
		div1.style['left'] = "0px";
		div1.style['top'] = "0px";
		div1.style['width'] = "280px";
		div1.style['height'] = "115px";
		div1.className='wgtTxtPopup';
		div1.onmousedown=function() {dragstart(this);};
		var div2=document.createElement('DIV');
		div2.setAttribute('id','head');
		div2.style['width'] = "280px";
		div1.appendChild(div2);
		var img1=document.createElement('IMG');
		img1.setAttribute('align','right');
		img1.setAttribute('src','./images/wgt/icons/close.gif');
		img1.onclick=function(){document.getElementById('txtareapopup').style['visibility']='hidden';disableZone(false, txtid );document.getElementById(document.getElementById('txtpopuptargetid').value).value=document.getElementById('txtareapopupcontent').value;};
		div2.appendChild(img1);
		var input1=document.createElement('INPUT');
		input1.setAttribute('type','hidden');
		input1.setAttribute('id','txtpopuptargetid');
		div1.appendChild(input1);
		var textarea1=document.createElement('TEXTAREA');
		textarea1.setAttribute('id','txtareapopupcontent');
		textarea1.className='wgtInputTextarea';
		textarea1.id = 'txtareapopupcontent';
		textarea1.style['width'] = "280px";
		textarea1.style['height'] = "100px";
		textarea1.style['zIndex'] = "5011";
		textarea1.onmousedown=stopEvent;
		div1.appendChild(textarea1);
		document.body.appendChild(div1);
	}
	if(byId(txtid).readOnly) {
		document.getElementById('txtareapopupcontent').readOnly = true;
		classAdd(document.getElementById('txtareapopupcontent'),'wgtReadonly');
	}	else {
		document.getElementById('txtareapopupcontent').readOnly = false;
		classRemove(document.getElementById('txtareapopupcontent'),'wgtReadonly');
	}
document.getElementById('txtpopuptargetid').value=txtid;
document.getElementById('txtareapopupcontent').value=document.getElementById(txtid).value;
centerDiv('txtareapopup');
}

// create status for textarea
function  setTextAreaStatus(taId, taStsId, max, tamsg) {
	var sts;
	var currentlength = document.getElementById(taId).value.length;
	sts =  max - currentlength;
	document.getElementById(taStsId).innerHTML=sts;
	if(currentlength>max){
		alert(tamsg + max);
		document.getElementById(taId).value = document.getElementById(taId).value.substring(0,max);
		sts =  0;
		document.getElementById(taStsId).innerHTML=sts;		
	}
}

// stop the event bubble
// http://ajaxcookbook.org/canceling-and-stopping-browser-events/
function stopEvent(e) {
    if (!e) e = window.event;
    if (e.stopPropagation) {
        e.stopPropagation();
    } else {
        e.cancelBubble = true;
    }
}

function cancelEvent(e) {
    if (!e) e = window.event;
    if (e.preventDefault) {
        e.preventDefault();
    } else {
        e.returnValue = false;
    }
}

/*
 * set a class in the attributes or remove it. onOff parameter is true/false to
 * add/remove the className in the class attribute
 */
function setClass(node, clName, onOff) {
	if (onOff) {
		classAdd(node, clName);
	} else {
		classRemove(node, clName );
	}
}

// ##################### File Upload #####################
var currentUploads = {};
var currentUpload = null;

function updateUploadProgress( uploadInfo, cssId )
{
	var fileIndex = uploadInfo[0];
	var totalSize = uploadInfo[1];
	var bytesRead = uploadInfo[2];
	var elapsedTime = uploadInfo[3];
	var status = uploadInfo[4];
	
    if ("progress" == status || "start" == status ) {
        var fileIndex = fileIndex;
        var progressPercent = Math.ceil((bytesRead / totalSize) * 100);
        document.getElementById( cssId 	+ '_progressBarText').innerHTML = '&nbsp;upload in progress: ' + progressPercent + '%';
        document.getElementById( cssId 	+ '_progressBarBoxContent').style.width = parseInt(progressPercent * 1.58) + 'px';
	    window.setTimeout("fireUploadEvent('"+cssId+"');", 1500);
    }
    else if( "nofile" == status ) {
	    document.getElementById( cssId 	+ '_progressBar').style.display = 'none';
        document.getElementById( cssId 	+ '_progressBarBoxContent').style.width = '0px';
	    document.getElementById( cssId 	+ '_progressBarText').innerHTML = '';
	    document.getElementById( cssId 	+ '_fileInput' ).value = '';
        document.getElementById( cssId  + '_uploadField').style.display = 'block';
	    currentUploads[cssId] = null;
    }
    else {
        document.getElementById( cssId  + '_progressBar').style.display = 'none';
        document.getElementById( cssId  + '_progressBarBoxContent').style.width = '0px';
        document.getElementById( cssId  + '_progressBarText').innerHTML = '';
        document.getElementById( cssId  + '_fileInput' ).value = '';
        currentUploads[cssId] = null;
    }
    return true;
}
function fireUploadEvent( cssId ){
    var fileUpload = byId( cssId );
    if( fileUpload ){
        fireWGTEvent(cssId,'99');
    }
}
function startUploadProgress( cssId )
{
	if( currentUploads[cssId] ) {
		window.setTimeout("alert('Upload in progess!')",100);
		return false;
	}
	else {
		currentUploads[cssId] = cssId;
		document.getElementById( cssId 	+ '_uploadField').style.display = 'none';
		document.getElementById( cssId 	+ '_progressBar').style.display = 'block';
		document.getElementById( cssId 	+ '_progressBarText').innerHTML = '&nbsp;upload in progress: 0%';
	    window.setTimeout("fireUploadEvent('"+cssId+"');", 1500);
	    return true;
    }
}

// ##################### End File Upload #####################

// dwr.engine.setRpcType( dwr.engine.IFrame );
dwr.engine.setErrorHandler(errorhandler);
// if the response is a text, the session is expired
dwr.engine.setTextHtmlHandler( timeouthandler );

function errorhandler(param0,param1) {
  showError('An error occurred!',param0,param1);
  disableLoadImage();
  bussy = false;
  processeQueue();
}
function timeouthandler() {
  if (typeof window.privateTimeoutHandler == 'function') {
		// function exists, so we can now call it
	  privateTimeoutHandler();
  }
  else {
	  window.alert("Your session has expired, please login again.");
	  document.location.href = document.location.href;
  }
}

function showError(message,errormessage,exception) {
	iconsrc = "./images/wgt/icons/msg_icon_error.gif"; 
	disableZone(true,'errorBox');
	if (!document.getElementById('errorBox')) {
		var messageBox=document.createElement('DIV');
		messageBox.style['zIndex'] = "5011";
		messageBox.style['position'] = "absolute";
		messageBox.style['left'] = "0px";
		messageBox.style['top'] = "0px";
		messageBox.style['height'] = "100px";
		messageBox.style['width'] = "250px";		
		messageBox.style['padding'] = "0px";
		messageBox.setAttribute('id','errorBox');
		var innermessageBox=document.createElement('DIV');
		innermessageBox.className='messageBox';
		innermessageBox.style['zIndex'] = "5011";
		innermessageBox.style['left'] = "0px";
		innermessageBox.style['top'] = "0px";
		innermessageBox.style['width'] = "250px";
		innermessageBox.style['margin'] = "0px";
		innermessageBox.style['padding'] = "0px";
		innermessageBox.style['paddingBottom'] = "2px";
		innermessageBox.setAttribute('id','innererrorBox');
		messageBox.appendChild(innermessageBox);
		var div1=document.createElement('div');
		div1.className='wgtCanvasWindowTitelBar';
		var table1=document.createElement('table');
		table1.setAttribute('width','100%');
		table1.setAttribute('cellspacing','0');
		table1.setAttribute('cellpadding','0');
		div1.appendChild(table1);
		var tbody1=document.createElement('tbody');
		table1.appendChild(tbody1);
		var tr1=document.createElement('tr');
		tbody1.appendChild(tr1);
		var td1=document.createElement('td');
		td1.className='wgtCanvasWindowTitel';
		tr1.appendChild(td1);
		var span1=document.createElement('span');
		td1.appendChild(span1);
		var txt2=document.createTextNode('Error');
		span1.appendChild(txt2);
		var td2=document.createElement('td');
		td2.setAttribute('align','right');
		tr1.appendChild(td2);
		var input1=document.createElement('input');
		input1.setAttribute('type','image');
		input1.className='wgtCanvasWindowButton';
		input1.setAttribute('src','images/wgt/icons/close.gif');
		input1.onclick=closeError;
		input1.style['margin'] = "1px";
		input1.setAttribute('title','close');
		td2.appendChild(input1);
		innermessageBox.appendChild(div1);
		innermessageBox.style['backgroundImage'] = "url("+iconsrc+")";
		innermessageBox.style['backgroundRepeat'] = "no-repeat";
		innermessageBox.style['backgroundPosition'] = "4px 23px";
		var messageText=document.createElement('DIV');
		messageText.setAttribute('id','message_text');
		messageText.style.marginLeft="35px";
		messageText.style['marginTop'] = "2px";
		innermessageBox.appendChild(messageText);
		messageText.innerHTML = message;
		var errordetailText=document.createElement('DIV');
		errordetailText.setAttribute('id','errorDetail');
		errordetailText.style.marginLeft="35px";
		errordetailText.style['marginTop'] = "5px";
		errordetailText.style['marginBottom'] = "5px";
		errordetailText.style['display'] = "none";
		errordetailText.innerHTML = createErrorOutput(errormessage,exception);
		innermessageBox.appendChild(errordetailText);
		var buttonarea=document.createElement('DIV');
		buttonarea.setAttribute('align','center');
		buttonarea.style.clear="left";
		buttonarea.style['marginTop'] = "5px";
		buttonarea.style['marginBottom'] = "5px";
		innermessageBox.appendChild(buttonarea);
		var closeButton=document.createElement('INPUT');
		closeButton.setAttribute('type','button');
		closeButton.className='messagebutton';
		closeButton.onclick=closeError;
		closeButton.setAttribute('value','OK');
		closeButton.setAttribute('id','message_okaybutton');
		closeButton.style['zIndex'] = "5011";
		buttonarea.appendChild(closeButton);
		var detailButton=document.createElement('INPUT');
		detailButton.setAttribute('type','button');
		detailButton.setAttribute('id','detailButton');
		detailButton.className='messagebutton';
		detailButton.onclick=function(){
		edt = document.getElementById('errorDetail');
		if (edt) {
			visible = isShown(edt);
			show(edt,!visible);	
			if (!visible) {
				document.getElementById('detailButton').value = 'Hide details';
			} else {
				document.getElementById('detailButton').value = 'Show details';
			}
		}
		return false;
		};
		detailButton.setAttribute('value','Show details');
		detailButton.style['zIndex'] = "5011";
		detailButton.style['marginLeft'] = "3px";
		buttonarea.appendChild(detailButton);
		document.body.appendChild(messageBox);
		} 
		else {
			document.getElementById('errorDetail').innerHTML = createErrorOutput(errormessage,exception);
			document.getElementById('errorBox').style['visibility']='';
		}
	centerDiv('errorBox');
}

function closeError() {
	document.getElementById('errorBox').style['visibility']='hidden';
	 disableZone(false, 'errorBox'); 
	 return false;
}

function createErrorOutput(errormessage,exception) {
	returnstring = '';
	if (exception.javaClassName) {
		returnstring +=  '<b>'+exception.javaClassName+':</b> ';	
	}
	returnstring += errormessage + '<br>';
	// returnstring += errormessage + '<br>';
	if (exception.cause) {
		returnstring += '<b>cause:</b> '+ exception.cause.javaClassName + '<br>' + exception.cause.message + '<br>';
	}
	return returnstring;
}

function trim (zeichenkette) {
     return zeichenkette.replace (/^\s+/, '').replace (/\s+$/, '');
}
    
function readCookie(cname) {
       a = trim(document.cookie);
       res = '';
       while(a != '') {
           cookiename = a.substring(0,a.search('='));
           cookiewert = a.substring(a.search('=')+1,a.search(';'));
           if(cookiewert == '' || a.search(';')==-1) {
               cookiewert = a.substring(a.search('=')+1,a.length);
           } else {
               cookiewert = a.substring(a.search('=')+1,a.search(';'));
           }
        if(cname == trim(cookiename)){
           res = cookiewert;
           return (res);
         }
        i = a.search(';')+1;
        if(i == 0){i = a.length}
        a = a.substring(i,a.length);
    }
    return(res)
}

function checkSessionCookie(url) {
    if(!url) {
        url = "./nosessioncookie.jsp";
    }
    document.cookie = 'sessioncookieallowed=true;';
    var check = readCookie('sessioncookieallowed');
    if (check != 'true') {
        window.location.href = url;
    } 
}

/*
 * ################################## External Events
 * #############################################
 */
function fireExternalEvent( params ){
    eventParam( document.body.id, params );
    fireWGTEvent( document.body.id, 'externalEvent' );
}
function callFrame( frameName, params ){
	if( frameName == 'opener' ){
		opener.fireExternalEvent( params );
	}
	else
		eval( "window.parent."+frameName ).fireExternalEvent( params );
}


/*
 * ################################### DOM Operations
 * ########################################
 */

function addAsChild( cssId, value ){
	var cz = byIdReq('constructionZone');
	innerHTML2DOM( cz, value );
	
	var children = cz.childNodes;
    for( var i=0; i < children.length; i++ ){
	    var dlg = children[i];
	    cz.removeChild(dlg);
	    byIdReq( cssId ).appendChild( dlg );
    }
}
function insertAsChild( cssId, value ){
	var cz = byIdReq('constructionZone');
	innerHTML2DOM( cz, value );
	var dlg = cz.firstChild;
	cz.removeChild(dlg);
	if( childNodes.legth>0 )
		byIdReq(cssId).insertBefore( byIdReq(cssId).childNodes[0] );
	else
		byIdReq(cssId).appendChild( dlg );
}
function replaceNode( cssId, value ){
	var cz = byIdReq('constructionZone');
	innerHTML2DOM( cz, value );
	var dlg = cz.firstChild;
	cz.removeChild(dlg);
	byIdReq( cssId ).parentNode.replaceChild( byIdReq(cssId), dlg );
}

function domAppendAfter( cssId, value ){
	var target = byIdReq( cssId );
	var targetObj = jQueryById( cssId );
	var nextNode = targetObj.next().get(0);
	
	var children = html2dom( target.parentNode, value );
	while( children.length > 0 ){
		var child = children[0];
		target.parentNode.insertBefore( child, nextNode );
	}
}
function html2dom( target, newHtml ){
	var node = byIdReq('constructionZone');
	var tagname = target.tagName;
	removeIdFromCache(node); // remove old ids...
	if ( tagname == "TR") {
		node.innerHTML =
		"<table><tr>" + newHtml +
		"</tr></table>";
		var tr = cZone.getElementsByTagName("TR")[0];
		return tr.childNodes;
	}
	else if ( tagname == "TABLE" || tagname == "TBODY") {
		node.innerHTML =
		"<table>" + newHtml +
		"</table>";
		var table = node.getElementsByTagName("TBODY")[0];
		return table.childNodes;
	}
	else{
        node.innerHTML = newHtml;
        return node.childNodes;
	}
}

function treeRootVis( cssId, value ){
	var targetObj = jQueryById( cssId );
	var nextNode = targetObj.nextAll();
	nextNode.each(function (i) {
		var current = jQuery(this);
    	if (current.hasClass("wgtTreeTableRoot") ) {
    		return false;
        } else {
			if( value ) 
				current.show();
			else
				current.hide();
        }
    });
}


/*
 * ################################### canvas
 * ########################################
 */

function toggleTray(elemid,image){
    /**/
   var vista = toggleVisi(elemid);
    
    if (vista=='none') {
        image.src="./images/wgt/opentray.gif"
    }  else {
        image.src="./images/wgt/closetray.gif"
    }
}


/*
 * ################################### context menu
 * ########################################
 */
function contextMenuBind( elementId, properties ){
	// console.debug( "contextMenuBind props = " + properties );
    eval( "var param = " + properties );
	var target = jQueryById( param.targetId );
    if( param.bind ){
    	target.attr("bind","true");
    	target.contextMenu( param.menuId, 
            {   
                onContextMenu: function(e) { 
                    if ( target.attr("bind")=="false" )                    	
                    	return false; 
                    return true; 
                }, 
                bindings: param.bindings 
            });}
    else{
    	target.attr("bind","false");
    }
}

/*
 * ################################### END Base functions
 * ########################################
 */

