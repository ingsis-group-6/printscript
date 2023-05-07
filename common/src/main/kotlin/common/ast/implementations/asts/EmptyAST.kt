package common.ast.implementations.asts

import common.ast.implementations.node.Node
import common.token.Token

object EmptyAST : AST {
    override fun getChildren(): List<Node> =
        emptyList<Node>()

    override fun getTokensInLine(): List<Token> =
        emptyList<Token>()
}
