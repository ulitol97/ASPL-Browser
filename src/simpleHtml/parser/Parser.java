package simpleHtml.parser;

import simpleHtml.ast.*;
import java.util.*;

public class Parser {

	Lexicon lex;

	// Error management
	private List<String> errors = new ArrayList<String>();
	private boolean syntaxError = false;

	public Parser(Lexicon lex) {
		this.lex = lex;
	}

	public AstHtml parse() {
		// ...
		return ast;
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
