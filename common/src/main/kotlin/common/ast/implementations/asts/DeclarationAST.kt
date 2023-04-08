package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType

class DeclarationAST(private val tokens: List<Token>) : AST {

    private val letLeafNode: Node
    private val identifierLeafNode: Node
    private val typeLeafNode: Node

    init {
        val tokensWithoutWhitespace = tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }
        val isValid = validateInputTokens(tokensWithoutWhitespace)
        if (!isValid) throw InvalidTokenInputException("(Line ${tokens.first().row}) - There is a syntax error.")

        letLeafNode = LeafNode(TokenType.LET, "let")

        val identifierToken = tokens.find { token: Token -> token.tokenType == TokenType.IDENTIFIER }
        identifierLeafNode = LeafNode(TokenType.IDENTIFIER, identifierToken?.value ?: "")

        val typeToken = tokens.find { token: Token -> token.tokenType == TokenType.TYPE }
        typeLeafNode = LeafNode(TokenType.TYPE, typeToken?.value ?: "")
    }

    private fun validateInputTokens(tokens: List<Token>): Boolean {
        val templateList = listOf(
            TokenType.LET,
            TokenType.IDENTIFIER,
            TokenType.COLON,
            TokenType.TYPE,
            TokenType.SEMICOLON
        )
        return tokens.size == 5 && tokens.map { token: Token -> token.tokenType } == templateList
    }

    override fun getChildren(): List<Node> {
        return listOf(letLeafNode, identifierLeafNode, typeLeafNode)
    }

    override fun getTokensInLine(): List<Token> {
        return this.tokens
    }

    fun getIdentifier(): String {
        return identifierLeafNode.getValue()
    }

    fun getType(): String {
        return typeLeafNode.getValue()
    }
}
