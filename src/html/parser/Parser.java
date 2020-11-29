package html.parser;

import java.util.*;

import html.ast.*;
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
import html.parser.Token;
import html.parser.TokensId;

public class Parser {

	Lexicon lex;

	// Error management
	private List<String> errors = new ArrayList<String>();
	private boolean syntaxError = false;

	public Parser(Lexicon lex) {
		this.lex = lex;
	}

	public AstHtml parse() {
		return html();
	}

	private Html html() {

		// Expect <html>
		Token tok = lex.getToken();
		if (tok.getToken() != TokensId.OPENHTML) {
			syntaxError(String.format(
					"Expected '<html>' in the beginning of the document, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		Head head = head();
		Body body = body();

		tok = lex.getToken();
		if (tok.getToken() != TokensId.CLOSEHTML) {
			syntaxError(String.format(
					"Expected '</html>' in the end of the document, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new Html(head, body);
	}

	private Head head() {

		// Expect <head>
		Token tok = lex.getToken();
		if (tok.getToken() != TokensId.OPENHEAD) {
			syntaxError(String.format(
					"Expected '<head>' after '<html>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Look for title
		Title title = title();

		// Look for CSS links
		List<Link> links = links();

		// Expect <head>
		tok = lex.getToken();
		if (tok.getToken() != TokensId.CLOSEHEAD) {
			syntaxError(String.format(
					"Expected '</head>' closing the '<head>' tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new Head(title, links);
	}

	private Title title() {

		// Expect <title>
		Token tok = lex.getToken();
		if (tok.getToken() != TokensId.OPENTITLE) {
			syntaxError(String.format(
					"Expected '<title>' after <head> tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Look for title text
		List<Text> titleText = texts();

		// Expect </title>
		tok = lex.getToken();
		if (tok.getToken() != TokensId.CLOSETITLE) {
			syntaxError(String.format(
					"Expected '</title>' closing a <title> tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new Title(titleText);
	}

	private List<Link> links() {

		List<Link> links = new ArrayList<Link>();
		Link link;

		Token tok = lex.getToken();

		// Expect '<link'
		while (tok.getToken() == TokensId.OPENLINK) {
			// Fill links
			lex.returnLastToken();
			link = link();
			if (link != null) {
				links.add(link);
			}

			tok = lex.getToken();
		}

		// We consumed an extra token to check for more links
		lex.returnLastToken();

		if (syntaxError)
			return null;

		return links;
	}

	private Link link() {

		// Grammar: link: OPENLINK href rel type CLOSELINK
		// Example: <link href="EX1.css" rel="stylesheet" type="text/css">

		Link link = null;

		Token tok = lex.getToken();

		// Expect '<link' (we know it is OK)
		if (tok.getToken() != TokensId.OPENLINK) {
			syntaxError(String.format("Expected '<link', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect 'href'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.HREF) {
			syntaxError(String.format(
					"Expected 'href' in link declaration, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '='
		tok = lex.getToken();
		if (tok.getToken() != TokensId.EQ) {
			syntaxError(String.format(
					"Expected '=' after attribute 'href', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '"'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.QUOTE) {
			syntaxError(String.format(
					"Expected '\"' surrounding the value of 'href', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect href
		Text href = href();

		// Expect '"'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.QUOTE) {
			syntaxError(String.format(
					"Expected '\"' surrounding the value of 'href', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect 'rel'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.REL) {
			syntaxError(String.format(
					"Expected 'rel' in link declaration, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '='
		tok = lex.getToken();
		if (tok.getToken() != TokensId.EQ) {
			syntaxError(String.format(
					"Expected '=' after attribute 'rel', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '"'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.QUOTE) {
			syntaxError(String.format(
					"Expected '\"' surrounding the value of 'rel', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect rel
		Text rel = rel();

		// Expect '"'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.QUOTE) {
			syntaxError(String.format(
					"Expected '\"' surrounding the value of 'rel', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect 'type'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.TYPE) {
			syntaxError(String.format(
					"Expected 'type' in link declaration, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '='
		tok = lex.getToken();
		if (tok.getToken() != TokensId.EQ) {
			syntaxError(String.format(
					"Expected '=' after attribute 'type', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '"'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.QUOTE) {
			syntaxError(String.format(
					"Expected '\"' surrounding the value of 'type', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect type
		Text type = type();

		// Expect '"'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.QUOTE) {
			syntaxError(String.format(
					"Expected '\"' surrounding the value of 'type', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect '>'
		tok = lex.getToken();
		if (tok.getToken() != TokensId.TAGCLOSE) {
			syntaxError(String.format(
					"Expected '>' closing the tag 'link', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (href != null && rel != null && type != null)
			link = new Link(href, rel, type);

		if (syntaxError)
			return null;

		return link;
	}

	private Text href() {

		// Look for href text
		Text href = text();

		if (syntaxError)
			return null;

		return href;
	}

	private Text rel() {
		// Look for rel text
		Text rel = text();

		if (syntaxError)
			return null;

		return rel;
	}

	private Text type() {
		// Look for type text
		Text type = text();

		if (syntaxError)
			return null;

		return type;
	}

	private Body body() {

		Token tok = lex.getToken();

		// Expect '<body>'
		if (tok.getToken() != TokensId.OPENBODY) {
			syntaxError(String.format(
					"Expected '<body>' after '<head>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		List<Element> elements = elements();

		tok = lex.getToken();
		// Expect '</body>'
		if (tok.getToken() != TokensId.CLOSEBODY) {
			syntaxError(String.format(
					"Expected '</body>' closing the <body> tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new Body(elements);
	}

	private List<Element> elements() {

		List<Element> elements = new ArrayList<Element>();
		Element element = element();

		while (element != null) {
//			lex.returnLastToken();
			elements.add(element);
			element = element();

		}

		// We consumed an extra token to check for more elements
		lex.returnLastToken();

		return elements;
	}

	private Element element() {
		Element element = null;

		Token tok = lex.getToken();

		// Look for H1, H2, P...
		switch (tok.getToken()) {
		case OPENH1:
			lex.returnLastToken();
			element = h1();
			break;

		case OPENH2:
			lex.returnLastToken();
			element = h2();
			break;

		case OPENP:
			lex.returnLastToken();
			element = p();
			break;

		default:
			// No element matched
//			syntaxError(String.format(
//					"Expected an element tag '<{element}>' inside 'body', found '%s'",
//					tok.getLexeme()), tok.getLine());
			element = null;
			break;
		}

		return element;
	}

	private H1 h1() {

		Token tok = lex.getToken();

		// Expect '<h1>' (we know it is OK)
		if (tok.getToken() != TokensId.OPENH1) {
			syntaxError(String.format("Expected '<h1>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		List<Sentence> sentences = sentences(tok);

		tok = lex.getToken();
		// Expect '</h1>'
		if (tok.getToken() != TokensId.CLOSEH1) {
			syntaxError(String.format(
					"Expected '</h1>' closing a <h1> tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new H1(sentences);
	}

	private H2 h2() {

		Token tok = lex.getToken();

		// Expect '<h2>' (we know it is OK)
		if (tok.getToken() != TokensId.OPENH2) {
			syntaxError(String.format("Expected '<h2>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		List<Sentence> sentences = sentences(tok);

		tok = lex.getToken();
		// Expect '</h2>'
		if (tok.getToken() != TokensId.CLOSEH2) {
			syntaxError(String.format(
					"Expected '</h2>' closing a <h2> tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new H2(sentences);
	}

	private P p() {

		Token tok = lex.getToken();

		// Expect '<p>' (we know it is OK)
		if (tok.getToken() != TokensId.OPENP) {
			syntaxError(String.format("Expected '<p>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		List<Sentence> sentences = sentences(tok);

		tok = lex.getToken();
		// Expect '</p>'
		if (tok.getToken() != TokensId.CLOSEP) {
			syntaxError(String.format(
					"Expected '</p>' closing a <p> tag, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		if (syntaxError)
			return null;

		return new P(sentences);
	}

	private List<Sentence> sentences(Token predecessor) {

		List<Sentence> sentences = new ArrayList<Sentence>();
		Sentence sentence = sentence(predecessor);

		while (sentence != null) {
			sentences.add(sentence);
			sentence = sentence(predecessor);
		}
		// We consumed an extra token to check for more sentences
		lex.returnLastToken();

		return sentences;
	}

	private Sentence sentence(Token predecessor) {

		Sentence sentence = null;

		Token tok = lex.getToken();

		// Look for b, i, u... or ,eventually, text...
		switch (tok.getToken()) {
		case OPENBOLD:
			lex.returnLastToken();
			sentence = bold();
			break;

		case OPENITALIC:
			lex.returnLastToken();
			sentence = italic();
			break;

		case OPENUNDERLINE:
			lex.returnLastToken();
			sentence = underline();
			break;

		case TEXT:
			lex.returnLastToken();
			sentence = text();
			break;

		default:
			// No element matched
//			syntaxError(String.format(
//					"Expected formatted or raw text inside '%s', found '%s'",
//					predecessor.getLexeme(), tok.getLexeme()), tok.getLine());
			sentence = null;
			break;
		}

		if (syntaxError)
			return null;

		return sentence;

	}

	private Bold bold() {

		Bold bold = null;

		List<Text> texts = null;
		Token tok = lex.getToken();

		// Expect '<b>' (we know it is OK)
		if (tok.getToken() != TokensId.OPENBOLD) {
			syntaxError(String.format("Expected '<b>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect inner text
		texts = texts();

		tok = lex.getToken();
		// Expect '</b>'
		if (tok.getToken() != TokensId.CLOSEBOLD) {
			syntaxError(
					String.format("Expected '</b>' after '<b>' tag, found '%s'",
							tok.getLexeme()),
					tok.getLine());
		}

		if (syntaxError)
			return null;

		if (texts != null)
			bold = new Bold(texts);

		return bold;
	}

	private Italic italic() {

		Italic italic = null;

		List<Text> texts = null;
		Token tok = lex.getToken();

		// Expect '<i>' (we know it is OK)
		if (tok.getToken() != TokensId.OPENITALIC) {
			syntaxError(String.format("Expected '<i>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect inner text
		texts = texts();

		tok = lex.getToken();
		// Expect '</i>'
		if (tok.getToken() != TokensId.CLOSEITALIC) {
			syntaxError(
					String.format("Expected '</i>' after '<i>' tag, found '%s'",
							tok.getLexeme()),
					tok.getLine());
		}

		if (syntaxError)
			return null;

		if (texts != null)
			italic = new Italic(texts);

		return italic;
	}

	private Underline underline() {

		Underline underline = null;

		List<Text> texts = null;
		Token tok = lex.getToken();

		// Expect '<u>' (we know it is OK)
		if (tok.getToken() != TokensId.OPENUNDERLINE) {
			syntaxError(String.format("Expected '<u>', found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect inner text
		texts = texts();

		tok = lex.getToken();
		// Expect '</u>'
		if (tok.getToken() != TokensId.CLOSEUNDERLINE) {
			syntaxError(
					String.format("Expected '</u>' after '<u>' tag, found '%s'",
							tok.getLexeme()),
					tok.getLine());
		}

		if (syntaxError)
			return null;

		if (texts != null)
			underline = new Underline(texts);

		return underline;
	}

	private List<Text> texts() {

		List<Text> texts = new ArrayList<Text>();
		Text text = text();

		while (text != null) {
			texts.add(text);

			text = text();
		}

		// We consumed an extra token to check for more texts
		lex.returnLastToken();

		if (syntaxError)
			return null;

		return texts;

	}

	private Text text() {

		String text = "";
		Token tok = lex.getToken();

		// Expect raw text
		if (tok.getToken() != TokensId.TEXT) {
//			syntaxError(String.format("Expected some text, found '%s'",
//					tok.getLexeme()), tok.getLine());
			return null;
		} else
			text = tok.getLexeme();

		if (syntaxError)
			return null;

		return new Text(text);
	}

	/* ERROR MANAGEMENT */

	public boolean hasErrors() {
		return this.syntaxError;
	}

	private void syntaxError(String message, int line) {
		// Format error and add to error list
		String error = String.format("Syntax error: %s on line %d.", message,
				line);
		errors.add(error);

		this.syntaxError = true;
	}

	// Print all errors together on the err output stream
	public void printErrors() {
		if (this.syntaxError) {
			System.err.println("\nErrors found running the parser:");
			for (String error : errors) {
				System.err.println("\t => " + error);
			}
		}
	}

	public String getErrors() {
		if (this.syntaxError) {
			String ret = "\nErrors found running the parser:\n";
			for (String error : errors) {
				ret += "\t => " + error + "\n";
			}
			return ret;
		}
		return "Unknown";
	}

}
