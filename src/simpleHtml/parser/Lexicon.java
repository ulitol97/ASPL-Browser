package simpleHtml.parser;

import java.io.FileReader;
import java.util.*;
import java.io.*;

public class Lexicon {

	// Gestión de tokens
	List<Token> tokens = new ArrayList<Token>();
	int i = 0; //último token entregado en getToken()
	//Gestión de lectura del fichero
	FileReader filereader;
	boolean charBuffUsed = false;
	char charBuff;
	int line = 1; // indica la l�nea del fichero fuente
	
	HashSet<Character> charText = new HashSet<Character>();
	
	public Lexicon (FileReader f) {
		/*
		tokens.add(new Token(TokensId.HTML, "<html>"));
		tokens.add(new Token(TokensId.HTMLCLOSE, "</html>"));
		*/
		filereader = f;
		String lex;
		loadSet();
		try{
			char valor=(char) 0;
			while(valor!=(char) -1){
				valor=nextChar();
				switch ((char) valor) {
				case '<':
					valor=nextChar();
					if ((char) valor == '/') {
						valor=nextChar();
						switch ((char) valor) {
						case 'h':
							lex = getLexeme ("</h",'>');
							if (lex.equals("</html>"))
								tokens.add(new Token(TokensId.HTMLCLOSE, lex, line));
							else if (lex.equals("</head>"))
								tokens.add(new Token(TokensId.HEADC, lex, line));
							else if (lex.equals("</h1>"))
								tokens.add(new Token(TokensId.H1C, lex, line));
							else if (lex.equals("</h2>"))
								tokens.add(new Token(TokensId.H2C, lex, line));
							else
								errorLexico(lex);
							break;
							// Resto de etiquetas de cierre
							//...
						default:
							errorLexico(getLexeme("<"+valor, '>'));
						}
					}
					else {
						switch ((char) valor)  {
						case 'l':
							lex = getLexeme ("<l",'k');
							if (lex.equals("<link"))
								tokens.add(new Token(TokensId.LINK, lex, line));
							else
								errorLexico(lex);
							break;
							// Resto de etiquetas de apertura
							//...
						default:
							errorLexico(getLexeme("<"+valor, '>'));
						
						}
					}
					break;
				case '>':
						tokens.add(new Token(TokensId.CLOSE, new String(">"), line));
					break;
					// Resto de token de un solo caracter
					//...
				case '\n':
					line++;
				case '\r':
				case ' ':
				case '\t':
				case (char)-1:
					//Eliminar todos los espacios TokensId.WS
					break;
				default:
					//Texto
					lex = getLexemeTEXT(new String(""+(char)valor));
					tokens.add(new Token(TokensId.TEXT, lex, line));
					break;
				}
				//System.out.print((char)valor);
			}
			filereader.close();
        }catch(IOException e){
            System.out.println("Error E/S: "+e);
        }
		
	}
	
	// ++
	// ++ Operaciones para el Sintactico
	// ++
	// Devolver el último token
	public void returnLastToken () {
		i--;
	}
	
	// Get Token
	public Token getToken () {
		if (i < tokens.size()) {
			return tokens.get(i++);
		}
		return new Token (TokensId.EOF,"EOF", line);
	}	
	// ++
	// ++ Operaciones para el Sintactico
	// ++

	//Privadas
	
	// Dado el inicio de una cadena y el caracter final, devuelve el lexema correspondiente
	String getLexeme (String lexStart, char finChar) throws IOException {
		String lexReturned = lexStart;
		char valor;
		do {
			valor=nextChar();
			lexReturned = lexReturned+((char) valor);
		} while (((char) valor != finChar) && ((char) valor != -1));
		//returnChar(valor);
		return lexReturned;
	}
	
	// Devuelve un lexema de texto, el caracter final es un espacio
	String getLexemeTEXT (String lexStart) throws IOException {
		String lexReturned = lexStart;
		char valor = nextChar();
		while (charText.contains(((char) valor)) && ((char) valor != -1)) {
			lexReturned = lexReturned+((char) valor);
			valor=nextChar();
		}
		returnChar(valor);
		return lexReturned;
	}
	
	// Carga el conjunto de caracteres adminidos pro el analizador léxico
	void loadSet () {
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:+-*/()[]!?";
		int i=0;
		Character a = new Character('a');
		while (i < s.length()) {
			a = s.charAt(i);
			charText.add(a);
			i++;
		}
		//System.out.println(charText);
	}
	
	// Devuelde el siguiente caracter de fuente
	char nextChar() throws IOException{
		if (charBuffUsed) {
			charBuffUsed = false;
			return charBuff;
		} else {
		int valor=filereader.read();
		return ((char) valor);
		}
	}
	
	// Devuelve un caracger que se ha leído pero no se ha consumido al buffer
	void returnChar (char r) {
		charBuffUsed = true;
		charBuff = r;
	}

	// Emite error léxico
	void errorLexico (String e) {
		System.out.println("Error l�xico en : "+e);
	}
}
