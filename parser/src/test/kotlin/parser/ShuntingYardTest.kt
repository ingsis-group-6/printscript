package parser

import common.ast.implementations.asts.ShuntingYard
import common.ast.implementations.node.TreeNode
import common.token.Token
import common.token.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShuntingYardTest {

    private val sy = ShuntingYard()

//    @Test
//    fun testSimpleCase() {
//        val input = listOf(
//            "(",
//            "4",
//            "+",
//            "(",
//            "3",
//            "/",
//            "2",
//            ")",
//            ")",
//            "+",
//            "3"
//        )
// //        val input = listOf(
// //            "1",
// //            "+",
// //            "1",
// //        );
//        val tree = sy.shuntingYard(input)
//        print(tree)
//    }
//
//    @Test
//    fun testSimpleAddition() {
//        val tokens = listOf("1", "+", "2")
//        val expectedTree = TreeNode("+", TreeNode("1"), TreeNode("2"))
//        assertEquals(expectedTree, sy.shuntingYard(tokens))
//    }
//
//    @Test
//    fun testSimpleMultiplication() {
//        val tokens = listOf("myVar", "*", "4")
//        val expectedTree = TreeNode("*", TreeNode("myVar"), TreeNode("4"))
//        assertEquals(expectedTree, sy.shuntingYard(tokens))
//    }
//
//    @Test
//    fun testExpressionWithParentheses() {
//        val tokens = listOf("(", "1", "+", "2", ")", "*", "3")
//        val expectedTree = TreeNode("*", TreeNode("+", TreeNode("1"), TreeNode("2")), TreeNode("3"))
//        assertEquals(expectedTree, sy.shuntingYard(tokens))
//    }
//
//    @Test
//    fun testExpressionWithMultipleOperators() {
//        val tokens = listOf("3", "*", "(", "1", "+", "2", ")", "-", "5", "/", "2")
//        val expectedTree = TreeNode(
//            "-",
//            TreeNode("*", TreeNode("3"), TreeNode("+", TreeNode("1"), TreeNode("2"))),
//            TreeNode("/", TreeNode("5"), TreeNode("2"))
//        )
//        assertEquals(expectedTree, sy.shuntingYard(tokens))
//    }
//
//    // TODO: Fix this case
//    /*@Test
//    fun testInvalidExpression() {
//        val tokens = listOf("(", "1", "+", "2", ")", "*", "*", "3")
//
//        assertNull(sy.shuntingYard(tokens))
//    }*/

    @Test
    fun testSimpleExpression() {
        val input = listOf(
            Token(1, TokenType.NUMERIC_LITERAL, "2", 1, 0),
            Token(2, TokenType.OPERATOR, "+", 1, 0),
            Token(3, TokenType.NUMERIC_LITERAL, "3", 1, 0)
        )
        val expected = TreeNode("+", TokenType.OPERATOR, TreeNode("2", TokenType.NUMERIC_LITERAL), TreeNode("3", TokenType.NUMERIC_LITERAL))
        assertEquals(expected, ShuntingYard().shuntingYard(input))
    }

    @Test
    fun testExpressionWithParentheses() {
        val input = listOf(
            Token(1, TokenType.OPEN_PARENTHESIS, "(", 1, 0),
            Token(2, TokenType.NUMERIC_LITERAL, "2", 1, 0),
            Token(3, TokenType.OPERATOR, "+", 1, 0),
            Token(4, TokenType.NUMERIC_LITERAL, "3", 1, 0),
            Token(5, TokenType.CLOSE_PARENTHESIS, ")", 1, 0),
            Token(6, TokenType.OPERATOR, "*", 1, 0),
            Token(7, TokenType.NUMERIC_LITERAL, "4", 1, 0)
        )
        val expected = TreeNode("*", TokenType.OPERATOR, TreeNode("+", TokenType.OPERATOR, TreeNode("2", TokenType.NUMERIC_LITERAL), TreeNode("3", TokenType.NUMERIC_LITERAL)), TreeNode("4", TokenType.NUMERIC_LITERAL))
        assertEquals(expected, ShuntingYard().shuntingYard(input))
    }

    @Test
    fun testExpressionWithVariables() {
        val input = listOf(
            Token(1, TokenType.IDENTIFIER, "a", 1, 0),
            Token(2, TokenType.OPERATOR, "+", 1, 0),
            Token(3, TokenType.IDENTIFIER, "b", 1, 0),
            Token(4, TokenType.OPERATOR, "*", 1, 0),
            Token(5, TokenType.IDENTIFIER, "c", 1, 0)
        )
        val expected = TreeNode("+", TokenType.OPERATOR, TreeNode("a", TokenType.IDENTIFIER), TreeNode("*", TokenType.OPERATOR, TreeNode("b", TokenType.IDENTIFIER), TreeNode("c", TokenType.IDENTIFIER)))
        assertEquals(expected, ShuntingYard().shuntingYard(input))
    }

    @Test
    fun testExpressionWithStrings() {
        val input = listOf(
            Token(1, TokenType.STRING_LITERAL, "Hello", 1, 0),
            Token(2, TokenType.OPERATOR, "+", 1, 0),
            Token(3, TokenType.STRING_LITERAL, "world", 1, 0)
        )
        val expected = TreeNode("+", TokenType.OPERATOR, TreeNode("Hello", TokenType.STRING_LITERAL), TreeNode("world", TokenType.STRING_LITERAL))
        assertEquals(expected, ShuntingYard().shuntingYard(input))
    }
}
