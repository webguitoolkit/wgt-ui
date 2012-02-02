// Depends: wgt.controller.base.js, jquery/ui/1.8.1/jquery.ui.core.js, jquery/ui/1.8.1/jquery.ui.widget.js, jquery/jquery.dynatree.0.5.3.js
/**
 * ################### Tree functions #############################
 *
 * Scripts used by the tree
 * 
 * File: wgt.controller.tree.js
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
			case "jtin":
				eval( "var val= " + aktContext.value);
				initJQTree( aktContext.cssId, val );
				break;
			case "jtre": // construct a tree
				loadJQTree( aktContext.cssId, aktContext.value );
				break;
			case "jtll": // tree lazy loading
				loadJQBranch( aktContext.cssId, aktContext.value );
				break;
			case "jtrc": // tree reload children
				reloadJQChildren( aktContext.cssId, aktContext.value );
				break;
			case "jtse":// tree select: select a node inside the tree
				selNodeFromJQTree(toLastDot(aktContext.cssId), aktContext.value);
				break;
			case "jtss":// tree select silent: select a node inside the tree
				selSilentNodeFromJQTree(toLastDot(aktContext.cssId), aktContext.value);
				break;
			case "jtne":// tree node expand. expand node inside the tree
				expandNodeFromJQTree(toLastDot(aktContext.cssId), aktContext.value);			
				break;
			case "jtnse":// tree node expand. expand node inside the tree
				silentExpandNodeFromJQTree(toLastDot(aktContext.cssId), aktContext.value);			
				break;
			case "jtnu":// tree node update. update node
				treeJQNodeUpdate( aktContext.cssId, aktContext.value);			
				break;
			case "jtnc":// tree node update. update node
				treeJQCollapseTree( toLastDot(aktContext.cssId), aktContext.value);			
				break;
            default: 
            	return false;          	
		}
		return true;
	});

registerChangeCalculator( 
		function( ce, chgContext ){
			if (ce.type == "jtse") { // selection of tree
				var treeName = toLastDot(ce.cssId);
				var nodeId = getJQTreeSelectedItemId( treeName );
				if (nodeId != ce.value) { // actually changed?
					ce.value = nodeId;
				    chgContext[chgContext.length]=ce;
				}
				return true;
			}
			else if (ce.type == "jtrv") { // transfer the checked nodes of a tree to server
				var checkedTreeNodes = getJQTreeCheckedNodes(toLastDot(ce.cssId));
				if ( ce.value != checkedTreeNodes ) {
					ce.value = checkedTreeNodes; // selection has changed
				    chgContext[chgContext.length]=ce;
				}
				return true;
			}
			return false;
		}
	);


function initJQTree( id, param ){ 

	jQuery( "#"+id ).dynatree({
		onActivate: function( dtnode ) {
			sendJQTNEvent( dtnode.data.key, "node_onclick", getJQTreeId(dtnode.data.key));
		},
		onSelect: function( flag, dtnode ) {
			sendJQTNEvent( dtnode.data.key, "node_checked", getJQTreeId(dtnode.data.key));
		},
		onLazyRead: function( dtnode ) {			
			sendJQTNEvent( dtnode.data.key, "node_load_lazy", getJQTreeId(dtnode.data.key));
		},
		onExpand: function( flag, dtnode ) {
			var silent = jQuery( "#"+id ).data("silent");
			if( !silent ){
				if( flag )
					sendJQTNEvent( dtnode.data.key, "node_onopen", getJQTreeId(dtnode.data.key));
				else
					sendJQTNEvent( dtnode.data.key, "node_onclose", getJQTreeId(dtnode.data.key));
			}
		},
		onDeactivate: function( dtnode ) {
			
		},
		imagePath: './',
		checkbox: param.checkboxes,
		debugLevel: 2,
		clickFolderMode: param.clickFolderMode,
		idPrefix: ""
	});
}

/**
Contruct a tree and move it to the right position.
*/			
function loadJQTree( id, jason ){ 
	// Now get the root node object
    var rootNode = jQuery( "#"+id ).dynatree("getRoot");
	// remove children
    rootNode.removeChildren(); 
    // Call the DynaTreeNode.append() member function and pass options for the new node
    var opt = eval( jason );
    var childNode = rootNode.append( opt );
}

/**
 * laods a branch from the tree
 * @param id - tree node id
 * @param jason - jason formated string representing the children
 * @return void
 */
function loadJQBranch( id, jason ){ 
	var treeId = getJQTreeId(id);

	// get tree
    var tree = jQuery( "#"+treeId ).dynatree("getTree");
    // get tree node
    var treeNode = tree.getNodeByKey( id );
    // remove old children
    
    // There was a problem when expanding the node and loading the node afterwards in the same request the 
    // expand event triggers a lazy loading and the nodes are displayed twice

    // store child size do decide how to handle the lazy loading
    var childSize = 0;
    if( treeNode.childList ){
    	childSize = treeNode.childList.length;
    }
    
    // if there are more the one child, remove them, due to the reason mentioned before
    if( childSize > 1 ){
    	return;
    }
    //when there are no children add a temporary child 
    if( childSize == 0 ){
    	treeNode.append(eval({title: "", key: "99999999"}));
    }
    
    // add new children
    var opt = eval( jason );
    treeNode.append( opt );
    	
    
    // set the load lazy state for displaying load icon
    // if there is one child node the setLazyNodeStatus function will remove it
    treeNode.setLazyNodeStatus(DTNodeStatus_Ok);
}

