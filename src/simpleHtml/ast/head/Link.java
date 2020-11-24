package simpleHtml.ast.head;

import simpleHtml.ast.AstHtml;
import simpleHtml.ast.body.element.sentence.Text;
import simpleHtml.visitor.Visitor;

public class Link implements AstHtml {

	public Text href;
	public Text rel;
	public Text style;

	public Link(Text href, Text rel, Text style) {
		this.href = href;
		this.rel = rel;
		this.style = style;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}


}
