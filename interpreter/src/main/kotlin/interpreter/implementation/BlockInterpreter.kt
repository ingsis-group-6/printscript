package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.BlockAST
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope
import interpreter.output.Outputter

class BlockInterpreter(private val scope: Scope, private val outputter: Outputter, private val booleanWrapper: BooleanWrapper) : Interpreter {
    override fun interpret(ast: AST) {
        ast as BlockAST
        val interpreter = Interpreter(
            interpreter.Scope(mutableMapOf(), mutableMapOf(), scope),
            outputter,
            booleanWrapper
        )
        val astList = ast.getContainedASTs()
        astList.forEach { interpreter.interpret(it) }
    }
}
