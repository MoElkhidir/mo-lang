package com.moelkhidir.languages.core;

import java.util.List;
public interface MoLangCallable {
  Object call(Interpreter interpreter, List<Object> arguments);
  int arity();
}
