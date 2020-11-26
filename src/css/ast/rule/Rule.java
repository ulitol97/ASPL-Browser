package css.ast.rule;

import css.ast.AstCss;
import css.visitor.Visitor;

public class Rule implements AstCss {

	public String key;
	public String value;

	public Rule(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public Rule(String value) {
		this.value = value;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
