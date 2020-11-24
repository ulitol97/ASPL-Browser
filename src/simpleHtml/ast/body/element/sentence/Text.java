package simpleHtml.ast.body.element.sentence;

import simpleHtml.visitor.Visitor;

public class Text implements Sentence {

	public String text;

	public Text(String text) {
		this.text = text;
	}
	
	@Override
	public String getContent() {
		return text;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
