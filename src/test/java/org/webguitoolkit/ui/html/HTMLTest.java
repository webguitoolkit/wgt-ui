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
package org.webguitoolkit.ui.html;

import junit.framework.TestCase;

import org.webguitoolkit.ui.html.definitions.HTMLAttribute;

/**
 * @author i01002455
 * 
 */
public class HTMLTest extends TestCase {
	public void testHTML() {
		XHTMLTagFactory factory = XHTMLTagFactory.getInstance();

		HTMLTag html = factory.newHtml(null);
		HTMLTag head = factory.newHead(html);
		HTMLTag meta = factory.newMeta(head);
		meta.addAttribute(HTMLAttribute.httpEquiv, "pragma");
		meta.addAttribute(HTMLAttribute.content, "no-cache");
		HTMLTag body = factory.newBody(html, "body.id");
		HTMLTag style = factory.newStyle(body);
		style.addAttribute(HTMLAttribute.type, "text/css");
		style.setContent(".anyclass {text-align:center !important;}");

		HTMLTag table = factory.newTable(body, "table.id");
		HTMLTag row = factory.newTr(table, "table.row1.id");
		factory.newTd(row, "table.row1cell1.id", "1.1");
		factory.newTd(row, "table.row1cell2.id", "1.2");
		factory.newTd(row, "table.row1cell3.id", "1.3");
		row = factory.newTr(table, "table.row1.id");
		factory.newTd(row, "table.row2cell1.id", "2.1");
		factory.newTd(row, "table.row2cell2.id", "2.2");
		factory.newTd(row, "table.row2cell3.id", "2.3");
		row = factory.newTr(table, "table.row1.id");
		factory.newTd(row, "table.row3cell1.id", "3.1");
		factory.newTd(row, "table.row3cell2.id", "3.2");
		HTMLTag col = factory.newTd(row, "table.row3cell3.id");
		HTMLTag span1 = factory.newSpan(col, null, "3.3");

		// logs a warning with log4j enabled
		span1.addAttribute(HTMLAttribute.href, "http://somewhere.com");

		HTMLTag link = factory.newA(body, null);
		link.addAttribute(HTMLAttribute.id, "link.id");
		link.setCssClass("anyclass");
		link.addAttribute(HTMLUtil.getHTMLAttribute("href"), "http://somewhere.com");
		link.addAttribute(HTMLAttribute.target, "_blank");
		link.setContent("Link to somewhere");

		link = factory.newA(body, "link2.id");
		link.addAttribute(HTMLUtil.getHTMLAttribute("href"), "http://somewhereelse.com");
		link.addAttribute(HTMLAttribute.target, "_blank");
		link.setContent("Link to somewhere else");
		
		HTMLTag list = factory.newUl(body, null);
		factory.newLi(list, null,"1>2");
		factory.newLi(list, null,"3<2");
		factory.newLi(list, null,"This & That");
		
		factory.newBr(body, null);

//		html.setFormat(Format.getPrettyFormat());
//		html.output(System.out);

		assertNotNull("No output available", html.output());
	}
}
