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

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.Button;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.FormControl;
import org.webguitoolkit.ui.controls.form.button.LinkButton;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;

/**
 * The LinkButtonColumnRenderer is an easy way to render links into a table and handle events with a special action listener.
 * 
 * <code>
 * createTable(){
 *  ...
 * 	LinkButtonColumnRenderer bcr = new LinkButtonColumnRenderer(col,"img/edit.gif", "Edit", new ButtonListener() );
 *  col.setRenderer(bcr);
 * }
 * 
 * class ButtonListener implements ITableButtonActionListener{
 *   public void onAction(ClientEvent event, Table table, int row ){
 *    	table.getPage().sendInfo("Selected row " + row );
 *    }
 * }
 * </code>
 * 
 * @author i102455
 * 
 */
public class LinkButtonColumnRenderer extends ChildColumnRenderer {

	private String labelKey = null;
	protected ITableButtonActionListener listener;
	private Button button;

	protected boolean isStatic = true;
	private final String titleProperty;

	/**
	 * @param newLabelKey pass null to use a value from a property
	 * @param newListener listener
	 */
	public LinkButtonColumnRenderer(String newLabelKey, ITableButtonActionListener newListener) {
		this(newLabelKey, null, newListener);
	}

	/**
	 * @param newLabelKey pass null to use a value from a property
	 * @param newListener listener
	 */
	public LinkButtonColumnRenderer(String newLabelKey, String titleProperty, ITableButtonActionListener newListener) {
		super();
		this.titleProperty = titleProperty;
		if (StringUtils.isBlank(newLabelKey)) {
			isStatic = false;
		}
		else {
			this.labelKey = newLabelKey;
		}
		this.listener = newListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webguitoolkit.ui.controls.table.renderer.ChildColumnRenderer#getFormControls()
	 */
	@Override
	protected FormControl[] getFormControls() {
		Compound compound = table.getRowCompound();
		button = new LinkButton() {
			private String label;

			public void dispatch(ClientEvent event) {
				if (!hasExecutePermission())
					return;
				if (event.getTypeAsInt() == ClientEvent.TYPE_ACTION) {

					// id should look like: "Ta296.r1.fcnull"
					// we have to find the row number between ".r" and ".fc"
					String id = event.getSourceId();

					id = id.substring(id.indexOf(".r") + 2, id.indexOf(".fc"));

					// if row is numeric parse to int
					int row = 0;
					int rowOffset = table.getFirstRow();
					if (StringUtils.isNumeric(id))
						row = Integer.parseInt(id);
					// row offset is added to the currently returned row id. when a table is scrolled down it's row numbers stay
					// the same as bevor.
					// therefore the offset must be added to the current row number!
					row += rowOffset;
					// call listener
					listener.onAction(event, table, row);
				}
			}

			public void loadFrom(Object data) {
				if (!isStatic) {
					// get the property from the data objecrt
					String value = "";
					String title = "";
					if (data != null) {
						value = PropertyAccessor.retrieveString(data, getProperty(), getConverter());
						if (titleProperty != null)
							title = PropertyAccessor.retrieveString(data, titleProperty, getConverter());
					}
					// move to contxt
					if (StringUtils.isEmpty(value)) {
						setVisible(true);
						setVisible(false);
					}
					else {
						setVisible(true);
						button.setLabel(value);
						button.setTooltip(title);
					}
				}
			}

			public void setTooltip(String tooltip) {
				super.setTooltip(tooltip);
				getContext().setAttribute(getId(), "title", tooltip);
			}

			protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D) {
				if (StringUtils.isEmpty(text))
					text = label;
				if (!isStatic) {
					super.setTooltip(getContext().processValue(getId() + ".title"));
				}
				super.endHTML(out, imgSrc, text, mode3D);
			}

			public void setLabel(String label) {
				super.setLabel(label);
				this.label = label;
			}
		};
		button.setId(id4FormControl(0, tableColumn.getProperty()));
		button.setProperty(tableColumn.getProperty());
		button.setCancelBubble(true);

		// // if static button set image or text directly
		if (isStatic) {
			if (labelKey != null) {
				button.setLabelKey(labelKey);
			}
		}
		else {
			button.setLabel("&nbsp;");
		}
		button.setParent(table);
		button.setCompound(compound);
		if (tableColumn.getIsDisplayed())
			compound.addElement(button);

		return new FormControl[] { button };
	}

	/**
	 * 
	 * @param whereId the id
	 * @param data the data to load
	 * @param col the tableColumn
	 * @param ctx the context
	 * @param idxRow the row
	 * @param idxCol the column
	 */
	public void load(String whereId, Object data, ITableColumn col, IContext ctx, int idxRow, int idxCol) {
		if (isStatic) {
			super.load(whereId, data, col, ctx, idxRow, idxCol);
		}
		else {
			init(col);
		}
	}
	
	/**
	 * That extended classes can use button reference...
	 * @return IButton
	 */
	protected Button getButton(){
		return button;
	}

}
