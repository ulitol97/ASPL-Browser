package simpleHtml.visitor;

import simpleHtml.ast.body.Body;
import simpleHtml.ast.body.element.Element;
import simpleHtml.ast.body.element.H1;
import simpleHtml.ast.body.element.H2;
import simpleHtml.ast.body.element.P;
import simpleHtml.ast.body.element.sentence.Bold;
import simpleHtml.ast.body.element.sentence.Italic;
import simpleHtml.ast.body.element.sentence.Sentence;
import simpleHtml.ast.body.element.sentence.Text;
import simpleHtml.ast.body.element.sentence.Underline;
import simpleHtml.ast.head.Head;
import simpleHtml.ast.head.Link;
import simpleHtml.ast.head.Title;
import simpleHtml.ast.html.Html;

public class PrintHtmlAstVisitor implements Visitor {
	String sp = "   ";

	@Override
	public Object visit(Html html, Object param) {
		String ret;
		String head;
		String body;
		head = (String) html.head.accept(this, sp);
		body = (String) html.body.accept(this, sp);
		ret = "(Program\n" + head + "\n" + body + "\n)";
		return ret;
	}

	@Override
	public Object visit(Head head, Object param) {
		String ret, title;
		title = (String) head.title.accept(this, (String) param + sp);

		String slinks = (String) param + "(links\n";

		for (Link link : head.links)
			slinks = slinks + (String) link.accept(this, (String) param + sp)
					+ "\n";

		slinks = slinks + (String) param + ")";

		ret = sp + "(Head\n" + title + "\n" + slinks + "\n" + sp + ")";
		return ret;
	}

	@Override
	public Object visit(Title title, Object param) {
		String sTitle = "";
		for (Text text : title.title) {
			sTitle += text.toString() + " ";
		}
		return (String) param + "(title -> " + sTitle.strip() + ")";
	}

//	@Override
//	public Object visit(Link link, Object param) {
//		String ret = "";
//		ret = (String) param + sp + sp + "(link " + "\n" + sp + sp + sp
//				+ " href -> <" + link.href.accept(this, (String) param) + ">\n"
//				+ sp + sp + sp + " rel -> <"
//				+ link.rel.accept(this, (String) param) + ">\n" + sp + sp + sp
//				+ " type -> <" + link.type.accept(this, (String) param) + ">"
//				+ "\n" + sp + sp + ")";
//		return ret;
//	}

	@Override
	public Object visit(Link link, Object param) {
		String ret = "";
		ret = (String) param + "(link href -> <" + link.href.accept(this, "")
				+ "> rel -> <" + link.rel.accept(this, "") + "> type -> <"
				+ link.type.accept(this, "") + ">)";
		return ret;
	}

	@Override
	public Object visit(Body body, Object param) {
		String ret = (String) param + "(body\n";
		for (Element element : body.elements)
			ret = ret + (String) element.accept(this, (String) param + sp)
					+ "\n";
		ret = ret + (String) param + ")";
		return ret;
	}

	@Override
	public Object visit(H1 h1, Object param) {
		String ret = (String) param + "(header 1\n";
		for (Sentence sentence : h1.contents)
			ret = ret + (String) sentence.accept(this, (String) param + sp)
					+ "\n";
		ret = ret + (String) param + ")";
		return ret;
	}

	@Override
	public Object visit(H2 h2, Object param) {
		String ret = (String) param + "(header 2\n";
		for (Sentence sentence : h2.contents)
			ret = ret + (String) sentence.accept(this, (String) param + sp)
					+ "\n";
		ret = ret + (String) param + ")";
		return ret;
	}

	@Override
	public Object visit(P p, Object param) {
		String ret = (String) param + "(paragraph\n";
		for (Sentence sentence : p.contents)
			ret = ret + (String) sentence.accept(this, (String) param + sp)
					+ "\n";
		ret = ret + (String) param + ")";
		return ret;
	}

	@Override
	public Object visit(Text text, Object param) {
		String ret = (String) param + "(TEXT -> ";
		ret = ret + text.text + ")";
		return ret;
	}

	@Override
	public Object visit(Bold bold, Object param) {

		String stexts = (String) param + sp + "(texts\n";
		for (Text text : bold.text)
			stexts = stexts + (String) text.accept(this, (String) param + sp + sp)
					+ "\n";

		stexts = stexts + (String) param + sp + ")";

		String ret = (String) param + "(bold\n" + stexts + "\n" + (String) param + ")";

		return ret;
	}

	@Override
	public Object visit(Italic italic, Object param) {
		String stexts = (String) param + sp + "(texts\n";
		for (Text text : italic.text)
			stexts = stexts + (String) text.accept(this, (String) param + sp + sp)
					+ "\n";

		stexts = stexts + (String) param + sp + ")";

		String ret = (String) param + "(italic\n" + stexts + "\n" + (String) param + ")";

		return ret;
	}

	@Override
	public Object visit(Underline underline, Object param) {
		String stexts = (String) param + sp + "(texts\n";
		for (Text text : underline.text)
			stexts = stexts + (String) text.accept(this, (String) param + sp + sp)
					+ "\n";

		stexts = stexts + (String) param + sp + ")";

		String ret = (String) param + "(underline\n" + stexts + "\n" + (String) param + ")";

		return ret;
	}

}
