package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.exceptions.InvalidExpressionException
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType
import java.util.*

class AssignationAST(private val tokens: List<Token>) : AST {

    private val identifierLeafNode: Node
    private val valueNode: Node

    init {
        val tokensWithoutWhitespace = tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }
        val isValid = validateInputTokens(tokensWithoutWhitespace)
        if (!isValid) throw InvalidTokenInputException("There is a syntax error in line ${tokens.first().row}")

        identifierLeafNode = LeafNode(TokenType.IDENTIFIER, tokensWithoutWhitespace.first().value)

        val rhs = extractRHS(tokensWithoutWhitespace)
        valueNode = if (rhs.size == 1) LeafNode(rhs.first().tokenType, rhs.first().value) else createExpressionNode(rhs)
    }

    //  5 + ( 3 * ( 2 + 1 ) )
    private fun createExpressionNode(rhs: List<Token>): Node {
        val rhsValuesList = rhs.map { token: Token -> token.value }
        val validExpression = isValidMathExpression(rhsValuesList)
        if (!validExpression) throw InvalidExpressionException("The expression on the right-hand side is invalid")
        val shuntingYard = ShuntingYard()
        return shuntingYard.shuntingYard(rhsValuesList)
    }

    private fun isValidMathExpression(tokens: List<String>): Boolean {
        val stack = mutableListOf<String>()
        var prevToken = ""

        for (i in tokens.indices) {
            val token = tokens[i]
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
                    if (i == 0 || i == tokens.lastIndex || prevToken.matches(Regex("[+\\-*/]")) || prevToken == "(") {
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

    private fun isStringOrNumericValue(token: String) = token.matches(Regex("[a-zA-Z][a-zA-Z0-9]*|[0-9.]+"))

    private fun validateInputTokens(tokens: List<Token>): Boolean {
        val validBody =
            tokens.size >= 4 &&
                tokens.first().tokenType == TokenType.IDENTIFIER &&
                tokens[1].tokenType == TokenType.ASSIGNATION &&
                tokens.last().tokenType == TokenType.SEMICOLON

        val rightHandSide = extractRHS(tokens)
        val rhsIsValid = validateRightHandSide(rightHandSide)

        return validBody && rhsIsValid
    }

    private fun extractRHS(tokens: List<Token>) = tokens.subList(2, tokens.size - 1).toList()

    private fun validateRightHandSide(rightHandSide: List<Token>): Boolean {
        val validTokenTypes = listOf(
            TokenType.OPERATOR,
            TokenType.OPEN_PARENTHESIS,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.IDENTIFIER,
            TokenType.NUMERIC_LITERAL,
            TokenType.STRING_LITERAL
        )

        return rightHandSide.all { token: Token -> validTokenTypes.contains(token.tokenType) }
    }

    override fun getChildren(): List<Node> {
        return listOf(this.identifierLeafNode, this.valueNode)
    }

    override fun getTokensInLine(): List<Token> {
        return this.tokens
    }
}

class ShuntingYard {

    fun shuntingYard(tokens: List<String>): Node {
        val stack = Stack<String>()
        val valueQueue: Queue<String> = LinkedList()

        for (token in tokens) {
            when {
                // TODO si es un string, deberia entrar tambien
                // token.toDoubleOrNull() != null -> valueQueue.add(token)
                isStringOrNumberValue(token) -> valueQueue.add(token)
                isOperator(token) -> {
                    while (!stack.isEmpty() && hasHigherPrecedence(stack) && stack.peek() != "(") {
                        valueQueue.add(stack.pop()!!)
                    }
                    stack.push(token)
                }
                token == "(" -> stack.push(token)
                token == ")" -> {
                    while (stack.peek() != "(") {
                        valueQueue.add(stack.pop()!!)
                    }
                    stack.pop()
                }
            }
        }

        while (!stack.isEmpty()) {
            valueQueue.add(stack.pop()!!)
        }

        val treeStack = generateTreeFromQueue(valueQueue)

        return treeStack.pop()
    }

    private fun hasHigherPrecedence(stack: Stack<String>) = stack.peek() in listOf("*", "/")

    private fun isOperator(token: String) = token in listOf("+", "-", "*", "/")

    private fun generateTreeFromQueue(valueQueue: Queue<String>): Stack<TreeNode> {
        val treeStack = Stack<TreeNode>()

        for (element in valueQueue) {
            if (isStringOrNumberValue(element)) {
                treeStack.push(TreeNode(element))
            } else {
                val right = treeStack.pop()
                val left = treeStack.pop()
                treeStack.push(TreeNode(element, left, right))
            }
        }
        return treeStack
    }

    private fun isStringOrNumberValue(token: String) = token.matches(Regex("[a-zA-Z][a-zA-Z0-9]*|[0-9.]+"))
}
