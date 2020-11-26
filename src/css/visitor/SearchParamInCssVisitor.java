package css.visitor;

import css.ast.AstCss;
import css.ast.declaration.Declaration;
import css.ast.rule.Rule;
import css.ast.stylesheet.Stylesheet;

public class SearchParamInCssVisitor implements Visitor {
	
	String key = null, selector = null;

	@Override
	public Object visit(Stylesheet css, Object param) {
		for (Declaration declaration : css.declarations) {
			if (declaration.selector.equals(selector)) {
				return (String) declaration.accept(this, null);
			}
		}
		
		return null;
	}

	@Override
	public Object visit(Declaration declaration, Object param) {
		for (Rule rule : declaration.rules) {
			if (rule.key.equals(key)) {
				return (String) rule.accept(this, null);
			}
		}
		
		return null;
	}

	@Override
	public Object visit(Rule rule, Object param) {
		return rule.value;
	}
	
	public String search (String selector, String key, AstCss stylesheet) {
		this.selector = selector;
		this.key = key;
		
		if (selector == null || key == null) return null;
		
		return (String) stylesheet.accept(this, null);
	}


}
