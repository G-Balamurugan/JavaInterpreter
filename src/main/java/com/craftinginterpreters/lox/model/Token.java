package com.craftinginterpreters.lox.model;


public class Token {
    public final TokenType tokenType;
    public final String lexeme;
    public final Object literal;
    public final int lineNumber;

    Token(TokenType tokenType, String lexeme, Object literal, int lineNumber) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.literal = literal;
        this.lineNumber = lineNumber;
    }

    public String toString() {
        return tokenType + " " + lexeme + " " + literal;
    }
}
