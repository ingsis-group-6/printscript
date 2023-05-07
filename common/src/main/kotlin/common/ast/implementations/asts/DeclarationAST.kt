package common.ast.implementations.asts

import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType

class DeclarationAST(private val tokens: List<Token>) : AST {

    private val declaratorLeafNode: Node
    private val identifierLeafNode: Node
    private val typeLeafNode: Node

    init {
        val isValid = validateInputTokens(tokens)
        if (!isValid) throw InvalidTokenInputException("(Line ${this.tokens.first().row}) - There is a syntax error.")

        declaratorLeafNode = LeafNode(TokenType.DECLARATOR, this.tokens.find { token: Token -> token.tokenType == TokenType.DECLARATOR }!!.value)

        val identifierToken = this.tokens.find { token: Token -> token.tokenType == TokenType.IDENTIFIER }
        identifierLeafNode = LeafNode(TokenType.IDENTIFIER, identifierToken?.value ?: "")

        val typeToken = this.tokens.find { token: Token -> token.tokenType == TokenType.TYPE }
        typeLeafNode = LeafNode(TokenType.TYPE, typeToken?.value ?: "")
    }

    private fun validateInputTokens(tokens: List<Token>): Boolean {
        val templateList = listOf(
            TokenType.DECLARATOR,
            TokenType.IDENTIFIER,
            TokenType.COLON,
            TokenType.TYPE,
            TokenType.SEMICOLON
        )
        return tokens.size == 5 && tokens.map { token: Token -> token.tokenType } == templateList
    }

    override fun getChildren(): List<Node> {
        return listOf(declaratorLeafNode, identifierLeafNode, typeLeafNode)
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

    fun getDeclarator(): String {
        return declaratorLeafNode.getValue()
    }
}
