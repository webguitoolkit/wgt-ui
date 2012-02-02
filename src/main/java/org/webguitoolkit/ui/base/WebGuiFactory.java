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
package org.webguitoolkit.ui.base;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.IComposite;
import org.webguitoolkit.ui.controls.chart.Chart;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.HtmlElement;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.container.SimpleHtmlElement;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenu;
import org.webguitoolkit.ui.controls.contextmenu.ContextMenuItem;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenuItem;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenuListener;
import org.webguitoolkit.ui.controls.dialog.IWizard;
import org.webguitoolkit.ui.controls.dialog.Wizard;
import org.webguitoolkit.ui.controls.dialog.WizardView;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.ButtonBar;
import org.webguitoolkit.ui.controls.form.CheckBox;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.IButtonBar;
import org.webguitoolkit.ui.controls.form.IButtonBarListener;
import org.webguitoolkit.ui.controls.form.ICheckBox;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.IMultiselect;
import org.webguitoolkit.ui.controls.form.IRadio;
import org.webguitoolkit.ui.controls.form.IRichTextArea;
import org.webguitoolkit.ui.controls.form.ISelect;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.form.ITextSuggest;
import org.webguitoolkit.ui.controls.form.ITextarea;
import org.webguitoolkit.ui.controls.form.Label;
import org.webguitoolkit.ui.controls.form.Multiselect;
import org.webguitoolkit.ui.controls.form.Radio;
import org.webguitoolkit.ui.controls.form.RichTextArea;
import org.webguitoolkit.ui.controls.form.Select;
import org.webguitoolkit.ui.controls.form.Text;
import org.webguitoolkit.ui.controls.form.TextSuggest;
import org.webguitoolkit.ui.controls.form.Textarea;
import org.webguitoolkit.ui.controls.form.button.GlossButton;
import org.webguitoolkit.ui.controls.form.button.IconButton;
import org.webguitoolkit.ui.controls.form.button.LinkButton;
import org.webguitoolkit.ui.controls.form.button.StandardButton;
import org.webguitoolkit.ui.controls.form.fileupload.FileUpload;
import org.webguitoolkit.ui.controls.form.fileupload.IFileHandler;
import org.webguitoolkit.ui.controls.form.fileupload.IFileUpload;
import org.webguitoolkit.ui.controls.form.popupselect.IPopupSelect;
import org.webguitoolkit.ui.controls.form.popupselect.PopupSelect;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.layout.TableLayout;
import org.webguitoolkit.ui.controls.list.IList;
import org.webguitoolkit.ui.controls.list.ISortableList;
import org.webguitoolkit.ui.controls.list.SimpleList;
import org.webguitoolkit.ui.controls.list.SortableList;
import org.webguitoolkit.ui.controls.menu.IMenu;
import org.webguitoolkit.ui.controls.menu.IMenuBar;
import org.webguitoolkit.ui.controls.menu.IMenuItem;
import org.webguitoolkit.ui.controls.menu.IMenuItemSeparator;
import org.webguitoolkit.ui.controls.menu.Menu;
import org.webguitoolkit.ui.controls.menu.MenuBar;
import org.webguitoolkit.ui.controls.menu.MenuItem;
import org.webguitoolkit.ui.controls.menu.MenuItemSeperator;
import org.webguitoolkit.ui.controls.tab.ButtonTabStrip;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.tab.Tab;
import org.webguitoolkit.ui.controls.table.ISimpleTable;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.ITableFilter;
import org.webguitoolkit.ui.controls.table.SimpleTable;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.table.TableFilter;
import org.webguitoolkit.ui.controls.tree.ITree;
import org.webguitoolkit.ui.controls.tree.Tree;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.Tooltip;

/**
 * <p>
 * Factory for creation of controls.
 * </p>
 * <p>
 * first parameter is either the parent or the object id if you want to set the id manually.
 * </p>
 * <p>
 * You can also create the controls by calling there constructor but you have to call a lot of setters and you have to take care
 * of adding it to the parent control.
 * </p>
 * 
 * <pre>
 *  // create a layout on a page
 *  ITableLayout layout factory.createTableLayout( page );
 *  // create a text field on the layout
 * 	factory.createText( layout, &quot;name&quot; );
 * </pre>
 */
public class WebGuiFactory implements Serializable {

	public static final String NO_LOCATOR = null;
	public static final String NO_TEXT = null;
	public static final String NO_IMAGE = null;

	// singleton, there is only one instance of this class.
	protected static WebGuiFactory instance;

	/**
	 * singleton pattern, use this method to get a instance of the factory
	 * 
	 * @return a instance of the factory
	 */
	public static WebGuiFactory getInstance() {
		if (instance == null) {
			// sounds strange to make an instance of an abstract class.
			instance = new WebGuiFactory();
		}
		return instance;
	}

	/**
	 * The instance can be changed
	 * 
	 * @param instance new instance
	 */
	public static void setInstance(WebGuiFactory instance) {
		WebGuiFactory.instance = instance;
	}

	/**
	 * helper function to assign a label to a form control. The label is displayed with the style "wgtLabelFor" and is used as
	 * validation hint when the form control has validation errors.
	 * 
	 * @param label the describing label
	 * @param aControl the form control
	 */
	public void linkLabelToControl(ILabel label, IFormControl aControl) {
		aControl.setDescribingLabel(label);
	}

	/**
	 * Create a DataBag for holding the UI-value before they are transfered to the Backend object (which is the delegate Object)
	 * This method is called from the WGT whereever a Databag instance is missing.
	 * 
	 * @param delegate The backend objct to which the data will be saved once transfered from the UI components,then validated and
	 *            converted into appropriate DataFormats
	 * @return The DataBag with reference to the delegate
	 */
	public IDataBag createDataBag(Object delegate) {
		return new DataBag(delegate);
	}

