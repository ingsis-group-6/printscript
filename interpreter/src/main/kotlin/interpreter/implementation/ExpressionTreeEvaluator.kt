package interpreter.implementation

import common.ast.implementations.node.TreeNode
import common.token.TokenType
import java.lang.Exception

class ExpressionTreeEvaluator(private val symbolTable: MutableMap<String, Pair<String, String?>>) {

    fun evaluateExpression(node: TreeNode): Pair<String?, TokenType> =
        when (node.headToken) {
            "+" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)

                val leftResult = getIdentifierValue(left, symbolTable)
                val rightResult = getIdentifierValue(right, symbolTable)

                when {
                    leftResult.second == TokenType.NUMERIC_LITERAL && rightResult.second == TokenType.NUMERIC_LITERAL -> {
                        val value = leftResult.first!!.toDouble() + rightResult.first!!.toDouble()
                        Pair(value.toString(), TokenType.NUMERIC_LITERAL)
                    }
                    else -> Pair(leftResult.first + rightResult.first, TokenType.STRING_LITERAL)
                }
            }
            "-" -> evaluateBinaryOperation(node, Double::minus)
            "*" -> evaluateBinaryOperation(node, Double::times)
            "/" -> evaluateBinaryOperation(node, Double::div)
            else -> evaluateLiteral(node.headToken)
        }
    fun getIdentifierValue(expression: Pair<String?, TokenType>, symbolTable: MutableMap<String, Pair<String, String?>>): Pair<String?, TokenType> {
        return when (expression.second) {
            TokenType.IDENTIFIER -> {
                val (type, value) = symbolTable.get(expression.first)
                    ?: throw Exception("The variable ${expression.first} is not declared")
                val typeAsTokenType = if (type == "number") TokenType.NUMERIC_LITERAL else TokenType.STRING_LITERAL
                Pair(value, typeAsTokenType)
            }
            else -> expression
        }
    }

    private fun evaluateBinaryOperation(node: TreeNode, operation: (Double, Double) -> Double): Pair<String, TokenType> {
        val left = evaluateExpression(node.left!!)
        val right = evaluateExpression(node.right!!)

        val leftValue = getValue(left)
        val rightValue = getValue(right)

        if (leftValue.second == TokenType.NUMERIC_LITERAL && rightValue.second == TokenType.NUMERIC_LITERAL) {
            val result = operation(leftValue.first.toDouble(), rightValue.first.toDouble())
            return Pair(result.toString(), TokenType.NUMERIC_LITERAL)
        } else {
            throw Exception("Unsupported Operation")
        }
    }

    private fun getValue(token: Pair<String?, TokenType>): Pair<Double, TokenType> {
        return if (token.second == TokenType.IDENTIFIER) {
            if (!symbolTable.contains(token.first)) {
                throw Exception("The variable ${token.first} is not declared")
            }
            if (symbolTable[token.first]!!.second == null) {
                throw Exception("The variable ${token.first} was not initialized")
            }
            val (type, value) = symbolTable[token.first]!!
            val typeAsTokenType = if (type == "number") TokenType.NUMERIC_LITERAL else TokenType.STRING_LITERAL
            Pair(value!!.toDouble(), typeAsTokenType)
        } else {
            Pair(token.first!!.toDouble(), TokenType.NUMERIC_LITERAL)
        }
    }

    private fun evaluateLiteral(token: String): Pair<String?, TokenType> {
        return when {
            isNumber(token) -> {
                Pair(token, TokenType.NUMERIC_LITERAL)
            }
            (symbolTable.contains(token)) -> {
                if (this.symbolTable.get(key = token)!!.second == null) {
                    throw Exception("Variable not initialized")
                } else {
                    val dataInTable = this.symbolTable.get(key = token)
                    Pair(dataInTable!!.second, if (dataInTable.first == "number") TokenType.NUMERIC_LITERAL else TokenType.STRING_LITERAL)
                }
            }
            (token.startsWith('"') && token.endsWith('"') || token.startsWith('\'') && token.endsWith('\'')) -> {
                Pair(token.dropLast(1).substring(1), TokenType.STRING_LITERAL)
            }
            else -> throw Exception("Variable $token not assigned")
        }
    }

    private fun isNumber(token: String) = token.toDoubleOrNull() != null
}
