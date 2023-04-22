package common.providers.ast

import common.ast.AST
import java.util.*

interface ASTErrorReporter {

    fun checkASTCreation(): Optional<Pair<AST, List<String>>>
}
