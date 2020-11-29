package html.main;

import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import html.ast.AstHtml;
import html.parser.Lexicon;
import html.parser.Parser;
import html.parser.Token;
import html.parser.TokensId;
import html.visitor.FindCssAstVisitor;
import html.visitor.PrintHtmlAstVisitor;

public class Main {

	public static String separator = "\n------------------------\n";
	public static String defaultFile = "res/html/EX1.html";

	public static void main(String[] args) {

		// File reader
		FileReader fileReader = readInput(args);

		// Lexer
		System.out.println("LEXICAL\n");
		Lexicon lex = runLexicon(fileReader);
		System.out.println("\nEND LEXICAL\n");

		System.out.println(separator);

		// Parser
		System.out.println("SYNTAX\n");
		AstHtml ast = runParser(lex);
		System.out.println("\nEND SYNTAX\n");

		System.out.println(separator);

		// VISITORS

		// Print Visitor
		System.out.println("\nPRINT VISITOR\n");
		runPrintVisitor(ast);
		System.out.println("\nEND PRINT VISITOR\n");

		// Find CSS visitor
		System.out.println("\nPRINT VISITOR\n");
		runFindCssVisitor(ast);
		System.out.println("\nEND PRINT VISITOR\n");

		// Search Visitor
//		System.out.println("\nSEARCH VISITOR\n");
//		SearchParamInCssVisitor searchVisitor = new SearchParamInCssVisitor();
//		String h1Color = searchVisitor.search("h1", "color", ast);
//		System.out.println(h1Color);
//
//		System.out.println("\nEND SEARCH VISITOR\n");
	}

	static FileReader readInput(String[] args) {

		FileReader fileReader = null;

		try {
			String filename = defaultFile;
			if (args.length > 0)
				filename = args[0].strip();

			fileReader = new FileReader(filename);
		} catch (Exception e) {
			System.err.println(String.format("Error reading input:\n\t%s",
					e.getMessage()));
		}

		if (fileReader == null) {
			System.err.println("\nError parsing input file. Ending execution.");
			System.exit(-1);
			return null;
		} else
			return fileReader;

	}

	static Lexicon runLexicon(FileReader fileReader) {
		Lexicon lex = new Lexicon(fileReader);
		listTokens(lex);

		// If lexer failed, do not launch parser
		if (lex.hasErrors()) {
			lex.printErrors();
			System.exit(-2);
			return null;
		} else
			return lex;

	}

	static AstHtml runParser(Lexicon lex) {
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

	static void runPrintVisitor(AstHtml ast) {
		if (ast != null) {
			PrintHtmlAstVisitor printVisitor = new PrintHtmlAstVisitor();
			System.out.println(ast.accept(printVisitor, null));
		} else
			System.err.println("PRINT VISITOR: AST has not been generated.");
	}

	static Set<String> runFindCssVisitor(AstHtml ast) {
		if (ast != null) {
			FindCssAstVisitor findCssVisitor = new FindCssAstVisitor();

			@SuppressWarnings("unchecked")
			Set<String> styles = (Set<String>) ast.accept(findCssVisitor, null);
			return styles;
		}

		System.err.println("FIND CSS VISITOR: AST has not been generated.");
		return new HashSet<String>();
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
