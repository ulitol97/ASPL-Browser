package simpleCss.parser;

import java.io.FileReader;
import java.util.*;
import java.io.*;

public class Lexicon {

	// Token management
	List<Token> tokens = new ArrayList<Token>();
	int i = 0; // Last token retrieved in getToken()
	// File read management
	FileReader filereader;
	boolean charBuffUsed = false;
	char charBuff;
	int line = 1; // line of the input file

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
					

				// New line
				case '\n':
					line++;

				// White spaces and indentation: consume and break
				case '\r':
				case '\t':
				case ' ':
				case (char) -1:
					break;
				
				// Comments
				case '/':
					deleteComment();
					break;

				// n-length tokens
				default:

					// Number-based properties (font-size)
					if (Character.isDigit(valor)) {
						String size = getSize("" + valor);
						if (size != null)
							tokens.add(new Token(TokensId.SIZE, size, line));
					}
					// Text-based properties
					else if (Character.isAlphabetic(valor)) {
						String ident = getText("" + valor);
						switch (ident) {
						
						// Selectors
						case "h1":
							tokens.add(new Token(TokensId.H1, ident, line));
							break;
							
						case "h2":
							tokens.add(new Token(TokensId.H2, ident, line));
							break;

						case "p":
							tokens.add(new Token(TokensId.P, ident, line));
							break;
						
						// Property keys
						case "color":
							tokens.add(new Token(TokensId.COLOR, ident, line));
							break;
							
						case "font-size":
							tokens.add(new Token(TokensId.FONTSIZE, ident, line));
							break;

						case "text-align":
							tokens.add(new Token(TokensId.TEXTALIGN, ident, line));
							break;

						case "font-style":
							tokens.add(new Token(TokensId.FONTSTYLE, ident, line));
							break;

						// Colors
						case "black":
							tokens.add(new Token(TokensId.BLACK, ident, line));
							break;
						case "red":
							tokens.add(new Token(TokensId.RED, ident, line));
							break;
						case "green":
							tokens.add(new Token(TokensId.GREEN, ident, line));
							break;
						case "blue":
							tokens.add(new Token(TokensId.BLUE, ident, line));
							break;
						case "white":
							tokens.add(new Token(TokensId.WHITE, ident, line));
							break;
							
						// Alignments
						case "left":
							tokens.add(new Token(TokensId.LEFT, ident, line));
							break;
						case "right":
							tokens.add(new Token(TokensId.RIGHT, ident, line));
							break;
						case "center":
							tokens.add(new Token(TokensId.CENTER, ident, line));
							break;
							
						// Font styles
						case "normal":
							tokens.add(new Token(TokensId.NORMAL, ident, line));
							break;
						case "bold":
							tokens.add(new Token(TokensId.BOLD, ident, line));
							break;
						case "italic":
							tokens.add(new Token(TokensId.ITALIC, ident, line));
							break;
						
						default:
							// ERROR: no matched tokens
							lexicalError("Invalid token '" + ident + "' found in line " + line);
						}
					} else
						lexicalError("Character " + valor + " found in line " + line);
				}
			}
			filereader.close();
		} catch (IOException e) {
			System.out.println("Error E/S: " + e);
		}

	}

	/* SYNTAX OPERATIONS */
	
	// Return last token
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
	
	// Reset lexicon to initial position
	public void reset() {
		this.i = 0;
	}

	// Retrieves a token of type 'size', including the trailing 'px'
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
				lexicalError("Encontrado " + lexReturned + ". Se esperada un token SIZE.");
				return null;
			}
		}
		return lexReturned;
	}

	// Retrieves a token longer than 1 character
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

	// Removes a comment enclosed between '/*' and '*/'
	boolean deleteComment() throws IOException {
		boolean r = false;
		char c = nextChar();
		if (c != '*')
			return false;
		do {
			c = nextChar();
			if (c == (char) -1) // Unexpected EOF
				return false;
			if (c == '\n') // Keep counting lines
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

	// Returns the next character in the input file
	char nextChar() throws IOException {
		if (charBuffUsed) {
			charBuffUsed = false;
			return charBuff;
		} else {
			int valor = filereader.read();
			return ((char) valor);
		}
	}

	// Returns 1 character to the buffer if it was read and not consumed
	void returnChar(char r) {
		charBuffUsed = true;
		charBuff = r;
	}

	// Lexical error
	void lexicalError(String e) {
		System.out.println("Lexical error: " + e);
	}
}
