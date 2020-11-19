package simpleHtml.ast;

import simpleHtml.visitor.*;

public interface Ast {
	Object accept(Visitor v, Object param);
}
