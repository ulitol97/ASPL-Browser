package html.ast.body;

import java.util.List;

import html.ast.AstHtml;
import html.ast.body.element.Element;
import html.visitor.Visitor;

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
