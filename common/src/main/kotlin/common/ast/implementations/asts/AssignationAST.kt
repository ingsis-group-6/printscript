package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.ExpressionTreeCreator
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
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
        valueNode = if (rhs.size == 1) LeafNode(rhs.first().tokenType, rhs.first().value) else ExpressionTreeCreator.createExpressionNode(rhs)
    }

    //  5 + ( 3 * ( 2 + 1 ) )

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

    fun getIdentifier(): String = this.identifierLeafNode.getValue()

    fun getRhsNode(): Node = this.valueNode
}

class ShuntingYard {

    fun shuntingYard(tokens: List<Token>): Node {
        val stack = Stack<Token>()
        val tokenQueue: Queue<Token> = LinkedList()

        for (token in tokens) {
            when {
                // TODO si es un string, deberia entrar tambien
                // token.toDoubleOrNull() != null -> valueQueue.add(token)
                isStringOrNumberValue(token) -> tokenQueue.add(token)
                isOperator(token) -> {
                    while (!stack.isEmpty() && hasHigherPrecedence(stack) && stack.peek().tokenType != TokenType.OPEN_PARENTHESIS) {
                        tokenQueue.add(stack.pop()!!)
                    }
                    stack.push(token)
                }
                token.tokenType == TokenType.OPEN_PARENTHESIS -> stack.push(token)
                token.tokenType == TokenType.CLOSE_PARENTHESIS -> {
                    while (stack.peek().tokenType != TokenType.OPEN_PARENTHESIS) {
                        tokenQueue.add(stack.pop()!!)
                    }
                    stack.pop()
                }
            }
        }

        while (!stack.isEmpty()) {
            tokenQueue.add(stack.pop()!!)
        }

        val treeStack = generateTreeFromQueue(tokenQueue)

        return treeStack.pop()
    }

    private fun hasHigherPrecedence(stack: Stack<Token>) = stack.peek().value in listOf("*", "/")

    private fun isOperator(token: Token) = token.tokenType == TokenType.OPERATOR

    private fun generateTreeFromQueue(valueQueue: Queue<Token>): Stack<TreeNode> {
        val treeStack = Stack<TreeNode>()

        for (token in valueQueue) {
            if (isStringOrNumberValue(token)) {
                treeStack.push(TreeNode(token.value, token.tokenType))
            } else {
                val right = treeStack.pop()
                val left = treeStack.pop()
                treeStack.push(TreeNode(token.value, token.tokenType, left, right))
            }
        }
        return treeStack
    }

    private fun isStringOrNumberValue(token: Token) = token.tokenType in setOf(
        TokenType.STRING_LITERAL,
        TokenType.NUMERIC_LITERAL,
        TokenType.IDENTIFIER
    )
}
