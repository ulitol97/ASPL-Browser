package simpleCss.visitor;

import simpleCss.ast.declaration.Declaration;
import simpleCss.ast.rule.Rule;
import simpleCss.ast.stylesheet.Stylesheet;

public class PrintCssAstVisitor implements Visitor {
	String sp = "   ";

	@Override
	public Object visit(Stylesheet css, Object param) {
		String sd = "", sr;
		for (Declaration declaration : css.declarations) {
			sd = sd + (String) declaration.accept(this, sp);
		}
		sr = "(CSS declarations\n" + sd + ")";
		return sr;
	}

	@Override
	public Object visit(Declaration declaration, Object param) {
		String spar = "", sr;
		for (Rule rule : declaration.rules) {
			spar = spar + (String) rule.accept(this, (String) param + sp);
		}
		sr = (String) param + "(" + declaration.selector + "\n" + spar
				+ (String) param + ")\n";
		return sr;
	}

	@Override
	public Object visit(Rule rule, Object param) {
		return (String) param + rule.key + " --> " + rule.value + "\n";
	}

}
