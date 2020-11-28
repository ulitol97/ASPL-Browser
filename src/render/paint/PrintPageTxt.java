package render.paint;

import java.io.PrintStream;

import render.format.FormattedLine;
import render.format.FormattedPage;
import render.format.FormattedText;

public class PrintPageTxt implements IPrintPage {

	public static String separator = "\n------------------------\n";
	private FormattedPage formattedPage;
	private PrintStream printStream;

	@Override
	public void printPage(FormattedPage formattedPage,
			PrintStream printStream) {
		this.formattedPage = formattedPage;
		this.printStream = printStream;

		System.out.println(String.format("\nPrinting page: %s:\n",
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
			printStream.println(String.format("(Line align: %s | Metrics: %s >>",
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

	// EXAMPLE OUTPUT

	/*
	 * Página título: Ejemplo de estilos CSS en el propio documento (Line align
	 * : center | Metrics : 192.0 >> (Format : black, 32.0, italic | Metrics :
	 * 192.0 >> Titulo) ) bold (Line align : left | Metrics : 162.0 >> (Format :
	 * blue, 18.0, italic | Metrics : 162.0 >> Ejemplo 1) )
	 * 
	 * (Line align : left | Metrics : 240.0 >> (Format : green, 12.0, normal |
	 * Metrics : 240.0 >> Un parrafo de texto.) )
	 * 
	 * (Line align : left | Metrics : 420.0 >> (Format : green, 12.0, normal |
	 * Metrics : 48.0 >> Esto) (Format : green, 12.0, bold | Metrics : 72.0 >>
	 * prueba) (Format : green, 12.0, normal | Metrics : 36.0 >> las) (Format :
	 * green, 12.0, italic | Metrics : 108.0 >> etiquetas) (Format : green,
	 * 12.0, normal | Metrics : 24.0 >> de) (Format : green, 12.0, underlined |
	 * Metrics : 84.0 >> formato) (Format : green, 12.0, normal | Metrics : 48.0
	 * >> html) )
	 */
}
