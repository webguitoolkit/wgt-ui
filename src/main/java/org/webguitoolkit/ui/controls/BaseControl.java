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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.ConcreteElement;
import org.webguitoolkit.ui.ajax.Context;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.DWRController;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.ajax.VoidContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.layout.ILayout;
import org.webguitoolkit.ui.controls.layout.ILayoutData;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.Tooltip;
import org.webguitoolkit.ui.controls.util.style.IStyleChangeListener;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.controls.util.style.selector.InlineSelector;
import org.webguitoolkit.ui.html.HTMLTag;
import org.webguitoolkit.ui.html.HTMLUtil;
import org.webguitoolkit.ui.permission.PermissionManager;

/**
 * <pre>
 * Base class for all controls of WGT framework.
 * 
 * </pre>
 */
public abstract class BaseControl implements IBaseControl {
	/**
	 * 
	 */
	private static final String ELEMENT_ATTRIBUTE_NAME = "name";

	private static final long serialVersionUID = 1L;

	protected BaseControl parent;
	protected Page page;
	protected ArrayList<BaseControl> children;
	protected Style style;
//	protected String tooltip;
	protected String id;
	private IContext context;
	private boolean isDrawn;
	private Tooltip tooltipAdvanced;
	protected ILayoutData layoutData;
	protected ILayout layoutManger;
	protected List<String> cssClassList;
	protected boolean useDefaultCssClass = true;
	private final Set<String> attributes = new HashSet<String>();

	/**
	 * <b>Standard constructor</b><br>
	 * ID is generated automatically, it is default the first 2 characters plus a sequence that is unique in the session.
	 */
	public BaseControl() {
		this(null);
	}

	/**
	 * <b>Constructor with id</b><br>
	 * A id has to be unique on the page.
	 * 
	 * @param id HTML id for the created code
	 */
	public BaseControl(String id) {
		this.id = id;
		// make this component searchable by its ID
		DWRController.getInstance().registerForEvents(getId(), this);
	}

	/**
	 * this method is called to produce the closing HTML Tag.
	 * 
	 * @param out the PrintWirter to write to
	 */
	protected abstract void endHTML(PrintWriter out);

	/**
	 * init -- informs the component that the init event is taking place. This method is only called on the component if there is
	 * no InitListener registered with the component. By default it should call the model initially and transport the data into
	 * the context. init is being called right after generateHTML.
	 */
	protected abstract void init();

	/**
	 * detaches all children of this component. They will not be in the tree hierarchy afterwards.
	 * 
	 * If the application does not reference it any more, the garbage collector will remove the objects.
	 */
	public void removeAllChildren() {
		// merken aller knoten im baum
		final List<BaseControl> deleteList = new ArrayList<BaseControl>();
		// ganzen componenten wieder removen.
		BaseControl.Visitor delSubs = new BaseControl.Visitor() {
			public boolean visit(BaseControl host) {
				if (host != BaseControl.this) {
					deleteList.add(host);
				}
				return true;
			}
		};
		travelDFS(delSubs);
		// now do the real delete to not disturb the recursion
		for (Iterator<BaseControl> it = deleteList.iterator(); it.hasNext();) {
			BaseControl mortem = it.next();
			mortem.removeInternal();
		}

	}

