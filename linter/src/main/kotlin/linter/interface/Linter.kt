package linter.`interface`

import common.ast.AST

interface Linter {
    fun lint(ast: Pair<AST, List<String>>): Unit
}
