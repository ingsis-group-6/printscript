package formatter.`interface`

import common.ast.implementations.asts.AST

interface Formatter<T: AST> {
    fun format(ast: T): String
}
