package parser

import common.ast.implementations.node.LeafNode
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import parser.exceptions.EmptyTokenInputException
import parser.implementation.Parser

class ParserDeclarationASTCreationTest {
    private val parser = Parser()

    @Test
    fun testEmptyInput() {
        val inputList = emptyList<Token>()
        assertThrows(EmptyTokenInputException::class.java) {
            val resultAST = parser.parse(inputList)
        }
    }

    @Test
    fun testDeclarationASTCreationWithNumberType() {
        val inputTokens = listOf(
            Token(0, TokenType.LET, "let", 0),
            Token(1, TokenType.IDENTIFIER, "myVar", 0),
            Token(2, TokenType.COLON, ":", 0),
            Token(3, TokenType.TYPE, "number", 0),
            Token(4, TokenType.SEMICOLON, ";", 0)
        )
        val resultAST = parser.parse(inputTokens)
        val children = resultAST.getChildren()
        assertTrue(children.contains(LeafNode(TokenType.LET, "let")))
        assertTrue(children.contains(LeafNode(TokenType.IDENTIFIER, "myVar")))
        assertTrue(children.contains(LeafNode(TokenType.TYPE, "number")))
        assertEquals(3, children.size)
    }

    @Test
    fun testDeclarationASTCreationWithStringType() {
        val inputTokens = listOf(
            Token(0, TokenType.LET, "let", 0),
            Token(1, TokenType.IDENTIFIER, "myVar", 0),
            Token(2, TokenType.COLON, ":", 0),
            Token(3, TokenType.TYPE, "string", 0),
            Token(4, TokenType.SEMICOLON, ";", 0)
        )
        val resultAST = parser.parse(inputTokens)
        val children = resultAST.getChildren()
        assertTrue(children.contains(LeafNode(TokenType.LET, "let")))
        assertTrue(children.contains(LeafNode(TokenType.IDENTIFIER, "myVar")))
        assertTrue(children.contains(LeafNode(TokenType.TYPE, "string")))
        assertEquals(3, children.size)
    }

    companion object {

        @JvmStatic
        fun invalidTokenLists() = listOf(
            Arguments.of(
                listOf(
                    Token(0, TokenType.LET, "let", 0),
                    Token(1, TokenType.IDENTIFIER, "myVar", 0),
                    Token(2, TokenType.TYPE, "string", 0),
                    Token(3, TokenType.SEMICOLON, ";", 0)
                )
            ),
            Arguments.of(
                listOf(
                    Token(0, TokenType.LET, "let", 0),
                    Token(1, TokenType.LET, "let", 0),
                    Token(2, TokenType.COLON, ":", 0),
                    Token(3, TokenType.TYPE, "string", 0),
                    Token(4, TokenType.SEMICOLON, ";", 0)
                )
            ),
            Arguments.of(
                listOf(
                    Token(0, TokenType.LET, "let", 0),
                    Token(1, TokenType.IDENTIFIER, "myVar", 0),
                    Token(3, TokenType.SEMICOLON, ";", 0)
                )
            )
        )
    }

    @ParameterizedTest
    @MethodSource("invalidTokenLists")
    fun testMyFunctionWithEmptyList(inputTokens: List<Token>) {
        assertThrows(InvalidTokenInputException::class.java) { parser.parse(inputTokens) }
    }
}
