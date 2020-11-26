package html.visitor;

import java.util.HashSet;
import java.util.Set;

import html.ast.body.Body;
import html.ast.body.element.H1;
import html.ast.body.element.H2;
import html.ast.body.element.P;
import html.ast.body.element.sentence.Bold;
import html.ast.body.element.sentence.Italic;
import html.ast.body.element.sentence.Text;
import html.ast.body.element.sentence.Underline;
import html.ast.head.Head;
import html.ast.head.Link;
import html.ast.head.Title;
import html.ast.html.Html;

public class FindCssAstVisitor implements Visitor {

	@Override
	public Object visit(Html html, Object param) {
		return html.head.accept(this, null);
	}

	@Override
	public Object visit(Head head, Object param) {
		Set<String> styles = new HashSet<>();
		for (Link link : head.links) {
			styles.add((String) link.accept(this, null));
		}
		return styles;
	}

	@Override
	public Object visit(Title title, Object param) {
		return null;
	}

	@Override
	public Object visit(Link link, Object param) {
		return link.href.accept(this, null);
	}

	@Override
	public Object visit(Body body, Object param) {
		return null;
	}

	@Override
	public Object visit(H1 h1, Object param) {
		return null;
	}

	@Override
	public Object visit(H2 h2, Object param) {
		return null;
	}

	@Override
	public Object visit(P p, Object param) {
		return null;
	}

	@Override
	public Object visit(Text text, Object param) {
		return text.text;
	}

	@Override
	public Object visit(Bold bold, Object param) {
		return null;
	}

	@Override
	public Object visit(Italic italic, Object param) {
		return null;
	}

	@Override
	public Object visit(Underline underline, Object param) {
		return null;
	}

}
