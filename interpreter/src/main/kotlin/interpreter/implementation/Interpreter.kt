package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.asts.FunctionAST
import interpreter.interfaces.Interpreter

class Interpreter : Interpreter {

    private val mutableSymbolTable = mutableMapOf<String, Pair<String, String?>>()
    private val immutableSymbolTable = mutableMapOf<String, Pair<String, String?>>()

    override fun interpret(ast: AST) {
        return when (ast) {
            is DeclarationAST -> {
                DeclarationInterpreter(mutableSymbolTable, immutableSymbolTable).interpret(ast)
            }

            is DeclarationAssignationAST -> {
                DeclarationInterpreter(mutableSymbolTable, immutableSymbolTable).interpret(ast.getDeclarationAST())
                AssignationInterpreter(mutableSymbolTable, immutableSymbolTable).interpret(ast.getAssignationAST())
            }

            is AssignationAST -> {
                AssignationInterpreter(mutableSymbolTable, immutableSymbolTable).interpret(ast)
            }

            is FunctionAST -> {
                FunctionInterpreter(mutableSymbolTable, immutableSymbolTable).interpret(ast)
            }

            else -> {
                throw Exception("Invalid AST")
            }
        }
    }
    fun getSymbolTable(): Map<String, Pair<String, String?>> {
        return this.mutableSymbolTable
    }
}
