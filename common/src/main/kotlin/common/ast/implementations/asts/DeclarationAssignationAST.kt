package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType

class DeclarationAssignationAST(private val tokens: List<Token>) : AST {

    private val declarationAST: AST
    private val assignationAST: AST

    // LET ID : TYPE = (LITERAL / EXPRESSION / ID);

    init {
        val tokensWithoutWhitespace = tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }
        if (tokensWithoutWhitespace.size < 7) throw InvalidTokenInputException("There is a syntax error in line ${tokens.first().row}")
        val listForDeclaration = tokensWithoutWhitespace.subList(0, 4).toMutableList()
        listForDeclaration.add(tokensWithoutWhitespace.last())
        val listForAssignation =
            listOf(tokensWithoutWhitespace[1]) + tokensWithoutWhitespace.subList(5, tokensWithoutWhitespace.size)
                .toList()

        declarationAST = DeclarationAST(listForDeclaration)
        assignationAST = AssignationAST(listForAssignation)
    }

    override fun getChildren(): List<Node> {
        return declarationAST.getChildren() + assignationAST.getChildren()
    }

    override fun getTokensInLine(): List<Token> {
        return this.tokens
    }
}
