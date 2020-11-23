package simpleHtml.ast.html;

import simpleHtml.ast.AstHtml;
import simpleHtml.ast.body.Body;
import simpleHtml.ast.head.Head;
import simpleHtml.visitor.Visitor;

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
