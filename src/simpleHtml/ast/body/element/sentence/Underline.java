package simpleHtml.ast.body.element.sentence;

import simpleHtml.visitor.Visitor;

public class Underline implements Sentence {

	public Text text;

	public Underline(Text text) {
		this.text = text;
	}
	
	@Override
	public String getContent() {
		return text.text;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
