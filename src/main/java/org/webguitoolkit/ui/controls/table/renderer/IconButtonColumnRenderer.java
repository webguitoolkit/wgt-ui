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
import org.webguitoolkit.ui.controls.form.button.IconButton;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.util.PropertyAccessor;
import org.webguitoolkit.ui.controls.util.Tooltip;

/**
 * The ButtonColumnRenderer is an easy way to render buttons into a table and handle events with a special action listener.
 * 
 * <code>
 * createTable(){
 *  ...
 * 	ButtonColumnRenderer bcr = new ButtonColumnRenderer(col,"img/edit.gif", "Edit", new ButtonListener() );
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
 * @author i102454
 * 
 */
public class IconButtonColumnRenderer extends ChildColumnRenderer {

	private Button button;

	protected String labelKey;
	protected String imageSource;
	protected Tooltip tooltipAdvanced;
	protected String tooltip;
	protected boolean cancelBubble;
	protected ITableButtonActionListener listener;

	protected boolean isStatic = true;
	protected boolean isImageProperty = true;

	private String titleProperty = null;

	/**
	 * Constructor for property dependent image or text values
	 * 
	 * With this constructor you can have different Buttons in one column
	 * 
	 * @param col the Table column
	 * @param isImageProperty if the image is source is the property value or the buttons text
	 * @param listener
	 * 
	 * @deprecated
	 */
	public IconButtonColumnRenderer(final TableColumn col, boolean isImageProperty, final ITableButtonActionListener listener) {
		this(col, null, null, null, false, listener);
		isStatic = false;
		this.isImageProperty = isImageProperty;
	}

	/**
	 * @deprecated
	 */
	public IconButtonColumnRenderer(final TableColumn col, String imageSource, String tooltip, final ITableButtonActionListener listener) {
		this(col, null, imageSource, tooltip, false, listener);
	}

	/**
	 * @deprecated
	 */
	public IconButtonColumnRenderer(final TableColumn col, String imageSource, String tooltip, final ITableButtonActionListener listener,
			boolean cancelBubble) {
		this(col, null, imageSource, tooltip, cancelBubble, listener);
	}

	/**
	 * @deprecated
	 */
	public IconButtonColumnRenderer(final TableColumn col, String imageSource, Tooltip tooltip, final ITableButtonActionListener listener) {
		this(col, null, imageSource, tooltip, null, false, listener);
	}

	/**
	 * @deprecated
	 */
	public IconButtonColumnRenderer(final TableColumn col, String labelKey, final ITableButtonActionListener listener) {
		this(col, labelKey, null, null, false, listener);
	}

	/**
	 * @deprecated
	 */
	public IconButtonColumnRenderer(final TableColumn col, String labelKey, final ITableButtonActionListener listener, boolean cancelBubble) {
		this(col, labelKey, null, null, null, cancelBubble, listener);
	}

	/**
	 * @deprecated
	 */
	private IconButtonColumnRenderer(final TableColumn col, String labelKey, String imageSource, String tooltip, boolean cancelBubble,
			final ITableButtonActionListener listener) {
		this(col, null, imageSource, null, tooltip, cancelBubble, listener);
	}

	/**
	 * @deprecated
	 */
	private IconButtonColumnRenderer(final TableColumn col, String labelKey, String imageSource, Tooltip tooltipAdvanced, String tooltip,
			boolean cancelBubble, final ITableButtonActionListener listener) {
		this(labelKey, imageSource, tooltipAdvanced, tooltip, cancelBubble, listener);
	}

	/**
	 * Constructor for property dependent image or text values
	 * 
	 * With this constructor you can have different Buttons in one column
	 * 
	 * @param isImageProperty if the image is source is the property value or the buttons text
	 * @param listener
	 */
	public IconButtonColumnRenderer(boolean isImageProperty, final ITableButtonActionListener listener) {
		this(null, null, null, false, listener);
		isStatic = false;
		this.isImageProperty = isImageProperty;
	}

	public IconButtonColumnRenderer(boolean isImageProperty, final ITableButtonActionListener listener, boolean cancelBubble) {
		this(null, null, null, cancelBubble, listener);
		isStatic = false;
		this.isImageProperty = isImageProperty;
	}

