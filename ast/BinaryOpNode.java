package ast;

import runtime.Environment;

/**
 * Represents an operation between two expressions, such as x + y * 2.
 * It holds a left expression, an operator symbol, and a right expression.
 * When evaluated, it evaluates both sides first and then applies the operator.
 */
public class BinaryOpNode implements Expression {
    private final Expression left;
    private final String operator;
    private final Expression right;

    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // Both operands must be numbers for arithmetic and comparison
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
                case ">":  return l > r;
                case "<":  return l < r;
                case "==": return l == r;
                default:
                    throw new RuntimeException("Unknown operator: " + operator);
            }
        }

        // Support string equality comparison
        if (operator.equals("==")) {
            return leftVal.equals(rightVal);
        }

        // Support string concatenation with +
        if (operator.equals("+")) {
            return String.valueOf(leftVal) + String.valueOf(rightVal);
        }

        throw new RuntimeException("Invalid operands for operator '" + operator + "': "
                + leftVal + ", " + rightVal);
    }
}
