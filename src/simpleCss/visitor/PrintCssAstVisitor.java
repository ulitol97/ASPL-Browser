package simpleCss.visitor;

import simpleCss.ast.declaration.Declaration;
import simpleCss.ast.rule.Rule;
import simpleCss.ast.stylesheet.Stylesheet;

public class PrintCssAstVisitor implements Visitor {
	String separator = "   ";

	@Override
	public Object visit(Stylesheet css, Object param) {
		String str = "";
		String ret;
		for (Declaration declaration : css.declarations) {
			str = str + (String) declaration.accept(this, separator);
		}
		ret = "(CSS declarations\n" + str + ")";
		return ret;
	}

	@Override
	public Object visit(Declaration declaration, Object param) {
		String str = "";
		String ret;
		for (Rule rule : declaration.rules) {
			str = str + (String) rule.accept(this, (String) param + separator);
		}
		ret = (String) param + "(" + declaration.selector + "\n" + str
				+ (String) param + ")\n";
		return ret;
	}

	@Override
	public Object visit(Rule rule, Object param) {
		return (String) param + rule.key + " --> " + rule.value + "\n";
	}

}
