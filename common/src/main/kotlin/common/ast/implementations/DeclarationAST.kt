package common.ast.implementations

import common.ast.AST
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType

class DeclarationAST(val tokens: List<Token>): AST {

    val letNode: Node<String>
    val identifierNode: Node<String>
    val typeNode: Node<String>


    init {
        val tokensWithoutWhitespace = tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }
        val isValid = validateInputTokens(tokensWithoutWhitespace)
        if(!isValid) throw InvalidTokenInputException("There is a syntax error in line ${tokens.first().row}")

        letNode = Node(TokenType.LET, "let")

        val identifierToken = tokens.find { token: Token -> token.tokenType == TokenType.IDENTIFIER }
        identifierNode = Node(TokenType.IDENTIFIER, identifierToken?.value ?: "")

        val typeToken = tokens.find { token: Token -> token.tokenType == TokenType.TYPE }
        typeNode = Node(TokenType.TYPE, typeToken?.value ?: "")
    }


    // let myNum : number;

    private fun validateInputTokens(tokens: List<Token>): Boolean {
        val templateList = listOf<TokenType>(
            TokenType.LET,
            TokenType.IDENTIFIER,
            TokenType.COLON,
            TokenType.TYPE,
            TokenType.SEMICOLON
        )
        return tokens.size == 5 && tokens.map { token: Token -> token.tokenType }.equals(templateList)

    }

    override fun getChildren(): List<AST> {
        TODO("Not yet implemented")
    }

    override fun getTokensInLine(): List<Token> {
        TODO("Not yet implemented")
    }
}


data class Node<T>(val type: TokenType, val value: T)


interface OperationTree {
    fun <T> calculate(): T
}

class BinaryOperationTree<T> (val left: OperationTree, val operation: (T, T) -> T, val right: OperationTree): OperationTree {
    override fun <T> calculate(): T {
        TODO("Not yet implemented")
    }

}
// () podria ser Unary

