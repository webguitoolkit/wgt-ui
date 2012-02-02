package org.webguitoolkit.ui.html.definitions;

public enum HTMLElement {
	a(RuleSetFactory.getInstance().newARuleSet(),ElementType.INLINE), 
	abbr(RuleSetFactory.getInstance().newabbrRuleSet() ,ElementType.INLINE), 
	acronym(RuleSetFactory.getInstance().newAcronymRuleSet() ,ElementType.INLINE), 
	address(RuleSetFactory.getInstance().newAddressRuleSet() ,ElementType.BLOCK),
	applet(RuleSetFactory.getInstance().newAppletRuleSet() ,ElementType.INLINE), 
	area(RuleSetFactory.getInstance().newAreaRuleSet() ,ElementType.NONE),
	
	b(RuleSetFactory.getInstance().newBRuleSet() ,ElementType.INLINE),
	base(RuleSetFactory.getInstance().newBaseRuleSet() ,ElementType.NONE), 
	basefont(RuleSetFactory.getInstance().newBasefontRuleSet() ,ElementType.INLINE), 
	bdo(RuleSetFactory.getInstance().newBdoRuleSet() ,ElementType.INLINE), 
	big(RuleSetFactory.getInstance().newBigRuleSet() ,ElementType.INLINE), 
	blockquote(RuleSetFactory.getInstance().newBlockquoteRuleSet() ,ElementType.BLOCK), 
	body(RuleSetFactory.getInstance().newBodyRuleSet() ,ElementType.NONE), 
	br(RuleSetFactory.getInstance().newBrRuleSet() ,ElementType.INLINE), 
	button(RuleSetFactory.getInstance().newButtonRuleSet() ,ElementType.INLINE), 
	
	caption(RuleSetFactory.getInstance().newCaptionRuleSet() ,ElementType.NONE), 
	center(RuleSetFactory.getInstance().newCenterRuleSet() ,ElementType.BLOCK), 
	cite(RuleSetFactory.getInstance().newCiteRuleSet() ,ElementType.INLINE), 
	code(RuleSetFactory.getInstance().newCodeRuleSet() ,ElementType.INLINE), 
	col(RuleSetFactory.getInstance().newColRuleSet() ,ElementType.NONE), 
	colgroup(RuleSetFactory.getInstance().newColgroupRuleSet() ,ElementType.NONE), 
	
	dd(RuleSetFactory.getInstance().newDdRuleSet() ,ElementType.NONE), 
	del(RuleSetFactory.getInstance().newDelRuleSet() ,ElementType.BLOCK), 
	dfn(RuleSetFactory.getInstance().newDfnRuleSet() ,ElementType.INLINE), 
	dir(RuleSetFactory.getInstance().newDirRuleSet() ,ElementType.BLOCK), 
	div(RuleSetFactory.getInstance().newDivRuleSet() ,ElementType.BLOCK), 
	dl(RuleSetFactory.getInstance().newDlRuleSet() ,ElementType.BLOCK), 
	dt(RuleSetFactory.getInstance().newDtRuleSet() ,ElementType.NONE), 
	
	em(RuleSetFactory.getInstance().newEmRuleSet() ,ElementType.INLINE), 
	
	fieldset(RuleSetFactory.getInstance().newFieldsetRuleSet() ,ElementType.BLOCK), 
	font(RuleSetFactory.getInstance().newFontRuleSet() ,ElementType.INLINE), 
	form(RuleSetFactory.getInstance().newFormRuleSet() ,ElementType.BLOCK), 
	frame(RuleSetFactory.getInstance().newFrameRuleSet() ,ElementType.NONE), 
	frameset(RuleSetFactory.getInstance().newFramesetRuleSet() ,ElementType.NONE), 
	
	h1(RuleSetFactory.getInstance().newHRuleSet() ,ElementType.BLOCK), 
	h2(RuleSetFactory.getInstance().newHRuleSet() ,ElementType.BLOCK), 
	h3(RuleSetFactory.getInstance().newHRuleSet() ,ElementType.BLOCK), 
	h4(RuleSetFactory.getInstance().newHRuleSet() ,ElementType.BLOCK), 
	h5(RuleSetFactory.getInstance().newHRuleSet() ,ElementType.BLOCK), 
	h6(RuleSetFactory.getInstance().newHRuleSet() ,ElementType.BLOCK), 
	head(RuleSetFactory.getInstance().newHeadRuleSet() ,ElementType.NONE), 
	hr(RuleSetFactory.getInstance().newHrRuleSet() ,ElementType.BLOCK), 
	html(RuleSetFactory.getInstance().newHtmlRuleSet() ,ElementType.NONE), 
	
	i(RuleSetFactory.getInstance().newIRuleSet() ,ElementType.INLINE), 
	iframe(RuleSetFactory.getInstance().newIframeRuleSet() ,ElementType.INLINE), 
	img(RuleSetFactory.getInstance().newImgRuleSet() ,ElementType.INLINE), 
	input(RuleSetFactory.getInstance().newInputRuleSet() ,ElementType.INLINE), 
	ins(RuleSetFactory.getInstance().newInsRuleSet() ,ElementType.INLINE), 
	isindex(RuleSetFactory.getInstance().newIsindexRuleSet() ,ElementType.BLOCK), 
	
