package com.moelkhidir.languages.core.parser;

import com.moelkhidir.languages.core.Token;

public abstract class Expr {

  public interface Visitor<R> {

    R visitBinaryExpression(Binary expr);

    R visitGroupingExpression(Grouping expr);

    R visitLiteralExpression(Literal expr);

    R visitUnaryExpression(Unary expr);
  }

  public static class Binary extends Expr {

    public final Expr left;
    public final Token operator;
    public final Expr right;

    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpression(this);
    }
  }

  public static class Grouping extends Expr {

    public final Expr expr;

    Grouping(Expr expr) {
      this.expr = expr;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpression(this);
    }
  }

  public static class Literal extends Expr {

    public final Object value;

    Literal(Object value) {
      this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpression(this);
    }
  }

  public static class Unary extends Expr {

    public final Token operator;
    public final Expr right;

    Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpression(this);
    }
  }

  public abstract <R> R accept(Visitor<R> visitor);
}
