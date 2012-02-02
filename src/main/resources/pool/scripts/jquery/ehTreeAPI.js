var wgtTrees = {};

function initTree( id, param ){ 
	var tree = tree = new dhtmlXTreeObject( id, param.width, param.height, id ); 
	tree.setImagePath( param.imagePath ); 
	if( param.dragDrop )
		tree.enableDragAndDrop(true);
	if( param.checkboxes )
		tree.enableCheckBoxes(1);
	if( param.checkEvent )
		tree.attachEvent("onCheck", onTreeNodeCheck );
	tree.attachEvent("onClick", onTreeNodeSelect );
	tree.attachEvent("onOpenStart", onTreeNodeOpen );
    tree.attachEvent("onDblClick", onTreeNodeOpen );	
	if( tree.setIconSize ){
	   // only available in professional edition
	   tree.setIconSize("16px","19px");//set global icon size
	}
	wgtTrees[id] = tree;
}

/**
Contruct a tree and move it to the right position.
*/			
function loadTree( id, jason ){ 
	var tree = wgtTrees[id];
	tree.deleteChildItems(id);
	tree.loadXMLString( xml );
}

function loadBranch( id, jason ){ 
	var nodeIdStart = xml.indexOf( "id=" );
	var nodeIdEnd = xml.indexOf( "\"", nodeIdStart + 4 );
	var nodeId = xml.substring( nodeIdStart+4, nodeIdEnd );
	var tree = wgtTrees[id];

	var children = tree.getAllSubItems(nodeId);
	if( children ){
        var childArray = children.split(",");
	    for( var i = 0; i < childArray.length; i++ ){
	        for( var j = 0; j < jQuery.ui.ddmanager.droppables.length; j++ ){
	           var droppable = jQuery.ui.ddmanager.droppables[j];
	           if( droppable.element.attr("id") == childArray[i] ){
	               droppable.destroy();
	           }
	        }
	    } 
	}
	tree.deleteChildItems(nodeId);
	tree.loadXMLString( xml );
}

function onTreeNodeSelect( nodeId ){
	var treeId = getTreeId(nodeId);
	var para = new Array();
	para[0] = nodeId;
	para[1] = "node_onclick";
	para[2] = "";
	para[3] = "";
	eventParam( treeId, para );
	fireWGTEvent( treeId, "node_onclick" );
	return true;
}
function onTreeNodeOpen( nodeId ){
	var treeId = getTreeId(nodeId);
	var openState = wgtTrees[treeId].getOpenState( nodeId );
	if( openState == "-1" ){
		var isLoaded = wgtTrees[treeId].getChildItemIdByIndex( nodeId, 0 ).indexOf("_temp");
		if( isLoaded > 0 ){
			var para = new Array();
			para[0] = nodeId;
			para[1] = "node_onclick";
			para[2] = "";
			para[3] = "";
			eventParam( treeId, para );
			fireWGTEvent( treeId, "node_onopen" );
		}
	}
	return true;
}

function onTreeNodeCheck( nodeId ){
	var treeId = getTreeId(nodeId);
	var para = new Array();
	para[0] = nodeId;
	para[1] = "node_checked";
	para[2] = "";
	para[3] = "";
	eventParam( treeId, para );
	fireWGTEvent( treeId, "node_checked" );
	return true;
}

function onTreeNodeMouseOver( nodeId ){
	var treeId = getTreeId(nodeId);
	var eventTypes = wgtTrees[treeId].getUserData(nodeId,"eventTypes");
	if( eventTypes.indexOf("node_onmouseover") > -1 ){
		var para = new Array();
		para[0] = nodeId;
		para[1] = "node_onmouseover";
		para[2] = "";
		para[3] = "";
		eventParam( treeId, para );
		fireWGTEvent( treeId, "node_onmouseover" );
	}
	return true;
}
function onTreeNodeMouseOut( nodeId ){
	var treeId = getTreeId(nodeId);
	var eventTypes = wgtTrees[treeId].getUserData(nodeId,"eventTypes");
	if( eventTypes.indexOf("node_onmouseout") > -1 ){
		var para = new Array();
		para[0] = nodeId;
		para[1] = "node_onmouseout";
		para[2] = "";
		para[3] = "";
		eventParam( treeId, para );
		fireWGTEvent( treeId, "node_onmouseout" );
	}
	return true;
}

/*
In the tree treeName, select the node with nodeId.
*/
function selNodeFromTree(id, nodeId) {
	wgtTrees[id].selectItem( nodeId );
}

/*
* expand treeNode
* expandNode(index, noRedraw, select) 
* Expands node with the given index. 
* If "noRedraw" parameter is true then this call does not redraw the tree. 
* If "select" is true then node's index will be passed to "selectNode()" method.
*/
function expandNodeFromTree( id, nodeId) {
	wgtTrees[id].openItem( nodeId );
}

/*
* update the node item text
*/
function treeNodeUpdate( nodeId, val ) {
	var treeId = getTreeId(nodeId);
	wgtTrees[treeId].setItemText( nodeId, val );
}



/**
tree node event functions
*/
function sendTNEvent(node, type, treeId) {
	var para = new Array();
	para[0] = node.getId();
	para[1] = type;
	para[2] = node.level;
	para[3] = node.getMinorIndex();
	eventParam(treeId,para);
	fireWGTEvent(treeId, type);
}


function getTreeCheckedNodes( id ){
	return wgtTrees[id].getAllChecked();
}

function initTreeDroppable( treeId, nodeId ){
	var objBel = jQuery( '#' + treeId + ' table[objBelong]' );
	for(var i = 0;i<objBel.length;i++){
		if( objBel.get(i).objBelong.id == nodeId ){
		  var droppableObject = jQuery( objBel.get(i).childNodes[0].childNodes[0] );
		    if( !droppableObject.hasClass( "ui-droppable" ) ){
				droppableObject.droppable(
					{
						accept: 'tr',
						hoverClass: 'wgtDroppableHover',
						drop: function(ev, ui) {
							eventParam( treeId, new Array( nodeId, ui.draggable.get(0).id ) );
							fireWGTEvent( treeId, "node_dropped" );
						}
					}); 
			}
			break;
		} 
	}
}
function initTreeDraggable( treeId, nodeId ){
	var objBel = jQuery( '#' + treeId + ' table[objBelong]' );
	for(var i = 0; i<objBel.length; i++ ){
		if( objBel.get(i).objBelong.id == nodeId ){
			jQuery( objBel.get(i).childNodes[0].childNodes[0] ).attr( 'id', nodeId );
			jQuery( objBel.get(i).childNodes[0].childNodes[0] ).draggable( "destroy" );
			jQuery( objBel.get(i).childNodes[0].childNodes[0] ).draggable( {
					helper: function(ev, ui) {
					    var helperObj = jQuery('#dragObject').clone().attr( 'id', 'dragObject_clone' ).show();
						return helperObj;
				    }, 
					cursor: 'move',
					cursorAt: {top:5, left:5},
					appendTo: 'body',
					zIndex: 20,
					start: function(ev, ui) {
						eventParam( treeId, new Array( this.id ) );
				    	fireWGTEvent( treeId, 'node_dragged' );
				    }
				} );
		} 
	}
}

function getTreeId( nodeId ) {
    return nodeId.substring(0, nodeId.indexOf("."));
}

function getTreeSelectedItemId( treeName ){
	var nodeId = wgtTrees[ treeName ].getSelectedItemId();
	return nodeId;
}
