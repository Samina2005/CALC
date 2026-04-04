package tokens;

import java.util.ArrayList;
import java.util.List;

// The Tokenizer reads the raw source code as a single String and produces
// a List of Tokens. It walks through the characters one at a time, recognises
// patterns, and emits a Token for each one.

public class Tokenizer {
    private final String source;
    private int pos;
    private int line;

    public Tokenizer(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }

    // Walk through source character by character.
    // When you recognise a complete token, add it to a list.
    // At the end, add a token of type EOF.
    // Return the completed list.

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char current = source.charAt(pos);

            // Skip spaces and tabs
            if (current == ' ' || current == '\t' || current == '\r') {
                pos++;
                continue;
            }

            // Newlines
            if (current == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                line++;
                pos++;
                continue;
            }

            // Single-line comments 
            if (current == '#') {
                while (pos < source.length() && source.charAt(pos) != '\n') {
                    pos++;
                }
                continue;
            }

            // fallback move forward 
            pos++;
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }
}