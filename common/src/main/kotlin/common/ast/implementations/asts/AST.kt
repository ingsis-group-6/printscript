package common.ast.implementations.asts

import common.ast.implementations.node.Node
import common.token.Token

sealed interface AST {

    fun getChildren(): List<Node>
    fun getTokensInLine(): List<Token>
}
