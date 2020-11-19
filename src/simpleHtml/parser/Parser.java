package simpleHtml.parser;

import java.util.List;

import simpleHtml.ast.*;
import java.util.*;

public class Parser {
	
	Lexicon lex;
	
	public Parser (Lexicon lex) {
		this.lex = lex;
	}
	
	public Ast parse () {
		//...
		return ast;
	}
	

	//Gestión de Errores Sintáctico
	void errorSintactico (String e, int line) {
		errorSint = true;
		System.out.println("Error Sint�ctico : "+e+" en la l�nea "+line);
	}
}
