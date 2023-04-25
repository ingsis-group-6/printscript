package lexer.factory

import lexer.implementation.StreamLexer
import java.io.InputStream

object LexerFactory {

    fun createLexer(stream: InputStream, version: String): StreamLexer {
        return StreamLexer(stream, TokenTypeManagerFactory.createPrintScriptTokenTypeManager(version), listOf(';', ':', '(', ')', ' ', '\n', '\t', '+', '=', '-', '*', '/', '{', '}'))
    }
}
