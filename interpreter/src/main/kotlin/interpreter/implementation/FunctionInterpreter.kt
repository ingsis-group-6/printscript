package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.FunctionAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.Utils
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope
import interpreter.output.ConsolePrintOutputter
import interpreter.output.Outputter

class FunctionInterpreter(
    private val scope: Scope
) : Interpreter {

    private val outputter: Outputter = ConsolePrintOutputter()

    override fun interpret(ast: AST) {
        ast as FunctionAST
        val paramNode = ast.getParamNode()
        val currentLine = ast.getTokensInLine().first().row
        when (paramNode) {
            is LeafNode -> {
                val outputValue = when (paramNode.type) {
                    TokenType.IDENTIFIER -> getIdentifierValue(paramNode.getValue(), currentLine)
                    TokenType.STRING_LITERAL -> removeStartAndEndStringQuotes(paramNode)
                    TokenType.NUMERIC_LITERAL -> paramNode.getValue()
                    else -> throw java.lang.Exception("(Line $currentLine) - Unsupported Operation")
                }
                outputter.output(outputValue)
            }
            is TreeNode -> {
                val evaluator = ExpressionTreeEvaluator(scope.getAllVariables())
                outputter.output(Utils.checkIfInteger(evaluator.evaluateExpression(paramNode)).first!!)
            }
            else -> {
            }
        }
    }

    private fun getIdentifierValue(value: String, currentLine: Int): String {
        // if (value !in mutableSymbolTable.keys && value !in immutableSymbolTable.keys) {
        if (!scope.existsVariable(value)) {
            throw Exception("(Line $currentLine) - Variable $value is not declared")
        }

        return (scope.findVariableData(value, currentLine).second)
            ?: throw Exception("(Line $currentLine) - Variable $value is not initialized")
    }

    private fun removeStartAndEndStringQuotes(paramNode: Node) = paramNode.getValue().substring(1).dropLast(1)
}
