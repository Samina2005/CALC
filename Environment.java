package runtime;
import java.util.*;


public class Environment {
    private final Map<String, Object> variables = new HashMap<>();

    
    public void set(String name, Object value) {  //Store or update the value for the given variable name.
        variables.put(name, value);
    }

    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable not defined: " + name);
        }
        return variables.get(name);
    }
}
