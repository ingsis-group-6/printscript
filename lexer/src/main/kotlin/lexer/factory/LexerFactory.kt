package lexer.factory

import lexer.implementation.StreamLexer
import java.io.File

object LexerFactory {

    fun createLexer(file: File, version: String): StreamLexer {
        return StreamLexer(file, TokenTypeManagerFactory.createPrintScriptTokenTypeManager(version), listOf(';', ':', '(', ')', ' ', '\n', '\t', '+', '=', '-', '*', '/', '{', '}'))
    }
}
