package com.moelkhidir.languages.core;

import com.moelkhidir.languages.core.Expr.Assign;
import com.moelkhidir.languages.core.Expr.Binary;
import com.moelkhidir.languages.core.Expr.Grouping;
import com.moelkhidir.languages.core.Expr.Literal;
import com.moelkhidir.languages.core.Expr.Logical;
import com.moelkhidir.languages.core.Expr.Unary;
import com.moelkhidir.languages.core.Expr.Variable;

public class SyntaxTreePrinter implements Expr.Visitor<String> {

  // just an example expr and how it gets printed
  public static void main(String[] args) {
    Expr expr = new Expr.Binary(
        new Expr.Unary(
            new Token(TokenType.MINUS, "-", null, 1),
            new Expr.Literal(123)),
        new Token(TokenType.STAR, "*", null, 1),
        new Expr.Grouping(
            new Expr.Literal(45.67)));
    System.out.println(new SyntaxTreePrinter().print(expr));
  }

  public String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitAssignExpr(Assign expr) {
    return null;
  }

  @Override
  public String visitBinaryExpr(Binary expr) {
    return parenthesize(expr.operator.lexeme,expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpr(Grouping expr) {
    return parenthesize("group", expr.expr);
  }

  @Override
  public String visitLiteralExpr(Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitLogicalExpr(Logical expr) {
    return null;
  }

  @Override
  public String visitUnaryExpr(Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);

  }

  @Override
  public String visitVariableExpr(Variable expr) {
    return null;
  }

  private String parenthesize(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();
    builder.append("(").append(name);
    for (Expr expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");
    return builder.toString();
  }

}