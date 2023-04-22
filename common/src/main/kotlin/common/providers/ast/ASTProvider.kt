package common.providers.ast

import common.ast.AST
import java.util.Optional

interface ASTProvider {
    fun getAST(): Optional<AST>
}
