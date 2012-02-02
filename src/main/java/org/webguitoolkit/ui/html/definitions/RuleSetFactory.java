package org.webguitoolkit.ui.html.definitions;


public class RuleSetFactory {

	// singleton, there is only one instance of this class.
	protected static RuleSetFactory instance;

	/**
	 * singleton pattern, use this method to get a instance of the factory
	 * 
	 * @return a instance of the factory
	 */
	public static RuleSetFactory getInstance() {
		if (instance == null) {
			// sounds strange to make an instance of an abstract class.
			instance = new RuleSetFactory();
		}
		return instance;
	}

	/**
	 * The instance can be changed
	 * 
	 * @param instance new instance
	 */
	public static void setInstance(RuleSetFactory instance) {
		RuleSetFactory.instance = instance;
	}

	public RuleSet newARuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.accesskey,
				HTMLAttribute.charset, HTMLAttribute.coords, HTMLAttribute.href, HTMLAttribute.hreflang, HTMLAttribute.name,
				HTMLAttribute.onblur, HTMLAttribute.onfocus, HTMLAttribute.rel, HTMLAttribute.rev, HTMLAttribute.shape,
				HTMLAttribute.tabindex, HTMLAttribute.target, HTMLAttribute.type });
	}

	public RuleSet newabbrRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newAcronymRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newAddressRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newAppletRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.align,
				HTMLAttribute.alt, HTMLAttribute.archive, HTMLAttribute.code, HTMLAttribute.codebase, HTMLAttribute.height,
				HTMLAttribute.hspace, HTMLAttribute.name, HTMLAttribute.object, HTMLAttribute.vspace, HTMLAttribute.width });
	}

	public RuleSet newAreaRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.alt, HTMLAttribute.accesskey, HTMLAttribute.coords,
				HTMLAttribute.href, HTMLAttribute.nohref, HTMLAttribute.onblur, HTMLAttribute.onfocus, HTMLAttribute.shape,
				HTMLAttribute.tabindex, HTMLAttribute.target });
	}

	public RuleSet newBRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newBaseRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.href, HTMLAttribute.target });
	}

	public RuleSet newBasefontRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.color,
				HTMLAttribute.face, HTMLAttribute.size });
	}

	public RuleSet newBdoRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.dir });
	}

	public RuleSet newBigRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newBlockquoteRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.cite });
	}

	public RuleSet newBodyRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.alink, HTMLAttribute.background, HTMLAttribute.bgcolor,
				HTMLAttribute.link, HTMLAttribute.onload, HTMLAttribute.onunload, HTMLAttribute.text, HTMLAttribute.vlink });
	}

	public RuleSet newBrRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.clear });
	}

	public RuleSet newButtonRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.accesskey,
				HTMLAttribute.disabled, HTMLAttribute.name, HTMLAttribute.onblur, HTMLAttribute.onfocus, HTMLAttribute.tabindex,
				HTMLAttribute.type, HTMLAttribute.value });
	}

	public RuleSet newCaptionRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align });
	}

	public RuleSet newCenterRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newCiteRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newCodeRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newColRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.charAttribute, HTMLAttribute.charoff,
				HTMLAttribute.span, HTMLAttribute.valign, HTMLAttribute.width });
	}

	public RuleSet newColgroupRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.charAttribute, HTMLAttribute.charoff,
				HTMLAttribute.span, HTMLAttribute.valign, HTMLAttribute.width });
	}

	public RuleSet newDdRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newDelRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.cite,
				HTMLAttribute.datetime });
	}

	public RuleSet newDfnRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newDirRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.compact });
	}

	public RuleSet newDivRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align });
	}

	public RuleSet newDlRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.compact });
	}

	public RuleSet newDtRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newEmRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newFieldsetRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newFontRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.color,
				HTMLAttribute.face, HTMLAttribute.size });
	}

	public RuleSet newFormRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.action, HTMLAttribute.accept, HTMLAttribute.acceptCharset,
				HTMLAttribute.enctype, HTMLAttribute.method, HTMLAttribute.name, HTMLAttribute.onreset, HTMLAttribute.onsubmit,
				HTMLAttribute.target });
	}

	public RuleSet newFrameRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.frameborder, HTMLAttribute.longdesc, HTMLAttribute.marginwidth,
				HTMLAttribute.marginheight, HTMLAttribute.name, HTMLAttribute.noresize, HTMLAttribute.scrolling, HTMLAttribute.src });
	}

	public RuleSet newFramesetRuleSet() {
		return new RuleSet(null,
				new HTMLAttribute[] { HTMLAttribute.cols, HTMLAttribute.onload, HTMLAttribute.onunload, HTMLAttribute.rows });
	}

	public RuleSet newHRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align });
	}

	public RuleSet newHeadRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.profile });
	}

	public RuleSet newHrRuleSet() {
		return new RuleSet(null,
				new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.noshade, HTMLAttribute.size, HTMLAttribute.width });
	}

	public RuleSet newHtmlRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.version });
	}

	public RuleSet newIRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newIframeRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.align,
				HTMLAttribute.frameborder, HTMLAttribute.height, HTMLAttribute.longdesc, HTMLAttribute.marginwidth,
				HTMLAttribute.marginheight, HTMLAttribute.name, HTMLAttribute.scrolling, HTMLAttribute.src, HTMLAttribute.width });
	}

	public RuleSet newImgRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.align,
				HTMLAttribute.alt, HTMLAttribute.border, HTMLAttribute.height, HTMLAttribute.hspace, HTMLAttribute.ismap,
				HTMLAttribute.longdesc, HTMLAttribute.name, HTMLAttribute.src, HTMLAttribute.usemap, HTMLAttribute.vspace,
				HTMLAttribute.width });
	}

	public RuleSet newInputRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.accept,
				HTMLAttribute.accesskey, HTMLAttribute.align, HTMLAttribute.alt, HTMLAttribute.checked, HTMLAttribute.disabled,
				HTMLAttribute.ismap, HTMLAttribute.maxlength, HTMLAttribute.name, HTMLAttribute.onblur, HTMLAttribute.onchange,
				HTMLAttribute.onfocus, HTMLAttribute.onselect, HTMLAttribute.readonly, HTMLAttribute.size, HTMLAttribute.src,
				HTMLAttribute.tabindex, HTMLAttribute.type, HTMLAttribute.usemap, HTMLAttribute.value });
	}

	public RuleSet newInsRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.cite,
				HTMLAttribute.datetime });
	}

	public RuleSet newIsindexRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.prompt });
	}

	public RuleSet newKbdRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newLabelRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.accesskey,
				HTMLAttribute.forAttribute, HTMLAttribute.onblur, HTMLAttribute.onfocus });
	}

	public RuleSet newLegendRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.accesskey, HTMLAttribute.align });
	}

	public RuleSet newLiRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.type, HTMLAttribute.value });
	}

	public RuleSet newLinkRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.charset, HTMLAttribute.href, HTMLAttribute.hreflang,
				HTMLAttribute.media, HTMLAttribute.rel, HTMLAttribute.rev, HTMLAttribute.target, HTMLAttribute.type });
	}

	public RuleSet newMapRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.name });
	}

	public RuleSet newMenuRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.compact });
	}

	public RuleSet newMetaRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.name, HTMLAttribute.content, HTMLAttribute.httpEquiv,
				HTMLAttribute.scheme });
	}

	public RuleSet newNoframesRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newNoscriptRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newObjectRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.align,
				HTMLAttribute.archive, HTMLAttribute.border, HTMLAttribute.classid, HTMLAttribute.codebase, HTMLAttribute.codetype,
				HTMLAttribute.data, HTMLAttribute.declare, HTMLAttribute.height, HTMLAttribute.hspace, HTMLAttribute.name,
				HTMLAttribute.standby, HTMLAttribute.tabindex, HTMLAttribute.type, HTMLAttribute.usemap, HTMLAttribute.vspace,
				HTMLAttribute.width });
	}

	public RuleSet newOlRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.compact, HTMLAttribute.start, HTMLAttribute.type });
	}

	public RuleSet newOptgroupRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.disabled, HTMLAttribute.label });
	}

	public RuleSet newOptionRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.disabled, HTMLAttribute.label, HTMLAttribute.selected,
				HTMLAttribute.value });
	}

	public RuleSet newPRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align });
	}

	public RuleSet newParamRuleSet() {
		return new RuleSet(null,
				new HTMLAttribute[] { HTMLAttribute.name, HTMLAttribute.value, HTMLAttribute.valuetype, HTMLAttribute.type });
	}

	public RuleSet newPreRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.width });
	}

	public RuleSet newQRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.cite });
	}

	public RuleSet newSRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newSampRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newScriptRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.charset,
				HTMLAttribute.defer, HTMLAttribute.event, HTMLAttribute.language, HTMLAttribute.forAttribute, HTMLAttribute.src,
				HTMLAttribute.type });
	}

	public RuleSet newSelectRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.disabled,
				HTMLAttribute.multiple, HTMLAttribute.name, HTMLAttribute.onblur, HTMLAttribute.onchange, HTMLAttribute.onfocus,
				HTMLAttribute.size, HTMLAttribute.tabindex });
	}

	public RuleSet newSmallRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newSpanRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newStrikeRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newStrongRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newStyleRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.media, HTMLAttribute.title, HTMLAttribute.type });
	}

	public RuleSet newSubRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newSupRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newTableRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.border, HTMLAttribute.bgcolor,
				HTMLAttribute.cellpadding, HTMLAttribute.cellspacing, HTMLAttribute.frame, HTMLAttribute.rules, HTMLAttribute.summary,
				HTMLAttribute.width });
	}

	public RuleSet newTbodyRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.charAttribute, HTMLAttribute.charoff,
				HTMLAttribute.valign });
	}

	public RuleSet newTdRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.abbr, HTMLAttribute.align, HTMLAttribute.axis, HTMLAttribute.bgcolor,
				HTMLAttribute.charAttribute, HTMLAttribute.charoff, HTMLAttribute.colspan, HTMLAttribute.headers, HTMLAttribute.height,
				HTMLAttribute.nowrap, HTMLAttribute.rowspan, HTMLAttribute.scope, HTMLAttribute.valign, HTMLAttribute.width });
	}

	public RuleSet newTextareaRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, new HTMLAttribute[] { HTMLAttribute.accesskey,
				HTMLAttribute.cols, HTMLAttribute.disabled, HTMLAttribute.name, HTMLAttribute.onblur, HTMLAttribute.onchange,
				HTMLAttribute.onfocus, HTMLAttribute.onselect, HTMLAttribute.readonly, HTMLAttribute.rows, HTMLAttribute.tabindex });
	}

	public RuleSet newTfootRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.charAttribute, HTMLAttribute.charoff,
				HTMLAttribute.valign });
	}

	public RuleSet newThRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.abbr, HTMLAttribute.align, HTMLAttribute.axis, HTMLAttribute.bgcolor,
				HTMLAttribute.charAttribute, HTMLAttribute.charoff, HTMLAttribute.colspan, HTMLAttribute.headers, HTMLAttribute.height,
				HTMLAttribute.nowrap, HTMLAttribute.rowspan, HTMLAttribute.scope, HTMLAttribute.valign, HTMLAttribute.width });
	}

	public RuleSet newTheadRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.charAttribute, HTMLAttribute.charoff,
				HTMLAttribute.valign });
	}

	public RuleSet newTitleRuleSet() {
		return new RuleSet(null, null);
	}

	public RuleSet newTrRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.align, HTMLAttribute.bgcolor, HTMLAttribute.charAttribute,
				HTMLAttribute.charoff, HTMLAttribute.valign });
	}

	public RuleSet newTtRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newURuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

	public RuleSet newUlRuleSet() {
		return new RuleSet(null, new HTMLAttribute[] { HTMLAttribute.compact, HTMLAttribute.type });
	}

	public RuleSet newVarRuleSet() {
		return new RuleSet(new ElementType[] { ElementType.BLOCK, ElementType.INLINE }, null);
	}

}
