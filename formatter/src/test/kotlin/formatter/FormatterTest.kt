package formatter

import common.token.Token
import common.token.TokenType
import formatter.implementations.Formatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FormatterTest {

    private var formatter: Formatter = Formatter("../formatter_config.json")

    @Test
    fun test() {
        val input: List<Token> = listOf(
            Token(0, TokenType.DECLARATOR, "let", 0, 0),
            Token(1, TokenType.IDENTIFIER, "a", 0, 0),
            Token(2, TokenType.COLON, ":", 0, 0),
            Token(3, TokenType.TYPE, "number", 0, 0),
            Token(4, TokenType.ASSIGNATION, "=", 0, 0),
            Token(5, TokenType.NUMERIC_LITERAL, "4", 0, 0),
            Token(6, TokenType.OPERATOR, "/", 0, 0),
            Token(7, TokenType.NUMERIC_LITERAL, "2", 0, 0),
            Token(8, TokenType.SEMICOLON, ";", 0, 0)
        )

        assertEquals("let a: number = 4 / 2;\n", formatter.formatDeclarationAssignationAST(input))
    }
}
