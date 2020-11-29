package render.paint;

import java.awt.Color;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import render.format.FormattedLine;
import render.format.FormattedPage;
import render.format.FormattedText;

public class PrintPageGUI implements IPrintPage {

	public static String separator = "\n------------------------\n";
	private FormattedPage formattedPage;
	private JPanel pane;

	public PrintPageGUI(JPanel pane) {
		this.pane = pane;
	}

	@Override
	public void printPage(FormattedPage formattedPage, PrintStream printStream)
			throws Exception {
		this.formattedPage = formattedPage;
		System.out.println(String.format("\nShowing page in GUI browser: %s:",
				formattedPage.getTitle()));
		System.out.print(separator);

		printPage();

	}

	private void printPage() throws BadLocationException {
		// Clear panel
		pane.removeAll();

		// Write to panel
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);

		StyledDocument doc = textPane.getStyledDocument();

		Style style = textPane.addStyle("docStyle", null);
		SimpleAttributeSet alignment = new SimpleAttributeSet();

		for (FormattedLine line : formattedPage.getLines()) {
			// Change the style with the line alignment and continue.
			StyleConstants.setAlignment(style,
					cssToSwingAlignment(line.getTextAlign()));

			StyleConstants.setAlignment(alignment,
					cssToSwingAlignment(line.getTextAlign()));

			doc.setParagraphAttributes(doc.getLength(), 0, alignment, false);

			for (int i = 0; i < line.getContents().size(); i++) {

				FormattedText text = line.getContents().get(i);

				// Change the style with the line alignment and insert the text.
				// Color
				StyleConstants.setForeground(style,
						cssToSwingColor(text.color));

				// Size
				StyleConstants.setFontSize(style, text.fontSize);

				// Style
				cssToSwingStyle(style, text.fontStyle);

				doc.insertString(doc.getLength(), text.text, style);

				if (i != line.getContents().size() - 1) {
					resetStyle(style);
					doc.insertString(doc.getLength(), " ", style);
				}
			}

			// Artificially add newline
			doc.insertString(doc.getLength(), "\n", null);

		}

		pane.add(textPane);

		// Scroll to top
		textPane.setCaretPosition(0);

	}

	private int cssToSwingAlignment(String cssValue) {
		int ret = StyleConstants.ALIGN_LEFT;

		switch (cssValue) {
		case "left":
			ret = StyleConstants.ALIGN_LEFT;
			break;

		case "center":
			ret = StyleConstants.ALIGN_CENTER;
			break;

		case "right":
			ret = StyleConstants.ALIGN_RIGHT;
			break;

		default:
			break;
		}

		return ret;
	}

	private Color cssToSwingColor(String cssValue) {
		Color ret = Color.BLACK;

		switch (cssValue) {
		case "black":
			ret = Color.BLACK;
			break;

		case "red":
			ret = Color.RED;
			break;

		case "green":
			ret = Color.GREEN;
			break;

		case "blue":
			ret = Color.BLUE;
			break;

		case "white":
			ret = Color.WHITE;
			break;

		default:
			break;
		}

		return ret;
	}

	private void cssToSwingStyle(Style style, String cssValue) {
		switch (cssValue) {
		case "bold":
			StyleConstants.setBold(style, true);
			StyleConstants.setItalic(style, false);
			StyleConstants.setUnderline(style, false);
			break;

		case "italic":
			StyleConstants.setItalic(style, true);
			StyleConstants.setBold(style, false);
			StyleConstants.setUnderline(style, false);
			break;

		case "underline":
			StyleConstants.setUnderline(style, true);
			StyleConstants.setItalic(style, true);
			StyleConstants.setBold(style, false);
			break;

		default:
			StyleConstants.setBold(style, false);
			StyleConstants.setItalic(style, false);
			StyleConstants.setUnderline(style, false);
			break;
		}

	}

	private void resetStyle(Style style) {
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		StyleConstants.setUnderline(style, false);
	}
}
