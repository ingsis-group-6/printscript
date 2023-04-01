package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType

class FunctionAST(private val tokens: List<Token>) : AST {

    // PRINT ( ID / LITERAL );

    private val functionNode: Node
    private val paramNode: Node

    init {
        val tokensWithoutWhitespace = tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }
        val isValid = validateBody(tokensWithoutWhitespace)
        if (!isValid) throw InvalidTokenInputException("There is a syntax error in line ${tokens.first().row}")
        functionNode = LeafNode(tokensWithoutWhitespace.first().tokenType, tokensWithoutWhitespace.first().value)
        paramNode = LeafNode(tokensWithoutWhitespace[2].tokenType, tokensWithoutWhitespace[2].value)
    }

    private fun validateBody(tokensWithoutWhitespace: List<Token>): Boolean {
        val templateList1 = listOf(
            TokenType.PRINTLN,
            TokenType.OPEN_PARENTHESIS,
            TokenType.IDENTIFIER,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.SEMICOLON
        )
        val templateList2 = listOf(
            TokenType.PRINTLN,
            TokenType.OPEN_PARENTHESIS,
            TokenType.STRING_LITERAL,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.SEMICOLON
        )
        val templateList3 = listOf(
            TokenType.PRINTLN,
            TokenType.OPEN_PARENTHESIS,
            TokenType.NUMERIC_LITERAL,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.SEMICOLON
        )
        return tokensWithoutWhitespace == templateList1 || tokensWithoutWhitespace == templateList2 || tokensWithoutWhitespace == templateList3
    }

    override fun getChildren(): List<Node> {
        return listOf(functionNode, paramNode)
    }

    override fun getTokensInLine(): List<Token> {
        return tokens
    }
}
