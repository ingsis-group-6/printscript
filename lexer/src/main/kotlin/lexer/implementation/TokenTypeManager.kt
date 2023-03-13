import lexer.implementation.TokenTypeChecker
import lexer.`interface`.TokenTypeManager
import lexer.token.Token
import lexer.token.TokenType

class TokenTypeManager(private val tokenTypeList: List<TokenTypeChecker>):
    TokenTypeManager {
    override fun getTokenTypeList(): List<TokenTypeChecker> {
        return tokenTypeList
    }
}