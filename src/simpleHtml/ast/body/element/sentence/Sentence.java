package simpleHtml.ast.body.element.sentence;

import simpleHtml.ast.AstHtml;
import simpleHtml.visitor.Visitor;

public interface Sentence extends AstHtml {
	
	// Bold, italic, underline...
	
	Object accept(Visitor v, Object param);
	
	String getContent();	

}
