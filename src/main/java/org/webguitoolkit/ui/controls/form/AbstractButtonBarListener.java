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
package org.webguitoolkit.ui.controls.form;

/**
 * <pre>
 * This abstract class is intended to be the base for typical ButtonBar Listener. It handles the common issues
 * of Create Read Update Delete processing. It is independent from a specific persistence model. 
 * When subclassing it you have to implement the following methods
 *  
 * public Object newDelegate();
 * public boolean refresh(Object delegate);
 * public int save();
 * public void postSave(); 
 * 
 * In order to customize the Messages you have to provide the following properties in your application.resources
 * 
 *  common.crud.tooltip.override
 *  common.crud.tooltip.abort
 *  common.crud.button.abort
 *  common.crud.button.override
 *  common.crud.concurrent.message
 * </pre>
 *  
 * @author Peter
 */
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.BaseControl;
import org.webguitoolkit.ui.controls.dialog.ConfirmationDialog;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.util.TextService;

public abstract class AbstractButtonBarListener implements IButtonBarListener, IServerEventListener {

	private static final String OVERRIDE_TOOLTIP = "common.crud.tooltip.override@Data of other user will be lost.";
	private static final String ABORT_TOOLTIP = "common.crud.tooltip.abort@Your changes are lost, data reloaded.";
	private static final String ABORT_BUTTON_LABEL = "common.crud.button.abort@Abort";
	private static final String OVERRIDE_BUTTON_LABEL = "common.crud.button.override@Override";
	public static final int SAVE_OK = 0;
	public static final int SAVE_ERROR = -2;
	public static final int SAVE_CONCURRENT_MODIFICATION = -1;

	private final String CONCURRENT_MODIFICATION_MESSAGE = "common.crud.concurrent.message@Another user has changed the data you have been working on.\nDo you want to override the changes or abort your editing?";
	protected ICompound compound;
	private IDataBag lastEditBag;
	private int modeBeforeAction;

