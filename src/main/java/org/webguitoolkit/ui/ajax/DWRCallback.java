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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.security.AntiSamyFilter;
import org.webguitoolkit.ui.ajax.security.ContextFilter;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.LifecycleServerEvent;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.ITextarea;
import org.webguitoolkit.ui.controls.layout.SequentialTableLayout;
import org.webguitoolkit.ui.controls.util.TextService;

/**
 * <pre>
 * This class is called by the DWR framework to execute the clients request.
 * 
 * The callback class and the callback method are defined in the dwr.xml witch
 * is located in the WEB-INF folder of the web application.
 * 
 * In our case the callback method is event( id, eventType, contextChanes ).
 * </pre>
 */
public class DWRCallback {
	// debug context history for logging
	public static List contextLog = Collections.synchronizedList(new ArrayList(100));

	// max len for above
	public static int maxLogEntries = 100;

	// debug flag for logger
	public static boolean debug = false;

	private final ContextFilter filterChain = new ContextFilter();

	/**
	 * Constructor is called from DWR Library. it will construct a connection to the DWRController and pass the Event.
	 */
	public DWRCallback() {
		super();
		filterChain.addFilter(new AntiSamyFilter());
	}

	/**
	 * Callback method called by the DWR framework on <b>every</b> DWR call.
	 * 
	 * @param source id of the event firing control
	 * @param type event type
	 * @param context changes from the client
	 * @return context changes on the server
	 */
	public ContextElement[] event(String source, String type, ContextElement[] context) {
		BaseControl control = null;
		try {
			long start = System.currentTimeMillis();

			// filter the input for dangerous values
			filterChain.filterContexts(context);

			control = (BaseControl)DWRController.getInstance().getComponents().get(source);
			if (control == null) {
				if (Page.UN_LOAD_EVENT.equals(type))
					return null; // unload allready done
				Logger.getLogger(this.getClass()).error("Component not found: " + source + " eventtype: " + type);

				throw new WGTException("Component not found: " + source + " eventtype: " + type + " from Session "
						+ Page.getServletRequest().getSession().getId());
			}
			// the contextchanges returned to the client to be executed.
			ContextElement[] ret;

			final Page page = control.getPage();
			synchronized (page) {

				if (context != null)
					updateServerContext(page, context);
				ClientEvent event = new ClientEvent(control, source, type, context);
				// Lifecycle management
				LifecycleServerEvent preDispatch = new LifecycleServerEvent(control, ServerEvent.EVENT_PREDISPATCH, event);
				page.fireServerEvent(preDispatch);
				control.dispatch(event);
				// event to inform that the dispatch is done.
				LifecycleServerEvent postDispatch = new LifecycleServerEvent(control, ServerEvent.EVENT_POSTDISPATCH, event);
				page.fireServerEvent(postDispatch);

				ret = page.calculateContextChange();

				// filter the output for dangerous values
				// filterChain.filterContexts(ret);

				if (debug) {
					// debug
					// log the events to monitor
					IDataBag bag = WebGuiFactory.getInstance().createDataBag(control);
					bag.addProperty("time", new Date());
					bag.addProperty("inbound", context);
					bag.addProperty("outbound", ret);
					bag.addProperty("source", source);
					bag.addProperty("type", type);
					bag.addProperty("duration", (System.currentTimeMillis() - start) + "ms.");
					if (contextLog.size() >= maxLogEntries) {
						contextLog.remove(maxLogEntries - 1);
					}
					contextLog.add(0, bag);
					// debug ende
				}
			}

			return ret;
		}
		catch (WGTException e) {
			if (control == null) {
				return null;
			}
			handleException(e, control);
			return control.getPage().calculateContextChange();
		}
		catch (Throwable e) {
			if (control == null) {
				return null;
			}
			// clear context
			control.getPage().calculateContextChange();

			handleException(e, control);
			return control.getPage().calculateContextChange();
		}
	}

	/**
	 * handle errors to present to the user in a readable way. Assuming we can recover from this error....
	 * 
	 * @param t
	 * @param comp
	 */
	public static void handleException(final Throwable t, final BaseControl comp) {
		if (comp == null || t == null)
			throw new WGTException(t);
		Logger.getLogger(DWRCallback.class).error(t.getMessage(), t);

		final StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));

		comp.getPage().getContext().appendHtml(comp.getPage().getId(), "");
		final AbstractPopup dialog = new AbstractPopup(WebGuiFactory.getInstance(), comp.getPage(),
				TextService.getString("general.exception@Exception"), 700, 500) {

			@Override
			protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
				// TODO Auto-generated method stub

				viewConnector.setLayout(new SequentialTableLayout());

				ILabel label = factory.createLabel(viewConnector, "");
				label.setText(t.getClass().getSimpleName() + ": " + t.getMessage());
				label.addCssClass("wgtLabelFor");
				label.setLayoutData(SequentialTableLayout.getLastInRow());

				final ITextarea text = factory.createTextarea(viewConnector, "text");
				text.setValue(sw.toString());
				text.setVisible(false);
				text.getStyle().addWidth("670px");
				text.getStyle().addHeight("400px");
				text.setLayoutData(SequentialTableLayout.getLastInRow());

				IButton details = factory.createButton(viewConnector, null, "general.exceptionShow@Details",
						"general.exceptionShowTitle@Details of Exception", new IActionListener() {
							public void onAction(ClientEvent event) {
								text.setVisible(true);
							}
						});
				IButton close = factory.createButton(viewConnector, null, "general.exceptionclose@Close", "general.exceptionclose@Close",
						new IActionListener() {
							public void onAction(ClientEvent event) {
								close();
							}
						});
				close.setLayoutData(SequentialTableLayout.APPEND);

			}

		};
		dialog.show();

		LifecycleServerEvent postDispatch = new LifecycleServerEvent(comp, ServerEvent.EVENT_POSTDISPATCH, null);
		comp.getPage().fireServerEvent(postDispatch);
	}

	/**
	 * This method is being called from the DWRFramework and asks for updating the server context with the information the client
	 * has send.
	 * 
	 */
	public void updateServerContext(Page page, ContextElement[] con) {
		IContext context = page.getContext();
		for (int i = 0; i < con.length; i++) {
			if (con[i] != null) {
				context.getContext().put(con[i].getCssId(), con[i]);
			}
		}
	}

}
