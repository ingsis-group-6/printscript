package lexer.provider

import common.providers.token.TokenProvider
import common.token.Token
import lexer.factory.LexerFactory
import java.io.InputStream
import java.util.*

class FileTokenProvider(stream: InputStream, version: String) : TokenProvider {
    private val lexer = LexerFactory.createLexer(stream, version)
    override fun getToken(): Optional<Token> {
        return lexer.lexToken()
    }

    override fun peekToken(): Token {
        return lexer.peekToken()
    }
}
