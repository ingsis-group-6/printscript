package parser

import common.ast.implementations.asts.ShuntingYard
import common.ast.implementations.node.TreeNode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShuntingYardTest {

    private val sy = ShuntingYard()

    @Test
    fun testSimpleCase() {
        val input = listOf(
            "(",
            "4",
            "+",
            "(",
            "3",
            "/",
            "2",
            ")",
            ")",
            "+",
            "3"
        )
//        val input = listOf(
//            "1",
//            "+",
//            "1",
//        );
        val tree = sy.shuntingYard(input)
        print(tree)
    }

    @Test
    fun testSimpleAddition() {
        val tokens = listOf("1", "+", "2")
        val expectedTree = TreeNode("+", TreeNode("1"), TreeNode("2"))
        assertEquals(expectedTree, sy.shuntingYard(tokens))
    }

    @Test
    fun testSimpleMultiplication() {
        val tokens = listOf("3", "*", "4")
        val expectedTree = TreeNode("*", TreeNode("3"), TreeNode("4"))
        assertEquals(expectedTree, sy.shuntingYard(tokens))
    }

    @Test
    fun testExpressionWithParentheses() {
        val tokens = listOf("(", "1", "+", "2", ")", "*", "3")
        val expectedTree = TreeNode("*", TreeNode("+", TreeNode("1"), TreeNode("2")), TreeNode("3"))
        assertEquals(expectedTree, sy.shuntingYard(tokens))
    }

    @Test
    fun testExpressionWithMultipleOperators() {
        val tokens = listOf("3", "*", "(", "1", "+", "2", ")", "-", "5", "/", "2")
        val expectedTree = TreeNode(
            "-",
            TreeNode("*", TreeNode("3"), TreeNode("+", TreeNode("1"), TreeNode("2"))),
            TreeNode("/", TreeNode("5"), TreeNode("2"))
        )
        assertEquals(expectedTree, sy.shuntingYard(tokens))
    }

    // TODO: Fix this case
    /*@Test
    fun testInvalidExpression() {
        val tokens = listOf("(", "1", "+", "2", ")", "*", "*", "3")

        assertNull(sy.shuntingYard(tokens))
    }*/
}
