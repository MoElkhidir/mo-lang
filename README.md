# Welcome to `mo-lang`
**Disclaimer:** The language is still in an early phase of development.

**Current features:**
1. Source-code scanning and token generation.
2. Expression parsing.
3. Statement parsing.
4. Evaluating statements (currently only print and expression statements.)
5. Global variable management.
6. Scoped variable management.


## How to run your first program:
Goes without saying that given the list of feature(s) above, your first program is not even executable. 
Nonetheless, it is still fun to run the scanner and see what it does!

1. Package the source code

```shell
mvn package
```
2. Run the program in interactive mode

```shell
java -jar ./target/mo-lang-1.0-SNAPSHOT.jar
```
You could also run a source code file:
```shell
java -jar ./target/mo-lang-1.0-SNAPSHOT.jar ./src/main/resources/examples/statements.mo
```
Mo-Lang currently can only execute two types of statements, print and expression.

The expression statements are evaluated but not printed.

If you run `examples/statements.mo` you should see something like this:
```text
hello world!
nil
20
false
```