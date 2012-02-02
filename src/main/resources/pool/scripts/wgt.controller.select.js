// Depends: wgt.controller.base.js, jquery/jquery.bgiframe.min.js, jquery/wgt.dropdown.js

// for next release change dropdown implementation jquery/wgt.dropdown-new.js
/**
 * ################### Select Box #############################
 *
 * Scripts used by the select box
 * 
 * File: wgt.controller.select.js
 * 
 * Author: Martin Hermann
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
            case "cbx":// load a combobox with optionslist
                loadCombo(toLastDot(aktContext.cssId), aktContext.value);
                break;
            case "cbi":// load a combobox with optionslist
                initCombo(aktContext.cssId, aktContext.value);
                break;
            case "cbv":// set the value of the combobox
                setCombo(toLastDot(aktContext.cssId), aktContext.value);
                break;
            default:
                return false;
        }
        return true;
    });

registerChangeCalculator( 
		function( ce, chgContext ){
			if (ce.type == "cbv") { // transfer the value of the combobox to server
				if (ce.value != getCombo(toLastDot(ce.cssId))) {
					ce.value = getCombo(toLastDot(ce.cssId)); // selection has changed
				    chgContext[chgContext.length]=ce;
				}
				return true;
			}
			return false;
		}
	);


var wgtCombos = {};
var isComboInit = false;
function initCombo( cbxId, cbxOptions ){
    cbxId = jQueryEscaped(cbxId);
    eval( "var param = "+cbxOptions );
    $j("#"+ cbxId ).dropdown( param.opt, 
        { 
            minChars: 0, 
            max: param.opt.length, 
            autoFill: true, 
            mustMatch: true, 
            matchContains: false,
            formatItem: function(row, i, max) { return row.option; },
            formatResult: function(row) { return row.option; },
            
            highlight: function(value, term) {
                if( term != '' )
                    return value.replace(new RegExp("(?!<[^<>]*)(" + term + ")(?![^<>]*>)", "gi"), "<strong>$1</strong>");
                else
                    return value;
            }
        }
    );
    if( param.changeAction ){
        $j("#"+ cbxId ).result(
            function(event, data, formatted, skipEvent ) {
                var hiddenField = $j("#"+ jQueryEscaped(this.id) + "_hidden");
                var oldValue = hiddenField.val();
                if( data )
                    hiddenField.val( data.value );
                else
                    hiddenField.val( "" );
                if(!skipEvent){ 
                    // fire event only when really changed
                    if( oldValue != hiddenField.val() )
                        fireWGTEvent( this.id, '0' ); 
                }
            }
        );
    }
    else{
        $j("#"+ cbxId ).result(
            function(event, data, formatted, skipEvent ) {
                if( data )
                    $j("#"+ jQueryEscaped(this.id) + "_hidden").val( data.value );
                else
                    $j("#"+ jQueryEscaped(this.id) + "_hidden").val( "" );
            }
        );
    }
    
//    alert('come here: ' + cbxId + " id: " + $j("#" + cbxId + "\\.button" ).attr('id') );
    $j("#" + cbxId + "\\.button" ).click(
        function( event ) {
    		return selectButtonClicked(this, event);          
        }
    );
    wgtCombos[ cbxId ] = $j("#"+ cbxId );   
    if( !isComboInit )
        initComboEventHandler(); // just once
}
function selectButtonClicked(element, event){
	closeAllCombo();
  	if( jQuery(element)[0].src.indexOf('_disabled')>0 ) return false;
  	jQuery("#"+jQueryEscaped( toLastDot(element.id) ) ).openSelect();
  	if( event )
  		stopEvent( event );
  	return false;
}
function initComboEventHandler(){
    jQuery(document).bind("click", function(e){
    	var el;
        el = e.target ? e.target : e.srcElement;
        while ( el.nodeType && el.nodeType != 1) el = el.parentNode;
        var elcl = el.className;
        if( !elcl || elcl.indexOf("wgtCombo")!=0  ){
            closeAllCombo();
        }
        else if ( elcl.indexOf("wgtComboInput") >= 0  ){
            closeAllCombo( el.id );
        }
    });
    isComboInit = true;
}
function closeAllCombo( comId ){
//  console.debug( "closeAllCombo()");
    for (var comboId in wgtCombos ){
       // console.debug( comboId );
        if( comId != comboId )
            wgtCombos[ comboId ].closeSelect();
    }
}
function loadCombo(cbxName, cbxNewOptions) {
    var opt = eval( cbxNewOptions );
    $j("#"+jQueryEscaped(cbxName) )
        // clear existing data
        .val("")
        .setOptions({data: opt, max: opt.length })
}
/**
setting the value of the comboboc means choosing one of the elements.
*/
function setCombo(cbxName, newval) {
    var combo = $j( "#"+jQueryEscaped(cbxName) );
    combo.selectValue( newval );
}
function getCombo(cbxName) {
    var hidden = $j( "#"+jQueryEscaped(cbxName)+"_hidden" );
    return hidden.val();
}
/**
################### End Select Box #############################
*/