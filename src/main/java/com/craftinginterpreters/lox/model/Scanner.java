package com.craftinginterpreters.lox.model;

import com.craftinginterpreters.lox.Lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokenList = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int lineNumber = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false",  TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super",  TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while",  TokenType.WHILE);
    }

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokenList.add(new Token(TokenType.EOF, "", null, lineNumber));
        return tokenList;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char chr = advance();
        switch (chr) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                if (match('/')) {
                    // Comment statement , till end of the line.
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                lineNumber++;
                break;

            case '"':
                string();
                break;

            default:
                if (isDigit(chr)) {
                    number();
                } else if (isAlpha(chr)) {
                    identifier();
                } else {
                    Lox.error(lineNumber, "Unexpected character.");
                }
                break;
        }
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object literal) {
        String text = source.substring(start, current);
        tokenList.add(new Token(tokenType, text, literal, lineNumber));
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                lineNumber++;
            }
            advance();
        }

        if (isAtEnd()) {
            Lox.error(lineNumber, "Unterminated string.");
            return;
        }

        advance();      // For closing " - for a string

        String stringValue = source.substring(start+1, current-1);          // Exclude double quotes "stringValue"
        addToken(TokenType.STRING, stringValue);
    }

    private boolean isDigit(char chr) {
        return chr >= '0' && chr <= '9';
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        // Check for fractional part
        if (peek() == '.' && isDigit(peekNext())) {
            // do-while , so that in first iteration we consume the '.' in fractional part
            do {
                advance();
            } while (isDigit(peek()));
        }

        addToken(TokenType.NUMBER, Double.parseDouble( source.substring(start, current) ));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current+1);
    }

    private boolean isAlpha(char chr) {
        return (chr >= 'a' && chr <= 'z') ||
                (chr >= 'A' &&  chr <= 'Z') ||
                ( chr == '_' );
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String textValue = source.substring(start, current);
        TokenType tokenType = keywords.get(textValue);
        if (tokenType == null) {
            tokenType = TokenType.IDENTIFIER;               // else it is a Keyword
        }
        addToken(tokenType);
    }

    private boolean isAlphaNumeric(char chr) {
        return isAlpha(chr) || isDigit(chr);
    }
}
