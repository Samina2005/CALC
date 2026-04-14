import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import runtime.Interpreter;

/**
 * Entry point for the CALC Mini Scripting Engine.
 * Reads a source file path from the command-line argument,
 * reads its contents into a String, and passes it to Interpreter.run().
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <source-file.calc>");
            System.exit(1);
        }

        String filePath = args[0];

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));
            Interpreter interpreter = new Interpreter();
            interpreter.run(sourceCode);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println("Runtime error: " + e.getMessage());
            System.exit(1);
        }
    }
}
