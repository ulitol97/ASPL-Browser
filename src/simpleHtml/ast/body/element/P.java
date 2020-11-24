package simpleHtml.ast.body.element;

import java.util.List;

import simpleHtml.ast.body.element.sentence.Sentence;
import simpleHtml.visitor.Visitor;

public class P implements Element {

	public List<Sentence> contents;

	public P(List<Sentence> contents) {
		this.contents = contents;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
