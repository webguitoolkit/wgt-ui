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
/*
 *
 */
package org.webguitoolkit.ui.ajax;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.Input;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <b>The context is a representation of the application session state.</b>
 * <p/>
 * This class represents the server context, there is an other context on the client side.
 * <p/>
 * All changes that are done to the controls have to be populated to the context. Only the context changes and commands are
 * submitted to the client. Therefore the order of the context changes are important. (e.g. label.setText("first");
 * label.setText("second"); at the end the last context change wins )
 * <p/>
 *
 * <b>Type of context Elements</b><br>
 *
 * STATUS_NOT_EDITABLE<br>
 * not editable value, represents a value in the client position in the context
 * <p/>
 *
 * STATUS_COMMAND<br>
 * means that this is a command which must be sent to the client
 * <p/>
 *
 * STATUS_EDITABLE<br>
 * the client in the order it was added to the context
 * <p/>
 *
 * STATUS_SERVER_ONLY<br>
 * This element is not meant to be send to the client
 * <p/>
 *
 * @author Martin
 */
public class Context implements IContext {

	private List<ContextElement> c2c = new ArrayList<ContextElement>();
	private Map<String, ContextElement> context = new HashMap<String, ContextElement>();

	public Context() {
		super();
	}

	public static HashSet<String> stateItems = new HashSet<String>(Arrays.asList(new String[] { TYPE_ATT, TYPE_CBV, TYPE_COMBO_LOAD,
			TYPE_HTML, TYPE_LIGHT, TYPE_READONLY, TYPE_READWRITE, TYPE_SEL, TYPE_TAB, TYPE_TXT, TYPE_VAL, TYPE_VIS, TYPE_CLASS_EXIST }));

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#add(com.endress.infoserve.wgt.ajax.ContextElement)
	 */
	public void add(final ContextElement ce) {
		// calculate if this entry is a change at all.
		boolean doublet = false;
		boolean alreadyAdded = false;
		String ceId = ce.getCssId();
		String ceStatus = ce.getStatus();
		if (STATUS_EDITABLE.equals(ceStatus) || STATUS_NOT_EDITABLE.equals(ceStatus) || STATUS_IDEM.equals(ceStatus)) {
			// if the state of the contextElements on the client is already the same
			// we don't need to send the element at all!
			// only some types represent states onthe client!
			if (stateItems.contains(ce.getType()) || STATUS_IDEM.equals(ceStatus)) {
				// lets see if it is in the context already....
				ContextElement otherItem = context.get(ceId);
				if (otherItem != null) {
					if (otherItem.equals(ce)) {
						// equal
						doublet = true;
					}
					else {
						// this is an update.
						// check if it exists in c2c (context to client)
						for (int i = 0; i < c2c.size(); i++) {
							ContextElement ce2client = c2c.get(i);
							if (ce2client.getCssId().equals(ceId)) {
								// double in c2c not needed.
								c2c.set(i, ce);
								alreadyAdded = true;
							}
						}
					}
				}
			}
		}
		if (!doublet) {
			context.put(ceId, ce);
			watchCE(ce);
			if (!alreadyAdded && !STATUS_SERVER_ONLY.equals(ceStatus)) {
				c2c.add(ce);
			}
		}
	}

