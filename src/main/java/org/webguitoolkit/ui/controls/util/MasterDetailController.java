package org.webguitoolkit.ui.controls.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.Canvas;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.ButtonBar;
import org.webguitoolkit.ui.controls.form.IButton;
import org.webguitoolkit.ui.controls.form.IButtonBar;
import org.webguitoolkit.ui.controls.form.IButtonBarListener;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.ICompoundLifecycleElement;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.SequentialTableLayout;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabListener;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.tab.StandardTabStrip;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.ITableListener;
import org.webguitoolkit.ui.controls.table.Table;

/**
 * <h1>Support for standard master detail behavior</h1>
 * <p>
 * This Factory can be used to create controls for master detail scenarios that act with each other.
 * </p>
 * That means,
 * <ul>
 * <li>if a row is selected in the master table the compounds of the master button bars are reloaded with the new value</li>
 * <li>If a tab or row is changed and a compound is in edit mode the user is prompted if he wants to discard the changes</li>
 * <li>If a value has changed in a master compound the table data is reloaded</li>
 * <li>if a entry is deleted the will be removed from the table</li>
 * <li>...</li>
 * </ul>
 * 
 * @author Martin
 */
public class MasterDetailController implements Serializable {
	private ITable table;
	private ITabStrip tabStrip;
	private final List<ICompound> compounds = new ArrayList<ICompound>();
	private final List<IButtonBar> buttonBars = new ArrayList<IButtonBar>();

	public void setMasterTable(ITable table) {
		this.table = table;
		ITableListener inner = ((Table)table).getListener();
		table.setListener(new DelegateTableListener(inner));
	}

	public void setTabStrip(ITabStrip tabStrip) {
		this.tabStrip = tabStrip;
		tabStrip.setListener(new DelegateTabListener(((StandardTabStrip)tabStrip).getListener()));
	}

	public void addMasterCompound(ICompound compound, IButtonBar buttonBar) {
		buttonBars.add(buttonBar);
		compounds.add(compound);
		((DelegateTableListener)((Table)table).getListener()).addCompound(compound);
		buttonBar.setListener(new DelegateButtonBarListener(((ButtonBar)buttonBar).getListener(), table));
	}

	public void addObservedCompound(ICompound compound) {
		BaseControl tempParent = (BaseControl)compound;
		while (tempParent.getParent() != null && tempParent.getParent() != tabStrip) {
			tempParent = tempParent.getParent();
		}
		if (tempParent.getParent() == tabStrip && tempParent instanceof ITab) {
			// register compound on the tab strip
			((DelegateTabListener)((StandardTabStrip)tabStrip).getListener()).addCompound((ITab)tempParent, compound);
		}
	}

	/**
	 * DelegateTableListener handles interaction with table events
	 */
	public class DelegateTableListener extends AbstractTableListener implements ITableListener {

		private int oldSelection;

		private ITableListener delegate = null;
		private final List<ICompound> tablecompounds = new ArrayList<ICompound>();

		public DelegateTableListener() {
		}

		public DelegateTableListener(ITableListener delegate) {
			setDelegate(delegate);
		}

		public void setDelegate(ITableListener listener) {
			delegate = listener;
		}

		private ITableListener getDelegate() {
			if (delegate == null)
				return new AbstractTableListener() {
					@Override
					public void onRowSelection(ITable table, int row) {
					}
				};
			return delegate;
		}

		public void addCompound(ICompound compound) {
			tablecompounds.add(compound);
		}

		@Override
		public void onCheckAll(ClientEvent event) {
			getDelegate().onCheckAll(event);
		}

		@Override
		public void onClickDown(ClientEvent event) {
			getDelegate().onClickDown(event);
		}

		@Override
		public void onClickUp(ClientEvent event) {
			getDelegate().onClickUp(event);
		}

		@Override
		public void onDDFilter(ClientEvent event) {
			getDelegate().onDDFilter(event);
		}

		@Override
		public void onDragStart(ClientEvent event, String draggableId) {
			getDelegate().onDragStart(event, draggableId);
		}

		@Override
		public void onDropped(ClientEvent event, String draggabelId, String droppableId) {
			getDelegate().onDropped(event, draggabelId, droppableId);
		}

		@Override
		public void onEditTableLayout(ClientEvent event, int rowCount, String tableSetting) {
			getDelegate().onEditTableLayout(event, rowCount, tableSetting);
		}

