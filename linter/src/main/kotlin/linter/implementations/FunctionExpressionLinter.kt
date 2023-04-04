package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.FunctionAST
import common.ast.implementations.node.TreeNode
import linter.`interface`.Linter

class FunctionExpressionLinter : Linter {
    override fun lint(ast: AST) {
        if (ast is FunctionAST) {
            if (ast.getParamNode() is TreeNode) {
                val currentLine = ast.getTokensInLine().first().row
                println("(Line $currentLine) - There is an expression passed as argument for function.")
            }
        }
    }
}
