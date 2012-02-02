package org.webguitoolkit.ui.html;

import org.webguitoolkit.ui.html.definitions.HTMLElement;


public class XHTMLTagFactory{

	// singleton, there is only one instance of this class.
	protected static XHTMLTagFactory instance;

	/**
	 * singleton pattern, use this method to get a instance of the factory
	 * 
	 * @return a instance of the factory
	 */
	public static XHTMLTagFactory getInstance() {
		if (instance == null) {
			// sounds strange to make an instance of an abstract class.
			instance = new XHTMLTagFactory();
		}
		return instance;
	}

	/**
	 * The instance can be changed
	 * 
	 * @param instance new instance
	 */
	public static void setInstance(XHTMLTagFactory instance) {
		XHTMLTagFactory.instance = instance;
	}

	public HTMLTag newA(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.a);
	}
	
	public HTMLTag newA(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.a, content);
	}

	public HTMLTag newAbbr(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.abbr);
	}
	
	public HTMLTag newAbbr(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.abbr, content);
	}

	public HTMLTag newAcronym(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.acronym);
	}
	
	public HTMLTag newAcronym(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.acronym, content);
	}

	public HTMLTag newAddress(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.address);
	}
	
	public HTMLTag newAddress(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.address, content);
	}

	public HTMLTag newApplet(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.applet);
	}

	public HTMLTag newArea(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.area);
	}

	public HTMLTag newB(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.b);
	}
	
	public HTMLTag newB(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.b, content);
	}

	public HTMLTag newBase(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.base);
	}

	public HTMLTag newBasefont(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.basefont);
	}

	public HTMLTag newBdo(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.bdo);
	}
	
	public HTMLTag newBdo(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.bdo, content);
	}

	public HTMLTag newBig(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.big);
	}
	
	public HTMLTag newBig(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.big, content);
	}

	public HTMLTag newBlockquote(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.blockquote);
	}

	public HTMLTag newBody(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.body);
	}

	public HTMLTag newBr(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.br);
	}

	public HTMLTag newButton(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.button);
	}
	
	public HTMLTag newButton(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.button, content);
	}

	public HTMLTag newCaption(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.caption);
	}
	
	public HTMLTag newCaption(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.caption, content);
	}

	public HTMLTag newCenter(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.center);
	}
	
	public HTMLTag newCenter(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.center, content);
	}

	public HTMLTag newCite(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.cite);
	}
	
	public HTMLTag newCite(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.cite, content);
	}

	public HTMLTag newCode(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.code);
	}
	
	public HTMLTag newCode(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.code, content);
	}

	public HTMLTag newCol(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.col);
	}

	public HTMLTag newColgroup(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.colgroup);
	}

	public HTMLTag newDd(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.dd);
	}
	
	public HTMLTag newDd(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.dd, content);
	}

	public HTMLTag newDel(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.del);
	}
	
	public HTMLTag newDel(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.del, content);
	}

	public HTMLTag newDfn(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.dfn);
	}
	
	public HTMLTag newDfn(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.dfn, content);
	}

	public HTMLTag newDir(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.dir);
	}

	public HTMLTag newDiv(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.div);
	}
	
	public HTMLTag newDiv(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.div, content);
	}

	public HTMLTag newDl(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.dl);
	}

	public HTMLTag newDt(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.dt, content);
	}
	
	public HTMLTag newDt(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.dt);
	}

	public HTMLTag newEm(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.em);
	}
	
	public HTMLTag newEm(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.em, content);
	}

	public HTMLTag newFieldset(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.fieldset);
	}
	
	public HTMLTag newFieldset(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.fieldset, content);
	}

	public HTMLTag newFont(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.font);
	}
	
	public HTMLTag newFont(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.font, content);
	}

	public HTMLTag newForm(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.form);
	}

	public HTMLTag newFrame(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.frame);
	}

	public HTMLTag newFrameset(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.frameset);
	}

	public HTMLTag newH1(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.h1, content);
	}


	public HTMLTag newH2(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.h2, content);
	}

	public HTMLTag newH3(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.h3, content);
	}

	public HTMLTag newH4(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.h4, content);
	}

	public HTMLTag newH5(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.h5, content);
	}

	public HTMLTag newH6(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.h6, content);
	}

	public HTMLTag newHead(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.head);
	}

	public HTMLTag newHr(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.hr);
	}

	public HTMLTag newHtml(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.html);
	}

	public HTMLTag newI(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.i);
	}
	
	public HTMLTag newI(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.i, content);
	}

	public HTMLTag newIframe(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.iframe);
	}

	public HTMLTag newImg(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.img);
	}

	public HTMLTag newInput(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.input);
	}

	public HTMLTag newIns(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.ins);
	}
	
	public HTMLTag newIns(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.ins, content);
	}

	public HTMLTag newIsindex(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.isindex);
	}

	public HTMLTag newKbd(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.kbd);
	}
	
	public HTMLTag newKbd(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.kbd, content);
	}

	public HTMLTag newLabel(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.label);
	}
	
	public HTMLTag newLabel(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.label, content);
	}

	public HTMLTag newLegend(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.legend);
	}
	
	public HTMLTag newLegend(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.legend, content);
	}

	public HTMLTag newLi(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.li);
	}
	
	public HTMLTag newLi(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.li, content);
	}

	public HTMLTag newLink(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.link);
	}

	public HTMLTag newMap(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.map);
	}

	public HTMLTag newMenu(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.menu);
	}

	public HTMLTag newMeta(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.meta);
	}

	public HTMLTag newNoframes(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.noframes);
	}

	public HTMLTag newNoscript(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.noscript);
	}

	public HTMLTag newObject(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.object);
	}
	
	public HTMLTag newObject(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.object, content);
	}

	public HTMLTag newOl(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.ol);
	}

	public HTMLTag newOptgroup(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.optgroup);
	}

	public HTMLTag newOption(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.option);
	}

	public HTMLTag newP(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.p);
	}
	
	public HTMLTag newP(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.p, content);
	}

	public HTMLTag newParam(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.param);
	}

	public HTMLTag newPre(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.pre);
	}
	
	public HTMLTag newPre(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.pre, content);
	}

	public HTMLTag newQ(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.q);
	}
	
	public HTMLTag newQ(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.q, content);
	}

	public HTMLTag newS(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.s);
	}
	
	public HTMLTag newS(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.s, content);
	}

	public HTMLTag newSamp(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.samp);
	}
	
	public HTMLTag newSamp(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.samp, content);
	}

	public HTMLTag newScript(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.script);
	}
	
	public HTMLTag newScript(HTMLTag parentElement, String script) {
		return createNew(parentElement, null, HTMLElement.script, script);
	}

	public HTMLTag newSelect(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.select, content);
	}
	
	public HTMLTag newSelect(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.select);
	}

	public HTMLTag newSmall(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.small);
	}
	
	public HTMLTag newSmall(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.small, content);
	}

	public HTMLTag newSpan(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.span);
	}
	
	public HTMLTag newSpan(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.span, content);
	}

	public HTMLTag newStrike(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.strike, content);
	}
	
	public HTMLTag newStrike(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.strike);
	}

	public HTMLTag newStrong(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.strong, content);
	}
	
	public HTMLTag newStrong(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.strong);
	}

	public HTMLTag newStyle(HTMLTag parentElement, String style) {
		return createNew(parentElement, null, HTMLElement.style, style);
	}
	
	public HTMLTag newStyle(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.style);
	}

	public HTMLTag newSub(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.sub);
	}
	
	public HTMLTag newSub(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.sub, content);
	}

	public HTMLTag newSup(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.sup);
	}
	
	public HTMLTag newSup(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.sup, content);
	}

	public HTMLTag newTable(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.table);
	}

	public HTMLTag newTbody(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.tbody);
	}

	public HTMLTag newTd(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.td);
	}
	
	public HTMLTag newTd(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.td, content);
	}

	public HTMLTag newTextarea(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.textarea);
	}
	
	public HTMLTag newTextarea(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.textarea, content);
	}

	public HTMLTag newTfoot(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.tfoot);
	}

	public HTMLTag newTh(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.th);
	}

	public HTMLTag newThead(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.thead);
	}
	
	public HTMLTag newThead(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.thead, content);
	}

	public HTMLTag newTitle(HTMLTag parentElement) {
		return createNew(parentElement, null, HTMLElement.title);
	}
	
	public HTMLTag newTitle(HTMLTag parentElement, String content) {
		return createNew(parentElement, null, HTMLElement.title, content);
	}

	public HTMLTag newTr(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.tr);
	}

	public HTMLTag newTt(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.tt);
	}
	
	public HTMLTag newTt(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.tt, content);
	}

	public HTMLTag newU(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.u);
	}
	
	public HTMLTag newU(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.u, content);
	}

	public HTMLTag newUl(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.ul);
	}

	public HTMLTag newVar(HTMLTag parentElement, String id) {
		return createNew(parentElement, id, HTMLElement.var);
	}
	
	public HTMLTag newVar(HTMLTag parentElement, String id, String content) {
		return createNew(parentElement, id, HTMLElement.var, content);
	}

	private HTMLTag createNew(HTMLTag parentElement, String id, HTMLElement type) {
		HTMLTag result = new XHTMLTag(type, parentElement);
		result.setId(id);
		return result;
	}
	
	private HTMLTag createNew(HTMLTag parentElement, String id, HTMLElement type, String content) {
		HTMLTag result = createNew(parentElement, id, type);
		if(content!=null)
			result.setContent(content);
		return result;
	}

}
