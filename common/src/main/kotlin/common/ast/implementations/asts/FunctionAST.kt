package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.ExpressionTreeCreator
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType
import java.lang.Exception

class FunctionAST(private val tokens: List<Token>) : AST {

    // PRINT ( ID / LITERAL );

    private val functionNode: Node
    private val paramNode: Node

    init {
        val tokensWithoutWhitespace = tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }
        val isValid = validateBody(tokensWithoutWhitespace)
        if (!isValid) throw InvalidTokenInputException("(Line ${tokens.first().row}) - There is a syntax error.")
        functionNode = LeafNode(tokensWithoutWhitespace.first().tokenType, tokensWithoutWhitespace.first().value)
        val param = extractParam(tokensWithoutWhitespace)
        if (!paramIsValid(param)) throw Exception("Invalid Parameter syntax")
        paramNode = if (param.size == 1) LeafNode(tokensWithoutWhitespace[2].tokenType, tokensWithoutWhitespace[2].value) else ExpressionTreeCreator.createExpressionNode(param)
    }

    // print ( xx x x xx xx x x );
    private fun extractParam(tokens: List<Token>): List<Token> = tokens.subList(2, tokens.lastIndex - 1)

    private fun paramIsValid(param: List<Token>): Boolean {
        val validTokenTypes = listOf(
            TokenType.OPERATOR,
            TokenType.OPEN_PARENTHESIS,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.IDENTIFIER,
            TokenType.NUMERIC_LITERAL,
            TokenType.STRING_LITERAL
        )

        return param.all { token: Token -> validTokenTypes.contains(token.tokenType) }
    }

    // print ( x ) ;
    private fun validateBody(tokens: List<Token>): Boolean {
        val validBody =
            tokens.size >= 5 &&
                (tokens.first().tokenType == TokenType.PRINTLN || tokens.first().tokenType == TokenType.FUNCTION) &&
                tokens[1].tokenType == TokenType.OPEN_PARENTHESIS &&
                tokens[tokens.lastIndex - 1].tokenType == TokenType.CLOSE_PARENTHESIS &&
                tokens.last().tokenType == TokenType.SEMICOLON

        val param = extractParam(tokens)
        return validBody && paramIsValid(param)
    }

    override fun getChildren(): List<Node> {
        return listOf(functionNode, paramNode)
    }

    override fun getTokensInLine(): List<Token> {
        return tokens
    }

    // no me gusta nada esto
    fun getParamNode(): Node {
        return this.paramNode
    }
}
