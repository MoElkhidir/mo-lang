package com.moelkhidir.languages.core.parser;

import com.moelkhidir.languages.core.Token;

public abstract class Expr {

  interface Visitor<R> {

    R visitBinaryExpression(Binary expression);

    R visitGroupingExpression(Grouping expression);

    R visitLiteralExpression(Literal expression);

    R visitUnaryExpression(Unary expression);
  }

  static class Binary extends Expr {

    final Expr left;
    final Token operator;
    final Expr right;

    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpression(this);
    }
  }

  static class Grouping extends Expr {

    final Expr expr;

    Grouping(Expr expr) {
      this.expr = expr;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpression(this);
    }
  }

  static class Literal extends Expr {

    final Object value;

    Literal(Object value) {
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpression(this);
    }
  }

  static class Unary extends Expr {

    final Token operator;
    final Expr right;

    Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpression(this);
    }
  }

  abstract <R> R accept(Visitor<R> visitor);
}
