package simpleHtml.visitor;

import simpleHtml.ast.*;

public interface Visitor {
	Object visit(Program p, Object param);

}