	public IconButtonColumnRenderer(boolean isImageProperty, String titleProperty, final ITableButtonActionListener listener,
			boolean cancelBubble) {
		this(null, null, null, cancelBubble, listener);
		this.titleProperty = titleProperty;
		isStatic = false;
		this.isImageProperty = isImageProperty;
	}

	public IconButtonColumnRenderer(String imageSource, String tooltip, final ITableButtonActionListener listener) {
		this(null, imageSource, tooltip, false, listener);
	}

	public IconButtonColumnRenderer(String imageSource, String tooltip, final ITableButtonActionListener listener, boolean cancelBubble) {
		this(null, imageSource, tooltip, cancelBubble, listener);
	}

	public IconButtonColumnRenderer(String imageSource, Tooltip tooltip, final ITableButtonActionListener listener) {
		this(null, imageSource, tooltip, null, false, listener);
	}

	public IconButtonColumnRenderer(String labelKey, final ITableButtonActionListener listener) {
		this(labelKey, null, null, false, listener);
	}

	public IconButtonColumnRenderer(String labelKey, final ITableButtonActionListener listener, boolean cancelBubble) {
		this(labelKey, null, null, null, cancelBubble, listener);
	}

	public IconButtonColumnRenderer(String labelKey, String imageSource, String tooltip, boolean cancelBubble,
			final ITableButtonActionListener listener) {
		this(labelKey, imageSource, null, tooltip, cancelBubble, listener);
	}

	public IconButtonColumnRenderer(String labelKey, String imageSource, Tooltip tooltipAdvanced, String tooltip, boolean cancelBubble,
			final ITableButtonActionListener listener) {
		super();
		this.labelKey = labelKey;
		this.imageSource = imageSource;
		this.tooltipAdvanced = tooltipAdvanced;
		this.tooltip = tooltip;
		this.cancelBubble = cancelBubble;
		this.listener = listener;
	}

	protected FormControl[] getFormControls() {
		Compound compound = table.getRowCompound();
		button = new IconButton() {
			String src = null;
			String label = null;

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
					String tooltip = "";
					if (data != null) {
						value = PropertyAccessor.retrieveString(data, getProperty(), getConverter());
						if (titleProperty != null)
							tooltip = PropertyAccessor.retrieveString(data, titleProperty, getConverter());
					}
					// move to contxt
					if (StringUtils.isEmpty(value)) {
						setVisible(true);
						setVisible(false);
					}
					else {
						setVisible(true);
						if (isImageProperty)
							button.setSrc(value);
						else
							button.setLabel(value);
						if (tooltip != null)
							button.setTooltip(tooltip);
					}
				}
			}

			protected void endHTML(PrintWriter out, String imgSrc, String text, boolean mode3D) {
				if (StringUtils.isEmpty(imgSrc))
					imgSrc = src;
				if (StringUtils.isEmpty(text))
					text = label;
				if (!isStatic)
					super.setTooltip(getContext().processValue(getId() + ".title"));
				super.endHTML(out, imgSrc, text, mode3D);
			}

			public String getSrc() {
				return getContext().getValue(id4ImageSrc());
			}

			public void setSrc(String src) {
				super.setSrc(src);
				this.src = src;
			}

			public void setLabel(String label) {
				super.setLabel(label);
				this.label = label;
			}

			public void setTooltip(String tooltip) {
				super.setTooltip(label);
				getContext().setAttribute(getId(), "title", tooltip);
			}
		};
		button.setId(id4FormControl(0, tableColumn.getProperty()));
		button.setProperty(tableColumn.getProperty());
		button.setCancelBubble(cancelBubble);

		// if static button set image or text directly
		if (isStatic) {
			if (labelKey != null) {
				button.setLabelKey(labelKey);
			}
			if (imageSource != null) {
				button.setSrc(imageSource);
				if (tooltip != null) {
					button.setTooltip(tooltip);
				}
				if (tooltipAdvanced != null) {
					button.setTooltip(tooltipAdvanced);
				}
			}
		}
		else {
			if (isImageProperty) {
				button.setSrc("./images/wgt/1.gif");
			}
			else {
				button.setLabel("");
			}
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
	
	public String generateHTML(ITableColumn col, String cellId, int idxRow, int idxCol) {
		IContext ctx = col.getPage().getContext();
		String result = super.generateHTML(col, cellId, idxRow, idxCol);
		ctx.add(cellId + ".align", "center", IContext.TYPE_ATT, IContext.STATUS_NOT_EDITABLE);
		return result;
	}
}
