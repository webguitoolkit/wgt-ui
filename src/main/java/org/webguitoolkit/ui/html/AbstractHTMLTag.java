package org.webguitoolkit.ui.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.Format;
import org.webguitoolkit.ui.html.definitions.AttributeNotValidException;
import org.webguitoolkit.ui.html.definitions.HTMLAttribute;
import org.webguitoolkit.ui.html.definitions.HTMLElement;
import org.webguitoolkit.ui.html.definitions.TagNotValidException;
import org.webguitoolkit.ui.html.definitions.Version;

/**
 * @author i102455
 * 
 */
public abstract class AbstractHTMLTag {
	private Element this_ = null;
	private Format format;
	private Version type;
	private HTMLElement tag;

	public AbstractHTMLTag(HTMLElement element, Version type) {
		init(element, null, null, type);
	}

	public AbstractHTMLTag(HTMLElement element, HTMLTag parent, Version type) {
		init(element, parent, null, type);
	}

	public AbstractHTMLTag(HTMLElement element, HTMLTag parent, String content, Version type) {
		init(element, parent, content, type);
	}

	private void init(HTMLElement name, HTMLTag parent, String content, Version type) {
		this.type = type;
		this_ = new Element(name.getName(type));
		this.tag = name;
		setParent(parent);
		setContent(content);
	}

	public void setParent(HTMLTag parent) {
		if (parent == null)
			return;
		// check ruleset
		AbstractHTMLTag p = ((AbstractHTMLTag)parent);
		if (p.tag.isValidChild(this.tag)) {
			p.getElement().addContent(this_);
		}
		else {
			try {
				throw new TagNotValidException(p.tag, this.tag);
			}
			catch (TagNotValidException e) {
				e.printStackTrace();
			}
		}

	}

	public void addAttribute(HTMLAttribute attribute, String value) {
		// check ruleset
		if (null != value)
			this_.setAttribute(attribute.getName(type), value);
		if (!tag.isValidAttribute(attribute)) {
			try {
				throw new AttributeNotValidException(tag, attribute);
			}
			catch (AttributeNotValidException e) {
				Logger.getLogger(this.getClass()).warn(e);
			}
		}
	}
	
	
	public void removeAttribute(HTMLAttribute attribute) {
			this_.removeAttribute(attribute.getName(type));
	}

	public void setContent(String value) {
		this_.addContent(value);
	}

	public void setStyle(String style) {
		this.addAttribute(HTMLAttribute.style, style);
	}

	public void setId(String id) {
		this.addAttribute(HTMLAttribute.id, id);
	}

	public void setName(String name) {
		this.addAttribute(HTMLAttribute.name, name);
	}

	public void setTitle(String title) {
		this.addAttribute(HTMLAttribute.title, title);
	}

	public void setCssClass(String cssClass) {
		this.addAttribute(HTMLAttribute.classAttribute, cssClass);
	}

	public String output() {
		HTMLOutputter x = new HTMLOutputter(getFormat());		
		return x.outputString(this_);
	}

	public void output(OutputStream out) {
		HTMLOutputter x = new HTMLOutputter(getFormat());
		try {
			x.output(this_, out);
		}
		catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}
	}
	
	public void output(Writer out) {
		HTMLOutputter x = new HTMLOutputter(getFormat());
		try {
			x.output(this_, out);
		}
		catch (IOException e) {
			Logger.getLogger(this.getClass()).error(e);
		}		
	}


	private Format getFormat() {
		if (format == null)
			format = Format.getCompactFormat();
		return format;
	}

	public void add(AbstractHTMLTag newElement) {
		// check ruleset
		if (newElement.tag.isValidChild(newElement.tag)) {
			this_.addContent(newElement.getElement());
		}
		else {
			try {
				throw new TagNotValidException(tag, newElement.tag);
			}
			catch (TagNotValidException e) {
				e.printStackTrace();
			}
		}
	}

	protected Element getElement() {
		return this_;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

}
