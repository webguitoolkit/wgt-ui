/*
Copyright 2008 Endress+Hauser Infoserve GmbH&Co KG 
Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied. See the License for the specific language governing permissions 
and limitations under the License.
*/
package org.webguitoolkit.ui.ajax;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 * The context is a representation of the application session state. 
 * This class represents the server context, there is an other context on the client side.
 * 
 * All changes that are done to the controls have to be populated to the context.
 * Only the context changes and commands are submitted to the client. Therefore the order
 * of the context changes are important. (e.g. label.setText("first"); label.setText("second"); at the end the last context change wins ) 
 * 
 * <b>Type of context Elements</b>
 * 
 * STATUS_NOT_EDITABLE
 * not editable value, represents a value in the client position in the context
 * 
 * STATUS_COMMAND
 * means that this is a command which must be sent to the client
 * 
 * STATUS_EDITABLE
 * the client in the order it was added to the context
 * 
 * STATUS_SERVER_ONLY
 * This element is not meant to be send to the client
 * </pre>
 */
public interface IContext extends Serializable {

	public static final String TYPE_CLONE_ADD = "ca";
	public static final String TYPE_CLONE_INSERT = "ci";
	public static final String DOT_VIS = ".vis";
	//
	// Stati --------------------------------------------------
	public static final String STATUS_NOT_EDITABLE = "n"; // not editable value, represents a value in the client position in the
	// context
	// should not matter, dubelleten should not be send. BUT some contextelement which should be 'c' and actually labeld 'n'!
	public static final String STATUS_COMMAND = "c"; // means that this is a command which must be sent to
	// the client in the order it was added to the context
	public static final String STATUS_EDITABLE = "e";
	// This element is not meant to be send to the client
	public static final String STATUS_SERVER_ONLY = "s";
	// this state is almost the same as 'not_editable', but is 'not_editable' there is quite a mix
	// of commands as well, such that I need a new ststus for a clean concept of
	// a state which describes can be replaced by other contexts with the same status,
	// this is independent of the type of the context (as opposed to 'not_editable').
	public static final String STATUS_IDEM = "i";
	public static final String TYPE_REMOVE_FROM_CONTEXT = "rce"; // remove all context elements for the given id
	public static final String TYPE_VAL = "val"; // the value should be moved to the value attribute of the node
	public static final String TYPE_TXT = "txt"; // the value should be moved to the first text node
	public static final String TYPE_ALERT = "alr"; // ask JS to pop up an alert box.
	public static final String TYPE_LIGHT = "lgh"; // ask JS to call highlite with
	// public static final String TYPE_OPT = "opt"; // ask JS to add an option to a selectbox
	// public static final String TYPE_CLROPT = "cop"; // ask JS to clear and create the options of a select DEPRECATED
	// public static final String TYPE_SEL_IDX = "sdx"; // ask JS to set the index of the select
	public static final String TYPE_SEL = "sel"; // data goes into attribute selected (boolean) DEPRECATED
	public static final String TYPE_VIS = "vis"; // makes a item visible / invisible,
	// id must have suffix '.vis', value is boolean
	public static final String TYPE_HTML = "htm";
	// replace the outerhtml of a node
	public static final String TYPE_OUT = "out";
	// set a bund of controls to read-only (comma separated ids)
	public static final String TYPE_READONLY = "ro";
	public static final String TYPE_READWRITE = "rw";
	// tel js to set a attribute to the value given.
	public static final String TYPE_ATT = "att";
	public static final String TYPE_TAB = "tab";
	public static final String TYPE_ACCORDION = "acc";
	public static final String TYPE_TABREDRAW = "trd";
	public static final String TYPE_TABVISI = "ttv";
	// change focus to the formelement, cssId= getId()+".focus"
	public static final String TYPE_FOCUS = "foc";
	public static final String TYPE_REMOVE = "rem";
	public static final String DOT_STYLE = ".style";
	public static final String TYPE_RPL = "rpl";
	public static final String TYPE_URL = "url";
	public static final String TYPE_JS = "js";
	public static final String TYPE_LLJ = "llj"; // javascript load lazy
	public static final String TYPE_LLS = "lls"; // style load lazy
	public static final String TYPE_SCROLL_RELOAD = "scr";
	public static final String TYPE_COMBO_LOAD = "cbx";
	/** type for reading and setting the value of a combobox */
	public static final String TYPE_CBI = "cbi";// combo init
	public static final String TYPE_CBV = "cbv";
	// add a string at the end of the existing class definition separated by space
	public static final String TYPE_CLASS_ADD = "cad";
	// remove all occurences of the class in the class-atrribute of the node
	public static final String TYPE_CLASS_REMOVE = "crm";
	// ensuring that a class-attribute has a spcific class in it.
	public static final String TYPE_CLASS_EXIST = "cex";
	public static final String TYPE_TAKE_SNAPSHOT = "tts";
	public static final String TYPE_REST_SNAPSHOT = "trs";
	public static final String TYPE_FILE_UPLOAD_INFO = "fui";
	// create dom nodes as siblings
	public static final String TYPE_SIBLING = "cs";
	// move a div with id, to X/y position, relative to viewport
	public static final String TYPE_MOVETO = "mov";
	// external event on a different frame
	public static final String TYPE_EXT = "ext";
	// add to node as last child
	public static final String TYPE_ADD_2_NODE_LAST = "ala";
	// add to node as first child
	public static final String TYPE_ADD_2_NODE_FIRST = "afi";
	// add to node as first child
	public static final String TYPE_APPEND_AFTER = "apa";
	// replace node
	public static final String TYPE_REPLACE_NODE = "rep";
	public static final String TYPE_TREETABLE_ELEMENT_VIS = "tev";
	public static final String TYPE_CONTEXT_MENU_BIND = "cmb";
	public static final String TYPE_MULTI_SELECT_VALUE = "msv";
	// suggest components result
	public static final String TYPE_SUGGEST_RESULT = "sug";

