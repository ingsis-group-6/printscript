package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.ReadInputNode
import common.ast.implementations.node.TreeNode
import common.token.TokenType
import interpreter.Utils
import interpreter.interfaces.Interpreter
import java.lang.AssertionError
import kotlin.Exception
import kotlin.collections.HashMap

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
        val typeToAssign = mutableSymbolTable[identifier]?.first ?: immutableSymbolTable[identifier]?.first

        val rhs = ast.getRhsNode() // ID, LITERAL, EXPRESSION TREE

        val rhsValue: Pair<String?, TokenType> = Utils.checkIfInteger(evaluateRhs(rhs, typeToAssign))

        val rhsCalculatedValueType = rhsValue.second

        when (rhsCalculatedValueType) {
            TokenType.IDENTIFIER -> {
                try {
                    val rhsValueFirst = rhsValue.first
                    val typeString = typeToAssign.toString()
                    val rhsValueInMutable = rhsValueFirst in mutableSymbolTable.keys
                    val rhsValueInImmutable = rhsValueFirst in immutableSymbolTable.keys
                    val identifierInMutable = identifier in mutableSymbolTable.keys
                    val identifierInImmutable = identifier in immutableSymbolTable.keys

                    assert(
                        rhs.getValue() in mutableSymbolTable.keys &&
                            (rhsValueInMutable || rhsValueInImmutable) &&
                            (
                                (rhsValueInMutable && mutableSymbolTable[rhsValueFirst]!!.first == typeToAssign) ||
                                    (rhsValueInImmutable && immutableSymbolTable[rhsValueFirst]!!.first == typeToAssign)
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
                if (typeToAssign != simplifiedType) throw Exception("(Line $currentLine) - Type mismatch in $identifier assignation")
                if (identifier in mutableSymbolTable.keys) {
                    mutableSymbolTable[identifier] = Pair(typeToAssign.toString(), rhsValue.first)
                } else {
                    immutableSymbolTable[identifier] = Pair(typeToAssign.toString(), rhsValue.first)
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

    private fun evaluateRhs(rhs: Node, typeToAssign: String?): Pair<String?, TokenType> {
        val foundValue = when (rhs) {
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
            is ReadInputNode -> {
                val inputValue = when(rhs.messageType) {
                    TokenType.IDENTIFIER -> {
                        val identifierData = getIdentifierValue(rhs.message)
                        readValueFromConsole(identifierData.second)
                    }
                    TokenType.STRING_LITERAL -> {
                        readValueFromConsole(rhs.message)
                    }
                    else -> ""
                }
                interpretInputText(inputValue, typeToAssign!!)
            }
            else -> {
                throw Exception("(Line $currentLine) - Unsupported operation")
            }
        }

        return foundValue
    }

    private fun tokenTypeOf(stringSimplifiedType: String): TokenType {
        return when(stringSimplifiedType) {
            "number" -> TokenType.NUMERIC_LITERAL
            "string" -> TokenType.STRING_LITERAL
            "boolean" -> TokenType.BOOLEAN_TRUE
            else -> TokenType.STRING_LITERAL
        }
    }

    private fun interpretInputText(inputValue: String?, typeToAssign: String): Pair<String?, TokenType> {
        if(inputValue == null) return Pair(null, tokenTypeOf(typeToAssign))
        return when(typeToAssign) {
            "number" -> {
                val parsedValue =inputValue.toDoubleOrNull()
                if(parsedValue == null) throw Exception("(Line $currentLine) - Type mismatch in input. Expected $typeToAssign.")
                Pair(inputValue, tokenTypeOf(typeToAssign))
            }
            "string" -> {
                Pair(inputValue, tokenTypeOf(typeToAssign))
            }
            "boolean" -> {
                val parsedValue =toBooleanOrNull(inputValue)
                if(parsedValue == null) throw Exception("(Line $currentLine) - Type mismatch in input. Expected $typeToAssign.")
                Pair(inputValue, tokenTypeOf(typeToAssign))
            }
            else -> Pair(inputValue, tokenTypeOf(typeToAssign))
        }
    }

    private fun toBooleanOrNull(inputValue: String): Boolean? {
        return when(inputValue.lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }

    }

    private fun readValueFromConsole(message: String): String? {
        println(message)
        return readlnOrNull()

    }

    fun getIdentifierValue(identifierKey: String): Pair<String, String> {
        if (identifierKey in this.mutableSymbolTable.keys) {
            return mutableSymbolTable[identifierKey] as Pair<String, String>
        }

        if (identifierKey in this.immutableSymbolTable.keys) {
            return immutableSymbolTable[identifierKey] as Pair<String, String>
        }

        throw Exception("$identifierKey not initialized")
    }


    private fun checkIfIdentifierWasDeclared(ast: AssignationAST) {
        val identifier = ast.getIdentifier()

        if (identifier !in mutableSymbolTable.keys && identifier !in immutableSymbolTable.keys) {
            throw Exception("(Line $currentLine) - Variable $identifier is not declared")
        }

        if (identifier in immutableSymbolTable && immutableSymbolTable[identifier]!!.second != null) {
            throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is immutable")
        }

    }


}
