package simpleHtml.visitor;

import simpleHtml.ast.body.Body;
import simpleHtml.ast.head.Head;
import simpleHtml.ast.html.Html;

public interface Visitor {
	
	Object visit(Html p, Object param);

	Object visit(Head head, Object param);

	Object visit(Body body, Object param);

}
