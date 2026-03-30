package tokenizer ;  

public enum TokenType {
    // Literals
    NUMBER,    // for defining number data types
    STRING,    // for holding string data types
    IDENTIFIER,  // for variable names

    // Arithmetic operators
    PLUS,       // +
    MINUS,      // -
    STAR,       // *
    SLASH,      // /

    // Comparison operators
    GT,         // >
    LT,         // <
    EQ_EQ,      // ==

    // CALC-specific keywords / symbols
    ASSIGN,     // :=
    PRINT,      // >>
    IF,         // ?
    REPEAT,     // @
    ARROW,      // =>

    // Structural
    NEWLINE,
    EOF
}
