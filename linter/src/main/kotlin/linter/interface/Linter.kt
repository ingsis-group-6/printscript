package linter.`interface`

import common.ast.implementations.asts.AST

interface Linter {
    fun lint(ast: Pair<AST, List<String>>): Unit
}
