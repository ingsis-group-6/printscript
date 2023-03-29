package common.ast.implementations.node

import common.token.TokenType

interface Node {
    fun getValue(): String
}

data class LeafNode(val type: TokenType, private val value: String) : Node {
    override fun getValue(): String {
        return value
    }
}

data class TreeNode(val headToken: String, val left: TreeNode? = null, val right: TreeNode? = null) : Node {
    override fun getValue(): String {
        return headToken
    }
}
