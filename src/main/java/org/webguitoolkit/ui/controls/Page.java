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
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Link;
import org.apache.log4j.Logger;
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.DWRCallback;
import org.webguitoolkit.ui.ajax.DWRController;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.dialog.DialogUtil;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IExternalEventListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.LifecycleServerEvent;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.http.ResourceServlet;
import org.webguitoolkit.ui.http.resourceprocessor.JSResourceProcessor;

/**
 * <pre>
 * Base class for implementation of custom pages.
 * 
 * To implement your own page you have to extend Page and overwrite the initPage() to create
 * the controls and the title() to get the page's title.
 * 
 * In the browser you can call the page: 
 * http://[yourserver]/[yourcontext]/pages/[yourpackage].[yourpageclass]
 * and if You have mapped the package:
 * http://[yourserver]/[yourcontext]/pages/[yourpageclass]
 * 
 * </pre>
 */
public abstract class Page extends BaseControl implements IComposite {

	private static final int STADARD_WAIT_TIMEOUT = 1000;
	private static final int DEFAULT_WAIT_TIME = 300;

	public static final String DOT_URL = ".url";
	public static final String UN_LOAD_EVENT = "unLoadEvent";
	public static final String EXTERNAL_EVENT = "externalEvent";
	public static final String TIMER_EVENT = "timerEvent";
	public static final String BASE_PARAMETER = "base";


	protected static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	protected static ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();

	private long creationDate = 0;

	protected IContext context = new Context();
	// boolean service for components is cleared on each render process
	protected HashSet<String> present;
	// used for the components to determine if the client changed their value in the context
	protected Set<String> clientContextChange = new HashSet<String>();
	protected String waitMessage = null;
	protected int waitTimeout = STADARD_WAIT_TIMEOUT;
	protected String sessionExpiredMessage = null;
	// handles the listener and events for page.
	protected ListenerManager listenerManager = new ListenerManager();
	protected IExternalEventListener externalEventListener = null;
	protected Draggable draggable = new Draggable();
	protected Set<String> header = new LinkedHashSet<String>();
	// scripts that are already present on the page
	protected Set<String> loadedScripts = new HashSet<String>();
	// what is the name of our application prefix in the actual server?
	protected static String applicationRoot;
	protected static String serverScheme;

	/**
	 * default constructor must exist.
	 */
	public Page() {
		// prevent page from being null
		this.page = this;
		creationDate = System.currentTimeMillis();
	}
	
	/**
	 * init the page, here the application should create all sub components. this will be called once per initial request. Also
	 * you can load your data into the controls.
	 */
	protected abstract void pageInit();

	
	/**
	 * Applicationprogrammer gets access to the raw response object. The application programmer must be aware that ther might me
	 * no request object, or the request is from an Ajax call (usng XML) or from an JSP, or trying to get a chart (picture).
	 * However the programmer must be aware of what kind of request the application is in to use this.
	 * 
	 * @return the pages request object
	 */
	public static HttpServletRequest getServletRequest() {
		return (HttpServletRequest)requests.get();
	}

	/**
	 * this is only supposed to be called once a the very beginning of the request-cycle to give the rest of the gang acces to the
	 * object via the get method.
	 * 
	 * @param req the pages request object
	 */
	public static void setServletRequest(HttpServletRequest req) {
		requests.set(req);
	}

	/**
	 * Application programmer gets access to the raw response object. The application programmer must be aware that there might be
	 * no request object, or the request is from an AJAX call (using XML) or from an JSP, or trying to get a chart (picture).
	 * However the programmer must be aware of what kind of request the application is in to use this.
	 * 
	 * @return the pages response object
	 */
	public static HttpServletResponse getServletResponse() {
		return (HttpServletResponse)responses.get();
	}

	/**
	 * this is only supposed to be called once a the very beginning of the request-cycle to give the rest of the gang acces to the
	 * object via the get method.
	 * 
	 * @param res the pages response object
	 */
	public static void setServletResponse(HttpServletResponse res) {
		responses.set(res);
	}


