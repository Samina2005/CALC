package ast;

import runtime.Environment;

public interface Expression {
    
    Object evaluate(Environment env);
}
