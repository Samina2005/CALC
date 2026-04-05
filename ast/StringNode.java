package ast;

import runtime.Environment;

// Represents a literal string in the source code, such as "hello".
public final class StringNode implements Expression {
    private final String value;

    public StringNode(String value) {
        if (value == null) {
            throw new IllegalArgumentException("StringNode value cannot be null");
        }
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
