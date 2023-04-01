package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.TreeNode
import common.token.Token
import common.token.TokenType
import interpreter.interfaces.Interpreter
import java.lang.AssertionError

class Interpreter: Interpreter {

    private val symbolTable = mutableMapOf<String, Pair<String, String>>()

    override fun interpret(ast: AST) {
        return when (ast){
            is DeclarationAST -> {
                if (ast.getIdentifier() in symbolTable.keys) throw Exception("Variable ${ast.getIdentifier()} is already declared")
                val identifier = ast.getIdentifier()
                val type = ast.getType()
                symbolTable[identifier] = Pair(type, "")
            }

//            is DeclarationAssignationAST -> {
//                val identifier = ast.getIdentifier()
//                val type = ast.getType()
//                symbolTable[identifier] = Pair(type, "")
//            }

            is AssignationAST -> {
                if (ast.getIdentifier() !in symbolTable.keys) throw Exception("Variable ${ast.getIdentifier()} is not declared")

                val identifier = ast.getIdentifier()
                val type = symbolTable[identifier]?.first

                val rhs = ast.getRhsNode() // ID, LITERAL, EXPRESSION TREE

                val rhsValue: Pair<String, TokenType> = evaluateRhs(rhs)

                when (rhsValue.second) {
                    TokenType.IDENTIFIER -> {
                        try {
                            assert(rhs.getValue() in symbolTable.keys && symbolTable[rhsValue.first]!!.first == type)
                            symbolTable[identifier] = Pair(type.toString(), symbolTable[rhsValue.first]!!.second)
                        } catch (error: AssertionError) {
                            throw Exception("Variable ${rhsValue.first} is not declared")
                        }
                    }

                    TokenType.NUMERIC_LITERAL, TokenType.STRING_LITERAL -> {
                        if (type != rhsValue.second.toString()) throw Exception("Type mismatch in $identifier assignation")
                        symbolTable[identifier] = Pair(type.toString(), rhsValue.first)
                    }

                    else -> {
                        throw Exception("Unsupported operation")
                    }
                }

            }

            else -> {
                throw Exception("Invalid AST")
            }

        }
    }

    private fun evaluateRhs(rhs: Node): Pair<String, TokenType> {

        val foundValue: Pair<String, TokenType> = when(rhs) {
            is LeafNode -> {
                if(rhs.type == TokenType.IDENTIFIER || rhs.type == TokenType.STRING_LITERAL || rhs.type == TokenType.NUMERIC_LITERAL) {
                    Pair(rhs.getValue(), rhs.type)
                }
                else {
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
                    Pair( (left.first.toDouble() + right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    Pair(left.first + right.first, TokenType.STRING_LITERAL)
                }
            }
            "-" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair( (left.first.toDouble() - right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    throw Exception("Unsupported operation")
                }
            }
            "*" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair( (left.first.toDouble() * right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    throw Exception("Unsupported operation")
                }            }
            "/" -> {
                val left = evaluateExpression(node.left!!)
                val right = evaluateExpression(node.right!!)
                if (left.second == TokenType.NUMERIC_LITERAL && right.second == TokenType.NUMERIC_LITERAL) {
                    Pair( (left.first.toDouble() / right.first.toDouble()).toString(), TokenType.NUMERIC_LITERAL)
                } else {
                    throw Exception("Unsupported operation")
                }            }
            else -> {
                throw Exception("Unsupported operation")
            }
        }
    }
}
