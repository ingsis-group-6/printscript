package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.ExpressionTreeCreator
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.ReadInputNode
import common.ast.implementations.node.TreeNode
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType
import java.util.*

class AssignationAST(private val tokens: List<Token>) : AST {

    private val identifierLeafNode: Node
    private val valueNode: Node

    init {
        val isValid = validateInputTokens(tokens)
        if (!isValid) throw InvalidTokenInputException("(Line ${this.tokens.first().row}) - There is a syntax error.")

        identifierLeafNode = LeafNode(TokenType.IDENTIFIER, tokens.first().value)

        val rhs = extractRHS(tokens)

        valueNode =
            if (rhs.size == 1) {
                LeafNode(
                    rhs.first().tokenType,
                    if (rhs.first().tokenType == TokenType.STRING_LITERAL) {
                        rhs.first().value.substring(1).dropLast(1)
                    } else {
                        rhs.first().value
                    }
                )
            } else if (isReadInput(rhs)) {
                ReadInputNode(rhs[2].value, rhs[2].tokenType)
            } else {
                ExpressionTreeCreator.createExpressionNode(rhs)
            }
    }

    //  5 + ( 3 * ( 2 + 1 ) )
    private fun isReadInput(tokens: List<Token>): Boolean {
        return tokens.size == 4 &&
            tokens.first().tokenType == TokenType.READ_INPUT &&
            validateReadInputMessage(tokens)
    }

    private fun validateReadInputMessage(tokens: List<Token>): Boolean {
        if (tokens[2].tokenType == TokenType.STRING_LITERAL || tokens[2].tokenType == TokenType.IDENTIFIER) return true
        throw java.lang.Exception("(Line ${tokens.first().row}) - Invalid message provided for ReadInput function.")
    }

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

    private fun extractRHS(tokens: List<Token>) = try {
        tokens.subList(2, tokens.size - 1).toList()
    } catch (e: Exception) {
        throw java.lang.Exception("(Line ${tokens.first().row}) - Invalid number of tokens in line.")
    }

    // TODO : renombrar
    private fun validateRightHandSide(rightHandSide: List<Token>): Boolean {
        val validTokenTypes = listOf(
            TokenType.OPERATOR,
            TokenType.OPEN_PARENTHESIS,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.IDENTIFIER,
            TokenType.NUMERIC_LITERAL,
            TokenType.STRING_LITERAL,
            TokenType.BOOLEAN_TRUE,
            TokenType.BOOLEAN_FALSE,
            TokenType.READ_INPUT
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

// TODO: modularizar
class ShuntingYard {

    fun shuntingYard(tokens: List<Token>): Node {
        val stack = Stack<Token>()
        val tokenQueue: Queue<Token> = LinkedList()

        for (token in tokens) {
            when {
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
        TokenType.IDENTIFIER,
        TokenType.BOOLEAN_TRUE,
        TokenType.BOOLEAN_FALSE
    )
}
