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
                DeclarationInterpreter(symbolTable).interpret(ast)
            }

            is DeclarationAssignationAST -> {
                DeclarationInterpreter(symbolTable).interpret(ast)
                AssignationInterpreter(symbolTable).interpret(ast)
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
