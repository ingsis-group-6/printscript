package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.asts.FunctionAST
import interpreter.interfaces.Interpreter

class Interpreter : Interpreter {

    private val symbolTable = mutableMapOf<String, Pair<String, String>>()

    override fun interpret(ast: AST) {
        return when (ast) {
            is DeclarationAST -> {
                DeclarationInterpreter(symbolTable).interpret(ast)
            }

            is DeclarationAssignationAST -> {
                DeclarationInterpreter(symbolTable).interpret(ast.getDeclarationAST())
                AssignationInterpreter(symbolTable).interpret(ast.getAssignationAST())
            }

            is AssignationAST -> {
                AssignationInterpreter(symbolTable).interpret(ast)
            }

            is FunctionAST -> {
                FunctionInterpreter(symbolTable).interpret(ast)
            }

            else -> {
                throw Exception("Invalid AST")
            }
        }
    }
    fun getSymbolTable(): Map<String, Pair<String, String>> {
        return this.symbolTable
    }
}
