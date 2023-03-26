package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType

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
        for (token in rhs) {
        }

        // x

        return LeafNode(TokenType.TYPE, "sa")
    }

    // num = 4;

    // ID = LITERAL / EXPRESSION ;
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
