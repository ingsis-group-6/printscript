package common.ast.implementations.asts

import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.token.Token

class ConditionalAST(conditionToken: Token, ifASTs: List<AST>, elseASTs: List<AST>) : AST {
    private val condition = LeafNode(conditionToken.tokenType, conditionToken.value)
    private val currentLine = conditionToken.row
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
        return listOf()
    }

    fun getCondition(): Node {
        return condition
    }

    fun getCurrentLine() = this.currentLine
}
