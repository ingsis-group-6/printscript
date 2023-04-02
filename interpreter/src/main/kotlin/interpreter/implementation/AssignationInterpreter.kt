package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.Utils
import interpreter.interfaces.Interpreter
import java.lang.AssertionError
import java.lang.Exception

class AssignationInterpreter(
    private val symbolTable: MutableMap<String, Pair<String, String>>
) : Interpreter {

    override fun interpret(ast: AST) {
        ast as AssignationAST
        checkIfIdentifierWasDeclared(ast)

        val identifier = ast.getIdentifier()
        val type = symbolTable[identifier]?.first

        val rhs = ast.getRhsNode() // ID, LITERAL, EXPRESSION TREE

        val rhsValue: Pair<String, TokenType> = Utils.checkIfInteger(evaluateRhs(rhs))

        val rhsCalculatedValueType = rhsValue.second

        when (rhsCalculatedValueType) {
            TokenType.IDENTIFIER -> {
                try {
                    assert(rhs.getValue() in symbolTable.keys && symbolTable[rhsValue.first]!!.first == type)
                    symbolTable[identifier] = Pair(type.toString(), symbolTable[rhsValue.first]!!.second)
                } catch (error: AssertionError) {
                    throw Exception("Variable ${rhsValue.first} is not declared")
                }
            }

            TokenType.NUMERIC_LITERAL, TokenType.STRING_LITERAL -> {
                val simplifiedType = if (rhsValue.second == TokenType.NUMERIC_LITERAL) "number" else "string"
                if (type != simplifiedType) throw Exception("Type mismatch in $identifier assignation")
                symbolTable[identifier] = Pair(type.toString(), rhsValue.first)
            }

            else -> {
                throw Exception("Unsupported operation")
            }
        }
    }

    private fun evaluateRhs(rhs: Node): Pair<String, TokenType> {
        val foundValue: Pair<String, TokenType> = when (rhs) {
            is LeafNode -> {
                if (rhs.type == TokenType.IDENTIFIER || rhs.type == TokenType.STRING_LITERAL || rhs.type == TokenType.NUMERIC_LITERAL) {
                    Pair(rhs.getValue(), rhs.type)
                } else {
                    throw Exception("Unsupported operation")
                }
            }
            is TreeNode -> {
                val evaluator = ExpressionTreeEvaluator(symbolTable)
                evaluator.evaluateExpression(rhs)
            }
            else -> {
                throw Exception("Unsupported operation")
            }
        }

        return foundValue
    }

    private fun checkIfIdentifierWasDeclared(ast: AssignationAST) {
        if (ast.getIdentifier() !in symbolTable.keys) throw Exception("Variable ${ast.getIdentifier()} is not declared")
    }
}
