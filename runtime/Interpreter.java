package runtime;

import java.util.List;

import tokens.Token;
import tokens.Tokenizer;
import instructions.Instruction;

/**
 * The Interpreter is the entry point that connects all three steps.
 * Its run method accepts source code as a String and produces the program's
 * output.
 */
public class Interpreter {

    /**
     * Run the CALC interpreter pipeline:
     * Step 1 — Tokenize the source code into a list of tokens.
     * Step 2 — Parse the tokens into a list of instructions (AST).
     * Step 3 — Create an Environment and execute each instruction.
     */
    public void run(String sourceCode) {
        // Step 1: Tokenize
        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token> tokens = tokenizer.tokenize();

        // Step 2: Parse
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        // Step 3: Execute
        Environment env = new Environment();
        for (Instruction instruction : instructions) {
            instruction.execute(env);
        }
    }
}
