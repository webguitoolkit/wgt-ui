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
/*
 */
package org.webguitoolkit.ui.controls.form;

import org.webguitoolkit.ui.base.WebGuiFactory;

/**
 * <pre>
 * stores the content of the Model for the component, so the 
 * application programmer can use it in a push fashion.
 * </pre>
 * 
 * @author arno
 */
public class DefaultCompoundModel implements ICompoundModel {
    // the data the be storeed for the application
    Object data = WebGuiFactory.getInstance().createDataBag(null);
    public DefaultCompoundModel() {
        super();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