	public String createUnloadEventString() {
		StringBuffer unload = new StringBuffer();
		unload.append(JSUtil.jsEventParam(getId(), new String[] { Long.toString(creationDate) }));
		unload.append(JSUtil.jsFireEvent(getId(), UN_LOAD_EVENT));
		return unload.toString();
	}

	ListenerManager timers = new ListenerManager();

	public void timer(int callMeInMillis, IServerEventListener liz) {
		// determine the expiration time
		int ex = (int)System.currentTimeMillis() + callMeInMillis;
		// register call back, id is the expiration time
		timers.registerListener(ex, liz);
		// now send timer to client
		String exString = Integer.toString(ex);
		String timeoutCommand = "setTimeout(\"" + JSUtil.jsEventParam(getId(), new String[] { exString })
				+ JSUtil.jsFireEvent(getId(), TIMER_EVENT) + "\"," + callMeInMillis + ");";
		getContext().sendJavaScript(getId(), timeoutCommand);
	}

	/**
     * 
     */
	@Override
	protected void startHTML(PrintWriter out) {
		start = System.currentTimeMillis();
		// we need this to place the onload js-handler
		out.print("<BODY "
				// loadOnInit will be defined at the end.
				+ JSUtil.atNotEmpty("onload", "loadOnInit();fillIdCache();") + JSUtil.atId(getId())
				+ JSUtil.atNotEmpty("class", getCssClass()) + JSUtil.atNotEmpty("style", getStyleAsString())
				+ JSUtil.atNotEmpty("onunload", createUnloadEventString()) + ">");
		// send unload eventhandler with load event so it can't be fired too early
		// getContext().setAttribute(getId(), "onunload", createUnloadEventString());
		// write needed things for load indicator
		if (waitMessage != null) {
			out.write("\n<script>var waitmessage =" + JSUtil.wrApoEsc(TextService.getString(waitMessage)) + ";");
			out.write("var timeout = " + waitTimeout + ";");
			out.write("</script>");
			// caching wait pictures specially for IE
			out.write("<div style=\"display:none;\"><img src=\"./images/wgt/window_title.gif\"><img src=\"./images/wgt/icons/close.gif\"><img src=\"./images/wgt/loadingbar2.gif\"></div>");
			draggable.draw(out);
		}
		if (getSessionExpiredMessage() != null) {
			out.write("\n<script>var sessionexpiredmessage =" + JSUtil.wrApoEsc(TextService.getString(sessionExpiredMessage)) + ";");
			out.write("</script>");
		}
	}
	protected long start;
	
