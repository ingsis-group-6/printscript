package common.providers.ast

import common.ast.implementations.asts.AST
import java.util.Optional

interface ASTProvider {
    fun getAST(): Optional<AST>
}
