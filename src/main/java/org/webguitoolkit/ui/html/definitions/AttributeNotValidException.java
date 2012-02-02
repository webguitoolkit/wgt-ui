package org.webguitoolkit.ui.html.definitions;

public class AttributeNotValidException extends Exception {

	private static final long serialVersionUID = 2948616072062452926L;

	public AttributeNotValidException(HTMLElement tag, HTMLAttribute attrib){
		super("Attribute '" + attrib.name() + "' not allowed for tag '" + tag.name()+"'");
	}

}
