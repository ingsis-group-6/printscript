package linter.implementations

import common.ast.AST
import linter.`interface`.Linter

class Linter : Linter {

    private val linters: Set<Linter>
    init {
        linters = readRules()
    }

    private fun readRules(): Set<Linter> {
        return setOf(
            FunctionExpressionLinter(),
            IdentifierCaseLinter(CaseConvention.CAMEL_CASE)
        )
    }

    override fun lint(ast: AST) {
        linters.forEach { linter: Linter -> linter.lint(ast) }
    }
}
