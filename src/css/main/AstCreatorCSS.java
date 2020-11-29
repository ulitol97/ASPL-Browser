package css.main;

import java.io.FileReader;

import css.ast.AstCss;
import css.parser.Lexicon;
import css.parser.Parser;

public class AstCreatorCSS {

	public static AstCss generateAst(FileReader reader, String filename)
			throws Exception {
		System.out.println(
				String.format("Generating ast for CSS (\"%s\")", filename));

		Lexicon lex = runLexicon(reader);
		AstCss ast = runParser(lex);

		reader.close();

		return ast;

	}

	private static Lexicon runLexicon(FileReader fileReader) throws Exception {
		Lexicon lex = new Lexicon(fileReader);

		// If lexer failed, do not launch parser
		if (lex.hasErrors()) {
			lex.printErrors();
			throw new Exception(lex.getErrors());
		} else
			return lex;

	}

	private static AstCss runParser(Lexicon lex) throws Exception {
		lex.reset();
		Parser parser = new Parser(lex);
		AstCss ast = parser.parse();

		// If parser failed, do not proceed further
		if (parser.hasErrors()) {
			parser.printErrors();
			throw new Exception(parser.getErrors());
		} else
			return ast;
	}

}
