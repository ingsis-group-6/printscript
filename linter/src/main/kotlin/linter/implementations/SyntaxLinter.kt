package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.EmptyAST
import linter.`interface`.Linter

class SyntaxLinter: Linter {
    override fun lint(ast: Pair<AST, List<String>>) {
        when(ast.first){
            is EmptyAST -> ast.second.map { println(it) }
            else -> return
        }
    }
}