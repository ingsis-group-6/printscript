package interpreter.implementation

import common.ast.implementations.asts.AST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.FunctionAST
import common.ast.implementations.asts.EndOfFileAST
import common.ast.implementations.asts.EmptyAST
import common.ast.implementations.asts.ConditionalAST
import common.ast.implementations.asts.BlockAST
import interpreter.Scope
import interpreter.input.ConsoleInputter
import interpreter.input.Inputter
import interpreter.interfaces.Interpreter
import interpreter.output.ConsolePrintOutputter
import interpreter.output.Outputter

class Interpreter(
    private val scope: Scope,
    private val inputter: Inputter,
    private val outputter: Outputter,
    private val isEOF: BooleanWrapper
) : Interpreter<AST> {

    constructor(scope: Scope) : this(scope, ConsoleInputter(), ConsolePrintOutputter(), BooleanWrapper(false))

    override fun interpret(ast: AST) {
        return when (ast) {
            is DeclarationAST -> {
                DeclarationInterpreter(scope).interpret(ast)
            }

            is DeclarationAssignationAST -> {
                DeclarationInterpreter(scope).interpret(ast.getDeclarationAST() as DeclarationAST)
                AssignationInterpreter(scope, inputter, outputter).interpret(ast.getAssignationAST() as AssignationAST)
            }

            is AssignationAST -> {
                AssignationInterpreter(scope, inputter, outputter).interpret(ast)
            }

            is FunctionAST -> {
                FunctionInterpreter(scope, outputter).interpret(ast)
            }

            is EndOfFileAST -> {
                EOFInterpreter(isEOF).interpret(ast)
            }
            is ConditionalAST -> {
                val scope = Scope(
                    mutableMapOf(),
                    mutableMapOf(),
                    scope
                )
                ConditionalInterpreter(scope, inputter, outputter, isEOF).interpret(ast)
            }

            is BlockAST -> TODO()
            EmptyAST -> TODO()
        }
    }
    fun getSymbolTable(): Map<String, Pair<String, String?>> {
        return scope.getAllVariables()
    }
}
