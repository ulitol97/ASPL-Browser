package css.main;

import java.io.FileReader;


import html.ast.AstHtml;
import html.parser.Lexicon;
import html.parser.Parser;

public class AstCreatorHTML {

	public static AstHtml generateAst(FileReader reader, String filename) {
		System.out.println(
				String.format("Generating ast for HTML (\"%s\")", filename));

		Lexicon lex = runLexicon(reader);
		AstHtml ast = runParser(lex);
		return ast;

	}

	private static Lexicon runLexicon(FileReader fileReader) {
		Lexicon lex = new Lexicon(fileReader);

		// If lexer failed, do not launch parser
		if (lex.hasErrors()) {
			lex.printErrors();
			System.exit(-1);
			return null;
		} else
			return lex;

	}

	private static AstHtml runParser(Lexicon lex) {
		lex.reset();
		Parser parser = new Parser(lex);
		AstHtml ast = parser.parse();

		// If parser failed, do not proceed further
		if (parser.hasErrors()) {
			parser.printErrors();
			System.exit(-2);
			return null;
		} else
			return ast;
	}

}
