package lexer.provider

import common.providers.token.TokenProvider
import common.token.Token
import lexer.factory.LexerFactory
import java.io.File
import java.util.*

class FileTokenProvider(file: File, version: String) : TokenProvider {
    private val lexer = LexerFactory.createLexer(file, version)
    override fun getToken(): Optional<Token> {
        return lexer.lexToken()
    }

    override fun peekToken(): Token {
        return lexer.peekToken()
    }
}
