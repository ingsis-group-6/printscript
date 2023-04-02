package common.ast.implementations

import common.ast.implementations.asts.ShuntingYard
import common.ast.implementations.node.Node
import common.exceptions.InvalidExpressionException
import common.token.Token

object ExpressionTreeCreator {
    // a + 4 + "hello"
    // "a", "+", "4", "+", "\"hello\""
    fun createExpressionNode(rhs: List<Token>): Node {
        val validExpression = isValidMathExpression(rhs)
        if (!validExpression) throw InvalidExpressionException("The expression on the right-hand side is invalid")
        val shuntingYard = ShuntingYard()
        return shuntingYard.shuntingYard(rhs)
    }

    private fun isValidMathExpression(tokens: List<Token>): Boolean {
        val tokenValues = tokens.map { token: Token -> token.value }

        val stack = mutableListOf<String>()
        var prevToken = ""

        for (i in tokenValues.indices) {
            val token = tokenValues[i]
            when {
                token == "(" -> {
                    if (prevToken.isNotEmpty() && (prevToken.matches(Regex("[0-9.]+")) || prevToken == ")")) {
                        return false // opening parenthesis cannot follow a number, decimal point, or closing parenthesis
                    }
                    stack.add(token)
                    prevToken = ""
                }
                token == ")" -> {
                    if (stack.isNotEmpty() && stack.last() == "(" && prevToken != "(" && prevToken != "") {
                        stack.removeAt(stack.lastIndex)
                        prevToken = ")"
                    } else {
                        return false
                    }
                }

                isOperator(token) -> {
                    if (i == 0 || i == tokenValues.lastIndex || prevToken.matches(Regex("[+\\-*/]")) || prevToken == "(") {
                        return false // operator cannot be at beginning or end of expression, or follow another operator or opening parenthesis
                    } else {
                        prevToken = token
                    }
                }
                isStringOrNumericValue(token) -> {
                    if (prevToken.isNotEmpty() && (prevToken.matches(Regex("[a-zA-Z][a-zA-Z0-9]*")) || prevToken.matches(Regex("[0-9.]+")))) {
                        return false // variables and numbers cannot be consecutive
                    }
                    prevToken = token
                }
                else -> return false // invalid character
            }
        }

        return stack.isEmpty() && prevToken != "" && prevToken != "(" && prevToken != "." // last token must be a number, variable, or closing parenthesis
    }

    private fun isOperator(token: String) = token in setOf("+", "-", "*", "/")

    private fun isStringOrNumericValue(token: String) = token.matches(Regex("\"[^\"]*\"|'[^']*'|[a-zA-Z][a-zA-Z0-9]*|[0-9.]+"))
}
