package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.ConditionalAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.asts.EndOfFileAST
import common.ast.implementations.asts.FunctionAST
import interpreter.Scope
import interpreter.interfaces.Interpreter
import interpreter.output.ConsolePrintOutputter
import interpreter.output.Outputter

class Interpreter(
    private val scope: Scope,
    private val outputter: Outputter,
    private val isEOF: BooleanWrapper
) : Interpreter {

    constructor(scope: Scope) : this(scope, ConsolePrintOutputter(), BooleanWrapper(false))

    override fun interpret(ast: AST) {
        return when (ast) {
            is DeclarationAST -> {
                DeclarationInterpreter(scope).interpret(ast)
            }

            is DeclarationAssignationAST -> {
                DeclarationInterpreter(scope).interpret(ast.getDeclarationAST())
                AssignationInterpreter(scope).interpret(ast.getAssignationAST())
            }

            is AssignationAST -> {
                AssignationInterpreter(scope).interpret(ast)
            }

            is FunctionAST -> {
                FunctionInterpreter(scope, outputter).interpret(ast)
            }

            is EndOfFileAST -> {
                EOFInterpreter(isEOF).interpret(ast)
            }
            is ConditionalAST -> {
//                val scope = interpreter.Scope(mutableSymbolTable, immutableSymbolTable)
//                ConditionalInterpreter(startScope).interpret(ast)
                val scope = interpreter.Scope(
                    mutableMapOf(),
                    mutableMapOf(),
                    scope
                )
                ConditionalInterpreter(scope).interpret(ast)
            }

            else -> {
                throw Exception("Invalid AST")
            }
        }
    }
    fun getSymbolTable(): Map<String, Pair<String, String?>> {
        return scope.getAllVariables()
    }
}
