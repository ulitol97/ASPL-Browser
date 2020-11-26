package render.visitor;

import render.format.FormattedLine;
import render.format.FormattedPage;
import render.format.FormattedText;

public interface Visitor {

	Object visit(FormattedLine formattedLine, Object param);

	Object visit(FormattedPage formattedPage, Object param);

	Object visit(FormattedText formattedText, Object param);

}
