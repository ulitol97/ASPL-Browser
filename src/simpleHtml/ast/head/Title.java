package simpleHtml.ast.head;

import java.util.List;

import simpleHtml.ast.AstHtml;
import simpleHtml.ast.body.element.sentence.Text;
import simpleHtml.visitor.Visitor;

public class Title implements AstHtml {

	public List<Text> title;

	public Title(List<Text> title) {
		this.title = title;
	}
	
	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}


}
