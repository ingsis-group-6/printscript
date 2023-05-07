package linter.implementations

import common.ast.implementations.asts.AST
import common.ast.implementations.asts.FunctionAST
import common.ast.implementations.node.TreeNode
import linter.`interface`.Linter

class FunctionExpressionLinter : Linter {
    override fun lint(ast: Pair<AST, List<String>>) {
        if (ast.first is FunctionAST) {
            if ((ast.first as FunctionAST).getParamNode() is TreeNode) {
                val currentLine = ast.first.getTokensInLine().first().row
                println("(Line $currentLine) - There is an expression passed as argument for function.")
            }
        }
    }
}
