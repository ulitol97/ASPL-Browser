package simpleHtml.ast;

import simpleHtml.visitor.*;

public interface AstHtml {
	Object accept(Visitor v, Object param);
}
