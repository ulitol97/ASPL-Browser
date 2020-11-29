package main;

import java.io.FileReader;
import java.io.IOException;
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

	private static String defaultHtmlFile = "res/html/EX4.html";
	private static String defaultCssFile = "res/css/Default.css";

	/**
	 * @author UO251436 Main method to be invoked when running the program
	 *         without UI.
	 */
	public static void main(String[] args) {

		String filePath = args.length > 0 ? args[0] : defaultHtmlFile;
		FormattedPage formattedPage = null;

		// Process HTML
		try {
			formattedPage = processFile(filePath);
		} catch (Exception e) {
			System.err.println(
					"\nERROR: Could not parse the selected HTML file. Reason:\n\t=> "
							+ e.getMessage());
			System.exit(1);
		}

		// Print page
		if (formattedPage == null) {
			System.err.println(
					"RENDER PAGE TEXT: The formatted page could not be generated");
			System.exit(2);
		} else {
			IPrintPage printer = new PrintPageTxt();
			try {
				printer.printPage(formattedPage, System.out);
			} catch (Exception e) {
				System.err.println(
						"An error occurred printing the page. Reason:\n\t"
								+ e.getMessage());
				System.exit(3);
			}
		}

	}

	public static FormattedPage processFile(String path) throws Exception {
		// File reader
		FileReader fileReader = readInput(path);

		// Create HTML ast
		AstHtml astHtml = AstCreatorHTML.generateAst(fileReader,
				defaultHtmlFile);

		// Find associated CSS
		List<AstCss> styles = new ArrayList<AstCss>();
		Set<String> foundStyles = runFindCssVisitor(astHtml);

		// Add default CSS first so the rest can override it
		fileReader = readInput(defaultCssFile);
		styles.add(AstCreatorCSS.generateAst(fileReader, defaultCssFile));

		// Create CSS asts
		for (String css : foundStyles) {
			fileReader = readInput(css);
			styles.add(AstCreatorCSS.generateAst(fileReader, css));
		}

		// Render / Create formatted page
		RenderVisitor renderVisitor = new RenderVisitor(astHtml, styles);
		FormattedPage formattedPage = (FormattedPage) astHtml
				.accept(renderVisitor, null);

		return formattedPage;

	}

	private static FileReader readInput(String path) throws IOException {

		FileReader fileReader = null;

		String filename = path != null ? path : defaultHtmlFile;
		fileReader = new FileReader(filename);

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

		throw new RuntimeException(
				"FIND CSS VISITOR: AST has not been generated.");
	}

}
