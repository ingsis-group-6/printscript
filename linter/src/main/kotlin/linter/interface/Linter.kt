package linter.`interface`

import common.ast.AST
import common.io.Outputter

interface Linter {
    fun lint(ast: Pair<AST, List<String>>, outputter: Outputter): Unit
}