	/**
	 * The component shall use this method to manipiulate the context Changes made here are automatically transported to the
	 * client.
	 * 
	 * @param ce
	 */
	public abstract void add(final ContextElement ce);

	public abstract void add(String cssId, int value, String type, String status);

	public abstract Map getWatchMap();

	public abstract String getCETrace(ContextElement ce);

	/**
	 * This method registers a context element for delivery to the client. It is assumed, that you pass only new information to
	 * this method. which means: do not call the method with parameters which are already present on the client.
	 * 
	 * @param cssId The css-id by which the element on the client side is recognized
	 * @param value payload, the new value to set.
	 * @param type determines which method is used to get the value in the dom-element
	 * @param status
	 * 
	 *            type possible values: val - value will be move into the value attribute of the dom-element identified by cssId
	 *            txt - value will be moved into first text-children
	 * 
	 *            status e - field is editable, client must keep it in its client side context n - field is read-only, client does
	 *            not need to keep in its context c - this is an command to be send to the client, no need to keep in context,
	 *            just execute s - server only, no need to sedn to client.
	 */
	public abstract void add(String cssId, String value, String type, String status);

	/**
	 * call from DWRFramework to ask for the context to send back to the client.
	 * 
	 * @return
	 */
	public abstract ContextElement[] calculateContextChange();

	/**
	 * set a value in the context
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public abstract void setValue(String key, String value);

	public abstract void setValue(String key, int value);

	public abstract String getValue(String key);

	/**
	 * 
	 * @param key
	 * @return Integer.MIN_VALUE if key not found in context.
	 */
	public abstract int getValueAsInt(String key);

	public abstract boolean getValueAsBool(String key, boolean defaults);

	public abstract boolean getValueAsBool(String key);

	/**
	 * put a contextelement into the context, including the lcontext list to be transported to the client. This overrides any
	 * value in the context if present. It adds the conetext element as a new one if not found yet.
	 * 
	 * @param ce
	 */
	public abstract void put(ContextElement ce);

	public abstract void appendValue(String cssId, String value, String type, String status);

	/**
	 * makes sure, that the specified cssId will get the value in the contextElement. if the cssId has the same value already, it
	 * does nothing, otherwise it changes the current value to the new one, or add a new context element, if there was no before.
	 * If there were changes, they will be transported to the client.
	 * 
	 */
	public abstract void modifyValue(ContextElement ce);

	/**
	 * sending arbitrary javascript to the client , use with caution
	 * 
	 * @param id The identification for this Command. Scritly this is not needed, it is not used by the client. However, it is
	 *            mandatory to send it, as it simplifies debugging. Use the id of the component you are working on or the
	 *            page.getId() and suffix it with a unique string so it doesn't conölide with other ids.
	 * @param js the javascript command, client will execute 'eval(js)'
	 */
	public abstract void sendJavaScript(String id, String js);

