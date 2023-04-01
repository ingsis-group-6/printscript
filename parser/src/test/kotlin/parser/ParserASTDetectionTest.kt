package parser

import common.ast.ASTType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.implementation.Parser

class ParserASTDetectionTest {
    val parser = Parser()

    @Test
    fun testCorrectDelaractionASTCreation() {
        val tokens = TokenTypeListGenerator.generateTokenTypes(
            "LET",
            "IDENTIFIER",
            "COLON",
            "TYPE",
            "SEMICOLON"
        )
        val ast1 = parser.detectASTType(tokens)
        Assertions.assertEquals(ASTType.DECLARATION, ast1)
    }

    @Test
    fun testCorrectFunctionASTCreation() {
        val tokens = TokenTypeListGenerator.generateTokenTypes(
            "PRINTLN",
            "OPEN_PARENTHESIS",
            "STRING_LITERAL",
            "CLOSE_PARENTHESIS",
            "SEMICOLON"
        )
        val ast1 = parser.detectASTType(tokens)
        Assertions.assertEquals(ASTType.FUNCTION, ast1)
    }

    @Test
    fun testCorrectAssignartonASTCreation() {
        val tokens = TokenTypeListGenerator.generateTokenTypes(
            "IDENTIFIER",
            "ASSIGNATION",
            "STRING_LITERAL",
            "SEMICOLON"
        )
        val ast1 = parser.detectASTType(tokens)
        Assertions.assertEquals(ASTType.ASSIGNATION, ast1)
    }

    @Test
    fun testCorrectDeclarationAssignationASTCreation() {
        val tokens = TokenTypeListGenerator.generateTokenTypes(
            "LET",
            "IDENTIFIER",
            "COLON",
            "TYPE",
            "ASSIGNATION",
            "STRING_LITERAL",
            "SEMICOLON"
        )

        val ast1 = parser.detectASTType(tokens)

        Assertions.assertEquals(ASTType.DECLARATION_ASSIGNATION, ast1)
        val tokens2 = TokenTypeListGenerator.generateTokenTypes(
            "LET",
            "IDENTIFIER",
            "COLON",
            "TYPE",
            "ASSIGNATION",
            "NUMERIC_LITERAL",
            "SEMICOLON"
        )
        val ast2 = parser.detectASTType(tokens2)
        Assertions.assertEquals(ASTType.DECLARATION_ASSIGNATION, ast2)
    }
}
