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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WGTException;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.ActionControl;
import org.webguitoolkit.ui.controls.IBaseControl;
import org.webguitoolkit.ui.controls.contextmenu.IContextMenu;
import org.webguitoolkit.ui.controls.dialog.DialogUtil;
import org.webguitoolkit.ui.controls.event.IServerEventListener;
import org.webguitoolkit.ui.controls.event.ListenerManager;
import org.webguitoolkit.ui.controls.event.ServerEvent;
import org.webguitoolkit.ui.controls.layout.ILayout;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.Tooltip;
import org.webguitoolkit.ui.controls.util.style.Style;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;


/**
 * <pre>
 * A compound is a container for a set of FormControls, it provides easy methods for setting
 * and retrieving the values of its FromControls.
 * It can be compared to a FORM element in HTML.
 * 
 * It is not displayed but is automatically added to all FormControls underneath in 
 * the component tree, but you can also add FormControls "by hand" when using 
 * formControl.setCompound(theCompound).
 * 
 * All FormControls state can be controlled by the compound by using the changeElementMode( Compound.MODE_... ).
 * 
 * The ButtonBar can be used very easily with the compound, in the ButtonBarListener you have to implement on...
 * methods in the following way:
 * 
 * on...( ClientEvent event ){
 * 		ButtonBar buttonBar = (ButtonBar)event.getSource();
 * 		buttonBar.surroundingCompound().changeElementMode( Compound.MODE_... );
 * }
 * 
 * </pre>
 */
public class Compound extends ActionControl implements ICompound {

	private static final long serialVersionUID = 1L;

	protected ICompoundModel model;
    /**
     * manages all event fired by this compound. which is by now only the load event 
     *(someone called load()-method)
     */
    protected ListenerManager serverListener = new ListenerManager();

    // all elements inside this compound. they are of type ICompoundCycle
    // maybe even formControl
    protected Collection<ICompoundLifecycleElement> cycleElements = new HashSet<ICompoundLifecycleElement>();

    protected List<String[]> errorList;

    private String errorDialogName;
    
	public Compound() {
        super();
    }
	public Compound(String id) {
		super(id);
	}

    protected void endHTML(PrintWriter out) {
        // has no HTML equivalent
    }
    /**
     * adds the Element to the list of this compound to
     * be included in the lifecycle of this compound.
     * This is typically an Textfield or the like.
     * @param formi
     */
    public void addElement(ICompoundLifecycleElement formi) {
        cycleElements.add(formi);
    }

    public boolean removeElement(ICompoundLifecycleElement formi) {
        return cycleElements.remove(formi);
    }

    protected void removeInternal() {
    	getPage().removeListener( ServerEvent.EVENT_POSTDISPATCH, errorOutHandler);
		super.removeInternal();
		
		// avoid circular references
		serverListener = new ListenerManager();
	}
	public ICompoundModel getModel() {
        if (model == null) {
           model = new DefaultCompoundModel();
        }
        return model;
    }

    /**
     * clears all elements in the compound
     */
    public void clearElements() {
        for (ICompoundLifecycleElement element : cycleElements ) {
        	element.loadFrom(null);
        }
    }

    /**
     * method to be called to load the compound object
     */
    public void load() {
        // push data into my subs
        Object dataObject = getModel().getData();
        load(dataObject);
        // inform every page about the load mechanism.
        // must be after the compound is loaded, so the listeners
        // can access the new data. 
        clearErrors();
    }

    /**
     * load the dataObject into the context to transport it to the client
     * 
     * @param dataObject the data object
     */
    public void load(Object dataObject) {
        // go through children and pass them the data
        for (ICompoundLifecycleElement element : cycleElements ) {
            if( !(element instanceof org.webguitoolkit.ui.controls.table.Table ) )
            	element.loadFrom(dataObject);
        }
        fireServerEvent(new ServerEvent(this, EVENT_COMPOUND_LOAD ));
    }

    /**
     * default behavior while loading the compound, is to load the data from
     * the model into the context. That means that the Model is created and must
     * retrieve the Data Object from that object the properties are inquired by
     * reflection and the values set to the context.
     * 
     */
    protected void init() {
    	getPage().registerListener( ServerEvent.EVENT_POSTDISPATCH, errorOutHandler );
    }

    /**
     * this must be called from the (action listener) of some button to be
     * executed it is not called automatically. it save the context to the
     * dataObject(s) if possible, that a formElement is not bound to a
     * data object not action is taken. (you must save those item yourself)
     * 
     */
    public void save() {
        Object dataObject = getModel().getData();
        saveTo(dataObject);
    }

    /**
     * saves the data in the context into the given dataObject. The dataObject
     * may have associations to other objects, which are used depending on the
     * property-settings of the form elements.
     * 
     * @param dataObject the DataBag or object to save to
     */
    public void saveTo(Object dataObject) {
    	clearErrors();
        // go through children and pass them the data
        for (ICompoundLifecycleElement element : cycleElements ) {
            element.saveTo(dataObject);
        }
    }

    /**
     * adds an error message to the output. the property parameter can be null, if
     * no specific element if affected. If not null the label of that element is
     * taken to prepend the message
     */
    public void addError(String msg, String property) {
       if (property != null && findElementByProperty(property) == null) {
            throw new WGTException("Property " + property + " not found. ");
        }
        if (errorList == null)
            errorList = new ArrayList<String[]>(3);
        errorList.add(new String[] { msg, property });
    }

    public void addError(String msg) {
        addError(msg, null);
    }

    /**
     * clear all errors on client side. This must be called from the
     * application, as we don't know when this should take place (in absence)
     */
    public void clearErrors() {  
        for (ICompoundLifecycleElement element : cycleElements ) {
            element.clearError();
        }
        errorList = new ArrayList<String[]>(0); // indicates to delete all errors
    }

