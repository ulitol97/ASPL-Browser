package css.ast.declaration;

import java.util.List;

import css.ast.AstCss;
import css.ast.rule.Rule;
import css.visitor.Visitor;

public class Declaration implements AstCss {

	public String selector;
	public List<Rule> rules;

	public Declaration(String selector, List<Rule> rules) {
		this.selector = selector;
		this.rules = rules;
	}
	
	public Declaration(List<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