	kbd(RuleSetFactory.getInstance().newKbdRuleSet() ,ElementType.INLINE), 
	
	label(RuleSetFactory.getInstance().newLabelRuleSet() ,ElementType.INLINE), 
	legend(RuleSetFactory.getInstance().newLegendRuleSet() ,ElementType.NONE), 
	li(RuleSetFactory.getInstance().newLiRuleSet() ,ElementType.NONE), 
	link(RuleSetFactory.getInstance().newLinkRuleSet() ,ElementType.NONE), 
	
	map(RuleSetFactory.getInstance().newMapRuleSet() ,ElementType.INLINE), 
	menu(RuleSetFactory.getInstance().newMenuRuleSet() ,ElementType.BLOCK), 
	meta(RuleSetFactory.getInstance().newMetaRuleSet() ,ElementType.NONE), 
	
	noframes(RuleSetFactory.getInstance().newNoframesRuleSet() ,ElementType.BLOCK), 
	noscript(RuleSetFactory.getInstance().newNoscriptRuleSet() ,ElementType.BLOCK), 
	
	object(RuleSetFactory.getInstance().newObjectRuleSet() ,ElementType.INLINE), 
	ol(RuleSetFactory.getInstance().newOlRuleSet() ,ElementType.BLOCK), 
	optgroup(RuleSetFactory.getInstance().newOptgroupRuleSet() ,ElementType.NONE), 
	option(RuleSetFactory.getInstance().newOptionRuleSet() ,ElementType.NONE), 
	
	p(RuleSetFactory.getInstance().newPRuleSet() ,ElementType.BLOCK), 
	param(RuleSetFactory.getInstance().newParamRuleSet() ,ElementType.NONE), 
	pre(RuleSetFactory.getInstance().newPreRuleSet() ,ElementType.BLOCK), 
	
	q(RuleSetFactory.getInstance().newQRuleSet() ,ElementType.INLINE), 
	
	s(RuleSetFactory.getInstance().newSRuleSet() ,ElementType.NONE), 
	samp(RuleSetFactory.getInstance().newSampRuleSet() ,ElementType.INLINE), 
	script(RuleSetFactory.getInstance().newScriptRuleSet() ,ElementType.INLINE), 
	select(RuleSetFactory.getInstance().newSelectRuleSet() ,ElementType.INLINE), 
	small(RuleSetFactory.getInstance().newSmallRuleSet() ,ElementType.INLINE), 
	span(RuleSetFactory.getInstance().newSpanRuleSet() ,ElementType.INLINE), 
	strike(RuleSetFactory.getInstance().newStrikeRuleSet() ,ElementType.NONE), 
	strong(RuleSetFactory.getInstance().newStrongRuleSet() ,ElementType.INLINE), 
	style(RuleSetFactory.getInstance().newStyleRuleSet() ,ElementType.NONE), 
	sub(RuleSetFactory.getInstance().newSubRuleSet() ,ElementType.INLINE), 
	sup(RuleSetFactory.getInstance().newSupRuleSet() ,ElementType.INLINE), 
	
	table(RuleSetFactory.getInstance().newTableRuleSet() ,ElementType.BLOCK), 
	tbody(RuleSetFactory.getInstance().newTbodyRuleSet() ,ElementType.NONE), 
	td(RuleSetFactory.getInstance().newTdRuleSet() ,ElementType.NONE), 
	textarea(RuleSetFactory.getInstance().newTextareaRuleSet() ,ElementType.INLINE), 
	tfoot(RuleSetFactory.getInstance().newTfootRuleSet() ,ElementType.NONE), 
	th(RuleSetFactory.getInstance().newThRuleSet() ,ElementType.NONE), 
	thead(RuleSetFactory.getInstance().newTheadRuleSet() ,ElementType.NONE), 
	title(RuleSetFactory.getInstance().newTitleRuleSet() ,ElementType.NONE), 
	tr(RuleSetFactory.getInstance().newTrRuleSet() ,ElementType.NONE), 
	tt(RuleSetFactory.getInstance().newTtRuleSet() ,ElementType.INLINE), 
	
	u(RuleSetFactory.getInstance().newURuleSet() ,ElementType.NONE), 
	ul(RuleSetFactory.getInstance().newUlRuleSet() ,ElementType.BLOCK), 
	
	var(RuleSetFactory.getInstance().newVarRuleSet() ,ElementType.INLINE);
	
	private RuleSet rule;
	private ElementType type;
	
	HTMLElement(RuleSet rules, ElementType type) {
		this.rule = rules;
		this.type = type;
		this.rule.setTag(this);		
	}
	
	public String getName(Version version) {
		switch (version) {
			case HTML:
				return name().toUpperCase();
			default:
				return name();
		}
	}
	
	public ElementType getType() {
		return this.type;
	}
	
	public boolean isValidAttribute(HTMLAttribute attribute){
		return this.rule.isAttributePossible(attribute);
	}
	
	public boolean isValidChild(HTMLElement tag){
		return this.rule.isChildValid(tag);
	}
}
