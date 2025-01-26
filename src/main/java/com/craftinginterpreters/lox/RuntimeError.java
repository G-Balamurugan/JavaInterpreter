package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.model.Token;

class RuntimeError extends RuntimeException {
    final Token token;
    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
