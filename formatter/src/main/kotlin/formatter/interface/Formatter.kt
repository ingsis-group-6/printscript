package formatter.`interface`

import common.ast.AST

interface Formatter {
    fun format(ast: AST): String
}
