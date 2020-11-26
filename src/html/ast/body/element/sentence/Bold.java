package html.ast.body.element.sentence;

import java.util.List;

import html.visitor.Visitor;

public class Bold implements Sentence {

	public List<Text> text;

	public Bold(List<Text> text) {
		this.text = text;
	}

	@Override
	public String getContent() {
		String str = "";
		for (Text t : text) {
			str += t + " ";
		}
		return str;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
