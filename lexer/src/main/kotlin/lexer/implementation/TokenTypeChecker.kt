package lexer.implementation

import lexer.token.TokenType

data class TokenTypeChecker(val tokenType: TokenType, val validateType: (String) -> Boolean)