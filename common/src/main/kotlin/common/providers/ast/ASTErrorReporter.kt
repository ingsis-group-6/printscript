package common.providers.ast

import common.ast.AST
import common.token.Token
import java.util.*

interface ASTErrorReporter {

    fun checkASTCreation(): Optional<Pair<AST, List<String>>>
}
