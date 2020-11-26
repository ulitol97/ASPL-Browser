package html.ast.html;

import html.ast.AstHtml;
import html.ast.body.Body;
import html.ast.head.Head;
import html.visitor.Visitor;

public class Html implements AstHtml {

	public Head head;
	public Body body;

	public Html(Head head, Body body) {
		this.head = head;
		this.body = body;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
