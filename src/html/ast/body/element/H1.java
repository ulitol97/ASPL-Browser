package html.ast.body.element;

import java.util.List;

import html.ast.body.element.sentence.Sentence;
import html.visitor.Visitor;

public class H1 implements Element {

	public List<Sentence> contents;

	public H1(List<Sentence> contents) {
		this.contents = contents;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
