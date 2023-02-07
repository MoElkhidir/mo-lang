# Welcome to `mo-lang`
**Disclaimer:** The language is still in an early phase of development.

**Current features:**
1. Source-code scanning and token generation.


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
java -jar ./target/mo-lang-1.0-SNAPSHOT.jar ./src/main/resources/examples/variables.mo
```
Mo-Lang currently only spits out the tokens generated by the scanner, in the example of using
`variables.mo` you might see something like this:
```text
IDENTIFIER string null
IDENTIFIER name null
EQUAL = null
STRING "mo" mo
IDENTIFIER number null
IDENTIFIER height null
EQUAL = null
NUMBER 1843 1843.0
EOF  null
```