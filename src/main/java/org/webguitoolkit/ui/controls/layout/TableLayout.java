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
package org.webguitoolkit.ui.controls.layout;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.EcsAdapter;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.form.CheckBox;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.Label;
import org.webguitoolkit.ui.controls.form.Radio;
import org.webguitoolkit.ui.controls.form.Select;
import org.webguitoolkit.ui.controls.form.Text;

/**
 * <pre>
 * The TableLayout helps to place controls on the screen, you can place 
 * layouts in other layouts and add controls to some positions of the layout.
 * 
 * The composition of the layout is linear, you add components into a row
 * and call newLine() at the end of the row.
 * 
 * Underneath there is a ECS table, there are functions to receive the ECS table and it
 * components for further manipulation like setting styles.
 * 
 * There are some convenience methods to obtain the current control, mainly 
 * used by FixPriceProgrammers.
 * 
 *  --------------- --------------
 * | 1. add(label) | 2. add(text) | 3. newLine()
 *  --------------- --------------
 * | 4. add(label) | 5. add(text) | 6. newLine()
 *  --------------- --------------
 * 
 * </pre>
 * 
 */
public class TableLayout extends BaseControl implements ITableLayout {
	protected TR currentRow;
	// array of all cells we added
	protected List cells;
	protected int maxHorizontalCells = 0; // by default turned off
	protected EcsAdapter currentAdapter;

	// he is doing the real work:
	protected Table ecsTable;

	public TableLayout() {
		super();
		initTableLayout();
		// removing actually the children compartment.
		removeAllChildren();
	}

	public TableLayout(String id) {
		super(id);
		// removing actually the children compartment.
		removeAllChildren();
	}

	/**
	 * create a tablelayout using automatic new lines.
	 * 
	 * @param maxHorizontalCells
	 *            -- number of cells when automatic newline should appear
	 */
	public TableLayout(int maxHorizontalCells) {
		this();
		this.maxHorizontalCells = maxHorizontalCells;
	}

	protected void endHTML(PrintWriter out) {
		// nothing to do because of draw method.
	}

	/**
	 * if you want to initialize this layout and want to start over. It is like having a new copy of TableLayout object. except maxHorizontalCells.
	 * That stays the same.
	 */
	public void removeAllChildren() {
		initTableLayout();
		super.removeAllChildren();
	}

	
	/**
	 * initialises the layout
	 */
	protected void initTableLayout(){
		ecsTable = new Table();
		ecsTable.setID(getId()); // this is the html produce by this component
		cells = new ArrayList();
		currentAdapter = null;
		currentRow = null;	
	}
	
	protected void init() {
	}

	public void draw(PrintWriter out) {
		if (StringUtils.isNotBlank(getCssClass()))
			ecsTable.setClass(getCssClass());
		ecsTable.output(out);
	}

	/**
	 * add a baseControl component to the current location in the table. You may access the cell by calling getCurrentCell()
	 * 
	 * @param comp
	 *            to add (will be wrapped into EcsAdpater automatically.
	 * @return
	 */
	public void add(IBaseControl comp) {
		if (layoutMode) {
			if (maxHorizontalCells > 0 && size(getCurrentRow().elements()) >= maxHorizontalCells) {
				newLine();
			}
			addCell(new TD(createAdapter(comp)));
		}
		// make sure its not added twice as children.
		if (!getChildren().contains(comp)) {
			super.add(comp);
		}
	}

	protected boolean layoutMode = true;

	/**
	 * adds a Cell to the table. You need to construct the TD-element yourself, then it is added with the correct methods, so internal reerences for
	 * row and cells don't get confused. Don't add cells manually.
	 * 
	 * @param cell
	 * @return
	 */
	public TD addCell(TD cell) {
		cells.add(cell);
		getCurrentRow().addElement(cell);
		return cell;
	}

	public TR getCurrentRow() {
		if (currentRow == null) {
			currentRow = new TR();
			getEcsTable().addElement(currentRow);
		}
		return currentRow;
	}

