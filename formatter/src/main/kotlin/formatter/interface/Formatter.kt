package formatter.`interface`

import common.ast.implementations.asts.AST

interface Formatter {
    fun format(ast: AST): String
}
