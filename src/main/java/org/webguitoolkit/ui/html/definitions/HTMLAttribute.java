package org.webguitoolkit.ui.html.definitions;

public enum HTMLAttribute {
	abbr("abbr"),
	accesskey("accesskey"),
	accept("accept"),
	acceptCharset("accept-charset"),
	action("action"),
	align("align"),
	alink("alink"),
	alt("alt"),
	archive("archive"),
	axis("axis"),
	
	background("background"),
	bgcolor("bgcolor"),
	border("border"),
	
	cellpadding("cellpadding"),
	cellspacing("cellspacing"),
	charAttribute("char"),
	charset("charset"),
	charoff("charoff"),
	checked("checked"),
	cite("cite"),
	classAttribute("class"),
	classid("classid"),
	clear("clear"),
	code("code"),
	codebase("codebase"),
	codetype("codetype"),
	color("color"),
	cols("cols"),
	colspan("colspan"),
	compact("compact"),
	content("content"),
	coords("coords"),
	
	data("data"),
	datetime("datetime"),
	declare("declare"),
	defer("defer"),
	dir("dir"),
	disabled("disabled"),
	
	enctype("enctype"),
	event("event"),
	
	face("face"),
	forAttribute("for"),
	frame("frame"),
	frameborder("frameborder"),
	
	headers("headers"),
	height("height"),
	href("href"),
	hreflang("hreflang"),
	hspace("hspace"),
	httpEquiv("http-equiv"),
	
	id("id"),
	ismap("ismap"),
	
	label("label"),
	lang("lang"),
	language("language"),
	link("link"),
	longdesc("longdesc"),
	
	marginheight("marginheight"),
	marginwidth("marginwidth"),
	maxlength("maxlength"),
	media("media"),
	method("method"),
	multiple("multiple"),
	
	name("name"),
	nohref("nohref"),
	noresize("noresize"),
	noshade("noshade"),
	nowrap("nowrap"),
	
	object("object"),
	onclick("onclick"),
	onblur("onblur"),
	onchange("onchange"),
	ondblclick("ondblclick"),
	onfocus("onfocus"),
	onload("onload"),
	onmousedown("onmousedown"),
	onmouseup("onmouseup"),
	onmouseover("onmouseover"),
	onmousemove("onmousemove"),
	onmouseout("onmouseout"),
	onkeypress("onkeypress"),
	onkeydown("onkeydown"),
	onkeyup("onkeyup"),
	onreset("onreset"),
	onselect("onselect"),
	onsubmit("onsubmit"),
	onunload("onunload"),
	
	profile("profile"),
	prompt("prompt"),
	
	readonly("readonly"),
	rel("rel"),
	rev("rev"),
	rows("rows"),
	rowspan("rowspan"),
	rules("rules"),
	
	scheme("scheme"),
	scope("scope"),
	scrolling("scrolling"),
	selected("selected"),
	shape("shape"),
	size("size"),
	span("span"),
	src("src"),
	standby("standby"),
	start("start"),
	style("style"),
	summary("summary"),
	
	tabindex("tabindex"),
	target("target"),
	text("text"),
	title("title"),
	type("type"),
	
	usemap("usemap"),
	
	valign("valign"),
	value("value"),
	valuetype("valuetype"),
	version("version"),
	vlink("vlink"),
	vspace("vspace"),
	
	width("width");
	
	private String tag;

	HTMLAttribute(String tag) {
		this.tag = tag;
	}

	public String getName(Version type) {
		switch (type) {
			case HTML:
				return tag.toUpperCase();
			default:
				return tag;
		}
	}

}
