package simpleCss.visitor;

import simpleCss.ast.declaration.Declaration;
import simpleCss.ast.rule.Rule;
import simpleCss.ast.stylesheet.Stylesheet;

public interface Visitor {
	
	Object visit(Stylesheet stylesheet, Object param);

	Object visit(Declaration declaration, Object param);

	Object visit(Rule rule, Object param);
	
}