	public AbstractButtonBarListener(ICompound compound) {
		this.compound = compound;
		compound.registerLoadListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserve.gui.form.ICrudListener#onCancel(com.endress.infoserve.gui.event.Event)
	 */
	public void onCancel(ClientEvent event) {
		modeBeforeAction = compound.getMode();
		preCancel();
		compound.getBag().undo(); // undo changed within bag
		compound.load(); // transfer old values to UI
		compound.changeElementMode(Compound.MODE_READONLY);
		postCancel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserve.gui.form.ICrudListener#onDelete(com.endress.infoserve.gui.event.Event)
	 */
	public void onDelete(ClientEvent event) {
		modeBeforeAction = compound.getMode();
		preDelete();
		if (compound.getBag() != null)
			// delete returns false, if there is an error
			// in case of an error immediately return
			// error handling has to be done by your own in delete
			if (!delete(compound.getBag().getDelegate())) {
				return;
			}
		compound.changeElementMode(Compound.MODE_READONLY);
		postDelete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserve.gui.form.ICrudListener#onEdit(com.endress.infoserve.gui.event.Event)
	 */
	public void onEdit(ClientEvent event) {
		modeBeforeAction = compound.getMode();
		preEdit();
		IDataBag bag = compound.getBag();
		// Implicitly perform an refresh of the underlying object to prevent
		// from concurrent modifications
		if (bag != null) {
			bag.undo();
			if (bag.getDelegate() != null)
				refresh(bag.getDelegate());
			compound.load();
			compound.changeElementMode(Compound.MODE_EDIT);
		}
		else if (bag == null)
			throw new WGTException("There is no Bag in the Compound");
		postEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserve.gui.form.ICrudListener#onNew(com.endress.infoserve.gui.event.Event)
	 */
	public void onNew(ClientEvent event) {
		modeBeforeAction = compound.getMode();
		preNew();
		compound.setBag(WebGuiFactory.getInstance().createDataBag(null));
		compound.load();
		compound.changeElementMode(Compound.MODE_NEW);
		postNew();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.endress.infoserve.gui.form.ICrudListener#onSave(com.endress.infoserve.gui.event.Event)
	 * 
	 * Will call the save() method that the subclasser provides.
	 */
	public void onSave(ClientEvent event) {
		modeBeforeAction = compound.getMode();
		preSave();

		// should be checked after preSave too, because compound.save() clears errors
		// and preSave may has set errors
		if (compound.hasErrors())
			return;

		compound.save();

		validate(compound.getBag());

		// mandatory checks are reported by caller
		if (compound.hasErrors())
			return;

		// throw new WGTException("Save faild due to reported errors from compound");

		// when new object is needed create one
		if (compound.getMode() == Compound.MODE_NEW && compound.getBag().getDelegate() == null) {
			Object o = newDelegate();
			compound.getBag().setDelegate(o);
		}
		// mandatory checks are reported by caller
		if (compound.hasErrors())
			return;

		// no errors try saving to delegate and commit
		compound.getBag().save();

		// should be have a load here, since the backend may change value when
		// transported into it? (new Objects: default values?)
		compound.load();

		// 
		switch (persist()) {
			case SAVE_OK:
				break;
			case SAVE_CONCURRENT_MODIFICATION:
				concurrentModificationHandling();
			case SAVE_ERROR:
				return;
			default:
				break;
		}

		// do special post processing on save
		postSave();
		compound.changeElementMode(Compound.MODE_READONLY);
	}

	/**
	 * Do whatever is needed to persist changes that have been made on the delegate
	 * 
	 * @return SAVE_OK when everything is fine. SAVE_CONCURRENT_MODIFICATION if a concurrent modification was detected.
	 */
	public abstract int persist();

	/**
	 * Perform pre onSave() actions if needed. Default implementation is doing nothing.
	 */
	public void preSave() {
	}

	/**
	 * Perform post onSave() actions if needed. Default implementation is doing nothing.
	 */
	public void postSave() {
	}

	/**
	 * Perform pre onEdit() actions if needed. Default implementation is doing nothing.
	 */
	public void preEdit() {
	}

	/**
	 * Perform post onEdit() actions if needed. Default implementation is doing nothing.
	 */
	public void postEdit() {
	}

	/**
	 * Perform pre onNew() actions if needed. Default implementation is doing nothing.
	 */
	public void preNew() {
	}

	/**
	 * Perform post onNew() actions if needed. Default implementation is doing nothing.
	 */
	public void postNew() {
	}

	/**
	 * Perform pre onCancel() actions if needed. Default implementation is doing nothing.
	 */

	public void preCancel() {
	}

	/**
	 * Perform post onCancel() actions if needed. Default implementation is doing nothing.
	 */
	public void postCancel() {
	}

	/**
	 * Perform pre onCancel() actions if needed. Default implementation is doing nothing.
	 */

	public void preDelete() {
	}

	/**
	 * Perform post onCancel() actions if needed. Default implementation is doing nothing.
	 */
	public void postDelete() {
	}

	/**
	 * Overwrite this method for custom input validation like comparing passwords
	 * 
	 * @param dataBag the bag with the values to validate
	 */
	public void validate(IDataBag dataBag) {
	}

	/**
	 * Refresh the content of the passed delegate by means of the underlying persistence model. This method is being called from
	 * within onEdit()
	 * 
	 * @param delegate
	 * @return
	 */
	public abstract boolean refresh(Object delegate);

	/**
	 * Delete the delegate depending on your persistence strategy persistence model
	 * 
	 * @param delegate
	 * @return
	 */
	public abstract boolean delete(Object delegate);

	/**
	 * Create a new delegate object to be maintained by this form. This method will be called whenever the new button is pressed.
	 * 
	 * @return the new instance of the delegate
	 */
	public abstract Object newDelegate();

	/**
	 * The default handling for concurrent modification exceptions
	 */
	public void concurrentModificationHandling() {
		// Data has been changed by another user
		final ConfirmationDialog dialog = new ConfirmationDialog(((BaseControl)compound).getPage());
		dialog.setWindowTitle("Concurrency Conflict.");
		dialog.setMsg(TextService.getString(CONCURRENT_MODIFICATION_MESSAGE));
		dialog.addButton(OVERRIDE_BUTTON_LABEL, OVERRIDE_TOOLTIP, new IActionListener() {
			public void onAction(ClientEvent event) {
				refresh(compound.getBag().getDelegate());
				preSave();
				compound.save();
				compound.getBag().save();
				persist();
				dialog.destroy();
				compound.changeElementMode(Compound.MODE_READONLY);
				postSave();
			}
		});
		dialog.addButton(ABORT_BUTTON_LABEL, ABORT_TOOLTIP, new IActionListener() {

			public void onAction(ClientEvent event) {
				final IDataBag bag = compound.getBag();
				bag.undo();
				refresh(compound.getBag().getDelegate());
				compound.load(); // load and show the new data
				// stay in edit mode
				dialog.destroy();
			}
		});
		dialog.show();
	}

	/**
	 * This method is being called if the surrounding compound has been reloaded (compound.load())
	 */
	public void handle(ServerEvent event) {
		// make an implicit cancel on the last bad, good that we save the last
		// bag when
		// going into edit mode.
		if (lastEditBag != null && lastEditBag != compound.getBag() && (compound.getMode() != Compound.MODE_READONLY)) {
			lastEditBag.undo(); // only do this if we are loading another object
			// (not our selves)
			// change to readonly
			compound.changeElementMode(Compound.MODE_READONLY);
		}

	}

	protected int getModeBeforeAction() {
		return modeBeforeAction;
	}
}
