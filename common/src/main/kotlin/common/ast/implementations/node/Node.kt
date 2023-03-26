package common.ast.implementations.node

import common.token.TokenType


interface Node {
    fun getValue(): String
}


class LeafNode(val type: TokenType, private val value: String) : Node {
    override fun getValue(): String {
        return value
    }

}






class ExpressionNode(private val left: Node, private val operation: String, private val right: Node) : Node {
    fun getLeft(): Node {
        return this.left
    }

    fun getRight(): Node {
        return this.right
    }

    override fun getValue(): String {
        return operation
    }
}