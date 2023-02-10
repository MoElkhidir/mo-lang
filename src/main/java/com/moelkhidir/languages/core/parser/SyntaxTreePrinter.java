package com.moelkhidir.languages.core.parser;

import com.moelkhidir.languages.core.Token;
import com.moelkhidir.languages.core.TokenType;
import com.moelkhidir.languages.core.parser.Expression.Binary;
import com.moelkhidir.languages.core.parser.Expression.Grouping;
import com.moelkhidir.languages.core.parser.Expression.Literal;
import com.moelkhidir.languages.core.parser.Expression.Unary;

public class SyntaxTreePrinter implements Expression.Visitor<String> {

  // just an example expression and how it gets printed
  public static void main(String[] args) {
    Expression expression = new Expression.Binary(
        new Expression.Unary(
            new Token(TokenType.MINUS, "-", null, 1),
            new Expression.Literal(123)),
        new Token(TokenType.STAR, "*", null, 1),
        new Expression.Grouping(
            new Expression.Literal(45.67)));
    System.out.println(new SyntaxTreePrinter().print(expression));
  }

  String print(Expression expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpression(Binary expression) {
    return parenthesize(expression.operator.lexeme,expression.left, expression.right);
  }

  @Override
  public String visitGroupingExpression(Grouping expression) {
    return parenthesize("group", expression.expression);
  }

  @Override
  public String visitLiteralExpression(Literal expression) {
    if (expression.value == null) return "nil";
    return expression.value.toString();
  }

  @Override
  public String visitUnaryExpression(Unary expression) {
    return parenthesize(expression.operator.lexeme, expression.right);

  }

  private String parenthesize(String name, Expression... exprs) {
    StringBuilder builder = new StringBuilder();
    builder.append("(").append(name);
    for (Expression expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");
    return builder.toString();
  }

}