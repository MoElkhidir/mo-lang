package com.moelkhidir.languages;

import com.moelkhidir.languages.core.REPLInterpreter;
import com.moelkhidir.languages.core.RuntimeError;
import com.moelkhidir.languages.core.Interpreter;
import com.moelkhidir.languages.core.Scanner;
import com.moelkhidir.languages.core.Token;
import com.moelkhidir.languages.core.TokenType;
import com.moelkhidir.languages.core.Parser;
import com.moelkhidir.languages.core.Stmt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MoLang {
  private static final Interpreter interpreter = new Interpreter();
  static boolean hadError = false;
  static boolean hadRuntimeError = false;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: mo-lang [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    Interpreter interpreter = new Interpreter();
    run(new String(bytes, Charset.defaultCharset()), interpreter);
    if (hadError) {
      System.exit(65);
    }
    if (hadRuntimeError) {
      System.exit(70);
    }
  }

  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    Interpreter REPLInterpreter = new REPLInterpreter();
    for (; ; ) {
      System.out.print("> ");
      String line = reader.readLine();
      if (line == null) {
        break;
      }
      run(line, REPLInterpreter);
      hadError = false;
    }
  }

  private static void run(String source, Interpreter interpreter) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();
    Parser parser = new Parser(tokens);
    List<Stmt> statements = parser.parse();    // Stop if there was a syntax error.
    if (hadError) {
      return;
    }

    interpreter.interpret(statements);
  }

  public static void error(int line, String message) {
    report(line, "", message);
  }

  public static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

  private static void report(int line, String where,
      String message) {
    System.err.println(
        "[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }

  public static void runtimeError(RuntimeError error) {
    System.err.println(error.getMessage() +
        "\n[line " + error.token.line + "]");
    hadRuntimeError = true;
  }

}
