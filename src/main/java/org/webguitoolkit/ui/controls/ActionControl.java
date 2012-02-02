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
package org.webguitoolkit.ui.controls;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.ConcreteElement;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.util.JSUtil;

/**
 * Base class for controls using an ActionListener.
 *
 * @author Martin, Arno
 *
 *         Changed 235.03.2009 by Peter : exception handling in
 *         setActionListener(), getActionListener(), dispatch()
 */
public abstract class ActionControl extends BaseControl implements IActionControl {

	public ActionControl(String id) {
		super(id);
	}

	public ActionControl() {
		super();
	}

	// comma separated list of actions for witch the ActionListener is
	// registered (e.g.: onClick, onBlur)
	protected String registeredActions;

	@Override
	public void dispatch(ClientEvent event) {
		if (!hasExecutePermission())
			return;
		if (actionListener == null) {
			getPage().sendError("No Action Listener defined for " + getClass().getName() + ",id= " + getId());
			return;
		}

		if (event.getTypeAsInt() == ClientEvent.TYPE_ACTION) {
			getActionListener().onAction(event);
		}

	}

	protected IActionListener actionListener;

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.webguitoolkit.ui.controls.IActionControl#setActionListener(org.
	 * webguitoolkit.ui.controls.event.IActionListener)
	 */
	public void setActionListener(IActionListener al) {
		if (al == null)
			throw new WGTException("listener must be not null");
		actionListener = al;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.webguitoolkit.ui.controls.IActionControl#getActionListener()
	 */
	public IActionListener getActionListener() {

		return actionListener;
	}

	public String getRegisteredActions() {
		return registeredActions;
	}

	public void setRegisteredActions(String registeredActions) {
		this.registeredActions = registeredActions;
	}

	public String[] getRegisteredActionsAsArray() {
		if (registeredActions == null || "".equals(registeredActions))
			return new String[] {};
		else
			return registeredActions.split(",");
	}
	/**
	 * this registers a single action is not already present.
	 * @param thisAction
	 */
	public void addRegisteredAction(String thisAction) {
		if (StringUtils.isBlank(registeredActions)){
			registeredActions = thisAction;
			return;
		}
		// test if present...
		if (registeredActions.contains(thisAction)) return;
		registeredActions += "," + StringUtils.trimToEmpty(thisAction);
	}
	
	public void removeRegisteredAction(String thisAction) {
		if (StringUtils.isBlank(registeredActions)){
			return;
		}
		int ind = registeredActions.indexOf(thisAction);
		if (ind == -1) return;
		int commaind = registeredActions.indexOf(thisAction +",");
		if (commaind >= 0) {
			registeredActions = registeredActions.replace(thisAction +",", "");
		} else {
			registeredActions = registeredActions.replace(thisAction, "");			
		}
	}

	public void writeOnActionCommands(PrintWriter out, String defaultAction) {
		// action listener
		if (hasActionListener()) {

			// get registered Actions
			String[] actions = getRegisteredActionsAsArray();

			// if no action is registered do the default behavior for this
			// element
			if (actions.length == 0)
				actions = new String[] { defaultAction };

			for (int i = 0; i < actions.length; i++)
				out.print(JSUtil.onAction(actions[i], JSUtil.jsFireEvent(getId(),
						ClientEvent.TYPE_ACTION, actions[i])));
		}
	}

	public void writeOnActionCommands(ConcreteElement element, String defaultAction) {
		// action listener
		if (hasActionListener()) {

			// get registered Actions
			String[] actions = getRegisteredActionsAsArray();

			// if no action is registered do the default behavior for this
			// element
			if (actions.length == 0)
				actions = new String[] { defaultAction };

			for (int i = 0; i < actions.length; i++) {
				setOnAction(element, actions[i], JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION, actions[i]));
			}
		}
	}

	private void setOnAction(ConcreteElement element, String action, String js) {
		Method[] m = element.getClass().getMethods();
		for (int i = 0; i < m.length; i++) {
			if (m[i].getName().equalsIgnoreCase(("set" + action).toUpperCase())) {
				try {
					m[i].invoke(element, new String[] { js });
				} catch (Exception e) {
					Logger.getLogger(this.getClass()).error("Error setting action", e);
				}
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.webguitoolkit.ui.controls.IActionControl#hasActionListener()
	 */
	public boolean hasActionListener() {
		return actionListener != null;
	}

}
