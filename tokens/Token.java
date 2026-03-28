

/**
 * Token class
 * stores type, value and line
 */
public class Token {

    private TokenType type; // what is TokenType??
    private String value;
    private int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() {
        System.out.println("Getting type...");
        return type;
    }

    public String getValue() {
        if(value == null){
            return "";
        }
        return value;
    }

    public int getLine() {
        return line;
    }

    public void setValue(String value){
        System.out.println("Setting value...");
        this.value = value;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Token{";
        result += type;
        result += ", \"";
        result += value;
        result += "\"";
        result += ", line=";
        result += line;
        result += "}";
        return result;
    }
}
