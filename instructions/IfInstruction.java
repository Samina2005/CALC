package instructions;

import java.util.List;

import ast.Expression;
import runtime.Environment;

/**
 * Handles a conditional block in CALC: ? condition => body
 * If the condition evaluates to true, the body instructions are executed;
 * otherwise they are skipped.
 */
public class IfInstruction implements Instruction {
    private final Expression condition;
    private final List<Instruction> body;

    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        Object result = condition.evaluate(env);

        if (result instanceof Boolean && (Boolean) result) {
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
    }
}
