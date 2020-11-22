package simpleCss.parser;

import java.util.ArrayList;
import java.util.List;

import simpleCss.ast.AstCss;
import simpleCss.ast.declaration.Declaration;
import simpleCss.ast.rule.Rule;
import simpleCss.ast.stylesheet.Stylesheet;

public class Parser {

	private Lexicon lex;

	// Error management
	private List<String> errors = new ArrayList<String>();
	private boolean syntaxError = false;

	public Parser(Lexicon lex) {
		this.lex = lex;
	}

	public AstCss parse() {
		return stylesheet();
	}

	private Stylesheet stylesheet() {

		List<Declaration> declarations = declarations();
		return new Stylesheet(declarations);
	}

	private List<Declaration> declarations() {

		List<Declaration> declarations = new ArrayList<Declaration>();
		Declaration declaration;
		String selector = selector();

		// Look for selector
		while (selector != null) {
			// Fill declarations
			lex.returnLastToken();
			declaration = declaration();
			if (declaration != null) {
				declaration.selector = selector;
				declarations.add(declaration);
			}

			selector = selector();
		}

		if (syntaxError)
			return null;
		return declarations;

	}

	private Declaration declaration() {

		Declaration declaration = null;

		// Expect selector
		String selector = selector();

		Token tok = lex.getToken();
		// Expect '{'
		if (tok.getToken() != TokensId.OPENBRACE) {
			syntaxError(
					String.format("Expected '{' before CSS block, found '%s'",
							tok.getLexeme()),
					tok.getLine());
		}

		// Expect rules
		List<Rule> rules = rules();

		tok = lex.getToken();
		// Expect '}
		if (tok.getToken() != TokensId.CLOSEBRACE) {
			syntaxError(
					String.format("Expected '}' after CSS block, found '%s'",
							tok.getLexeme()),
					tok.getLine());
		}

		if (syntaxError)
			return null;

		if (selector != null && rules != null) {
			declaration = new Declaration(selector, rules);
		}
		return declaration;

	}

	private List<Rule> rules() {
		List<Rule> rules = new ArrayList<Rule>();

		// Expect keys
		Rule rule;
		String key = key();

		// Look for key
		while (key != null) {
			// Fill rules
			lex.returnLastToken();
			rule = rule();
			if (rule != null) {
				rule.key = key;
				rules.add(rule);
			}

			key = key();
		}

		if (syntaxError)
			return null;
		return rules;
	}

	private Rule rule() {

		Rule rule = null;

		// Expect key
		String key = key();

		// Save key for deciding the value later
		lex.returnLastToken();
		Token keyToken = lex.getToken();

		Token tok = lex.getToken();
		// Expect ':'
		if (tok.getToken() != TokensId.COLON) {
			syntaxError(String.format(
					"Expected ':' between CSS key and its value, found '%s'",
					tok.getLexeme()), tok.getLine());
		}

		// Expect value
		String value = null;
		switch (keyToken.getToken()) {
		case COLOR:
			// We need a color related value
			value = color();
			break;

		case FONTSIZE:
			// We need a font-size related value
			value = size();
			break;

		case TEXTALIGN:
			// We need an alignment related value
			value = alignment();
			break;

		case FONTSTYLE:
			// We need a font-style related value
			value = fontStyle();
			break;

		default:
			syntaxError(String.format(
					"Expected a valid CSS property value, found '%s'",
					tok.getLexeme()), tok.getLine());
			break;
		}

		tok = lex.getToken();
		// Expect ';'
		if (tok.getToken() != TokensId.SEMICOLON) {
			syntaxError(
					String.format("Expected ';' to close CSS rule, found '%s'",
							tok.getLexeme()),
					tok.getLine());
		}

		if (syntaxError)
			return null;

		if (key != null && value != null) {
			rule = new Rule(key, value);
		}
		return rule;

	}

	private String selector() {

		Token tok = lex.getToken();

		String selector = null;

		switch (tok.getToken()) {

		// No selector found, CSS file ends
		case EOF:
			lex.returnLastToken();
			break;
		case H1:
			selector = "h1";
			break;

		case H2:
			selector = "h2";
			break;

		case P:
			selector = "p";
			break;

		default:
			syntaxError(String.format("Expected a CSS selector, found '%s'",
					tok.getLexeme()), tok.getLine());
			break;
		}

		return selector;
	}

	private String key() {

		Token tok = lex.getToken();
		String key = null;

		switch (tok.getToken()) {
		// Return null so that the parser stops looking for rules
		case CLOSEBRACE:
			lex.returnLastToken();
			break;
		case COLOR:
			key = "color";
			break;

		case FONTSIZE:
			key = "font-size";
			break;

		case TEXTALIGN:
			key = "text-align";
			break;

		case FONTSTYLE:
			key = "font-style";
			break;

		default:
			syntaxError(
					String.format("Expected a CSS property name, found '%s'",
							tok.getLexeme()),
					tok.getLine());
			break;
		}

		return key;
	}

	private String color() {

		Token tok = lex.getToken();
		String color = null;

		switch (tok.getToken()) {
		case RED:
			color = "red";
			break;

		case GREEN:
			color = "green";
			break;

		case BLUE:
			color = "blue";
			break;

		case BLACK:
			color = "black";
			break;

		case WHITE:
			color = "white";
			break;

		default:
			syntaxError(String.format("Expected a CSS color value, found '%s'",
					tok.getLexeme()), tok.getLine());
			break;
		}

		return color;
	}

	private String size() {

		Token tok = lex.getToken();
		String size = null;

		switch (tok.getToken()) {
		case SIZE:
			size = tok.getLexeme();
			break;

		default:
			syntaxError(
					String.format("Expected a CSS size value (px), found '%s'",
							tok.getLexeme()),
					tok.getLine());
			break;
		}

		return size;
	}

	private String alignment() {

		Token tok = lex.getToken();
		String alignment = null;

		switch (tok.getToken()) {
		case LEFT:
			alignment = "left";
			break;

		case RIGHT:
			alignment = "right";
			break;

		case CENTER:
			alignment = "center";
			break;

		default:
			syntaxError(
					String.format("Expected a CSS alignment value, found '%s'",
							tok.getLexeme()),
					tok.getLine());
			break;
		}

		return alignment;
	}

	private String fontStyle() {

		Token tok = lex.getToken();
		String fontStyle = null;

		switch (tok.getToken()) {
		case NORMAL:
			fontStyle = "normal";
			break;

		case BOLD:
			fontStyle = "bold";
			break;

		case ITALIC:
			fontStyle = "italic";
			break;

		default:
			syntaxError(
					String.format("Expected a CSS font-style value, found '%s'",
							tok.getLexeme()),
					tok.getLine());
			break;
		}

		return fontStyle;
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

}
