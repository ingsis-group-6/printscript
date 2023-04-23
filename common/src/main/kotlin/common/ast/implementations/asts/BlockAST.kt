package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.Node
import common.token.Token

class BlockAST(private val containedASTs: List<AST>) : AST {

    fun isEmpty(): Boolean = containedASTs.isEmpty()

    override fun getChildren(): List<Node> {
        TODO("Not yet implemented")
    }

    override fun getTokensInLine(): List<Token> {
        TODO("Not yet implemented")
    }
}