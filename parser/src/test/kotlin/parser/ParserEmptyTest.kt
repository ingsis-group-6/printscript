package parser

import common.ast.ASTType
import common.token.Token
import common.token.TokenType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.implementation.Parser

class ParserEmptyTest {

    val parser = Parser()

@Test
fun testEmpty() {
    val tokens = listOf<Token>(
        Token(0, TokenType.LET,"let",0,0),
        Token(1, TokenType.WHITESPACE," ",0,0),
        Token(2, TokenType.IDENTIFIER,"a",0,0),
        Token(3, TokenType.SEMICOLON,";",0,0),
    )
    val ast1 = parser.check(tokens)
    Assertions.assertTrue(ast1.second.size > 0)
}
}