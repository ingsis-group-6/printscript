package common.providers.ast

import common.ast.AST
import common.token.Token

interface ASTErrorReporter {

    fun checkASTCreation(tokens: List<Token>): Pair<AST, List<String>>
}
