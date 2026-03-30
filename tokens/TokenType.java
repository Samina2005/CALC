package tokens;  

public enum TokenType {
    // literals
    NUMBER,    // for defining number data types
    STRING,    // for holding string data types
    IDENTIFIER,  // for variable names

    // arithmetic operators
    PLUS,       // +
    MINUS,      // -
    STAR,       // *
    SLASH,      // /

    // comparison operators
    GT,         // >
    LT,         // <
    EQ_EQ,      // ==

    // CALC specific keywords or symbols
    ASSIGN,     // :=
    PRINT,      // >>
    IF,         // ?
    REPEAT,     // @
    ARROW,      // =>

    // structural
    NEWLINE,
    EOF
}
