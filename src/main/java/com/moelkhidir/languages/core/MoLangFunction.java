package com.moelkhidir.languages.core;

import java.util.List;

public class MoLangFunction implements MoLangCallable {

  private final Stmt.Function declaration;

  MoLangFunction(Stmt.Function declaration) {
    this.declaration = declaration;
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    Environment environment = new Environment(interpreter.globals);
    for (int i = 0; i < declaration.params.size(); i++) {
      environment.define(declaration.params.get(i).lexeme,
          arguments.get(i));
    }
    interpreter.executeBlock(declaration.body, environment);
    return null;
  }

  @Override
  public int arity() {
    return declaration.params.size();
  }

  @Override
  public String toString() {
    return "<fn " + declaration.name.lexeme + ">";
  }

}
