package common.providers.ast

import common.ast.implementations.asts.AST
import java.util.*

interface ASTErrorReporter {

    fun checkASTCreation(): Optional<Pair<AST, List<String>>>
}
