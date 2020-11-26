package css.visitor;

import css.ast.declaration.Declaration;
import css.ast.rule.Rule;
import css.ast.stylesheet.Stylesheet;

public interface Visitor {
	
	Object visit(Stylesheet stylesheet, Object param);

	Object visit(Declaration declaration, Object param);

	Object visit(Rule rule, Object param);
	
}