    public FormControl findElementByProperty(String prop) {
        if (prop == null)
            return null;
        for (ICompoundLifecycleElement element : cycleElements ) {
            if ((element instanceof FormControl)
                    && prop.equals(((FormControl) element).getProperty())) {
                return (FormControl) element;
            }
        }
        return null;
    }
    
    /**
     * check if there where any errors.
     */
    public boolean hasErrors() {
        return (errorList != null && !errorList.isEmpty());
    }

    /**
     * Eventhandler for internal events, handling the output of errors The error
     * array, can have null values for the property.
     */
    public IServerEventListener errorOutHandler = new IServerEventListener() {

		private static final long serialVersionUID = 1L;

		public void handle(ServerEvent event) {
			// serveral states for the variable errorList
			// null: no changes to errors on client side
			// empty array: clear the errors
			// filled array: show the errors.
			if (errorList == null) {
				return;
			}
			if (errorList.isEmpty()) {
				errorList = null;
				return;
			}
			// show errors inside the html
			// there is no msgAreaId
			StringBuffer msg = new StringBuffer(30);
			for (int i=0;i<errorList.size();i++) {
				String[] errorObj = (String[]) errorList.get(i);
				String prop = errorObj[1];
				if (prop != null) {
					FormControl fc = findElementByProperty(prop); // errorObj 1 is
					// the property
					msg.append( "<i>" + ((fc.getDescribingLabel() == null) ? "" : fc
							.getDescribingLabel().getText()
							+ "</i> "));
					fc.showError();					
				}
				msg.append(  TextService.getString( errorObj[0] ) + "<br>");
			}
			// we have to use the sendMessageDirectly cause we are in already in the handler queue
			DialogUtil.sendMessageDirectly( Compound.this.getPage(), msg.toString(), DialogUtil.ERROR, getId(), null, getErrorDialogName() );
			
			errorList = null;
		}
	};

    /**
	 * change the read only mode of all sub components, the component may object
	 * with its setting in no edit
	 * 
	 * @param mode the new mode
	 */
    public void changeElementMode(int mode) {
        for (ICompoundLifecycleElement element : cycleElements ) {
        	element.changeMode(mode);
        }
        getContext().add(getId() + ".mode", mode, IContext.TYPE_VAL,
                IContext.STATUS_SERVER_ONLY);
    }

    /**
	 * returns the mode which was last set using chgAllMode
	 * 
	 * @return the mode or Integer.MIN_VALUE if not set.
	 */
    public int getMode() {
        return getContext().getValueAsInt(getId() + ".mode");
    }

    /**
     * This methods is a convenient method if you use the compound in the
     * following (recommended) way: The model is a default model which holds a
     * DataBag instance as modelObject.
     * 
     * The method will return the dataBag, which in the object of the model It
     * will create such a bad is it didn't exist before
     * 
     * @return the DataBag, if null a new DataBag is created and set to the DefaulCompoundModel
     */
    public IDataBag getBag() {
        IDataBag bag = (IDataBag) getModel().getData();
        if (bag == null) {
            bag = WebGuiFactory.getInstance().createDataBag(null);
            ((DefaultCompoundModel) getModel()).setData(bag);
        }
        return bag;
    }

    public void setBag(IDataBag bag) {
        ((DefaultCompoundModel) getModel()).setData(bag);
    }

    /**
	 * @return the errorDialogName
	 */
	public String getErrorDialogName() {
		return errorDialogName;
	}
	/**
	 * @param newErrorDialogName the errorDialogName to set
	 */
	public void setErrorDialogName(String newErrorDialogName) {
		this.errorDialogName = newErrorDialogName;
	}
	
	public List<String[]> getErrorList() {
        return errorList;
    }

	public void fireServerEvent(ServerEvent event) {
		serverListener.fireServerEvent(event);
	}

	/**
	 * @see ICompound#registerLoadListener(IServerEventListener)
	 * @param liz the load listener
	 */
	public void registerLoadListener( IServerEventListener liz) {
		serverListener.registerListener( EVENT_COMPOUND_LOAD, liz);
	}

	public void registerListener(int eventtype, IServerEventListener liz) {
		serverListener.registerListener(eventtype, liz);
	}

	public void removeListener(int eventtype, IServerEventListener liz) {
		serverListener.removeListener(eventtype, liz);
	}

	public void add(IBaseControl child) {
		super.add(child);
	}
	
	public void setVisible(boolean vis) {
		//not supported by compound
	}

	/**
	 * sets the layout for the controls displayed in the tab (GridLayout, BorderLayout, ... )
	 * @param layout the layout to be set
	 */
	public void setLayout( ILayout layout ){
		super.setLayout( layout );
	}
	
	public List<String[]> validate(){
		List<String[]> result = new ArrayList<String[]>();
		for( ICompoundLifecycleElement element : cycleElements ){
			if( element instanceof IFormControl ){
				FormControl formControl = (FormControl)element;
				try {
					formControl.validate();
				}
				catch (ValidationException e) {
					result.add( new String[]{e.getMessage(), formControl.getProperty()} );
				}
			}
		}
		return result;
	}

	@Override
	public void setStyle(Style style) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void setDefaultCssClass(String baseCssClass) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void setCssClass(String cssClass) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void setTooltip(String tooltip) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void setTooltipKey(String tooltipKey) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void addContextMenu(IContextMenu contextMenu) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void setTooltip(Tooltip tooltip) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	public void setTooltipAdvanced(Tooltip tooltipAdvanced) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}
	@Override
	protected void setAttribute(String attributeName, String attributeValue) {
		throw new WGTException("A Compound has no HTML aquivalent, hence calling this method is invalid.");
	}

}
