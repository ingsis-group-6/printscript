package lexer.`interface`

import lexer.implementation.TokenTypeChecker

interface TokenTypeManager {

    fun getTokenTypeList(): List<TokenTypeChecker>
}
