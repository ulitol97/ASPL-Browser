package html.ast.head;

import java.util.List;

import html.ast.AstHtml;
import html.visitor.Visitor;

public class Head implements AstHtml {
	
	public Title title;
	public List<Link> links;

	public Head(Title title, List<Link> links) {
		this.title = title;
		this.links = links;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
