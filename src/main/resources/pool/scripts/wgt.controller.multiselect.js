// Depends: wgt.controller.base.js
/**
 * ################### MultiSelect #############################
 *
 * Scripts used for the multible select control
 * 
 * File: wgt.controller.mulitselect.js
 * 
 * Author: Benjamin Klug
 */
registerApplyer( function(aktContext){
        switch(aktContext.type) {
            case "msv":
                loadMultiselect( toLastDot(aktContext.cssId), aktContext.value );
                break;				
            default:
                return false;
        }
        return true;
    });

registerChangeCalculator( 
		function( ce, chgContext ){
			if (ce.type == "msv") { // transfer the value of the multiselect to server
			    if (ce.value != getMultiSelectValue( toLastDot(ce.cssId) )) {
			        ce.value = getMultiSelectValue(toLastDot(ce.cssId)); // selection has changed
			        chgContext[chgContext.length]=ce;
			    }
				return true;
			}		
			return false;
		}
	);




function clickMultiselectEntry(m,sid, mso){
     var selected;
     if(m.className.indexOf("_dis")>0){
        //disabled, return!
        return;
     }
    if(mso<0){
        //multiple selects
        toggleMultiselectSelection(m);
    } else if(mso==1){
        //single mode
        resetMultiselect(sid);
        toggleMultiselectSelection(m);
    } else {
        // n mode
        selected = getSelectedOptions(sid);
        if(selected!==null && selected.length==mso && m.className=="wgtMultiselect_entry"){
            alert("Maximum selection reached!");
            return;
        } else {
            toggleMultiselectSelection(m);
        }
    }
    
    //find all selected and update hidden field
    selected = getSelectedOptions(sid);
    var val = "";  
    for (var i=0;i<selected.length;i++) {
       val = appendValueToCSV(val,selected[i].getAttribute('value'));
    }
    jQueryById(sid + "_hidden").get(0).value=val;
}

function getSelectedOptions(sid){
    return jQuery('#'+sid+'_list >.wgtMultiselect_entry_selected');
}

function appendValueToCSV(cv, newValue){
    if(cv===null || cv===""){
        cv = newValue;
    } else {
        cv = cv + "," + newValue;
    }
    return cv;
}

function loadMultiselect(sid,csv){
    //alert("SID: " + sid);
    //alert("VAL: " + csv);
    //csv is a list of selected values
    //sid is the id of the selectionlist
    resetMultiselect(sid);
    if(csv!==null && csv!==""){
        if(!isNaN(csv)){
            csv = csv.toString();
        }
        var seperatedValues = csv.split(",");
        for (var i = 0; i < seperatedValues.length; i++) {
            var sel = jQueryById(sid + "_" + seperatedValues[i]).get(0);
            if(sel.className.indexOf("_dis")>0){
                sel.className = "wgtMultiselect_entry_selected_dis";
            } else {
                toggleMultiselectSelection(sel);
            }
        }
        jQueryById(sid + "_hidden").get(0).value=csv;
    } else {
        jQueryById(sid + "_hidden").get(0).value="";
    }    
}

function resetMultiselect(sid){
    var selected = getSelectedOptions(sid);
    for (var i=0;i<selected.length;i++) {
        selected[i].className="wgtMultiselect_entry";
    }
    jQueryById(sid + "_hidden").get(0).value="";
}

function selectAllMultiselect(sid){
    var selected = jQuery('#'+sid+'_list > *');
    for (var i=0;i<selected.length;i++) {
        selected[i].className="wgtMultiselect_entry_selected";
    }
    //find all selected and update hidden field
    selected = getSelectedOptions(sid);
    var val = "";  
    for (var i=0;i<selected.length;i++) {
       val = appendValueToCSV(val,selected[i].value);
    }
    jQueryById(sid + "_hidden").get(0).value=val;
}

function disableAllMultiselect(sid, ro){
    var selected = jQuery('#'+sid+'_list > *');
    for (var i=0;i<selected.length;i++) {
        if(ro){
            selected[i].className=selected[i].className + "_dis";
        } else {
            var nam = selected[i].className;
            if(nam.indexOf("_dis")>0){
                nam = nam.substr(0,(nam.length-4));
                selected[i].className=nam;
            }
        }
    }
    //switch souurounding border
    var surrounding = jQuery('#'+sid);
    if(surrounding[0]!=null){
        if(ro){
            surrounding[0].className='wgtMultiselectContainer_dis';
        } else {
            surrounding[0].className='wgtMultiselectContainer';        
        }
    }
}

function getMultiSelectValue(sid){
    var ms = jQueryById(sid + "_hidden");
    //alert(ms);
    if(ms!=null && ms.get(0)!=null){
        return ms.get(0).value;
    } else {
        return null;
    }
}

function toggleMultiselectSelection(m){
    //change layout of selected / unselected
    //m is selected li element
    if(m.className=="wgtMultiselect_entry"){
        m.className = "wgtMultiselect_entry_selected";
    } else if(m.className=="wgtMultiselect_entry_selected"){
        m.className="wgtMultiselect_entry";
    }
}
/*
################################### END MultiSelect ########################################
*/
