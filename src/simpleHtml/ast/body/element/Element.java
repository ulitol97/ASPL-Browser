package simpleHtml.ast.body.element;

import simpleHtml.ast.AstHtml;
import simpleHtml.visitor.Visitor;

public interface Element extends AstHtml {
	
	// H1, H2, P...
	
	Object accept(Visitor v, Object param);

}
