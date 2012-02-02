package org.webguitoolkit.ui.html.definitions;

public class TagNotValidException extends Exception {

	private static final long serialVersionUID = 2948616072062452926L;

	public TagNotValidException(HTMLElement parent, HTMLElement child){
		super("Tag '" + child.name() + "' not allowed as child of tag '" + parent.name()+"'");
	}

}
