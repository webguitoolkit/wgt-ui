// Depends: wgt.controller.base.js
/**
 * ################### Gloss button functions #############################
 *
 * Scripts with some core functionality of the WGT Gloss button
 * 
 * File: wgt.controller.glossbutton.js
 * 
 * Author: Martin Hermann, Ben Klug
 */

function initGlossButton(gbid){
	var gloss = jQuery('#'+gbid);
	gloss.mouseover( function(){
			jQuery(this).addClass('btn_cm_ov');
	});
	gloss.mouseout( function(){
		jQuery(this).removeClass('btn_cm_ov');
		jQuery(this).removeClass('btn_cm_cl');
	});
	gloss.bind('mousedown', function(){
		jQuery(this).removeClass('btn_cm_ov');
		jQuery(this).addClass('btn_cm_cl');
	});
	gloss.bind('mouseup', function(){
		jQuery(this).addClass('btn_cm_ov');
		jQuery(this).removeClass('btn_cm_cl');
	});
}