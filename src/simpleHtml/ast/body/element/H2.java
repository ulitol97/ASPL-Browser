package simpleHtml.ast.body.element;

import java.util.List;

import simpleHtml.ast.body.element.sentence.Sentence;
import simpleHtml.visitor.Visitor;

public class H2 implements Element {

	public List<Sentence> contents;

	public H2(List<Sentence> contents) {
		this.contents = contents;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
