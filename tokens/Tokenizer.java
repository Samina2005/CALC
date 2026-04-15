package tokens;

import java.util.ArrayList;
import java.util.List;

/**
 * The Tokenizer reads the raw source code as a single String and produces
 * a List of Tokens. It walks through the characters one at a time, recognises
 * patterns, and emits a Token for each one.
 */
public class Tokenizer {
    private final String source;
    private int pos;
    private int line;

    public Tokenizer(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }

    /**
     * Walk through source character by character.
     * When you recognise a complete token, add it to a list.
     * At the end, add a token of type EOF.
     * Return the completed list.
     */
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char current = source.charAt(pos);

            // Skip spaces and tabs (but NOT newlines)
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

            // Single-line comments (optional extension — skip lines starting with #)
            if (current == '#') {
                while (pos < source.length() && source.charAt(pos) != '\n') {
                    pos++;
                }
                continue;
            }

            // String literals: "..."
            if (current == '"') {
                tokens.add(readString());
                continue;
            }

            // Numbers: digits and decimals
            if (Character.isDigit(current)) {
                tokens.add(readNumber());
                continue;
            }

            // Identifiers: letters and underscores
            if (Character.isLetter(current) || current == '_') {
                tokens.add(readIdentifier());
                continue;
            }

            // Two-character tokens
            if (current == ':' && peek() == '=') {
                tokens.add(new Token(TokenType.ASSIGN, ":=", line));
                pos += 2;
                continue;
            }
            if (current == '>' && peek() == '>') {
                tokens.add(new Token(TokenType.PRINT, ">>", line));
                pos += 2;
                continue;
            }
            if (current == '=' && peek() == '>') {
                tokens.add(new Token(TokenType.ARROW, "=>", line));
                pos += 2;
                continue;
            }
            if (current == '=' && peek() == '=') {
                tokens.add(new Token(TokenType.EQ_EQ, "==", line));
                pos += 2;
                continue;
            }

            // Single-character tokens
            switch (current) {
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", line));
                    pos++;
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", line));
                    pos++;
                    break;
                case '*':
                    tokens.add(new Token(TokenType.STAR, "*", line));
                    pos++;
                    break;
                case '/':
                    tokens.add(new Token(TokenType.SLASH, "/", line));
                    pos++;
                    break;
                case '>':
                    tokens.add(new Token(TokenType.GT, ">", line));
                    pos++;
                    break;
                case '<':
                    tokens.add(new Token(TokenType.LT, "<", line));
                    pos++;
                    break;
                case '?':
                    tokens.add(new Token(TokenType.IF, "?", line));
                    pos++;
                    break;
                case '@':
                    tokens.add(new Token(TokenType.REPEAT, "@", line));
                    pos++;
                    break;
                default:
                    throw new RuntimeException("Unexpected character '" + current
                            + "' at line " + line);
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    // ---- Helper methods ------------------------------------------------

    /**
     * Peek at the next character without advancing the position.
     */
    private char peek() {
        if (pos + 1 < source.length()) {
            return source.charAt(pos + 1);
        }
        return '\0';
    }

    /**
     * Read a string literal enclosed in double quotes.
     */
    private Token readString() {
        int startLine = line;
        pos++; // skip opening quote
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') {
                line++;
            }
            sb.append(source.charAt(pos));
            pos++;
        }

        if (pos >= source.length()) {
            throw new RuntimeException("Unterminated string at line " + startLine);
        }

        pos++; // skip closing quote
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    /**
     * Read a numeric literal (integer or decimal).
     */
    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && (Character.isDigit(source.charAt(pos))
                || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }

    /**
     * Read an identifier (variable name or keyword).
     */
    private Token readIdentifier() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && (Character.isLetterOrDigit(source.charAt(pos))
                || source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token(TokenType.IDENTIFIER, sb.toString(), startLine);
    }
}
