package interpreter.implementation

import common.ast.implementations.asts.AST
import common.ast.implementations.asts.BlockAST
import common.ast.implementations.asts.ConditionalAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.ReadInputNode
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.Scope
import interpreter.input.Inputter
import interpreter.interfaces.Interpreter
import interpreter.output.Outputter
import kotlin.Exception

class ConditionalInterpreter(
    private val scope: Scope,
    private val inputter: Inputter,
    private val outputter: Outputter,
    private val isEOF: BooleanWrapper
) : Interpreter<ConditionalAST> {
    override fun interpret(ast: ConditionalAST) {
        val conditionNode = ast.getCondition()
        val condition: Boolean = evaluateConditionNode(conditionNode, ast.getCurrentLine())
        val blockInterpreter = BlockInterpreter(
            Scope(mutableMapOf(), mutableMapOf(), scope),
            inputter,
            outputter,
            isEOF
        )
        if (condition) {
            blockInterpreter.interpret(ast.getIfBlock())
        } else {
            if ((ast.getElseBlock() as BlockAST).isEmpty()) return
            blockInterpreter.interpret(ast.getElseBlock() as BlockAST)
        }
    }

    private fun evaluateConditionNode(conditionNode: Node, currentLine: Int): Boolean {
        return when (conditionNode) {
            is LeafNode -> {
                when (conditionNode.type) {
                    TokenType.IDENTIFIER -> {
                        val foundValue = searchInScopes(conditionNode.getValue(), currentLine)
                        if (foundValue.first != "boolean") throw Exception("($currentLine) - ${conditionNode.getValue()} is not a boolean.")
                        if (foundValue.second == null) throw Exception("($currentLine) - ${conditionNode.getValue()} is not initialized.")
                        foundValue.second!!.toBoolean()
                    }
                    TokenType.BOOLEAN_TRUE -> {
                        true
                    }
                    TokenType.BOOLEAN_FALSE -> {
                        false
                    }

                    else -> { throw Exception("($currentLine) - Not a boolean.") }
                }
            }

//            else -> {
//                throw Exception("($currentLine) - Unsupported operation.")
//            }

//            is ReadInputNode -> {
//                throw Exception("($currentLine) - Boolean expressions not supported.")
//            }
            is ReadInputNode -> throw Exception("($currentLine) - Boolean expressions not supported.")
            is TreeNode -> throw Exception("($currentLine) - Expressions not supported.")
        }
    }

    private fun searchInScopes(identifier: String, currentLine: Int): Pair<String, String?> {
        return scope.findVariableData(identifier, currentLine)
    }
}
