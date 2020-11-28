package render.main;

import render.format.FormattedLine;
import render.format.FormattedPage;
import render.format.FormattedText;
import render.paint.*;

public class Main {

	public static void main(String[] args) {
		FormattedPage formattedPage = createPage();
		IPrintPage printer = new PrintPageTxt();
		printer.printPage(formattedPage, System.out);
	}

	static FormattedPage createPage() {
		FormattedPage formattedPage = new FormattedPage();
		FormattedLine line1 = createLine1();
		formattedPage.add(line1);
		FormattedLine line2 = createLine2();
		formattedPage.add(line2);
		FormattedLine line3 = createLine3();
		formattedPage.add(line3);
		FormattedLine line4 = createLine4();
		formattedPage.add(line4);
		return formattedPage;
	}

	static FormattedLine createLine1() {
		FormattedLine line = new FormattedLine("center");
		FormattedText text = new FormattedText("Titulo", "black", "32px", "normal");
		line.add(text);
		return line;
	}

	static FormattedLine createLine2() {
		FormattedLine line = new FormattedLine("left");
		FormattedText text = new FormattedText("Ejemplo 1", "blue", "18px", "italic");
		line.add(text);
		return line;
	}

	static FormattedLine createLine3() {
		FormattedLine line = new FormattedLine("left");
		FormattedText text = new FormattedText("Un parrafo de texto.", "green",
				"12px", "normal");
		line.add(text);
		return line;
	}

	static FormattedLine createLine4() {
		FormattedLine line = new FormattedLine("left");
		FormattedText text = new FormattedText("Esto ", "green", "12px", "normal");
		line.add(text);
		text = new FormattedText("prueba  ", "green", "12px", "bold");
		line.add(text);
		text = new FormattedText("las ", "green", "12px", "normal");
		line.add(text);
		text = new FormattedText("etiquetas ", "green", "12px", "italic");
		line.add(text);
		text = new FormattedText("de ", "green", "12px", "normal");
		line.add(text);
		text = new FormattedText("formato ", "green", "12px", "underlined");
		line.add(text);
		text = new FormattedText("html ", "green", "12px", "normal");
		line.add(text);
		return line;
	}

}