/**
 * laods a branch from the tree
 * @param id - tree node id
 * @param jason - jason formated string representing the children
 * @return void
 */
function reloadJQChildren( id, jason ){ 
	var treeId = getJQTreeId(id);
	// get tree
    var tree = jQuery( "#"+treeId ).dynatree("getTree");
    // get tree node
    var treeNode = tree.getNodeByKey( id );

    var toRemove = new Array(); 
    // remove old children
    treeNode.visit(function(dtnode){
    	toRemove[ toRemove.length ] = dtnode;
        return true;
    }, toRemove, false );

    // create temp node that will be removed later
    var tempNode = treeNode.append({
        title: " ",
        tooltip: " "
    });

    for( var i = 0; i < toRemove.length; i++ ){
    	if( toRemove[i].hasChildren() ){
    		toRemove[i].removeChildren();
    	}
    	toRemove[i].remove();
    	
    }
    
    // add new children
    var opt = eval( jason );
    treeNode.append( opt );
    // set the load lazy state for displaying load icon
    if( treeNode.data.isLazy )
    	treeNode.setLazyNodeStatus(DTNodeStatus_Ok);
    else
        tempNode.remove();
}

/*
In the tree treeName, select the node with nodeId.
*/
function selNodeFromJQTree(id, nodeId) {
	// get tree
    var tree = jQuery( "#"+id ).dynatree("getTree");

    // get tree node
    tree.activateKey( nodeId );
}

/*
 * In the tree treeName, select the node with nodeId.
 */
function selSilentNodeFromJQTree(id, nodeId) {
	// get tree
	var tree = jQuery( "#"+id ).dynatree("getTree");
	
    // get tree node
    var treeNode = tree.getNodeByKey( nodeId );

    if( treeNode != null ){
    	treeNode._activate(true,false)
    }

}

/*
* expand treeNode
* expandNode(index, noRedraw, select) 
* Expands node with the given index. 
*/
function expandNodeFromJQTree( id, nodeId) {

	var jqtree = jQuery( "#"+id );
	// get tree
	var tree = jqtree.dynatree("getTree");
    // get tree node
    var treeNode = tree.getNodeByKey( nodeId );

    if( treeNode != null ){
    	expandJQTreeNode( jqtree, tree, treeNode, true );
    }
}

function silentExpandNodeFromJQTree( id, nodeId) {
	
	var jqtree = jQuery( "#"+id );
	// get tree
	var tree = jqtree.dynatree("getTree");
	// get tree node
	var treeNode = tree.getNodeByKey( nodeId );
	
	if( treeNode != null ){
		expandJQTreeNode( jqtree, tree, treeNode, false );
	}
}

function expandJQTreeNode( jqtree, tree, node, fireEvent ){
	if( node.parent  ){
		expandJQTreeNode( jqtree, tree, node.parent, false );
	}
	if( !node.bExpanded  ){
		if( !fireEvent )
			jqtree.data( "silent", true );

		node.expand(true);

		jqtree.data( "silent", false );
	}
}

/*
* update the node item text
*/
function treeJQNodeUpdate( nodeId, val ) {
	
    // add new children
    var opt = eval( val )[0];

	
	// get tree id
	var treeId = getJQTreeId(nodeId);
	// get tree
    var tree = jQuery( "#"+treeId ).dynatree("getTree");
    // get tree node
    var treeNode = tree.getNodeByKey( nodeId );

    if( treeNode ){
        treeNode.data.title = opt.title;
        if(opt.icon){
            treeNode.data.icon = opt.icon;
            treeNode.data.select = opt.select;
            treeNode.select( opt.select );
        }
        // strange, we need to call it again to display the result
        treeNode.render(false,false);
    }
}
/*
 * collapse the tree
 */
function treeJQCollapseTree( id, value ){
	jQuery("#"+id).dynatree("getRoot").visitDeepFirst(function(dtnode){
	    dtnode.expand(false);
	    return true;
	});
}



/**
tree node event functions
*/
function sendJQTNEvent(nodeId, type, treeId) {
	var para = new Array();
	para[0] = nodeId;
	para[1] = type;
	eventParam(treeId,para);
	fireWGTEvent(treeId, type);
}


function getJQTreeCheckedNodes( id ){
	// get tree
    var tree = jQuery( "#"+id ).dynatree("getTree");
    // gets the selected nodes
    var selectedNodes = tree.getSelectedNodes(); 
    var selNodeString = "";
    for( var i = 0; i < selectedNodes.length; i++ ){
    	if( i!=0)
    		selNodeString+= ",";
    	selNodeString += selectedNodes[i].data.key;
    }
	return selNodeString;
}
function getJQTreeSelectedItemId( id ){
	// get tree
    var tree = jQuery( "#"+id ).dynatree("getTree");
    // gets the selected nodes
    var activeNode = tree.getActiveNode(); 
    
    if( activeNode != null ){
    	return activeNode.data.key;
    }
    return "";
}


function getJQTreeId( nodeId ) {
    return nodeId.substring(0, nodeId.indexOf("."));
}
//######################### END Tree #################################
