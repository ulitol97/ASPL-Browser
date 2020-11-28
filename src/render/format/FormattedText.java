package render.format;

import render.visitor.Visitor;

public class FormattedText implements FormattedElement {

	public String text;
	public String color;
	public int fontSize;
	public String fontStyle;

	public FormattedText() {
	};

	public FormattedText(String text, String color, String fontSize,
			String fontStyle) {
		super();
		this.text = text;
		this.color = color;
		this.fontSize = fontSizeToInt(fontSize);
		this.fontStyle = fontStyle;
	}
	
	// "32px" => 32
	private int fontSizeToInt (String sizePx) {
		int index = sizePx.indexOf("px");
		String number = sizePx.substring(0, index);
		return Integer.parseInt(number);
	}
	

	public int getMetrics() {
		if (text != null)
			return text.length()*fontSize;
		return 0;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