		@Override
		public void onGotoBottom(ClientEvent event) {
			getDelegate().onGotoBottom(event);
		}

		@Override
		public void onGotoRow(ClientEvent event) {
			Table table = (Table)event.getSource();
			// if there is a least one row...
			boolean success = true;
			if (table.getRowsLoaded() > 0) {
				int userRow = table.getPage().getContext().getValueAsInt(table.id4RowInput()) - 1;
				// set userrowinto bounds
				userRow = Math.max(0, Math.min(userRow, table.getPage().getContext().getValueAsInt(table.id4size()) - 1));
				IDataBag bag = table.getRow(userRow);
				success = handleRowChange(bag, event, false);
			}
			if (success)
				getDelegate().onGotoRow(event);
		}

		@Override
		public void onGotoTop(ClientEvent event) {
			getDelegate().onGotoTop(event);
		}

		@Override
		public void onImplicitFilter(ClientEvent event) {
			getDelegate().onImplicitFilter(event);
		}

		@Override
		public void onPageDown(ClientEvent event) {
			getDelegate().onPageDown(event);
		}

		@Override
		public void onPageUp(ClientEvent event) {
			getDelegate().onPageUp(event);
		}

		@Override
		public void onRowSelected(ClientEvent event) {
			// Reference to the Table is always good
			Table table = (Table)event.getSource();
			// we do the controlling of the highlite
			int rowSelected = Integer.parseInt(event.getParameter(0));
			// there are some misguided events, when the table size is smaller than
			// the viewable size
			int firstRow = table.getPage().getContext().getValueAsInt(table.id4FirstRow());
			int absSelection = firstRow + rowSelected;
			IDataBag bag = table.getRow(absSelection);
			boolean success = handleRowChange(bag, event, false);
			if (success)
				getDelegate().onRowSelected(event);
		}

		@Override
		public void onScrollTo(ClientEvent event) {
			getDelegate().onScrollTo(event);
		}

		@Override
		public void onSort(ClientEvent event) {
			getDelegate().onSort(event);
		}

		@Override
		public void onRowSelection(ITable table, int row) {
			for (Iterator iter = tablecompounds.iterator(); iter.hasNext();) {
				ICompound compound = (ICompound)iter.next();
				compound.setBag(table.getRow(row));
				compound.load();
			}
			if (getDelegate() instanceof AbstractTableListener)
				((AbstractTableListener)getDelegate()).onRowSelection(table, row);
		}

		private boolean handleRowChange(IDataBag newItem, ClientEvent evnet, boolean isRowSelect) {
			List<ICompound> compoundsInEditMode = new ArrayList<ICompound>();
			for (Iterator iter = tablecompounds.iterator(); iter.hasNext();) {
				ICompound compound = (ICompound)iter.next();
				if (compound.getMode() == ICompound.MODE_EDIT || compound.getMode() == ICompound.MODE_NEW) {
					compoundsInEditMode.add(compound);
				}
			}
			if (!compoundsInEditMode.isEmpty()) {
				new UnsavedCompoundsView(WebGuiFactory.getInstance(), table.getPage(), new ConfirmListener(delegate, evnet, tablecompounds,
						compoundsInEditMode, isRowSelect, newItem, ((Table)table).getSelectedRowIndex())).show();
				return false;
			}
			for (Iterator iter = tablecompounds.iterator(); iter.hasNext();) {
				ICompound compound = (ICompound)iter.next();
				compound.setBag(newItem);
				compound.load();
			}
			return true;
		}

		public class ConfirmListener implements IConfirmationListener {
			private final boolean isRowSelelct;
			private final List allCompounds;
			private final List compoundsInEdit;
			private final ClientEvent tableEvent;
			private final ITableListener delegatedListener;
			private final IDataBag newItem;
			private final int oldSelectionId;

			public ConfirmListener(ITableListener delegate, ClientEvent tableEvent, List allCompounds, List compoundsInEdit,
					boolean isRowSelelct, IDataBag newItem, int oldSelection) {
				super();
				this.isRowSelelct = isRowSelelct;
				this.allCompounds = allCompounds;
				this.compoundsInEdit = compoundsInEdit;
				this.tableEvent = tableEvent;
				this.delegatedListener = delegate;
				this.newItem = newItem;
				this.oldSelectionId = oldSelection;
			}

