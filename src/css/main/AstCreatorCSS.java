package css.main;

import java.io.FileReader;

import css.ast.AstCss;
import css.parser.Lexicon;
import css.parser.Parser;

public class AstCreatorCSS {

	public static AstCss generateAst(FileReader reader, String filename) {
		System.out.println(
				String.format("Generating ast for CSS (\"%s\")", filename));

		Lexicon lex = runLexicon(reader);
		AstCss ast = runParser(lex);
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

	private static AstCss runParser(Lexicon lex) {
		lex.reset();
		Parser parser = new Parser(lex);
		AstCss ast = parser.parse();

		// If parser failed, do not proceed further
		if (parser.hasErrors()) {
			parser.printErrors();
			System.exit(-2);
			return null;
		} else
			return ast;
	}

}
