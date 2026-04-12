package ast;

import runtime.Environment;
import java.util.Objects;
import java.util.Set;

//Represents an operation between two expressions, such as x + y * 2.

public final class BinaryOpNode implements Expression {
    private static final Set<String> VALID_OPERATORS = Set.of("+", "-", "*", "/", ">", "<", "==");

    private final Expression left;
    private final String operator;
    private final Expression right;

    public BinaryOpNode(Expression left, String operator, Expression right) {
        if (left == null || operator == null || right == null) {
            throw new IllegalArgumentException("BinaryOpNode arguments cannot be null");
        }

        if (!VALID_OPERATORS.contains(operator)) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // Numeric operations
        if (leftVal instanceof Double && rightVal instanceof Double) {
            double l = (Double) leftVal;
            double r = (Double) rightVal;

            switch (operator) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/":
                    if (r == 0) {
                        throw new RuntimeException("Division by zero");
                    }
                    return l / r;
                case ">": return l > r;
                case "<": return l < r;
                case "==": return l == r;
                default:
                    // This should never happen due to validation
                    throw new IllegalStateException("Unexpected operator: " + operator);
            }
        }

        // Non-numeric operations
        switch (operator) {
            case "==":
                return Objects.equals(leftVal, rightVal);
            case "+":
                return String.valueOf(leftVal) + String.valueOf(rightVal);
            default:
                throw new RuntimeException(
                        "Invalid operands for operator '" + operator + "': "
                                + leftVal + ", " + rightVal
                );
        }
    }
}
