package lexer

import lexer.implementation.LexerImpl
import lexer.implementation.TokenTypeList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import lexer.token.Token
import lexer.token.TokenType

class TokenTest {

    private val lexer = LexerImpl(TokenTypeList(listOf("let","string","number")), listOf(' '), listOf(';',':'))

    @Test
    fun stringLiteralTest(){
        val token = Token(0,TokenType.STRING_LITERAL, "'soy un string'", 0)
        val tokenList = listOf<Token>(token)
        val testToken = lexer.extractTokensFromLine("'soy un string'", 0)

        Assertions.assertEquals(tokenList, testToken)

    }
}

//TODO: arreglar bien el string literal, este test de arriba no anda perro