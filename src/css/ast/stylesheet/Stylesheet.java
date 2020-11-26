package css.ast.stylesheet;

import java.util.List;

import css.ast.AstCss;
import css.ast.declaration.Declaration;
import css.visitor.Visitor;

public class Stylesheet implements AstCss {

	public List<Declaration> declarations;
	
	public Stylesheet(List<Declaration> declarations) {
		this.declarations = declarations;
	}
	
	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
