package simpleCss.parser;

import java.io.FileReader;
import java.util.*;
import java.io.*;

public class Lexicon {

	// Gestión de tokens
	List<Token> tokens = new ArrayList<Token>();
	int i = 0; // Último token entregado en getToken()
	// Gestión de lectura del fichero
	FileReader filereader;
	boolean charBuffUsed = false;
	char charBuff;
	int line = 1; // indica la línea del fichero fuente

	HashSet<Character> charText = new HashSet<Character>();

	public Lexicon(FileReader f) {
		filereader = f;
		// String lex;
		try {
			char valor = (char) 0;
			while (valor != (char) -1) {
				valor = nextChar();
				switch (valor) {
				// 1-length chars:
				case '{':
					tokens.add(new Token(TokensId.OPENBRACE, "{", line));
					break;
				case '}':
					tokens.add(new Token(TokensId.CLOSEBRACE, "}", line));
					break;
				case ':':
					tokens.add(new Token(TokensId.COLON, ":", line));
					break;
				case ';':
					tokens.add(new Token(TokensId.SEMICOLON, ";", line));
					break;
				case 'p':
					tokens.add(new Token(TokensId.P, "p", line));
					break;

				// New line
				case '\n':
					line++;
					
				// White spaces and indentation: consume and break
				case '\r':
				case '\t':
				case ' ':
				case (char) -1:
					break;
				
				// n-length tokens
				default:
					
					// Number-based properties (font-size)
					if (Character.isDigit(valor)) {
						String size = getSize("" + valor);
						if (size != null)
							tokens.add(new Token(TokensId.FONTSIZE, size, line));
					} 
					// Text-bases properties
					else if (Character.isAlphabetic(valor)) {
						String ident = getText("" + valor);
						switch (ident) {
						case "black":
							tokens.add(new Token(TokensId.BLACK, ident, line));
							break;
						// Aquí van todos los tokens de más de 1 caracter
						// ...
						default:
							// Error, no encajo con ningun token
							tokens.add(new Token(TokensId.IDENT, ident, line));
						}
					} else
						errorLexico("Caracter " + valor + " encontrado en l�nea " + line);
				}
			}
			filereader.close();
		} catch (IOException e) {
			System.out.println("Error E/S: " + e);
		}

	}

	// ++
	// ++ Operaciones para el Sintactico
	// ++
	// Devolver el último token
	public void returnLastToken() {
		i--;
	}

	// Get Token
	public Token getToken() {
		if (i < tokens.size()) {
			return tokens.get(i++);
		}
		return new Token(TokensId.EOF, "EOF", line);
	}

	// Recupera un token de tipo size incluyendo el px final
	String getSize(String lexStart) throws IOException {
		String lexReturned = lexStart;
		char valor;
		do {
			valor = nextChar();
			lexReturned = lexReturned + (valor);
		} while ((valor != 'p') && (valor != -1));
		// returnChar(valor);
		if (valor == 'p') {
			// lexReturned = lexReturned+(valor);
			valor = nextChar();
			if (valor == 'x') {
				lexReturned = lexReturned + (valor);
			} else {
				errorLexico("Encontrado " + lexReturned + ". Se esperada un token SIZE.");
				return null;
			}
		}
		return lexReturned;
	}

	// Recupera un token de m�s de 1 caracter
	String getText(String lexStart) throws IOException {
		String lexReturned = lexStart;
		char valor = nextChar();
		while (Character.isDigit(valor) || Character.isAlphabetic(valor) || (valor == '-')) {
			lexReturned = lexReturned + (valor);
			valor = nextChar();
		}
		returnChar(valor);
		return lexReturned;
	}

	// Borra un comentario que está encerrado en /* y */
	boolean deleteComment() throws IOException {
		boolean r = false;
		char c = nextChar();
		if (c != '*')
			return false;
		do {
			c = nextChar();
			if (c == (char) -1) // Inesperado fin de fichero
				return false;
			if (c == '\n') // debe seguir contando l�neas
				line++;
			if (c == '*') {
				c = nextChar();
				if (c == '/') {
					r = true;
				}
			}
		} while (!r);

		return r;
	}

	// Devuelve el siguiente caracter del fichero
	char nextChar() throws IOException {
		if (charBuffUsed) {
			charBuffUsed = false;
			return charBuff;
		} else {
			int valor = filereader.read();
			return ((char) valor);
		}
	}

	// Devuelve un caracter al buffer si fue leído y no consumido
	void returnChar(char r) {
		charBuffUsed = true;
		charBuff = r;
	}

	// Error léxico
	void errorLexico(String e) {
		System.out.println("Error l�xico en : " + e);
	}
}
