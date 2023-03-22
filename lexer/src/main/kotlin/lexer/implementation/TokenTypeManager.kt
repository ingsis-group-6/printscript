import lexer.implementation.TokenTypeChecker
import lexer.`interface`.TokenTypeManager

class TokenTypeManager(private val tokenTypeList: List<TokenTypeChecker>) :
    TokenTypeManager {
    override fun getTokenTypeList(): List<TokenTypeChecker> {
        return tokenTypeList
    }
}
