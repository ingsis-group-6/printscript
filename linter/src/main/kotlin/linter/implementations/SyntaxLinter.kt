package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.EmptyAST
import common.io.Outputter
import linter.`interface`.Linter

class SyntaxLinter : Linter {
    override fun lint(ast: Pair<AST, List<String>>, outputter: Outputter) {
        when (ast.first) {
            is EmptyAST -> ast.second.map { outputter.output(it) }
            else -> return
        }
    }
}
