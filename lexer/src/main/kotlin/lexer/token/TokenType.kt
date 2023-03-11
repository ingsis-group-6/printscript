package lexer.token

enum class TokenType {
    IDENTIFIER,

    //KEYWORDS
    LET,
    PRINTLN,

    //TYPES
    STRINGTYPE,
    NUMBERTYPE,

    //OPERATIONS
    PLUS,
    MINUS,
    TIMES,
    DIVIDEDBY,
    OPENPAREN,
    CLOSEPAREN,

    //DECLARATIONS
    NumericLiteral,
    StringLiteral,
    RegularExpressionLiteral,
}