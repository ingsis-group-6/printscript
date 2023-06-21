package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.node.LeafNode
import common.ast.implementations.node.Node
import common.ast.implementations.node.ReadInputNode
import common.ast.implementations.node.TreeNode
import common.io.Inputter
import common.io.Outputter
import common.token.TokenType
import interpreter.Utils
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope
import java.lang.AssertionError
import kotlin.Exception
import kotlin.collections.HashMap

class AssignationInterpreter(
    private val scope: Scope,
    private val inputter: Inputter,
    private val outputter: Outputter
) : Interpreter {

    private var currentLine: Int? = null
    private var currentColumn: Int? = null
    override fun interpret(ast: AST) {
        ast as AssignationAST

        currentLine = ast.getTokensInLine().first().row
        currentColumn = ast.getTokensInLine().first().col

        checkIfIdentifierWasDeclared(ast)
        val identifier = ast.getIdentifier()
        val typeToAssign = scope.findVariableData(identifier, currentLine!!).first

        val rhs = ast.getRhsNode() // ID, LITERAL, EXPRESSION TREE

        val rhsValue: Pair<String?, TokenType> = Utils.checkIfInteger(evaluateRhs(rhs, typeToAssign))

        val rhsCalculatedValueType = rhsValue.second

        when (rhsCalculatedValueType) {
            TokenType.IDENTIFIER -> {
                try {
                    val rhsValueFirst = rhsValue.first
                    val typeString = typeToAssign.toString()
                    val rhsValueInMutable = scope.existsMutableVariable(rhsValueFirst)
                    val rhsValueInImmutable = scope.existsImmutableVariable(rhsValueFirst)
                    val identifierInMutable = scope.existsMutableVariable(identifier)
                    val identifierInImmutable = scope.existsImmutableVariable(identifier)

                    assert(
                        rhs.getValue() in scope.getMutableVariables().keys &&
                            (rhsValueInMutable || rhsValueInImmutable) &&
                            (
                                (rhsValueInMutable && scope.findVariableData(rhsValueFirst!!, currentLine!!)!!.first == typeToAssign) // || (rhsValueInImmutable && immutableSymbolTable[rhsValueFirst]!!.first == typeToAssign)
                                )
                    )

                    if (rhsValueInMutable) {
                        if (identifierInMutable) {
                            scope.putMutableVariable(identifier, Pair(typeString, scope.findVariableData(rhsValueFirst!!, currentLine!!)!!.second), currentLine!!) // mutableSymbolTable[identifier] = Pair(typeString, mutableSymbolTable[rhsValueFirst]!!.second)
                        } else {
                            scope.putImmutableVariable(identifier, Pair(typeString, scope.findVariableData(rhsValueFirst!!, currentLine!!)!!.second), currentLine!!) // immutableSymbolTable[identifier] = Pair(typeString, mutableSymbolTable[rhsValueFirst]!!.second)
                        }
                    } else {
                        if (identifierInImmutable) {
                            scope.putImmutableVariable(identifier, Pair(typeString, scope.findVariableData(rhsValueFirst!!, currentLine!!)!!.second), currentLine!!) // immutableSymbolTable[identifier] = Pair(typeString, immutableSymbolTable[rhsValueFirst]!!.second)
                        } else {
                            scope.putMutableVariable(identifier, Pair(typeString, scope.findVariableData(rhsValueFirst!!, currentLine!!)!!.second), currentLine!!) // mutableSymbolTable[identifier] = Pair(typeString, immutableSymbolTable[rhsValueFirst]!!.second)
                        }
                    }
                } catch (error: AssertionError) {
                    throw Exception("(Line $currentLine - Variable ${rhsValue.first} is not declared")
                }
            }

            TokenType.NUMERIC_LITERAL, TokenType.STRING_LITERAL, TokenType.BOOLEAN_TRUE, TokenType.BOOLEAN_FALSE -> {
                val simplifiedType = getSimplifiedType(rhsValue)
                if (typeToAssign != simplifiedType) throw Exception("(Line $currentLine) - Type mismatch in $identifier assignation")
                if (scope.existsMutableVariable(identifier!!)) {
                    scope.putMutableVariable(identifier, Pair(typeToAssign.toString(), rhsValue.first), currentLine!!) // mutableSymbolTable[identifier] = Pair(typeToAssign.toString(), rhsValue.first)
                } else {
                    scope.putImmutableVariable(identifier, Pair(typeToAssign.toString(), rhsValue.first), currentLine!!) // immutableSymbolTable[identifier] = Pair(typeToAssign.toString(), rhsValue.first)
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
                val copyMap = HashMap(scope.getMutableVariables())
                copyMap.putAll(scope.getImmutableVariables())
                val evaluator = ExpressionTreeEvaluator(copyMap)
                evaluator.evaluateExpression(rhs)
            }
            is ReadInputNode -> {
                val inputValue = when (rhs.messageType) {
                    TokenType.IDENTIFIER -> {
                        val identifierData = getIdentifierValue(rhs.message)
                        readValueFromInput(identifierData.second)
                    }
                    TokenType.STRING_LITERAL -> {
                        readValueFromInput(rhs.message)
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
        return when (stringSimplifiedType) {
            "number" -> TokenType.NUMERIC_LITERAL
            "string" -> TokenType.STRING_LITERAL
            "boolean" -> TokenType.BOOLEAN_TRUE
            else -> TokenType.STRING_LITERAL
        }
    }

    private fun interpretInputText(inputValue: String?, typeToAssign: String): Pair<String?, TokenType> {
        if (inputValue == null) return Pair(null, tokenTypeOf(typeToAssign))
        return when (typeToAssign) {
            "number" -> {
                val parsedValue = inputValue.toDoubleOrNull()
                if (parsedValue == null) throw Exception("(Line $currentLine) - Type mismatch in input. Expected $typeToAssign.")
                Pair(inputValue, tokenTypeOf(typeToAssign))
            }
            "string" -> {
                Pair(inputValue, tokenTypeOf(typeToAssign))
            }
            "boolean" -> {
                val parsedValue = toBooleanOrNull(inputValue)
                if (parsedValue == null) throw Exception("(Line $currentLine) - Type mismatch in input. Expected $typeToAssign.")
                Pair(inputValue, tokenTypeOf(typeToAssign))
            }
            else -> Pair(inputValue, tokenTypeOf(typeToAssign))
        }
    }

    private fun toBooleanOrNull(inputValue: String): Boolean? {
        return when (inputValue.lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

    private fun readValueFromInput(message: String): String? {
        outputter.output(message.substring(1).dropLast(1))
        val input = inputter.getInputLine() // todo ver porque desde la terminal no espera al input

        return input ?: "[INPUT NOT READ]"
    }

    fun getIdentifierValue(identifierKey: String): Pair<String, String> {
//        if (identifierKey in this.mutableSymbolTable.keys) {
//            return mutableSymbolTable[identifierKey] as Pair<String, String>
//        }
//
//        if (identifierKey in this.immutableSymbolTable.keys) {
//            return immutableSymbolTable[identifierKey] as Pair<String, String>
//        }
        if (scope.existsVariable(identifierKey)) {
            return scope.findVariableData(identifierKey, currentLine!!) as Pair<String, String>
        }

        throw Exception("$identifierKey not initialized")
    }

    private fun checkIfIdentifierWasDeclared(ast: AssignationAST) {
        val identifier = ast.getIdentifier()

        if (!scope.existsVariable(identifier)) {
            throw Exception("(Line $currentLine) - Variable $identifier is not declared")
        }

        if (scope.existsImmutableVariable(identifier) && scope.findVariableData(identifier, currentLine!!).second != null) {
            throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is immutable")
        }
    }
}
