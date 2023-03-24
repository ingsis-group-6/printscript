package lexer

import lexer.factory.TokenTypeManagerFactory
import lexer.implementation.Lexer
import common.token.Token
import common.token.TokenType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class TokenTest {

    private val lexer = Lexer(TokenTypeManagerFactory.createPrintScriptTokenTypeManager(), listOf(' ', '\n'), listOf(';', ':'))

    @Test
    fun stringLiteralTest() {
        val token = Token(0, TokenType.STRING_LITERAL, "'Ricardo mira como anda el Regex!$!@#!'", 0)
        val tokenList = listOf<Token>(token)
        val testToken = lexer.extractTokensFromLine("'Ricardo mira como anda el Regex!$!@#!'", 0)

        Assertions.assertEquals(tokenList, testToken)

        // este test no anda
    }

    @Test
    fun matchRegex() {
        val stringRegex = Regex("^\'[a-zA-Z_$].+( [a-zA-Z_\$])*\'")
        val stringToMatch = "'Ricardo mira como anda el Regex!$!@#!'"
        println(stringToMatch)
        Assertions.assertTrue(stringRegex.matches(stringToMatch))
    }

    @Test
    fun extractStringLiteralToken() {
        // val stringLine = "let myVar: string = \"hello\";"
        val stringLine = "let myVar: string = \"hello\";"
        val expectedTokens = listOf(
            Token(0, TokenType.LET, "let", 0),
            Token(1, TokenType.IDENTIFIER, "myVar", 0),
            Token(2, TokenType.COLON, ":", 0),
            Token(3, TokenType.STRING_TYPE, "string", 0),
            Token(4, TokenType.ASSIGNATION, "=", 0),
            Token(5, TokenType.STRING_LITERAL, "\"hello\"", 0),
            Token(6, TokenType.SEMICOLON, ";", 0)
        )
        val result = lexer.extractTokensFromLine(stringLine, 0)
        result.map { println(it) }
        Assertions.assertEquals(expectedTokens, result)
    }

    @Test
    fun testStringChecker() {
        val expectedWords = listOf("\"ok\"")
        val result = lexer.getWordsFromLine("\"ok\" ")
        Assertions.assertEquals(expectedWords, result)
    }

    @Test
    fun testStringCheckerWithFile() {
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/OneLineFile1.txt"))
    }
}

// TODO: arreglar bien el string literal, este test de arriba no anda perro
