package org.webguitoolkit.ui.html;

import java.io.OutputStream;
import java.io.Writer;

import org.jdom.output.Format;
import org.webguitoolkit.ui.html.definitions.HTMLAttribute;

public interface HTMLTag {

	void setParent(HTMLTag parent);

	void addAttribute(HTMLAttribute attribute, String value);

	void removeAttribute(HTMLAttribute attribute);

	void setContent(String value);

	void setStyle(String style);

	void setId(String id);

	void setTitle(String title);
	
	void setName(String name);

	void setCssClass(String cssClass);

	String output();

	void output(OutputStream out) ;
	
	void output(Writer out) ;

	void add(AbstractHTMLTag element);

	void setFormat(Format format);

}
