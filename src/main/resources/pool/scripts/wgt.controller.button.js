// Depends: wgt.controller.base.js, wgt.util.json.js, jquery/ui/1.8.1/jquery.ui.core.js, jquery/ui/1.8.1/jquery.ui.widget.js, jquery/ui/1.8.1/jquery.ui.button.js,

/**
 * ################### Button #############################
 * 
 * Scripts used for the button control
 * 
 * File: wgt.controller.button.js
 * 
 * Author: Martin Hermann
 */
registerApplyer(function(aktContext) {
	switch (aktContext.type) {
	case "buttonI": // init Button
		wgtbutton.init( aktContext.cssId, aktContext.value);
		break;
	case "buttonO": // set Options
		wgtbutton.options( toLastDot( aktContext.cssId ), aktContext.value);
		break;
	case "buttonE": // enable/disable Button
	//	wgtbutton.enable(toLastDot( aktContext.cssId ), aktContext.value);
		break;
	default:
		return false;
	}
	return true;
});

var wgtbutton = new function() {
//    var foo = "Private variable foo";
    return {
        lastInit: "no",
        init: function( buttonId, value ) {
    		lasInit = buttonId;
    		if( value != '' ){
    	    	var val = JSON.parse(value);	
    	    	jQueryById( buttonId ).button( val )
    	    		.click( function() { fireWGTEvent( buttonId,'0' ) } );
    		}
    		else{
    	    	jQueryById( buttonId ).button()
    	    		.click( function() { fireWGTEvent( buttonId,'0' ) } );
    		}
        },
        options: function( buttonId, options ) {
	    	var opts = JSON.parse(options);	
    		jQueryById( buttonId ).button( "option", opts );
        },
        enable: function( buttonId, enable ) {
        	if( enable == 'true')
        		jQueryById( buttonId ).button( "enable" );
        	else
        		jQueryById( buttonId ).button( "disable" );
        },
        test: function( string ){
        	alert(string);
        }
    };
}
