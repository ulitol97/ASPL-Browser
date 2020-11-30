package html;

import java.io.FileReader;
import html.ast.AstHtml;
import html.parser.Lexicon;
import html.parser.Parser;

public class AstCreatorHTML {

	public static AstHtml generateAst(FileReader reader, String filename)
			throws Exception {
		System.out.println(
				String.format("Generating ast for HTML (\"%s\")", filename));

		Lexicon lex = runLexicon(reader);
		AstHtml ast = runParser(lex);
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

	private static AstHtml runParser(Lexicon lex) throws Exception {
		lex.reset();
		Parser parser = new Parser(lex);
		AstHtml ast = parser.parse();

		// If parser failed, do not proceed further
		if (parser.hasErrors()) {
			parser.printErrors();
			throw new Exception(parser.getErrors());
		} else
			return ast;
	}

}
