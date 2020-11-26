package html.ast.body.element;

import html.ast.AstHtml;
import html.visitor.Visitor;

public interface Element extends AstHtml {
	
	// H1, H2, P...
	
	Object accept(Visitor v, Object param);

}
