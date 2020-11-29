package render.paint;

import java.io.PrintStream;

import javax.swing.JPanel;

import render.format.FormattedLine;
import render.format.FormattedPage;
import render.format.FormattedText;

public class PrintPageGUI implements IPrintPage {

	public static String separator = "\n------------------------\n";
	private FormattedPage formattedPage;
	private PrintStream printStream;
	private JPanel pane;

	public PrintPageGUI(JPanel pane) {
		this.pane = pane;
	}

	@Override
	public void printPage(FormattedPage formattedPage,
			PrintStream printStream) {
		this.formattedPage = formattedPage;
		this.printStream = printStream;

		System.out.println(String.format("\nPrinting page: %s:",
				formattedPage.getTitle()));
		System.out.print(separator);
		

		printTitle();
		printBody();

	}

	private void printTitle() {
		printStream.print(String.format("Title: %s", formattedPage.getTitle()));
		printStream.println(separator);
	}

	private void printBody() {
		for (FormattedLine line : formattedPage.getLines()) {
			printStream
					.println(String.format("(Line align: %s | Metrics: %s >>",
							line.getTextAlign(), line.getMetrics()));

			for (FormattedText text : line.getContents()) {
				printStream.println(String.format(
						"\t(Format: %s, %d, %s | Metrics: %d >> %s)",
						text.color, text.fontSize, text.fontStyle,
						text.getMetrics(), text.text));
			}

			printStream.println(")\n");
		}
	}
}
