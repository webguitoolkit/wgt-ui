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
package org.webguitoolkit.ui.controls.event;

import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.controls.IBaseControl;


/**
 * Events, fired on user actions on the GUI, evaluated by the components and<br>
 * processed by the Listeners.<br>
 * A client event contains the control that fired the event, 
 */

public class ClientEvent implements IEvent {
    // TYPES of EVENTS:
    protected static int e = 0; // event number counter;
    public static final int TYPE_ACTION = e++;
    
    
    public static final String EVENT_PARAMETER = ".ev.p";
    public static final String EVENT_PARAMETER_SIZE = ".ev.size";
    IBaseControl source;
    String sourceId;
    String type;
    ContextElement[] chgContext;
    /**
     * @param source the control that fires the event
     * @param sourceId the id of that control
     * @param type the event type
     * @param chgContext the context changes
     */
    public ClientEvent(IBaseControl source, String sourceId, String type, ContextElement[] chgContext) {
        this.source = source;
        this.sourceId = sourceId;
        this.type = type;
        this.chgContext = chgContext;
    }
	/**
	 * @return Returns the chgContext.
	 */
    public ContextElement[] getChgContext() {
        return chgContext;
    }
    public boolean hasChanged(String cssId) {
    	if (cssId==null) return true;
    	for (int i = 0; i < chgContext.length; i++) {
			if (cssId.equals(chgContext[i].getCssId())) return true;
		} 
    	return false;
    }
    /**
	 * @see com.endress.infoserve.wgt.controls.event.IEvent#getControl()
	 */
    public IBaseControl getSource() {
        return source;
    }
    /**
	 * @see com.endress.infoserve.wgt.controls.event.IEvent#getControlId()
	 */
    public String getSourceId() {
        return sourceId;
    }

    /**
	 * @return Returns the type.
	 */
    public String getType() {
        return type;
    }
    /**
	 * @return Returns the type as an integer.
	 */
    public int getTypeAsInt() {
        // TODO Exceptionhandling
        return Integer.parseInt(type);
    }

    /**
	 * An javascript event can be augmented by parameters, see JSUtil.jsEventParam()
	 * these parameters can be retrieved here.
	 * The symantics of the parameters is up to the event, see the eventdefinition in the component
	 * (They are passed as normal context item but will not be send back to the client)
	 * 
	 * @param i the index of the parameter
	 * @return the parameter
	 */
    public String getParameter(int i) {
        return source.getPage().getContext().getValue(sourceId+EVENT_PARAMETER+i);
    }

    /**
     * @return the parameter count
     */
    public int getParameterSize() {
        return source.getPage().getContext().getValueAsInt(sourceId+EVENT_PARAMETER_SIZE );
    }
    
    public String toString() {
    	return " Source: "+sourceId + " Type: "+type +
    	" Component id: "+source.getId() +
    	" Component type: "+ source.getClass().getName();
    }
}
