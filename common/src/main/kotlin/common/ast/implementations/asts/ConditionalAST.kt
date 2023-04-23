package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.Node
import common.token.Token

class ConditionalAST(private val condition: Token, ifASTs: List<AST>, elseASTs: List<AST>) : AST {
    private val ifBlock = BlockAST(ifASTs)
    private val elseBlock = BlockAST(elseASTs)

    fun getIfBlock(): BlockAST {
        return ifBlock
    }

    fun getElseBlock(): AST {
        return elseBlock
    }

    override fun getChildren(): List<Node> {
        TODO("Not yet implemented")
    }

    override fun getTokensInLine(): List<Token> {
        TODO("Not yet implemented")
    }
}
