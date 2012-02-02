/*
 * ContextMenu - jQuery plugin for right-click context menus
 *
 * Author: Chris Domigan
 * Contributors: Dan G. Switzer, II
 * Parts of this plugin are inspired by Joern Zaefferer's Tooltip plugin
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Version: r2
 * Date: 16 July 2007
 *
 * For documentation visit http://www.trendskitchens.co.nz/jquery/contextmenu/
 * 
 * davysorn@gmail.com 
 * Revision on: 03/03/09
 * Fix: memory leak with event and hash 
 */

(function($) {

    var shadow, content, hash;
    //need z-index and position:absolute in order to show the context menu at the mouse's position
    var newPos = {'left':0,'top':0, 'z-index':500, 'position':'absolute'};
    var newShadowPos = {width:0,height:0,left:0,top:0};
    var shadowStyle = {backgroundColor:'#000',position:'absolute',opacity:0.2,zIndex:499};
    var iconStyle = {verticalAlign:'middle',paddingRight:'2px'};
    
    var defaults = {
        menuStyle: {
          listStyle: 'none',
          padding: '1px',
          margin: '0px',
          backgroundColor: '#fff',
          border: '1px solid #999'
        },
        itemStyle: {
          margin: '0px',
          color: '#000',
          display: 'block',
          cursor: 'default',
          padding: '3px',
          border: '1px solid #fff',
          backgroundColor: 'transparent',
          'white-space': 'nowrap' //force text no break line
        },
        itemHoverStyle: {
          border: '1px solid #0a246a',
          backgroundColor: '#b6bdd2'
        },
        eventPosX: 'pageX',
        eventPosY: 'pageY',
        shadow : true,
        onContextMenu: null,
        onShowMenu: null
    };

    //plugin jquery contextmenu                 //divMenu's id,  bindings action
  $.fn.contextMenu = function(id, options) {
    //shadow is create only one time
    if (!shadow) {
      shadow = $('<div></div>')
                 .css(shadowStyle)
                 .appendTo('body')
                 .hide();
    }
    hash = hash || [];

    hash.push({
    	id : id,
    	menuStyle: $.extend({}, defaults.menuStyle, options.menuStyle || {}),
    	itemStyle: $.extend({}, defaults.itemStyle, options.itemStyle || {}),
    	itemHoverStyle: $.extend({}, defaults.itemHoverStyle, options.itemHoverStyle || {}),
    	bindings: options.bindings || {},
    	shadow: options.shadow || options.shadow === false ? options.shadow : defaults.shadow,
    			onContextMenu: options.onContextMenu || defaults.onContextMenu,
    			onShowMenu: options.onShowMenu || defaults.onShowMenu,
    			eventPosX: options.eventPosX || defaults.eventPosX,
    			eventPosY: options.eventPosY || defaults.eventPosY
    });
   
    var index = hash.length - 1;
    
    //we need to unbind first //to clear the old event.
    $(this).unbind("contextmenu");
    
    //fire when rclick  //bind for rclick event
    $(this).bind('contextmenu', function(e) {
      hideAll();
      var bShowContext = (!!hash[index].onContextMenu) ? hash[index].onContextMenu(e) : true;
      if (bShowContext) display(index, this, e, options);     
      return false;
    });
    return this;
  };

  function display(index, trigger, e, options) {
    var cur = hash[index];
    console.debug('hash element: ' + hash.length + " :" + hash + " id: " + cur.id);
    
    //var msstart = new Date().getTime();
    content = $('#'+cur.id);  //content is divMenu's id
    //console.debug(cur.id + ': elapse time to display: ' + ((new Date().getTime()) - msstart));    
    
    //unbinding contextmenu action first.
    $.each(cur.bindings, function(id, func) {
        $('#'+id,content).unbind('click');
    });  
        
    //binding contextmenu action
    $.each(cur.bindings, function(id, func) {//id is key action, func is action in bindings
        $('#'+id,content).bind('click',function(event){
            hide();
            func(trigger);
        });
    });    
    
    newPos.left = e[cur.eventPosX];
    newPos.top = e[cur.eventPosY];
    
    //execute only for ie version 6
    if(jQuery.browser['msie'] && jQuery.browser.version<parseInt('7.0')){
        cur.menuStyle.width = '120px';
        cur.itemStyle['white-space'] = 'normal';
    }

    //need to apply stylesheet for contextmenu with a new position when rclick
    content.css(newPos);
    
    //apply stylesheet for contextmenu item only one time. //has pb with iev6, get z-index=undefined
    //var mshoverstart = new Date().getTime();
    if(content.find('ul:first').css('z-index') !='500'){
        content
        .find('ul:first').css(cur.menuStyle)
        .find('li').css(cur.itemStyle).hover(
            function() { //over
                $(this).css(cur.itemHoverStyle);
            },
            function(){ //not over
                $(this).css(cur.itemStyle);
            }
        ).find('img').css(iconStyle);
    }
    
    content.show();
    
    newShadowPos.width = content.width();
    newShadowPos.height= content.height();
    newShadowPos.left = e.pageX+2;
    newShadowPos.top = e.pageY+2;
    if (cur.shadow) shadow.css(newShadowPos).show();
  }
  
  function hideAll() { 
    //hide all the context menu  div with class contextMenu and its shadow
    $("div.contextMenu").hide();
    if( shadow )
    	shadow.hide();
  }
  
  function hide(){
    content.hide();
    if( shadow )
    	shadow.hide();
  }
  
  //bind document left n right click only one time.
  $(document).bind('contextmenu', function(e) {hideAll();}).click(function(e){hideAll();});
  
  
  //revision on 27.02.09
  function idExist(obj,id){
	  if(obj){
		  for(var i=0; i<obj.length; i++){
			  var eachObj = obj[i];
			  if(eachObj.id==id){
				  return true;
			  }
		  }
		  return false;
	  }
	  return false;
  }
  
  function getHashIndex(obj,id){
	  if(obj){
		  for(var i=0; i<obj.length; i++){
			  var eachObj = obj[i];
			  if(eachObj.id==id) return i;
		  }
		  return -1;
	  }
	  return -1;	  
  }
  
})(jQuery);


$(function(){
    //hide all context menus when page load
    $("div.contextMenu").hide();
});
