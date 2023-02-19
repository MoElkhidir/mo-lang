package com.moelkhidir.languages.core;

import java.util.List;

public abstract class Expr {
  public interface Visitor<R> {
    public R visitAssignExpr(Assign expr);
    public R visitBinaryExpr(Binary expr);
    public R visitCallExpr(Call expr);
    public R visitGroupingExpr(Grouping expr);
    public R visitLiteralExpr(Literal expr);
    public R visitLogicalExpr(Logical expr);
    public R visitUnaryExpr(Unary expr);
    public R visitVariableExpr(Variable expr);
  }
  public static class Assign extends Expr {

    public final Token name;
    public final Expr value;
    Assign(Token name, Expr value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitAssignExpr(this);
    }
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
      return visitor.visitBinaryExpr(this);
    }
  }
  public static class Call extends Expr {

    public final Expr callee;
    public final Token paren;
    public final List<Expr> arguments;
    Call(Expr callee, Token paren, List<Expr> arguments) {
      this.callee = callee;
      this.paren = paren;
      this.arguments = arguments;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitCallExpr(this);
    }
  }
  public static class Grouping extends Expr {

    public final Expr expr;
    Grouping(Expr expr) {
      this.expr = expr;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpr(this);
    }
  }
  public static class Literal extends Expr {

    public final Object value;
    Literal(Object value) {
      this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }
  }
  public static class Logical extends Expr {

    public final Expr left;
    public final Token operator;
    public final Expr right;
    Logical(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitLogicalExpr(this);
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
      return visitor.visitUnaryExpr(this);
    }
  }
  public static class Variable extends Expr {

    public final Token name;
    Variable(Token name) {
      this.name = name;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitVariableExpr(this);
    }
  }

  public abstract <R> R accept(Visitor<R> visitor);
}
