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
package org.webguitoolkit.ui.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.SequentialTableLayout;

/**
 * @author i102415
 *
 */
public class ManifestPage extends Page {

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.Page#pageInit()
	 */
	@Override
	protected void pageInit() {
		
		addWgtCSS("standard_theme.css");
		
		setLayout( new SequentialTableLayout() );
		
		ILabel headerLabel = getFactory().createLabel(this, "manifest.info@Manifest File");
		headerLabel.addCssClass("wgtLabelFor");
		headerLabel.setLayoutData( SequentialTableLayout.getLastInRow() );
		
		String path = Page.getServletRequest().getSession().getServletContext().getRealPath( "/META-INF/MANIFEST.MF" );
		FileInputStream is = null;
		try {
			is = new FileInputStream(path);
			Manifest mf = new Manifest( is );
			Set<Object> keys = mf.getMainAttributes().keySet();
			for( Object key: keys ){
				Name keyName = (Name)key;
				String value = mf.getMainAttributes().getValue( keyName );
				ILabel keyLabel = getFactory().createLabel(this, "");
				keyLabel.setText(keyName.toString()+":");
				keyLabel.addCssClass("wgtLabelFor");
				ILabel valueLabel = getFactory().createLabel(this, "");
				valueLabel.setText( value );
				valueLabel.setLayoutData( SequentialTableLayout.getLastInRow() );
			}
		}
		catch (FileNotFoundException e) {
			sendError("No Manifest file found"); 
		}
		catch (IOException e) {
			sendError( e.getMessage() );
		}
		finally{
			if( is != null){
				try {
					is.close();
				}
				catch (IOException e) {
					sendError( e.getMessage() );
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.webguitoolkit.ui.controls.Page#title()
	 */
	@Override
	protected String title() {
		return "Manifest info";
	}

}
