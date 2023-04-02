package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.asts.FunctionAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.interfaces.Interpreter
import java.lang.AssertionError

class Interpreter : Interpreter {

    private val symbolTable = mutableMapOf<String, Pair<String, String>>()

    override fun interpret(ast: AST) {
        return when (ast) {
            is DeclarationAST -> {
                interpretDeclarationAST(ast)
            }

            is DeclarationAssignationAST -> {
                val declarationAST = ast.getDeclarationAST()
                val assignationAST = ast.getAssignationAST()
                interpretDeclarationAST(declarationAST as DeclarationAST)
                interpretAssignationAST(assignationAST as AssignationAST)
            }

            is AssignationAST -> {
                interpretAssignationAST(ast)
            }

            is FunctionAST -> {
                interpretFunctionAST(ast)
            }

            else -> {
                throw Exception("Invalid AST")
            }
        }
    }

    private fun interpretDeclarationAST(ast: DeclarationAST) {
        if (ast.getIdentifier() in symbolTable.keys) throw Exception("Variable ${ast.getIdentifier()} is already declared")
        val identifier = ast.getIdentifier()
        val type = ast.getType()
        symbolTable[identifier] = Pair(type, "")
    }

    private fun interpretFunctionAST(ast: FunctionAST) {
        val paramNode = ast.getParamNode()
        when (paramNode.type) {
            TokenType.IDENTIFIER -> {
                if (paramNode.getValue() !in symbolTable.keys) {
                    throw Exception("Variable ${paramNode.getValue()} is not declared")
                } else {
                    println(symbolTable[paramNode.getValue()]!!.second)
                }
            }

            TokenType.STRING_LITERAL, TokenType.NUMERIC_LITERAL -> {
                println(paramNode.getValue())
            }

            else -> {
                throw java.lang.Exception("Unsupported Operation")
            }
        }
    }

    private fun interpretAssignationAST(ast: AssignationAST) {
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

    private fun checkIfIdentifierWasDeclared(ast: AssignationAST) {
        if (ast.getIdentifier() !in symbolTable.keys) throw Exception("Variable ${ast.getIdentifier()} is not declared")
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

    private fun evaluateExpression(node: TreeNode): Pair<String, TokenType> {
        return when (node.headToken) {
            "+" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair((left.first.toDouble() + right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    Pair(left.first + right.first, TokenType.STRING_LITERAL)
                }
            }
            "-" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair((left.first.toDouble() - right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    throw Exception("Unsupported operation")
                }
            }
            "*" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair((left.first.toDouble() * right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    throw Exception("Unsupported operation")
                }
            }
            "/" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair((left.first.toDouble() / right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    throw Exception("Unsupported operation")
                }
            }
            else -> {
                val isNumber = node.headToken.toDoubleOrNull() != null
                if (isNumber) Pair(node.headToken, TokenType.NUMERIC_LITERAL) else Pair(node.headToken, TokenType.STRING_LITERAL)
            }
        }
    }

    fun getSymbolTable(): Map<String, Pair<String, String>> {
        return this.symbolTable
    }
}
