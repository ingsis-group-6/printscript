package common.ast.implementations.asts

import common.ast.AST
import common.ast.implementations.node.Node
import common.token.Token

object EndOfFileAST : AST {
    override fun getChildren(): List<Node> {
        return listOf()
    }

    override fun getTokensInLine(): List<Token> {
        return listOf()
    }
}
