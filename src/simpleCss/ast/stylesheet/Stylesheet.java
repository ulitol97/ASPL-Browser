package simpleCss.ast.stylesheet;

import java.util.List;

import simpleCss.ast.AstCss;
import simpleCss.ast.declaration.Declaration;
import simpleCss.visitor.Visitor;

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
