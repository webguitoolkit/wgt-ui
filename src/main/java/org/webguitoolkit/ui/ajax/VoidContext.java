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

import java.util.Map;

/**
 * <pre>
 * This context is used for components that are not rendered and therefore 
 * they must not send anything to the clients context
 * </pre>
 */
public class VoidContext implements IContext {

	public void add(ContextElement ce) {
	}

	public void add(String cssId, int value, String type, String status) {
	}

	public void add(String cssId, String value, String type, String status) {
	}

	public void addClass(String nodeId, String newclass) {
	}

	public void appendAfter(String id, String html) {
	}

	public void appendHtml(String nodeId, String html) {
	}

	public void appendToBody(String htmlNode) {
	}

	public void appendValue(String cssId, String value, String type, String status) {
	}

	public ContextElement[] calculateContextChange() {
		return new ContextElement[] {};
	}

	public void cloneAndAdd(String fromID, String fatherID, String postfix) {
	}

	public void cloneAndInsert(String fromID, String beforeID, String postfix) {
	}

	public void doPost(String uri, Map par, String target) {
	}

	public String getCETrace(ContextElement ce) {
		return null;
	}

	public Map getContext() {
		return null;
	}

	public int getSize() {
		return 0;
	}

	public String getValue(String key) {
		return null;
	}

	public boolean getValueAsBool(String key, boolean defaults) {
		return false;
	}

	public boolean getValueAsBool(String key) {
		return false;
	}

	public int getValueAsInt(String key) {
		return 0;
	}

	public Map getWatchMap() {
		return null;
	}

	public void innerHtml(String nodeId, String html) {
	}

	public void makeVisible(String nodeId, boolean visible) {
	}

	public void modifyValue(ContextElement ce) {
	}

	public void moveDown(String id) {
	}

	public void moveTo(String id, int x, int y) {
	}

	public void openURL(String url) {
	}

	public void outerHtml(String nodeId, String html) {
	}

	public void popup(String nodeId, boolean visible) {
	}

	public boolean processBool(String id) {
		return false;
	}

	public boolean processBool(String id, boolean defValue) {
		return false;
	}

	public ContextElement processContextElement(String id) {
		return null;
	}

	public int processInt(String id) {
		return 0;
	}

	public void processPrefix(String pref) {
	}

	public String processValue(String id) {
		return null;
	}

	public void put(ContextElement ce) {
	}

	public void readOnlyState(String inputs, boolean readable) {
	}

	public void removeClass(String nodeId, String newclass) {
	}

	public void removeNode(String nodeId) {
	}

	public void replicateNode(String cssId, int times) {
	}

	public void restoreSnapshot(String nodeId, String version, boolean delete) {
	}

	public void restoreState(String id) {
	}

	public void sendJavaScript(String id, String js) {
	}

	public void sendJavaScriptState(String id, String js) {
	}

	public void setAttribute(String nodeId, String attributeName, String attributeValue) {
	}

	public void setClass(String nodeId, String className, boolean shouldExist) {
	}

	public void setValue(String key, String value) {
	}

	public void setValue(String key, int value) {
	}

	public void setVisible(String nodeId, boolean visible) {
	}

	public void takeSnapshot(String nodeId, String version) {
	}

	public void textChild(String nodeId, String text) {
	}

	public void openURL(String url, String windowname) {
	}

	public void openURL(String url, String windowname, String parameter) {
	}

	public void doGet(String uri, Map par, String target) {
	}
	public void doFormSubmit(String methode, String uri, Map<String, String> par, String target, boolean showLoadingBar){
	}

	public void removeContextElement(String contextElementId) {
	}

	public void appendHtml(String targetId, String toAppendId, String html) {
	}
}
