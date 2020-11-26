package css.ast;

import css.visitor.Visitor;

public interface AstCss {
	Object accept(Visitor v, Object param);
}