	@Override
	protected void endHTML(PrintWriter out) {
		try {
			StringBuffer contexts = new StringBuffer();
			// get all the chages which are collectable until now.
			ContextElement[] chg = calculateContextChange();
			ContextElement aktChg;
			for (int i = 0; i < chg.length; i++) {
				aktChg = chg[i];
				if (aktChg.getCssId().startsWith(getId() + ".append")) {
					out.println(aktChg.getValue());
				}
				else {
					// collect all changes in an array
					contexts.append("ctx[ctx.length] = { cssId: \"" + aktChg.getCssId() + "\", value:" + JSUtil.wrApoEsc(aktChg.getValue())
							+ ", type:\"" + aktChg.getType() + "\", status:\"" + aktChg.getStatus() + "\"};\n\r");
				}
			}

			// now is the latest time to define out onload context elements
			// applyContext({1:{ cssId: "contextSize", value:"force", type:"txt", status:"n"}})
			out.println("<script type=\"text/javascript\">");
			out.println("function loadOnInit() {");
			out.println("var ctx = new Array();");
			out.println(contexts.toString());
			// to manually process it in owrk-loop for processing context elements.
			out.println("applyContext(ctx);");
			out.println("}</script>");
			out.print("</BODY>");
			
			if (DWRCallback.debug) {
				// debug
				// log the events to monitor
				IDataBag bag = WebGuiFactory.getInstance().createDataBag(this);
				bag.addProperty("time", new Date());
				bag.addProperty("inbound", new ContextElement[]{});
				bag.addProperty("outbound", chg);
				bag.addProperty("source", getId());
				bag.addProperty("type", "html");
				bag.addProperty("duration", (System.currentTimeMillis() - start) + "ms.");
				DWRCallback.contextLog.add(0, bag);
				// debug ende
			}

		}
		catch (RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * this method is being called by the framework only. When a ajax request comes in it gets processed here.
	 */
	@Override
	public void dispatch(ClientEvent event) {
		if (!id.equals(event.getSourceId()))
			throw new WGTException("Wrong source id. " + event.getSourceId() + ", expected " + id);
		else if (UN_LOAD_EVENT.equals(event.getType())) {
			String cDate = event.getParameter(0);
			// MH: only remove current body (bug when refresh on a invalid session) 19.11.2008
			if (Long.toString(creationDate).equals(cDate)) {
				unload(event);
			}
		}
		else if (EXTERNAL_EVENT.equals(event.getType())) {
			getExternalEventListener().onExternalEvent(event);
		}
		else if (TIMER_EVENT.equals((event.getType()))) {
			// convert client event into server event
			int timerId = Integer.parseInt(event.getParameter(0));
			// timerId = eventtype in the timerRegistry
			ServerEvent timerEvent = new ServerEvent();
			timerEvent.setControl(this);
			timerEvent.setType(timerId);
			timers.fireServerEvent(timerEvent);
			// fire timers only once if you need repetition, then set it again.
			timers.removeListener(timerId, null); // null means all from this type
		}
	}

	/**
	 * javascript body.unload has been called. So this page is not seen by any user anymore. So we are destroying it here.
	 * 
	 * @param event
	 */
	protected void unload(ClientEvent event) {
		HttpSession session = Page.getServletRequest().getSession();
		if (session == null)
			return; // session may have timed out already
		remove();
	}

	/**
	 * @return Returns the context.
	 */
	@Override
	public IContext getContext() {
		return context;
	}

	/**
	 * return true if the client has change a value in regards of this cssId.
	 * 
	 * @param cssId
	 * @return
	 */
	public boolean clientContextChanged(String cssId) {
		return clientContextChange.contains(cssId);
	}

	public BaseControl searchById(String id) {
		return DWRController.getInstance().getComponentById(id);
	}

	public void fireServerEvent(ServerEvent event) {
		listenerManager.fireServerEvent(event);
	}

	public void removeListener(int eventtype, IServerEventListener liz) {
		listenerManager.removeListener(eventtype, liz);
	}

	public void registerListener(int eventtype, IServerEventListener liz) {
		listenerManager.registerListener(eventtype, liz);
	}

	@Override
	protected void init() {
		if (Page.getServletRequest() != null) {
			HttpServletRequest request = Page.getServletRequest();
			applicationRoot = request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
			serverScheme = request.getScheme();
		}
		// record for each request what changed
		registerListener(ServerEvent.EVENT_PREDISPATCH, new IServerEventListener() {
			public void handle(ServerEvent event) {
				ContextElement[] chg = ((LifecycleServerEvent)event).getClientEvent().getChgContext();
				for (int i = 0; i < chg.length; i++) {
					if (chg[i] != null)
						clientContextChange.add(chg[i].getCssId());
				}
			}
		});
		// clear the changes at the end of the request
		registerListener(ServerEvent.EVENT_POSTDISPATCH, new IServerEventListener() {
			public void handle(ServerEvent event) {
				clientContextChange.clear();
			}
		});
		// this init must be overridden by the applications
		pageInit();
	}


	public ContextElement[] calculateContextChange() {
		ContextElement[] ret = context.calculateContextChange();
		return ret;
	}

	public void gotoUrl(String url) {
		// TODO discard current component-tree!
		getContext().add(getId() + DOT_URL, url, IContext.TYPE_URL, IContext.STATUS_NOT_EDITABLE);
	}

	/**
	 * Tell the client to load a page with the given uri. Actually the parameter is not quite the uri. It will be prepended with
	 * the name of the application, so that you only need to pass in the path within the application itself. Do not begin with a
	 * "/".
	 */
	public void gotoApplicationPage(String uri) {
		gotoUrl(serverScheme + "://" + applicationRoot + "/" + uri);
	}

	// public void showDialog(ConfirmDialog diag) {
	// diag.getAnswer().ok(new Event(this, this.getId(), null, null)); // fake for now
	// }

	public boolean isPresent(String key) {
		if (present == null)
			return false;
		return present.contains(key);
	}

	public void setPresent(String key, boolean p) {
		if (p) {
			if (present == null)
				present = new HashSet(2);
			present.add(key);
		}
		else {
			if (present != null) {
				present.remove(key);
			}
		}
	}

	/**
	 * 
	 * @return the sessionExpiredMessage
	 */
	public String getSessionExpiredMessage() {
		return sessionExpiredMessage;
	}

	/**
	 * @param sessionExpiredMessage the sessionExpiredMessage to set
	 */
	public void setSessionExpiredMessage(String sessionExpiredMessage) {
		this.sessionExpiredMessage = sessionExpiredMessage;
	}

	/**
	 * @return the waitMessage
	 */
	public String getWaitMessage() {
		return waitMessage;
	}

	/**
	 * @return the waitTimeout
	 */
	public int getWaitTimeout() {
		return waitTimeout;
	}

	/**
	 * @param waitMessage the waitMessage to set
	 */
	public void setWaitMessage(String waitMessage) {
		this.waitMessage = waitMessage;
	}

	/**
	 * @param waitTimeout the waitTimeout to set
	 */
	public void setWaitTimeout(int waitTimeout) {
		this.waitTimeout = waitTimeout;
	}

	/**
	 * this works on the hole page and diables the underlying layer only element with special zIndex will receive user input. Used
	 * mainly from canvas.modal. Moved here because it affects the hole page, so its rather a property of the page than the
	 * canvas.
	 * 
	 * @param vis
	 */
	public void disableZone(boolean vis, String elementId) {
		getContext().sendJavaScript(getId() + ".zone", "disableZone(" + vis + ",'" + elementId + "');");

	}

	/**
	 * pops up a error message box
	 * 
	 * @param msg the message
	 */
	public void sendError(String msg) {
		sendError(msg, null);
	}

	public void sendError(String msg, String errorDialogId) {
		DialogUtil.sendMessage(this, msg, DialogUtil.ERROR, null, errorDialogId);
	}
	
	/**
	 * pops up a error message box
	 * 
	 * @param msg the message
	 */
	public void sendInfo(String msg) {
		sendInfo(msg, null);
	}

	public void sendInfo(String msg, String infoDialogId) {
		DialogUtil.sendMessage(this, msg, DialogUtil.INFO, null, infoDialogId);
	}
	
	/**
	 * pops up a error message box
	 * 
	 * @param msg the message
	 */
	public void sendWarn(String msg) {
		sendWarn(msg, null);
	}

	public void sendWarn(String msg, String warnDialogId) {
		DialogUtil.sendMessage(this, msg, DialogUtil.WARN, null, warnDialogId);
	}
	
	/**
	 * 
	 * @return the external event listener for inter frame communication
	 */
	public IExternalEventListener getExternalEventListener() {
		if (externalEventListener == null) {
			throw new WGTException("No ExternalEventListenerClass " + "defined for event at component-id " + getId());
		}
		return externalEventListener;
	}

	/**
	 * is called to generate the content of the page. this is the content of the request. Is called from the page-servlet. Don't
	 * call this from your application.
	 * 
	 * @param out the stream to write to
	 */
	public void writePage(PrintWriter out) {
		// first init everything so it get the opportunity to access the header
		// init page
		pageDefaults();
		// must call the init myself, as the page is never added to ans component, so the add will not
		// call init.
		init();

		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<html>");
		out.println("<head>");
		writeHeader(out);
		out.println("</head>");
		drawInternal(out);
		out.println("</html>");
	}

	/**
	 * write the collected headers to the output stream. The framework and the component, as well as application can set the
	 * headers (javascript, css,...) of the page. see addHeaderXXX(...)
	 * 
	 * @param out the stream to write to
	 */
	protected void writeHeader(PrintWriter out) {
		for (Iterator it = header.iterator(); it.hasNext();) {
			String hsLine = (String)it.next();
			out.println(hsLine);
		}
	}

	/**
	 * writes the base tag to the header
	 */
	protected void defBase() {
		HttpServletRequest request = Page.getServletRequest();
		// calculate the base path, as each resource references relatively from the root of the project
		String path = request.getContextPath();
		String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		addHeaderLine("<base href=\"" + base + "\">");
	}

	/**
	 * adds any header line to the page headers
	 * 
	 * @param line the line to add
	 */
	public void addHeaderLine(String line) {
		// we can not add something to the header when it is already rendered
		if (!header.contains(line) && isDrawn()) {
			// System.out.println("WARN: ### Trying to add a header line when the page is already rendered! ### " + line);
			Logger.getLogger(this.getClass()).warn("Trying to add a header line when the page is already rendered! " + line);
		}
		header.add(line);
	}

	/**
	 * adds the default meta tags
	 */
	protected void defMeta() {
		addHeaderLine("<meta http-equiv=\"cache-control\" content=\"no-cache\">");
		addHeaderLine("<meta http-equiv=\"pragma\" content=\"no-cache\">");
	}

	/**
	 * adds the default HTML headers
	 */
	protected void pageDefaults() {
		// everytime this is executed, the page is created from scratch
		// you may change that by overriding this method.
		setWaitMessage("general.loadingdata@Loading data");
		setWaitTimeout(DEFAULT_WAIT_TIME);
		// header files loaded...
		header.clear();
		defBase();
		defMeta();
		defTitle();
		defJS();
		defCSS();

	}

	/**
	 * @return the default factory
	 */
	protected WebGuiFactory getFactory() {
		return WebGuiFactory.getInstance();
	}

	/**
	 * function to write title to the HTML
	 */
	protected void defTitle() {
		String titel = title();
		if (titel == null)
			addHeaderLine("<title>" + getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1) + "</title>");
		else
			addHeaderLine("<title>" + title() + "</title>");
	}

	/**
	 * since this method is not a property getter as defined in JavaBean convention we can't name it 'getTitle'.. Subclasses must
	 * override it to make sure the title of the page is set correctly.
	 * 
	 * @return the pages title
	 */
	protected abstract String title();

	/**
	 * write the JavaScript part, usually you don't need to override this, just add additional JavaScript with addJS(...);
	 */
	protected void defJS() {
		addHeaderJS("dwr/interface/DWRCaller.js");
		addHeaderJS("dwr/engine.js");
		addHeaderJS("dwr/util.js");
		if( JSResourceProcessor.isBundleScripts() ){
			// deliver one big js file
			addHeaderLine("<script type='text/javascript' src='./" + ResourceServlet.SERVLET_URL_PATTERN + "/" + JSResourceProcessor.WGT_BASE
					+ "'></script>");
		}
		else{
			// deliver each js file separately
			for( String jsFile : JSResourceProcessor.WGT_BASE_FILES ){
				internalAddHeaderJS( "./" + ResourceServlet.SERVLET_URL_PATTERN + "/" + jsFile );
			}
		}
		loadedScripts.addAll(JSResourceProcessor.WGT_BASE_FILES);
	}

	/**
	 * adds a JavaScript file to the header
	 * 
	 * @param jsFile the file name
	 */
	public void addHeaderJS(String jsFile) {

		if (jsFile.indexOf(ResourceServlet.SERVLET_URL_PATTERN + "/") > -1) {
			String shortName = jsFile.substring(jsFile.indexOf(ResourceServlet.SERVLET_URL_PATTERN + "/")
					+ (ResourceServlet.SERVLET_URL_PATTERN + "/").length());
			addWgtJS(shortName);
		}
		else
			internalAddHeaderJS(jsFile);
	}
	
	private void internalAddHeaderJS(String jsFile){
		
		if (!isDrawn()) // add to page header
			header.add("<script type=\'text/javascript\' src=\'" + jsFile + "\'></script>");
		else // load lazy
			getContext().add(getId() + "_loadJs", jsFile, IContext.TYPE_LLJ, IContext.STATUS_COMMAND);		
	}

	/**
	 * adds a CSS file to the header
	 * 
	 * @param cssFile the css file
	 */
	public void addHeaderCSS(String cssFile) {
		String line = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + cssFile + "\">";

		// we have to send the JavaScript dynamically to the client
		if (!header.contains(line) && isDrawn()) {
			getContext().add(getId() + "_loadCss", cssFile, "llcss", IContext.STATUS_COMMAND);		
		}
		header.add(line);
	}

	/**
	 * adds a WGT JavaScript file
	 * 
	 * @param jsFile the file name
	 */
	public void addWgtJS(String jsFile) {
		Logger.getLogger(this.getClass()).debug("added Script to page: " + jsFile );
		
		if( loadedScripts.contains(jsFile) )
			return;

		List<String> dependendFiles = JSResourceProcessor.getDependendFiles(jsFile, loadedScripts);
		loadedScripts.addAll(dependendFiles);
		loadedScripts.add(jsFile);
		
		String include = "";
		if(JSResourceProcessor.isBundleScripts() ){
			// deliver one file that knows its includes
			for( String file: dependendFiles){
				if( file.equals(jsFile))
					continue;
				include+=file+",";
			}		
			if( !StringUtils.isEmpty(include) ){
				jsFile += ( "?include=" + include );
			}
			internalAddHeaderJS( "./" + ResourceServlet.SERVLET_URL_PATTERN + "/" + jsFile );
		}
		else{
			// deliver every file separately
			for( String file: dependendFiles){
				if( file.equals(jsFile))
					continue;
				internalAddHeaderJS( "./" + ResourceServlet.SERVLET_URL_PATTERN + "/" + file );
			}		
			internalAddHeaderJS( "./" + ResourceServlet.SERVLET_URL_PATTERN + "/" + jsFile );
		}
	}
	/**
	 * @param tableJs
	 */
	public void addControllerJS(String controller) {
		addWgtJS(JSResourceProcessor.PREFIX_WGT_CONTROLLER+controller);
	}

	/**
	 * adds a WGT CSS file
	 * 
	 * @param cssFile the cssFile name
	 */
	public void addWgtCSS(String cssFile) {
		addHeaderCSS("./" + ResourceServlet.SERVLET_URL_PATTERN + "/" + cssFile);
	}

	/**
	 * pass path of .ico, .gif od .jpg for for favicon.
	 * 
	 * @param iconFile the file name
	 */
	public void addFavicon(String iconFile) {
		if (StringUtils.isNotBlank(iconFile)) {
			Link favicon = new Link();
			favicon.setRel("SHORTCUT ICON");
			favicon.setHref(iconFile.trim());
			this.addHeaderLine(favicon.toString());
		}
	}

	protected void defCSS() {
		// project specific css reference already the base css.
	}

	public void fireExternalEvent(String frame, List parameter) {
		String paramArray = "[";
		for (Iterator iter = parameter.iterator(); iter.hasNext();) {
			paramArray += " '" + iter.next() + "' ";
			if (iter.hasNext())
				paramArray += ",";
		}
		paramArray += "]";
		String value = "{ frame: '" + frame + "', parameter: " + paramArray + " }";
		getContext().add(new ContextElement(getId(), value, IContext.TYPE_EXT, IContext.STATUS_COMMAND));
	}

	@Override
	public void add(IBaseControl child) {
		super.add(child);
	}

	public class Draggable implements Serializable {
		private Object dataObject = null;
		private String dragLabel = null;

		public void setLabel(String label) {
			dragLabel = label;
			getContext().sendJavaScript(getId(), "jQuery('#dragObject').html( '" + label + "' );");
			getContext().sendJavaScript(getId(), "jQuery('#dragObject_clone').html( '" + label + "' );");
		}

		public String getLabel() {
			return dragLabel;
		}

		public void draw(PrintWriter out) {
			out.write("<div id=\"dragObject\" class=\"wgtDraggable\" style=\"display:none;\">test</div>");
		}

		public Object getDataObject() {
			return dataObject;
		}

		public void setDataObject(Object dataObject) {
			this.dataObject = dataObject;
		}
	}

	public Draggable getDraggable() {
		return draggable;
	}

}
