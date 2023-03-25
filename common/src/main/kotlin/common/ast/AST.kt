package common.ast

import common.token.Token

interface AST {

    fun getChildren(): List<AST>
    fun getTokensInLine(): List<Token>
}
