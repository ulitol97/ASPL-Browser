package simpleHtml.visitor;

import simpleHtml.ast.body.Body;
import simpleHtml.ast.body.element.H1;
import simpleHtml.ast.body.element.H2;
import simpleHtml.ast.body.element.P;
import simpleHtml.ast.body.element.sentence.Bold;
import simpleHtml.ast.body.element.sentence.Italic;
import simpleHtml.ast.body.element.sentence.Text;
import simpleHtml.ast.body.element.sentence.Underline;
import simpleHtml.ast.head.Head;
import simpleHtml.ast.head.Link;
import simpleHtml.ast.head.Title;
import simpleHtml.ast.html.Html;

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
