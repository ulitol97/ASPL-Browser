package simpleCss.parser;


public class Token {
	private TokensId token;
	private String lexeme;
	private int line;
	
	public Token (TokensId token, String lexeme, int line) {
		this.token = token;
		this.lexeme = lexeme;
		this.line = line;
	}
	
	public TokensId getToken () {
		return token;
	}
	
	public String getLexeme () {
		return lexeme;
	}
	
	public int getLine() {
		return line;
	}

	public String toString() {
		return "TOKEN: "+token+" - LEXEMA: "+lexeme+" - LINE: "+line;
	}
}
