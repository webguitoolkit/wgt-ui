package org.webguitoolkit.ui.html.definitions;

import java.util.HashSet;
import java.util.Set;

/**
 * @author i01002455
 * 
 *         based on http://de.selfhtml.org/
 * 
 */
public class RuleSet {
	private Set<HTMLAttribute> allowedAttributes = null;
	private Set<HTMLElement> allowedChildren= null;
	private ElementType[] parentTypes = null;
	private HTMLElement tag = null;

	public RuleSet(ElementType[] allowedParentTags, HTMLAttribute[] allowedAttribs) {
		parentTypes = allowedParentTags;
		if (allowedAttribs != null) {
			for (int i = 0; i < allowedAttribs.length; i++) {
				addAllowedAttribute(allowedAttribs[i]);
			}
		}
	}

	private void addUniversalAttributes() {
		String name = tag.name();

		if (!name.equals("base") && !name.equals("basefont") && !name.equals("head") && !name.equals("html") && !name.equals("meta")
				&& !name.equals("param") && !name.equals("script") && !name.equals("style") && !name.equals("title")) {
			addAllowedAttribute(HTMLAttribute.classAttribute);
			addAllowedAttribute(HTMLAttribute.style);
			addAllowedAttribute(HTMLAttribute.title);
		}

		if (!name.equals("base") && !name.equals("head") && !name.equals("html") && !name.equals("meta") && !name.equals("script")
				&& !name.equals("style") && !name.equals("title")) {
			addAllowedAttribute(HTMLAttribute.id);
		}
		if (!name.equals("applet") && !name.equals("base") && !name.equals("basefont") && !name.equals("br") && !name.equals("frame")
				&& !name.equals("frameset") && !name.equals("hr") && !name.equals("iframe") && !name.equals("param") && !name.equals("script")) {
			addAllowedAttribute(HTMLAttribute.dir);
			if (!name.equals("meta"))
				addAllowedAttribute(HTMLAttribute.lang);
		}
		if (!name.equals("applet") && !name.equals("base") && !name.equals("basefont") && !name.equals("bdo") && !name.equals("br")
				&& !name.equals("font") && !name.equals("frame") && !name.equals("frameset") && !name.equals("head") && !name.equals("html")
				&& !name.equals("iframe") && !name.equals("isindex") && !name.equals("param") && !name.equals("script") && !name.equals("style")
				&& !name.equals("title")) {
			addAllowedAttribute(HTMLAttribute.onclick);
			addAllowedAttribute(HTMLAttribute.ondblclick);
			addAllowedAttribute(HTMLAttribute.onmousedown);
			addAllowedAttribute(HTMLAttribute.onmouseup);
			addAllowedAttribute(HTMLAttribute.onmouseover);
			addAllowedAttribute(HTMLAttribute.onmousemove);
			addAllowedAttribute(HTMLAttribute.onmouseout);
			addAllowedAttribute(HTMLAttribute.onkeypress);
			addAllowedAttribute(HTMLAttribute.onkeydown);
			addAllowedAttribute(HTMLAttribute.onkeyup);
		}
	}

	private void addAllowedAttribute(HTMLAttribute attribute) {
		if (null == allowedAttributes)
			allowedAttributes = new HashSet<HTMLAttribute>();

		allowedAttributes.add(attribute);
	}

	private void addAllowedChild(HTMLElement child) {
		if (null == allowedChildren)
			allowedChildren = new HashSet<HTMLElement>();
		
		allowedChildren.add(child);
	}

	public boolean isAttributePossible(HTMLAttribute attribute) {
		if (null == allowedAttributes)
			return false;
		if (allowedAttributes.contains(attribute))
			return true;
		else
			return false;
	}
	
	public boolean isChildValid(HTMLElement tag) {
//		if(parentTypes!=null){
//			//check if allowed for any of Block / Inline
//			return true;
//		}
		return true;
	}

	public HTMLElement getTag() {
		return tag;
	}

	public void setTag(HTMLElement tag) {
		this.tag = tag;
		addUniversalAttributes();
	}

}
