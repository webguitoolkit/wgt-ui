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
package org.webguitoolkit.ui.controls.table;

import java.io.Serializable;

public class ColumnSetting implements Serializable {
	private static final String SEPARATOR = ",";

	private String property = null;
	private String width = null;

	public ColumnSetting(String asString) {
		String[] artifacts = asString.split(SEPARATOR);
		if (artifacts.length > 0) {
			property = artifacts[0].trim();
			if (artifacts.length > 1) {
				width = artifacts[1].trim();
			}
		}
	}

	public ColumnSetting(String property, String width) {
		this.property = property;
		this.width = width;
	}

	@Override
	public String toString() {
		return property + SEPARATOR + width;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
}