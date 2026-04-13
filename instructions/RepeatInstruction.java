package instructions;

import java.util.List;

import runtime.Environment;

/**
 * Handles a loop that runs its body a fixed number of times.
 * CALC syntax: @ count => body
 */
public class RepeatInstruction implements Instruction {
    private final int count;
    private final List<Instruction> body;

    public RepeatInstruction(int count, List<Instruction> body) {
        this.count = count;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        for (int i = 0; i < count; i++) {
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
    }
}
