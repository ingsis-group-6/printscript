package lexer

import TokenTypeManager
import lexer.factory.TokenTypeManagerFactory
import lexer.implementation.Lexer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import lexer.token.Token
import lexer.token.TokenType
import java.lang.AssertionError

class TokenTest {

    private val lexer = Lexer(TokenTypeManagerFactory.createPrintScriptTokenTypeManager(), listOf(' ', '\n'), listOf(';',':'))

    @Test
    fun stringLiteralTest(){
        val token = Token(0,TokenType.STRING_LITERAL, "'Ricardo mira como anda el Regex!$!@#!'", 0)
        val tokenList = listOf<Token>(token)
        val testToken = lexer.extractTokensFromLine("'Ricardo mira como anda el Regex!$!@#!'", 0)

        Assertions.assertEquals(tokenList, testToken)

        //este test no anda

    }

    @Test
    fun matchRegex(){
        val stringRegex = Regex("^\'[a-zA-Z_$].+( [a-zA-Z_\$])*\'")
        val stringToMatch = "'Ricardo mira como anda el Regex!$!@#!'"
        println(stringToMatch)
        Assertions.assertTrue(stringRegex.matches(stringToMatch))
    }
}

//TODO: arreglar bien el string literal, este test de arriba no anda perro