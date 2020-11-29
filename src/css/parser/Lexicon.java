package css.parser;

import java.io.FileReader;
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
	private int line = 1; // line of the input file

	// Error management
	private List<String> errors = new ArrayList<String>();
	private boolean lexError = false;

	/**
	 * Main method. Automaton in charge of parsing tokens in the input file
	 */
	public Lexicon(FileReader filereader) {
		this.filereader = filereader;
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
							tokens.add(
									new Token(TokensId.FONTSIZE, ident, line));
							break;

						case "text-align":
							tokens.add(
									new Token(TokensId.TEXTALIGN, ident, line));
							break;

						case "font-style":
							tokens.add(
									new Token(TokensId.FONTSTYLE, ident, line));
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
							
						case "underline":
							tokens.add(new Token(TokensId.UNDERLINE, ident, line));
							break;

						default:
							// ERROR: no matched tokens
							lexicalError("Invalid token '" + ident
									+ "' found in line " + line);
						}
					} else
						lexicalError("Character " + valor + " found in line "
								+ line);
				}
			}
			this.filereader.close();
		} catch (IOException e) {
			System.out.println("Error E/S: " + e);
		}

	}

	/* SYNTAX OPERATIONS */

	// Return last token
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
				lexicalError(String.format(
						"Invalid token '%s' on line %d . Expected token of type SIZE.",
						lexReturned, line));
				return null;
			}
		}
		return lexReturned;
	}

	// Retrieves a token longer than 1 character
	String getText(String lexStart) throws IOException {
		String lexReturned = lexStart;
		char valor = nextChar();
		while (Character.isDigit(valor) || Character.isAlphabetic(valor)
				|| (valor == '-')) {
			lexReturned = lexReturned + (valor);
			valor = nextChar();
		}
		returnChar(valor);
		return lexReturned;
	}

	// Removes a comment enclosed between '/*' and '*/'
	boolean deleteComment() throws IOException {
		boolean ret = false;
		char nextChar = nextChar();
		if (nextChar != '*')
			return false;
		do {
			nextChar = nextChar();
			if (nextChar == (char) -1) // Unexpected EOF
				return false;
			if (nextChar == '\n') // Keep counting lines
				line++;
			if (nextChar == '*') {
				nextChar = nextChar();
				if (nextChar == '/') {
					ret = true;
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
			String ret = "\nErrors found running the lexicon:\n";
			for (String error : errors) {
				ret += "\t => " + error + "\n";
			}
			return ret;
		}
		return "Unknown";
	}
}
