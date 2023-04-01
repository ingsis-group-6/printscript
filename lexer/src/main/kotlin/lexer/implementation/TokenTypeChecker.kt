package lexer.implementation

import common.token.TokenType

data class TokenTypeChecker(val tokenType: TokenType, val validateType: (String) -> Boolean)
