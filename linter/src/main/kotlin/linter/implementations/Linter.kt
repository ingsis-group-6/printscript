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
            IdentifierCaseLinter(CaseConvention.CAMEL_CASE),
            SyntaxLinter()
        )
    }

    override fun lint(ast: Pair<AST, List<String>>) {
        linters.forEach { linter: Linter -> linter.lint(ast) }
    }
}
