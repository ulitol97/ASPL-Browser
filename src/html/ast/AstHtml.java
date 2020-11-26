package html.ast;

import html.visitor.*;

public interface AstHtml {
	Object accept(Visitor v, Object param);
}
