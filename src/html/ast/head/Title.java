package html.ast.head;

import java.util.List;

import html.ast.AstHtml;
import html.ast.body.element.sentence.Text;
import html.visitor.Visitor;

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
