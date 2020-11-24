//package simpleHtml.visitor;
//
//import simpleHtml.ast.*;
//
//public class PrintHtmlAstVisitor implements Visitor {
//	String sp="   ";
//
//	@Override
//	public Object visit(Program p, Object param) {
//		String s, sh, sb;
//		sh = (String) p.h.accept(this,sp);
//		sb = (String) p.b.accept(this,sp);
//		s = "(Program\n"+sh+"\n"+sb+"\n)";
//		return s;
//	}
//
//	@Override
//	public Object visit(Head h, Object param) {
//		String s, st, sl;
//		st = (String) h.t.accept(this,(String)param+sp);
//		sl = (String) h.l.accept(this,(String)param+sp);
//		s = sp+"(Head\n"+st+"\n"+sl+"\n"+sp+")";
//		return s;
//	}
//
//	@Override
//	public Object visit(Title t, Object param) {
//		return (String)param+"(title -> "+t.txt+")";
//	}
//
//	@Override
//	public Object visit(Link l, Object param) {
//		String txt="";
//		txt = (String) param + "(link href -> <"+l.urlhref+"> rel -> <"+l.urlrel+"> type -> <"+l.urltype+">)";
//		return txt;
//	}
//
//	@Override
//	public Object visit(Body b, Object param) {
//		String s = (String) param +"(body\n";
//		for (Paragraph p : b.paragraphs)
//			s = s + (String) p.accept(this,(String)param+sp)+"\n";
//		s = s + (String) param + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(H1 h1, Object param) {
//		String s= (String) param + "(header 1\n";
//		for (HtmlText h : h1.htmltexts)
//			s = s + (String) h.accept(this,(String)param+sp)+"\n";
//		s = s + (String) param + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(H2 h2, Object param) {
//		String s= (String) param + "(header 2\n";
//		for (HtmlText h : h2.htmltexts)
//			s = s + (String) h.accept(this,(String)param+sp)+"\n";
//		s = s + (String) param + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(P p, Object param) {
//		String s= (String) param + "(paragraph\n";
//		for (HtmlText h : p.htmltexts)
//			s = s + (String) h.accept(this,(String)param+sp)+"\n";
//		s = s + (String) param + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(Text tx, Object param) {
//		String s= (String) param + "(TEXT -> ";
//		s = s + tx.text + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(BoldText bt, Object param) {
//		String s= (String) param + "(bold\n";
//		s = s + (String) param + bt.text;
//		s = s + "\n" + (String) param + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(ItalicText it, Object param) {
//		String s= (String) param + "(italic\n";
//		s = s + (String) param + it.text;
//		s = s + "\n" + (String) param + ")";
//		return s;
//	}
//
//	@Override
//	public Object visit(UnderlineText ut, Object param) {
//		String s= (String) param + "(underlined\n";
//		s = s + (String) param + ut.text;
//		s = s + "\n" + (String) param + ")";
//		return s;
//	}
//
//}
