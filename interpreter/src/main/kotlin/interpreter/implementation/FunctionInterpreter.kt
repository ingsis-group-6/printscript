package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.FunctionAST
import common.token.TokenType
import interpreter.interfaces.Interpreter

class FunctionInterpreter (
    private val symbolTable : MutableMap<String, Pair<String, String>>
) : Interpreter {


    override fun interpret(ast: AST) {
        ast as FunctionAST
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
}