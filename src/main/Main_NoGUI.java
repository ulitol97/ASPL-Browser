package main;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import css.ast.AstCss;
import css.main.AstCreatorCSS;
import css.main.AstCreatorHTML;
import html.ast.AstHtml;
import html.visitor.FindCssAstVisitor;
import render.format.FormattedPage;
import render.paint.IPrintPage;
import render.paint.PrintPageTxt;
import render.visitor.RenderVisitor;

public class Main_NoGUI {

	public static String htmlFile = "EX4.html";
	public static String defaultCssFile = "Default.css";

	public static void main(String[] args) {

		// File reader
		FileReader fileReader = readInput(args);

		// Create HTML ast
		AstHtml astHtml = AstCreatorHTML.generateAst(fileReader, htmlFile);

		// Find associated CSS
		List<AstCss> styles = new ArrayList<AstCss>();
		Set<String> foundStyles = runFindCssVisitor(astHtml);

		// Add default CSS first so the rest can override it
		fileReader = readInput(new String[] { defaultCssFile });
		styles.add(AstCreatorCSS.generateAst(fileReader, defaultCssFile));

		// Create CSS asts
		for (String css : foundStyles) {
			String[] params = new String[1];
			params[0] = css;
			fileReader = readInput(params);
			styles.add(AstCreatorCSS.generateAst(fileReader, css));
		}

		// Render / Create formatted page
		RenderVisitor renderVisitor = new RenderVisitor(astHtml, styles);
		FormattedPage formattedPage = (FormattedPage) astHtml
				.accept(renderVisitor, null);

		// Print page
		renderPageText(formattedPage, System.out);

	}

	private static FileReader readInput(String[] args) {

		FileReader fileReader = null;

		try {
			String filename = htmlFile;
			if (args.length > 0) {
				filename = args[0].strip();
				htmlFile = filename;
			}

			fileReader = new FileReader(filename);
		} catch (IOException e) {
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

	private static Set<String> runFindCssVisitor(AstHtml ast) {
		if (ast != null) {
			FindCssAstVisitor findCssVisitor = new FindCssAstVisitor();

			@SuppressWarnings("unchecked")
			Set<String> styles = (LinkedHashSet<String>) ast
					.accept(findCssVisitor, null);
			return styles;
		}

		System.err.println("FIND CSS VISITOR: AST has not been generated.");
		return new LinkedHashSet<String>();
	}

	private static void renderPageText(FormattedPage formattedPage,
			PrintStream printStream) {

		if (formattedPage == null) {
			System.err.println(
					"RENDER PAGE TEXT: The formatted page could not be generated");
			return;
		}

		IPrintPage printer = new PrintPageTxt();
		printer.printPage(formattedPage, printStream);

	}

}