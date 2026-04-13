package instructions;

import ast.Expression;
import runtime.Environment;

/**
 * Handles printing — for example >> x in CALC.
 * Evaluates the expression and prints the result to standard output.
 * Whole numbers are printed without a decimal point (e.g. 16 not 16.0).
 */
public class PrintInstruction implements Instruction {
    private final Expression expression;

    public PrintInstruction(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(Environment env) {
        Object value = expression.evaluate(env);

        // Format doubles that are whole numbers without the decimal point
        if (value instanceof Double) {
            double d = (Double) value;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                System.out.println((long) d);
            } else {
                System.out.println(d);
            }
        } else {
            System.out.println(value);
        }
    }
}
