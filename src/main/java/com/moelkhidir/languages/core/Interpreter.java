package com.moelkhidir.languages.core;

import com.moelkhidir.languages.MoLang;
import com.moelkhidir.languages.core.Expr.Assign;
import com.moelkhidir.languages.core.Expr.Binary;
import com.moelkhidir.languages.core.Expr.Call;
import com.moelkhidir.languages.core.Expr.Grouping;
import com.moelkhidir.languages.core.Expr.Literal;
import com.moelkhidir.languages.core.Expr.Logical;
import com.moelkhidir.languages.core.Expr.Unary;
import com.moelkhidir.languages.core.Expr.Variable;
import com.moelkhidir.languages.core.Stmt.Block;
import com.moelkhidir.languages.core.Stmt.Expression;
import com.moelkhidir.languages.core.Stmt.Function;
import com.moelkhidir.languages.core.Stmt.If;
import com.moelkhidir.languages.core.Stmt.Print;
import com.moelkhidir.languages.core.Stmt.Var;
import com.moelkhidir.languages.core.Stmt.While;
import java.util.ArrayList;
import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
  final Environment globals = new Environment();
  private Environment environment = globals;

  public Interpreter() {
    globals.define("clock", new MoLangCallable() {
      @Override
      public int arity() { return 0; }
      @Override
      public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double)System.currentTimeMillis() / 1000.0;
      }

      @Override
      public String toString() { return "<native fn>"; }
    });
  }

  public void interpret(List<Stmt> statements) {
    try {
      for (Stmt statement : statements) {
        execute(statement);
      }
    } catch (RuntimeError error) {
      MoLang.runtimeError(error);
    }
  }

  private void execute(Stmt stmt) {
    stmt.accept(this);
  }

  String stringify(Object object) {
    if (object == null) {
      return "nil";
    }
    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length() - 2);
      }
      return text;
    }
    return object.toString();
  }

  @Override
  public Object visitAssignExpr(Assign expr) {
    Object value = evaluate(expr.value);
    environment.assign(expr.name, value);
    return value;
  }

  @Override
  public Object visitBinaryExpr(Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);
    switch (expr.operator.type) {
      case GREATER:
        checkNumberOperands(expr.operator, left, right);
        return (double) left > (double) right;
      case GREATER_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return (double) left >= (double) right;
      case LESS:
        checkNumberOperands(expr.operator, left, right);
        return (double) left < (double) right;
      case LESS_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return (double) left <= (double) right;
      case MINUS:
        checkNumberOperands(expr.operator, left, right);
        return (double) left - (double) right;
      case PLUS:

        if (left instanceof Double && right instanceof Double) {
          return (double) left + (double) right;
        }
        if (left instanceof String && right instanceof String) {
          return (String) left + (String) right;
        }
        throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
      case SLASH:
        checkNumberOperands(expr.operator, left, right);
        return (double) left / (double) right;
      case STAR:
        checkNumberOperands(expr.operator, left, right);
        return (double) left * (double) right;
      case BANG_EQUAL:
        return !isEqual(left, right);
      case EQUAL_EQUAL:
        return isEqual(left, right);
    }
    // Unreachable.
    return null;
  }

  @Override
  public Object visitCallExpr(Call expr) {
    Object callee = evaluate(expr.callee);
    List<Object> arguments = new ArrayList<>();
    for (Expr argument : expr.arguments) {
      arguments.add(evaluate(argument));
    }

    // making sure that the callee is actually a callable thing
    if (!(callee instanceof MoLangCallable)) {
      throw new RuntimeError(expr.paren,
          "Can only call functions and classes.");
    }

    MoLangCallable function = (MoLangCallable) callee;

    // verifying that the arguments passed matches the arguments length
    // in function definition
    if (arguments.size() != function.arity()) {
      throw new RuntimeError(expr.paren, "Expected " +
          function.arity() + " arguments but got " +
          arguments.size() + ".");
    }
    return function.call(this, arguments);
  }

  @Override
  public Object visitGroupingExpr(Grouping expr) {
    return evaluate(expr.expr);
  }

  @Override
  public Object visitLiteralExpr(Literal expr) {
    return expr.value;
  }

  @Override
  public Object visitLogicalExpr(Logical expr) {
    Object left = evaluate(expr.left);
    if (expr.operator.type == TokenType.OR) {
      if (isTruthy(left)) {
        return left;
      }
    } else {
      if (!isTruthy(left)) {
        return left;
      }
    }
    return evaluate(expr.right);
  }

  @Override
  public Object visitUnaryExpr(Unary expr) {
    Object right = evaluate(expr.right);
    switch (expr.operator.type) {
      case BANG:
        return !isTruthy(right);
      case MINUS:
        checkNumberOperand(expr.operator, right);
        return -(double) right;
    }
    // Unreachable.
    return null;
  }

  @Override
  public Object visitVariableExpr(Variable expr) {
    return environment.get(expr.name);
  }

  private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null) {
      return false;
    }
    return a.equals(b);
  }

  Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  private boolean isTruthy(Object object) {
    if (object == null) {
      return false;
    }
    if (object instanceof Boolean) {
      return (boolean) object;
    }
    return true;
  }

  private void checkNumberOperand(Token operator, Object operand) {
    if (operand instanceof Double) {
      return;
    }
    throw new RuntimeError(operator, "Operand must be a number.");
  }

  private void checkNumberOperands(Token operator,
      Object left, Object right) {
    if (left instanceof Double && right instanceof Double) {
      return;
    }
    throw new RuntimeError(operator, "Operands must be numbers.");
  }

  @Override
  public Void visitBlockStmt(Block stmt) {
    executeBlock(stmt.statements, new Environment(environment));
    return null;
  }

  void executeBlock(List<Stmt> statements, Environment environment) {
    Environment previous = this.environment;
    try {
      this.environment = environment;
      for (Stmt statement : statements) {
        execute(statement);
      }
    } finally {
      this.environment = previous;
    }
  }

  @Override
  public Void visitExpressionStmt(Expression stmt) {
    evaluate(stmt.expression);
    return null;
  }

  @Override
  public Void visitFunctionStmt(Function stmt) {
    MoLangFunction function = new MoLangFunction(stmt);
    environment.define(stmt.name.lexeme, function);
    return null;
  }

  @Override
  public Void visitIfStmt(If stmt) {
    if (isTruthy(evaluate(stmt.condition))) {
      execute(stmt.thenBranch);
    } else if (stmt.elseBranch != null) {
      execute(stmt.elseBranch);
    }
    return null;
  }

  @Override
  public Void visitPrintStmt(Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
    return null;
  }

  @Override
  public Void visitVarStmt(Var stmt) {
    Object value = null;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }
    environment.define(stmt.name.lexeme, value);
    return null;
  }

  @Override
  public Void visitWhileStmt(While stmt) {
    while (isTruthy(evaluate(stmt.condition))) {
      execute(stmt.body);
    }
    return null;
  }
}
