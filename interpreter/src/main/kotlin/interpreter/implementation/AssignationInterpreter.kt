package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.interfaces.Interpreter
import java.lang.AssertionError
import java.lang.Exception

class AssignationInterpreter (
    private val symbolTable : MutableMap<String, Pair<String, String>>
) : Interpreter {


    override fun interpret(ast: AST) {
        ast as AssignationAST
        checkIfIdentifierWasDeclared(ast)

        val identifier = ast.getIdentifier()
        val type = symbolTable[identifier]?.first

        val rhs = ast.getRhsNode() // ID, LITERAL, EXPRESSION TREE

        val rhsValue: Pair<String, TokenType> = checkIfInteger(evaluateRhs(rhs))

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

    private fun checkIfInteger(rhsValue: Pair<String, TokenType>): Pair<String, TokenType> {
        var rhsValue1 = rhsValue
        if (rhsValue1.second == TokenType.NUMERIC_LITERAL) {
            val parsed = rhsValue1.first.toDouble()
            rhsValue1 = if (isWhole(parsed)) Pair(parsed.toInt().toString(), TokenType.NUMERIC_LITERAL) else rhsValue1
        }
        return rhsValue1
    }

    private fun isWhole(parsed: Double) = parsed % 1 == 0.0

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
                evaluateExpression(rhs)
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

    private fun evaluateExpression(node: TreeNode): Pair<String, TokenType> =
        when (node.headToken) {
            "+" -> evaluateBinaryOperation(node, Double::plus, TokenType.NUMERIC_LITERAL, OperationTypeResult.STRING_RESULT)
            "-" -> evaluateBinaryOperation(node, Double::minus, TokenType.NUMERIC_LITERAL, OperationTypeResult.UNSUPPORTED_OPERATION)
            "*" -> evaluateBinaryOperation(node, Double::times, TokenType.NUMERIC_LITERAL, OperationTypeResult.UNSUPPORTED_OPERATION)
            "/" -> evaluateBinaryOperation(node, Double::div, TokenType.NUMERIC_LITERAL, OperationTypeResult.UNSUPPORTED_OPERATION)
            else -> evaluateLiteral(node.headToken)
        }


    private fun evaluateBinaryOperation(node: TreeNode, operation: (Double, Double) -> Double, numericResultType: TokenType, otherResultType: OperationTypeResult): Pair<String, TokenType> {
        val left = evaluateExpression(node.left!!)
        val right = evaluateExpression(node.right!!)
        if (left.second == numericResultType && right.second == numericResultType) {
            val result = operation(left.first.toDouble(), right.first.toDouble())
            return Pair(result.toString(), numericResultType)
        } else {
            if (otherResultType == OperationTypeResult.STRING_RESULT) return Pair(left.first + right.first, TokenType.STRING_LITERAL)
            else throw Exception("Unsupported Operation")

        }
    }

    private fun evaluateLiteral(token: String): Pair<String, TokenType> =
        if (token.toDoubleOrNull() != null) Pair(token, TokenType.NUMERIC_LITERAL) else Pair(token, TokenType.STRING_LITERAL)

    enum class OperationTypeResult{

        UNSUPPORTED_OPERATION,
        STRING_RESULT
    }



}



