// Depends: wgt.controller.base.js
/**
 * ################### TabStrip #############################
 *
 * Scripts used by the tab strip
 * 
 * File: wgt.controller.tabstrip.js
 * 
 * Author: Martin Hermann, Thorsten Springhart
 */


/** 
 * register a function for applying the context that comes from the server
 * 
 * context.type - type of the context element
 * context.value - value to be set
 * context.cssId - id of the element
 * context.status - transfer state of the context element
 */ 
registerApplyer( function(aktContext){
		switch(aktContext.type) {
			case "tab": // select a tab
				selectTab(aktContext.cssId, aktContext.value);
				break;
		    case "acc": // select a accordion tab
	            accordion(aktContext.cssId, aktContext.value);
	            break;	
			case "trd": // redraw a tabstrip
				redrawTabStrip(aktContext.cssId, aktContext.value);
				break;
			case "ttv":// set the visibility of an tab element
				show(byIdReq(aktContext.cssId), "true"==aktContext.value);
				break;
			default:
				return false;
		}
		return true;
	});

function selectTab(strip, varray) {
//	console.time("selectTab Time");
	stripid = toLastDot(strip);
	sdot = 	stripid+".";
	// 0 = index of selected tab
	// 1 = status of selected tab
	// 2 = index of old active tab
	// 3 = status of old active tab
	// 4 = type of tabstrip; (n = notops, v = vertical, h = horizontal)
	var values = eval(varray);
	//first handle old selection
	if (values[2]!=-1) {
		if (values[4]=="h") {
			var theTab = jQueryById( sdot+"lab"+values[2] );
			var next = theTab.next().get(0);
			var prev = theTab.prev().get(0);
			var nextStyle = next.className;
			var prevStyle = prev.className;
			
			if( nextStyle == "wgtTabRightOfSelected" || nextStyle == "wgtTabLeftOfSelected" )
				next.className = "wgtTabBetweenUnselected";
			else
				next.className = nextStyle.replace( /Selected/g, "Unselected" );
			
			if( prevStyle == "wgtTabRightOfSelected" || prevStyle == "wgtTabLeftOfSelected" )
				prev.className = "wgtTabBetweenUnselected";
			else
				prev.className = prevStyle.replace( /Selected/g, "Unselected" );
			
			theTab.get(0).className = "wgtTabUnselected";
		}
		//hide inactive tabpane
		if (byId(sdot + "pane"+values[2])) {
			show(byId(sdot + "pane"+values[2]), false);
		}
	}
	if (values[4]=="h") {
		var theTab = jQueryById( sdot+"lab"+values[0] );
		var next = theTab.next().get(0);
		var prev = theTab.prev().get(0);
		var nextStyle = next.className;
		var prevStyle = prev.className;
		
		if( nextStyle == "wgtTabBetweenUnselected" )
			next.className = "wgtTabRightOfSelected";
		else
			next.className = nextStyle.replace( /Unselected/g, "Selected" );

		if( prevStyle == "wgtTabBetweenUnselected" )
			prev.className = "wgtTabLeftOfSelected";
		else
			prev.className = prevStyle.replace( /Unselected/g, "Selected" );
		
		theTab.get(0).className = "wgtTabSelected";
		
		redrawTabStrip(stripid, values[0]);
	}
	//show active tab
	show(byId(sdot + "pane"+values[0]), true);
//	console.timeEnd("selectTab Time");
}

var tri; // holds the pictures of the triangles
function redrawTabStrip(strip, tabno) {
	// lets see if the triangle array is constructed already
	if (tri==null) {
		tri = new Array();
		// bit array:
		// (leftmargin * 1, selected * 2, leftselected * 4)
		tri[1] = "wgtTabFirstUnselected";
		tri[2] = "wgtTabLeftOfSelected";
		tri[4] = "wgtTabRightOfSelected";
		tri[0] = "wgtTabBetweenUnselected";
		tri[3] = "wgtTabFirstSelected";
	}
	var sdot = strip + ".";
	// go through tab and set the delimiter, strip and the content panes
	var isFirst = true;
	var lastIsSelected = false;
	var i=0;
	var bSel; // this item selected?
	while (true) { // will break if expected element not found
		// show content
		var contentPane = byId(sdot + "pane"+i);
		if (contentPane==null) break; // end of tabstrib reached
		bSel = (i == tabno);
		show(contentPane, bSel);
		// tabstrip settings
		var theTri = byId(sdot+"tri"+i);
		var spaceBefore = jQueryById(sdot+"tri"+i);
//		var isVisible = spaceBefore.is(':visible');
		var isVisible = spaceBefore.css("display")!="none";
		if (theTri!=null) { // tabstrip present
			// calc boolean
			var first = (i==0);
			var left = (i-1 == tabno);
			triClass = tri[(isFirst ? 1:0) + (bSel? 2: 0) + (lastIsSelected? 4 : 0)];
			theTri.className = triClass;
			// selection of the label
			if( "wgtTabDisabled" != byIdReq(sdot + "lab"+i).className )	
				byIdReq(sdot + "lab"+i).className = bSel ? "wgtTabSelected" : "wgtTabUnselected";
			isFirst = isFirst && !isVisible;
			if( isVisible )
				lastIsSelected = bSel;
		}
		i++;
	}
	// now what about the last thingi in the strip
	var last = byId(sdot+"last");
	if (last!=null) {
		last.className = (lastIsSelected ? "wgtTabLastSelected" : "wgtTabLastUnselected");
	}
}

/* returns which tabumber is selected
*/
function whichTab(strip) {
	// find the first visible pane
	var i=0;
	do { // break if no further pane found
		var thePane = byId(strip+".pane"+ i);
		if (thePane==null) {
			// error we should have found a visible element before
			alert("Error. no visible element in Tab found "+strip); 
			break;
		}
		if (isShown(thePane)) {
			break;
		}
		i++;
	} while (true);
	return i;
}


function accordion(menueId, startShow){
	var accconfig = startShow.split("_");
	var fold = accconfig[0];
    var acc = jQueryById( menueId + '.tab' + fold );

    var onlyoneopen = accconfig[1];
    if(onlyoneopen=='true'){
    	jQuery('div#'+menueId+'> div').hide();
        jQuery(acc).slideToggle('fast').siblings('div:visible').slideUp('fast');
    } else {
        jQuery(acc).slideToggle('fast');
    }
}

/*
################################### End TabStrip ########################################
*/
