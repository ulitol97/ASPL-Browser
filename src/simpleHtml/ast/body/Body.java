 package simpleHtml.ast.body;

import simpleHtml.ast.AstHtml;
import simpleHtml.visitor.Visitor;

public class Body implements AstHtml {

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
