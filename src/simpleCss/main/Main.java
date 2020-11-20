package simpleCss.main;

import java.io.FileReader;

import simpleCss.ast.AstCss;
import simpleCss.parser.Lexicon;
import simpleCss.parser.Parser;
import simpleCss.parser.Token;
import simpleCss.parser.TokensId;
import simpleCss.visitor.PrintCssAstVisitor;

public class Main {

	public static FileReader filereader;

	public static void main(String[] args) {

		try {
			String filename = "EX1.css";
			if (args.length > 0)
				filename = args[0].strip();

			filereader = new FileReader(filename);
		} catch (Exception e) {
			System.err.println(
					String.format("Error reading input:\n%s", e.getMessage()));
			System.exit(-1);
		}

		// Lexer
		System.out.println("LEXICAL\n");
		Lexicon lex = new Lexicon(filereader);
		listTokens(lex);
		System.out.println("\nEND LEXICAL\n");

		// Parser
		System.out.println("SYNTAX\n");
		lex.reset();
		Parser parser = new Parser(lex);
		AstCss ast = parser.parse();
		System.out.println("\nEND SYNTAX\n");

		// VISITORS
		// Print Visitor
		System.out.println("\nPRINT VISITOR\n");
		if (ast != null) {
			PrintCssAstVisitor printVisitor = new PrintCssAstVisitor();
			ast.accept(printVisitor, null);
		} else
			System.err.println("PRINT VISITOR: AST has not been generated.");
		System.out.println("\nEND PRINT VISITOR\n");

	}

	/* AUX */

	// List all tokens found by lexer
	static void listTokens(Lexicon lex) {
		Token t = lex.getToken();
		while (t.getToken() != TokensId.EOF) {
			System.out.println(t.toString());
			t = lex.getToken();
		}

		System.out.println("\nEnd of file reached. \n" + t.toString());
	}

}
