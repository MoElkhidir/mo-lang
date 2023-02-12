package com.moelkhidir.languages.core;

import com.moelkhidir.languages.MoLang;
import com.moelkhidir.languages.core.Expr.Assign;
import com.moelkhidir.languages.core.Expr.Binary;
import com.moelkhidir.languages.core.Expr.Grouping;
import com.moelkhidir.languages.core.Expr.Literal;
import com.moelkhidir.languages.core.Expr.Unary;
import com.moelkhidir.languages.core.Expr.Variable;
import com.moelkhidir.languages.core.Stmt.Block;
import com.moelkhidir.languages.core.Stmt.Expression;
import com.moelkhidir.languages.core.Stmt.Print;
import com.moelkhidir.languages.core.Stmt.Var;
import java.util.List;

public class REPLInterpreter extends Interpreter{

  @Override
  public Void visitExpressionStmt(Expression stmt) {
    System.out.println(super.stringify(super.evaluate(stmt.expression)));
    return null;
  }

}
