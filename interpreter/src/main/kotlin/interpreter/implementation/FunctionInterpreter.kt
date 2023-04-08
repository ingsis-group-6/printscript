package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.FunctionAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.Utils
import interpreter.interfaces.Interpreter

class FunctionInterpreter(
    private val symbolTable: MutableMap<String, Pair<String, String?>>
) : Interpreter {

    override fun interpret(ast: AST) {
        ast as FunctionAST
        val paramNode = ast.getParamNode()
        val currentLine = ast.getTokensInLine().first().row
        when (paramNode) {
            is LeafNode -> {
                when (paramNode.type) {
                    TokenType.IDENTIFIER -> {
                        if (paramNode.getValue() !in symbolTable.keys) {
                            throw Exception("(Line $currentLine) - Variable ${paramNode.getValue()} is not declared")
                        }
                        val identifierValue = symbolTable[paramNode.getValue()]!!.second
                        if (identifierValue == null) {
                            throw Exception("(Line $currentLine) - Variable ${paramNode.getValue()} is not initialized")
                        } else {
                            println(identifierValue)
                        }
                    }

                    TokenType.STRING_LITERAL -> {
                        println(removeStartAndEndStringQuotes(paramNode))
                    }

                    TokenType.NUMERIC_LITERAL -> {
                        println(paramNode.getValue())
                    }

                    else -> {
                        throw java.lang.Exception("(Line $currentLine) - Unsupported Operation")
                    }
                }
            }
            is TreeNode -> {
                val evaluator = ExpressionTreeEvaluator(symbolTable)
                println(Utils.checkIfInteger(evaluator.evaluateExpression(paramNode)).first)
            }
            else -> {
            }
        }
    }

    private fun removeStartAndEndStringQuotes(paramNode: Node) = paramNode.getValue().substring(1).dropLast(1)
}
