package com.moelkhidir.languages.core.parser;

import java.util.List;

public abstract class Stmt {
  public interface Visitor<R> {
    public R visitExpressionStmt(Expression stmt);
    public R visitPrintStmt(Print stmt);
  }
  public static class Expression extends Stmt {

    public final Expr expression;
    Expression(Expr expression) {
      this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitExpressionStmt(this);
    }
  }
  public static class Print extends Stmt {

    public final Expr expression;
    Print(Expr expression) {
      this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
      return visitor.visitPrintStmt(this);
    }
  }

  public abstract <R> R accept(Visitor<R> visitor);
}