	public void removeContextElement(String contextElementId) {
		// send remove command to client
		add(contextElementId + ".removeCE", null, IContext.TYPE_REMOVE_FROM_CONTEXT, IContext.STATUS_NOT_EDITABLE);

		// remove on server side
		for (Iterator<Map.Entry<String, ContextElement>> iterator = context.entrySet().iterator(); iterator.hasNext();) {
			String key = iterator.next().getKey();
			if (key.startsWith(contextElementId + ".") || key.equals(contextElementId))
				iterator.remove();
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#add(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	public void add(String cssId, int value, String type, String status) {
		add(new ContextElement(cssId, Integer.toString(value), type, status));
	}

	/**
	 * for debugging there is a logging of the stacktrace, when the context was last changed
	 */
	private void watchCE(ContextElement ce) {
		if (!debugTrace)
			return;
		// don't let the map grow too large
		if (watchMap.size() > 5000)
			watchMap.clear();
		iamhere.fillInStackTrace();
		StringWriter s = new StringWriter();
		iamhere.printStackTrace(new PrintWriter(s));
		watchMap.put(ce, s.toString().intern());
	}

	public static boolean debugTrace = false;

	private Map<ContextElement, String> watchMap = new HashMap<ContextElement, String>();

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#getWatchMap()
	 */
	public Map<ContextElement, String> getWatchMap() {
		return watchMap;
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#getCETrace(com.endress.infoserve.wgt.ajax.ContextElement)
	 */
	public String getCETrace(ContextElement ce) {
		String s = watchMap.get(ce);
		if (s == null)
			return "";
		return s;
	}

	private Throwable iamhere = new Throwable();

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#add(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void add(String cssId, String value, String type, String status) {
		add(new ContextElement(cssId, value, type, status));
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#calculateContextChange()
	 */
	public ContextElement[] calculateContextChange() {
		ContextElement[] cea = c2c.toArray(new ContextElement[c2c.size()]);
		c2c.clear(); // after send lets forget about them
		return cea;
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#setValue(java.lang.String, java.lang.String)
	 */
	public void setValue(String key, String value) {
		ContextElement ce = context.get(key);
		if (ce == null)
			throw new WGTException("could not find context element with key " + key);
		ce.setValue(value);
		add(ce);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#setValue(java.lang.String, int)
	 */
	public void setValue(String key, int value) {
		ContextElement ce = context.get(key);
		if (ce == null)
			throw new WGTException("could not find context element with key " + key);
		ce.setValue(Integer.toString(value));
		add(ce);
	}

	/**
	 * @see com.endress.infoserve.wgt.ajax.IContext#getValue(java.lang.String)
	 */
	public String getValue(String key) {
		ContextElement ce = context.get(key);
		return (ce == null) ? null : ce.getValue();
	}

	/**
	 * @see com.endress.infoserve.wgt.ajax.IContext#getValueAsInt(java.lang.String)
	 */
	public int getValueAsInt(String key) {
		// null maps to -max
		String number = getValue(key);
		if (number == null)
			return Integer.MIN_VALUE;
		// parse error equals to null
		try {
			return Integer.parseInt(number);
		}
		catch (NumberFormatException e) {
			return Integer.MIN_VALUE;
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#getValueAsBool(java.lang.String, boolean)
	 */
	public boolean getValueAsBool(String key, boolean defaults) {
		String val = getValue(key);
		if (StringUtils.isEmpty(val)) {
			return defaults;
		}
		else {
			return "true".equalsIgnoreCase(val);
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#getValueAsBool(java.lang.String)
	 */
	public boolean getValueAsBool(String key) {
		// null maps to false
		String val = getValue(key);
		// parse error results in false
		return "true".equalsIgnoreCase(val);

	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#put(com.endress.infoserve.wgt.ajax.ContextElement)
	 */
	public void put(ContextElement ce) {
		if (StringUtils.isBlank(ce.getCssId())) {
			throw new WGTException("cssId should not be empty.");
		}
		if (!STATUS_SERVER_ONLY.equals(ce.status)) {
			// update in the context to be send to the client
			boolean found = false;
			for (int i = 0; i < c2c.size(); i++) {
				ContextElement theCe = c2c.get(i);
				if (theCe.getCssId() == ce.getCssId()) {
					// replace!
					c2c.set(i, ce);
					found = true;
					break;
				}
			}
			if (!found) {
				c2c.add(ce);
			}
		}
		context.put(ce.getCssId(), ce);
		watchCE(ce);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#appendValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void appendValue(String cssId, String value, String type, String status) {
		ContextElement theCe = context.get(cssId);
		if (theCe != null) {
			value += theCe.getValue();
		}
		put(new ContextElement(cssId, value, type, status));
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#modifyValue(com.endress.infoserve.wgt.ajax.ContextElement)
	 */
	public void modifyValue(ContextElement ce) {
		if (StringUtils.isBlank(ce.getCssId())) {
			throw new WGTException("cssId should not be empty.");
		}
		ContextElement oldCE = context.get(ce.getCssId());
		if (oldCE == null) {
			// our element is new
			add(ce);
		}
		else {
			// lets see if the value differs from old
			if (!StringUtils.equals(oldCE.getValue(), ce.getValue())) {
				// do a change
				put(ce);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#sendJavaScript(java.lang.String, java.lang.String)
	 */
	public void sendJavaScript(String id, String js) {
		add(id, js, TYPE_JS, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#sendJavaScriptState(java.lang.String, java.lang.String)
	 */
	public void sendJavaScriptState(String id, String js) {
		add(id, js, TYPE_JS, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#readOnlyState(java.lang.String, boolean)
	 */
	public void readOnlyState(String inputs, boolean readable) {
		// css ID not used for readonly
		add("", inputs, readable ? TYPE_READWRITE : TYPE_READONLY, STATUS_NOT_EDITABLE);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#innerHtml(java.lang.String, java.lang.String)
	 */
	public void innerHtml(String nodeId, String html) {
		add(nodeId, html, IContext.TYPE_HTML, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#outerHtml(java.lang.String, java.lang.String)
	 */
	public void outerHtml(String nodeId, String html) {
		add(nodeId + ".redraw", html, IContext.TYPE_OUT, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#appendHtml(java.lang.String, java.lang.String)
	 */
	public void appendHtml(String nodeId, String html) {
		add(nodeId + ".append", html, IContext.TYPE_ADD_2_NODE_LAST, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#appendHtml(java.lang.String, java.lang.String)
	 */
	public void appendHtml(String targetId, String toAppendId, String html) {
		add(targetId + "." + toAppendId.replace('.', '_'), html, IContext.TYPE_ADD_2_NODE_LAST, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#makeVisible(java.lang.String, boolean)
	 */
	public void makeVisible(String nodeId, boolean visible) {
		add(nodeId + DOT_VIS, Boolean.toString(visible), TYPE_VIS, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#popup(java.lang.String, boolean)
	 */
	public void popup(String nodeId, boolean visible) {
		add(nodeId + DOT_VIS, Boolean.toString(visible), TYPE_VIS, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#removeNode(java.lang.String)
	 */

	public void removeNode(String nodeId) {
		add(nodeId, "", TYPE_REMOVE, STATUS_NOT_EDITABLE);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#replicateNode(java.lang.String, int)
	 */
	public void replicateNode(String cssId, int times) {
		add(cssId, times, TYPE_RPL, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#setAttribute(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void setAttribute(String nodeId, String attributeName, String attributeValue) {
		add(nodeId + "." + attributeName, attributeValue, TYPE_ATT, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#setVisible(java.lang.String, boolean)
	 */
	public void setVisible(String nodeId, boolean visible) {
		add(nodeId + IContext.DOT_VIS, Boolean.toString(visible), TYPE_VIS, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#textChild(java.lang.String, java.lang.String)
	 */
	public void textChild(String nodeId, String text) {
		add(nodeId, text, TYPE_TXT, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#cloneAndInsert(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void cloneAndInsert(String fromID, String beforeID, String postfix) {
		add(fromID, beforeID + "." + postfix, TYPE_CLONE_INSERT, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#cloneAndAdd(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void cloneAndAdd(String fromID, String fatherID, String postfix) {
		add(fromID, fatherID + "." + postfix, TYPE_CLONE_ADD, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#addClass(java.lang.String, java.lang.String)
	 */
	public void addClass(String nodeId, String newclass) {
		add(nodeId + ".addClass", newclass, TYPE_CLASS_ADD, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#removeClass(java.lang.String, java.lang.String)
	 */
	public void removeClass(String nodeId, String newclass) {
		add(nodeId + ".rmClass", newclass, TYPE_CLASS_REMOVE, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#setClass(java.lang.String, java.lang.String, boolean)
	 */
	public void setClass(String nodeId, String className, boolean shouldExist) {
		add(nodeId + "." + className, Boolean.toString(shouldExist), TYPE_CLASS_EXIST, STATUS_IDEM);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#takeSnapshot(java.lang.String, java.lang.String)
	 */
	public void takeSnapshot(String nodeId, String version) {
		add(nodeId + "." + version, "", TYPE_TAKE_SNAPSHOT, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#restoreSnapshot(java.lang.String, java.lang.String, boolean)
	 */
	public void restoreSnapshot(String nodeId, String version, boolean delete) {
		add(nodeId + "." + version, "" + delete, TYPE_REST_SNAPSHOT, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#processValue(java.lang.String)
	 */
	public String processValue(String id) {
		ContextElement ce = processContextElement(id);
		if (ce == null)
			return "";
		return ce.getValue();
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#processBool(java.lang.String)
	 */
	public boolean processBool(String id) {
		return "true".equals(processValue(id));
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#processBool(java.lang.String, boolean)
	 */
	public boolean processBool(String id, boolean defValue) {
		return defValue != Boolean.toString(!defValue).equals(processValue(id));
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#processInt(java.lang.String)
	 */
	public int processInt(String id) {
		try {
			return Integer.parseInt(processValue(id));
		}
		catch (NumberFormatException e) {
			return Integer.MIN_VALUE;
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#processContextElement(java.lang.String)
	 */
	public ContextElement processContextElement(String id) {
		ContextElement ce = context.get(id);
		if (ce == null)
			return null; // never returning null
		// do not eat editables... because they are likey to change on the client and
		// the client should know, which items to observe (for change)
		if (!STATUS_EDITABLE.equals(ce.getStatus())) {
			// remove from context to be sent to client...
			c2c.remove(ce);
		}
		return ce;
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#moveDown(java.lang.String)
	 */
	public void moveDown(String id) {
		if (id == null)
			return;
		int size = c2c.size() - 1; // no need to look at last element
		for (int i = 0; i < size; i++) {
			if (id.equals(c2c.get(i).getCssId())) {
				c2c.add(c2c.remove(i--));
				size--; // don't go over the stuff we moved already to the end
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#getSize()
	 */
	public int getSize() {
		return context.size();
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#toString()
	 */
	@Override
	public String toString() {
		// only printing c2c...
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		for (int i = 0; i < c2c.size(); i++) {
			buf.append(c2c.get(i).toString());
			buf.append("\r\n");
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#appendToBody(java.lang.String)
	 */
	public void appendToBody(String htmlNode) {
		// put the html into the contruction zone to convert it into DOM Objects
		innerHtml("constructionZone", htmlNode);

		// now move in to the documents right place.
		sendJavaScript("a2b", "var cz = byIdReq(\'constructionZone\');" + "var dlg = cz.firstChild;" + "cz.removeChild(dlg);"
				+ "document.getElementsByTagName('body')[0].appendChild(dlg);");

	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#restoreState(java.lang.String)
	 */
	public void restoreState(String id) {
		if (id == null)
			return; // very funny
		for (int i = c2c.size() - 1; i >= 0; i--) {
			if (id.equals(c2c.get(i).getCssId())) {
				c2c.remove(i); // may delete more than one, does that make sense?
			}
		}
		ContextElement ce = context.get(id);
		if (ce != null) {
			// readd at this position
			c2c.add(ce);
		}

	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#processPrefix(java.lang.String)
	 */
	public void processPrefix(String pref) {
		for (int i = c2c.size() - 1; i >= 0; i--) {
			if (c2c.get(i).getCssId().startsWith(pref)) {
				c2c.remove(i);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#moveTo(java.lang.String, int, int)
	 */
	public void moveTo(String id, int x, int y) {
		add(id + ".moveTo", "" + x + "x" + y, TYPE_MOVETO, STATUS_COMMAND);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#openURL(java.lang.String)
	 */
	public void openURL(String url) {
		openURL(url, "wgtopen");
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#openURL(java.lang.String, java.lang.String)
	 */
	public void openURL(String url, String windowname) {
		openURL(url, "wgtopen", "location=yes,menubar=yes,resizable=yes,scrollbars=yes,status=yes,toolbar=yes");
	}

	/* (non-Javadoc)
	 *@see com.endress.infoserve.wgt.ajax.IContext#openURL(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void openURL(String url, String windowname, String parameter) {
		String jsOpen = "WGTFenster = window.open(" + JSUtil.wrApoEsc(url) + ", \"" + windowname + "\", \"" + parameter
				+ "\" );WGTFenster.focus();";
		sendJavaScript("", jsOpen);
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.ajax.IContext#doPost(java.lang.String, java.util.Map, java.lang.String)
	 */
	public void doPost(String uri, Map<String, String> par, String target) {
		doFormSubmit("POST", uri, par, target);
	}

	/**
	 * @see com.endress.infoserve.wgt.ajax.IContext#doGet(java.lang.String, java.util.Map, java.lang.String)
	 */
	public void doGet(String uri, Map<String, String> par, String target) {
		doFormSubmit("GET", uri, par, target);
	}

	private void doFormSubmit(String methode, String uri, Map<String, String> par, String target) {
		doFormSubmit(methode, uri, par, target, false);
	}
	
	public void doFormSubmit(String methode, String uri, Map<String, String> par, String target, boolean showLoadingBar) {
		// Create the form using ecs:
		Form form = new Form();
		form.setMethod(methode);
		form.setTarget((target == null) ? "_blank" : target);
		form.setAction(uri);
		Input hidden;

		// loop over all parameters and include them.
		if (par != null) {
			for (Iterator<Map.Entry<String, String>> it = par.entrySet().iterator(); it.hasNext();) {
				Entry<String, String> entry = it.next();
				hidden = new Input();
				hidden.setType("hidden");
				hidden.setName(StringUtils.defaultString(entry.getKey()));
				hidden.setValue(StringUtils.defaultString(entry.getValue())); // convert value to String
				form.addElement(hidden);
			}
		}

		String fromString = form.toString();
//		innerHtml("constructionZone", fromString);
		add("constructionZone", fromString, IContext.TYPE_HTML, STATUS_COMMAND );
		// now we only need to automatically submit the created form
		String submitString = "byIdReq('constructionZone').firstChild.submit();";
		if( showLoadingBar )
			submitString = "showindicator();"+submitString;

		String js = "try{ " + submitString + " }catch (e) {alert('"
				+ TextService.getString("popupblocker.error.msg@Please change your browser options to allow popups for this page!")
				+ "'+e);}";
		sendJavaScript("doFormSubmit", js);
	}

	
	/**
	 * @see com.endress.infoserve.wgt.ajax.IContext#getContext()
	 */
	public Map<String, ContextElement> getContext() {
		return context;
	}

	/**
	 * @see com.endress.infoserve.wgt.ajax.IContext#appendAfter(java.lang.String, java.lang.String)
	 */
	public void appendAfter(String id, String html) {
		add(id + ".appendAfter", html, IContext.TYPE_APPEND_AFTER, STATUS_IDEM);

	}

	public String getInfo() {
		String info = "Context Elements size: " + context.size() + "\n";
		for (Iterator<Map.Entry<String, ContextElement>> iter = context.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, ContextElement> entry = iter.next();
			info += entry.getKey() + ", ";
		}
		return info;
	}
}
