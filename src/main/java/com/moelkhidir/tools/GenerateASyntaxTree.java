package com.moelkhidir.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateASyntaxTree {

  public static void main(String[] args) throws IOException {
    String outputDir = "src/main/java/com/moelkhidir/languages/core";
//    if (args.length != 1) {
//      InputStreamReader input = new InputStreamReader(System.in);
//      BufferedReader reader = new BufferedReader(input);
//      outputDir = reader.readLine();
//    } else {
//      outputDir = args[0];
//    }

    defineAst(
        outputDir,
        "Expr",
        Arrays.asList(
            "Assign   : Token name, Expr value",
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expr",
            "Literal  : Object value",
            "Logical  : Expr left, Token operator, Expr right",
            "Unary    : Token operator, Expr right",
            "Variable : Token name"
        )
    );

    defineAst(
        outputDir,
        "Stmt",
        Arrays.asList(
            "Block      : List<Stmt> statements",
            "Expression : Expr expression",
            "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Print      : Expr expression",
            "Var        : Token name, Expr initializer"
        ));
  }

  private static void defineAst(String outputDir, String baseName, List<String> types)
      throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, "UTF-8");
    writer.println("package com.moelkhidir.languages.core;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println();
    writer.println("public abstract class " + baseName + " {");

    defineVisitor(writer, baseName, types);

    // The AST classes.
    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineType(writer, baseName, className, fields);
    }

    // The base accept() method.
    writer.println();
    writer.println("  public abstract <R> R accept(Visitor<R> visitor);");

    writer.println("}");
    writer.close();
  }

  private static void defineType(
      PrintWriter writer, String baseName, String className, String fieldList) {
    writer.println("  public static class " + className + " extends " + baseName + " {");
    // Fields.
    String[] fields = fieldList.split(", ");
    writer.println();
    for (String field : fields) {
      writer.println("    public final " + field + ";");
    }

    // Constructor.
    writer.println("    " + className + "(" + fieldList + ") {");
    // Store parameters in fields.
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }
    // end constructor
    writer.println("    }");

    // Visitor pattern.
    writer.println();
    writer.println("    @Override");
    writer.println("    public <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" +
        className + baseName + "(this);");
    writer.println("    }");

    // end class
    writer.println("  }");
  }

  private static void defineVisitor(
      PrintWriter writer, String baseName, List<String> types) {
    writer.println("  public interface Visitor<R> {");
    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    public R visit" + typeName + baseName + "(" +
          typeName + " " + baseName.toLowerCase() + ");");
    }
    writer.println("  }");
  }
}
