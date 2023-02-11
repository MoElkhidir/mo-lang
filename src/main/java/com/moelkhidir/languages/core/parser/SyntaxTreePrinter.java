package com.moelkhidir.languages.core.parser;

import com.moelkhidir.languages.core.Token;
import com.moelkhidir.languages.core.TokenType;
import com.moelkhidir.languages.core.parser.Expr.Binary;
import com.moelkhidir.languages.core.parser.Expr.Grouping;
import com.moelkhidir.languages.core.parser.Expr.Literal;
import com.moelkhidir.languages.core.parser.Expr.Unary;

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
  public String visitBinaryExpression(Binary expr) {
    return parenthesize(expr.operator.lexeme,expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpression(Grouping expr) {
    return parenthesize("group", expr.expr);
  }

  @Override
  public String visitLiteralExpression(Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpression(Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);

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