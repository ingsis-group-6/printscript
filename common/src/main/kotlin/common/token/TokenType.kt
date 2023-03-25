package common.token

enum class TokenType {

    // ASSIGNATION
    ASSIGNATION,

    SEMICOLON,
    COLON,
    EOF,
    WHITESPACE,

    IDENTIFIER,

    // KEYWORDS
    LET,
    PRINTLN,

    // TYPES
//    STRING_TYPE,
//    NUMBER_TYPE,
    TYPE,

    // OPERATIONS
    PLUS,
    MINUS,
    TIMES,
    DIVIDED_BY,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,

    // DECLARATIONS
    NUMERIC_LITERAL,
    STRING_LITERAL,

    // EVALUATORS
    EQUALS,
    GREATER_EQUALS,
    LESSER_EQUALS,
    GREATER_THAN,
    LESSER_THAN,
    DIFFERENT,
    AND,
    OR
}