package render.visitor;

import java.util.ArrayList;
import java.util.List;
import css.ast.AstCss;
import css.visitor.SearchParamInCssVisitor;
import html.ast.AstHtml;
import html.ast.body.Body;
import html.ast.body.element.Element;
import html.ast.body.element.H1;
import html.ast.body.element.H2;
import html.ast.body.element.P;
import html.ast.body.element.sentence.Bold;
import html.ast.body.element.sentence.Italic;
import html.ast.body.element.sentence.Sentence;
import html.ast.body.element.sentence.Text;
import html.ast.body.element.sentence.Underline;
import html.ast.head.Head;
import html.ast.head.Link;
import html.ast.head.Title;
import html.ast.html.Html;
import html.visitor.Visitor;
import render.format.FormattedLine;
import render.format.FormattedPage;

public class RenderVisitor implements Visitor {

	// CSS selectors
	private final String SELECTOR_H1 = "h1";
	private final String SELECTOR_H2 = "h2";
	private final String SELECTOR_P = "p";

	// CSS properties
	private final String TEXT_ALIGN = "text-align";
	private final String COLOR = "color";
	private final String FONT_SIZE = "font-size";
	private final String FONT_STYLE = "font-style";

	private AstHtml astHtml;
	private List<AstCss> styles;
	private SearchParamInCssVisitor searchParam = new SearchParamInCssVisitor();

	public RenderVisitor(AstHtml astHtml, List<AstCss> styles) {
		this.astHtml = astHtml;
		this.styles = styles;
	}

	// Build a formatted page while visiting the ASTs.
	// Visit the HTML while filling with the CSS properties.

	@Override
	public Object visit(Html html, Object param) {
		FormattedPage formattedPage = new FormattedPage();
		// Visit the head and get the document title
		formattedPage.setTitle((String) html.head.accept(this, null));

		// Visit the body and get the page lines
		@SuppressWarnings("unchecked")
		List<FormattedLine> lines = (List<FormattedLine>) html.body.accept(this,
				formattedPage);

		for (FormattedLine line : lines) {
			formattedPage.add(line);
		}

		System.err.println(String.format("PRINT: %s", formattedPage));

		return formattedPage;
	}

	@Override
	public Object visit(Head head, Object param) {
		// Get the page title and ignore links (not rendered)
		return head.title.accept(this, null);
	}

	@Override
	public Object visit(Body body, Object param) {

		List<FormattedLine> lines = new ArrayList<FormattedLine>();

		// Each element should translate to a line

		// Cascade styles: for the line, apply the text-align property of the
		// last CSS file.
		for (Element element : body.elements) {
			FormattedLine line = new FormattedLine();
			// TODO
		}

		return lines;
	}

	@Override
	public Object visit(Title title, Object param) {

		String sTitle = "";

		for (Text text : title.title) {
			sTitle += text.accept(this, param) + " ";
		}

		return sTitle.strip();
	}

	@Override
	public Object visit(Link link, Object param) {
		return null;
	}

	@Override
	public Object visit(Bold bold, Object param) {

		return null;
	}

	@Override
	public Object visit(Italic italic, Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Underline underline, Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Text text, Object param) {
		return text.text;
	}

	@Override
	public Object visit(H1 h1, Object param) {
		// For each element inside, apply each style in cascade
//		for (Sentence sentence : h1.contents) {
//			FormattedLine line = new FormattedLine();
//			for (AstCss style : styles) {
//				// Form the line with each style
//				FormattedLine innerLine = (FormattedLine) element.accept(this,
//						style);
//			}
//			// Aggregate all styles into the final line?
//			// TODO
//		}
		return null;
	}

	@Override
	public Object visit(H2 h2, Object param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(P p, Object param) {
//		String alignment = searchParam.search(SELECTOR_P, TEXT_ALIGN, stylesheet)
//		FormattedLine line = new FormattedLine()
		return null;
	}
}
