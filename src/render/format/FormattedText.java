package render.format;

import render.visitor.Visitor;

public class FormattedText implements FormattedElement {

	public String text;
	public String color;
	public int fontSize;
	public String fontStyle;

	public FormattedText(String text, String color, int fontSize,
			String fontStyle) {
		super();
		this.text = text;
		this.color = color;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
	}
	
	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
