package simpleCss.ast.declaration;

import java.util.List;

import simpleCss.ast.AstCss;
import simpleCss.ast.rule.Rule;
import simpleCss.visitor.Visitor;

public class Declaration implements AstCss {

	public String selector;
	public List<Rule> rules;

	public Declaration(String selector, List<Rule> rules) {
		this.selector = selector;
		this.rules = rules;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
