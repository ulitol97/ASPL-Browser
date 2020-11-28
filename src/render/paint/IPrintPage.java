package render.paint;

import java.io.PrintStream;

import render.format.FormattedPage;

public interface IPrintPage {

	void printPage(FormattedPage formattedPage, PrintStream printStream);

}
