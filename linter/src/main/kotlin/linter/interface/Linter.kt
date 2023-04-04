package linter.`interface`

import common.ast.AST

interface Linter {
    fun lint(ast: AST): Unit
}