	/**
	 * sending arbitrary javascript to the client , use with caution
	 * 
	 * @param id The identification for this Command. Scritly this is not needed, it is not used by the client. However, it is
	 *            mandatory to send it, as it simplifies debugging. Use the id of the component you are working on or the
	 *            page.getId() and suffix it with a unique string so it doesn't conölide with other ids.
	 * @param js the javascript command, client will execute 'eval(js)'
	 */
	public abstract void sendJavaScriptState(String id, String js);

	/**
	 * 
	 * Node ids must be comma seperated.
	 * 
	 * @param inputs
	 * @param readable
	 */
	public abstract void readOnlyState(String inputs, boolean readable);

	/**
	 * replace the html inside a DOM Node with new html on client side.
	 * 
	 * @param nodeId
	 * @param html
	 */
	public abstract void innerHtml(String nodeId, String html);

	/**
	 * replace the html inside a DOM Node with new html on client side.
	 * 
	 * @param nodeId
	 * @param html
	 */
	public abstract void outerHtml(String nodeId, String html);

	/**
	 * appends the html to a DOM Node.
	 * 
	 * @param nodeId
	 * @param html
	 */
	public abstract void appendHtml(String nodeId, String html);

	/**
	 * set the display style of a list of nodes to be visible / not visible.
	 * 
	 * @param nodeId
	 * @param visible
	 */
	public abstract void makeVisible(String nodeId, boolean visible);

	/**
	 * Show / Hides a popup dialog.
	 * 
	 * For this to use you must have a DIV tag in the JSP, Inside the DIV tag is the dialog itself. As of now the div also needs
	 * the following style:
	 * 
	 * @param nodeId
	 * @param visible
	 */
	public abstract void popup(String nodeId, boolean visible);

	/**
	 * remove a DOM node on hte client
	 * 
	 * @param nodeId
	 */

	public abstract void removeNode(String nodeId);

	/**
	 * This replicates a node on the client side and insertsit right where the node is. So it just adds several copies of itself
	 * at the present location. Note: All id-attributes inside the clone and the original will be changed the clone number (row
	 * number) will be appended to their id. e.g.
	 * <tr
	 * id="toBeCloned" ... will get
	 * <tr
	 * id="toBeCloned0" ... Note: the id 'cssId' will not be valied after this operation .
	 * 
	 * @param page TODO
	 * @param cssId the location and subtree to be copied
	 * @param times how many times we copy.
	 */
	public abstract void replicateNode(String cssId, int times);

	/**
	 * set the attribute with name attributeName in the DOM tree. if attributeValue id null it removes the attribute.
	 * 
	 * @param nodeId
	 * @param attributeName
	 * @param attributeValue
	 */
	public abstract void setAttribute(String nodeId, String attributeName, String attributeValue);

	/**
	 * sets a element to visible or hidden, usefull elements that are no controls
	 * 
	 * @param nodeId
	 * @param attributeName
	 * @param attributeValue
	 */
	public abstract void setVisible(String nodeId, boolean visible);

	/**
	 * replace the first text child of the DOM Node identified through nodeId with the text.
	 * 
	 * @param nodeId
	 * @param text
	 */
	public abstract void textChild(String nodeId, String text);

	/**
	 * this copies an node to another location. It does so by deep cloning the node identified by fromID. Then is inserts this
	 * clone before the node identified by beforeID. To identify the node inside the new clonse, all id are changed. icluding the
	 * top level id, which is fromID. All ids get the String postfix added to theier id. The original node is not changed at all.
	 * User must add a suitable postfix such that in the resulting DOM there are no two identical IDs.
	 * 
	 * @param fromID which node to clone, must not be null.
	 * @param postfix String added to the ids of the clone. Must not contain character '.'
	 * @param beforeID location where the clonse is inserted, must not be null.
	 */
	public abstract void cloneAndInsert(String fromID, String beforeID, String postfix);

	public abstract void cloneAndAdd(String fromID, String fatherID, String postfix);

	/**
	 * add a new class to the class-list of the node
	 * 
	 * @param nodeId
	 * @param newclass
	 */
	public abstract void addClass(String nodeId, String newclass);

