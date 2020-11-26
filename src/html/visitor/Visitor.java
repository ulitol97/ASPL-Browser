package html.visitor;

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

public interface Visitor {

	Object visit(Html p, Object param);

	Object visit(Head head, Object param);

	Object visit(Body body, Object param);

	Object visit(Title title, Object param);

	Object visit(Link link, Object param);

	Object visit(Bold bold, Object param);

	Object visit(Italic italic, Object param);

	Object visit(Underline underline, Object param);

	Object visit(Text text, Object param);

	Object visit(H1 h1, Object param);

	Object visit(H2 h2, Object param);

	Object visit(P p, Object param);

}
