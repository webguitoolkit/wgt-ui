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
package org.webguitoolkit.ui.util.export;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.webguitoolkit.ui.controls.table.Table;

/**
 * @author i102415
 *
 */
public interface ITableExport {
	public static final String EXPORT_TYPE_XML = "XML";
	public static final String EXPORT_TYPE_EXCEL = "EXCEL";
	public static final String EXPORT_TYPE_PDF = "PDF";
	public static final String EXPORT_TYPE_CSV = "CSV";
	public static final String EXPORT_TYPE_WORD = "WORD";

	public boolean canHandle(String format);
	public void writeTo(Table table, HttpServletResponse response );
	public void writeTo(Table table, OutputStream out );
}
