package com.craftinginterpreters.lox.model;

public abstract class Expr {
    public static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public final Expr left;
        public final Token operator;
        public final Expr right;
    }

    public static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        public final Expr expression;
    }

    public static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        public final Token operator;
        public final Expr right;
    }

    public static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        public final Object value;
    }
}
