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
package org.webguitoolkit.ui.controls.table.renderer;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.TableColumn;

/**
 * <pre>
 * we delegate the rendering to the children. This is used for mainly for
 * FormControl inside the tablecolumn tag. (editable tables).
 * 
 * This renderer must initialise the children (input fields) so that they use
 * the table's compound.
 * </pre>
 * 
 * @author arno@schatz.to
 * 
 */
public abstract class ChildColumnRenderer extends AbstractColumRenderer implements IColumnRenderer {

	protected FormControl[] myControls;
	protected Table table;
	protected Compound compound;
	protected boolean isInit = false;

	public ChildColumnRenderer() {
		super();
	}

	public ChildColumnRenderer(final ITableColumn col) {
		super();
	}

	public final void init(final ITableColumn col) {
		if (!isInit) {
			super.init(col);
			table = (Table)((TableColumn)col).getParent();
			compound = table.getRowCompound();
			myControls = getFormControls();
			move2Row(0);
		}
		isInit = true;
	}

	protected abstract FormControl[] getFormControls();

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.table.IColumnRenderer#generateHTML(org.webguitoolkit.ui.controls.table.TableColumn, java.lang.String, int, int)
	 */
	public String generateHTML(final ITableColumn col, final String cellId, final int idxRow, int idxCol) {

		init(col);

		// tell components that we are in the actual row
		move2Row(idxRow);
		// generate the html for each
		StringWriter htmlWriter = new StringWriter();
		PrintWriter pw = new PrintWriter(htmlWriter);

		for (int i = 0; i < myControls.length; i++) {
			try {
				table.addCellControl(myControls[i]);
				myControls[i].drawFormControl(pw);
			}
			catch (Exception e) {
				throw new WGTException(e);
			}
		}
		return htmlWriter.toString();
	}

	/**
	 * tell the controls that they should act on row 'row'. As 1 control is responsible for many html controls (one per row)
	 * 
	 * @param row current row
	 */
	public void move2Row(int row) {
		for (int i = 0; i < myControls.length; i++) {
			String theId = id4FormControl(row, myControls[i].getProperty());
			myControls[i].setId(theId);
		}
	}

	protected String id4FormControl(int row, String property) {
		return (table.id4Row(row) + ".fc" + property).intern();
	}

	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		init(col);
		// make the rows visible which got actually loaded.
		// the load process itself is in the compound statement
		// move2Row(idxRow);
		boolean show = StringUtils.isNotEmpty(col.getMappedProperty(data));

		for (int i = 0; i < myControls.length; i++) {
			if (myControls[i].isVisible() != show) {
				myControls[i].setVisible(show);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.endress.infoserve.wgt.controls.table.IColumnRenderer#clear(java.lang.String, com.endress.infoserve.wgt.controls.table.TableColumn, com.endress.infoserve.wgt.ajax.Context, int, int)
	 */
	public void clear(String whereId, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		init(col);
		for (int i = 0; i < myControls.length; i++) {
			myControls[i].setVisible(false);
		}
	}

	public FormControl[] getMyControls() {
		return myControls;
	}
}