			public void onYes(ClientEvent event) {
				for (Iterator iter = compoundsInEdit.iterator(); iter.hasNext();) {
					ICompound comp = (ICompound)iter.next();
					comp.getBag().undo();
					comp.changeElementMode(ICompound.MODE_READONLY);
				}
				for (Iterator iter = allCompounds.iterator(); iter.hasNext();) {
					ICompound compound = (ICompound)iter.next();
					compound.setBag(newItem);
					compound.load();
				}
				if (delegate != null) {
					if (isRowSelelct)
						delegatedListener.onRowSelected(tableEvent);
					else
						delegatedListener.onGotoRow(tableEvent);
				}
			}

			public void onNo(ClientEvent event) {
				// selectionChange has to be called twice cause
				// the old selection may be the same in the context
				((Table)tableEvent.getSource()).selectionChange(-1,false);
				((Table)tableEvent.getSource()).selectionChange(oldSelectionId,false);
			}
		}
	}

	/**
	 * DelegateTableListener handles interaction with tab events
	 */
	public class DelegateTabListener implements ITabListener {
		private ITabListener delegate = null;
		private final Hashtable compoundsOnTab = new Hashtable();

		/**
		 * @param listener
		 */
		public DelegateTabListener(ITabListener listener) {
			setDelegate(listener);
		}

		/**
		 */
		public DelegateTabListener() {
		}

		public void setDelegate(ITabListener listener) {
			delegate = listener;
		}

		public void addCompound(ITab tab, ICompound compound) {
			List comps = (List)compoundsOnTab.get(tab);
			if (comps == null) {
				comps = new ArrayList();
				compoundsOnTab.put(tab, comps);
			}
			comps.add(compound);
		}

		public boolean onTabChange(ITab old, ITab selected, ClientEvent event) {
			List compounds = (List)compoundsOnTab.get(old);
			List compoundsInEditMode = new ArrayList();
			if (compounds != null) {
				for (Iterator iter = compounds.iterator(); iter.hasNext();) {
					ICompound compound = (ICompound)iter.next();
					if (compound.getMode() == ICompound.MODE_EDIT || compound.getMode() == ICompound.MODE_NEW) {
						compoundsInEditMode.add(compound);
					}
				}
			}
			if (!compoundsInEditMode.isEmpty()) {
				new UnsavedCompoundsView(WebGuiFactory.getInstance(), table.getPage(), new ConfirmListener(delegate, event,
						compoundsInEditMode, old, selected)).show();
				return false;
			}
			if (delegate != null)
				return delegate.onTabChange(old, selected, event);
			return true;
		}

		public class ConfirmListener implements IConfirmationListener {
			private final ITab oldTab;
			private final ITab newTab;
			private final List tabcompounds;
			private final ClientEvent tabEvent;
			private final ITabListener delegateTabListener;

			public ConfirmListener(ITabListener delegate, ClientEvent tabEvent, List compounds, ITab oldTab, ITab newTab) {
				super();
				this.oldTab = oldTab;
				this.newTab = newTab;
				this.tabcompounds = compounds;
				this.tabEvent = tabEvent;
				this.delegateTabListener = delegate;
			}

			public void onYes(ClientEvent event) {
				for (Iterator iter = tabcompounds.iterator(); iter.hasNext();) {
					ICompound comp = (ICompound)iter.next();
					comp.getBag().undo();
					comp.load();
					comp.changeElementMode(ICompound.MODE_READONLY);
				}
				if (delegateTabListener != null)
					delegateTabListener.onTabChange(oldTab, newTab, tabEvent);
				else
					oldTab.getTabStrip().selectTab(oldTab.getTabStrip().getTabIndex(newTab));
			}

			public void onNo(ClientEvent event) {
				// nothing to do
			}
		}
	}

	/**
	 * DelegateTableListener handles interaction with button bar events
	 */
	public class DelegateButtonBarListener implements IButtonBarListener {

		private final ITable masterTable;

		private IButtonBarListener delegate;

		private int oldTableSelection = -1;

		public DelegateButtonBarListener(ITable table) {
			this.masterTable = table;
		}

		public DelegateButtonBarListener(IButtonBarListener listener, ITable table) {
			this(table);
			delegate = listener;
		}

		public void setDelegate(IButtonBarListener listener) {
			this.delegate = listener;
		}

		public void onCancel(ClientEvent event) {
			if (getCompound(event).getMode() == ICompound.MODE_NEW)
				((Table)masterTable).selectionChange(oldTableSelection,true);
			if (delegate != null)
				delegate.onCancel(event);
		}

