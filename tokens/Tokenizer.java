package tokens;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private int pos;
    private int line;

    public Tokenizer(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char current = source.charAt(pos);

            // skip whitespace
            if (current == ' ' || current == '\t' || current == '\r') {
                pos++;
                continue;
            }

            // newline
            if (current == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                line++;
                pos++;
                continue;
            }

            // comments
            if (current == '#') {
                while (pos < source.length() && source.charAt(pos) != '\n') {
                    pos++;
                }
                continue;
            }

            // string
            if (current == '"') {
                tokens.add(readString());
                continue;
            }

            // number
            if (Character.isDigit(current)) {
                tokens.add(readNumber());
                continue;
            }

            // identifier
            if (Character.isLetter(current) || current == '_') {
                tokens.add(readIdentifier());
                continue;
            }

            // operators
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
                case '<':
                    tokens.add(new Token(TokenType.LT, "<", line));
                    pos++;
                    break;
                case '>':
                    tokens.add(new Token(TokenType.GT, ">", line));
                    pos++;
                    break;
                default:
                    throw new RuntimeException(
                        "Unexpected character '" + current + "' at line " + line
                    );
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private char peek() {
        if (pos + 1 >= source.length()) return '\0';
        return source.charAt(pos + 1);
    }

    private Token readString() {
        int startLine = line;
        pos++; // skip opening "

        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && source.charAt(pos) != '"') {
            if (source.charAt(pos) == '\n') line++;
            sb.append(source.charAt(pos));
            pos++;
        }

        if (pos >= source.length()) {
            throw new RuntimeException("Unterminated string at line " + startLine);
        }

        pos++; // skip closing "
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() &&
               (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }

    private Token readIdentifier() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() &&
              (Character.isLetterOrDigit(source.charAt(pos)) ||
               source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token(TokenType.IDENTIFIER, sb.toString(), startLine);
    }
}