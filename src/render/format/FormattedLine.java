package render.format;

import java.util.ArrayList;
import java.util.List;

import render.visitor.Visitor;

public class FormattedLine implements FormattedElement {

	private String textAlign;
	public List<FormattedText> contents;

	public FormattedLine() {
		contents = new ArrayList<FormattedText>();
	}

	public FormattedLine(String textAlign) {
		contents = new ArrayList<FormattedText>();
		this.textAlign = textAlign;
	}
	
	public int getMetrics() {
		int acc = 0;
		for (FormattedText formattedText : contents) {
			acc += formattedText.getMetrics();
		}
		return acc;
	}

	public void add(FormattedText text) {
		this.contents.add(text);
	}
	
	public String getTextAlign() {
		return this.textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
