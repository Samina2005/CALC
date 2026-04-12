package runtime;

import java.util.ArrayList;
import java.util.List;

import tokens.*;
import ast.*;
import instructions.*;

/**
 * The Parser reads the List of Tokens produced by the Tokenizer and builds
 * a List of Instructions. It uses recursive descent with operator-precedence
 * parsing for expressions.
 *
 * Operator precedence (lowest → highest):
 *   comparison  (>, <, ==)
 *   addition    (+, -)
 *   multiplication (*, /)
 *   primary     (NUMBER, STRING, IDENTIFIER)
 */
public class Parser {
    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    /**
     * Parse the full token stream into a list of top-level instructions.
     */
    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();

        skipNewlines();

        while (!isAtEnd()) {
            instructions.add(parseInstruction());
            skipNewlines();
        }

        return instructions;
    }

    // ================================================================
    //  Instruction Parsing
    // ================================================================

    /**
     * Decide what kind of instruction we are reading based on the current token.
     */
    private Instruction parseInstruction() {
        Token current = current();

        switch (current.getType()) {
            case IDENTIFIER:
                return parseAssignment();
            case PRINT:
                return parsePrint();
            case IF:
                return parseIf();
            case REPEAT:
                return parseRepeat();
            default:
                throw new RuntimeException("Unexpected token " + current
                        + " at line " + current.getLine());
        }
    }

    /**
     * Parse assignment:  IDENTIFIER := expression
     */
    private Instruction parseAssignment() {
        String varName = current().getValue();
        advance(); // consume IDENTIFIER

        expect(TokenType.ASSIGN, ":=");
        advance(); // consume :=

        Expression expr = parseExpression();
        return new AssignInstruction(varName, expr);
    }

    /**
     * Parse print:  >> expression
     */
    private Instruction parsePrint() {
        advance(); // consume >>
        Expression expr = parseExpression();
        return new PrintInstruction(expr);
    }

    /**
     * Parse conditional:
     *   ? condition =>
     *       body (indented instructions)
     */
    private Instruction parseIf() {
        advance(); // consume ?

        Expression condition = parseExpression();

        expect(TokenType.ARROW, "=>");
        advance(); // consume =>

        List<Instruction> body = parseBlock();
        return new IfInstruction(condition, body);
    }

    /**
     * Parse repeat loop:
     *   @ count =>
     *       body (indented instructions)
     */
    private Instruction parseRepeat() {
        advance(); // consume @

        Token countToken = current();
        if (countToken.getType() != TokenType.NUMBER) {
            throw new RuntimeException("Expected a number after '@' at line "
                    + countToken.getLine());
        }
        int count = (int) Double.parseDouble(countToken.getValue());
        advance(); // consume number

        expect(TokenType.ARROW, "=>");
        advance(); // consume =>

        List<Instruction> body = parseBlock();
        return new RepeatInstruction(count, body);
    }

    /**
     * Parse a block of indented instructions following => NEWLINE.
     * The block ends when we hit a line that is NOT indented (i.e. does not
     * start with whitespace in the original source), or when we reach EOF.
     *
     * Strategy: after the =>, we expect a NEWLINE. Then we collect instructions
     * on subsequent lines that belong to the block. We detect block membership
     * by checking whether the next non-newline token is on a line number
     * greater than the header line, and the block continues as long as consecutive
     * instructions follow. We use a simple heuristic: the first instruction
     * in the block sets the "body line" reference; subsequent instructions
     * on lines > header line are part of the body.
     */
    private List<Instruction> parseBlock() {
        // Consume the NEWLINE after =>
        if (!isAtEnd() && current().getType() == TokenType.NEWLINE) {
            advance();
        }
        skipNewlines();

        List<Instruction> body = new ArrayList<>();
        int headerLine = tokens.get(pos > 0 ? pos - 1 : 0).getLine();

        // Collect instructions that are on lines after the header
        while (!isAtEnd()) {
            Token next = current();

            // If the next meaningful token is on a line > headerLine,
            // it is part of the body
            if (next.getLine() > headerLine) {
                body.add(parseInstruction());
                skipNewlines();

                // Update headerLine reference to track that body continues
                // as long as line numbers keep increasing
            } else {
                break;
            }
        }

        if (body.isEmpty()) {
            throw new RuntimeException("Expected at least one instruction in block "
                    + "after '=>' at line " + headerLine);
        }

        return body;
    }

    // ================================================================
    //  Expression Parsing (Recursive Descent with Precedence)
    // ================================================================

    /**
     * Parse a comparison expression (lowest precedence among expressions).
     * Handles: >, <, ==
     */
    private Expression parseExpression() {
        Expression left = parseAddition();

        while (!isAtEnd() && isComparisonOperator(current().getType())) {
            String op = current().getValue();
            advance();
            Expression right = parseAddition();
            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    /**
     * Parse addition / subtraction.
     * Handles: +, -
     */
    private Expression parseAddition() {
        Expression left = parseTerm();

        while (!isAtEnd() && (current().getType() == TokenType.PLUS
                || current().getType() == TokenType.MINUS)) {
            String op = current().getValue();
            advance();
            Expression right = parseTerm();
            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    /**
     * Parse multiplication / division.
     * Handles: *, /
     */
    private Expression parseTerm() {
        Expression left = parsePrimary();

        while (!isAtEnd() && (current().getType() == TokenType.STAR
                || current().getType() == TokenType.SLASH)) {
            String op = current().getValue();
            advance();
            Expression right = parsePrimary();
            left = new BinaryOpNode(left, op, right);
        }

        return left;
    }

    /**
     * Parse a primary expression: NUMBER, STRING, or IDENTIFIER.
     */
    private Expression parsePrimary() {
        Token token = current();

        switch (token.getType()) {
            case NUMBER:
                advance();
                return new NumberNode(Double.parseDouble(token.getValue()));
            case STRING:
                advance();
                return new StringNode(token.getValue());
            case IDENTIFIER:
                advance();
                return new VariableNode(token.getValue());
            default:
                throw new RuntimeException("Expected a number, string, or variable "
                        + "but found " + token + " at line " + token.getLine());
        }
    }

    // ================================================================
    //  Utility Methods
    // ================================================================

    /** Return the current token without advancing. */
    private Token current() {
        return tokens.get(pos);
    }

    /** Advance to the next token and return the one we just consumed. */
    private Token advance() {
        Token t = tokens.get(pos);
        pos++;
        return t;
    }

    /** Check whether we have reached the EOF token. */
    private boolean isAtEnd() {
        return tokens.get(pos).getType() == TokenType.EOF;
    }

    /** Skip over any consecutive NEWLINE tokens. */
    private void skipNewlines() {
        while (!isAtEnd() && current().getType() == TokenType.NEWLINE) {
            advance();
        }
    }

    /** Assert that the current token has the expected type. */
    private void expect(TokenType type, String description) {
        if (current().getType() != type) {
            throw new RuntimeException("Expected '" + description + "' but found '"
                    + current().getValue() + "' at line " + current().getLine());
        }
    }

    /** Check whether a token type is a comparison operator. */
    private boolean isComparisonOperator(TokenType type) {
        return type == TokenType.GT || type == TokenType.LT || type == TokenType.EQ_EQ;
    }
}
