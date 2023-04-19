package lexer.provider

import common.providers.token.TokenProvider
import common.token.Token
import lexer.implementation.Lexer
import lexer.implementation.StreamLexer
import java.io.File
import java.util.*

class FileTokenProvider(file: File): TokenProvider {
    private val lexer = StreamLexer(file)
    override fun getToken(): Optional<Token> {
        return lexer.lexToken()
    }
}