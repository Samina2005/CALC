package runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * The Environment is the variable store — a simple map from variable names
 * to their current values. Every instruction shares one Environment instance
 * during execution.
 */
public class Environment {
    private final Map<String, Object> variables = new HashMap<>();

    /**
     * Store or update the value for the given variable name.
     */
    public void set(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * Return the current value of the variable.
     * If the variable has not been defined, throw a RuntimeException.
     */
    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return variables.get(name);
    }
}


// Using IllegalArgumentException instead of RuntimeException would make the error more specific.
// You could mark method parameters as final to prevent accidental reassignment.

