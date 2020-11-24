package simpleHtml.ast.body;

import java.util.List;

import simpleHtml.ast.AstHtml;
import simpleHtml.ast.body.element.Element;
import simpleHtml.visitor.Visitor;

public class Body implements AstHtml {

	public List<Element> elements;

	public Body(List<Element> elements) {
		this.elements = elements;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