	/**
	 * just add a new line into the gridlayout, all following components will be added to a new TR element. currentRow() changes.
	 * 
	 * @return
	 */
	public ITableLayout newLine() {
		// new element will actually be created when needed (getCurrentRow())
		currentRow = null;
		return this;
	}

	/**
	 * if you want to add some option (probably html stuff) to the current cell (the last one inserted). Technically this is the cell produced in the
	 * 
	 * @return
	 */
	public TD getCurrentCell() {
		return (TD) cells.get(cells.size() - 1);
	}

	/**
	 * return the last component added and casts it as text.
	 * 
	 * @return
	 */
	public Text getCurrentAsText() {
		return (Text) currentAdapter.getCurrentControl();
	}

	/** just add a new line into the gridlayout, all following components will be added to a new TR element. currentRow() changes.
	 * 
	 * @return ITableLayout
	 */
	public ITableLayout newRow() {
		return newLine();
	}

	/**
	 * @return ITableLayout
	 */
	public ITableLayout appendToCell(IBaseControl control) {
		return appendToCurrentCell(control);
	}
	
	public ITableLayout addWithRef(IFormControl aFormControl, ILabel refLabel) {
		add(aFormControl);
		aFormControl.setDescribingLabel(refLabel);
		return this;
	}

	/**
	 * return the last component added and casts it as Select.
	 * 
	 * @return
	 */
	public Select getCurrentAsSelect() {
		return (Select) currentAdapter.getCurrentControl();
	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.layout.ITableLayout#appendToCurrentCell(org.webguitoolkit.ui.controls.IBaseControl)
	 */
	public TableLayout appendToCurrentCell(IBaseControl comp) {
		if (maxHorizontalCells > 0 && size(getCurrentRow().elements()) >= maxHorizontalCells) {
			newLine();
		}
		getCurrentCell().addElement(createAdapter((BaseControl) comp));
		super.add((BaseControl) comp);
		return this;
	}

	/**
	 * creates an adapter for the control and assumes that this is the currentadapter for now.+ That means the newly created adapater must be added to
	 * the layout to make sense. (the geCurrentAdapter() will return the newly created adapter.)
	 * 
	 * @param comp
	 * @return
	 */
	protected EcsAdapter createAdapter(IBaseControl comp) {
		EcsAdapter apt = new EcsAdapter((BaseControl) comp);
		currentAdapter = apt;
		return apt;
	}

	/**
	 * just counts the elements in the enumeration.
	 * 
	 * @param en
	 * @return
	 */
	private static int size(Enumeration en) {
		int i = 0;
		for (; en.hasMoreElements(); en.nextElement()) {
			i++;
		}
		return i;
	}

	public Table getEcsTable() {
		return ecsTable;
	}

	public EcsAdapter getCurrentAdapter() {
		return currentAdapter;
	}

	protected void setCurrentAdapter(EcsAdapter currentAdapter) {
		this.currentAdapter = currentAdapter;
	}

	public Label getCurrentAsLabel() {
		return (Label) currentAdapter.getCurrentControl();
	}

	public CheckBox getCurrentAsCheckBox() {
		return (CheckBox) currentAdapter.getCurrentControl();
	}

	public FormControl getCurrentAsFormControl() {
		return (FormControl) currentAdapter.getCurrentControl();
	}

	public Radio getCurrentAsRadio() {
		return (Radio) currentAdapter.getCurrentControl();
	}

	public void setCssClass(String cssClass) {
		getEcsTable().setClass(cssClass);
	}

	public void setStyle(String style) {
		getEcsTable().setStyle(style);
	}

	public boolean isLayoutMode() {
		return layoutMode;
	}

	public void setLayoutMode(boolean layoutMode) {
		this.layoutMode = layoutMode;
	}

	public TD addEmptyCell() {
		TD cell = new TD();
		cells.add(cell);
		getCurrentRow().addElement(cell);
		return cell;
	}

}