	/**
	 * Creates a standard button
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param listener the ActionListener to handle click events
	 * @param id the unique HTML id
	 * @return a standard button
	 */
	public IButton createButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener, String id) {
		return newStandardButton(id, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * Creates a standard button
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param listener the ActionListener to handle click events
	 * @return a standard button
	 */
	public IButton createButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newStandardButton(null, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * Creates a standard button for table
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param listener the ActionListener to handle click events
	 * @return a standard button
	 */
	public IButton createButton(ITable parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newButton(parent, imageSrc, titleKey, tooltipKey, listener);
	}

	public IButton createButton(ITable parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener, String id) {
		return newButton(id, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * Creates a link
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param listener the ActionListener to handle click events
	 * @param id the unique HTML id
	 * @return a standard button
	 */
	public IButton createLinkButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener,
			String id) {
		return newLinkButton(id, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * Creates a link
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param listener the ActionListener to handle click events
	 * @return a standard button
	 */
	public IButton createLinkButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newLinkButton(null, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * Creates a link to a URL
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param linkURL the URL that has to be called
	 * @param target the target frame
	 * @param id the unique HTML id
	 * @return a standard button
	 */
	public IButton createLink(IComposite parent, String imageSrc, String titleKey, String tooltipKey, String linkURL, String target,
			String id) {
		return newLinkButton(id, parent, imageSrc, titleKey, tooltipKey, linkURL, target);
	}

	/**
	 * Creates a link to a URL
	 * 
	 * @param parent the composite to be placed in
	 * @param imageSrc the image URL
	 * @param titleKey the resource bundle key of the label
	 * @param tooltipKey the resource bundle key of the tooltip
	 * @param linkURL the URL that has to be called
	 * @param target the target frame
	 * @return a standard button
	 */
	public IButton createLink(IComposite parent, String imageSrc, String titleKey, String tooltipKey, String linkURL, String target) {
		return newLinkButton(null, parent, imageSrc, titleKey, tooltipKey, linkURL, target);
	}

	/**
	 * Creates a ButtonBar
	 * 
	 * @param parent the composite to be placed in
	 * @param buttons comma separated string like: "edit,delete,cancel"
	 * @param listener the ButtonBarListener
	 * @param id the unique HTML id
	 * @return the ButtonBar
	 */
	public IButtonBar createButtonBar(IComposite parent, String buttons, IButtonBarListener listener, String id) {
		return newButtonBar(id, parent, buttons, listener);
	}

	/**
	 * Creates a ButtonBar
	 * 
	 * @param parent the composite to be placed in
	 * @param buttons array of stings defined in IButtonBar
	 * @param displayMode the display mode (image, image with text, text)
	 * @param listener the ButtonBarListener
	 * @return the ButtonBar
	 */
	public IButtonBar createButtonBar(IComposite parent, String[] buttons, int displayMode, IButtonBarListener listener) {
		return newButtonBar(parent, buttons, displayMode, listener);
	}

	/**
	 * Creates a ButtonBar
	 * 
	 * @param parent
	 * @param buttons
	 * @param displayMode
	 * @param listener
	 * @param id
	 * @return
	 */
	public IButtonBar createButtonBar(IComposite parent, String[] buttons, int displayMode, IButtonBarListener listener, String id) {
		return newButtonBar(id, parent, buttons, displayMode, listener);
	}

	/**
	 * Creates a ButtonBar
	 * 
	 * @param parent the composite to be placed in
	 * @param buttons comma separated string like: "edit,delete,cancel"
	 * @param listener the ButtonBarListener
	 * @return the ButtonBar
	 */
	public IButtonBar createButtonBar(IComposite parent, String buttons, IButtonBarListener listener) {
		return newButtonBar(null, parent, buttons, listener);
	}

	public ICanvas createCanvas(IComposite parent, String id) {
		return newCanvas(id, parent);
	}

	public ICanvas createCanvas(IComposite parent) {
		return newCanvas(null, parent);
	}

	public Chart createChart(IComposite parent, String id) {
		return newChart(id, parent);
	}

	public Chart createChart(IComposite parent) {
		return newChart(null, parent);
	}

	public ICheckBox createCheckBoxWithLabel(IComposite parent, String labelKey, String id) {
		return newCheckBoxWithLabel(id, parent, labelKey);
	}

	public ICheckBox createCheckBoxWithLabel(IComposite parent, String labelKey) {
		return newCheckBoxWithLabel(null, parent, labelKey);
	}

	public ICheckBox createCheckBox(IComposite parent, String property, String id) {
		return newCheckBox(id, parent, property);
	}

	public ICheckBox createCheckBox(IComposite parent, String property) {
		return newCheckBox(null, parent, property);
	}

	public ICompound createCompound(IComposite parent, String id) {
		return newCompound(id, parent);
	}

	public ICompound createCompound(IComposite parent) {
		return newCompound(null, parent);
	}

	public IContextMenu createContextMenu(IBaseControl parent, String id) {
		return newContextMenu(id, (BaseControl)parent);
	}

	public IContextMenu createContextMenu(IBaseControl parent) {
		return createContextMenu(parent, null);
	}

	public IContextMenuItem createContextMenuItem(IContextMenu parent, String labelKey, IContextMenuListener listener, String id) {
		return newContextMenuItem(id, (ContextMenu)parent, labelKey, listener);
	}

	public IContextMenuItem createContextMenuItem(IContextMenu parent, String labelKey, IContextMenuListener listener) {
		return newContextMenuItem(null, (ContextMenu)parent, labelKey, listener);
	}

	public IFileUpload createFileUpload(IComposite parent, String property, IFileHandler handler, IActionListener actionlistener, String id) {
		return newFileUpload(id, parent, property, handler, actionlistener);
	}

	public IFileUpload createFileUpload(IComposite parent, String property, IFileHandler handler, IActionListener actionlistener) {
		return newFileUpload(null, parent, property, handler, actionlistener);
	}

	public IHtmlElement createHtmlElement(IComposite parent, String tagName, String id) {
		IHtmlElement html;
		if (tagName != null && ("br".equals(tagName.toLowerCase()) || "hr".equals(tagName.toLowerCase()))) {
			html = new SimpleHtmlElement(id);
			parent.add(html);
		}
		else {
			html = new HtmlElement(id);
			parent.add(html);
		}
		html.setTagName(tagName);
		return html;
	}

	public IHtmlElement createHtmlElement(IComposite parent, String tagName) {
		return createHtmlElement(parent, tagName, null);
	}

	/**
	 * @deprecated use createHtmlElement(IComposite parent, String tagName )
	 * @param parent
	 * @return
	 */
	@Deprecated
	public IHtmlElement createHtmlElement(IComposite parent) {
		return newHtmlElement(null, parent);
	}

	public ILabel createLabel(IComposite parent, String labelKey, String id) {
		return newLabel(id, parent, labelKey);
	}

	public ILabel createLabel(IComposite parent, String labelKey) {
		return newLabel(null, parent, labelKey);
	}

	/**
	 * this creates a label-control and binds it to the formcontrol. Actually it is recommended to use the Factory to create the
	 * controls directly, so here is an exception from the rule. add a component and create a Label-Component for it. The text of
	 * the label component will be the getLabel parameter. Label component and comp get linked together via Label.labelFor() if
	 * possible (should always be the case).
	 * 
	 * @param labelKey -- translatable text for the user.
	 * @param aFormControl -- for which component is this label?
	 */
	public ILabel createLabel(IComposite parent, String labelKey, IFormControl aFormControl, String id) {
		return newLabel(id, parent, labelKey, (FormControl)aFormControl);
	}

	public ILabel createLabel(IComposite parent, String labelKey, IFormControl aFormControl) {
		return newLabel(null, parent, labelKey, (FormControl)aFormControl);
	}

	public IMenuBar createMenuBar(IComposite parent, String id) {
		return newMenuBar(id, parent);
	}

	public IMenuBar createMenuBar(IComposite parent) {
		return newMenuBar(null, parent);
	}

	public IMenu createMenu(IMenuBar parent, String labelKey, String id) {
		return newMenu(id, (MenuBar)parent, labelKey);
	}

	public IMenu createMenu(IMenuBar parent, String labelKey) {
		return newMenu(null, (MenuBar)parent, labelKey);
	}

	public IMenu createMenu(IMenu parent, String labelKey, String id) {
		return newMenu(id, (Menu)parent, labelKey);
	}

	public IMenu createMenu(IMenu parent, String labelKey) {
		return newMenu(null, (Menu)parent, labelKey);
	}

	public IMenuItem createMenuItem(IMenu parent, String label, String icon, IActionListener listener, String id) {
		return newMenuItem(id, (Menu)parent, label, icon, listener);
	}

	public IMenuItem createMenuItem(IMenu parent, String label, String icon, IActionListener listener) {
		return newMenuItem(null, (Menu)parent, label, icon, listener);
	}

	public IMenuItem createMenuItem(IMenu parent, String label, IActionListener listener) {
		return newMenuItem(null, (Menu)parent, label, null, listener);
	}

	public IMenuItem createMenuItem(IMenuBar parent, String label, IActionListener listener) {
		return createMenuItem(null, parent, label, null, listener);
	}

	public IMenuItem createMenuItem(IMenuBar parent, String label, String icon, IActionListener listener) {
		return createMenuItem(null, parent, label, icon, listener);
	}

	public IMenuItem createMenuItem(IMenuBar parent, String label, String icon, IActionListener listener, String id) {
		return createMenuItem(id, parent, label, icon, listener);
	}

	public MenuItem createMenuItem(String id, IMenuBar parent, String label, String icon, IActionListener listener) {
		MenuItem menuItem = new MenuItem(id);
		parent.addMenuItem(menuItem);
		menuItem.setLabelKey(label);
		menuItem.setIconSrc(icon);
		menuItem.setActionListener(listener);
		return menuItem;
	}

	public IMenuItemSeparator createMenuItemSeperator(IMenu parent, String id) {
		return newMenuItemSeperator(id, (Menu)parent);
	}

	public IMenuItemSeparator createMenuItemSeperator(IMenu parent) {
		return newMenuItemSeperator(null, (Menu)parent);
	}

	public IRadio createRadio(IComposite parent, String group, String labelKey, String id) {
		return newRadio(id, parent, group, labelKey);
	}

	public IRadio createRadio(IComposite parent, String group, String labelKey) {
		return newRadio(null, parent, group, labelKey);
	}

	public ISelect createSelect(IComposite parent, String property, String id) {
		return newSelect(id, parent, property);
	}

	public ISelect createSelect(IComposite parent, String property) {
		return newSelect(null, parent, property);
	}

	/**
	 * Factory function for creating a muliselect field
	 * 
	 * @param parent parent control
	 * @param property the property
	 * @param rows rows
	 * @return new created MuliSelect
	 */
	public IMultiselect createMultiselect(IComposite parent, String property, int rows) {
		Multiselect multi = new Multiselect();
		parent.add(multi);
		multi.setProperty(property);
		multi.setRows(rows);
		return multi;
	}

	/**
	 * Factory function for creating a muliselect field with id
	 * 
	 * @param parent parent control
	 * @param property the property
	 * @param rows rows
	 * @param id multiselect field id
	 * @return new created MuliSelect
	 */
	public IMultiselect createMultiselect(IComposite parent, String property, int rows, String id) {
		Multiselect multi = new Multiselect(id);
		parent.add(multi);
		multi.setProperty(property);
		multi.setRows(rows);
		return multi;
	}

	public ITab createTab(ITabStrip parent, String labelKey, String id) {
		return newTab(id, parent, labelKey);
	}

	public ITab createTab(IWizard parent, String labelKey, String id) {
		Tab control = new Tab(id);
		if (parent instanceof Wizard)
			((Wizard)parent).addTab(control);
		else if (parent instanceof WizardView)
			((WizardView)parent).addTab(control);
		control.setLabelKey(labelKey);
		return control;

	}

	public ITab createTab(ITabStrip parent, String labelKey) {
		return newTab(null, parent, labelKey);
	}

	public ITabStrip createTabStrip(IComposite parent, String id) {
		return newTabStrip(id, parent);
	}

	public ITabStrip createTabStrip(IComposite parent) {
		return newTabStrip(null, parent);
	}

	public ITabStrip createButtonTabStrip(IComposite parent, Class<? extends Button> buttonImpl) {
		ButtonTabStrip tabStrip = new ButtonTabStrip();
		parent.add(tabStrip);
		if (buttonImpl != null)
			tabStrip.setButtonImpl(buttonImpl);
		return tabStrip;
	}

	public ITabStrip createButtonTabStrip(IComposite parent) {
		return createButtonTabStrip(parent, null);
	}

	public ISimpleTable createSimpleTable(IComposite parent, String headerKey, String id) {
		return newSimpleTable(id, parent, headerKey);
	}

	public ISimpleTable createSimpleTable(IComposite parent, String headerKey) {
		return newSimpleTable(null, parent, headerKey);
	}

	public ITable createTable(IComposite parent, String headerKey, int rows, String id) {
		return newTable(id, parent, headerKey, rows);
	}

	public ITable createTable(IComposite parent, String headerKey, int rows) {
		return newTable(null, parent, headerKey, rows);
	}

	public ITableColumn createTableColumn(ITable parent, String headerKey, String property, boolean filterOn, String id) {
		return newTableColumn(id, (Table)parent, headerKey, property, filterOn);
	}

	public ITableColumn createTableColumn(ITable parent, String headerKey, String property, boolean filterOn) {
		String id = null;
		if (parent instanceof Table) {
			// calculate the column number for the new column (=current table width because we start counting with 0)
			Table table = (Table)parent;
			id = table.createNewColumnId();
		}
		return newTableColumn(id, (Table)parent, headerKey, property, filterOn);
	}

	public ITableFilter createTableFilter(ITable parent, String id) {
		return newTableFilter(id, (Table)parent);
	}

	public ITableFilter createTableFilter(ITable parent) {
		return newTableFilter(null, (Table)parent);
	}

	public ITableLayout createTableLayout(IComposite parent, String id) {
		return newTableLayout(id, parent);
	}

	public ITableLayout createTableLayout(IComposite parent) {
		return newTableLayout(null, parent);
	}

	public IText createText(IComposite parent, String property, String id) {
		return newText(id, parent, property);
	}

	public IText createText(IComposite parent, String property) {
		return newText(null, parent, property);
	}

	public IText createText(IComposite parent, String property, ILabel currentLabel, String id) {
		return newText(id, parent, property, (Label)currentLabel);
	}

	public IText createText(IComposite parent, String property, ILabel currentLabel) {
		return newText(null, parent, property, (Label)currentLabel);
	}

	public ITextarea createTextarea(IComposite parent, String property, String id) {
		return newTextarea(id, parent, property);
	}

	public IRichTextArea createRichTextArea(IComposite parent, String property, String id) {
		return newRichTextArea(id, parent, property);
	}

	public ITextarea createTextarea(IComposite parent, String property) {
		return newTextarea(null, parent, property);
	}

	public IRichTextArea createRichTextArea(IComposite parent, String property) {
		return newRichTextArea(null, parent, property);
	}

	public ITree createTree(IComposite parent, String id) {
		Tree tree = new Tree(id);
		parent.add(tree);
		return tree;
	}

	public ITree createTree(IComposite parent) {
		return createTree(parent, null);
	}

	public IWizard createWizard(IComposite parent, String id) {
		return newWizard(id, parent);
	}

	public IWizard createWizard(IComposite parent) {
		return newWizard(null, parent);
	}

	public IPopupSelect createPopupSelect(IComposite parent, String property, String displayProperty, boolean isMultiselect,
			String[] columns, String[] columnTitles, ILabel describingLabel) {
		return newPopupSelect(parent, property, displayProperty, isMultiselect, columns, columnTitles, describingLabel);
	}

	public IPopupSelect createPopupSelect(IComposite parent, String property, String displayProperty, boolean isMultiselect,
			String[] columns, String[] columnTitles, ILabel describingLabel, String id) {
		return newPopupSelect(parent, property, displayProperty, isMultiselect, columns, columnTitles, describingLabel, id);
	}

	public IList createList(IComposite parent) {
		SimpleList result = new SimpleList();
		parent.add(result);
		return result;
	}

	public ISortableList createSortableList(IComposite parent) {
		SortableList result = new SortableList();
		parent.add(result);
		return result;
	}

	public ITextSuggest createTextSuggest(IComposite parent) {
		return createTextSuggest(parent, null, null, null);
	}

	public ITextSuggest createTextSuggest(IComposite parent, String[] data) {
		return createTextSuggest(parent, data, null, null);
	}

	public ITextSuggest createTextSuggest(IComposite parent, String property) {
		return createTextSuggest(parent, null, property, null);
	}

	public ITextSuggest createTextSuggest(IComposite parent, String[] data, String property, ILabel currentLabel) {
		return createTextSuggest(parent, data, property, currentLabel, null);
	}

	public ITextSuggest createTextSuggest(IComposite parent, String[] data, String property, ILabel currentLabel, String componentName) {
		TextSuggest suggest = null;
		if (data != null) {
			suggest = new TextSuggest(data);
		}
		else {
			suggest = new TextSuggest();
		}
		parent.add(suggest);
		suggest.setProperty(property);
		suggest.setName(componentName);
		if (currentLabel != null) {
			linkLabelToControl(currentLabel, suggest);
		}
		return suggest;
	}

	/**
	 * @deprecated use createTextSuggest without url parameter
	 */
	private ITextSuggest createTextSuggest(IComposite parent, String[] data, String property, String url, ILabel currentLabel, String id) {
		TextSuggest suggest = null;
		if (data != null)
			suggest = new TextSuggest(data);
		else
			suggest = new TextSuggest();
		// suggest = new TextSuggest();
		parent.add(suggest);
		suggest.setProperty(property);
		if (StringUtils.isNotEmpty(id)) {
			suggest.setId(id);
		}
		if (currentLabel != null) {
			linkLabelToControl(currentLabel, suggest);
		}
		return suggest;
	}

	/*******************************************************************************************
	 * DEPRICATED FACTORY
	 *******************************************************************************************/

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public Button newButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newButton(null, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public Button newButton(String id, IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		Button button = null;
		if (imageSrc != null && titleKey == null) {
			button = new StandardButton(id);
			parent.add(button);
			button.setSrc(imageSrc);
		}
		else if (imageSrc != null && titleKey != null) {
			button = new StandardButton(id);
			parent.add(button);
			button.setLabel(TextService.getString(titleKey));
			button.setSrc(imageSrc);
		}
		else {
			button = new StandardButton(id);
			parent.add(button);
			button.setLabel(TextService.getString(titleKey));
		}
		button.setTooltip(TextService.getString(tooltipKey));
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public Button newButton(ITable parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newButton(null, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	public Button newButton(String id, ITable parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		Button button = null;
		if (imageSrc != null && titleKey == null) {
			button = new StandardButton(id);
			parent.addButton(button);
			button.setSrc(imageSrc);
		}
		else if (imageSrc != null && titleKey != null) {
			button = new StandardButton(id);
			parent.addButton(button);
			button.setLabel(TextService.getString(titleKey));
			button.setSrc(imageSrc);
		}
		else {
			button = new StandardButton(id);
			parent.addButton(button);
			button.setLabel(TextService.getString(titleKey));
		}
		button.setTooltip(TextService.getString(tooltipKey));
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public StandardButton newStandardButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newStandardButton(null, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public StandardButton newStandardButton(String id, IComposite parent, String imageSrc, String titleKey, String tooltipKey,
			IActionListener listener) {
		StandardButton button = new StandardButton(id);
		parent.add(button);
		button.setLabel(TextService.getString(titleKey));
		button.setSrc(imageSrc);
		button.setTooltip(TextService.getString(tooltipKey));
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public StandardButton newStandardButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, String linkURL,
			String target) {
		return newStandardButton(null, parent, imageSrc, titleKey, tooltipKey, linkURL, target);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public StandardButton newStandardButton(String id, IComposite parent, String imageSrc, String titleKey, String tooltipKey,
			String linkURL, String target) {
		StandardButton button = new StandardButton(id);
		parent.add(button);
		button.setLabel(TextService.getString(titleKey));
		button.setSrc(imageSrc);
		button.setTooltip(TextService.getString(tooltipKey));
		button.setLinkURL(linkURL);
		button.setTarget(target);
		return button;
	}

	/**
	 * @deprecated use createIconButton
	 */
	@Deprecated
	public IconButton newIconButton(IComposite parent, String imageSrc, String tooltipKey, IActionListener listener) {
		return newIconButton(null, parent, imageSrc, tooltipKey, listener);
	}

	public IButton createIconButton(IComposite parent, String imageSrc, String tooltipKey, IActionListener listener) {
		return newIconButton(null, parent, imageSrc, tooltipKey, listener);
	}

	/**
	 * @deprecated use createIconButton
	 */
	@Deprecated
	public IconButton newIconButton(String id, IComposite parent, String imageSrc, String tooltipKey, IActionListener listener) {
		IconButton button = new IconButton(id);
		parent.add(button);
		button.setSrc(imageSrc);
		button.setTooltip(TextService.getString(tooltipKey));
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	public IButton createIconButton(String id, IComposite parent, String imageSrc, String tooltipKey, IActionListener listener) {
		return newIconButton(id, parent, imageSrc, tooltipKey, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public LinkButton newLinkButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, IActionListener listener) {
		return newLinkButton(null, parent, imageSrc, titleKey, tooltipKey, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public LinkButton newLinkButton(String id, IComposite parent, String imageSrc, String titleKey, String tooltipKey,
			IActionListener listener) {
		LinkButton button = new LinkButton(id);
		parent.add(button);
		button.setLabel(TextService.getString(titleKey));
		button.setSrc(imageSrc);
		button.setTooltip(TextService.getString(tooltipKey));
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public LinkButton newLinkButton(IComposite parent, String imageSrc, String titleKey, String tooltipKey, String linkURL, String target) {
		return newLinkButton(null, parent, imageSrc, titleKey, tooltipKey, linkURL, target);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public LinkButton newLinkButton(String id, IComposite parent, String imageSrc, String titleKey, String tooltipKey, String linkURL,
			String target) {
		LinkButton button = new LinkButton(id);
		parent.add(button);
		button.setLabel(TextService.getString(titleKey));
		button.setSrc(imageSrc);
		button.setTooltip(TextService.getString(tooltipKey));
		button.setLinkURL(linkURL);
		button.setTarget(target);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public GlossButton newGlossButton(IComposite parent, String titleKey, String tooltipKey, IActionListener listener) {
		return newGlossButton(null, parent, titleKey, tooltipKey, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public GlossButton newGlossButton(String id, IComposite parent, String titleKey, String tooltipKey, IActionListener listener) {
		GlossButton button = new GlossButton(id);
		parent.add(button);
		button.setTooltip(TextService.getString(tooltipKey));
		button.setLabel(TextService.getString(titleKey));
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public GlossButton newGlossButton(IComposite parent, String imageSrc, int width, int height, String tooltipKey, IActionListener listener) {
		return newGlossButton(null, parent, imageSrc, width, height, tooltipKey, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public GlossButton newGlossButton(String id, IComposite parent, String imageSrc, int width, int height, String tooltipKey,
			IActionListener listener) {
		GlossButton button = new GlossButton(id);
		parent.add(button);
		button.setTooltip(TextService.getString(tooltipKey));
		button.setSrc(imageSrc);
		button.getStyle().addWidth(width, "px");
		button.getStyle().addHeight(height, "px");
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public GlossButton newGlossButton(IComposite parent, String imageSrc, int imgWidth, int imgHeight, String labelKey, String tooltipKey,
			boolean displayHorizontal, IActionListener listener) {
		return newGlossButton(null, parent, imageSrc, imgWidth, imgHeight, labelKey, tooltipKey, displayHorizontal, listener);
	}

	/**
	 * @deprecated use createButton
	 */
	@Deprecated
	public GlossButton newGlossButton(String id, IComposite parent, String imageSrc, int imgWidth, int imgHeight, String labelKey,
			String tooltipKey, boolean displayHorizontal, IActionListener listener) {
		GlossButton button = new GlossButton(id);
		parent.add(button);
		button.setTooltip(TextService.getString(tooltipKey));
		button.setDisplayMode(displayHorizontal ? GlossButton.DISPLAY_MODE_HORIZONTAL : GlossButton.DISPLAY_MODE_VERTICAL);
		button.setSrc(imageSrc, imgWidth, imgHeight);
		button.setLabelKey(labelKey);
		if (listener != null)// don't want a exception cause the ActionListener can be set later
			button.setActionListener(listener);
		return button;
	}

	/**
	 * @deprecated use createButtonBar
	 */
	@Deprecated
	public ButtonBar newButtonBar(IComposite parent, String buttons, IButtonBarListener listener) {
		return newButtonBar(null, parent, buttons, listener);
	}

	/**
	 * @deprecated use createButtonBar
	 */
	@Deprecated
	public ButtonBar newButtonBar(IComposite parent, String[] buttons, IButtonBarListener listener) {
		return newButtonBar(null, parent, buttons, ButtonBar.BUTTON_DISPLAY_MODE_IMAGE, listener);
	}

	/**
	 * @deprecated use createButtonBar
	 */
	@Deprecated
	public ButtonBar newButtonBar(IComposite parent, String[] buttons, int displayMode, IButtonBarListener listener) {
		return newButtonBar(null, parent, buttons, displayMode, listener);
	}

	/**
	 * @deprecated use createButtonBar
	 */
	@Deprecated
	public ButtonBar newButtonBar(String id, IComposite parent, String buttons, IButtonBarListener listener) {
		ButtonBar buttonBar = new ButtonBar(id);
		parent.add(buttonBar);
		buttonBar.setButtons(buttons);
		buttonBar.setListener(listener);
		return buttonBar;
	}

	/**
	 * @deprecated use createButtonBar
	 */
	@Deprecated
	public ButtonBar newButtonBar(String id, IComposite parent, String[] buttons, int displayMode, IButtonBarListener listener) {
		ButtonBar buttonBar = new ButtonBar(id);
		parent.add(buttonBar);
		buttonBar.setButtonDisplayMode(displayMode);
		buttonBar.setButtons(buttons);
		buttonBar.setListener(listener);
		return buttonBar;
	}

	/**
	 * @deprecated use createCanvas
	 */
	@Deprecated
	public Canvas newCanvas(IComposite parent) {
		return newCanvas(null, parent);
	}

	/**
	 * @deprecated use createCanvas
	 */
	@Deprecated
	public Canvas newCanvas(String id, IComposite parent) {
		Canvas canvas = new Canvas(id);
		parent.add(canvas);
		return canvas;
	}

	/**
	 * @deprecated use createChart
	 */
	@Deprecated
	public Chart newChart(IComposite parent) {
		return newChart(null, parent);
	}

	/**
	 * @deprecated use createChart
	 */
	@Deprecated
	public Chart newChart(String id, IComposite parent) {
		Chart chart = new Chart(id);
		parent.add(chart);
		return chart;
	}

	/**
	 * @deprecated use createCheckBox
	 */
	@Deprecated
	public CheckBox newCheckBox(IComposite parent) {
		return newCheckBox(null, parent);
	}

	/**
	 * @deprecated use createCheckBox
	 */
	@Deprecated
	public CheckBox newCheckBox(String id, IComposite parent) {
		CheckBox checkbox = new CheckBox(id);
		parent.add(checkbox);
		return checkbox;
	}

	/**
	 * @deprecated use createCheckBox
	 */
	@Deprecated
	public CheckBox newCheckBoxWithLabel(IComposite parent, String labelKey) {
		return newCheckBoxWithLabel(null, parent, labelKey);
	}

	/**
	 * @deprecated use createCheckBox
	 */
	@Deprecated
	public CheckBox newCheckBoxWithLabel(String id, IComposite parent, String labelKey) {
		CheckBox checkbox = new CheckBox(id);
		parent.add(checkbox);
		checkbox.setLabelKey(labelKey);
		return checkbox;
	}

	/**
	 * @deprecated use createCheckBox
	 */
	@Deprecated
	public CheckBox newCheckBox(IComposite parent, String property) {
		return newCheckBox(null, parent, property);
	}

	/**
	 * @deprecated use createCheckBox
	 */
	@Deprecated
	public CheckBox newCheckBox(String id, IComposite parent, String property) {
		CheckBox checkBox = new CheckBox(id);
		parent.add(checkBox);
		checkBox.setProperty(property);
		return checkBox;
	}

	/**
	 * @deprecated use createCompound
	 */
	@Deprecated
	public Compound newCompound(IComposite parent) {
		return newCompound(null, parent);
	}

	/**
	 * @deprecated use createCompound
	 */
	@Deprecated
	public Compound newCompound(String id, IComposite parent) {
		Compound compound = new Compound(id);
		parent.add(compound);
		return compound;
	}

	/**
	 * @deprecated use createContextMenu
	 */
	@Deprecated
	public ContextMenu newContextMenu(BaseControl parent) {
		return newContextMenu(null, parent);
	}

	/**
	 * @deprecated use createContextMenu
	 */
	@Deprecated
	public ContextMenu newContextMenu(String id, BaseControl parent) {
		ContextMenu menu = new ContextMenu(id);
		parent.addContextMenu(menu);
		return menu;
	}

	/**
	 * @deprecated use createContextMenuItem
	 */
	@Deprecated
	public ContextMenuItem newContextMenuItem(ContextMenu parent, String labelKey, IContextMenuListener listener) {
		return newContextMenuItem(null, parent, labelKey, listener);
	}

	/**
	 * @deprecated use createContextMenuItem
	 */
	@Deprecated
	public ContextMenuItem newContextMenuItem(String id, ContextMenu parent, String labelKey, IContextMenuListener listener) {
		ContextMenuItem menuItem = new ContextMenuItem(id);
		parent.addContextMenuItem(menuItem);
		menuItem.setLabelKey(labelKey);
		menuItem.setListener(listener);
		return menuItem;
	}

	/**
	 * @deprecated use createFileUpload
	 */
	@Deprecated
	public FileUpload newFileUpload(IComposite parent) {
		return newFileUpload(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public FileUpload newFileUpload(IComposite parent, String property, IFileHandler handler, IActionListener actionlistener) {
		return newFileUpload(null, parent, property, handler, actionlistener);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public FileUpload newFileUpload(String id, IComposite parent) {
		FileUpload fileUpload = new FileUpload(id);
		parent.add(fileUpload);
		return fileUpload;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public FileUpload newFileUpload(String id, IComposite parent, String property, IFileHandler handler, IActionListener actionlistener) {
		FileUpload fileUpload = new FileUpload(id);
		parent.add(fileUpload);
		fileUpload.setFileHandler(handler);
		fileUpload.setActionListener(actionlistener);
		fileUpload.setProperty(property);
		return fileUpload;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public HtmlElement newHtmlElement(IComposite parent) {
		return newHtmlElement(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public HtmlElement newHtmlElement(String id, IComposite parent) {
		HtmlElement html = new HtmlElement(id);
		parent.add(html);
		return html;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Label newLabel(IComposite parent, String labelKey) {
		return newLabel(null, parent, labelKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Label newLabel(String id, IComposite parent, String labelKey) {
		Label label = new Label(id);
		parent.add(label);
		label.setTextKey(labelKey);
		return label;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Label newLabel(IComposite parent, String labelKey, FormControl aFormControl) {
		return newLabel(null, parent, labelKey, aFormControl);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Label newLabel(String id, IComposite parent, String labelKey, FormControl aFormControl) {
		Label label = new Label(id);
		parent.add(label);
		if (aFormControl != null)
			linkLabelToControl(label, aFormControl);
		label.setTextKey(labelKey);
		return label;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Menu newMenu(MenuBar parent, String labelKey) {
		return newMenu(null, parent, labelKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Menu newMenu(String id, MenuBar parent, String labelKey) {
		Menu menu = new Menu(id);
		parent.addMenu(menu);
		menu.setLabelKey(labelKey);
		return menu;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Menu newMenu(Menu parent, String labelKey) {
		return newMenu(null, parent, labelKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Menu newMenu(String id, Menu parent, String labelKey) {
		Menu menu = new Menu(id);
		parent.addMenu(menu);
		menu.setLabelKey(labelKey);
		return menu;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public MenuBar newMenuBar(IComposite parent) {
		return newMenuBar(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public MenuBar newMenuBar(String id, IComposite parent) {
		MenuBar menuBar = new MenuBar(id);
		parent.add(menuBar);
		return menuBar;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public MenuItem newMenuItem(Menu parent, String label, String icon, IActionListener listener) {
		return newMenuItem(null, parent, label, icon, listener);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public MenuItem newMenuItem(String id, Menu parent, String label, String icon, IActionListener listener) {
		MenuItem menuItem = new MenuItem(id);
		parent.addMenuItem(menuItem);
		menuItem.setLabelKey(label);
		menuItem.setIconSrc(icon);
		menuItem.setActionListener(listener);
		return menuItem;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public MenuItemSeperator newMenuItemSeperator(Menu parent) {
		return newMenuItemSeperator(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public MenuItemSeperator newMenuItemSeperator(String id, Menu parent) {
		MenuItemSeperator menuItemSeperator = new MenuItemSeperator(id);
		parent.addMenuItemSeparator(menuItemSeperator);
		return menuItemSeperator;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Radio newRadio(IComposite parent, String group, String labelKey) {
		return newRadio(null, parent, group, labelKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Radio newRadio(String id, IComposite parent, String group, String labelKey) {
		Radio control = new Radio(id);
		parent.add(control);
		control.setLabelKey(labelKey);
		control.setGroup(group);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Select newSelect(IComposite parent, String property) {
		return newSelect(null, parent, property);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Select newSelect(String id, IComposite parent, String property) {
		Select control = new Select(id);
		parent.add(control);
		control.setProperty(property);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tab newTab(StandardTabStrip parent, String labelKey) {
		return newTab(null, parent, labelKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tab newTab(String id, ITabStrip parent, String labelKey) {
		Tab control = new Tab(id);
		parent.addTab(control);
		control.setLabelKey(labelKey);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tab newTab(Wizard parent, String labelKey) {
		return newTab(null, parent, labelKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tab newTab(String id, Wizard parent, String labelKey) {
		Tab control = new Tab(id);
		parent.addTab(control);
		control.setLabelKey(labelKey);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public StandardTabStrip newTabStrip(IComposite parent) {
		return newTabStrip(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public StandardTabStrip newTabStrip(String id, IComposite parent) {
		StandardTabStrip control = new StandardTabStrip(id);
		parent.add(control);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public SimpleTable newSimpleTable(IComposite parent, String headerKey) {
		return newSimpleTable(null, parent, headerKey);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public SimpleTable newSimpleTable(String id, IComposite parent, String headerKey) {
		SimpleTable control = new SimpleTable(id);
		parent.add(control);
		control.setEditable(false);
		control.setTitleKey(headerKey);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Table newTable(IComposite parent, String headerKey, int rows) {
		return newTable(null, parent, headerKey, rows);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Table newTable(String id, IComposite parent, String headerKey, int rows) {
		Table control = new Table(id);
		parent.add(control);
		control.setEditable(true);
		control.setRows(rows);
		control.setTitleKey(headerKey);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public TableColumn newTableColumn(Table parent, String headerKey, String property, boolean filterOn) {
		return newTableColumn(null, parent, headerKey, property, filterOn);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public TableColumn newTableColumn(String id, Table parent, String headerKey, String property, boolean filterOn) {
		TableColumn control = new TableColumn(id);
		parent.addColumn(control);
		control.setTitle(headerKey);
		control.setProperty(property);
		control.setFilter(filterOn);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public TableFilter newTableFilter(Table parent) {
		return newTableFilter(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public TableFilter newTableFilter(String id, Table parent) {
		TableFilter control = new TableFilter(id);
		parent.addFilter(control);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public TableLayout newTableLayout(IComposite parent) {
		return newTableLayout(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public TableLayout newTableLayout(String id, IComposite parent) {
		TableLayout control = new TableLayout(id);
		parent.add(control);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Text newText(IComposite parent, String property) {
		return newText(null, parent, property);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Text newText(String id, IComposite parent, String property) {
		Text control = new Text(id);
		parent.add(control);
		control.setProperty(property);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Text newText(IComposite parent, String property, Label currentLabel) {
		return newText(null, parent, property, currentLabel);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Text newText(String id, IComposite parent, String property, Label currentLabel) {
		Text control = new Text(id);
		parent.add(control);
		control.setProperty(property);
		linkLabelToControl(currentLabel, control);
		return control;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Textarea newTextarea(IComposite parent, String property) {
		return newTextarea(null, parent, property);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public RichTextArea newRichTextArea(IComposite parent, String property) {
		return newRichTextArea(null, parent, property);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Textarea newTextarea(String id, IComposite parent, String property) {
		Textarea textarea = new Textarea(id);
		parent.add(textarea);
		textarea.setProperty(property);
		return textarea;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public RichTextArea newRichTextArea(String id, IComposite parent, String property) {
		RichTextArea textarea = new RichTextArea(id);
		parent.add(textarea);
		textarea.setProperty(property);
		return textarea;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tree newTree(IComposite parent) {
		return newTree(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tree newTree(String id, IComposite parent) {
		Tree tree = new Tree(id);
		parent.add(tree);
		return tree;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Wizard newWizard(IComposite parent) {
		return newWizard(null, parent);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Wizard newWizard(String id, IComposite parent) {
		Wizard wiyard = new Wizard(id);
		parent.add(wiyard);
		return wiyard;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public PopupSelect newPopupSelect(IComposite parent, String property, String displayProperty, boolean isMultiselect, String[] columns,
			String[] columnTitles, ILabel describingLabel) {
		return newPopupSelect(parent, property, displayProperty, isMultiselect, columns, columnTitles, describingLabel, null);
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public PopupSelect newPopupSelect(IComposite parent, String property, String displayProperty, boolean isMultiselect, String[] columns,
			String[] columnTitles, ILabel describingLabel, String id) {
		PopupSelect select = new PopupSelect(id);
		parent.add(select);
		select.setProperty(property);
		if (describingLabel != null)
			select.setDescribingLabel(describingLabel);
		select.setColumns(columns);
		select.setColumnTitles(columnTitles);
		select.setDisplayProperty(displayProperty);
		select.setIsMultiselect(isMultiselect);

		return select;
	}

	/**
	 * @deprecated use create... method
	 */
	@Deprecated
	public Tooltip newTooltip(String textKey) {
		Tooltip t = new Tooltip(null);
		t.setTextKey(textKey);
		return t;
	}

}
