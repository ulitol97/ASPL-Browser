package render.format;

import java.util.List;

import render.visitor.Visitor;

public class FormattedLine implements FormattedElement {

	private String textAlign;
	public List<FormattedText> contents;

	public FormattedLine() {
	}

	public FormattedLine(String textAlign) {
		this.textAlign = textAlign;
	}

	public void add(FormattedText text) {
		this.contents.add(text);
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
