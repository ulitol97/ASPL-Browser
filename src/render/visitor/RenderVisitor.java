package render.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import render.format.FormattedText;

public class RenderVisitor implements Visitor {

	// CSS selectors
	private final String SELECTOR_H1 = "h1";
	private final String SELECTOR_H2 = "h2";
	private final String SELECTOR_P = "p";
	private List<String> selectors = new ArrayList<String>();

	// CSS properties
	private final String TEXT_ALIGN = "text-align";
	private final String COLOR = "color";
	private final String FONT_SIZE = "font-size";
	private final String FONT_STYLE = "font-style";

	private List<String> properties = new ArrayList<String>();

	// Base variables
	@SuppressWarnings("unused")
	private AstHtml astHtml;
	private List<AstCss> styles;
	private SearchParamInCssVisitor searchParam = new SearchParamInCssVisitor();
	private FinalStyle finalStyle;

	public RenderVisitor(AstHtml astHtml, List<AstCss> styles) {
		this.astHtml = astHtml;
		this.styles = styles;

		selectors.add(SELECTOR_H1);
		selectors.add(SELECTOR_H2);
		selectors.add(SELECTOR_P);

		properties.add(TEXT_ALIGN);
		properties.add(COLOR);
		properties.add(FONT_SIZE);
		properties.add(FONT_STYLE);
	}

	// Build a formatted page while visiting the ASTs.
	// Visit the HTML while filling with the CSS properties.

	@Override
	public Object visit(Html html, Object param) {
		FormattedPage formattedPage = new FormattedPage();
		// Visit the head and get the document title
		formattedPage.setTitle((String) html.head.accept(this, null));

		// Visit the body, completing the page with the formatted lines.
		@SuppressWarnings("unchecked")
		List<FormattedLine> lines = (List<FormattedLine>) html.body.accept(this,
				formattedPage);

		for (FormattedLine line : lines) {
			formattedPage.add(line);
		}

		return formattedPage;
	}

	@Override
	public Object visit(Head head, Object param) {
		// Get the page title and ignore links (not rendered)
		return head.title.accept(this, null);
	}

