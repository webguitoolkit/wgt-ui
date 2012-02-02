// Depends: wgt.controller.base.js, jquery/suggest.autocomplete.js


registerApplyer( function(aktContext){
    switch(aktContext.type) {
	    case "sug":// load suggested values to the suggest componet
	        suggestShowAjaxResult( toLastDot( aktContext.cssId ), aktContext.value);
	        break;
        default:
            return false;
    }
    return true;
});
