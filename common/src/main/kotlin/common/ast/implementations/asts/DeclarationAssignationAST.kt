package common.ast.implementations.asts

import common.ast.implementations.node.Node
import common.exceptions.InvalidTokenInputException
import common.token.Token

class DeclarationAssignationAST(private val tokens: List<Token>) : AST {

    private val declarationAST: AST
    private val assignationAST: AST

    // LET ID : TYPE = (LITERAL / EXPRESSION / ID);

    init {
        if (tokens.size < 7) throw InvalidTokenInputException("(Line ${this.tokens.first().row}) - There is a syntax error.")
        val listForDeclaration = tokens.subList(0, 4).toMutableList()
        listForDeclaration.add(tokens.last())
        val listForAssignation =
            listOf(tokens[1]) + tokens.subList(4, tokens.size)
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
    fun getDeclarationAST(): AST {
        return this.declarationAST
    }

    fun getAssignationAST(): AST {
        return this.assignationAST
    }
}
