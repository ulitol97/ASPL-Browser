package render.format;

import java.util.List;

import render.visitor.Visitor;

public class FormattedPage implements FormattedElement {

	private String title;
	private List<FormattedLine> lines;

	public FormattedPage() {
	}

	public FormattedPage(List<FormattedLine> lines) {
		this.lines = lines;
	}

	public void add(FormattedLine line) {
		this.lines.add(line);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}

}
