package render.format;

import render.visitor.Visitor;

public interface FormattedElement {
	Object accept(Visitor v, Object param);

}
