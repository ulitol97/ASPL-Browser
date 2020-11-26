package html.ast.body.element.sentence;

import html.ast.AstHtml;
import html.visitor.Visitor;

public interface Sentence extends AstHtml {
	
	// Bold, italic, underline...
	
	Object accept(Visitor v, Object param);
	
	String getContent();	

}
