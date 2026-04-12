package ast;

import runtime.Environment;

/**
 * Represents a variable reference such as x or total.
 * When evaluated, it looks up the variable's current value in the Environment.
 */
public class VariableNode implements Expression {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(Environment env) {
        return env.get(name);
    }
}
