package html.ast.head;

import html.ast.AstHtml;
import html.ast.body.element.sentence.Text;
import html.visitor.Visitor;

public class Link implements AstHtml {

	public Text href;
	public Text rel;
	public Text type;

	public Link(Text href, Text rel, Text style) {
		this.href = href;
		this.rel = rel;
		this.type = style;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}


}
