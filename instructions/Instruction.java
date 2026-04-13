package instructions;

import runtime.Environment;

/**
 * An instruction is one complete action the CALC language can perform.
 * All instruction classes implement this interface.
 */
public interface Instruction {
    /**
     * Execute this instruction, reading and writing variables via the Environment.
     */
    void execute(Environment env);
}