	/**
	 * notification for the child objects if the parent is set to visible rsp. invisible
	 * 
	 * @param visible true for showing the control
	 */
	public void processSetVisible(boolean visible) {
		if (getChildren() != null) {
			// then all subs
			List<BaseControl> childList = new ArrayList<BaseControl>(getChildren()); // abstracting from change in the sublist
			for (Iterator<BaseControl> it = childList.iterator(); it.hasNext();) {
				BaseControl child = it.next();
				child.processSetVisible(visible);
			}
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#getParent()
	 * 
	 * @return the parent control in the component tree
	 */
	public BaseControl getParent() {
		return parent;
	}

	/**
	 * sets the parent BaseControl in the component tree
	 * 
	 * @param parent the parent control in the component tree
	 */
	public void setParent(BaseControl parent) {
		if (this.parent == null && context != null) // local context is already filled -> move the local context to the
			// global
			updateContext(parent);
		parentInternal(parent);
	}

	/**
	 * copies the local context to the global context.
	 * 
	 * @param parent the parent control in the component tree
	 */
	private void updateContext(BaseControl parent) {
		// move the local context to the global
		Map<String, ContextElement> contextElements = context.getContext();
		ContextElement[] changes = context.calculateContextChange();
		List<ContextElement> contextChanges = new ArrayList<ContextElement>(Arrays.asList(changes));
		context = parent.getContext();
		for (Iterator<ContextElement> iter = contextElements.values().iterator(); iter.hasNext();) {
			// add all context elements except of the one from the change list, these are ordered
			ContextElement ce = iter.next();
			if (!contextChanges.contains(ce)) {
				context.add(ce);
			}
		}
		for (Iterator<ContextElement> iter = contextChanges.iterator(); iter.hasNext();) {
			// add context elements from the change list
			ContextElement ce = iter.next();
			context.add(ce);
		}

	}

	/**
	 * @param newParent the new parent
	 */
	protected void parentInternal(BaseControl newParent) {
		// remove old father child relationship
		if (parent != null && parent.children != null) {
			parent.children.remove(this);
		}
		if (newParent != null) {
			if (newParent.children == null) {
				newParent.children = new ArrayList<BaseControl>(2);
			}
			if (newParent.children.contains(this)) {
				throw new WGTException("Trying to add twice: " + this.getId());
			}
			newParent.children.add(this);
		}
		parent = newParent;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#getPage()
	 * 
	 * @return the page
	 */
	public Page getPage() {
		if (page == null) {
			BaseControl sb = this;
			while (!(sb instanceof Page)) {
				if (sb == null) {
					throw new WGTException("Can't find Page for id " + getId());
				}
				sb = sb.getParent();
			}
			page = (Page)sb;
		}
		return page;
	}

	/**
	 * Can be overwritten optionally.
	 * <p>
	 * This is useful if we write a start tag, then the children write themselves and later we write the end tag.
	 * </p>
	 * 
	 * <pre>
	 * protected void startHTML(PrintWriter out) {
	 * 	out.println(&quot;&lt;div&gt;&quot;);
	 * }
	 * 
	 * protected void endHTML(PrintWriter out) {
	 * 	out.println(&quot;&lt;/div&gt;&quot;);
	 * }
	 * </pre>
	 * 
	 * @param out the PrintWriter to write to
	 */
	protected void startHTML(PrintWriter out) {
		// default empty
	}

	/**
	 * A event from client side is being dispatched. Every Component has this method, regardless if it is registering an event at
	 * all. This makes the framework more simple, the components interested in the callback must implement this method.
	 * 
	 * @param event event to be dispatch
	 */
	public void dispatch(ClientEvent event) {
		// default empty
	}

	/**
	 * travels through the tree and visit each node until the visitor returns false, search algorithm is a variation of Breath
	 * first search, the surrounding component is visited first then the children
	 * 
	 * @param guest
	 * @return true if no visit returned false.
	 */
	public boolean travelBFS(Visitor guest) {
		if (guest.visit(this) && children != null) {
			// travel also to the children
			for (int i = 0; i < children.size(); i++) {
				if (!(children.get(i)).travelBFS(guest)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * travels through the tree and visit each node until the visitor returns false, search algorithm is Deep first search: first
	 * the children are visited, then the surround component.
	 * 
	 * @param guest
	 * @return true if no visit returned false.
	 */
	public boolean travelDFS(Visitor guest) {
		if (children != null) {
			// travel also to the children
			for (int i = 0; i < children.size(); i++) {
				if (!(children.get(i)).travelDFS(guest)) {
					return false;
				}
			}
		}
		return guest.visit(this);
	}

	/**
	 * call back for traveling the component tree visitor pattern.
	 * 
	 * @author arno
	 */
	public static interface Visitor {
		/**
		 * visiting this node
		 * 
		 * @param host the node visited
		 * @return if the process should be continued.
		 */
		boolean visit(BaseControl host);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.IBaseControl#setVisible(boolean)
	 */
	public void setVisible(boolean vis) {
		getContext().makeVisible(getId(), vis);
		processSetVisible(vis);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.IBaseControl#isVisible()
	 */
	public boolean isVisible() { // visibility is true unless set to false
		return getContext().getValueAsBool(getId() + IContext.DOT_VIS, true);
	}

	public List<BaseControl> getChildren() {
		if (children == null) {
			children = new ArrayList<BaseControl>(1);
		}
		return children;
	}

	/**
	 * get Style for this control. create a new on, if required. use hasStyle() to check for style
	 * 
	 * @return
	 */
	public Style getStyle() {
		if (style == null) {
			// define a new Style as style-attribute (inline) element of the
			// current control
			style = new Style(new InlineSelector());
			registerStyleChangeListener();
		}
		return style;
	}

	/**
	 * use this method to get the controls Style as String
	 * 
	 * @return
	 */
	public String getStyleAsString() {
		if (hasStyle()) {
			return style.getOutput();
		}
		else {
			return null;
		}
	}

	/**
	 * use this method to check if a style is set. use getStyle() to retrieve a style or create a new instance
	 * 
	 * @return
	 */
	public boolean hasStyle() {
		if (style == null)
			return false;
		else
			return true;
	}

	/**
	 * set new style
	 * 
	 * @param style
	 */
	@Deprecated
	public void setStyle(Style style) {
		if (this.style != null) {
			style.unregisterStyleChangedListener(getId());
		}
		this.style = style;
		if (style != null) {
			registerStyleChangeListener();
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#getCssClass()
	 */
	public String getCssClass() {
		return extractCssClassString();
	}

	private void manageCssClasses(String cssClass, boolean isDefaultStyle) {
		String[] splitStyle = cssClass.split(" ");
		if (cssClassList == null) {
			cssClassList = new ArrayList<String>();
		}
		for (int i = 0; i < splitStyle.length; i++) {
			if (!cssClassList.contains(splitStyle[i])) {
				if (isDefaultStyle && useDefaultCssClass) {
					cssClassList.add(0, splitStyle[i]);
				}
				else if (!isDefaultStyle) {
					cssClassList.add(splitStyle[i]);
				}
			}
		}
		if (isDrawn()) {
			getContext().setAttribute(getId(), "class", getCssClass());
		}
	}

	private String extractCssClassString() {
		String cssClasses = "";
		if (cssClassList == null) {
			cssClassList = new ArrayList<String>();
		}
		for (Iterator<String> iterator = cssClassList.iterator(); iterator.hasNext();) {
			String css = iterator.next();
			cssClasses += " " + css;
		}
		return cssClasses;
	}

	public void addCssClass(String cssClass) {
		manageCssClasses(cssClass, false);
	}

	public void setDefaultCssClass(String baseCssClass) {
		manageCssClasses(baseCssClass, true);
	}

	/**
	 * 
	 */
	public void removeCssClass(String cssClass) {
		String[] splitStyle = cssClass.split(" ");
		if (cssClassList == null) {
			cssClassList = new ArrayList<String>();
		}
		for (int i = 0; i < splitStyle.length; i++) {
			if (cssClassList.contains(splitStyle[i])) {
				cssClassList.remove(splitStyle[i]);
			}
		}
		if (isDrawn()) {
			getContext().setAttribute(getId(), "class", getCssClass());
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#setCssClass(java.lang.String)
	 * @deprecated
	 */
	@Deprecated
	public void setCssClass(String cssClass) {
		this.useDefaultCssClass = false;
		if (cssClassList != null && cssClassList.size() > 0) {
			cssClassList.clear();
		}
		manageCssClasses(cssClass, false);

		if (isDrawn()) {
			getContext().setAttribute(getId(), "class", cssClass);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.IBaseControl#getTooltip()
	 */
	public String getTooltip() {
		return getContext().getValue(getId() + ".title" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.IBaseControl#setTooltip(java.lang.String)
	 */
	public void setTooltip(String tooltip) {
//		if( StringUtils.isEmpty(tooltip))
		setAttribute("title", tooltip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.IBaseControl#setTooltipKey(java.lang.String)
	 */
	public void setTooltipKey(String tooltipKey) {
		setTooltip( TextService.getString(tooltipKey) ) ;
	}

	/**
	 * outputs the standard attributes for the HTML tags to be used by the genertaeHTML method.
	 * 
	 * @param out the PrintWriter to write to
	 */
	protected void stdParameter(PrintWriter out) {
		out.print(JSUtil.atId(getId()));
		IContext ctx = getContext();

		// style information can come from three sources
		// the visibility, style in context and style in the component
		// StringBuffer theStyle = new StringBuffer(StringUtils.trimToEmpty(getStyle()));
		boolean vis = ctx.processBool(getId() + IContext.DOT_VIS, true);
		String visString = "";
		if (!vis) {
			visString = "display: none;";
			// this.getStyle().addDisplay(Style.DISPLAY_NONE);
		}
		out.print(JSUtil.atNotEmpty("style", ctx.processValue(getId() + IContext.DOT_STYLE) + ";" + visString));

		// eat the setAttribute .class
		String classAt = ctx.processValue(getId() + ".class");
		if (StringUtils.isEmpty(classAt)) {
			out.print(JSUtil.at("class", getCssClass(), ""));
		}
		else {
			out.print(JSUtil.atNotEmpty("class", classAt));
		}
		writeAttributes(out);
	}

	/**
	 * outputs the standard attributes for the HTML tags to be used by the genertaeHTML method.
	 * 
	 * @param element the current apache ECS element
	 */
	protected void stdParameter(ConcreteElement element) {
		element.setID(getId());
		IContext ctx = getContext();

		// style information can come from three sources
		// the visibility, style in context and style in the component
		// StringBuffer theStyle = new StringBuffer(StringUtils.trimToEmpty(getStyle()));
		boolean vis = ctx.processBool(getId() + IContext.DOT_VIS, true);
		String visString = "";
		if (!vis) {
			visString = "display: none;";
			// this.getStyle().addDisplay(Style.DISPLAY_NONE);
		}

		element.setStyle(ctx.processValue(getId() + IContext.DOT_STYLE) + ";" + visString);
		// eat the setAttribute .class
		String classAt = ctx.processValue(getId() + ".class");
		if (StringUtils.isEmpty(classAt)) {
			if (StringUtils.isNotEmpty(getCssClass()))
				element.setClass(getCssClass());
		}
		else {
			element.setClass(classAt);
		}
		writeAttributes(element);
	}
	
	/**
	 * outputs the standard attributes for the HTML tags to be used by the genertaeHTML method.
	 * 
	 * @param element the current apache ECS element
	 */
	protected void stdParameter(HTMLTag element) {
		element.setId(getId());
		IContext ctx = getContext();

		// style information can come from three sources
		// the visibility, style in context and style in the component
		// StringBuffer theStyle = new StringBuffer(StringUtils.trimToEmpty(getStyle()));
		boolean vis = ctx.processBool(getId() + IContext.DOT_VIS, true);
		String visString = "";
		if (!vis) {
			visString = "display: none;";
			// this.getStyle().addDisplay(Style.DISPLAY_NONE);
		}

		element.setStyle(ctx.processValue(getId() + IContext.DOT_STYLE) + ";" + visString);
		// eat the setAttribute .class
		String classAt = ctx.processValue(getId() + ".class");
		if (StringUtils.isEmpty(classAt)) {
			if (StringUtils.isNotEmpty(getCssClass()))
				element.setCssClass(getCssClass());
		}
		else {
			element.setCssClass(classAt);
		}
		writeAttributes(element);
	}

	/**
	 * draw itself to the out PrintWriter.
	 * 
	 * @param out the PrintWriter to write to
	 */
	protected void draw(PrintWriter out) {
		startHTML(out);
		getLayoutManager().draw(getChildren(), out);
		endHTML(out);

		// if a tooltip is used and the Tooltip object is set, this method registers
		// the jquery event in the browser and set the necessary jquery options as defined in the Tooltip Object
		if (tooltipAdvanced != null) {
			getPage().addWgtJS("jquery/jquery.tooltip.js");
			String js = "jQuery(" + JSUtil.jQuery(getId()) + ").tooltip(" + tooltipAdvanced.getJQueryParameter() + ");";
			getContext().sendJavaScript(getId() + ".tooltip", js);
		}
	}

	/**
	 * 
	 */
	private ILayout getLayoutManager() {
		if (layoutManger == null) {
			layoutManger = new ILayout() {
				private static final long serialVersionUID = 1L;

				public void draw(List<BaseControl> controls, PrintWriter out) {
					for (Iterator<BaseControl> iter = controls.iterator(); iter.hasNext();) {
						BaseControl object = iter.next();
						object.drawInternal(out);
					}
				}
			};
		}
		return layoutManger;

	}

	/**
	 * 
	 * @param out the PrintWriter to write to
	 */
	protected final void drawInternal(PrintWriter out) {
		// only render if the control has read permission
		if (hasReadPermission())
			draw(out);
		else
			out.print("&nbsp;");
		// set flag that the control is drawn
		isDrawn = true;
	}

	/**
	 * redraws itself and all its subcomponents this is done by reevaluating the markerString and sending ti as outerhtml this
	 * works only correct on components, which have exactly one node in html. override if your components doesn't comply.
	 * 
	 * @param control the control to append as child
	 */
	public void append(final IBaseControl control) {

		// mark the position for the HTML in the context
		getContext().appendHtml(getId(), control.getId(), "");

		// register listener for later generation of the output
		getPage().registerListener(ServerEvent.EVENT_POSTDISPATCH, new IServerEventListener() {
			private static final long serialVersionUID = 1L;

			public void handle(ServerEvent event) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter out = new PrintWriter(stringWriter);

				((BaseControl)control).drawInternal(out);
				getContext().appendHtml(getId(), control.getId(), stringWriter.toString());

				// remove the listener from the page after processing it
				getPage().removeListener(ServerEvent.EVENT_POSTDISPATCH, this);
			}
		});
	}

	/**
	 * redraws itself and all its subcomponents this is done by reevaluating the markerString and sending it as outer HTML this
	 * works only correct on components, which have exactly one node in HTML. override if your components doesn't comply.
	 */
	public void redraw() {
		// mark the position for the HTML in the context
		getContext().outerHtml(getId(), "");

		// register listener for later generation of the output
		getPage().registerListener(ServerEvent.EVENT_POSTDISPATCH, new IServerEventListener() {
			private static final long serialVersionUID = 1L;

			public void handle(ServerEvent event) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter out = new PrintWriter(stringWriter);

				BaseControl.this.drawInternal(out);
				getContext().outerHtml(getId(), stringWriter.toString());

				// remove the listener from the page after processing it
				getPage().removeListener(ServerEvent.EVENT_POSTDISPATCH, this);
			}
		});
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#getId()
	 * @return the unique HTML id
	 */
	public String getId() {
		if (id == null) {
			// get the first two letter of the class name to more readability
			int lp = getClass().getName().lastIndexOf('.');
			String initials = getClass().getName().substring(lp + 1, lp + 3);
			// hope the class has 2 character haha
			id = initials + DWRController.getInstance().generateGuid();
		}
		return id;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#addContextMenu(org.webguitoolkit.ui.controls.contextmenu.ContextMenu)
	 * @param contextMenu the context menu object to add
	 */
	public void addContextMenu(IContextMenu contextMenu) {
		add(contextMenu);
	}

	/**
	 * adds a child to this component. This is protected and any subclasses which actually allows childs, will have their method
	 * to add the childs. We are not using the composite pattern to not clutter the object hierarchie with a feature.
	 * 
	 * After adding a component needs to be initialized, this is done automatically in most cases.
	 * 
	 * @param child child to add
	 */
	protected void add(IBaseControl child) {
		// if( isDrawn() )
		// redraw();

		// set the hierarchy
		((BaseControl)child).setParent(this);
		// we want to receive events:
		DWRController.getInstance().registerForEvents(((BaseControl)child).getId(), (BaseControl)child);
		// the component is ready for init. Init is not allowed to access any subcomponents (as
		// they are not there yet.
		((BaseControl)child).init();
	}

	/**
	 * removes this component from the tree of components. It also remove all subcomponents. After this operation all handles to
	 * this component should be released (from the framework) and the objects are subject to GC.
	 * 
	 * @param child
	 */
	public void remove() {
		removeAllChildren();
		removeInternal();
	}

	/**
	 * remove of the control itself
	 */
	protected void removeInternal() {

		// if a tooltip is used and the Tooltip object is set, this method removes all
		// events which are bound to the element which is to be removed
		// cleans up the web page
		if (tooltipAdvanced != null) {
			String js = "jQuery(" + JSUtil.jQuery(getId()) + ").unbind();";
			getContext().sendJavaScript(getId() + ".tooltip", js);
		}

		getContext().removeContextElement(getId());

		// setParent takes care of the children array and the form elements of the compound.
		setParent(null);

		// stop event registration
		DWRController.getInstance().deregisterForEvents(getId(), this);
	}

	/**
	 * access to the context, avoid to fail with null pointer exception when accessing the context when the control is not linked
	 * to the component tree.
	 * 
	 * @return the context of the control, if it has a parent return the context of the parent (page)
	 */
	protected IContext getContext() {
		if (context == null) {
			if (!hasReadPermission())
				context = new VoidContext();
			else if (getParent() == null)
				context = new Context(); // locale context -> add to global context when parent is set
			else
				context = getParent().getContext();
		}
		return context;
	}

	/**
	 * function to check if the control is rendered
	 * 
	 * @return if the control is drawn or in creation phase
	 */
	public boolean isDrawn() {
		return isDrawn;
	}

	// protected void initDroppable(){
	// String js = JSUtil.jQuery( getId() )+".droppable(" +
	// "{ " +
	// "accept: \"tr\", "+
	// "drop: function(ev, ui) {" +
	// "eventParam('"+getId()+"', new Array( ui.draggable.element.id, '"+getId()+"') );" +
	// JSUtil.jsFireEvent(getId(), 0 ) +
	// "}" +
	// "});";
	// getBody().getContext().sendJavaScript( getId(), js );
	// }
	// protected void initDraggable(){
	// String js = JSUtil.jQuery( getId() ) + ".draggable({helper: 'clone'});";
	// getBody().getContext().sendJavaScript( getId(), js );
	// }

	/**
	 * checks if the control has execute permission
	 * 
	 * @return true if EXECUTE_PERMISSION or no entry found
	 */
	protected boolean hasExecutePermission() {
		if (PermissionManager.isInitialized()) {
			return PermissionManager.getPermissionBroker().hasExecutePermission(getId());
		}
		return true;
	}

	/**
	 * checks if the control has read permission
	 * 
	 * @return true if READ_PERMISSION, EXECUTE_PERMISSION or no entry found
	 */
	protected boolean hasReadPermission() {
		if (PermissionManager.isInitialized()) {
			return PermissionManager.getPermissionBroker().hasReadPermission(getId());
		}
		return true;
	}

	/**
	 * register a StyleChangedListener to the Style object.
	 */
	protected void registerStyleChangeListener() {
		style.registerStyleChangedListener(new StyleChangedListener());
	}

	/**
	 * the StyleChangeListener sends changes in the style to the browser.
	 */
	class StyleChangedListener implements IStyleChangeListener {
		private static final long serialVersionUID = 1L;

		/**
		 * handles style changes, sends the style to the browser
		 * 
		 * @param eventType type of the event
		 * @param attribute the style attribute
		 */
		public void handle(int eventType, String attribute) {
			if (getParent() != null)
				getContext().add(getId() + Context.DOT_STYLE, getStyleAsString(), Context.TYPE_ATT, Context.STATUS_NOT_EDITABLE);
		}

		/**
		 * @return the current control's id
		 */
		public String getListenerId() {
			return getId();
		}
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#setTooltip(org.webguitoolkit.ui.controls.util.Tooltip)
	 */
	public void setTooltip(Tooltip tooltip) {
		this.tooltipAdvanced = tooltip;
		setTooltip( tooltip.getText() );
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#getTooltipAdvanced()
	 */
	public Tooltip getTooltipAdvanced() {
		return tooltipAdvanced;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#setTooltipAdvanced(org.webguitoolkit.ui.controls.util.Tooltip)
	 */
	public void setTooltipAdvanced(Tooltip tooltipAdvanced) {
		this.tooltipAdvanced = tooltipAdvanced;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.IBaseControl#toggleVisible()
	 */
	public void toggleVisible() {
		this.setVisible(!this.isVisible());
	}

	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayoutElement#getLayoutPosition()
	 */
	public ILayoutData getLayoutData() {
		return layoutData;
	}

	/**
	 * @see org.webguitoolkit.ui.controls.layout.ILayoutElement#setLayoutPosition(org.webguitoolkit.ui.controls.layout.ILayoutData)
	 */
	public void setLayoutData(ILayoutData data) {
		this.layoutData = data;
	}

	/**
	 * @param manager
	 */
	protected void setLayout(ILayout layout) {
		this.layoutManger = layout;
	}

	protected void setAttribute(String attributeName, String attributeValue) {
		attributes.add(attributeName);
		getContext().add(getId() + "." + attributeName, attributeValue, IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
	}

	protected String getAttribute(String attributeName) {
		ContextElement ce = getContext().getContext().get(getId() + "." + attributeName);
		if (ce != null) {
			return ce.getValue();
		}
		else {
			return null;
		}
	}

	private void writeAttributes(PrintWriter out) {
		IContext ctx = getContext();
		for (Iterator<String> iter = attributes.iterator(); iter.hasNext();) {
			String attrib = iter.next();
			String value = ctx.processValue(getId() + "." + attrib);
			out.print(JSUtil.atNotEmpty(attrib, value));
		}
	}

	private void writeAttributes(ConcreteElement el) {
		IContext ctx = getContext();
		for (Iterator<String> iter = attributes.iterator(); iter.hasNext();) {
			String attrib = iter.next();
			String value = ctx.processValue(getId() + "." + attrib);
			if( value != null )
				el.addAttribute(attrib, value);
			else
				el.removeAttribute(attrib);
		}
	}
	
	private void writeAttributes(HTMLTag el) {
		IContext ctx = getContext();
		for (Iterator iter = attributes.iterator(); iter.hasNext();) {
			String attrib = (String)iter.next();
			String value = ctx.processValue(getId() + "." + attrib);
			if( value != null )
				el.addAttribute(HTMLUtil.getHTMLAttribute(attrib), value);
			else
				el.removeAttribute(HTMLUtil.getHTMLAttribute(attrib));
		}
	}
	
	public void setName( String name ){
		setAttribute(ELEMENT_ATTRIBUTE_NAME, name);
	}

	
	public String getName() {
		return getAttribute(ELEMENT_ATTRIBUTE_NAME);
	}
}
