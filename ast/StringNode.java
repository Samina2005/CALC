package ast;

import runtime.Environment;

//Represents a literal string in the source code, such as "hello".

public class StringNode implements Expression {
    private final String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}
