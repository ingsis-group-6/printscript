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
    private val mutableSymbolTable: MutableMap<String, Pair<String, String?>>,
    private val immutableSymbolTable: MutableMap<String, Pair<String, String?>>
) : Interpreter {

    private var currentLine: Int? = null
    private var currentColumn: Int? = null
    override fun interpret(ast: AST) {
        ast as AssignationAST

        currentLine = ast.getTokensInLine().first().row
        currentColumn = ast.getTokensInLine().first().col

        checkIfIdentifierWasDeclared(ast)
        val identifier = ast.getIdentifier()
        val type = mutableSymbolTable[identifier]?.first ?: immutableSymbolTable[identifier]?.first

        val rhs = ast.getRhsNode() // ID, LITERAL, EXPRESSION TREE

        val rhsValue: Pair<String?, TokenType> = Utils.checkIfInteger(evaluateRhs(rhs))

        val rhsCalculatedValueType = rhsValue.second

        when (rhsCalculatedValueType) {
            TokenType.IDENTIFIER -> {
                try {
                    val rhsValueFirst = rhsValue.first
                    val typeString = type.toString()
                    val rhsValueInMutable = rhsValueFirst in mutableSymbolTable.keys
                    val rhsValueInImmutable = rhsValueFirst in immutableSymbolTable.keys
                    val identifierInMutable = identifier in mutableSymbolTable.keys
                    val identifierInImmutable = identifier in immutableSymbolTable.keys

                    assert(
                        rhs.getValue() in mutableSymbolTable.keys &&
                            (rhsValueInMutable || rhsValueInImmutable) &&
                            (
                                (rhsValueInMutable && mutableSymbolTable[rhsValueFirst]!!.first == type) ||
                                    (rhsValueInImmutable && immutableSymbolTable[rhsValueFirst]!!.first == type)
                                )
                    )

                    if (rhsValueInMutable) {
                        if (identifierInMutable) {
                            mutableSymbolTable[identifier] = Pair(typeString, mutableSymbolTable[rhsValueFirst]!!.second)
                        } else {
                            immutableSymbolTable[identifier] = Pair(typeString, mutableSymbolTable[rhsValueFirst]!!.second)
                        }
                    } else {
                        if (identifierInImmutable) {
                            immutableSymbolTable[identifier] = Pair(typeString, immutableSymbolTable[rhsValueFirst]!!.second)
                        } else {
                            mutableSymbolTable[identifier] = Pair(typeString, immutableSymbolTable[rhsValueFirst]!!.second)
                        }
                    }
                } catch (error: AssertionError) {
                    throw Exception("(Line $currentLine - Variable ${rhsValue.first} is not declared")
                }
            }

            TokenType.NUMERIC_LITERAL, TokenType.STRING_LITERAL, TokenType.BOOLEAN_TRUE, TokenType.BOOLEAN_FALSE -> {
                val simplifiedType = getSimplifiedType(rhsValue)
                if (type != simplifiedType) throw Exception("(Line $currentLine) - Type mismatch in $identifier assignation")
                if (identifier in mutableSymbolTable.keys) {
                    mutableSymbolTable[identifier] = Pair(type.toString(), rhsValue.first)
                } else {
                    immutableSymbolTable[identifier] = Pair(type.toString(), rhsValue.first)
                }
            }

            else -> {
                throw Exception("(Line $currentLine) - Unsupported operation")
            }
        }
    }

    private fun getSimplifiedType(rhsValue: Pair<String?, TokenType>) =
        when (rhsValue.second) {
            TokenType.NUMERIC_LITERAL -> "number"
            TokenType.STRING_LITERAL -> "string"
            TokenType.BOOLEAN_TRUE, TokenType.BOOLEAN_FALSE -> "boolean"
            else -> ""
        }

    private fun evaluateRhs(rhs: Node): Pair<String?, TokenType> {
        val foundValue: Pair<String?, TokenType> = when (rhs) {
            is LeafNode -> {
                if (rhs.type == TokenType.IDENTIFIER ||
                    rhs.type == TokenType.STRING_LITERAL ||
                    rhs.type == TokenType.NUMERIC_LITERAL ||
                    rhs.type == TokenType.BOOLEAN_TRUE ||
                    rhs.type == TokenType.BOOLEAN_FALSE
                ) {
                    Pair(rhs.getValue(), rhs.type)
                } else {
                    throw Exception("(Line $currentLine) - Unsupported operation")
                }
            }
            is TreeNode -> {
                val copyMap = HashMap(mutableSymbolTable)
                copyMap.putAll(immutableSymbolTable)
                val evaluator = ExpressionTreeEvaluator(copyMap)
                evaluator.evaluateExpression(rhs)
            }
            else -> {
                throw Exception("(Line $currentLine) - Unsupported operation")
            }
        }

        return foundValue
    }

    private fun checkIfIdentifierWasDeclared(ast: AssignationAST) {
        val identifier = ast.getIdentifier()

        if (identifier !in mutableSymbolTable.keys && identifier !in immutableSymbolTable.keys) {
            throw Exception("(Line $currentLine) - Variable $identifier is not declared")
        }

        if (identifier in immutableSymbolTable && immutableSymbolTable[identifier]!!.second != null) {
            throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is immutable")
        }

//        if (ast.getIdentifier() !in mutableSymbolTable.keys) {
//            println("no es mutable")
//            if (ast.getIdentifier() !in immutableSymbolTable.keys) {
//                println("tampoco es inmutable, osea no esta declarada digamo")
//                throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is not declared")
//            } else {
//                println("es un const")
//                if(immutableSymbolTable[ast.getIdentifier()]!!.second != null){
//                    println("es const pero ya tiene valor degamo")
//                    throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is immutable")
//                }
//
//            }
//        }
    }
}
