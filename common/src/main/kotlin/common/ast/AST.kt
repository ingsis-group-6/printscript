package common.ast

import common.ast.implementations.node.Node
import common.token.Token

interface AST {

    fun getChildren(): List<Node>
    fun getTokensInLine(): List<Token>
}