		public void onDelete(ClientEvent event) {
			if (delegate != null)
				delegate.onDelete(event);
			if(!getCompound(event).hasErrors()) {
				((Table)masterTable).removeAndReload(getCompound(event).getBag());
				masterTable.selectionChange(-1, false);
				getCompound(event).setBag(null);
				getCompound(event).load();
			}
		}

		public void onEdit(ClientEvent event) {
			if (delegate != null)
				delegate.onEdit(event);
		}

		public void onNew(ClientEvent event) {
			if (delegate != null)
				delegate.onNew(event);
			oldTableSelection = ((Table)masterTable).getSelectedRowIndex();
			masterTable.selectionChange(-1, false);
		}

		public void onSave(ClientEvent event) {
			boolean isNew = getCompound(event).getMode() == ICompound.MODE_NEW;
			if (delegate != null)
				delegate.onSave(event);
			if (isNew && !getCompound(event).hasErrors())
				masterTable.addAndReload(getCompound(event).getBag());
			else if (!getCompound(event).hasErrors())
				masterTable.load();
		}

		private ICompound getCompound(ClientEvent event) {
			return ((ICompoundLifecycleElement)event.getSource()).surroundingCompound();
		}
	}

	/**
	 * Confirmation dialog that is displayed when there are compounds in edit mode
	 */
	class UnsavedCompoundsView extends AbstractPopup {
		private final IConfirmationListener proceedListener;

		public UnsavedCompoundsView(WebGuiFactory factory, Page page, IConfirmationListener proceedListener) {
			super(factory, page, "popup.header.unsaved.compounds@There are unsaved Forms!", 400, 100);
			this.proceedListener = proceedListener;
		}

		@Override
		protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
			String msgType = "warn";
			String msg = "popup.body.unsaved.compounds@There are unsaved Forms! Proceed and discard changes?";

			// the canvas will be inserted manually into the page
			viewConnector.setDragable(true);
			viewConnector.setDisplayMode(Canvas.DISPLAY_MODE_WINDOW_MODAL);
			viewConnector.addCssClass("wgtPopup wgtAlertBox");

			if (StringUtils.isNotEmpty(msgType))
				viewConnector.setTitle("msgbox." + msgType + "@" + msgType.substring(0, 1).toUpperCase() + msgType.substring(1));

			IActionListener noListener = new IActionListener() {
				public void onAction(ClientEvent event) {
					proceedListener.onNo(event);
					close();
				}
			};
			IActionListener yesListener = new IActionListener() {
				public void onAction(ClientEvent event) {
					proceedListener.onYes(event);
					close();
				}
			};

			viewConnector.setWindowActionListener(new Window2ActionAdapter(noListener));

			SequentialTableLayout manager = new SequentialTableLayout();
			manager.setTableStyle("width: 100%;");
			viewConnector.setLayout(manager);
			// add image
			if (StringUtils.isNotEmpty(msgType)) {
				IHtmlElement img = factory.createHtmlElement(viewConnector, "img");
				img.setAttribute("src", "./images/wgt/icons/msg_icon_" + msgType + ".gif");
				img.setLayoutData(SequentialTableLayout.getLayoutData(0, 0, false).setCellStyle("text-align: center; width: 40px;"));
			}

			// add label
			ILabel infoLabel = factory.createLabel(viewConnector, msg);
			infoLabel.setLayoutData(SequentialTableLayout.getLastInRow().setCellStyle("text-align: left;"));

			// add button yes
			IButton yesButton = factory.createButton(viewConnector, null, "button.yes@Yes", "button.yes@Yes", yesListener);
			if (StringUtils.isNotEmpty(msgType))
				yesButton.setLayoutData(SequentialTableLayout.getLastInRow().setCellColSpan(2).setCellStyle("text-align: center;"));
			else
				yesButton.setLayoutData(SequentialTableLayout.getLastInRow().setCellStyle("text-align: center;"));

			// add button no
			IButton noButton = factory.createButton(viewConnector, null, "button.no@No", "button.no@No", noListener);
			noButton.setLayoutData(SequentialTableLayout.APPEND);

		}
	}

	/**
	 * listener that handles the confirmation result.
	 */
	interface IConfirmationListener {
		void onYes(ClientEvent event);

		void onNo(ClientEvent event);
	}
}
