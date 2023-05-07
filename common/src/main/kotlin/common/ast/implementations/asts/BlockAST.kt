package common.ast.implementations.asts

import common.ast.implementations.node.Node
import common.token.Token

class BlockAST(private val containedASTs: List<AST>) : AST {

    fun isEmpty(): Boolean = containedASTs.isEmpty()

    override fun getChildren(): List<Node> {
        TODO("Not yet implemented")
    }

    override fun getTokensInLine(): List<Token> {
        return listOf()
    }

    fun getContainedASTs(): List<AST> {
        return this.containedASTs
    }
}