	@Override
	public Object visit(Body body, Object param) {

		// Compute the aggregate of all style-sheets linked to the document
		computeFinalStyle();

		// Get contents
		List<FormattedLine> lines = new ArrayList<FormattedLine>();

		// Each element should translate to a line
		for (Element element : body.elements) {
			FormattedLine line = (FormattedLine) element.accept(this, param);
			lines.add(line);
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
		List<FormattedText> texts = new ArrayList<FormattedText>();

		// Recover map of styles from the parameter
		Map<String, String> style = finalStyle.getStyleMap((String) param);

		// Parse all contents inside the tag to formatted texts
		for (Text text : bold.text) {
			// Get string
			String textContent = (String) text.accept(this, null);
			// Create the formatted text
			FormattedText formattedText = new FormattedText(textContent,
					style.get(COLOR), style.get(FONT_SIZE),
					style.get(FONT_STYLE));
			// Add the formatted text
			texts.add(formattedText);
		}

		return texts;
	}

	@Override
	public Object visit(Italic italic, Object param) {
		List<FormattedText> texts = new ArrayList<FormattedText>();

		// Recover map of styles from the parameter
		Map<String, String> style = finalStyle.getStyleMap((String) param);

		// Parse all contents inside the tag to formatted texts
		for (Text text : italic.text) {
			// Get string
			String textContent = (String) text.accept(this, null);
			// Create the formatted text
			FormattedText formattedText = new FormattedText(textContent,
					style.get(COLOR), style.get(FONT_SIZE),
					style.get(FONT_STYLE));
			// Add the formatted text
			texts.add(formattedText);
		}

		return texts;
	}

	@Override
	public Object visit(Underline underline, Object param) {
		List<FormattedText> texts = new ArrayList<FormattedText>();

		// Recover map of styles from the parameter
		Map<String, String> style = finalStyle.getStyleMap((String) param);

		// Parse all contents inside the tag to formatted texts
		for (Text text : underline.text) {
			// Get string
			String textContent = (String) text.accept(this, null);
			// Create the formatted text
			FormattedText formattedText = new FormattedText(textContent,
					style.get(COLOR), style.get(FONT_SIZE),
					style.get(FONT_STYLE));
			// Add the formatted text
			texts.add(formattedText);
		}

		return texts;
	}

	@Override
	public Object visit(Text text, Object param) {
		// It is part of a sentence
		if (param != null) {
			List<FormattedText> texts = new ArrayList<FormattedText>();
			// Recover map of styles from the parameter
			Map<String, String> style = finalStyle.getStyleMap((String) param);
			FormattedText formattedText = new FormattedText(text.text,
					style.get(COLOR), style.get(FONT_SIZE),
					style.get(FONT_STYLE));

			texts.add(formattedText);
			return texts;

		}
		// It is just fetching the title
		return text.text;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(H1 h1, Object param) {
		// Create the formatted line
		String textAlign = finalStyle.getStyleMap(SELECTOR_H1).get(TEXT_ALIGN);
		FormattedLine line = new FormattedLine(textAlign);

		// Each sentence contains a list of formatted texts
		for (Sentence sentence : h1.contents) {
			List<FormattedText> texts = (List<FormattedText>) sentence
					.accept(this, SELECTOR_H1);
			for (FormattedText formattedText : texts) {
				line.add(formattedText);
			}
		}

		return line;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(H2 h2, Object param) {
		// Create the formatted line
		String textAlign = finalStyle.getStyleMap(SELECTOR_H2).get(TEXT_ALIGN);
		FormattedLine line = new FormattedLine(textAlign);

		// Each sentence contains a list of formatted texts
		for (Sentence sentence : h2.contents) {
			List<FormattedText> texts = (List<FormattedText>) sentence
					.accept(this, SELECTOR_H2);
			for (FormattedText formattedText : texts) {
				line.add(formattedText);
			}
		}

		return line;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(P p, Object param) {
		// Create the formatted line
		String textAlign = finalStyle.getStyleMap(SELECTOR_P).get(TEXT_ALIGN);
		FormattedLine line = new FormattedLine(textAlign);

		// Each sentence contains a list of formatted texts
		for (Sentence sentence : p.contents) {
			List<FormattedText> texts = (List<FormattedText>) sentence
					.accept(this, SELECTOR_P);
			for (FormattedText formattedText : texts) {
				line.add(formattedText);
			}
		}

		return line;
	}

	// Apply all style-sheets in cascade to compute a final style guide
	private void computeFinalStyle() {
		FinalStyle fs = new FinalStyle();

		for (String selector : selectors) {
			Map<String, String> selectorStyle = new HashMap<String, String>();
			for (String property : properties) {
				for (AstCss style : styles) {
					
					String value = searchParam.search(selector, property,
							style);
					if (value != null) {
						selectorStyle.put(property, value);
					}

				}
			}
			fs.setStyleMap(selector, selectorStyle);
		}

		this.finalStyle = fs;
	}

	private class FinalStyle {

		public Map<String, String> h1Style;
		public Map<String, String> h2Style;
		public Map<String, String> pStyle;

		public FinalStyle() {
			h1Style = new HashMap<String, String>();
			h2Style = new HashMap<String, String>();
			pStyle = new HashMap<String, String>();
		}

		public Map<String, String> getStyleMap(String key) {
			if (SELECTOR_H1.equals(key))
				return h1Style;
			else if (SELECTOR_H2.equals(key))
				return h2Style;
			else
				return pStyle;
		}

		public void setStyleMap(String key, Map<String, String> styleMap) {
			if (SELECTOR_H1.equals(key))
				h1Style = styleMap;
			else if (SELECTOR_H2.equals(key))
				h2Style = styleMap;
			else
				pStyle = styleMap;
		}

	}
}