	/**
	 * remove class-def from the class list.
	 * 
	 * @param nodeId
	 * @param newclass
	 */
	public abstract void removeClass(String nodeId, String theClass);

	public abstract void setClass(String nodeId, String className, boolean shouldExist);

	/**
	 * copy a dom tree into a JS variable, we will save the dom node (and subnodes) in an associative array to be later restored.
	 * 
	 * @param nodeId
	 * @param version
	 */
	public abstract void takeSnapshot(String nodeId, String version);

	public abstract void restoreSnapshot(String nodeId, String version, boolean delete);

	/**
	 * get value from context and don't send this item to the client
	 * 
	 * @param id
	 * @return
	 */
	public abstract String processValue(String id);

	public abstract boolean processBool(String id);

	public abstract boolean processBool(String id, boolean defValue);

	public abstract int processInt(String id);

	public abstract ContextElement processContextElement(String id);

	/**
	 * move all entries with cssId.equals(id) at the end of the list
	 * 
	 * @param id
	 */
	public abstract void moveDown(String id);

	/**
	 * just debug info the context size
	 */
	public abstract int getSize();

	public abstract String toString();

	/**
	 * this constructs a a node from the given html using innerHTML, then it appends that node to the childs of the page.
	 * Specifically usefull to create dailog (popups) dynamically.
	 * 
	 * @param htmlNode must represent ONE node in html
	 */
	public abstract void appendToBody(String htmlNode);

	/**
	 * this restores and retransmitte a state to the client. We want to override the smartness of the state caching because for
	 * example in the case of redraw to states are saved in the context , yet we want to transfer them again, since the html is
	 * transfered again. In the case the there is a context-entry in the c2c this acts as moveDown otherwise it acts as put.
	 * 
	 * You will be sure that there is a c2c entry for this id after the methodexecution iff there is an entry in the main context.
	 * 
	 * @param id
	 */
	public abstract void restoreState(String id);

	public abstract void processPrefix(String pref);

	public abstract void moveTo(String id, int x, int y);

	/**
	 * open a separate Browser with the following url, using 'window.open'
	 * 
	 * @param url
	 */
	public abstract void openURL(String url);

	/**
	 * open a separate Browser with the following url, using 'window.open'
	 * 
	 * @param url
	 * @param windowname
	 */
	public abstract void openURL(String url, String windowname);

	/**
	 * open a separate Browser with the following url, using 'window.open'
	 * 
	 * @param url
	 * @param windowname
	 * @param parameter
	 */
	public abstract void openURL(String url, String windowname, String parameter);

	/**
	 * create a form and perform a post to the given uri with parameters. if target is null, the target of the post will be
	 * _blank, so its opening in a new window.
	 * 
	 * @param uri Action of the form
	 * @param par Parameters to send in the page (will make hidden fields out of it)
	 * @param target the target of the form, pass in null to open a new window / tab.
	 */
	public abstract void doPost(String uri, Map<String, String> par, String target);

	/**
	 * create a form and perform a get to the given uri with parameters. if target is null, the target of the post will be _blank,
	 * so its opening in a new window.
	 * 
	 * @param uri Action of the form
	 * @param par Parameters to send in the page (will make hidden fields out of it)
	 * @param target the target of the form, pass in null to open a new window / tab.
	 */
	public abstract void doGet(String uri, Map<String, String> par, String target);

	/**
	 * create a form and perform a get to the given uri with parameters. if target is null, the target of the post will be _blank,
	 * so its opening in a new window.
	 * 
	 * @param methode POST or GET
	 * @param uri Action attribute of the form
	 * @param par Parameters to send in the page (will make hidden fields out of it)
	 * @param target the target of the form, pass in null to open a new window / tab.
	 * @param showLoadingBar if the loading bar should be displayed when the url is called
	 */
	public void doFormSubmit(String methode, String uri, Map<String, String> par, String target, boolean showLoadingBar);

	public abstract Map<String, ContextElement> getContext();

	public abstract void appendAfter(String id, String html);

	/**
	 * remove context entry, called from BaseControl when a control is removed
	 * 
	 * @param contextElementId id of the context element
	 */
	public void removeContextElement(String contextElementId);

	/**
	 * @param targetId where to append
	 * @param toAppendId what to append
	 * @param html html to append
	 */
	public void appendHtml(String targetId, String toAppendId, String html);
}
