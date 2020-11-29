package html.parser;

import java.util.*;
import java.io.*;

public class Lexicon {

	// Token management
	private List<Token> tokens = new ArrayList<Token>();
	private int currentToken = 0; // Last token retrieved in getToken()

	// File read management
	private FileReader filereader;
	private boolean charBuffUsed = false;
	private char charBuff;
	private int line = 1;

	// Error management
	private List<String> errors = new ArrayList<String>();
	private boolean lexError = false;

	HashSet<Character> charText = new HashSet<Character>();

	public Lexicon(FileReader filereader) {
		/*
		 * tokens.add(new Token(TokensId.HTML, "<html>")); tokens.add(new
		 * Token(TokensId.HTMLCLOSE, "</html>"));
		 */
		this.filereader = filereader;
		String lex;
		loadSet();
		try {
			char valor = (char) 0;
			while (valor != (char) -1) {
				valor = nextChar();
				switch (valor) {
				// Closing tags
				case '<':
					valor = nextChar();
					if ((char) valor == '/') {
						valor = nextChar();
						switch ((char) valor) {
						case 'h':
							lex = getLexeme("</h", '>');
							if (lex.equals("</html>"))
								tokens.add(new Token(TokensId.CLOSEHTML, lex,
										line));

							else if (lex.equals("</h1>"))
								tokens.add(
										new Token(TokensId.CLOSEH1, lex, line));

							else if (lex.equals("</h2>"))
								tokens.add(
										new Token(TokensId.CLOSEH2, lex, line));

							else if (lex.equals("</head>"))
								tokens.add(new Token(TokensId.CLOSEHEAD, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}
							break;

						case 'b':
							lex = getLexeme("</b", '>');
							if (lex.equals("</b>"))
								tokens.add(new Token(TokensId.CLOSEBOLD, lex,
										line));

							else if (lex.equals("</body>"))
								tokens.add(new Token(TokensId.CLOSEBODY, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'p':
							lex = getLexeme("</p", '>');
							if (lex.equals("</p>"))
								tokens.add(
										new Token(TokensId.CLOSEP, lex, line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 't':
							lex = getLexeme("</t", '>');
							if (lex.equals("</title>"))
								tokens.add(new Token(TokensId.CLOSETITLE, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'i':
							lex = getLexeme("</i", '>');
							if (lex.equals("</i>"))
								tokens.add(new Token(TokensId.CLOSEITALIC, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'u':
							lex = getLexeme("</u", '>');
							if (lex.equals("</u>")) {
								tokens.add(new Token(TokensId.CLOSEUNDERLINE,
										lex, line));
							}

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						default:
							lexicalError("Invalid string '"
									+ getLexeme("<" + valor, '>')
									+ "' found in line " + line);
						}
					} else {
						// Tag open '<'...
						switch ((char) valor) {

						case 'l':
							lex = getLexeme("<l", 'k');
							if (lex.equals("<link"))
								tokens.add(new Token(TokensId.OPENLINK, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'h':
							lex = getLexeme("<h", '>');
							if (lex.equals("<html>"))
								tokens.add(new Token(TokensId.OPENHTML, lex,
										line));

							else if (lex.equals("<h1>"))
								tokens.add(
										new Token(TokensId.OPENH1, lex, line));

							else if (lex.equals("<h2>"))
								tokens.add(
										new Token(TokensId.OPENH2, lex, line));

							else if (lex.equals("<head>"))
								tokens.add(new Token(TokensId.OPENHEAD, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}
							break;

						case 'b':
							lex = getLexeme("<b", '>');
							if (lex.equals("<b>"))
								tokens.add(new Token(TokensId.OPENBOLD, lex,
										line));

							else if (lex.equals("<body>"))
								tokens.add(new Token(TokensId.OPENBODY, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'p':
							lex = getLexeme("<p", '>');
							if (lex.equals("<p>"))
								tokens.add(
										new Token(TokensId.OPENP, lex, line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 't':
							lex = getLexeme("<t", '>');
							if (lex.equals("<title>"))
								tokens.add(new Token(TokensId.OPENTITLE, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'i':
							lex = getLexeme("<i", '>');
							if (lex.equals("<i>"))
								tokens.add(new Token(TokensId.OPENITALIC, lex,
										line));

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						case 'u':
							lex = getLexeme("<u", '>');
							if (lex.equals("<u>")) {
								tokens.add(new Token(TokensId.OPENUNDERLINE,
										lex, line));
							}

							else {
								lexicalError("Invalid string '" + lex
										+ "' found in line " + line);
							}

							break;

						// Comments
						case '!':
							valor = nextChar();
							if (valor != '-') {
								lexicalError("Invalid string '" + valor
										+ "' found after '<!' in line " + line);
							}
							valor = nextChar();
							if (valor != '-') {
								lexicalError("Invalid string '" + valor
										+ "' found after '<!-' in line "
										+ line);
							}

							// When we enter the comment, we already have <!--
							deleteComment();
							break;

						default:
							lexicalError("Invalid string '"
									+ getLexeme("<" + valor, '>')
									+ "' found in line " + line);

						}

					}
					break;

				// Rest of 1-length tokens:
				case '>':
					tokens.add(new Token(TokensId.TAGCLOSE, new String(">"),
							line));
					break;

				case '=':
					tokens.add(new Token(TokensId.EQ, new String(">"), line));
					break;

				case '"':
					tokens.add(
							new Token(TokensId.QUOTE, new String(">"), line));
					break;

				case '/':
					tokens.add(new Token(TokensId.BAR, new String(">"), line));
					break;

				// Other
				case '\n':
					line++;

					// White spaces and indentation: consume and break
				case '\r':
				case ' ':
				case '\t':
				case (char) -1:
					break;

				// n-length tokens
				default:
					lex = getLexemeTEXT(new String("" + (char) valor));
					// Search for rel, href, type
					switch (lex) {
					// CSS attributes
					case "href":
						tokens.add(new Token(TokensId.HREF, lex, line));
						break;

					case "rel":
						tokens.add(new Token(TokensId.REL, lex, line));
						break;

					case "type":
						tokens.add(new Token(TokensId.TYPE, lex, line));
						break;

					// Any non-recognized text will fall under the TEXT token
					default:
						tokens.add(new Token(TokensId.TEXT, lex, line));
						break;
					}

					break;
				}
			}
			this.filereader.close();
		} catch (IOException e) {
			System.out.println("Error E/S: " + e);
		}

	}

	// ++
	// ++ Operaciones para el Sintactico
	// ++
	// Devolver el último token
	public void returnLastToken() {
		currentToken--;
	}

	// Get Token
	public Token getToken() {
		if (currentToken < tokens.size()) {
			return tokens.get(currentToken++);
		}
		return new Token(TokensId.EOF, "EOF", line);
	}

	// Reset lexicon to initial position
	public void reset() {
		this.currentToken = 0;
	}

	// ++
	// ++ Operaciones para el Sintactico
	// ++

	// Privadas

	// Dado el inicio de una cadena y el caracter final, devuelve el lexema
	// correspondiente
	String getLexeme(String lexStart, char finChar) throws IOException {
		String lexReturned = lexStart;
		char valor;
		do {
			valor = nextChar();
			lexReturned = lexReturned + ((char) valor);
		} while (((char) valor != finChar) && ((char) valor != -1));
		// returnChar(valor);
		return lexReturned;
	}

	// Devuelve un lexema de texto, el caracter final es un espacio
	String getLexemeTEXT(String lexStart) throws IOException {
		String lexReturned = lexStart;
		char valor = nextChar();
		while (charText.contains(((char) valor)) && ((char) valor != -1)) {
			lexReturned = lexReturned + ((char) valor);
			valor = nextChar();
		}
		returnChar(valor);
		return lexReturned;
	}

	// Load the set of characters admitted by the lexical analyzer
	void loadSet() {
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,;:+-*/()[]!?";
		int i = 0;
		Character a = Character.valueOf('a');
		while (i < s.length()) {
			a = s.charAt(i);
			charText.add(a);
			i++;
		}
	}

	// TODO
	// Removes a comment enclosed between '<!--' and '-->' // /* */
	boolean deleteComment() throws IOException {
		boolean ret = false;
		char nextChar;

//		char nextChar = nextChar();
//		if (nextChar != '-')
//			return false;
//
//		nextChar = nextChar();
//		if (nextChar != '-')
//			return false;

		do {
			nextChar = nextChar();
			if (nextChar == (char) -1) // Unexpected EOF
				return false;
			if (nextChar == '\n') // Keep counting lines
				line++;
			if (nextChar == '-') {
				nextChar = nextChar();
				if (nextChar == '-') {
					nextChar = nextChar();

					// In case comments end like '--------->'
					while (nextChar == '-') {
						nextChar = nextChar();
					}

					if (nextChar == '>') {
						ret = true;
					}
				}
			}
		} while (!ret);

		return ret;
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

	/* ERROR MANAGEMENT */

	// Return whether if the lexicon found any errors or was executed correctly
	public boolean hasErrors() {
		return this.lexError;
	}

	// Lexical error: notify that errors occurred
	void lexicalError(String error) {
		errors.add(error);
		this.lexError = true;
	}

	// Print all errors together on the err output stream
	public void printErrors() {
		if (this.lexError) {
			System.err.println("\nErrors found running the lexicon:");
			for (String error : errors) {
				System.err.println("\t => " + error);
			}
		}
	}

	public String getErrors() {
		if (this.lexError) {
			String ret = "\nErrors found running the lexicon:";
			for (String error : errors) {
				ret += "\t => " + error + "\n";
			}
			return ret;
		}
		return "Unknown";
	}
}
