package org.webguitoolkit.ui.html;

import org.webguitoolkit.ui.html.definitions.HTMLElement;
import org.webguitoolkit.ui.html.definitions.Version;


public class XHTMLTag extends AbstractHTMLTag implements HTMLTag {
	public XHTMLTag(HTMLElement element) {
		super(element, Version.XHTML);
	}

	public XHTMLTag(HTMLElement element, HTMLTag parent) {
		super(element, parent, Version.XHTML);
	}

	public XHTMLTag(HTMLElement element, HTMLTag parent, String content) {
		super(element, parent, content, Version.XHTML);
	}
}
