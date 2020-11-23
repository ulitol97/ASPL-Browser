package simpleHtml.ast.head;

import simpleHtml.ast.AstHtml;
import simpleHtml.visitor.Visitor;

public class Head implements AstHtml {

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
